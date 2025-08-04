package magicbook.gtlitecore.common.metatileentities.multi.electric;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
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
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import magicbook.gtlitecore.api.GTLiteAPI;
import magicbook.gtlitecore.api.block.impl.WrappedIntTier;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.recipe.properties.ComponentCasingTierProperty;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.api.utils.GTLiteUtility;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.blocks.BlockBoilerCasing;
import magicbook.gtlitecore.common.blocks.BlockCleanroomCasing;
import magicbook.gtlitecore.common.blocks.BlockTransparentCasing;
import magicbook.gtlitecore.common.blocks.GTLiteMetaBlocks;
import magicbook.gtlitecore.common.metatileentities.GTLiteMetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static magicbook.gtlitecore.api.pattern.GTLiteTraceabilityPredicate.caCasings;
import static magicbook.gtlitecore.common.GTLiteConfigHolder.machines;

/**
 * Component Assembly Line
 *
 * @author MadMan310 (original author), Gate Guardian, Magic_Sweepy
 *
 * <p>
 *     Port from GoodGenerator, thanks my friend Gate Guardian help me fix somethings..
 * </p>
 *
 * @since 2.7.4-beta
 */
public class MetaTileEntityComponentAssemblyLine extends RecipeMapMultiblockController {

    private int casingTier;

    public MetaTileEntityComponentAssemblyLine(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GTLiteRecipeMaps.COMPONENT_ASSEMBLY_LINE_RECIPES);
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
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityComponentAssemblyLine(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object type = context.get("CACasingTieredStats");
        this.casingTier = GTLiteUtility.getOrDefault(() -> type instanceof WrappedIntTier,
                () -> ((WrappedIntTier) type).getIntTier(), 0);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.casingTier = 0;
    }

    @Override
    public boolean checkRecipe(@NotNull Recipe recipe,
                               boolean consumeIfSuccess) {
        return super.checkRecipe(recipe, consumeIfSuccess) && recipe.getProperty(ComponentCasingTierProperty.getInstance(), 0) <= casingTier;
    }
    @Override
    protected void initializeAbilities() {
        this.inputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.inputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.outputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        List<IEnergyContainer> energyContainer = new ArrayList<>(this.getAbilities(MultiblockAbility.INPUT_ENERGY));
        energyContainer.addAll(this.getAbilities(MultiblockAbility.INPUT_LASER));
        this.energyContainer = new EnergyContainerList(energyContainer);
    }
    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("HHHHHHHHH", "H  KKK  H", "H       H", "H       H", "H       H", "H       H", "HH     HH", " HHHHHHH ", "         ", "         ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " ELHHHLE ", "         ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A  n n  A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "HG     GH", "HG HHH GH", "HG     GH", "HG     GH", "HG  C  GH", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "HG     GH", "HG HHH GH", "HG     GH", "HG     GH", "HG  C  GH", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "HG     GH", "HG HHH GH", "HG     GH", "HG     GH", "HG  C  GH", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EL   LE ", "   BBB   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EL   LE ", "   HBH   ")
                .aisle("MHHHHHHHM", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " ELHHHLE ", "         ")
                .aisle("HHHHHHHHH", "H  N N  H", "H  JJJ  H", "H  JJJ  H", "H       H", "H       H", "HH III HH", " HHI~IHH ", "   III   ", "         ")
                .where('~', this.selfPredicate())
                .where('A', states(getGlassState()))
                .where('H', states(getCasingState()))
                .where('C', states(getSecondCasingState()))
                .where('D', states(getFrameState()))
                .where('G', states(getBoilerCasingState()))
                .where('E', states(getThirdCasingState()))
                .where('B', caCasings())
                .where('J', states(getCasingState())
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(6)
                                .setPreviewCount(3))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(6)
                                .setPreviewCount(3)))
                .where('N', states(MetaBlocks.FRAMES.get(Materials.TungstenSteel).getBlock(Materials.TungstenSteel))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(2)
                                .setPreviewCount(0))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(2)
                                .setPreviewCount(0)))
                .where('K', states(getCasingState())
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(3)
                                .setPreviewCount(1)))
                .where('L', states(getCasingState())
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMaxGlobalLimited(2)
                                .setPreviewCount(1))
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1)))
                .where('I', states(getCasingState())
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1)
                                .setPreviewCount(1)))
                .where('M', states(getCasingState())
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(6)
                                .setPreviewCount(0))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(6)
                                .setPreviewCount(0)))
                .where('n', states(MetaBlocks.FRAMES.get(Materials.TungstenSteel).getBlock(Materials.TungstenSteel)))
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.MULTIBLOCK_CASING.getState(magicbook.gtlitecore.common.blocks.BlockMultiblockCasing.MultiblockCasingType.IRIDIUM_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return MetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ASSEMBLY_LINE_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.CLEANROOM_CASING.getState(BlockCleanroomCasing.CleanroomCasingType.ADVANCED_FILTER_CASING);
    }

    private static IBlockState getGlassState() {
        return GTLiteMetaBlocks.TRANSPARENT_CASING.getState(BlockTransparentCasing.TransparentCasingType.PMMA_GLASS);
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.MARM200Steel).getBlock(GTLiteMaterials.MARM200Steel);
    }

    private static IBlockState getBoilerCasingState() {
        return GTLiteMetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.POLYBENZIMIDAZOLE);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GTLiteTextures.IRIDIUM_CASING;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("HHHHHHHHH", "H  HKH  H", "H       H", "H       H", "H       H", "H       H", "HH     HH", " HHHHHHH ", "         ", "         ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EHHHHHE ", "         ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A  N N  A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("QHHHHHHHP", "HG     GH", "HG HHH GH", "HG     GH", "HG     GH", "HG  C  GH", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("QHHHHHHHP", "HG     GH", "HG HHH GH", "HG     GH", "HG     GH", "HG  C  GH", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("QHHHHHHHP", "HG     GH", "HG HHH GH", "HG     GH", "HG     GH", "HG  C  GH", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "AG     GA", "AG HHH GA", "AG     GA", "AG     GA", "AG  C  GA", "HGG D GGH", "E GGDGG E", " EH   HE ", "   BBB   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A D   D A", "AC     CA", "AC     CA", "HC     CH", "E       E", " EH   HE ", "   HBH   ")
                .aisle("HHHHHHHHH", "A       A", "A  HHH  A", "A       A", "A       A", "A       A", "H       H", "E       E", " ELHHHLE ", "         ")
                .aisle("HHHHHHHHH", "H  N N  H", "H  XXX  H", "H  JJJ  H", "H       H", "H       H", "HH HIH HH", " HHH~HHH ", "   HHH   ", "         ")
                .where('~', GTLiteMetaTileEntities.COMPONENT_ASSEMBLY_LINE, EnumFacing.SOUTH)
                .where('A', getGlassState())
                .where('H', getCasingState())
                .where('C', getSecondCasingState())
                .where('D', getFrameState())
                .where('G', getBoilerCasingState())
                .where('E', getThirdCasingState())
                .where('J', MetaTileEntities.ITEM_IMPORT_BUS[4], EnumFacing.SOUTH)
                .where('X', MetaTileEntities.FLUID_IMPORT_HATCH[4], EnumFacing.SOUTH)
                .where('N', MetaBlocks.FRAMES.get(Materials.TungstenSteel).getBlock(Materials.TungstenSteel))
                .where('K', MetaTileEntities.ITEM_EXPORT_BUS[4], EnumFacing.NORTH)
                .where('L', MetaTileEntities.ENERGY_INPUT_HATCH[5], EnumFacing.SOUTH)
                .where('I', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH : getCasingState(), EnumFacing.SOUTH)
                .where('Q', MetaTileEntities.ITEM_IMPORT_BUS[4], EnumFacing.WEST)
                .where('P', MetaTileEntities.FLUID_IMPORT_HATCH[4], EnumFacing.EAST);
        GTLiteAPI.MAP_CA_CASING.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> ((WrappedIntTier) entry.getValue()).getIntTier()))
                .forEach(entry -> shapeInfo.add(builder.where('B', entry.getKey()).build()));
        return shapeInfo;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (this.isStructureFormed() && casingTier > 0) {
            textList.add(0,
                    new TextComponentTranslation("gtlitecore.machine.component_assembly_line.casing_tier",
                            new TextComponentTranslation("gtlitecore.machine.component_assembly_line.tier." + casingTier)));
        }
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @NotNull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtlitecore.machine.component_assembly_line.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.component_assembly_line.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.component_assembly_line.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.component_assembly_line.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.universal.tooltip.laser_input"));
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }
}