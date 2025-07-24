package magicbook.gtlitecore.common.metatileentities.multi.electric.adv;

import com.cleanroommc.modularui.utils.FluidTankHandler;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.*;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.util.GTTransferUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.RelativeDirection;
import gregtech.api.util.TextComponentUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import gregtech.core.sound.GTSoundEvents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import magicbook.gtlitecore.api.utils.GTLiteLog;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.blocks.BlockBoilerCasing;
import magicbook.gtlitecore.common.blocks.BlockMetalCasing;
import magicbook.gtlitecore.common.blocks.GTLiteMetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static gregtech.api.GTValues.MAX;
import static gregtech.api.GTValues.UV;
import static gregtech.api.GTValues.V;

public class MetaTileEntityDangoteDistillery extends MultiMapMultiblockController {

    @Getter
    protected int layerCount;
    protected List<IFluidHandler> orderedFluidOutputs;

    public MetaTileEntityDangoteDistillery(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.DISTILLERY_RECIPES,
                RecipeMaps.DISTILLATION_RECIPES
        });
        this.recipeMapWorkable = new DangoteDistilleryRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDangoteDistillery(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        if(this.structurePattern != null) {
            this.layerCount = determineLayerCount(this.structurePattern);
            this.orderedFluidOutputs = determineOrderedFluidOutputs();
        }
    }

    protected boolean usesAdvancedHatchLogic() {
        return this.getCurrentRecipeMap() == RecipeMaps.DISTILLATION_RECIPES;
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.layerCount = 0;
        this.orderedFluidOutputs = null;
    }

    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.FRONT, RelativeDirection.UP)
                .aisle("YSY", "YYY", "YYY")
                .aisle("XXX", "XPX", "XXX")
                .setRepeatable(1, 11)
                .aisle("XXX", "XXX", "XXX")
                .where('S', this.selfPredicate())
                .where('Y', states(getCasingState())
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMinGlobalLimited(1)
                                .setMaxGlobalLimited(3))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setExactLimit(1)))
                .where('X', states(getCasingState())
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)
                                .setMinLayerLimited(1) // TODO parallel logic doesn't support hatch omission without
                                // global voiding enabled
                                .setMaxLayerLimited(1, 1))
                        .or(this.autoAbilities(true, false)))
                .where('P', states(getBoilerCasingState()))
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.HG1223_CASING);
    }

    private static IBlockState getBoilerCasingState() {
        return GTLiteMetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.POLYBENZIMIDAZOLE);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        if (isStructureFormed()) {
            FluidStack stackInTank = importFluids.drain(Integer.MAX_VALUE, false);
            if (stackInTank != null && stackInTank.amount > 0) {
                ITextComponent fluidName = TextComponentUtil.setColor(GTUtility.getFluidTranslation(stackInTank),
                        TextFormatting.AQUA);
                textList.add(TextComponentUtil.translationWithColor(
                        TextFormatting.GRAY,
                        "gregtech.multiblock.distillation_tower.distilling_fluid",
                        fluidName));
            }
        }
        super.addDisplayText(textList);
    }

    @Override
    protected boolean allowSameFluidFillForOutputs() {
        return !usesAdvancedHatchLogic();
    }

    @Override
    public int getFluidOutputLimit() {
        return this.getLayerCount();
    }

    @Override
    protected Function<BlockPos, Integer> multiblockPartSorter() {
        return RelativeDirection.UP.getSorter(this.getFrontFacing(), this.getUpwardsFacing(), this.isFlipped());
    }

    /**
     * Whether this multi can be rotated or face upwards. <br>
     * There will be <i>consequences</i> if this returns true. Go override {@link #determineOrderedFluidOutputs()}
     */
    @Override
    public boolean allowsExtendedFacing() {
        return false;
    }

    @Override
    public SoundEvent getBreakdownSound() {
        return GTSoundEvents.BREAKDOWN_ELECTRICAL;
    }

    /**
     * Needs to be overriden for multiblocks that have different assemblies than the standard distillation tower.
     *
     * @param structurePattern the structure pattern
     * @return the number of layers that <b>could</b> hold output hatches
     */
    protected int determineLayerCount(@NotNull BlockPattern structurePattern) {
        return structurePattern.formedRepetitionCount[1] + 1;
    }

    /**
     * Needs to be overriden for multiblocks that have different assemblies than the standard distillation tower.
     *
     * @return the fluid hatches of the multiblock, in order, with null entries for layers that do not have hatches.
     */
    protected List<IFluidHandler> determineOrderedFluidOutputs() {
        List<MetaTileEntityMultiblockPart> fluidExportParts = this.getMultiblockParts().stream()
                .filter(iMultiblockPart -> iMultiblockPart instanceof IMultiblockAbilityPart<?> abilityPart &&
                        abilityPart.getAbility() == MultiblockAbility.EXPORT_FLUIDS &&
                        abilityPart instanceof MetaTileEntityMultiblockPart)
                .map(iMultiblockPart -> (MetaTileEntityMultiblockPart) iMultiblockPart)
                .collect(Collectors.toList());
        // the fluidExportParts should come sorted in smallest Y first, largest Y last.
        List<IFluidHandler> orderedHandlerList = new ObjectArrayList<>();
        int firstY = this.getPos().getY() + 1;
        int exportIndex = 0;
        for (int y = firstY; y < firstY + this.layerCount; y++) {
            if (fluidExportParts.size() <= exportIndex) {
                orderedHandlerList.add(null);
                continue;
            }
            MetaTileEntityMultiblockPart part = fluidExportParts.get(exportIndex);
            if (part.getPos().getY() == y) {
                List<IFluidTank> hatchTanks = new ObjectArrayList<>();
                // noinspection unchecked
                ((IMultiblockAbilityPart<IFluidTank>) part).registerAbilities(hatchTanks);
                if (hatchTanks.size() == 1)
                    orderedHandlerList.add(FluidTankHandler.getTankFluidHandler(hatchTanks.get(0)));
                else orderedHandlerList.add(new FluidTankList(false, hatchTanks));
                exportIndex++;
            } else if (part.getPos().getY() > y) {
                orderedHandlerList.add(null);
            } else {
                GTLiteLog.logger.error("The Distillation Tower at {} had a fluid export hatch with an unexpected Y position.", this.getPos());
                this.invalidateStructure();
                return new ObjectArrayList<>();
            }
        }
        return orderedHandlerList;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GTLiteTextures.HG_1223_CASING;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.DISTILLATION_TOWER_OVERLAY;
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @NotNull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.dangote_distillery.tooltip.7"));
    }

    protected class DangoteDistilleryRecipeLogic extends MultiblockRecipeLogic {

        public DangoteDistilleryRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        protected boolean applyFluidToOutputs(@NotNull List<FluidStack> fluids, boolean doFill) {
            boolean valid = true;
            for (int i = 0; i < fluids.size(); i++) {
                IFluidHandler handler = orderedFluidOutputs.get(i);
                // void if no hatch is found on that fluid's layer
                // this is considered trimming and thus ignores canVoid
                if (handler == null)
                    continue;
                int accepted = handler.fill(fluids.get(i), doFill);
                if (accepted != fluids.get(i).amount)
                    valid = false;
                if (!doFill && !valid)
                    break;
            }
            return valid;
        }

        @Override
        protected void outputRecipeOutputs() {
            if (usesAdvancedHatchLogic()) {
                GTTransferUtils.addItemsToItemHandler(this.getOutputInventory(), false, itemOutputs);
                this.applyFluidToOutputs(fluidOutputs, true);
            } else {
                super.outputRecipeOutputs();
            }
        }

        @Override
        protected boolean setupAndConsumeRecipeInputs(@NotNull Recipe recipe,
                                                      @NotNull IItemHandlerModifiable importInventory,
                                                      @NotNull IMultipleTankHandler importFluids) {
            if (!usesAdvancedHatchLogic()) {
                return super.setupAndConsumeRecipeInputs(recipe, importInventory, importFluids);
            }

            this.overclockResults = this.calculateOverclock(recipe);

            this.modifyOverclockPost(overclockResults, recipe.getRecipePropertyStorage());

            if (!this.hasEnoughPower(overclockResults)) {
                return false;
            }

            IItemHandlerModifiable exportInventory = this.getOutputInventory();

            // We have already trimmed outputs and chanced outputs at this time
            // Attempt to merge all outputs + chanced outputs into the output bus, to prevent voiding chanced outputs
            if (!metaTileEntity.canVoidRecipeItemOutputs() &&
                    !GTTransferUtils.addItemsToItemHandler(exportInventory, true, recipe.getAllItemOutputs())) {
                this.isOutputsFull = true;
                return false;
            }

            // Perform layerwise fluid checks
            if (!metaTileEntity.canVoidRecipeFluidOutputs() &&
                    !this.applyFluidToOutputs(recipe.getAllFluidOutputs(), false)) {
                this.isOutputsFull = true;
                return false;
            }

            this.isOutputsFull = false;
            if (recipe.matches(true, importInventory, importFluids)) {
                this.metaTileEntity.addNotifiedInput(importInventory);
                return true;
            }
            return false;
        }

        /**
         * @return Check if machine in Distillery mode.
         */
        private boolean isDistilleryMode() {
            return this.getRecipeMap() == RecipeMaps.DISTILLERY_RECIPES;
        }

        /**
         * @return Check if machine in Distillation mode.
         */
        private boolean isDistillationMode() {
            return this.getRecipeMap() == RecipeMaps.DISTILLATION_RECIPES;
        }

        /**
         * @param maxProgress If machine in distillery mode, then get 1/4 progress time,
         *                    If machine in distillation mode, then get 1/2 progress time.
         */
        @Override
        public void setMaxProgress(int maxProgress) {
            if (isDistilleryMode()) {
                this.maxProgressTime = maxProgress / 4;
            } else if (isDistillationMode()) {
                this.maxProgressTime = maxProgress / 2;
            } else {
                this.maxProgressTime = maxProgress;
            }
        }

        /**
         * @return If machine's voltage less than or equal UV, then use ParallelTier() to get parallel.
         *         If machine's voltage greater than UV, then use HigherParallelTier() to get parallel.
         *         Max Parallel: 720 (MAX voltage).
         */
        @Override
        public int getParallelLimit() {
            if (this.getMaxVoltage() > V[MAX]) { //  For MAX+, get (12 * 15 * 4) = 720.
                return 720;
            }
            int tier = GTUtility.getTierByVoltage(getMaxVoltage());
            if (tier == 0) {
                return 1;
            }
            if (tier <= UV) {
                return 4 * (tier * 4);
            } else {
                return 12 * (tier * 4);
            }
        }
    }
}