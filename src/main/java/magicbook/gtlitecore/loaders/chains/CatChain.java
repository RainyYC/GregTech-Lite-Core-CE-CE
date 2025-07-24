package magicbook.gtlitecore.loaders.chains;

import gregicality.multiblocks.api.recipes.GCYMRecipeMaps;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.utils.Mods;

import java.util.ArrayList;

import static gregtech.api.unification.material.Materials.Hafnium;
import static gregtech.api.unification.ore.OrePrefix.dust;
import static gregtech.api.unification.ore.OrePrefix.lens;
import static magicbook.gtlitecore.api.unification.GTLiteMaterials.*;
import static magicbook.gtlitecore.api.utils.GTLiteUtility.getMetaItemById;
import static gregtech.api.GTValues.*;
import static magicbook.gtlitecore.common.items.GTLiteMetaItems.*;

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
    }
}
