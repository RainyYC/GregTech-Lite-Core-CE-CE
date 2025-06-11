package magicbook.gtlitecore.common.metatileentities.multi.electric.generator;

import gregtech.api.GTValues;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.MultiblockFuelRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.*;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.TextComponentUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.blocks.MetaBlocks;
import magicbook.gtlitecore.api.capability.impl.AdvancedFuelRecipeLogic;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.blocks.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.GTValues.UIV;
import static gregtech.api.GTValues.VA;
import static magicbook.gtlitecore.common.GTLiteConfigHolder.machines;

public class MetaTileEntityDysonSwarm extends FuelMultiblockController {

    public MetaTileEntityDysonSwarm(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GTLiteRecipeMaps.DYSON_SWARM_RECIPES, UIV);
        this.recipeMapWorkable = new DysonSwarmWorkableHandler(this);
        this.recipeMapWorkable.setMaximumOverclockVoltage(VA[UIV]);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDysonSwarm(metaTileEntityId);
    }
    @Override
    public void checkStructurePattern() {
        if(machines.DelayStructureCheckSwitch) {
            if (this.getOffsetTimer() % 100 == 0 || this.isFirstTick()) {
                super.checkStructurePattern();
            }
        }
        else super.checkStructurePattern();
    }
    @Override
    protected void initializeAbilities() {
        this.inputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.inputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.outputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        List<IEnergyContainer> energyContainer = new ArrayList<>(this.getAbilities(MultiblockAbility.OUTPUT_LASER));
        energyContainer.addAll(this.getAbilities(MultiblockAbility.OUTPUT_ENERGY));
        this.energyContainer = new EnergyContainerList(energyContainer);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        DysonSwarmWorkableHandler recipeLogic = (DysonSwarmWorkableHandler) this.recipeMapWorkable;
        MultiblockDisplayText.Builder builder = MultiblockDisplayText.builder(textList, this.isStructureFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .addEnergyProductionLine(GTValues.V[UIV], recipeLogic.getRecipeEUt())
                .addCustom((tl) -> {
                    if (this.isStructureFormed() && recipeLogic.isCoolant) {
                        String key = "gtlitecore.machine.dyson_swarm.coolant";
                        tl.add(TextComponentUtil.translationWithColor(TextFormatting.AQUA, key));
                    }})
                .addWorkingStatusLine();
    }

    @Override
    protected void addErrorText(List<ITextComponent> textList) {
        super.addErrorText(textList);
        if (this.isStructureFormed()) {
            FluidStack coolantStack = this.getInputFluidInventory().drain(GTLiteMaterials.GelidCryotheum.getFluid(Integer.MAX_VALUE), false);
            if (coolantStack == null || coolantStack.amount == 0) {
                textList.add(TextComponentUtil.translationWithColor(TextFormatting.RED, "gtlitecore.machine.dyson_swarm.no_coolant"));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @NotNull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gregtech.universal.tooltip.base_production_eut", GTValues.V[UIV]));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.7"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.8"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.9"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.10"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.11"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.tooltip.12"));
        tooltip.add(I18n.format("gtlitecore.machine.dyson_swarm.uses_per_hour_coolant", 3600000));
        tooltip.add(I18n.format("gtlitecore.universal.tooltip.laser_output"));
    }

    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "  XXX       FZF ", "            FZF ", "            FZF ", "  RRR       FZF ", "            FZF ", "  RRR       FZF ", "            FZF ", "  RRR       FZF ", "             F  ", "  RRR        F  ", "             F  ", "             F  ", "  RRR        F  ", "  RRR        F  ", "  RRR        F  ", "             F  ")
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "CCXXXCC    BBOBB", " X   X      Z Z ", "            Z Z ", "            Z Z ", " R f R      Z Z ", "            Z Z ", " R f R      Z Z ", "            Z Z ", " R f R      Z Z ", "            F F ", " R f R      F F ", "            F F ", "  RRR       F F ", " RRRRR      F F ", " RRRRR      F F ", " RRRRR      F F ", "  RRR       F F ")
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "CCXCXCC    BBBBB", " X Y X      FZF ", "   Y        FZF ", "   Y        FZF ", " RfYfR      FZF ", "   Y        FZF ", " RfYfR      FZF ", "   Y        FZF ", " RfYfR      FZF ", "   Y         F  ", " RfYfR       F  ", "   Y         F  ", "  RRR        F  ", " RRRRR       F  ", " RRRRR       F  ", " RRRRR       F  ", "  RRR        F  ")
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC    BBBBB", "CCCCCCC    BBBBB", "CCXXXCC    BBBBB", " X   X          ", "                ", "                ", " R f R          ", "                ", " R f R          ", "                ", " R f R          ", "                ", " R f R          ", "                ", "  RRR           ", " RRRRR          ", " RRRRR          ", " RRRRR          ", "  RRR           ")
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC         ", "CCCCCCC         ", "CCCCCCC         ", "  XXX           ", "                ", "                ", "  RRR           ", "                ", "  RRR           ", "                ", "  RRR    yyy    ", "                ", "  RRR           ", "                ", "                ", "  RRR           ", "  RRR           ", "  RRR           ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "CCCCCCC         ", "CCCCCCC         ", "CCCCCCC         ", "                ", "                ", "                ", "                ", "                ", "                ", "         yyy    ", "        y   y   ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "        yyyyy   ", "       y     y  ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "        ccccc   ", "        ccccc   ", "        ccccc   ", "                ", "                ", "                ", "                ", "                ", "         yyy    ", "       yy   yy  ", "      y       y ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "        ccccc   ", "        coooc   ", "        ccccc   ", "         x x    ", "         x x    ", "         x x    ", "         x x    ", "         x x    ", "        yyyyy   ", "      yy     yy ", "     y         y", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "        ccccc   ", "        coooc   ", "        ccccc   ", "                ", "                ", "                ", "                ", "                ", "        yyyyy   ", "      yy  g  yy ", "     y    g    y", "          r     ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "        ccccc   ", "        coooc   ", "        ccccc   ", "         x x    ", "         x x    ", "         x x    ", "         x x    ", "         x x    ", "        yyyyy   ", "      yy     yy ", "     y         y", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "        ccScc   ", "        ccccc   ", "        ccccc   ", "                ", "                ", "                ", "                ", "                ", "         yyy    ", "       yy   yy  ", "      y       y ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "        yyyyy   ", "       y     y  ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "         yyy    ", "        y   y   ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .aisle("DDDDDDDDDDDDDDDD", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "         yyy    ", "                ", "                ", "                ", "                ", "                ", "                ", "                ", "                ")
                .where('S', this.selfPredicate())
                .where('B', states(getSecondCasingState()))
                .where('C', states(getThirdCasingState()))
                .where('D', states(getFourthCasingState()))
                .where('Z', states(getFifthCasingState()))
                .where('R', states(getSixthCasingState()))
                .where('c', states(getSeventhCasingState())
                        .setMinGlobalLimited(50)
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(1))
                        .or(metaTileEntities(MultiblockAbility.REGISTRY.get(MultiblockAbility.OUTPUT_ENERGY).stream()
                                .filter(mte -> {
                                    IEnergyContainer container = mte.getCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, null);
                                    return container != null && container.getOutputVoltage() >= GTValues.V[UIV];})
                                .toArray(MetaTileEntity[]::new))
                                .setMaxGlobalLimited(1)
                                .setPreviewCount(1))
                        .or(abilities(MultiblockAbility.OUTPUT_LASER)
                                .setMaxGlobalLimited(1)
                                .setPreviewCount(1)))
                .where('O', states(getUniqueCasingState()))
                .where('y', states(getSecondUniqueCasingState()))
                .where('r', states(getThirdUniqueCasingState()))
                .where('X', states(getCoilState()))
                .where('o', states(getSecondCoilState()))
                .where('Y', states(getThirdCoilState()))
                .where('F', states(getFrameState()))
                .where('f', states(getSecondFrameState()))
                .where('x', states(getThirdFrameState()))
                .where('g', states(getFourthFrameState()))
                .build();
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.DEPLOYMENT_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.CONTROL_CASING);
    }

    private static IBlockState getFourthCasingState() {
        return GTLiteMetaBlocks.SPACE_ELEVATOR_CASING.getState(BlockSpaceElevatorCasing.ElevatorCasingType.FLOOR);
    }

    private static IBlockState getFifthCasingState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.DEPLOYMENT_MAGNET);
    }

    private static IBlockState getSixthCasingState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.CONTROL_TOROID);
    }

    private static IBlockState getSeventhCasingState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.RECEIVER_CASING);
    }

    private static IBlockState getUniqueCasingState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.DEPLOYMENT_CORE);
    }

    private static IBlockState getSecondUniqueCasingState() {
        return GTLiteMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.REFLECTIVE_CASING);
    }

    private static IBlockState getThirdUniqueCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.SPACETIME_CASING);
    }

    private static IBlockState getCoilState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.CONTROL_PRIMARY);
    }

    private static IBlockState getSecondCoilState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.HYPOGEN_COIL_BLOCK);
    }

    private static IBlockState getThirdCoilState() {
        return GTLiteMetaBlocks.DYSON_SWARM_CASING.getState(BlockDysonSwarmCasing.DysonSwarmCasingType.CONTROL_SECONDARY);
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.Orichalcum).getBlock(GTLiteMaterials.Orichalcum);
    }

    private static IBlockState getSecondFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.Nitinol60).getBlock(GTLiteMaterials.Nitinol60);
    }

    private static IBlockState getThirdFrameState() {
        return MetaBlocks.FRAMES.get(Materials.HSSS).getBlock(Materials.HSSS);
    }

    private static IBlockState getFourthFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.BlackPlutonium).getBlock(GTLiteMaterials.BlackPlutonium);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GTLiteTextures.RECEIVER_CASING;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return GTLiteTextures.DYSON_SWARM_OVERLAY;
    }

    @Override
    protected boolean shouldShowVoidingModeButton() {
        return false;
    }

    private static class DysonSwarmWorkableHandler extends AdvancedFuelRecipeLogic {

        private boolean isCoolant = false;
        private final MetaTileEntityDysonSwarm dysonSwarm;
        private static final FluidStack GELID_CRYOTHEUM_STACK = GTLiteMaterials.GelidCryotheum.getFluid(3600000);

        public DysonSwarmWorkableHandler(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
            this.dysonSwarm = (MetaTileEntityDysonSwarm) tileEntity;
        }

        @Override
        protected void updateRecipeProgress() {
            if (this.canRecipeProgress && this.drawEnergy(this.recipeEUt, true)) {
                this.drainGelidCryotheum();
                this.drawEnergy(this.recipeEUt, false);
                if (++this.progressTime > this.maxProgressTime) {
                    this.completeRecipe();
                }
            }
        }

        protected boolean checkGelidCryotheum() {
            IMultipleTankHandler inputTank = this.dysonSwarm.getInputFluidInventory();
            if (GELID_CRYOTHEUM_STACK.isFluidStackIdentical(inputTank.drain(GELID_CRYOTHEUM_STACK, false))) {
                return true;
            } else {
                this.invalidate();
                return false;
            }
        }

        protected void drainGelidCryotheum() {
            if (this.isCoolant && this.totalContinuousRunningTime % 20L == 0L) {
                this.dysonSwarm.getInputFluidInventory().drain(GELID_CRYOTHEUM_STACK, true);
            }
        }

        @Override
        protected boolean canProgressRecipe() {
            return super.canProgressRecipe() && this.checkGelidCryotheum();
        }

        @Override
        public void invalidate() {
            this.isCoolant = false;
            super.invalidate();
        }
    }
}
