package magicbook.gtlitecore.common.metatileentities.multi.electric.adv;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiMapMultiblockController;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockComputerCasing;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import magicbook.gtlitecore.api.GTLiteAPI;
import magicbook.gtlitecore.api.block.impl.WrappedIntTier;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
import magicbook.gtlitecore.api.pattern.GTLiteTraceabilityPredicate;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.utils.GTLiteUtils;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.blocks.BlockScienceCasing;
import magicbook.gtlitecore.common.blocks.GTLiteMetaBlocks;
import magicbook.gtlitecore.common.metatileentities.GTLiteMetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static gregtech.api.GTValues.UHV;
import static gregtech.api.GTValues.UV;

public class MetaTileEntityIonLithographyFactory extends MultiMapMultiblockController {

    public int casingTier;

    public MetaTileEntityIonLithographyFactory(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.LASER_ENGRAVER_RECIPES,
                GTLiteRecipeMaps.NANO_SCALE_MASK_ALIGNER_RECIPES,
                GTLiteRecipeMaps.ION_IMPLANTATOR_RECIPES,
                RecipeMaps.ELECTROLYZER_RECIPES,
                RecipeMaps.POLARIZER_RECIPES,
                RecipeMaps.ELECTROMAGNETIC_SEPARATOR_RECIPES});
        this.recipeMapWorkable = new IonLithographyFactoryRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityIonLithographyFactory(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object type = context.get("FieldCasingTieredStats");
        this.casingTier = GTLiteUtils.getOrDefault(() -> type instanceof WrappedIntTier,
                () -> ((WrappedIntTier) type).getIntTier(), 0);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.casingTier = 0;
    }

    @Override
    protected void initializeAbilities() {
        this.inputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.inputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.outputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        List<IEnergyContainer> energyContainer = new ArrayList<>(this.getAbilities(MultiblockAbility.INPUT_ENERGY));
        energyContainer.addAll(this.getAbilities(MultiblockAbility.INPUT_LASER));
        this.energyContainer=new EnergyContainerList(energyContainer);
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle(" EDXXXDE ", "EOXCCCXOE", "EOXCMCXOE", "EOXCCCXOE", " EDXXXDE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EFYYYFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFYYYFE ")
                .aisle(" EDXXXDE ", "EOXCCCXOE", "EOXCSCXOE", "EOXCCCXOE", " EDXXXDE ")
                .where('S', this.selfPredicate())
                .where('C', states(getCasingState())
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMaxGlobalLimited(3))
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1)))
                .where('X', states(getSecondCasingState()))
                .where('Y', states(getCasingState())
                        .setMinGlobalLimited(16)
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)
                                .setMaxGlobalLimited(8)))
                .where('E', states(getThirdCasingState()))
                .where('F', GTLiteTraceabilityPredicate.FIELD_CASING.get())
                .where('D', states(getFourthCasingState()))
                .where('O', states(getCoilState()))
                .where('o', states(getSecondCoilState()))
                .where('P', states(getFifthCasingState()))
                .where('M', abilities(MultiblockAbility.MUFFLER_HATCH))
                .build();
    }

    private static IBlockState getCasingState() {
        return MetaBlocks.COMPUTER_CASING.getState(BlockComputerCasing.CasingType.HIGH_POWER_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.ADVANCED_HIGH_ENERGY_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HIGH_ENERGY_CASING);
    }

    private static IBlockState getFourthCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.DIMENSIONAL_BRIDGE_CASING);
    }

    private static IBlockState getFifthCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HOLLOW_CASING);
    }

    private static IBlockState getCoilState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.MOLECULAR_COIL);
    }

    private static IBlockState getSecondCoilState() {
        return MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.SUPERCONDUCTOR_COIL);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.HIGH_POWER_CASING;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return GTLiteTextures.DECAY_GENERATOR_OVERLAY;
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @Override
    protected boolean shouldShowVoidingModeButton() {
        return true;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle(" EDXXXDE ", "EOXCCCXOE", "EOXCMCXOE", "EOXCCCXOE", " EDXXXDE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EFiYfFE ", "EOXoooXOE", "EOXoPoXOE", "EOXoooXOE", " EFjYgFE ")
                .aisle(" EDXXXDE ", "EOXCcCXOE", "EOXCSCXOE", "EOXCeCXOE", " EDXXXDE ")
                .where('S', GTLiteMetaTileEntities.ION_LITHOGRAPHY_FACTORY, EnumFacing.SOUTH)
                .where('C', getCasingState())
                .where('c', MetaTileEntities.AUTO_MAINTENANCE_HATCH, EnumFacing.SOUTH)
                .where('e', MetaTileEntities.LASER_INPUT_HATCH_256[4], EnumFacing.SOUTH)
                .where('X', getSecondCasingState())
                .where('Y', getCasingState())
                .where('i', MetaTileEntities.ITEM_IMPORT_BUS[UHV], EnumFacing.DOWN)
                .where('f', MetaTileEntities.FLUID_IMPORT_HATCH[UHV], EnumFacing.DOWN)
                .where('j', MetaTileEntities.ITEM_EXPORT_BUS[UHV], EnumFacing.UP)
                .where('g', MetaTileEntities.FLUID_EXPORT_HATCH[UHV], EnumFacing.UP)
                .where('E', getThirdCasingState())
                .where('D', getFourthCasingState())
                .where('O', getCoilState())
                .where('o', getSecondCoilState())
                .where('P', getFifthCasingState())
                .where('M', MetaTileEntities.MUFFLER_HATCH[UV], EnumFacing.NORTH);
        GTLiteAPI.MAP_FIELD_CASING.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> ((WrappedIntTier) entry.getValue()).getIntTier()))
                .forEach(entry -> shapeInfo.add(builder.where('F', entry.getKey()).build()));
        return shapeInfo;
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @Nonnull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.7"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.8"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.9"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.10"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.11"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.12"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.13"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.14"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.15"));
        tooltip.add(I18n.format("gtlitecore.machine.ion_lithography_factory.tooltip.16"));
        tooltip.add(I18n.format("gtlitecore.universal.tooltip.laser_input"));
    }

    public class IonLithographyFactoryRecipeLogic extends AdvancedRecipeLogic {

        public IonLithographyFactoryRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        /**
         * @return Check if machine in Laser Engraver mode.
         */
        private boolean isLaserEngraverMode() {
            return this.getRecipeMap() == RecipeMaps.LASER_ENGRAVER_RECIPES;
        }

        /**
         * @return Check if machine in Nano-scale Mask Aligner mode.
         */
        private boolean isNanoScaleMaskAlignerMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.NANO_SCALE_MASK_ALIGNER_RECIPES;
        }

        /**
         * @return Check if machine in Ion Implantator mode.
         */
        private boolean isIonImplantatorMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.ION_IMPLANTATOR_RECIPES;
        }

        /**
         * @return Check if machine in Electrolyzer mode.
         */
        private boolean isElectrolyzerMode() {
            return this.getRecipeMap() == RecipeMaps.ELECTROLYZER_RECIPES;
        }

        /**
         * @return Check if machine in Polarizer mode.
         */
        private boolean isPolarizerMode() {
            return this.getRecipeMap() == RecipeMaps.POLARIZER_RECIPES;
        }

        /**
         * @return Check if machine in Electromagnetic Separator mode.
         */
        private boolean isElectromagneticSeparatorMode() {
            return this.getRecipeMap() == RecipeMaps.ELECTROMAGNETIC_SEPARATOR_RECIPES;
        }

        /**
         * @param casingTier Field Casing Tier.
         * @return Max Parallel of this machine: tier * (casingTier * 64), where tier means voltage tier.
         */
        public int getMaxParallel(int casingTier) {
            int tier = GTUtility.getTierByVoltage(getMaxVoltage());
            return tier * (casingTier * 64);
        }

        /**
         * @return Get (tier * (casing * 64)) parallel, and specially, different modes has different parallel limits.
         */
        @Override
        public int getParallelLimit() {
            if (isLaserEngraverMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else if (isNanoScaleMaskAlignerMode()) {
                return Math.min(getMaxParallel(casingTier), 1024);
            } else if (isIonImplantatorMode()) {
                return Math.min(getMaxParallel(casingTier), 2048);
            } else if (isElectrolyzerMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else if (isPolarizerMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else {
                return Math.min(getMaxParallel(casingTier), 4096);
            }
        }

        /**
         * @param maxProgress In some special mode, get 1/2^{casingTier} progress time.
         */
        @Override
        public void setMaxProgress(int maxProgress) {
            int MaxProgress = (int) Math.floor(maxProgress * Math.pow(0.5, casingTier));
            if (isLaserEngraverMode()) {
                super.setMaxProgress(MaxProgress);
            } else if (isElectrolyzerMode()) {
                super.setMaxProgress(MaxProgress);
            } else if (isPolarizerMode()) {
                super.setMaxProgress(MaxProgress);
            } else if (isElectromagneticSeparatorMode()) {
                super.setMaxProgress(MaxProgress);
            } else {
                super.setMaxProgress(maxProgress);
            }
        }
    }
}
