package magicbook.gtlitecore.common.metatileentities.multi.electric.adv;

import gregicality.multiblocks.api.unification.GCYMMaterials;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
import magicbook.gtlitecore.common.blocks.BlockStructureCasing;
import magicbook.gtlitecore.common.blocks.BlockTransparentCasing;
import magicbook.gtlitecore.common.blocks.GTLiteMetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MetaTileEntityMegaVacuumFreezer extends RecipeMapMultiblockController {

    public MetaTileEntityMegaVacuumFreezer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.VACUUM_RECIPES);
        this.recipeMapWorkable = new MegaVacuumFreezerRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityMegaVacuumFreezer(metaTileEntityId);
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("     CCCCC     ", "     CCGCC     ", "     CCGCC     ", "     CCGCC     ", "     CCGCC     ", "     CCGCC     ", "     CCGCC     ", "     CCCCC     ")
                .aisle("   CCCCCCCCC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CCCCCCCCC   ")
                .aisle("  CCCCCCCCCCC  ", "  G#####DF##G  ", "  G#####DF##G  ", "  G#####DF##G  ", "  G#####DF##G  ", "  G#####DF##G  ", "  G#####DF##G  ", "  CCCCCCCCCCC  ")
                .aisle(" CCCCCCCCCCCCC ", " CC####DF###CC ", " CC####DF###CC ", " CC####DF###CC ", " CC####DF###CC ", " CC####DF###CC ", " CC####DF###CC ", " CCCCCCCCCCCCC ")
                .aisle(" CCCCCCCCCCCCC ", " C####DF#####C ", " C####DF#####C ", " C####DF#####C ", " C####DF#####C ", " C####DF#####C ", " C####DF#####C ", " CCCCCCCCCCCCC ")
                .aisle("CCCCCCCCCCCCCCC", "CCF###DDD####CC", "CCF###DDD####CC", "CCF###DDD####CC", "CCF###DDD####CC", "CCF###DDD####CC", "CCF###DDD####CC", "CCCCCCCCCCCCCCC")
                .aisle("CCCCCCCCCCCCCCC", "C#DF#D#P#DD###C", "C#DF#D#P#DD###C", "C#DF#D#P#DD###C", "C#DF#D#P#DD###C", "C#DF#D#P#DD###C", "C#DF#D#P#DD###C", "CCCCCCCCCCCCCCC")
                .aisle("CCCCCCCCCCCCCCC", "G##DFDPXPDFD##G", "G##DFDPXPDFD##G", "G##DFDPXPDFD##G", "G##DFDPXPDFD##G", "G##DFDPXPDFD##G", "G##DFDPXPDFD##G", "CCCCCCCMCCCCCCC")
                .aisle("CCCCCCCCCCCCCCC", "C###DD#P#D#FD#C", "C###DD#P#D#FD#C", "C###DD#P#D#FD#C", "C###DD#P#D#FD#C", "C###DD#P#D#FD#C", "C###DD#P#D#FD#C", "CCCCCCCCCCCCCCC")
                .aisle("CCCCCCCCCCCCCCC", "CC####DDD###FCC", "CC####DDD###FCC", "CC####DDD###FCC", "CC####DDD###FCC", "CC####DDD###FCC", "CC####DDD###FCC", "CCCCCCCCCCCCCCC")
                .aisle(" CCCCCCCCCCCCC ", " C#####FD####C ", " C#####FD####C ", " C#####FD####C ", " C#####FD####C ", " C#####FD####C ", " C#####FD####C ", " CCCCCCCCCCCCC ")
                .aisle(" CCCCCCCCCCCCC ", " CC###FD####CC ", " CC###FD####CC ", " CC###FD####CC ", " CC###FD####CC ", " CC###FD####CC ", " CC###FD####CC ", " CCCCCCCCCCCCC ")
                .aisle("  CCCCCCCCCCC  ", "  G##FD#####G  ", "  G##FD#####G  ", "  G##FD#####G  ", "  G##FD#####G  ", "  G##FD#####G  ", "  G##FD#####G  ", "  CCCCCCCCCCC  ")
                .aisle("   CCCCCCCCC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CGC###CGC   ", "   CCCCCCCCC   ")
                .aisle("     CCSCC     ", "     CGGGC     ", "     CGGGC     ", "     CGGGC     ", "     CGGGC     ", "     CGGGC     ", "     CGGGC     ", "     CCCCC     ")
                .where('S', this.selfPredicate())
                .where('C', states(getCasingState())
                        .setMinGlobalLimited(500)
                        .or(autoAbilities(true, true, true, true, true, true, false)))
                .where('X', states(getBoilerCasingState()))
                .where('P', states(getSecondBoilerCasingState()))
                .where('D', states(getSecondCasingState()))
                .where('G', states(getGlassState()))
                .where('F', states(getFrameState()))
                .where('M', abilities(MultiblockAbility.MUFFLER_HATCH)
                        .setExactLimit(1)
                        .setPreviewCount(1))
                .where(' ', any())
                .where('#', air())
                .build();
    }

    private static IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.ALUMINIUM_FROSTPROOF);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.STRUCTURE_CASING.getState(BlockStructureCasing.StructureCasingType.ADVANCED_GRATE_CASING);
    }

    private static IBlockState getBoilerCasingState() {
        return MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE);
    }

    private static IBlockState getSecondBoilerCasingState() {
        return GTLiteMetaBlocks.BOILER_CASING.getState(magicbook.gtlitecore.common.blocks.BlockBoilerCasing.BoilerCasingType.POLYBENZIMIDAZOLE);
    }

    private static IBlockState getGlassState() {
        return GTLiteMetaBlocks.TRANSPARENT_CASING.getState(BlockTransparentCasing.TransparentCasingType.PMMA_GLASS);
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(GCYMMaterials.Zeron100).getBlock(GCYMMaterials.Zeron100);
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @Nonnull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.RAINBOW_SLOW + I18n.format("gregtech.machine.perfect_oc"));
        tooltip.add(TooltipHelper.RAINBOW + I18n.format("gtlitecore.universal.tooltip.async_recipe"));
        tooltip.add(I18n.format("gtlitecore.machine.mega_vacuum_freezer.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.mega_vacuum_freezer.tooltip.2"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.FROST_PROOF_CASING;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.VACUUM_FREEZER_OVERLAY;
    }

    @Override
    public boolean hasMufflerMechanics() {
        return true;
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    protected static class MegaVacuumFreezerRecipeLogic extends AdvancedRecipeLogic {

        private final MetaTileEntityMegaVacuumFreezer vacuumFreezer;

        public MegaVacuumFreezerRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity, true);
            this.vacuumFreezer = (MetaTileEntityMegaVacuumFreezer) tileEntity;
        }

        @Override
        public int getParallelLimit() {
            int v = getOverMAXV(getInputEUt());
            v = Math.max(10, v);
            return (int) (1024 * Math.pow(1.2, v - 10));
        }

        @Override
        public void setMaxProgress(int maxProgress) {
            this.maxProgressTime = maxProgress / 2;
        }

        @Override
        public boolean isAllowRecipeAsync() {
            return true;
        }
    }
}
