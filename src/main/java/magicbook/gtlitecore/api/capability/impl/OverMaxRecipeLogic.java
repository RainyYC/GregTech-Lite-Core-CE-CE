package magicbook.gtlitecore.api.capability.impl;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import gregtech.api.util.GTUtility;

public class OverMaxRecipeLogic extends MultiblockRecipeLogic {
    public OverMaxRecipeLogic(RecipeMapMultiblockController tileEntity) {
        super(tileEntity);
    }

    public OverMaxRecipeLogic(RecipeMapMultiblockController tileEntity, boolean hasPerfectOC) {
        super(tileEntity, hasPerfectOC);
    }

    @Override
    public boolean isAllowOverclocking() {
        return false;
    }

    long recipeEUt = 0;

    @Override
    protected void setupRecipe(Recipe recipe) {
        this.progressTime = 1;

        this.recipeEUt = overclockResults[0];
        double worktime = overclockResults[1];

        long maxEUt = Math.max(getEnergyContainer().getInputVoltage(), getEnergyContainer().getOutputVoltage());
        while(true) {
            long nextEUt = (long) (this.recipeEUt * getOverclockingVoltageMultiplier());
            if (nextEUt > maxEUt) break;
            this.recipeEUt = nextEUt;
            worktime /= getOverclockingDurationDivisor();
            if(worktime < 1) break;
        }
        setMaxProgress(Math.max(1, (int) worktime));

        int recipeTier = GTUtility.getTierByVoltage(recipe.getEUt());
        int machineTier = getOverclockForTier(getMaximumOverclockVoltage());
        this.fluidOutputs = GTUtility
                .copyFluidList(recipe.getResultFluidOutputs(recipeTier, machineTier, getRecipeMap()));
        this.itemOutputs = GTUtility
                .copyStackList(recipe.getResultItemOutputs(recipeTier, machineTier, getRecipeMap()));

        if (this.wasActiveAndNeedsUpdate) {
            this.wasActiveAndNeedsUpdate = false;
        } else {
            this.setActive(true);
        }
    }

    protected boolean drawEnergy(long recipeEUt, boolean simulate) {
        long resultEnergy = getEnergyStored() - recipeEUt;
        if (resultEnergy >= 0L && resultEnergy <= getEnergyCapacity()) {
            if (!simulate) getEnergyContainer().changeEnergy(-recipeEUt);
            return true;
        } else return false;
    }

    @Override
    protected void updateRecipeProgress() {
        if (canRecipeProgress && drawEnergy(recipeEUt, true)) {
            drawEnergy(recipeEUt, false);
            // as recipe starts with progress on 1 this has to be > only not => to compensate for it
            if (++progressTime > maxProgressTime) {
                completeRecipe();
            }
            if (this.hasNotEnoughEnergy && getEnergyInputPerSecond() > 19L * recipeEUt) {
                this.hasNotEnoughEnergy = false;
            }
        } else if (recipeEUt > 0) {
            this.hasNotEnoughEnergy = true;
            decreaseProgress();
        }
    }

    public long getLInfoProviderEUt() {
        return recipeEUt;
    }
}
