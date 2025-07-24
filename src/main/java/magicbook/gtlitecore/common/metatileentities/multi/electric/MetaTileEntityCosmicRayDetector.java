package magicbook.gtlitecore.common.metatileentities.multi.electric;

import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregtech.api.block.VariantActiveBlock;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.Recipe;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.blocks.MetaBlocks;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.recipe.properties.AltitudeProperty;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.api.utils.GTLiteUtility;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.blocks.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class MetaTileEntityCosmicRayDetector extends RecipeMapMultiblockController {

    private BlockPos topBlockPos = new BlockPos(0, -64, 0);

    public MetaTileEntityCosmicRayDetector(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GTLiteRecipeMaps.COSMIC_RAY_DETECTOR_RECIPES);
        this.recipeMapWorkable = new CosmicRayDetectorRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MetaTileEntityCosmicRayDetector(metaTileEntityId);
    }

    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      XXX      ", "               ")
                .aisle("               ", "               ", "               ", "               ", "               ", "               ", "               ", "      XXX      ", "    XX   XX    ", "               ")
                .aisle("               ", "               ", "               ", "               ", "               ", "               ", "       X       ", "    XXX XXX    ", "   X       X   ", "               ")
                .aisle("      CCC      ", "      CCC      ", "      CCC      ", "               ", "               ", "       C       ", "     XXXXX     ", "   XX     XX   ", "  X         X  ", "               ")
                .aisle("     CCCCC     ", "     C   C     ", "     C   C     ", "      CCC      ", "      CCC      ", "     CCCCC     ", "    XXXXXXX    ", "  XX       XX  ", " X           X ", "               ")
                .aisle("    CCCCCCC    ", "    C     C    ", "    C     C    ", "     C   C     ", "     C   C     ", "    CCXXXCC    ", "   XXX   XXX   ", "  X         X  ", " X           X ", "               ")
                .aisle("   CCCCCCCCC   ", "   C   E   C   ", "   C       C   ", "    C     C    ", "    C  F  C    ", "    CXXXXXC    ", "   XX     XX   ", " XX         XX ", "X             X", "               ")
                .aisle("   CCCCCCCCC   ", "   C  ELE  C   ", "   C   L   C   ", "    C  L  C    ", "    C FLF C    ", "   CCXXEXXCC   ", "  XXX  T  XXX  ", " X     T     X ", "X      T      X", "       O       ")
                .aisle("   CCCCCCCCC   ", "   C   E   C   ", "   C       C   ", "    C     C    ", "    C  F  C    ", "    CXXXXXC    ", "   XX     XX   ", " XX         XX ", "X             X", "               ")
                .aisle("    CCCCCCC    ", "    C     C    ", "    C     C    ", "     C   C     ", "     C   C     ", "    CCXXXCC    ", "   XXX   XXX   ", "  X         X  ", " X           X ", "               ")
                .aisle("     CCCCC     ", "     C   C     ", "     C   C     ", "      CCC      ", "      CCC      ", "     CCCCC     ", "    XXXXXXX    ", "  XX       XX  ", " X           X ", "               ")
                .aisle("      CCC      ", "      CSC      ", "      CCC      ", "               ", "               ", "       C       ", "     XXXXX     ", "   XX     XX   ", "  X         X  ", "               ")
                .aisle("               ", "               ", "               ", "               ", "               ", "               ", "       X       ", "    XXX XXX    ", "   X       X   ", "               ")
                .aisle("               ", "               ", "               ", "               ", "               ", "               ", "               ", "      XXX      ", "    XX   XX    ", "               ")
                .aisle("               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      XXX      ", "               ")
                .where('S', this.selfPredicate())
                .where('C', states(getCasingState())
                        .setMinGlobalLimited(148)
                        .or(autoAbilities()))
                .where('X', states(getUniqueCasingState()))
                .where('E', states(getSecondUniqueState()))
                .where('F', states(getSecondCasingState()))
                .where('L', states(getFourthCasingState()))
                .where('T', states(getFrameState()))
                .where('O', altitudePredicate(getThirdCasingState()))
                .where(' ', any())
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.QUANTUM_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ADVANCED_SUBSTRATE_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.SPACETIME_CASING);
    }

    private static IBlockState getFourthCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HOLLOW_CASING);
    }

    private static IBlockState getUniqueCasingState() {
        return GTLiteMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.REFLECTIVE_CASING);
    }

    private static IBlockState getSecondUniqueState() {
        return GCYMMetaBlocks.UNIQUE_CASING.getState(gregicality.multiblocks.common.block.blocks.BlockUniqueCasing.UniqueCasingType.HEAT_VENT);
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.BlackTitanium).getBlock(GTLiteMaterials.BlackTitanium);
    }

    @NotNull
    protected TraceabilityPredicate altitudePredicate(IBlockState... allowedStates) {
        return new TraceabilityPredicate(blockWorldState -> {
            topBlockPos = blockWorldState.getPos();
            IBlockState state = blockWorldState.getBlockState();
            if (state.getBlock() instanceof VariantActiveBlock) {
                blockWorldState.getMatchContext().getOrPut("VABlock", new LinkedList<>()).add(blockWorldState.getPos());
            }
            return ArrayUtils.contains(allowedStates, state);
        }, GTLiteUtility.getCandidates(allowedStates));
    }

    private boolean canSeeSky() {
        return this.getWorld().canSeeSky(topBlockPos);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GTLiteTextures.QUANTUM_CASING;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (this.isStructureFormed()) {
            textList.add(new TextComponentTranslation("gtlitecore.machine.cosmic_ray_detector.altitude", topBlockPos.getY()));
//            textList.add(new TextComponentTranslation("gtlitecore.machine.cosmic_ray_detector.property." + canSeeSky()));
        }
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World world,
                               @NotNull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(I18n.format("gtlitecore.machine.cosmic_ray_detector.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.cosmic_ray_detector.tooltip.2"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setIntArray("topBlockPos", new int[] {topBlockPos.getX(), topBlockPos.getY(), topBlockPos.getZ()});
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        int[] pos = data.getIntArray("topBlockPos");
        this.topBlockPos = new BlockPos(pos[0], pos[1], pos[2]);
    }

    protected class CosmicRayDetectorRecipeLogic extends AdvancedRecipeLogic {
        public CosmicRayDetectorRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        @Override
        public boolean checkRecipe(@NotNull Recipe recipe) {
            return super.checkRecipe(recipe) && recipe.getProperty(AltitudeProperty.getInstance(), -64) <= topBlockPos.getY();
        }
    }
}