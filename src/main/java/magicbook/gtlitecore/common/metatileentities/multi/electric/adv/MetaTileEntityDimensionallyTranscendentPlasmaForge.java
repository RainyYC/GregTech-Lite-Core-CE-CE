package magicbook.gtlitecore.common.metatileentities.multi.electric.adv;

import gregicality.multiblocks.api.recipes.GCYMRecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IHeatingCoil;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.HeatingCoilRecipeLogic;
import gregtech.api.capability.impl.ItemHandlerList;
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
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.recipeproperties.TemperatureProperty;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockComputerCasing;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MetaTileEntityDimensionallyTranscendentPlasmaForge extends MultiMapMultiblockController implements IHeatingCoil {

    private int blastFurnaceTemperature;
    protected int heatingCoilLevel;
    protected int coilTier;
    private static boolean init = false;
    private static List<IBlockState> finalListCoil;

    public MetaTileEntityDimensionallyTranscendentPlasmaForge(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.FURNACE_RECIPES,
                RecipeMaps.ARC_FURNACE_RECIPES,
                RecipeMaps.BLAST_RECIPES,
                GTLiteRecipeMaps.INDUSTRIAL_ROASTER_RECIPES,
                GTLiteRecipeMaps.BURNER_REACTOR_RECIPES,
                RecipeMaps.ALLOY_SMELTER_RECIPES,
                GCYMRecipeMaps.ALLOY_BLAST_RECIPES,
                GTLiteRecipeMaps.STELLAR_FURNACE_RECIPES,
        });
        this.recipeMapWorkable = new DTPFRecipeLogic(this);
        initMap();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDimensionallyTranscendentPlasmaForge(metaTileEntityId);
    }

    private void initMap() {
        if (init) return;

        List<IBlockState> listCoil = GregTechAPI.HEATING_COILS.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().getTier()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int maxLeng = GTLiteUtils.maxLength(new ArrayList<List<IBlockState>>() {{
            add(listCoil);
        }});
        finalListCoil = GTLiteUtils.consistentList(listCoil, maxLeng);

        init = true;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object coilType = context.get("CoilType");
        if (coilType instanceof IHeatingCoilBlockStats) {
            this.blastFurnaceTemperature = ((IHeatingCoilBlockStats) coilType).getCoilTemperature();
            this.heatingCoilLevel = ((IHeatingCoilBlockStats) coilType).getLevel();
            this.coilTier = ((IHeatingCoilBlockStats) coilType).getTier();
        } else {
            this.blastFurnaceTemperature = BlockWireCoil.CoilType.CUPRONICKEL.getCoilTemperature();
            this.heatingCoilLevel = BlockWireCoil.CoilType.CUPRONICKEL.getLevel();
            this.coilTier = BlockWireCoil.CoilType.CUPRONICKEL.getTier();
        }

        this.blastFurnaceTemperature += 100 * Math.max(0, GTUtility.getTierByVoltage(getEnergyContainer().getInputVoltage()) - GTValues.MV);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        blastFurnaceTemperature = 0;
        heatingCoilLevel = 0;
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

    @Override
    public boolean checkRecipe(@Nonnull Recipe recipe,
                               boolean consumeIfSuccess) {
        return this.blastFurnaceTemperature >= recipe.getProperty(TemperatureProperty.getInstance(), 0);
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle(" NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN   N     N   NNN   NNN " ,"         N   N     N   N         " ,"         N   N     N   N         " ,"                                 " ,"                                 " ,"                                 " ,"         N   N     N   N         " ,"         N   N     N   N         " ,"         N   N     N   N         " ,"                                 ")
                .aisle("NbbbN NbbbN    N N    NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ,"NbbbN NbbbN           NbbbN NbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbN NbbbN           NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC   N     N   CCC   CCC " ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         " ,"                                 " ,"         N   N     N   N         " ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ,"NbbbN NbbbN           NbbbN NbbbN" ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"  s     s               s     s  " ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"NbbbN NbbbN           NbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC   N     N   CbC   CbC " ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"  N     sbbbbbNNsNNbbbbbs     N  " ,"  N      bCCCb     bCCCb      N  " ,"  N      N   N     N   N      N  " ,"   s                         s   " ,"   s     N   N     N   N     s   " ,"    ss   bCCCb     bCCCb   ss    " ,"      NNNbbbbbNNsNNbbbbbNNN      " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ,"NbbbNNNbbbN           NbbbNNNbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbNNNbbbN           NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC   N     N   CCCCCCCCC " ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         NCCCN     NCCCN         " ,"  s      NCCCN     NCCCN      s  " ,"  s      NCCCN     NCCCN      s  " ,"         bCCCb     bCCCb         " ,"    ss   bCCCb     bCCCb   ss    " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle(" NNN   NNN     NbN     NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NN    N     N    NN   NNN " ,"         N   N     N   N         " ,"         NCCCN     NCCCN         " ,"                                 " ,"                                 " ,"                                 " ,"  s      NCCCN     NCCCN      s  " ,"   s     N   N     N   N     s   " ,"         N   N     N   N         " ,"                                 ")
                .aisle("   N   N       NbN       N   N   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   N   N                 N   N   " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"   N   N                 N   N   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   N   N                 N   N   " ,"                                 " ,"         NCCCN     NCCCN         " ,"                                 " ,"                                 " ,"                                 " ,"  s      NCCCN     NCCCN      s  " ,"   s                         s   " ,"                                 " ,"                                 ")
                .aisle(" NNN   NNN     NbN     NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NN    N     N    NN   NNN " ,"         N   N     N   N         " ,"         NCCCN     NCCCN         " ,"                                 " ,"                                 " ,"                                 " ,"         NCCCN     NCCCN         " ,"  N      N   N     N   N      N  " ,"         N   N     N   N         " ,"                                 ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ,"NbbbNNNbbbN           NbbbNNNbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbNNNbbbN           NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC   N     N   CCCCCCCCC " ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         NCCCN     NCCCN         " ,"         NCCCN     NCCCN         " ,"         NCCCN     NCCCN         " ,"         bCCCb     bCCCb         " ,"  N      bCCCb     bCCCb      N  " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ,"NbbbN NbbbN           NbbbN NbbbN" ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"  s     s               s     s  " ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"NbbbN NbbbN           NbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC   N     N   CbC   CbC " ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"  s     sbbbbbNNsNNbbbbbs     s  " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         " ,"                                 " ,"         N   N     N   N         " ,"         bCCCb     bCCCb         " ,"  N     sbbbbbNNsNNbbbbbs     N  " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbN NbbbN    NbN    NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ,"NbbbN NbbbN           NbbbN NbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbN NbbbN           NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC   N     N   CCC   CCC " ,"NNNN   NNNCCCb     bCCCNNN   NNNN" ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ," NNN   NNN   N     N   NNN   NNN " ,"   N   N                 N   N   " ," NNN   NNN   N     N   NNN   NNN " ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ," NNN   NNN   N     N   NNN   NNN ")
                .aisle(" NNN   NNN     NbN     NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," CCC   CCC   N     N   CCC   CCC " ," CbC   CbC   N     N   CbC   CbC " ," CCCCCCCCC   N     N   CCCCCCCCC " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," CCCCCCCCC   N     N   CCCCCCCCC " ," CbC   CbC   N     N   CbC   CbC " ," CCC   CCC   N     N   CCC   CCC " ,"                                 ")
                .aisle("  N     N      NbN      N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," CCC   CCC             CCC   CCC " ," CbC   CbC             CbC   CbC " ," CCCCCCCCC             CCCCCCCCC " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," CCCCCCCCC             CCCCCCCCC " ," CbC   CbC             CbC   CbC " ," CCC   CCC             CCC   CCC " ,"                                 ")
                .aisle("  N     N      NbN      N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," CCC   CCC             CCC   CCC " ," CbC   CbC             CbC   CbC " ," CCCCCCCCC             CCCCCCCCC " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," CCCCCCCCC             CCCCCCCCC " ," CbC   CbC             CbC   CbC " ," CCC   CCC             CCC   CCC " ,"                                 ")
                .aisle("  N     N     NsNsN     N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"NbbbN NbbbN           NbbbN NbbbN" ,"NbbbN NbbbN           NbbbN NbbbN" ,"NbbbNNNbbbN           NbbbNNNbbbN" ," NNN   NNN             NNN   NNN " ,"   N   N                 N   N   " ," NNN   NNN             NNN   NNN " ,"NbbbNNNbbbN           NbbbNNNbbbN" ,"NbbbN NbbbN           NbbbN NbbbN" ,"NbbbN NbbbN           NbbbN NbbbN" ," NNN   NNN             NNN   NNN ")
                .aisle("  N     N    NbbbbbN    N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 ")
                .aisle(" NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN " ,"                N                " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 ")
                .aisle("  NbbbbbNbbbbNbbbbbNbbbbNbbbbbN  " ,"               NNN               " ,"                ~                " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  s     s               s     s  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  s     s               s     s  " ,"                                 " ,"                                 ")
                .aisle(" NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN " ,"                N                " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 ")
                .aisle("  N     N    NbbbbbN    N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"  N     N               N     N  " ,"                                 " ,"                                 ")
                .aisle("  N     N     NsNsN     N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"NbbbN NbbbN           NbbbN NbbbN" ,"NbbbN NbbbN           NbbbN NbbbN" ,"NbbbNNNbbbN           NbbbNNNbbbN" ," NNN   NNN             NNN   NNN " ,"   N   N                 N   N   " ," NNN   NNN             NNN   NNN " ,"NbbbNNNbbbN           NbbbNNNbbbN" ,"NbbbN NbbbN           NbbbN NbbbN" ,"NbbbN NbbbN           NbbbN NbbbN" ," NNN   NNN             NNN   NNN ")
                .aisle("  N     N      NbN      N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," CCC   CCC             CCC   CCC " ," CbC   CbC             CbC   CbC " ," CCCCCCCCC             CCCCCCCCC " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," CCCCCCCCC             CCCCCCCCC " ," CbC   CbC             CbC   CbC " ," CCC   CCC             CCC   CCC " ,"                                 ")
                .aisle("  N     N      NbN      N     N  " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," CCC   CCC             CCC   CCC " ," CbC   CbC             CbC   CbC " ," CCCCCCCCC             CCCCCCCCC " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," CCCCCCCCC             CCCCCCCCC " ," CbC   CbC             CbC   CbC " ," CCC   CCC             CCC   CCC " ,"                                 ")
                .aisle(" NNN   NNN     NbN     NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," CCC   CCC   N     N   CCC   CCC " ," CbC   CbC   N     N   CbC   CbC " ," CCCCCCCCC   N     N   CCCCCCCCC " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," CCCCCCCCC   N     N   CCCCCCCCC " ," CbC   CbC   N     N   CbC   CbC " ," CCC   CCC   N     N   CCC   CCC " ,"                                 ")
                .aisle("NbbbN NbbbN    NbN    NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ,"NbbbN NbbbN           NbbbN NbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbN NbbbN           NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC   N     N   CCC   CCC " ,"NNNN   NNNCCCb     bCCCNNN   NNNN" ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ," NNN   NNN   N     N   NNN   NNN " ,"   N   N                 N   N   " ," NNN   NNN   N     N   NNN   NNN " ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ," NNN   NNN   N     N   NNN   NNN ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ,"NbbbN NbbbN           NbbbN NbbbN" ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"  s     s               s     s  " ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"NbbbN NbbbN           NbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC   N     N   CbC   CbC " ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"  s     sbbbbbNNsNNbbbbbs     s  " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         " ,"                                 " ,"         N   N     N   N         " ,"         bCCCb     bCCCb         " ,"  N     sbbbbbNNsNNbbbbbs     N  " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ,"NbbbNNNbbbN           NbbbNNNbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbNNNbbbN           NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC   N     N   CCCCCCCCC " ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         NCCCN     NCCCN         " ,"         NCCCN     NCCCN         " ,"         NCCCN     NCCCN         " ,"         bCCCb     bCCCb         " ,"  N      bCCCb     bCCCb      N  " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle(" NNN   NNN     NbN     NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NN    N     N    NN   NNN " ,"         N   N     N   N         " ,"         NCCCN     NCCCN         " ,"                                 " ,"                                 " ,"                                 " ,"         NCCCN     NCCCN         " ,"  N      N   N     N   N      N  " ,"         N   N     N   N         " ,"                                 ")
                .aisle("   N   N       NbN       N   N   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   N   N                 N   N   " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"   N   N                 N   N   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   N   N                 N   N   " ,"                                 " ,"         NCCCN     NCCCN         " ,"                                 " ,"                                 " ,"                                 " ,"  s      NCCCN     NCCCN      s  " ,"   s                         s   " ,"                                 " ,"                                 ")
                .aisle(" NNN   NNN     NbN     NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ,"   C   C                 C   C   " ," NNN   NN    N     N    NN   NNN " ,"         N   N     N   N         " ,"         NCCCN     NCCCN         " ,"                                 " ,"                                 " ,"                                 " ,"  s      NCCCN     NCCCN      s  " ,"   s     N   N     N   N     s   " ,"         N   N     N   N         " ,"                                 ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ,"NbbbNNNbbbN           NbbbNNNbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbNNNbbbN           NbbbNNNbbbN" ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC             CCCCCCCCC " ," CCCCCCCCC   N     N   CCCCCCCCC " ,"NbbbNNNbbNCCCb     bCCCNbbNNNbbbN" ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         NCCCN     NCCCN         " ,"  s      NCCCN     NCCCN      s  " ,"  s      NCCCN     NCCCN      s  " ,"         bCCCb     bCCCb         " ,"    ss   bCCCb     bCCCb   ss    " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ,"NbbbN NbbbN           NbbbN NbbbN" ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"  s     s               s     s  " ," NNN   NNN             NNN   NNN " ," NNN   NNN             NNN   NNN " ,"NbbbN NbbbN           NbbbN NbbbN" ," CbC   CbC             CbC   CbC " ," CbC   CbC             CbC   CbC " ," CbC   CbC   N     N   CbC   CbC " ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"  N     sbbbbbNNsNNbbbbbs     N  " ,"  N      bCCCb     bCCCb      N  " ,"  N      N   N     N   N      N  " ,"   s                         s   " ,"   s     N   N     N   N     s   " ,"    ss   bCCCb     bCCCb   ss    " ,"      NNNbbbbbNNsNNbbbbbNNN      " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle("NbbbN NbbbN    N N    NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ,"NbbbN NbbbN           NbbbN NbbbN" ,"  N     N               N     N  " ,"  N     N               N     N  " ,"                                 " ,"  N     N               N     N  " ,"  N     N               N     N  " ,"NbbbN NbbbN           NbbbN NbbbN" ," CCC   CCC             CCC   CCC " ," CCC   CCC             CCC   CCC " ," CCC   CCC   N     N   CCC   CCC " ,"NbbbN NbbNCCCb     bCCCNbbN NbbbN" ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         " ,"                                 " ,"         N   N     N   N         " ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         bCCCb     bCCCb         " ,"         N   N     N   N         ")
                .aisle(" NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN             NNN   NNN " ,"                                 " ,"                                 " ,"                                 " ," NNN   NNN   N     N   NNN   NNN " ,"         N   N     N   N         " ,"         N   N     N   N         " ,"                                 " ,"                                 " ,"                                 " ,"         N   N     N   N         " ,"         N   N     N   N         " ,"         N   N     N   N         " ,"                                 ")
                .where('~', this.selfPredicate())
                .where('C', heatingCoils())
                .where('b', states(getCasingState())
                        .setMinGlobalLimited(1100)
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH)
                                .setExactLimit(1))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMaxGlobalLimited(3))
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.IMPORT_ITEMS)
                                .setMaxGlobalLimited(32)
                                .setPreviewCount(1))
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS)
                                .setMaxGlobalLimited(32))
                        .or(abilities(MultiblockAbility.IMPORT_FLUIDS)
                                .setMaxGlobalLimited(32)
                                .setPreviewCount(1))
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)
                                .setMaxGlobalLimited(32)
                                .setPreviewCount(1)))
                .where('N', states(getSecondCasingState()))
                .where('s', states(getThirdCasingState()))
                .where(' ', any())
                .build();
    }

    private static IBlockState getCasingState() {
        return MetaBlocks.COMPUTER_CASING.getState(BlockComputerCasing.CasingType.HIGH_POWER_CASING);
    }

    private static IBlockState getSecondCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.ULTIMATE_HIGH_ENERGY_CASING);
    }

    private static IBlockState getThirdCasingState() {
        return GTLiteMetaBlocks.SCIENCE_CASING.getState(BlockScienceCasing.ScienceCasingType.DIMENSIONAL_BRIDGE_CASING);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return iMultiblockPart == null ? GTLiteTextures.DIMENSIONAL_BRIDGE_CASING : Textures.HIGH_POWER_CASING;
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
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @Nonnull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.BLINKING_RED + I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.4"));
        tooltip.add(I18n.format("gregtech.machine.electric_blast_furnace.tooltip.1"));
        tooltip.add(I18n.format("gregtech.machine.electric_blast_furnace.tooltip.2"));
        tooltip.add(I18n.format("gregtech.machine.electric_blast_furnace.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.7"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.8"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.9"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.10"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.11"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.12"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.13"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.14"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.15"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.16"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.17"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.18"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.19"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.20"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.21"));
        tooltip.add(I18n.format("gtlitecore.machine.dimensionally_transcendent_plasma_forge.tooltip.22"));
        tooltip.add(I18n.format("gtlitecore.universal.tooltip.laser_input"));
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed()) {
            IEnergyContainer energyContainer = this.recipeMapWorkable.getEnergyContainer();
            if (energyContainer != null && energyContainer.getEnergyCapacity() > 0L) {
                textList.add(2, new TextComponentTranslation("gregtech.multiblock.blast_furnace.max_temperature", blastFurnaceTemperature)
                        .setStyle(new Style().setColor(TextFormatting.RED)));
            }
        }
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle(" NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN   N     N   NNN   NNN ", "         N   N     N   N         ", "         N   N     N   N         ", "                                 ", "                                 ", "                                 ", "         N   N     N   N         ", "         N   N     N   N         ", "         N   N     N   N         ", "                                 ")
                .aisle("NbbbN NbbbN    N N    NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   N     N   CCC   CCC ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   N     N   CbC   CbC ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "  N     sbbbbbNNsNNbbbbbs     N  ", "  N      bCCCb     bCCCb      N  ", "  N      N   N     N   N      N  ", "   s                         s   ", "   s     N   N     N   N     s   ", "    ss   bCCCb     bCCCb   ss    ", "      NNNbbbbbNNsNNbbbbbNNN      ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ", "  s      NCCCN     NCCCN      s  ", "  s      NCCCN     NCCCN      s  ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle(" NNN   NNN     NbN     NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NN    N     N    NN   NNN ", "         N   N     N   N         ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "  s      NCCCN     NCCCN      s  ", "   s     N   N     N   N     s   ", "         N   N     N   N         ", "                                 ")
                .aisle("   N   N       NbN       N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N                 N   N   ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "   N   N                 N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N                 N   N   ", "                                 ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "  s      NCCCN     NCCCN      s  ", "   s                         s   ", "                                 ", "                                 ")
                .aisle(" NNN   NNN     NbN     NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NN    N     N    NN   NNN ", "         N   N     N   N         ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "  N      N   N     N   N      N  ", "         N   N     N   N         ", "                                 ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   N     N   CbC   CbC ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "  s     sbbbbbNNsNNbbbbbs     s  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbN NbbbN    NbN    NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   N     N   CCC   CCC ", "NNNN   NNNCCCb     bCCCNNN   NNNN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " NNN   NNN   N     N   NNN   NNN ")
                .aisle(" NNN   NNN     NbN     NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ", " CCC   CCC   N     N   CCC   CCC ", "                                 ")
                .aisle("  N     N      NbN      N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
                .aisle("  N     N      NbN      N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
                .aisle("  N     N     NsNsN     N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ", "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ")
                .aisle("  N     N    NbbbbbN    N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ")
                .aisle(" NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ", "                N                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ")
                .aisle("  NbbbbbNbbbbNbbbbbNbbbbNbbbbbN  ", "               NNN               ", "                ~                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  s     s               s     s  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  s     s               s     s  ", "                                 ", "                                 ")
                .aisle(" NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ", "                N                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ")
                .aisle("  N     N    NbIXJbN    N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ")
                .aisle("  N     N     NsNsN     N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ", "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ")
                .aisle("  N     N      NKN      N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
                .aisle("  N     N      NLN      N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
                .aisle(" NNN   NNN     NEN     NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ", " CCC   CCC   N     N   CCC   CCC ", "                                 ")
                .aisle("NbbbN NbbbN    NbN    NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   N     N   CCC   CCC ", "NNNN   NNNCCCb     bCCCNNN   NNNN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " NNN   NNN   N     N   NNN   NNN ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   N     N   CbC   CbC ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "  s     sbbbbbNNsNNbbbbbs     s  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle(" NNN   NNN     NbN     NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NN    N     N    NN   NNN ", "         N   N     N   N         ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "  N      N   N     N   N      N  ", "         N   N     N   N         ", "                                 ")
                .aisle("   N   N       NbN       N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N                 N   N   ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "   N   N                 N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N                 N   N   ", "                                 ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "  s      NCCCN     NCCCN      s  ", "   s                         s   ", "                                 ", "                                 ")
                .aisle(" NNN   NNN     NbN     NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NN    N     N    NN   NNN ", "         N   N     N   N         ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "  s      NCCCN     NCCCN      s  ", "   s     N   N     N   N     s   ", "         N   N     N   N         ", "                                 ")
                .aisle("NbbbNNNbbbN    NbN    NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ", "  s      NCCCN     NCCCN      s  ", "  s      NCCCN     NCCCN      s  ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   N     N   CbC   CbC ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "  N     sbbbbbNNsNNbbbbbs     N  ", "  N      bCCCb     bCCCb      N  ", "  N      N   N     N   N      N  ", "   s                         s   ", "   s     N   N     N   N     s   ", "    ss   bCCCb     bCCCb   ss    ", "      NNNbbbbbNNsNNbbbbbNNN      ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle("NbbbN NbbbN    N N    NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   N     N   CCC   CCC ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         N   N     N   N         ")
                .aisle(" NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN   N     N   NNN   NNN ", "         N   N     N   N         ", "         N   N     N   N         ", "                                 ", "                                 ", "                                 ", "         N   N     N   N         ", "         N   N     N   N         ", "         N   N     N   N         ", "                                 ")
                .where('~', GTLiteMetaTileEntities.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE, EnumFacing.SOUTH)
                .where('b', getCasingState())
                .where('X', MetaTileEntities.AUTO_MAINTENANCE_HATCH, EnumFacing.UP)
                .where('I', MetaTileEntities.ITEM_IMPORT_BUS_ME, EnumFacing.UP)
                .where('J', MetaTileEntities.ITEM_EXPORT_BUS_ME, EnumFacing.UP)
                .where('K', MetaTileEntities.FLUID_IMPORT_HATCH_ME, EnumFacing.UP)
                .where('L', MetaTileEntities.FLUID_EXPORT_HATCH_ME, EnumFacing.UP)
                .where('E', MetaTileEntities.LASER_INPUT_HATCH_256[5], EnumFacing.UP)
                .where('N', getSecondCasingState())
                .where('s', getThirdCasingState());
        AtomicInteger count = new AtomicInteger();
        finalListCoil.stream()
                .map(b -> {
                    if (builder != null) {
                        builder.where('C', b);
                        count.getAndIncrement();
                    }
                    return builder;
                })
                .filter(Objects::nonNull)
                .forEach(b -> shapeInfo.add(b.build()));
        return shapeInfo;
    }

    @Override
    public int getCurrentTemperature() {
        return this.blastFurnaceTemperature;
    }

    /**  getMaxParallel(int heatingCoilLevel)
     *     @param heatingCoilLevel the level to get the parallel for
     *     @return the max parallel for the heating coil level
     */
    public static int getMaxParallel(int heatingCoilLevel) {
        return heatingCoilLevel * 256;
    }

    protected class DTPFRecipeLogic extends AdvancedRecipeLogic {

        public DTPFRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        /**
         * @return Check if machine in Electric Furnace mode.
         */
        private boolean isFurnaceMode() {
            return this.getRecipeMap() == RecipeMaps.FURNACE_RECIPES;
        }

        /**
         * @return Check if machine in Arc Furnace mode.
         */
        private boolean isArcFurnaceMode() {
            return this.getRecipeMap() == RecipeMaps.ARC_FURNACE_RECIPES;
        }

        /**
         * @return Check if machine in Electric Blast Furnace mode.
         */
        private boolean isEBFMode() {
            return this.getRecipeMap() == RecipeMaps.BLAST_RECIPES;
        }

        /**
         * @return Check if machine in Industrial Roaster mode.
         */
        private boolean isRoasterMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.INDUSTRIAL_ROASTER_RECIPES;
        }

        /**
         * @return Check if machine in Burner Reactor mode.
         */
        private boolean isBurnerReactorMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.BURNER_REACTOR_RECIPES;
        }

        /**
         * @return Check if machine in Alloy Smelter mode.
         */
        private boolean isAlloySmelterMode() {
            return this.getRecipeMap() == RecipeMaps.ALLOY_SMELTER_RECIPES;
        }

        /**
         * @return Check if machine in Alloy Blast Smelter mode.
         */
        private boolean isABSMode() {
            return this.getRecipeMap() == GCYMRecipeMaps.ALLOY_BLAST_RECIPES;
        }

        /**
         * @return Check if machine in Stellar Furnace mode.
         */
        private boolean isStellarFurnaceMode() {
            return this.getRecipeMap() == GTLiteRecipeMaps.STELLAR_FURNACE_RECIPES;
        }


        @Override
        public int getParallelLimit() {
            if (isFurnaceMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 32768));
            } else if (isArcFurnaceMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 16384));
            } else if (isEBFMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 4096));
            } else if (isRoasterMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 8192));
            } else if (isBurnerReactorMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 8192));
            } else if (isAlloySmelterMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 16384));
            } else if (isABSMode()) {
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 4096));
            } else { // Stellar Furnace
                return (int) (Math.min(getMaxVoltage() * getMaxParallel(heatingCoilLevel), 256));
            }
        }

        /**
         * @param maxProgress In some special mode, get correspondence progress time.
         */
        @Override
        public void setMaxProgress(int maxProgress) {
            if (isFurnaceMode()) {
                int maxProgressFurnace = (int) Math.floor(maxProgress * Math.pow(0.5, coilTier));
                super.setMaxProgress(maxProgressFurnace);
            } else if (isArcFurnaceMode()) {
                int maxProgressArcFurnace = (int) Math.floor(maxProgress * Math.pow(0.5, coilTier));
                super.setMaxProgress(maxProgressArcFurnace);
            } else if (isEBFMode()) {
                int maxProgressEBF = (int) Math.floor(maxProgress * Math.pow(0.8, coilTier));
                super.setMaxProgress(maxProgressEBF);
            } else if (isRoasterMode()) {
                int maxProgressRoaster = (int) Math.floor(maxProgress * Math.pow(0.6, coilTier));
                super.setMaxProgress(maxProgressRoaster);
            } else if (isBurnerReactorMode()) {
                int maxProgressBurnerReactor = (int) Math.floor(maxProgress * Math.pow(0.6, coilTier));
                super.setMaxProgress(maxProgressBurnerReactor);
            } else if (isAlloySmelterMode()) {
                int maxProgressAlloySmelter = (int) Math.floor(maxProgress * Math.pow(0.5, coilTier));
                super.setMaxProgress(maxProgressAlloySmelter);
            } else if (isABSMode()) {
                int maxProgressABS = (int) Math.floor(maxProgress * Math.pow(0.8, coilTier));
                super.setMaxProgress(maxProgressABS);
            } else if (isStellarFurnaceMode()) {
                super.setMaxProgress(maxProgress);
            } else {
                super.setMaxProgress(maxProgress);
            }
        }
    }
}
