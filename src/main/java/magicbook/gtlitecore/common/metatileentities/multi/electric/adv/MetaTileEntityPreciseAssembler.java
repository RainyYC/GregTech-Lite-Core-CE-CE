package magicbook.gtlitecore.common.metatileentities.multi.electric.adv;

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
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import magicbook.gtlitecore.api.GTLiteAPI;
import magicbook.gtlitecore.api.block.impl.WrappedIntTier;
import magicbook.gtlitecore.api.capability.GTLiteDataCode;
import magicbook.gtlitecore.api.pattern.GTLiteTraceabilityPredicate;
import magicbook.gtlitecore.api.capability.impl.AdvancedRecipeLogic;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.recipe.properties.AssemblyCasingTierProperty;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.api.utils.GTLiteUtils;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.metatileentities.GTLiteMetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static gregtech.api.GTValues.LV;
import static gregtech.api.GTValues.LuV;

/**
 * Precise Assembler MT-3662
 *
 * @author Glodblock (original author)
 *
 * <p>
 *     Thanks my friend Gate Guardian help me fix my port of GoodGenerator's machine.
 * </p>
 *
 * @since 2.7.4-beta
 */
public class MetaTileEntityPreciseAssembler extends MultiMapMultiblockController {

    private int CasingTier;
    private int InternalCasingTier;
    private int tier;
    private static boolean init = false;
    private static List<IBlockState> finalListCasing;
    private static List<IBlockState> finalListInternalCasing;

    public MetaTileEntityPreciseAssembler(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.ASSEMBLER_RECIPES,
                GTLiteRecipeMaps.PRECISE_ASSEMBLER_RECIPES
        });
        this.recipeMapWorkable = new PreciseAssemblerRecipeLogic(this);
        initMap();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityPreciseAssembler(metaTileEntityId);
    }

    private void initMap() {
        if (init) return;

        List<IBlockState> ListCasing = GTLiteAPI.MAP_PA_CASING.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> ((WrappedIntTier) entry.getValue()).getIntTier()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<IBlockState> ListInternalCasing = GTLiteAPI.MAP_PA_INTERNAL_CASING.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> ((WrappedIntTier) entry.getValue()).getIntTier()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int maxLeng = GTLiteUtils.maxLength(new ArrayList<List<IBlockState>>() {{
            add(ListCasing);
            add(ListInternalCasing);
        }});

        finalListCasing = GTLiteUtils.consistentList(ListCasing, maxLeng);
        finalListInternalCasing = GTLiteUtils.consistentList(ListInternalCasing, maxLeng);

        init = true;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object CasingTier = context.get("PACasingTieredStats");
        Object InternalCasingTier = context.get("PAInternalCasingTieredStats");
        this.CasingTier = GTLiteUtils.getOrDefault(
                () -> CasingTier instanceof WrappedIntTier,
                () -> ((WrappedIntTier) CasingTier).getIntTier(), 0);
        this.InternalCasingTier = GTLiteUtils.getOrDefault(
                () -> InternalCasingTier instanceof WrappedIntTier,
                () -> ((WrappedIntTier) InternalCasingTier).getIntTier(), 0);
        this.tier = this.CasingTier;
        this.writeCustomData(GTLiteDataCode.ChannelPreciseAssembler1, buf -> buf.writeInt(this.CasingTier));
    }

    /**
     * @return This machine has a special property, if casing tier in multiblock structure is greater than or equal to recipe needs casing tier, then run recipe.
     */
    @Override
    public boolean checkRecipe(@Nonnull Recipe recipe,
                               boolean consumeIfSuccess) {
        return super.checkRecipe(recipe, consumeIfSuccess) && recipe.getProperty(AssemblyCasingTierProperty.getInstance(), 0) <= tier;
    }

    @Override
    public void update() {
        super.update();
        if (this.getWorld().isRemote && this.CasingTier == 0) {
            this.writeCustomData(GTLiteDataCode.ChannelPreciseAssembler2, buf -> {});
        }
    }

    @Nonnull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("DDDDDDDDD", "F       F", "F       F", "F       F", "DDDDDDDDD")
                .aisle("DMMMMMMMD", "DGGGGGGGD", "DGGGGGGGD", "DGGGGGGGD", "DDDDDDDDD")
                .aisle("DMMMMMMMD", "D       D", "D       D", "D       D", "DDDDODDDD")
                .aisle("DMMMMMMMD", "DGGGGGGGD", "DGGGGGGGD", "DGGGGGGGD", "DDDDDDDDD")
                .aisle("DDDDSDDDD", "F       F", "F       F", "F       F", "DDDDDDDDD")
                .where('S', this.selfPredicate())
                .where('D', GTLiteTraceabilityPredicate.PA_CASING.get()
                        .setMinGlobalLimited(42)
                        .or(autoAbilities(true, true, true, true, true, true, false)))
                .where('F', states(getFrameState()))
                .where('G', states(getGlassState()))
                .where('O', abilities(MultiblockAbility.MUFFLER_HATCH))
                .where('M', GTLiteTraceabilityPredicate.PA_INTERNAL_CASING.get())
                .build();
    }

    private static IBlockState getFrameState() {
        return MetaBlocks.FRAMES.get(GTLiteMaterials.MARM200Steel).getBlock(GTLiteMaterials.MARM200Steel);
    }

    private static IBlockState getGlassState() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.LAMINATED_GLASS);
    }

    @Override
    public boolean hasMufflerMechanics() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        if (this.CasingTier <= 3) {
            return Textures.FUSION_REACTOR_OVERLAY;
        } else {
            return GTLiteTextures.PRECISE_ASSEMBLER_OVERLAY;
        }
    }

    @SuppressWarnings("all")
    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return switch (this.CasingTier) {
            case (1) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK1;
            case (2) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK2;
            case (3) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK3;
            case (4) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK4;
            case (5) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK5;
            case (6) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK6;
            case (7) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK7;
            case (8) -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK8;
            default -> GTLiteTextures.PRECISE_ASSEMBLER_CASING_MK1;
        };
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = null;
        if (Blocks.AIR != null) {
            builder = MultiblockShapeInfo.builder()
                    .aisle("ETCCCCCCC", "F       F", "F       F", "F       F", "XYZCCCCCC")
                    .aisle("CMMMMMMMC", "CGGGGGGGC", "CGGGGGGGC", "CGGGGGGGC", "CCCCCCCCC")
                    .aisle("CMMMMMMMC", "C       C", "C       C", "C       C", "CCCCOCCCC")
                    .aisle("CMMMMMMMC", "CGGGGGGGC", "CGGGGGGGC", "CGGGGGGGC", "CCCCCCCCC")
                    .aisle("CCCCSCCCC", "F       F", "F       F", "F       F", "CCCCCCCCC")
                    .where('S', GTLiteMetaTileEntities.PRECISE_ASSEMBLER, EnumFacing.SOUTH)
                    .where('X', MetaTileEntities.ITEM_IMPORT_BUS[LuV], EnumFacing.NORTH)
                    .where('Y', MetaTileEntities.ITEM_EXPORT_BUS[LuV], EnumFacing.NORTH)
                    .where('Z', MetaTileEntities.FLUID_IMPORT_HATCH[LuV], EnumFacing.NORTH)
                    .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[LuV], EnumFacing.NORTH)
                    .where('T', () -> ConfigHolder.machines.enableMaintenance ? MetaTileEntities.MAINTENANCE_HATCH : MetaTileEntities.ENERGY_INPUT_HATCH[LuV], EnumFacing.NORTH)
                    .where('O', MetaTileEntities.MUFFLER_HATCH[LV], EnumFacing.UP)
                    .where('G', getGlassState())
                    .where('F', getFrameState())
                    .where(' ', Blocks.AIR.getDefaultState());
        }
        MultiblockShapeInfo.Builder finalBuilder = builder;
        AtomicInteger count = new AtomicInteger();
        finalListCasing.stream()
                .map(b -> {
                    if (finalBuilder != null) {
                        finalBuilder.where('C', b);
                        finalBuilder.where('M', finalListInternalCasing.get(count.get()));
                        count.getAndIncrement();
                    }
                    return finalBuilder;
                })
                .filter(Objects::nonNull)
                .forEach(b -> shapeInfo.add(b.build()));
        return shapeInfo;
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == GTLiteDataCode.ChannelPreciseAssembler1) {
            this.CasingTier = buf.readInt();
        }
        if (dataId == GTLiteDataCode.ChannelPreciseAssembler2) {
            this.writeCustomData(GTLiteDataCode.ChannelPreciseAssembler1, buf1 -> buf1.writeInt(this.CasingTier));
        }
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeInt(this.CasingTier);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.CasingTier = buf.readInt();
    }

    @Override
    public void addInformation(ItemStack stack,
                               @Nullable World player,
                               @Nonnull List<String> tooltip,
                               boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.RAINBOW + I18n.format("gtlitecore.universal.tooltip.async_recipe"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.2"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.3"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.4"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.5"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.6"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.7"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.8"));
        tooltip.add(I18n.format("gtlitecore.machine.precise_assembler.tooltip.9"));
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    /**
     * Getter of Casing Tier.
     *
     * @return  Main Casing Tier (Precise Assembly Casing).
     */
    public int getCasingTier() {
        return this.CasingTier;
    }

    protected class PreciseAssemblerRecipeLogic extends AdvancedRecipeLogic {
        public PreciseAssemblerRecipeLogic(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        /**
         * @return Check if machine in Precise Assembler mode.
         */
        private boolean isPrecise() {
            return this.getRecipeMap() == GTLiteRecipeMaps.PRECISE_ASSEMBLER_RECIPES;
        }

        /**
         * @param maxProgress If machine in common assembler, then get 1/2 progress time.
         */
        @Override
        public void setMaxProgress(int maxProgress) {
            if (isPrecise()) {
                if (getCasingTier() > 3) {
                    this.maxProgressTime = (int) Math.floor(maxProgress * Math.pow(0.8, tier));
                } else {
                    this.maxProgressTime = maxProgress;
                }
            } else {
                if (getCasingTier() > 3) {
                    this.maxProgressTime = (int) Math.floor(maxProgress * Math.pow(0.8, tier));
                } else {
                    this.maxProgressTime = maxProgress / 2;
                }
            }
        }

        /**
         * @return If machine in Precise Assembler mode, then no parallel.
         *         If machine in common assembler, then get 2^{CasingTier + 4}.
         *         Max Parallel: 4096 (Mk8 casing).
         */
        @Override
        public int getParallelLimit() {
            if (isPrecise()) {
                if (getCasingTier() > 3) {
                    return (int) Math.pow(2, CasingTier + 4);
                } else {
                    return 1;
                }
            } else {
                return (int) Math.pow(2, CasingTier + 4);
            }
        }

        @Override
        public boolean isAllowRecipeAsync() {
            return true;
        }
    }
}