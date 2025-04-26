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
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockComputerCasing;
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
import net.minecraft.init.Blocks;
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

public class MetaTileEntityDimensionalMixer extends MultiMapMultiblockController {

    public int casingTier;

    public MetaTileEntityDimensionalMixer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.MIXER_RECIPES,
                GTLiteRecipeMaps.TURBINE_MIXER_RECIPES,
                RecipeMaps.CENTRIFUGE_RECIPES,
                RecipeMaps.THERMAL_CENTRIFUGE_RECIPES,
                RecipeMaps.SIFTER_RECIPES,
                GTLiteRecipeMaps.SONICATION_RECIPES
        });
        this.recipeMapWorkable = new DimensionalMixerRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDimensionalMixer(metaTileEntityId);
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
                .aisle(" CAC ", " ABA ", " ABA ", " ABA ", " ABA ", " ABA ", " CAC ")
                .aisle("CBBBC", "A###A", "A###A", "A###A", "A###A", "A###A", "CBBBC")
                .aisle("ABBBA", "B#F#B", "B#F#B", "B#F#B", "B#F#B", "B#F#B", "ABBBA")
                .aisle("CBBBC", "A###A", "A###A", "A###A", "A###A", "A###A", "CBBBC")
                .aisle(" CAC ", " ABA ", " ABA ", " ASA ", " ABA ", " ABA ", " CAC ")
                .where('S', this.selfPredicate())
                .where('A', states(getCasingState()))
                .where('B', states(getSecondCasingState())
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMaxGlobalLimited(3))
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)
                                .setMaxGlobalLimited(8)))
                .where('C', states(getThirdCasingState()))
                .where('F', GTLiteTraceabilityPredicate.FIELD_CASING.get())
                .where('#', air())
                .where(' ', any())
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.ULTIMATE_HIGH_ENERGY_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return MetaBlocks.COMPUTER_CASING.getState(BlockComputerCasing.CasingType.HIGH_POWER_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.DIMENSIONAL_BRIDGE_CASING);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return iMultiblockPart == null ? GTLiteTextures.ULTIMATE_HIGH_ENERGY_CASING : Textures.HIGH_POWER_CASING;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return GTLiteTextures.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_OVERLAY;
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
                .aisle(" CAC ", " AMA ", " AXA ", " ABA ", " ABA ", " ABA ", " CAC ")
                .aisle("CBBBC", "A###A", "A###A", "A###A", "A###A", "A###A", "CBBBC")
                .aisle("ABBBA", "B#F#B", "B#F#B", "B#F#B", "B#F#B", "B#F#B", "ABBBA")
                .aisle("CBBBC", "A###A", "A###A", "A###A", "A###A", "A###A", "CBBBC")
                .aisle(" CAC ", " ALA ", " AKA ", " ASA ", " AJA ", " AIA ", " CAC ")
                .where('S', GTLiteMetaTileEntities.DIMENSIONAL_MIXER, EnumFacing.SOUTH)
                .where('A', getCasingState())
                .where('B', getSecondCasingState())
                .where('I', MetaTileEntities.ITEM_IMPORT_BUS[UHV], EnumFacing.SOUTH)
                .where('J', MetaTileEntities.ITEM_EXPORT_BUS[UHV], EnumFacing.SOUTH)
                .where('K', MetaTileEntities.FLUID_IMPORT_HATCH[UHV], EnumFacing.SOUTH)
                .where('L', MetaTileEntities.FLUID_EXPORT_HATCH[UHV], EnumFacing.SOUTH)
                .where('M', MetaTileEntities.AUTO_MAINTENANCE_HATCH, EnumFacing.NORTH)
                .where('X', MetaTileEntities.LASER_INPUT_HATCH_256[4], EnumFacing.NORTH)
                .where('C', getThirdCasingState())
                .where('#', Blocks.AIR.getDefaultState());
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
        tooltip.add(TooltipHelper.BLINKING_RED + I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.7"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.8"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.9"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.10"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.11"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.12"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.13"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.14"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.15"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensional_mixer.tooltip.16"));
        tooltip.add(I18n.format("gtlitecore.universal.tooltip.laser_input"));
    }

    public class DimensionalMixerRecipeLogic extends AdvancedRecipeLogic {

        public DimensionalMixerRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        /**
         * @return Check if machine in Mixer mode.
         */
        private boolean isMixerMode() {
            return this.getRecipeMap() == RecipeMaps.MIXER_RECIPES;
        }

        /**
         * @return Check if machine in Turbine Mixer mode.
         */
        private boolean isTurbineMixerMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.TURBINE_MIXER_RECIPES;
        }

        /**
         * @return Check if machine in Centrifuge mode.
         */
        private boolean isCentrifugeMode() {
            return this.getRecipeMap() == RecipeMaps.CENTRIFUGE_RECIPES;
        }

        /**
         * @return Check if machine in Thermal Centrifuge mode.
         */
        private boolean isThermalCentrifugeMode() {
            return this.getRecipeMap() == RecipeMaps.THERMAL_CENTRIFUGE_RECIPES;
        }

        /**
         * @return Check if machine in Sifter mode.
         */
        private boolean isSifterMode() {
            return this.getRecipeMap() == RecipeMaps.SIFTER_RECIPES;
        }

        /**
         * @return Check if machine in Sonicator mode.
         */
        private boolean isSonicatorMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.SONICATION_RECIPES;
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
            if (isMixerMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else if (isTurbineMixerMode()) {
                return Math.min(getMaxParallel(casingTier), 1024);
            } else if (isCentrifugeMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else if (isThermalCentrifugeMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else if (isSifterMode()) {
                return Math.min(getMaxParallel(casingTier), 4096);
            } else {
                return Math.min(getMaxParallel(casingTier), 2048);
            }
        }

        /**
         * @param maxProgress In some special mode, get 1/2^{casingTier} progress time.
         */
        @Override
        public void setMaxProgress(int maxProgress) {
            int MaxProgress = (int) Math.floor(maxProgress * Math.pow(0.5, casingTier));
            if (isMixerMode()) {
                super.setMaxProgress(MaxProgress);
            } else if (isCentrifugeMode()) {
                super.setMaxProgress(MaxProgress);
            } else if (isThermalCentrifugeMode()) {
                super.setMaxProgress(MaxProgress);
            } else if (isSifterMode()) {
                super.setMaxProgress(MaxProgress);
            } else {
                super.setMaxProgress(maxProgress);
            }
        }
    }
}
