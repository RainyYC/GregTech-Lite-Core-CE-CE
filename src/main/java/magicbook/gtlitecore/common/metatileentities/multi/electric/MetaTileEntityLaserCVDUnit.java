package magicbook.gtlitecore.common.metatileentities.multi.electric;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiMapMultiblockController;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.MetaBlocks;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.blocks.BlockMultiblockCasing;
import magicbook.gtlitecore.common.blocks.BlockTransparentCasing;
import magicbook.gtlitecore.common.blocks.GTLiteMetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MetaTileEntityLaserCVDUnit extends MultiMapMultiblockController {

    public MetaTileEntityLaserCVDUnit(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                GTLiteRecipeMaps.CVD_UNIT_RECIPES,
                GTLiteRecipeMaps.LASER_CVD_UNIT_RECIPES});
        this.recipeMapWorkable = new LICVDRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityLaserCVDUnit(metaTileEntityId);
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("   XXXXX", "   XGGGX", "   XGGGX", "   XGGGX", "    XXX ")
                .aisle("XXXXXXXX", "XXXFCCCF", "XXXF   F", "XXXF   F", "    FFF ")
                .aisle("XXXXXXXX", "X XFCCCF", "X G    X", "XXXF   F", "    FFF ")
                .aisle("XXXXXXXX", "XXXFCCCF", "XSXF   F", "XXXF   F", "    FFF ")
                .aisle("   XXXXX", "   XGGGX", "   XGGGX", "   XGGGX", "    XXX ")
                .where('S', this.selfPredicate())
                .where('X', states(getCasingState())
                        .setMinGlobalLimited(60)
                        .or(autoAbilities()))
                .where('G', states(getGlassState()))
                .where('C', states(getSecondCasingState()))
                .where('F', states(getFrameState()))
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.HASTELLOY_X78_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ADVANCED_SUBSTRATE_CASING);
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.HastelloyX78).getBlock(GTLiteMaterials.HastelloyX78);
    }

    private static IBlockState getGlassState() {
        return GTLiteMetaBlocks.TRANSPARENT_CASING.getState(BlockTransparentCasing.TransparentCasingType.PMMA_GLASS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GTLiteTextures.HASTELLOY_X78_CASING;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return GTLiteTextures.CVD_UNIT_OVERLAY;
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @NotNull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.RAINBOW + I18n.format("gtlitecore.universal.tooltip.async_recipe"));
        tooltip.add(I18n.format("gtlitecore.machine.laser_cvd_unit.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.laser_cvd_unit.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.laser_cvd_unit.tooltip.3"));

    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class LICVDRecipeLogic extends AdvancedRecipeLogic {

        public LICVDRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        /**
         * @return Check if machine in LICVD mode.
         */
        private boolean isLaserInduced() {
            return this.getRecipeMap() == GTLiteRecipeMaps.LASER_CVD_UNIT_RECIPES;
        }

        /**
         * @param maxProgress If machine in common CVD, then get 1/2 progress time.
         */
        @Override
        public void setMaxProgress(int maxProgress) {
            if (isLaserInduced()) {
                this.maxProgressTime = maxProgress ;
            } else {
                this.maxProgressTime = maxProgress / 2;
            }
        }

        @Override
        public int getParallelLimit() {
            return 4 * (int)(Math.pow(GTUtility.getTierByVoltage(getMaxVoltage()), 1.5));
        }

        @Override
        public boolean isAllowRecipeAsync() {
            return true;
        }
    }
}