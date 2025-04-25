package magicbook.gtlitecore.api.capability.impl;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.IRotorHolder;
import gregtech.api.metatileentity.IVoidable;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.metatileentity.multiblock.ParallelLogicType;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.FluidKey;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.logic.ParallelLogic;
import gregtech.api.util.GTHashMaps;
import gregtech.api.util.TextFormattingUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvancedFuelRecipeLogic extends AdvancedRecipeLogic {
    protected long totalContinuousRunningTime;

    public AdvancedFuelRecipeLogic(RecipeMapMultiblockController tileEntity) {
        super(tileEntity);
    }

    @Override
    public ParallelLogicType getParallelLogicType() {
        return ParallelLogicType.MULTIPLY; // TODO APPEND_FLUIDS
    }

    @Override
    protected boolean hasEnoughPower(int[] resultOverclock) {
        // generators always have enough power to run recipes
        return true;
    }

    @Override
    public void update() {
        super.update();
        if (workingEnabled && isActive && progressTime > 0) {
            totalContinuousRunningTime++;
        } else {
            totalContinuousRunningTime = 0;
        }
    }

    @Override
    public int getParallelLimit() {
        // parallel is limited by voltage
        return Integer.MAX_VALUE;
    }

    @Override
    protected long getMaxParallelVoltage() {
        return getMaxVoltage();
    }

    /**
     * Boost the energy production.
     * Should not change the state of the workable logic. Only read current values.
     *
     * @param production the energy amount to boost
     * @return the boosted energy amount
     */
    protected long boostProduction(long production) {
        return production;
    }

    @Override
    protected boolean drawEnergy(long recipeEUt, boolean simulate) {
        long euToDraw = boostProduction(recipeEUt);
        long resultEnergy = getEnergyStored() + euToDraw;
        if (resultEnergy >= 0L && resultEnergy <= getEnergyCapacity()) {
            if (!simulate) getEnergyContainer().changeEnergy(euToDraw);
            return true;
        } else return false;
    }

    @Override
    public long getLInfoProviderEUt() {
        return boostProduction(super.getLInfoProviderEUt());
    }

    @Override
    public boolean consumesEnergy() {
        return false;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        totalContinuousRunningTime = 0;
    }

    public String getRecipeFluidInputInfo() {
        IRotorHolder rotorHolder = null;

        if (metaTileEntity instanceof MultiblockWithDisplayBase multiblockWithDisplayBase) {
            List<IRotorHolder> abilities = multiblockWithDisplayBase.getAbilities(MultiblockAbility.ROTOR_HOLDER);
            rotorHolder = abilities.size() > 0 ? abilities.get(0) : null;
        }

        // Previous Recipe is always null on first world load, so try to acquire a new recipe
        Recipe recipe;
        if (previousRecipe == null) {
            recipe = findRecipe(Integer.MAX_VALUE, getInputInventory(), getInputTank());
            if (recipe == null) return null;
        } else {
            recipe = previousRecipe;
        }
        FluidStack requiredFluidInput = recipe.getFluidInputs().get(0).getInputFluidStack();

        int ocAmount = (int) (getMaxVoltage() / recipe.getEUt());
        int neededAmount = ocAmount * requiredFluidInput.amount;
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            neededAmount /= (rotorHolder.getTotalEfficiency() / 100f);
        } else if (rotorHolder != null && !rotorHolder.hasRotor()) {
            return null;
        }
        return TextFormatting.RED + TextFormattingUtil.formatNumbers(neededAmount) + "L";
    }

    public FluidStack getInputFluidStack() {
        // Previous Recipe is always null on first world load, so try to acquire a new recipe
        if (previousRecipe == null) {
            Recipe recipe = findRecipe(Integer.MAX_VALUE, getInputInventory(), getInputTank());

            return recipe == null ? null : getInputTank().drain(
                    new FluidStack(recipe.getFluidInputs().get(0).getInputFluidStack().getFluid(), Integer.MAX_VALUE),
                    false);
        }
        FluidStack fuelStack = previousRecipe.getFluidInputs().get(0).getInputFluidStack();
        return getInputTank().drain(new FluidStack(fuelStack.getFluid(), Integer.MAX_VALUE), false);
    }

    @Override
    public List<OCParameter> getOCList() {
        return new ArrayList<>();
    }

    @Override
    public boolean prepareRecipe(Recipe recipe, IItemHandlerModifiable inputInventory,
                                 IMultipleTankHandler inputFluidInventory) {
        recipe = Recipe.trimRecipeOutputs(recipe, getRecipeMap(), metaTileEntity.getItemOutputLimit(),
                metaTileEntity.getFluidOutputLimit());

        Pair<Recipe, Long> parallel = findParallel(recipe, inputInventory, inputFluidInventory, getOutputInventory(), getOutputTank(), getParallelLimit());

        recipe = parallel.getLeft();

        if (recipe != null && setupAndConsumeRecipeInputs(recipe, inputInventory, inputFluidInventory)) {
            LSetupRecipe(recipe, parallel.getRight(), recipe.getDuration());
            return true;
        }
        return false;
    }

    protected Pair<Recipe, Long> findParallel(Recipe currentRecipe, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, IItemHandlerModifiable outputs, IMultipleTankHandler fluidOutputs, int parallelLimit) {
        if (getRecipeMap() != null) {
            int parallelAmount = doParallel(currentRecipe, getRecipeMap(), inputs, fluidInputs, outputs, fluidOutputs, parallelLimit, Long.MAX_VALUE, getMetaTileEntity());

            // if the builder returned is null, no recipe was found.
            if (parallelAmount == 0) {
                invalidateInputs();
                return Pair.of(null, 0L);
            } else {
                Recipe tmp = getRecipeMap().recipeBuilder().append(currentRecipe, 1, false).EUt(1).build().getResult();
                return Pair.of(getRecipeMap().recipeBuilder().append(tmp, parallelAmount, false).build().getResult(),
                        (long) parallelAmount * currentRecipe.getEUt());
            }
        }
        return Pair.of(currentRecipe, (long) currentRecipe.getEUt());
    }

    protected int doParallel(Recipe currentRecipe, RecipeMap<?> recipeMap,
                             IItemHandlerModifiable importInventory, IMultipleTankHandler importFluids,
                             IItemHandlerModifiable exportInventory, IMultipleTankHandler exportFluids,
                             int parallelAmount, long maxVoltage, IVoidable voidable) {
        int multiplierByInputs = Math.min(parallelAmount, getMaxRecipeMultiplier(currentRecipe, importInventory, importFluids));
        if (multiplierByInputs == 0) {
            return 0;
        }
        boolean voidItems = voidable.canVoidRecipeItemOutputs();
        boolean voidFluids = voidable.canVoidRecipeFluidOutputs();

        // Simulate the merging of the maximum amount of recipes that can be run with these items
        // and limit by the amount we can successfully merge
        int limitByOutput;
        limitByOutput = ParallelLogic.limitByOutputMerging(currentRecipe, exportInventory, exportFluids,
                multiplierByInputs, voidItems, voidFluids);

        multiplierByInputs = Math.min(multiplierByInputs, limitByOutput);

        long recipeEUt = currentRecipe.getEUt();

        if (recipeEUt != 0) {
            int parallelizable = (int) Math.min(Integer.MAX_VALUE, Math.abs(maxVoltage / recipeEUt));
            if (parallelizable != 0)
                return Math.min(parallelizable, multiplierByInputs);
        }
        return 1;
    }

    private int getMaxRecipeMultiplier(Recipe recipe, IItemHandlerModifiable importInventory, IMultipleTankHandler importFluids) {
        Object2IntMap<ItemStack> ingredientStacks = GTHashMaps.fromItemHandler(importInventory);
        Map<FluidKey, Integer> fluidStacks = GTHashMaps.fromFluidHandler(importFluids);

        int itemMultiplier = getMaxRatioItem(ingredientStacks, recipe);
        // Find the maximum number of recipes that can be performed from the fluids in the fluid input inventories
        int fluidMultiplier = getMaxRatioFluid(fluidStacks, recipe);

        if (itemMultiplier == Integer.MAX_VALUE && fluidMultiplier == Integer.MAX_VALUE) {
            return 0;
        }

        // Find the maximum number of recipes that can be performed from all available inputs
        return Math.min(itemMultiplier, fluidMultiplier);
    }
}
