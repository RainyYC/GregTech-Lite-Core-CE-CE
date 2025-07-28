package magicbook.gtlitecore.loaders.chains;

import gregicality.multiblocks.api.recipes.GCYMRecipeMaps;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.common.blocks.BlockComputerCasing;
import gregtech.common.blocks.BlockFusionCasing;
import gregtech.common.blocks.MetaBlocks;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.utils.Mods;

import java.util.ArrayList;

import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.metatileentities.MetaTileEntities.*;
import static magicbook.gtlitecore.api.GTLiteValues.tierList;
import static magicbook.gtlitecore.api.unification.GTLiteMaterials.*;
import static magicbook.gtlitecore.api.utils.GTLiteUtility.getMetaItemById;
import static gregtech.api.GTValues.*;
import static magicbook.gtlitecore.common.items.GTLiteMetaItems.*;
import static gregtechfoodoption.GTFOMaterialHandler.EtirpsCranberry;
import gregtechfoodoption.item.GTFOMetaItem;
import static magicbook.gtlitecore.common.metatileentities.GTLiteMetaTileEntities.*;

public class CatChain {
    public static void init() {
        // 猫薄荷增值
        GTLiteRecipeMaps.TREE_GROWTH_RECIPES.recipeBuilder()
                .notConsumable(NEPATA_CATARIA)
                .output(NEPATA_CATARIA)
                .EUt(VA[UV])
                .duration(20)
                .buildAndRegister();

        // 半稳态哈
        GCYMRecipeMaps.ALLOY_BLAST_RECIPES.recipeBuilder()
                .fluidInputs(ConcentrateDragonBreath.getFluid(1000))
                .input(NEPATA_CATARIA, 2)
                .input(dust, HastelloyX78, 13)
                .fluidOutputs(UnstableCat.getFluid(999))
                .EUt(VA[UHV])
                .duration(60 * 20)
                .blastFurnaceTemp(12800)
                .buildAndRegister();

        // 稳定哈
        RecipeMaps.FUSION_RECIPES.recipeBuilder()
                .fluidInputs(UnstableCat.getFluid(1000))
                .fluidInputs(Hafnium.getFluid(1000))
                .fluidOutputs(Cat.getFluid(143))
                .EUt(VA[UV])
                .duration(15 * 20)
                .EUToStart(650000000) // mk4
                .buildAndRegister();

        // 稳定哈固化
        RecipeMaps.FLUID_SOLIDFICATION_RECIPES.recipeBuilder()
                .fluidInputs(Cat.getFluid(144))
                .output(dust, Cat)
                .EUt(VA[UHV])
                .duration(20)
                .buildAndRegister();

        // 移除流提
        RecipeMaps.EXTRACTOR_RECIPES.findRecipeCollisions(
                new ArrayList<>(){{
                    add(OreDictUnifier.get(dust, Cat));
                }}, new ArrayList<>()
        ).forEach(RecipeMaps.EXTRACTOR_RECIPES::removeRecipe);

        // 未解明
        RecipeMaps.MIXER_RECIPES.recipeBuilder()
                .notConsumable(getMetaItemById(Mods.GregTechFoodOption.getID(), "gtfo_meta_item", 78))
                .input(dust, Cat)
                .output(UNKNOWN_CAT)
                .EUt(VA[UHV])
                .duration(1)
                .buildAndRegister();

        // 坍缩
        RecipeMaps.LASER_ENGRAVER_RECIPES.recipeBuilder()
                .notConsumable(lens, CrystalMatrix)
                .input(UNKNOWN_CAT)
                .chancedOutput(STRESS_CAT, 1, 5000, 0)
                .chancedOutput(GOOD_CAT, 1, 5000, 0)
                .EUt(VA[UHV])
                .duration(60)
                .buildAndRegister();

        for (int voltage = UHV; voltage <= OpV; voltage++) {
            // energy hatches
            for (int ampler = 1; ampler <= 16; ampler *= 4) {
                RecipeMaps.ASSEMBLY_LINE_RECIPES.recipeBuilder()
                        .input(getHatch(voltage, false, ampler, false))
                        .inputs(MetaBlocks.FUSION_CASING.getItemVariant(BlockFusionCasing.CasingType.FUSION_COIL))
                        .inputs(MetaBlocks.FUSION_CASING.getItemVariant(BlockFusionCasing.CasingType.SUPERCONDUCTOR_COIL))
                        .input(ACTIVE_TRANSFORMER)
                        .inputs(MetaBlocks.COMPUTER_CASING.getItemVariant(BlockComputerCasing.CasingType.HIGH_POWER_CASING))
                        .input(STRESS_CAT, 4 * ampler)
                        .input(plateDense, Ichorium, ampler == 1 ? 2 : (ampler == 4 ? 4 : 7))
                        .input(circuit, tierList[voltage], 2 * ampler)
                        .fluidInputs(SolderingAlloy.getFluid(L * 9 * ampler))
                        .fluidInputs(Lubricant.getFluid(3000 * ampler))
                        .fluidInputs(EtirpsCranberry.getFluid(1234 * ampler))
                        .output(getHatch(voltage, false, ampler, true))
                        .EUt(VA[voltage])
                        .duration(1200)
                        .scannerResearch(b -> b.researchStack(STRESS_CAT.getStackForm()).EUt(VA[UHV]).duration(1200))
                        .buildAndRegister();

                RecipeMaps.ASSEMBLY_LINE_RECIPES.recipeBuilder()
                        .input(getHatch(voltage, true, ampler, false))
                        .inputs(MetaBlocks.FUSION_CASING.getItemVariant(BlockFusionCasing.CasingType.FUSION_COIL))
                        .inputs(MetaBlocks.FUSION_CASING.getItemVariant(BlockFusionCasing.CasingType.SUPERCONDUCTOR_COIL))
                        .input(ACTIVE_TRANSFORMER)
                        .inputs(MetaBlocks.COMPUTER_CASING.getItemVariant(BlockComputerCasing.CasingType.HIGH_POWER_CASING))
                        .input(GOOD_CAT, 4 * ampler)
                        .input(plateDense, Ichorium, ampler == 1 ? 2 : (ampler == 4 ? 4 : 7))
                        .input(circuit, tierList[voltage], 2 * ampler)
                        .fluidInputs(SolderingAlloy.getFluid(L * 9 * ampler))
                        .fluidInputs(Lubricant.getFluid(3000 * ampler))
                        .fluidInputs(EtirpsCranberry.getFluid(1234 * ampler))
                        .output(getHatch(voltage, true, ampler, true))
                        .EUt(VA[voltage])
                        .duration(1200)
                        .scannerResearch(b -> b.researchStack(GOOD_CAT.getStackForm()).EUt(VA[UHV]).duration(1200))
                        .buildAndRegister();
            }
            // upgrade
            RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                    .input(WIRELESS_INPUT_ENERGY_HATCH[voltage], 4)
                    .input(ACTIVE_TRANSFORMER)
                    .input(GTFOMetaItem.ETIRPS_CRANBERRY, 4)
                    .fluidInputs(Glue.getFluid(1000))
                    .output(WIRELESS_INPUT_ENERGY_HATCH_4A[voltage])
                    .EUt(VA[voltage])
                    .duration(1200)
                    .buildAndRegister();

            RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                    .input(WIRELESS_INPUT_ENERGY_HATCH_4A[voltage], 4)
                    .input(ACTIVE_TRANSFORMER)
                    .input(GTFOMetaItem.ETIRPS_CRANBERRY, 16)
                    .fluidInputs(Glue.getFluid(4000))
                    .output(WIRELESS_INPUT_ENERGY_HATCH_16A[voltage])
                    .EUt(VA[voltage + 1])
                    .duration(1200)
                    .buildAndRegister();

            // 64A output
            RecipeMaps.ASSEMBLY_LINE_RECIPES.recipeBuilder()
                    .input(getHatch(voltage, true, 64, false))
                    .inputs(MetaBlocks.FUSION_CASING.getItemVariant(BlockFusionCasing.CasingType.FUSION_COIL))
                    .inputs(MetaBlocks.FUSION_CASING.getItemVariant(BlockFusionCasing.CasingType.SUPERCONDUCTOR_COIL))
                    .input(ACTIVE_TRANSFORMER)
                    .inputs(MetaBlocks.COMPUTER_CASING.getItemVariant(BlockComputerCasing.CasingType.HIGH_POWER_CASING))
                    .input(GOOD_CAT, 64)
                    .input(GOOD_CAT, 64)
                    .input(plateDense, Ichorium, 6)
                    .input(plateDense, Ichorium, 6)
                    .input(circuit, tierList[voltage], 64)
                    .fluidInputs(SolderingAlloy.getFluid(L * 9 * 64))
                    .fluidInputs(Lubricant.getFluid(3000 * 64))
                    .fluidInputs(EtirpsCranberry.getFluid(1234 * 64))
                    .output(getHatch(voltage, true, 64, true))
                    .EUt(VA[voltage])
                    .duration(1200)
                    .scannerResearch(b -> b.researchStack(GOOD_CAT.getStackForm()).EUt(VA[UHV]).duration(1200))
                    .buildAndRegister();
        }
    }

    private static MetaTileEntity getHatch(int voltage, boolean output, int ampler, boolean wireless) {
        if (wireless) {
            if (ampler == 1) return output ? WIRELESS_OUTPUT_ENERGY_HATCH[voltage] : WIRELESS_INPUT_ENERGY_HATCH[voltage];
            if (ampler == 4) return output ? WIRELESS_OUTPUT_ENERGY_HATCH_4A[voltage] : WIRELESS_INPUT_ENERGY_HATCH_4A[voltage];
            if (ampler == 16) return output ? WIRELESS_OUTPUT_ENERGY_HATCH_16A[voltage] : WIRELESS_INPUT_ENERGY_HATCH_16A[voltage];
            if (ampler == 64) return output ? WIRELESS_OUTPUT_ENERGY_HATCH_64A[voltage] : WIRELESS_INPUT_ENERGY_HATCH_64A[voltage];
            throw new IllegalArgumentException(voltage + " " + output + " " + ampler + " " + wireless);
        }
        if (ampler == 1) return output ? ENERGY_OUTPUT_HATCH[voltage] : ENERGY_INPUT_HATCH[voltage];
        if (ampler == 4) {
            if (EV <= voltage && voltage <= UHV) {
                return output ? ENERGY_OUTPUT_HATCH_4A[voltage - EV] : ENERGY_INPUT_HATCH_4A[voltage - EV];
            } else {
                if (voltage < EV && !output)
                    throw new IllegalArgumentException(voltage + " " + output + " " + ampler + " " + wireless);
                int offset = voltage < EV ? 0 : 7;
                return output ? OUTPUT_ENERGY_HATCH_4A[voltage - offset] : INPUT_ENERGY_HATCH_4A[voltage - UEV];
            }
        }
        if (ampler == 16) {
            if (IV <= voltage && voltage <= UHV) {
                return output ? ENERGY_OUTPUT_HATCH_16A[voltage - IV] : ENERGY_INPUT_HATCH_16A[voltage - IV];
            } else {
                if (voltage < IV && !output)
                    throw new IllegalArgumentException(voltage + " " + output + " " + ampler + " " + wireless);
                int offset = voltage < IV ? 0 : 6;
                return output ? OUTPUT_ENERGY_HATCH_16A[voltage - offset] : INPUT_ENERGY_HATCH_16A[voltage - UEV];
            }
        }
        if (ampler == 64) {
            if (IV <= voltage && voltage <= UHV) {
                return output ? SUBSTATION_ENERGY_OUTPUT_HATCH[voltage - IV] : SUBSTATION_ENERGY_INPUT_HATCH[voltage - IV];
            } else {
                if (voltage < IV && !output)
                    throw new IllegalArgumentException(voltage + " " + output + " " + ampler + " " + wireless);
                int offset = voltage <= IV ? 0 : 6;
                return output ? SUBSTATION_OUTPUT_ENERGY_HATCH[voltage - offset] : SUBSTATION_INPUT_ENERGY_HATCH[voltage - UEV];
            }
        }
        throw new IllegalArgumentException(voltage + " " + output + " " + ampler + " " + wireless);
    }
}
