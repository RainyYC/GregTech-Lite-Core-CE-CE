package magicbook.gtlitecore.loaders.multiblock;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import net.minecraft.item.ItemStack;

import static gregtech.api.GTValues.*;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.items.MetaItems.CARBON_MESH;
import static gregtech.common.items.MetaItems.SHAPE_MOLD_BALL;
import static magicbook.gtlitecore.api.GTLiteValues.*;
import static magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps.*;
import static magicbook.gtlitecore.api.unification.GTLiteMaterials.*;
import static magicbook.gtlitecore.api.unification.materials.info.GTLiteOrePrefix.swarm;
import static magicbook.gtlitecore.common.items.GTLiteMetaItems.*;

public class LargeChemicalComplex {

    public static void init() {
        NaquadahTriniumProcessing();
        CatalystRecipes();
        CatalystBedRecipes();
    }

    private static void NaquadahTriniumProcessing() {

        //  Enriched Naquadah Emulsion
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_NAQUADAH)
                .input(dust, SodiumHydroxide, 2)
                .fluidInputs(EnrichedNaquadahSolution.getFluid(16000))
                .fluidInputs(SulfuricAcid.getFluid(8000))
                .fluidInputs(DistilledWater.getFluid(64000))
                .fluidOutputs(EnrichedNaquadahEmulsion.getFluid(64000))
                .EUt(VA[ZPM])
                .duration(MINUTE)
                .buildAndRegister();

        CENTRIFUGE_RECIPES.recipeBuilder()
                .fluidInputs(EnrichedNaquadahEmulsion.getFluid(8000))
                .output(dust, NaquadahEnriched)
                .chancedOutput(dust, NaquadahEnriched, 500, 250)
                .chancedOutput(dust, NaquadahEnriched, 1000, 400)
                .chancedOutput(dust, NaquadahEnriched, 1000, 400)
                .chancedOutput(dust, NaquadahEnriched, 2500, 800)
                .chancedOutput(dust, NaquadahEnriched, 5000, 150)
                .fluidOutputs(TriniumGoo.getFluid(2000))
                .EUt(VA[IV])
                .duration(350)
                .buildAndRegister();

        //  Naquadria Emulsion
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_NAQUADAH)
                .input(dust, SodiumHydroxide, 2)
                .fluidInputs(NaquadriaSolution.getFluid(16000))
                .fluidInputs(HydrofluoricAcid.getFluid(8000))
                .fluidInputs(DistilledWater.getFluid(64000))
                .fluidOutputs(NaquadriaEmulsion.getFluid(64000))
                .EUt(VA[UV])
                .duration(MINUTE)
                .buildAndRegister();

        CENTRIFUGE_RECIPES.recipeBuilder()
                .fluidInputs(NaquadriaEmulsion.getFluid(8000))
                .output(dust, Naquadria)
                .chancedOutput(dust, Naquadria, 500, 250)
                .chancedOutput(dust, Naquadria, 1000, 400)
                .chancedOutput(dust, Naquadria, 1000, 400)
                .chancedOutput(dust, Naquadria, 2500, 800)
                .chancedOutput(dust, Naquadria, 5000, 150)
                .fluidOutputs(TriniumGoo.getFluid(2000))
                .EUt(VA[IV])
                .duration(350)
                .buildAndRegister();

        //  Trinium Goo -> Trinium
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_SOLID_ACID)
                .fluidInputs(TriniumGoo.getFluid(16000))
                .fluidInputs(NitricAcid.getFluid(4000))
                .output(dust, DeepIron, 8)
                .fluidOutputs(TriniumWaste.getFluid(12000))
                .EUt(VA[ZPM])
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

        //  Trinium Waste -> Ga + Ti + HNO3 (cycle)
        CHEMICAL_DEHYDRATOR_RECIPES.recipeBuilder()
                .fluidInputs(TriniumWaste.getFluid(3000))
                .output(dust, Gallium)
                .output(dust, Titanium)
                .fluidOutputs(NitricAcid.getFluid(1000))
                .EUt(VA[ZPM])
                .duration(2 * SECOND)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();

    }

    private static void CatalystRecipes() {

        //  Empty Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(20)
                .input(plate, Steel, 8)
                .input(wireFine, Copper, 4)
                .input(screw, Tin, 6)
                .output(CATALYST_BASE)
                .EUt(VA[LV])
                .duration(MINUTE / 2)
                .buildAndRegister();

        ColoredMetalCatalystRecipes();
        InfinityMutationCatalystChain();
        PlatinumGroupCatalystChain();
        PlasticPolymerCatalystChain();
        RubberPolymerCatalystChain();
        AdhesionPromoterCatalystChain();
        IndiumCatalystChain();
        RadioactivityCatalystChain();
        RareEarthCatalystChain();
        NaquadahCatalystChain();
        RawIntelligenceCatalystChain();
        UltimatePlasticCatalystChain();
        BiologicalIntelligenceCatalystChain();
        TemporalHarmonyCatalystChain();
        FlawlessWaterCatalystChain();
        PhotoelectronicCatalystChain();
        ParticleAccelerationCatalystChain();
        SynchrotronCapableCatalystChain();
    }

    private static void ColoredMetalCatalystRecipes() {

        //  Green Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(24)
                .input(CATALYST_BASE, 10)
                .input(dust, Aluminium, 4)
                .input(dust, Silver, 4)
                .output(CATALYST_GREEN_METAL, 10)
                .EUt(VA[LV])
                .duration(20 * SECOND)
                .buildAndRegister();

        //  Red Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(25)
                .input(CATALYST_BASE, 10)
                .input(dust, Iron, 2)
                .input(dust, Copper, 2)
                .output(CATALYST_RED_METAL, 10)
                .EUt(VA[LV])
                .duration(20 * SECOND)
                .buildAndRegister();

        //  Yellow Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(22)
                .input(CATALYST_BASE, 10)
                .input(dust, Tungsten, 4)
                .input(dust, Nickel, 4)
                .output(CATALYST_YELLOW_METAL, 10)
                .EUt(VA[EV])
                .duration(MINUTE)
                .buildAndRegister();

        //  Blue Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(18)
                .input(CATALYST_BASE, 10)
                .input(dust, Cobalt, 3)
                .input(dust, Titanium, 3)
                .output(CATALYST_BLUE_METAL, 10)
                .EUt(VA[HV])
                .duration(40 * SECOND)
                .buildAndRegister();

        //  Orange Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(28)
                .input(CATALYST_BASE, 10)
                .input(dust, Vanadium, 5)
                .input(dust, Palladium, 5)
                .output(CATALYST_ORANGE_METAL, 10)
                .EUt(VA[HV])
                .duration(40 * SECOND)
                .buildAndRegister();

        //  Purple Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(16)
                .input(CATALYST_BASE, 10)
                .input(dust, Iridium, 6)
                .input(dust, Ruthenium, 6)
                .output(CATALYST_PURPLE_METAL, 10)
                .EUt(VA[IV])
                .duration(2 * MINUTE)
                .buildAndRegister();

        //  Brown Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(14)
                .input(CATALYST_BASE, 10)
                .input(dust, Nickel, 4)
                .input(dust, Aluminium, 4)
                .output(CATALYST_BROWN_METAL, 10)
                .EUt(VA[LV])
                .duration(15 * SECOND)
                .buildAndRegister();

        //  Pink Metal Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(12)
                .input(CATALYST_BASE, 10)
                .input(dust, Platinum, 4)
                .input(dust, Rhodium, 4)
                .output(CATALYST_PINK_METAL, 10)
                .EUt(VA[EV])
                .duration(MINUTE / 2)
                .buildAndRegister();

        //  Formaldehyde Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(13)
                .input(CATALYST_BASE, 4)
                .input(dust, Iron, 8)
                .input(dust, Vanadium)
                .output(CATALYST_FORMALDEHYDE, 4)
                .EUt(VHA[HV])
                .duration(MINUTE / 2)
                .buildAndRegister();

        //  Solid Acid Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(14)
                .input(CATALYST_BASE, 5)
                .input(dust, Lapis, 2)
                .fluidInputs(SulfuricAcid.getFluid(1000))
                .output(CATALYST_SOLID_ACID, 5)
                .EUt(VA[EV])
                .duration(MINUTE / 2)
                .buildAndRegister();
    }

    private static void InfinityMutationCatalystChain() {
        //无尽

    }

    private static void PlatinumGroupCatalystChain() {
        //铂系
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(11)
                .input(CATALYST_BASE, 6)
                .input(dust, PhillipsChromiumCatalyst, 14) // 2
                .input(dust, Platinum, 30) // 2
                .fluidInputs(Nitrogen.getFluid(600))
                .output(CATALYST_PLASTIC_POLYMER, 6)
                .EUt(VA[LuV])
                .duration(MINUTE)
                .buildAndRegister();
        /////////////////////////////////////////////////////////////
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, PlatinumGroupSludge, 1)
                .output(dust, Platinum, 1)
                .output(dust, Palladium, 1)
                .output(dust, Iridium, 1)
                .output(dust, Osmium, 1)
                .output(dust, Rhodium, 1)
                .output(dust, Ruthenium, 1)
                .EUt(VA[UV])
                .duration(100)
                .buildAndRegister();
    }

    private static void PlasticPolymerCatalystChain() {

        //  CrO3 + SiO2 -> (CrO3)(SiO2)
        VACUUM_CHAMBER_RECIPES.recipeBuilder()
                .input(dust, ChromiumTrioxide, 4)
                .input(dust, SilicaGel, 3)
                .circuitMeta(2)
                .output(dust, PhillipsChromiumCatalyst, 7)
                .EUt(VA[MV])
                .duration(15 * SECOND)
                .buildAndRegister();

        //  CoO + 2C2H4O2 -> Co(C2H3O2)2 + H2O
        CHEMICAL_RECIPES.recipeBuilder()
                .input(dust, CobaltOxide, 2)
                .fluidInputs(AceticAcid.getFluid(2000))
                .output(dust, CobaltAcetate, 15)
                .fluidOutputs(Water.getFluid(1000))
                .EUt(VA[MV])
                .duration(10 * SECOND)
                .buildAndRegister();

        //  Plastic Polymer
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(11)
                .input(CATALYST_BASE, 6)
                .input(dust, PhillipsChromiumCatalyst, 14) // 2
                .input(dust, CobaltAcetate, 30) // 2
                .fluidInputs(Nitrogen.getFluid(600))
                .output(CATALYST_PLASTIC_POLYMER, 6)
                .EUt(VA[LuV])
                .duration(MINUTE)
                .buildAndRegister();

        /////////////////////////////////////////////////////////////
        // Polyethylene (C2H4)n
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 2)
                .fluidInputs(Hydrogen.getFluid(4000))
                .fluidOutputs(Polyethylene.getFluid(6000))
                .EUt(VA[ZPM])
                .circuitMeta(1)
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

        // PolyvinylChloride (C2H3Cl)n
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 2)
                .fluidInputs(Hydrogen.getFluid(3000), Chlorine.getFluid(1000))
                .fluidOutputs(PolyvinylChloride.getFluid(6000))
                .EUt(VA[ZPM])
                .circuitMeta(2)
                .duration((int) (4.8 * 2 * SECOND))
                .buildAndRegister();

        // Polytetrafluoroethylene (C2F4)n
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 2)
                .fluidInputs(Fluorine.getFluid(4000))
                .fluidOutputs(Polytetrafluoroethylene.getFluid(6000))
                .EUt(VA[ZPM])
                .circuitMeta(3)
                .duration((int) (4.8 * 3 * SECOND))
                .buildAndRegister();

        // Epoxy Resin (C21H25ClO5)
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 21)
                .fluidInputs(Hydrogen.getFluid(25000), Chlorine.getFluid(1000), Oxygen.getFluid(5000))
                .fluidOutputs(Epoxy.getFluid(31000))
                .EUt(VA[ZPM])
                .circuitMeta(4)
                .duration((int) (4.8 * 4 * SECOND))
                .buildAndRegister();

        // Reinforced Epoxy Resin（增强环氧树脂）
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 24)
                .input(dust, Silicon, 2)
                .fluidInputs(Hydrogen.getFluid(28000), Chlorine.getFluid(1500), Oxygen.getFluid(6000))
                .fluidOutputs(ReinforcedEpoxyResin.getFluid(36000))
                .EUt(VA[ZPM])
                .circuitMeta(5)
                .duration((int) (4.8 * 5 * SECOND))
                .buildAndRegister();

        // Polybenzimidazole (C7H5N2)n
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 7)
                .fluidInputs(Hydrogen.getFluid(5000), Nitrogen.getFluid(2000))
                .fluidOutputs(Polybenzimidazole.getFluid(14000))
                .EUt(VA[ZPM])
                .circuitMeta(6)
                .duration((int) (4.8 * 6 * SECOND))
                .buildAndRegister();


    }

    private static void RubberPolymerCatalystChain() {
        //  Rubber Polymer
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(11)
                .input(CATALYST_BASE, 6)
                .input(dust, PhillipsChromiumCatalyst, 14) // 2
                .input(dust, CadmiumBromide, 30) // 2
                .fluidInputs(Nitrogen.getFluid(600))
                .output(CATALYST_PLASTIC_POLYMER, 6)
                .EUt(VA[LuV])
                .duration(MINUTE)
                .buildAndRegister();
        /////////////////////////////////////////////////////////////
        // 天然橡胶 (C5H8)
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 5)
                .fluidInputs(Hydrogen.getFluid(8000))
                .fluidOutputs(Rubber.getFluid(13000)) // (5+8)*1000
                .EUt(VA[ZPM])
                .circuitMeta(1)
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

        // 硅橡胶 Si(CH3)20 → SiC20H60
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Silicon, 1)
                .input(dust, Carbon, 20)
                .fluidInputs(Hydrogen.getFluid(60000)) // 60H
                .fluidOutputs(SiliconeRubber.getFluid(81000)) // (1+20+60)*1000
                .EUt(VA[ZPM])
                .circuitMeta(2)
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

        // 丁苯橡胶 (C4H6)3C8H8 → C20H26
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 20)
                .fluidInputs(Hydrogen.getFluid(26000))
                .fluidOutputs(StyreneButadieneRubber.getFluid(46000)) // (20+26)*1000
                .EUt(VA[ZPM])
                .circuitMeta(3)
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

        // 丁腈橡胶 C7H9N → 需拆解为 C7H9N
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Carbon, 7)
                .fluidInputs(Hydrogen.getFluid(9000), Nitrogen.getFluid(1000))
                .fluidOutputs(NitrileButadieneRubber.getFluid(17000)) // (7+9+1)*1000
                .EUt(VA[ZPM])
                .circuitMeta(4)
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

        // 聚膦腈氟橡胶 (PNF2)n → 磷+氮+氟复合
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_PLASTIC_POLYMER)
                .input(dust, Phosphorus, 1)
                .fluidInputs(Nitrogen.getFluid(1000))
                .fluidInputs(Fluorine.getFluid(2000))
                .fluidOutputs(PolyPhosphonitrileFluoroRubber.getFluid(4000)) // (1+1+2)*1000
                .EUt(VA[ZPM])
                .circuitMeta(5)
                .duration((int) (4.8 * SECOND))
                .buildAndRegister();

    }

    private static void AdhesionPromoterCatalystChain() {
    }

    private static void IndiumCatalystChain() {
    }

    private static void RadioactivityCatalystChain() {
    }

    private static void RareEarthCatalystChain() {
        //  Rare Earth Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(12)
                .input(CATALYST_BASE, 6)
                .input(dust, Lanthanum, 14) // 2
                .input(dust, CadmiumBromide, 30) // 2
                .fluidInputs(Nitrogen.getFluid(600))
                .output(CATALYST_RARE_EARTH, 6)
                .EUt(VA[LuV])
                .duration(MINUTE)
                .buildAndRegister();
        /////////////////////////////////////////////////////////////////
        //  Rare Earth Group
        LARGE_CHEMICAL_COMPLEX_RECIPES.recipeBuilder()
                .notConsumable(CATALYST_RARE_EARTH)
                .input(dust, Bastnasite, 1)
                .output(dust, Lanthanum, 1)
                .output(dust, Cerium, 1)
                .output(dust, Praseodymium, 1)
                .output(dust, Neodymium, 1)
                .output(dust, Promethium, 1)
                .output(dust, Samarium, 1)
                .EUt(VA[UV])
                .duration(100)
                .buildAndRegister();
    }

    private static void NaquadahCatalystChain() {

        //  *Nq* -> Nqx
        CHEMICAL_RECIPES.recipeBuilder()
                .input(dust, Naquadria, 6)
                .fluidInputs(FluoroantimonicAcid.getFluid(2000))
                .fluidInputs(Krypton.getPlasma(500))
                .output(dust, ExtremelyUnstableNaquadah, 6)
                .fluidOutputs(NaquadriaWaste.getFluid(2000))
                .EUt(VA[ZPM])
                .duration((int) (7.5 * SECOND))
                .buildAndRegister();

        //  Ke -> Qt
        //  back to Collider recipes.

        //  2Nq+ + 4Qt + Pu-239 -> Nq+2Qt4Pu (Atomic Separation Catalyst)
        MIXER_RECIPES.recipeBuilder()
                .input(dust, NaquadahEnriched, 2)
                .input(dust, Quantium, 4)
                .circuitMeta(3)
                .fluidInputs(Plutonium239.getFluid(L))
                .output(dust, AtomicSeparationCatalyst, 7)
                .EUt(VA[HV])
                .duration(3 * MINUTE)
                .buildAndRegister();

        //  MoO3 + TeO2 -> (MoO3)(TeO2)
        MIXER_RECIPES.recipeBuilder()
                .input(dust, MolybdenumTrioxide, 4)
                .input(dust, TelluriumDioxide, 3)
                .circuitMeta(2)
                .output(dust, MolybdenumTelluriumOxides, 7)
                .EUt(VA[HV])
                .duration((int) (6.5 * SECOND))
                .buildAndRegister();

        //  Naquadah Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(16)
                .input(CATALYST_BASE, 4)
                .input(dust, ExtremelyUnstableNaquadah) // 1/6
                .input(dust, AtomicSeparationCatalyst, 2) // 2/7
                .input(dust, MolybdenumTelluriumOxides, 3) // 3/7
                .fluidInputs(Quantium.getFluid(L * 2))
                .output(CATALYST_NAQUADAH, 4)
                .EUt(VA[ZPM])
                .duration(MINUTE)
                .buildAndRegister();

    }

    private static void RawIntelligenceCatalystChain() {
    }

    private static void UltimatePlasticCatalystChain() {

        //  2Al + 6C2H4 + 6H -> Al2(C2H5)6
        CHEMICAL_RECIPES.recipeBuilder()
                .circuitMeta(3)
                .input(dust, Aluminium, 2)
                .fluidInputs(Ethylene.getFluid(6000))
                .fluidInputs(Hydrogen.getFluid(6000))
                .fluidOutputs(Triethylaluminum.getFluid(1000))
                .EUt(VA[HV])
                .duration(12 * SECOND)
                .buildAndRegister();

        //  Al2(C2H5)6 + TiCl4 -> (Al2(C2H5)6)(TiCl4)
        VACUUM_CHAMBER_RECIPES.recipeBuilder()
                .notConsumable(SHAPE_MOLD_BALL)
                .fluidInputs(Triethylaluminum.getFluid(1000))
                .fluidInputs(TitaniumTetrachloride.getFluid(1000))
                .output(dust, ZieglerNattaCatalyst, 49)
                .EUt(VA[EV])
                .duration(6 * SECOND)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();

        //  Na + C5H6 -> NaC5H5 + H
        CHEMICAL_RECIPES.recipeBuilder()
                .input(dust, Sodium)
                .fluidInputs(Cyclopentadiene.getFluid(1000))
                .fluidOutputs(SodiumCyclopentadiene.getFluid(1000))
                .fluidOutputs(Hydrogen.getFluid(1000))
                .EUt(VA[MV])
                .duration(10 * SECOND)
                .buildAndRegister();

        //  Zi + 4Cl -> ZiCl4
        CHEMICAL_RECIPES.recipeBuilder()
                .input(dust, Zirconium)
                .circuitMeta(4)
                .fluidInputs(Chlorine.getFluid(4000))
                .output(dust, ZirconiumTetrachloride, 5)
                .EUt(VA[LV])
                .duration((int) (2.5 * SECOND))
                .buildAndRegister();

        //  ZiCl4 + 2NaC5H5-> Zr(C5H5)2Cl2 + 2NaCl
        CHEMICAL_RECIPES.recipeBuilder()
                .input(dust, ZirconiumTetrachloride, 5)
                .fluidInputs(SodiumCyclopentadiene.getFluid(2000))
                .output(dust, ZirconoceneDichloride, 23)
                .EUt(VA[IV])
                .duration(10 * SECOND)
                .buildAndRegister();

        //  Al2(CH3)6 + 2H2O -> 2AlCH3O + 4CH4
        CHEMICAL_RECIPES.recipeBuilder()
                .fluidInputs(Trimethylaluminium.getFluid(1000))
                .fluidInputs(Water.getFluid(2000))
                .output(dust, Methylaluminoxane, 12)
                .fluidOutputs(Methane.getFluid(4000))
                .EUt(VA[EV])
                .duration(12 * SECOND)
                .buildAndRegister();

        //  Zr(C5H5)2Cl2 + AlCH3O -> (Zr(C5H5)2Cl2)(AlCH3O)
        CHEMICAL_RECIPES.recipeBuilder()
                .input(dust, Methylaluminoxane, 12)
                .input(dust, ZirconoceneDichloride, 23)
                .output(dust, KaminskyCatalyst, 29)
                .EUt(VA[IV])
                .duration((int) (12.5 * SECOND))
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();

        //  Ultimate Plastic Catalyst
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(12)
                .input(CATALYST_BASE, 3)
                .input(dust, KaminskyCatalyst, 4) // almost 2/15
                .input(dust, ZieglerNattaCatalyst, 28) // 4/7
                .fluidInputs(Nitrogen.getPlasma(600))
                .output(CATALYST_ULTIMATE_PLASTIC, 3)
                .EUt(VA[UV])
                .duration(MINUTE / 2)
                .buildAndRegister();
    }

    private static void BiologicalIntelligenceCatalystChain() {
    }

    private static void TemporalHarmonyCatalystChain() {
    }

    private static void FlawlessWaterCatalystChain() {
    }

    private static void PhotoelectronicCatalystChain() {
    }

    private static void ParticleAccelerationCatalystChain() {
    }

    private static void SynchrotronCapableCatalystChain() {
    }

    private static void CatalystBedRecipes() {

        //  Empty Catalyst Bed
        ASSEMBLER_RECIPES.recipeBuilder()
                .circuitMeta(20)
                .input(frameGt, Polyethylene)
                .input(CARBON_MESH, 2)
                .input(foil, Polyurethane, 4)
                .output(CATALYST_BED_BASE)
                .EUt(VA[MV])
                .duration(MINUTE / 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();

        createCatalystBedRecipe(ULV, CATALYST_BED_ULV,
                Copper,
                OreDictUnifier.get(dust, TinChloride, 3),
                OreDictUnifier.get(dust, SodiumOxide, 2));

        createCatalystBedRecipe(LV, CATALYST_BED_LV,
                Bronze,
                OreDictUnifier.get(dust, CobaltOxide, 2),
                OreDictUnifier.get(dust, CopperChloride, 3));

        createCatalystBedRecipe(MV, CATALYST_BED_MV,
                Steel,
                OreDictUnifier.get(dust, ChromiumTrioxide, 4),
                OreDictUnifier.get(dust, LithiumFluoride, 2));

        createCatalystBedRecipe(HV, CATALYST_BED_HV,
                StainlessSteel,
                OreDictUnifier.get(dust, Alumina, 5),
                OreDictUnifier.get(dust, BoronTrioxide, 5));

        createCatalystBedRecipe(EV, CATALYST_BED_EV,
                Titanium,
                OreDictUnifier.get(dust, RedPhosphorus),
                OreDictUnifier.get(dust, SeleniumDioxide, 3));

        createCatalystBedRecipe(IV, CATALYST_BED_IV,
                TungstenSteel,
                OreDictUnifier.get(dust, GermaniumDioxide, 3),
                OreDictUnifier.get(dust, ThalliumCopperChloride, 5));

        createCatalystBedRecipe(LuV, CATALYST_BED_LuV,
                NiobiumTitanium,
                OreDictUnifier.get(dust, StrontiumDichloride, 3),
                OreDictUnifier.get(dust, BlackPhosphorus));

        createCatalystBedRecipe(ZPM, CATALYST_BED_ZPM,
                Iridium,
                OreDictUnifier.get(dust, HRAMagnesium),
                OreDictUnifier.get(dust, MagnesiumBromide, 3));

        createCatalystBedRecipe(UV, CATALYST_BED_UV,
                Naquadah,
                OreDictUnifier.get(dust, TriniumTitanide, 3),
                OreDictUnifier.get(dust, Picotite, 7));

        createCatalystBedRecipe(UHV, CATALYST_BED_UHV,
                Duranium,
                OreDictUnifier.get(dust, CadmiumSelenide, 2),
                OreDictUnifier.get(dust, BluePhosphorus));

        createCatalystBedRecipe(UEV, CATALYST_BED_UEV,
                Lafium,
                OreDictUnifier.get(dust, BismuthChalcogenide, 5),
                OreDictUnifier.get(dust, CubicHeterodiamond));

        createCatalystBedRecipe(UIV, CATALYST_BED_UIV,
                CrystalMatrix,
                OreDictUnifier.get(dust, LithiumTrifluoromethansulphonate, 9),
                OreDictUnifier.get(dust, NeptuniumAluminide, 4));

        createCatalystBedRecipe(UXV, CATALYST_BED_UXV,
                QuantumchromodynamicallyConfinedMatter,
                OreDictUnifier.get(dust, LanthanumFullereneMixture, 4),
                OreDictUnifier.get(dust, PalladiumFullereneMatrix));

        createCatalystBedRecipe(OpV, CATALYST_BED_OpV,
                Fatalium,
                OreDictUnifier.get(dust, HassiumTetrachloride, 5),
                OreDictUnifier.get(dust, Bisethylenedithiotetraselenafulvalene, 3));

        createCatalystBedRecipe(MAX, CATALYST_BED_MAX,
                Aetherium,
                OreDictUnifier.get(dust, TetraethylammoniumNonahydriorhenidetechnetide, 2),
                OreDictUnifier.get(dust, Hexanitrohexaaxaisowurtzitane));
    }

    private static void createCatalystBedRecipe(int tier,
                                                MetaItem<?>.MetaValueItem catalystBed,
                                                Material pipeMaterial,
                                                ItemStack catalystA,
                                                ItemStack catalystB) {
        VACUUM_CHAMBER_RECIPES.recipeBuilder()
                .input(CATALYST_BED_BASE)
                .input(pipeTinyFluid, pipeMaterial, 2)
                .inputs(catalystA)
                .inputs(catalystB)
                .fluidInputs(Nitrogen.getFluid(1000))
                .outputs(catalystBed.getStackForm())
                .EUt(VA[tier])
                .duration((int) (2.5 * (tier + 1) * SECOND))
                .buildAndRegister();
    }


}
