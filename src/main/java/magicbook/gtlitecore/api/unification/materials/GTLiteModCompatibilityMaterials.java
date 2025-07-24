package magicbook.gtlitecore.api.unification.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.properties.BlastProperty;
import magicbook.gtlitecore.api.annotation.MaterialIDRange;

import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static gregtech.api.util.GTUtility.gregtechId;
import static magicbook.gtlitecore.api.annotation.processor.MaterialIDProvider.getID;
import static magicbook.gtlitecore.api.unification.GTLiteMaterials.*;

@MaterialIDRange(startID = 21001, endID = 22000)
public class GTLiteModCompatibilityMaterials {

    public static void register() {

        //  21001 Fluix (Applied Energistics 2)
        Fluix = new Material.Builder(getID(), gregtechId("fluix"))
                .gem()
                .color(0x674FAF)
                .iconSet(CERTUS)
                .components(CertusQuartz, 1, NetherQuartz, 1, Redstone, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_GEAR, GENERATE_LENS, GENERATE_FINE_WIRE)
                .build()
                .setFormula("(SiO2)2(Si(FeS2)5(CrAl2O3)Hg3)", true); // Merged same formula of Certus Quartz and Nether Quartz to SiO2.

        //  21002 Dark Steel (Ender IO)
        DarkSteel = new Material.Builder(getID(), gregtechId("dark_steel"))
                .ingot()
                .fluid()
                .color(0x504650)
                .iconSet(METALLIC)
                .components(Steel, 1, Obsidian, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD, GENERATE_SPRING, GENERATE_SPRING_SMALL)
                .build();

        //  21003 Electrical Steel (Ender IO)
        ElectricalSteel = new Material.Builder(getID(), gregtechId("electrical_steel"))
                .ingot()
                .color(0xB9B9B9)
                .iconSet(METALLIC)
                .components(Steel, 1, Silicon, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_BOLT_SCREW, GENERATE_GEAR, GENERATE_SMALL_GEAR)
                .build();

        //  21004 Redstone Alloy (Ender IO)
        RedstoneAlloy = new Material.Builder(getID(), gregtechId("redstone_alloy"))
                .ingot()
                .fluid()
                .color(0xB53333)
                .iconSet(METALLIC)
                .components(Redstone, 1, Silicon, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_DOUBLE_PLATE)
                .build();

        //  21005 Soularium (Ender IO)
        Soularium = new Material.Builder(getID(), gregtechId("soularium"))
                .ingot()
                .fluid()
                .color(0x412E1D)
                .iconSet(METALLIC)
                .components(Gold, 1, RareEarth, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_BOLT_SCREW, GENERATE_DOUBLE_PLATE)
                .build();

        //  21006 Pulsating Iron (Ender IO)
        PulsatingIron = new Material.Builder(getID(), gregtechId("pulsating_iron"))
                .ingot()
                .color(0x80F69B)
                .iconSet(METALLIC)
                .components(Iron, 1, Uraninite, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW)
                .cableProperties(V[ULV], 1, 0, true)
                .build();

        //  21007 Conductive Iron (Ender IO)
        ConductiveIron = new Material.Builder(getID(), gregtechId("conductive_iron"))
                .ingot()
                .color(0xD9B2AB)
                .iconSet(METALLIC)
                .components(Iron, 1, Redstone, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_BOLT_SCREW)
                .cableProperties(V[LV], 1, 0, true)
                .build();

        //  21008 Energetic Alloy (Ender IO)
        EnergeticAlloy = new Material.Builder(getID(), gregtechId("energetic_alloy"))
                .ingot()
                .color(0xFFAA51)
                .iconSet(METALLIC)
                .components(Gold, 2, Redstone, 1, Glowstone, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_FRAME, GENERATE_BOLT_SCREW)
                .blast(b -> b
                        .temp(1250, BlastProperty.GasTier.LOW)
                        .blastStats(VA[MV], 400))
                .cableProperties(V[MV], 1, 0, true)
                .build();

        //  21009 Vibrant Alloy (Ender IO)
        VibrantAlloy = new Material.Builder(getID(), gregtechId("vibrant_alloy"))
                .ingot()
                .color(0x9DBC35)
                .iconSet(METALLIC)
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW)
                .blast(b -> b
                        .temp(1900, BlastProperty.GasTier.LOW)
                        .blastStats(VA[HV], 600)
                        .vacuumStats(VA[LV], 100))
                .cableProperties(V[HV], 1, 0, true)
                .build();
        
        //  21010 End Steel (Ender IO)
        EndSteel = new Material.Builder(getID(),gregtechId("end_steel"))
                .ingot()
                .color(0xDFD9A5)
                .iconSet(METALLIC)
                .components(DarkSteel, 1, Endstone, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_BOLT_SCREW)
                .cableProperties(V[EV], 1, 0, true)
                .blast(b -> b
                        .temp(3380, BlastProperty.GasTier.MID)
                        .blastStats(VA[EV], 800)
                        .vacuumStats(VA[MV], 200))
                .build();

        //  21011 Lumium (Thermal Expansion)
        Lumium = new Material.Builder(getID(), gregtechId("lumium"))
                .ingot()
                .fluid()
                .color(0xF6FF99)
                .iconSet(BRIGHT)
                .components(EndSteel, 1, BlueAlloy, 2, VibrantAlloy, 3)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_BOLT_SCREW)
                .cableProperties(V[IV], 1, 0, true)
                .blast(b -> b
                        .temp(4500, BlastProperty.GasTier.HIGH)
                        .blastStats(VA[IV], 1000)
                        .vacuumStats(VA[HV], 400))
                .build();

        //  21012 Signalum (Thermal Expansion)
        Signalum = new Material.Builder(getID(), gregtechId("signalum"))
                .ingot()
                .fluid()
                .color(0xFF7F07)
                .iconSet(SHINY)
                .components(Lumium, 3, AnnealedCopper, 2, Blaze, 1, Rhodium, 2)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[LuV], 1, 0, true)
                .blast(b -> b
                        .temp(5200, BlastProperty.GasTier.HIGHER)
                        .blastStats(VA[LuV], 1200)
                        .vacuumStats(VA[EV], 600))
                .build();

        //  21013 Enderium (Thermal Expansion)
        Enderium = new Material.Builder(getID(), gregtechId("enderium"))
                .ingot()
                .fluid()
                .color(0x1F6B62)
                .iconSet(SHINY)
                .components(Signalum, 4, Platinum, 2, Osmium, 1, EnderEye, 1)
                .flags(GENERATE_PLATE, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[ZPM], 1, 0, true)
                .blast(b -> b
                        .temp(6400, BlastProperty.GasTier.HIGHEST)
                        .blastStats(VA[ZPM], 1400)
                        .vacuumStats(VA[IV], 800))
                .build();

        //  21014 Construction Alloy (Ender IO)
        ConstructionAlloy = new Material.Builder(getID(), gregtechId("construction_alloy"))
                .ingot()
                .color(0xADADAD)
                .iconSet(ROUGH)
                .components(Iron, 1, Clay, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[ULV], 1, 0, true)
                .build();

        //  21015 Enriched Alloy (Mekanism)
        EnrichedAlloy = new Material.Builder(getID(), gregtechId("enriched_alloy"))
                .ingot()
                .color(0x024D96)
                .iconSet(SHINY)
                .components(ConstructionAlloy, 2, Lithium, 1, Cobalt, 1)
                .flags(GENERATE_PLATE, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[LV], 1, 0, true)
                .build();

        //  21016 Energetic Silver (Ender IO)
        EnergeticSilver = new Material.Builder(getID(), gregtechId("energetic_silver"))
                .ingot()
                .color(0x95B7CD)
                .iconSet(SHINY)
                .components(Silver, 2, Redstone, 1, Glowstone, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[MV], 1, 0, true)
                .blast(b -> b
                        .temp(1250, BlastProperty.GasTier.LOW)
                        .blastStats(VA[MV], 400))
                .build();

        //  21017 Vivid Alloy (Ender IO)
        VividAlloy = new Material.Builder(getID(), gregtechId("vivid_alloy"))
                .ingot()
                .color(0x46BCDB)
                .iconSet(SHINY)
                .components(EnergeticSilver, 1, EnderPearl, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_FRAME)
                .blast(b -> b
                        .temp(1900, BlastProperty.GasTier.LOW)
                        .blastStats(VA[HV], 600)
                        .vacuumStats(VA[LV], 100))
                .cableProperties(V[HV], 1, 0, true)
                .build();

        //  21018 Crystalline Alloy (Ender IO)
        CrystallineAlloy = new Material.Builder(getID(), gregtechId("crystalline_alloy"))
                .ingot()
                .color(0xA7ECEC)
                .iconSet(SHINY)
                .components(VividAlloy, 2, Emerald, 1, Plutonium241, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[EV], 1, 0, true)
                .blast(b -> b
                        .temp(3380, BlastProperty.GasTier.MID)
                        .blastStats(VA[EV], 800)
                        .vacuumStats(VA[MV], 200))
                .build()
                .setFormula("((Ag2(Si(FeS2)5(CrAl2O3)Hg3)?)(BeK4N5))2(Be3Al2Si6O18)Pu", true); // Regularization (Pu-241 -> Pu).

        //  21019 Crystalline Pink Slime (Ender IO)
        CrystallinePinkSlime = new Material.Builder(getID(), gregtechId("crystalline_pink_slime"))
                .ingot()
                .fluid()
                .color(0xF5B8EB)
                .iconSet(BRIGHT)
                .components(CrystallineAlloy, 3, RedSteel, 1, RedstoneAlloy, 2)
                .flags(GENERATE_PLATE, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[IV], 1, 0, true)
                .blast(b -> b
                        .temp(4500, BlastProperty.GasTier.HIGH)
                        .blastStats(VA[IV], 1000)
                        .vacuumStats(VA[HV], 400))
                .build();

        //  21020 Melodic Alloy (Ender IO)
        MelodicAlloy = new Material.Builder(getID(), gregtechId("melodic_alloy"))
                .ingot()
                .fluid()
                .color(0x886288)
                .iconSet(METALLIC)
                .components(CrystallinePinkSlime, 2, Iridium, 1, BlueAlloy, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .cableProperties(V[LuV], 1, 0, true)
                .blast(b -> b
                        .temp(5200, BlastProperty.GasTier.HIGHER)
                        .blastStats(VA[LuV], 1200)
                        .vacuumStats(VA[EV], 600))
                .build();

        //  21021 Stellar Alloy (Ender IO)
        StellarAlloy = new Material.Builder(getID(), gregtechId("stellar_alloy"))
                .ingot()
                .fluid()
                .color(0xD9DCCB)
                .iconSet(BRIGHT)
                .components(NetherStar, 2, MelodicAlloy, 1, Naquadah, 1)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_GEAR)
                .cableProperties(V[ZPM], 1, 0, true)
                .blast(b -> b
                        .temp(6400, BlastProperty.GasTier.HIGHEST)
                        .blastStats(VA[ZPM], 1400)
                        .vacuumStats(VA[IV], 800))
                .build();

        //  21022 Mysterious Crystal (Galaxy Space)
        MysteriousCrystal = new Material.Builder(getID(), gregtechId("mysterious_crystal"))
                .gem()
                .ore(2, 2)
                .color(0x16856C)
                .iconSet(DIAMOND)
                .flags(GENERATE_PLATE, GENERATE_LENS)
                .build()
                .setFormula("My", false);

        //  21023 Black Iron (ExtendedCrafting)
        BlackIron = new Material.Builder(getID(), gregtechId("black_iron"))
                .ingot()
                .fluid()
                .color(0x404040)
                .iconSet(DULL)
                .components(BlackSteel, 1, Bedrock, 1, Fullerene, 1, CarbonNanotube, 1)
                .flags(EXT_METAL, GENERATE_FRAME)
                .blast(b -> b
                        .temp(13500, BlastProperty.GasTier.HIGHEST)
                        .blastStats(VA[UHV], 2400)
                        .vacuumStats(VA[UHV], 1200))
                .build()
                .setFormula("(Ni(AuAgCu3)Fe3)C60CNT?", true);

        UnstableCat = new Material.Builder(getID(), gregtechId("unstable_cat"))
                .fluid()
                .color(0x966e4c)
                .iconSet(FLUID)
                .build()
                .setFormula("(^ω^)☆");

        Cat = new Material.Builder(getID(), gregtechId("stable_cat"))
                .fluid()
                .dust()
                .flags(NO_SMELTING)
                .color(0x9c6537)
                .iconSet(DULL)
                .build()
                .setFormula("(^ω^)", false);
    }

}
