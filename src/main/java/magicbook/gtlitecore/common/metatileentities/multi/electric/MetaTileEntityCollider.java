package magicbook.gtlitecore.common.metatileentities.multi.electric;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockComputerCasing;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import magicbook.gtlitecore.api.GTLiteAPI;
import magicbook.gtlitecore.api.block.impl.WrappedIntTier;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
import magicbook.gtlitecore.api.pattern.GTLiteTraceabilityPredicate;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.recipe.properties.FieldCasingTierProperty;
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

import static gregtech.api.GTValues.ZPM;

public class MetaTileEntityCollider extends RecipeMapMultiblockController {

    private int casingTier;

    public MetaTileEntityCollider(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GTLiteRecipeMaps.COLLIDER_RECIPES);
        this.recipeMapWorkable = new AdvancedRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityCollider(metaTileEntityId);
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
    public boolean checkRecipe(@Nonnull Recipe recipe,
                               boolean consumeIfSuccess) {
        return super.checkRecipe(recipe, consumeIfSuccess) && recipe.getProperty(FieldCasingTierProperty.getInstance(), 0) <= casingTier;
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("         A A A         ", "         AAAAA         ", "         A A A         ")
                .aisle("        AAAAAAA        ", "       AADDDDDAA       ", "        AAAAAAA        ")
                .aisle("      bbbbIIIbbbb      ", "      cDDeeeeeDDc      ", "      bbbbIIIbbbb      ")
                .aisle("     bAAAAAAAAAAAb     ", "     cDeeDDDDDeeDc     ", "     bAAAAAAAAAAAb     ")
                .aisle("    bAAAA     AAAAb    ", "    cDeDD     DDeDc    ", "    bAAAA     AAAAb    ")
                .aisle("   bAAA         AAAb   ", "   cDeD         DeDc   ", "   bAAA         AAAb   ")
                .aisle("  bAAA           AAAb  ", "  cDeD           DeDc  ", "  bAAA           AAAb  ")
                .aisle("  bAA             AAb  ", " ADeD             DeDA ", "  bAA             AAb  ")
                .aisle(" AbAA             AAbA ", " ADeD             DeDA ", " AbAA             AAbA ")
                .aisle("AAbA               AbAA", "ADeD               DeDA", "AAbA               AbAA")
                .aisle(" AIA               AIA ", "ADeD               DeDA", " AIA               AIA ")
                .aisle("AAIA               AIAA", "ADeD               DeDA", "AAIA               AIAA")
                .aisle(" AIA               AIA ", "ADeD               DeDA", " AIA               AIA ")
                .aisle("AAbA               AbAA", "ADeD               DeDA", "AAbA               AbAA")
                .aisle(" AbAA             AAbA ", " ADeD             DeDA ", " AbAA             AAbA ")
                .aisle("  bAA             AAb  ", " ADeD             DeDA ", "  bAA             AAb  ")
                .aisle("  bAAA           AAAb  ", "  cDeD           DeDc  ", "  bAAA           AAAb  ")
                .aisle("   bAAA         AAAb   ", "   cDeD         DeDc   ", "   bAAA         AAAb   ")
                .aisle("    bAAAAbJJJbAAAAb    ", "    cDeDDbJeJbDDeDc    ", "    bAAAAbJJJbAAAAb    ")
                .aisle("     bAHHbbbbbHHAb     ", "     cDeeDDDDDeeDc     ", "     bAHHbbbbbHHAb     ")
                .aisle("      bbbbGFGbbbb      ", "      cDDee~eeDDc      ", "      bbbbGFGbbbb      ")
                .where('~', this.selfPredicate())
                .where('A', states(getCasingState()))
                .where('b', states(getCasingState())
                        .setMinGlobalLimited(96)
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)))
                .where('c', GTLiteTraceabilityPredicate.FIELD_CASING.get())
                .where('D', states(getCoilState()))
                .where('e', states(getSecondCasingState()))
                .where('I', states(getSecondCoilState()))
                .where('J', states(getCasingState())
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS))
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)))
                .where('H', states(getThirdCasingState()))
                .where('F', states(getCasingState())
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1)
                                .setPreviewCount(1)))
                .where('G', states(getCasingState())
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMinGlobalLimited(2)
                                .setPreviewCount(4)))
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HIGH_ENERGY_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HOLLOW_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return MetaBlocks.COMPUTER_CASING.getState(BlockComputerCasing.CasingType.HIGH_POWER_CASING);
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
        return GTLiteTextures.HIGH_ENERGY_CASING;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return GTLiteTextures.COLLIDER_OVERLAY;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("         A A A         ", "         AAAAA         ", "         A A A         ")
                .aisle("        AAAAAAA        ", "       AADDDDDAA       ", "        AAAAAAA        ")
                .aisle("      bbbbIIIbbbb      ", "      cDDeeeeeDDc      ", "      bbbbIIIbbbb      ")
                .aisle("     bAAAAAAAAAAAb     ", "     cDeeDDDDDeeDc     ", "     bAAAAAAAAAAAb     ")
                .aisle("    bAAAA     AAAAb    ", "    cDeDD     DDeDc    ", "    bAAAA     AAAAb    ")
                .aisle("   bAAA         AAAb   ", "   cDeD         DeDc   ", "   bAAA         AAAb   ")
                .aisle("  bAAA           AAAb  ", "  cDeD           DeDc  ", "  bAAA           AAAb  ")
                .aisle("  bAA             AAb  ", " ADeD             DeDA ", "  bAA             AAb  ")
                .aisle(" AbAA             AAbA ", " ADeD             DeDA ", " AbAA             AAbA ")
                .aisle("AAbA               AbAA", "ADeD               DeDA", "AAbA               AbAA")
                .aisle(" AIA               AIA ", "ADeD               DeDA", " AIA               AIA ")
                .aisle("AAIA               AIAA", "ADeD               DeDA", "AAIA               AIAA")
                .aisle(" AIA               AIA ", "ADeD               DeDA", " AIA               AIA ")
                .aisle("AAbA               AbAA", "ADeD               DeDA", "AAbA               AbAA")
                .aisle(" AbAA             AAbA ", " ADeD             DeDA ", " AbAA             AAbA ")
                .aisle("  bAA             AAb  ", " ADeD             DeDA ", "  bAA             AAb  ")
                .aisle("  bAAA           AAAb  ", "  cDeD           DeDc  ", "  bAAA           AAAb  ")
                .aisle("   bAAA         AAAb   ", "   cDeD         DeDc   ", "   bAAA         AAAb   ")
                .aisle("    bAAAAbZJZbAAAAb    ", "    cDeDDbPePbDDeDc    ", "    bAAAAbZJZbAAAAb    ")
                .aisle("     bAHHbbbbbHHAb     ", "     cDeeDDDDDeeDc     ", "     bAHHbbbbbHHAb     ")
                .aisle("      bbbXGWGXbbb      ", "      cDDee~eeDDc      ", "      bbbYGFGYbbb      ")
                .where('~', GTLiteMetaTileEntities.COLLIDER, EnumFacing.SOUTH)
                .where('A', getCasingState())
                .where('b', getCasingState())
                .where('X', MetaTileEntities.ITEM_IMPORT_BUS[ZPM], EnumFacing.SOUTH)
                .where('Y', MetaTileEntities.ITEM_EXPORT_BUS[ZPM], EnumFacing.SOUTH)
                .where('D', getCoilState())
                .where('e', getSecondCasingState())
                .where('I', getSecondCoilState())
                .where('J', getCasingState())
                .where('Z', MetaTileEntities.FLUID_IMPORT_HATCH[ZPM], EnumFacing.NORTH)
                .where('P', MetaTileEntities.FLUID_EXPORT_HATCH[ZPM], EnumFacing.NORTH)
                .where('H', getThirdCasingState())
                .where('F', getCasingState())
                .where('W', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH : getCasingState(), EnumFacing.SOUTH)
                .where('G', MetaTileEntities.ENERGY_INPUT_HATCH[ZPM], EnumFacing.SOUTH);
        GTLiteAPI.MAP_FIELD_CASING.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> ((WrappedIntTier) entry.getValue()).getIntTier()))
                .forEach(entry -> shapeInfo.add(builder.where('c', entry.getKey()).build()));
        return shapeInfo;
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @Nonnull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtlitecore.machine.collider.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.collider.tooltip.2"));
    }
}