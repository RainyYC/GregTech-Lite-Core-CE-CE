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
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
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
import magicbook.gtlitecore.common.blocks.BlockStructureCasing;
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

import static gregtech.api.GTValues.UV;

public class MetaTileEntityCirculativeCoolingTower extends MultiMapMultiblockController {

    public int casingTier;

    public MetaTileEntityCirculativeCoolingTower(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.COMPRESSOR_RECIPES,
                GTLiteRecipeMaps.VACUUM_CHAMBER_RECIPES,
                GTLiteRecipeMaps.CHEMICAL_DRYER_RECIPES,
                GTLiteRecipeMaps.VACUUM_DRYING_RECIPES,
                RecipeMaps.VACUUM_RECIPES,
                GTLiteRecipeMaps.CRYOGENIC_REACTOR_RECIPES,
                GTLiteRecipeMaps.LARGE_GAS_COLLECTOR_RECIPES,
                GTLiteRecipeMaps.PLASMA_CONDENSER_RECIPES
        });
        this.recipeMapWorkable = new CirculativeCoolingTowerRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityCirculativeCoolingTower(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object type = context.get("CoolingCoreTieredStats");
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
        this.energyContainer = new EnergyContainerList(energyContainer);
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("                             ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "                             ")
                .aisle("             XXX             ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "             XXX             ")
                .aisle("           XXYZYXX           ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "           XXYZYXX           ")
                .aisle("          XYYYZYYYX          ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "          XYYYZYYYX          ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("        XYYYYXXXYYYYX        ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "        XYYYYXXXYYYYX        ")
                .aisle("        XZZZZXXXZZZZX        ", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "        XZZZZXXXZZZZX        ")
                .aisle("        XYYYYXXXYYYYX        ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "        XYYYYXXXYYYYX        ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("          XYYYZYYYX          ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "          XYYYZYYYX          ")
                .aisle("           XXYZYXX           ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "           XXYZYXX           ")
                .aisle("             XXX             ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CSC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "             XXX             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "                             ")
                .where('S', this.selfPredicate())
                .where('C', states(getCasingState())
                        .setMinGlobalLimited(600)
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMaxGlobalLimited(3))
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(32))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(32))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(32))
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)
                                .setMaxGlobalLimited(32)))
                .where('X', states(getSecondCasingState()))
                .where('Y', states(getThirdCasingState()))
                .where('Z', states(getFourthCasingState()))
                .where('O', states(getBoilerCasingState()))
                .where('F', states(getFrameState()))
                .where('Q', states(getUniqueCasingState()))
                .where('T', GTLiteTraceabilityPredicate.COOLING_CORE.get())
                .where('P', states(getCoilState()))
                .where('W', states(getCasingState()))
                .where('#', air())
                .where(' ', any())
                .build();
    }

    private static IBlockState getCasingState() {
        return GTLiteMetaBlocks.STRUCTURE_CASING.getState(BlockStructureCasing.StructureCasingType.HATTRIUM_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HIGH_ENERGY_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.ADVANCED_HIGH_ENERGY_CASING);
    }

    private static IBlockState getFourthCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.ULTIMATE_HIGH_ENERGY_CASING);
    }

    private static IBlockState getBoilerCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.HOLLOW_CASING);
    }

    private static IBlockState getCoilState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.MOLECULAR_COIL);
    }

    private static IBlockState getUniqueCasingState() {
        return GTLiteMetaBlocks.STRUCTURE_CASING.getState(BlockStructureCasing.StructureCasingType.CIRCULATIVE_COOLING_CASING);
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(Materials.Aluminium).getBlock(Materials.Aluminium);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GTLiteTextures.HATTRIUM_CASING;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return GTLiteTextures.LARGE_PROCESSING_FACTORY_OVERLAY;
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
                .aisle("                             ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "                             ")
                .aisle("             XXX             ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "             XXX             ")
                .aisle("           XXYZYXX           ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "           XXYZYXX           ")
                .aisle("          XYYYZYYYX          ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "          XYYYZYYYX          ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("        XYYYYXXXYYYYX        ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "        XYYYYXXXYYYYX        ")
                .aisle("        XZZZZXXXZZZZX        ", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "TPW     XZZZZXOXZZZZX     WPT", "        CFFFFFOFFFFFC        ", "        C#####O#####C        ", "        CFFFFFOFFFFFC        ", "        XZZZZXXXZZZZX        ")
                .aisle("        XYYYYXXXYYYYX        ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "QPW     XYYYYXXXYYYYX     WPQ", "        C#####F#####C        ", "        C###########C        ", "        C#####F#####C        ", "        XYYYYXXXYYYYX        ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("         XYYYYZYYYYX         ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", " QPW     XYYYYZYYYYX     WPQ ", "         C####F####C         ", "         C#########C         ", "         C####F####C         ", "         XYYYYZYYYYX         ")
                .aisle("          XYYYZYYYX          ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "  QPW     XYYYZYYYX     WPQ  ", "          C###F###C          ", "          C#######C          ", "          C###F###C          ", "          XYYYZYYYX          ")
                .aisle("           XXYZYXX           ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "   QPW     XXYZYXX     WPQ   ", "           CC#F#CC           ", "           CC###CC           ", "           CC#F#CC           ", "           XXYZYXX           ")
                .aisle("             XXX             ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             ERE             ", "             KSL             ", "             IMJ             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "    QPW      XXX      WPQ    ", "             CCC             ", "             CCC             ", "             CCC             ", "             XXX             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "     QPW             WPQ     ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "      QPW           WPQ      ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "       QPWWWWWWWWWWWPQ       ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "        QPPPPPPPPPPPQ        ", "                             ", "                             ", "                             ", "                             ")
                .aisle("                             ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "         QQQTQTQTQQQ         ", "                             ", "                             ", "                             ", "                             ")
                .where('S', GTLiteMetaTileEntities.CIRCULATIVE_COOLING_TOWER, EnumFacing.SOUTH)
                .where('C', getCasingState())
                .where('I', MetaTileEntities.ITEM_IMPORT_BUS_ME, EnumFacing.SOUTH)
                .where('J', MetaTileEntities.ITEM_EXPORT_BUS_ME, EnumFacing.SOUTH)
                .where('K', MetaTileEntities.FLUID_IMPORT_HATCH_ME, EnumFacing.SOUTH)
                .where('L', MetaTileEntities.FLUID_EXPORT_HATCH_ME, EnumFacing.SOUTH)
                .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[UV], EnumFacing.SOUTH)
                .where('R', MetaTileEntities.LASER_INPUT_HATCH_256[3], EnumFacing.SOUTH)
                .where('Q', getUniqueCasingState())
                .where('X', getSecondCasingState())
                .where('Y', getThirdCasingState())
                .where('Z', getFourthCasingState())
                .where('O', getBoilerCasingState())
                .where('F', getFrameState())
                .where('P', getCoilState())
                .where('W', getCasingState())
                .where('M', MetaTileEntities.AUTO_MAINTENANCE_HATCH, EnumFacing.SOUTH)
                .where('#', Blocks.AIR.getDefaultState());
        GTLiteAPI.MAP_COOLING_CORE.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> ((WrappedIntTier) entry.getValue()).getIntTier()))
                .forEach(entry -> shapeInfo.add(builder.where('T', entry.getKey()).build()));
        return shapeInfo;
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @Nonnull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.7"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.8"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.9"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.10"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.11"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.12"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.13"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.14"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.15"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.16"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.17"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.18"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.19"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.20"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.21"));
        tooltip.add(I18n.format("gtlitecore.machine.circulative_cooling_tower.tooltip.22"));
        tooltip.add(I18n.format("gtlitecore.universal.tooltip.laser_input"));
    }

    public class CirculativeCoolingTowerRecipeLogic extends AdvancedRecipeLogic {

        public CirculativeCoolingTowerRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        /**
         * @return Check if machine in Compressor mode.
         */
        private boolean isCompressorMode() {
            return this.getRecipeMap() == RecipeMaps.COMPRESSOR_RECIPES;
        }

        /**
         * @return Check if machine in Vacuum Chamber Mode.
         */
        private boolean isVacuumChamberMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.VACUUM_CHAMBER_RECIPES;
        }

        /**
         * @return Check if machine in Chemical Dryer Mode.
         */
        private boolean isChemicalDryerMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.CHEMICAL_DRYER_RECIPES;
        }

        /**
         * @return Check if machine in Vacuum Drying Mode.
         */
        private boolean isVacuumDryingMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.VACUUM_DRYING_RECIPES;
        }

        /**
         * @return Check if machine in Vacuum Freezer Mode.
         */
        private boolean isVacuumFreezerMode() {
            return this.getRecipeMap() == RecipeMaps.VACUUM_RECIPES;
        }

        /**
         * @return Check if machine in Cryogenic Reactor Mode.
         */
        private boolean isCryogenicReactorMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.CRYOGENIC_REACTOR_RECIPES;
        }

        /**
         * @return Check if machine in Gas Collector Mode.
         */
        private boolean isGasCollectorMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.LARGE_GAS_COLLECTOR_RECIPES;
        }

        /**
         * @return Check if machine in Plasma Condenser Mode.
         */
        private boolean isPlasmaCondenserMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.PLASMA_CONDENSER_RECIPES;
        }

        /**
         * @param casingTier Cooling Core  Tier.
         * @return Max Parallel of this machine: tier * (casingTier * 64), where tier means voltage tier.
         */
        public int getMaxParallel(int casingTier) {
            int tier = GTUtility.getTierByVoltage(getMaxVoltage());
            return tier * (casingTier * 64);
        }

        /**
         * @return Get some parallels, and specially, different modes has different parallel limits.
         */
        @Override
        public int getParallelLimit() {
            if (isCompressorMode()) {
                return (6 * getMaxParallel(casingTier));
            } else if (isVacuumChamberMode()) {
                return (4 * getMaxParallel(casingTier));
            } else if (isChemicalDryerMode()) {
                return (4 * getMaxParallel(casingTier));
            } else if (isVacuumDryingMode()) {
                return (2 * getMaxParallel(casingTier));
            } else if (isVacuumFreezerMode()) {
                return getMaxParallel(casingTier);
            } else if (isCryogenicReactorMode()) {
                return (2 * getMaxParallel(casingTier));
            } else if (isGasCollectorMode()) {
                return (2 * getMaxParallel(casingTier));
            } else { // Plasma Condenser
                return getMaxParallel(casingTier);
            }
        }

        @Override
        public void setMaxProgress(int maxProgress) {
            if (isCompressorMode()) {
                int maxProgressCompressor = maxProgress / (casingTier + 6);
                super.setMaxProgress(maxProgressCompressor);
            } else if (isVacuumChamberMode()) {
                int maxProgressVacuumChamber = maxProgress / (casingTier + 4);
                super.setMaxProgress(maxProgressVacuumChamber);
            } else if (isChemicalDryerMode()) {
                int maxProgressChemicalDryer = maxProgress / (casingTier + 4);
                super.setMaxProgress(maxProgressChemicalDryer);
            } else if (isVacuumDryingMode()) {
                int maxProgressVacuumDrying = maxProgress / (casingTier + 2);
                super.setMaxProgress(maxProgressVacuumDrying);
            } else if (isVacuumFreezerMode()) {
                int maxProgressVacuumFreezer = maxProgress / (casingTier + 1);
                super.setMaxProgress(maxProgressVacuumFreezer);
            } else if (isCryogenicReactorMode()) {
                int maxProgressCryogenicReactor = maxProgress / (casingTier + 2);
                super.setMaxProgress(maxProgressCryogenicReactor);
            } else if (isGasCollectorMode()) {
                int maxProgressGasCollector = maxProgress / (casingTier + 2);
                super.setMaxProgress(maxProgressGasCollector);
            } else if (isPlasmaCondenserMode()) {
                int maxProgressPlasmaCondenser = maxProgress / (casingTier + 1);
                super.setMaxProgress(maxProgressPlasmaCondenser);
            } else {
                super.setMaxProgress(maxProgress);
            }
        }

        @Override
        protected double getOverclockingDurationDivisor() {
            if(isPlasmaCondenserMode() || isCryogenicReactorMode())
                return 4;
            return 2;
        }
    }
}
