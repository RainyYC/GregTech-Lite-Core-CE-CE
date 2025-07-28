package magicbook.gtlitecore.api.unification.materials.properties;

import gregtech.api.unification.material.properties.OreProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.WireProperties;
import magicbook.gtlitecore.common.GTLiteConfigHolder;

import static gregicality.multiblocks.api.unification.GCYMMaterials.*;
import static gregtech.api.GTValues.UIV;
import static gregtech.api.GTValues.V;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtechfoodoption.GTFOMaterialHandler.BlueVitriol;
import static gregtechfoodoption.GTFOMaterialHandler.RainbowSap;
import static magicbook.gtlitecore.api.unification.GTLiteMaterials.*;
import static magicbook.gtlitecore.api.unification.materials.info.GTLiteMaterialFlags.*;

public class GTLiteMaterialFlagAddition {

    public static void init() {

        //  Milled
        Almandine.addFlags(GENERATE_MILLED);
        Chalcopyrite.addFlags(GENERATE_MILLED);
        Grossular.addFlags(GENERATE_MILLED);
        Monazite.addFlags(GENERATE_MILLED);
        Nickel.addFlags(GENERATE_MILLED);
        Platinum.addFlags(GENERATE_MILLED);
        Pyrope.addFlags(GENERATE_MILLED);
        Redstone.addFlags(GENERATE_MILLED);
        Spessartine.addFlags(GENERATE_MILLED);
        Sphalerite.addFlags(GENERATE_MILLED);
        Pentlandite.addFlags(GENERATE_MILLED);

        //  Plate
        Dubnium.addFlags(GENERATE_PLATE);
        Livermorium.addFlags(GENERATE_PLATE);
        Rhenium.addFlags(GENERATE_PLATE);
        RTMAlloy.addFlags(GENERATE_PLATE);
        Einsteinium.addFlags(GENERATE_PLATE);
        Fermium.addFlags(GENERATE_PLATE);
        Mendelevium.addFlags(GENERATE_PLATE);
        Berkelium.addFlags(GENERATE_PLATE);
        Protactinium.addFlags(GENERATE_PLATE);
        Curium.addFlags(GENERATE_PLATE);
        Plutonium239.addFlags(GENERATE_PLATE);
        Californium.addFlags(GENERATE_PLATE);
        Neptunium.addFlags(GENERATE_PLATE);
        Nobelium.addFlags(GENERATE_PLATE);
        Lawrencium.addFlags(GENERATE_PLATE);
        Technetium.addFlags(GENERATE_PLATE);
        Moscovium.addFlags(GENERATE_PLATE);
        Meitnerium.addFlags(GENERATE_PLATE);
        Roentgenium.addFlags(GENERATE_PLATE);
        Bohrium.addFlags(GENERATE_PLATE);
        Actinium.addFlags(GENERATE_PLATE);
        Vanadium.addFlags(GENERATE_PLATE);
        Indium.addFlags(GENERATE_PLATE);

        //  Double Plate
        HastelloyX.addFlags(GENERATE_DOUBLE_PLATE);
        Dubnium.addFlags(GENERATE_DOUBLE_PLATE);
        Livermorium.addFlags(GENERATE_DOUBLE_PLATE);
        Rhenium.addFlags(GENERATE_DOUBLE_PLATE);
        IncoloyMA956.addFlags(GENERATE_DOUBLE_PLATE);
        Tritanium.addFlags(GENERATE_DOUBLE_PLATE);
        HSSS.addFlags(GENERATE_DOUBLE_PLATE);
        RedSteel.addFlags(GENERATE_DOUBLE_PLATE);
        TitaniumCarbide.addFlags(GENERATE_DOUBLE_PLATE);
        Plutonium239.addFlags(GENERATE_DOUBLE_PLATE);
        TitaniumTungstenCarbide.addFlags(GENERATE_DOUBLE_PLATE);
        Ruridit.addFlags(GENERATE_DOUBLE_PLATE);
        Zeron100.addFlags(GENERATE_DOUBLE_PLATE);
        WatertightSteel.addFlags(GENERATE_DOUBLE_PLATE);
        MaragingSteel300.addFlags(GENERATE_DOUBLE_PLATE);
        BlueSteel.addFlags(GENERATE_DOUBLE_PLATE);
        TantalumCarbide.addFlags(GENERATE_DOUBLE_PLATE);
        BlackSteel.addFlags(GENERATE_DOUBLE_PLATE);
        Stellite100.addFlags(GENERATE_DOUBLE_PLATE);
        Bohrium.addFlags(GENERATE_DOUBLE_PLATE);
        HastelloyC276.addFlags(GENERATE_DOUBLE_PLATE);
        Ruthenium.addFlags(GENERATE_DOUBLE_PLATE);
        IndiumGalliumPhosphide.addFlags(GENERATE_DOUBLE_PLATE);
        NaquadahEnriched.addFlags(GENERATE_DOUBLE_PLATE);
        Vanadium.addFlags(GENERATE_DOUBLE_PLATE);
        Molybdenum.addFlags(GENERATE_DOUBLE_PLATE);
        VanadiumGallium.addFlags(GENERATE_DOUBLE_PLATE);
        RutheniumTriniumAmericiumNeutronate.addFlags(GENERATE_DOUBLE_PLATE);
        Iron.addFlags(GENERATE_DOUBLE_PLATE);

        //  Dense
        Steel.addFlags(GENERATE_DENSE);
        Rhenium.addFlags(GENERATE_DENSE);
        Tritanium.addFlags(GENERATE_DENSE);
        Osmiridium.addFlags(GENERATE_DENSE);
        WroughtIron.addFlags(GENERATE_DENSE);
        Lead.addFlags(GENERATE_DENSE);
        Vanadium.addFlags(GENERATE_DENSE);
        Molybdenum.addFlags(GENERATE_DENSE);
        VanadiumGallium.addFlags(GENERATE_DENSE);
        RutheniumTriniumAmericiumNeutronate.addFlags(GENERATE_DENSE);
        Ichorium.addFlags(GENERATE_DENSE);

        //  Foil
        Nickel.addFlags(GENERATE_FOIL);
        Germanium.addFlags(GENERATE_FOIL);
        Rhenium.addFlags(GENERATE_FOIL);
        RTMAlloy.addFlags(GENERATE_FOIL);

        //  Frame
        Polybenzimidazole.addFlags(GENERATE_FRAME);
        Naquadria.addFlags(GENERATE_FRAME);
        RhodiumPlatedPalladium.addFlags(GENERATE_FRAME);
        Darmstadtium.addFlags(GENERATE_FRAME);
        Naquadah.addFlags(GENERATE_FRAME);
        NaquadahEnriched.addFlags(GENERATE_FRAME);
        Osmiridium.addFlags(GENERATE_FRAME);
        Americium.addFlags(GENERATE_FRAME);
        Uranium238.addFlags(GENERATE_FRAME);
        Plutonium241.addFlags(GENERATE_FRAME);
        VanadiumSteel.addFlags(GENERATE_FRAME);
        RedSteel.addFlags(GENERATE_FRAME);
        CobaltBrass.addFlags(GENERATE_FRAME);
        Einsteinium.addFlags(GENERATE_FRAME);
        Fermium.addFlags(GENERATE_FRAME);
        Mendelevium.addFlags(GENERATE_FRAME);
        Protactinium.addFlags(GENERATE_FRAME);
        Trinium.addFlags(GENERATE_FRAME);
        Curium.addFlags(GENERATE_FRAME);
        Berkelium.addFlags(GENERATE_FRAME);
        Californium.addFlags(GENERATE_FRAME);
        Neptunium.addFlags(GENERATE_FRAME);
        Nobelium.addFlags(GENERATE_FRAME);
        Lawrencium.addFlags(GENERATE_FRAME);
        NiobiumNitride.addFlags(GENERATE_FRAME);
        TitaniumTungstenCarbide.addFlags(GENERATE_FRAME);
        Ruthenium.addFlags(GENERATE_FRAME);
        Rhodium.addFlags(GENERATE_FRAME);
        TantalumCarbide.addFlags(GENERATE_FRAME);
        Duranium.addFlags(GENERATE_FRAME);
        Trinaquadalloy.addFlags(GENERATE_FRAME);
        Polyethylene.addFlags(GENERATE_FRAME);
        Zeron100.addFlags(GENERATE_FRAME);
        CertusQuartz.addFlags(GENERATE_FRAME);

        //  Stick
        Dubnium.addFlags(GENERATE_ROD);
        Livermorium.addFlags(GENERATE_ROD);
        Graphene.addFlags(GENERATE_ROD);
        Einsteinium.addFlags(GENERATE_ROD);
        Fermium.addFlags(GENERATE_ROD);
        Mendelevium.addFlags(GENERATE_ROD);
        Berkelium.addFlags(GENERATE_ROD);
        Protactinium.addFlags(GENERATE_ROD);
        Curium.addFlags(GENERATE_ROD);
        Californium.addFlags(GENERATE_ROD);
        Neptunium.addFlags(GENERATE_ROD);
        Nobelium.addFlags(GENERATE_ROD);
        Lawrencium.addFlags(GENERATE_ROD);
        Roentgenium.addFlags(GENERATE_ROD);
        Meitnerium.addFlags(GENERATE_ROD);
        Rutherfordium.addFlags(GENERATE_ROD);
        Vanadium.addFlags(GENERATE_ROD);
        Trinaquadalloy.addFlags(GENERATE_ROD);
        Zeron100.addFlags(GENERATE_ROD);
        CertusQuartz.addFlags(GENERATE_ROD);

        //  Long Stick
        IronMagnetic.addFlags(GENERATE_LONG_ROD);
        WroughtIron.addFlags(GENERATE_LONG_ROD);
        SteelMagnetic.addFlags(GENERATE_LONG_ROD);
        NeodymiumMagnetic.addFlags(GENERATE_LONG_ROD);
        Chrome.addFlags(GENERATE_LONG_ROD);
        BlueAlloy.addFlags(GENERATE_LONG_ROD);

        //  Spring
        Trinium.addFlags(GENERATE_SPRING);
        Tritanium.addFlags(GENERATE_SPRING);
        RutheniumTriniumAmericiumNeutronate.addFlags(GENERATE_SPRING);

        //  Small Spring
        Europium.addFlags(GENERATE_SPRING_SMALL);

        //  Ring
        Palladium.addFlags(GENERATE_RING);
        RTMAlloy.addFlags(GENERATE_RING);
        Osmium.addFlags(GENERATE_RING);
        NaquadahEnriched.addFlags(GENERATE_RING);

        //  Round
        Steel.addFlags(GENERATE_ROUND);

        //  Gear
        RhodiumPlatedPalladium.addFlags(GENERATE_GEAR);
        Darmstadtium.addFlags(GENERATE_GEAR);
        Roentgenium.addFlags(GENERATE_GEAR);
        Lawrencium.addFlags(GENERATE_GEAR);
        CertusQuartz.addFlags(GENERATE_GEAR);

        //  Small Gear
        WroughtIron.addFlags(GENERATE_SMALL_GEAR);
        HSSE.addFlags(GENERATE_SMALL_GEAR);
        Naquadah.addFlags(GENERATE_GEAR);
        Iridium.addFlags(GENERATE_SMALL_GEAR);
        HSLASteel.addFlags(GENERATE_SMALL_GEAR);

        //  Bolt & Screw
        Dubnium.addFlags(GENERATE_BOLT_SCREW);
        Livermorium.addFlags(GENERATE_BOLT_SCREW);
        BlueSteel.addFlags(GENERATE_BOLT_SCREW);
        Rutherfordium.addFlags(GENERATE_BOLT_SCREW);
        CertusQuartz.addFlags(GENERATE_BOLT_SCREW);
        Palladium.addFlags(GENERATE_BOLT_SCREW);

        //  Rotor
        WroughtIron.addFlags(GENERATE_ROTOR);
        Cobalt.addFlags(GENERATE_ROTOR);
        Naquadah.addFlags(GENERATE_ROTOR);
        Meitnerium.addFlags(GENERATE_ROTOR);
        Nobelium.addFlags(GENERATE_ROTOR);

        //  Wire Fine
        Seaborgium.addFlags(GENERATE_FINE_WIRE);
        Kanthal.addFlags(GENERATE_FINE_WIRE);
        Nichrome.addFlags(GENERATE_FINE_WIRE);
        VanadiumGallium.addFlags(GENERATE_FINE_WIRE);
        Titanium.addFlags(GENERATE_FINE_WIRE);
        RutheniumTriniumAmericiumNeutronate.addFlags(GENERATE_FINE_WIRE);
        Graphene.addFlags(GENERATE_FINE_WIRE);
        Neodymium.addFlags(GENERATE_FINE_WIRE);
        Dubnium.addFlags(GENERATE_FINE_WIRE);
        NaquadahEnriched.addFlags(GENERATE_FINE_WIRE);
        Rhenium.addFlags(GENERATE_FINE_WIRE);
        Tungsten.addFlags(GENERATE_FINE_WIRE);
        RTMAlloy.addFlags(GENERATE_FINE_WIRE);
        Trinium.addFlags(GENERATE_FINE_WIRE);
        UraniumTriplatinum.addFlags(GENERATE_FINE_WIRE);
        SolderingAlloy.addFlags(GENERATE_FINE_WIRE);
        Neutronium.addFlags(GENERATE_FINE_WIRE);
        Ruthenium.addFlags(GENERATE_FINE_WIRE);
        Osmium.addFlags(GENERATE_FINE_WIRE);
        Chrome.addFlags(GENERATE_FINE_WIRE);
        Molybdenum.addFlags(GENERATE_FINE_WIRE);
        Vanadium.addFlags(GENERATE_FINE_WIRE);
        CertusQuartz.addFlags(GENERATE_FINE_WIRE);

        //  Crystallization
        Monazite.addFlags(DISABLE_CRYSTALLIZATION);
        Sapphire.addFlags(CRYSTALLIZABLE);
        Ruby.addFlags(CRYSTALLIZABLE);
        Emerald.addFlags(CRYSTALLIZABLE);
        Olivine.addFlags(CRYSTALLIZABLE);
        Amethyst.addFlags(CRYSTALLIZABLE);
        Opal.addFlags(CRYSTALLIZABLE);
        NetherStar.addFlags(CRYSTALLIZABLE);

        //  Nano Swarm
        Carbon.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        CarbonNanotube.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Graphene.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Fullerene.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        FullerenePolymerMatrix.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Platinum.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Europium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Titanium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Tungsten.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Neodymium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Americium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Dubnium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Naquadah.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        NaquadahEnriched.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Naquadria.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Tin.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Tritanium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Orichalcum.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Silver.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Gold.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        WhiteDwarfMatter.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        BlackDwarfMatter.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Galaxium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        Eternity.addFlags(GENERATE_SWARM);

        Steel.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        TungstenSteel.addFlags(GENERATE_SWARM);

        Chrome.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Molybdenum.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Vanadium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        HSSG.addFlags(GENERATE_SWARM);

        Iridium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Osmium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        HSSS.addFlags(GENERATE_SWARM);

        Neutronium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        TungstenCarbide.addFlags(GENERATE_SWARM);
        TitaniumCarbide.addFlags(GENERATE_SWARM);
        TitaniumTungstenCarbide.addFlags(GENERATE_SWARM);

        Electrum.addFlags(GENERATE_SWARM);

        Ruthenium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Ruridit.addFlags(GENERATE_SWARM);

        Copper.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        RoseGold.addFlags(GENERATE_SWARM);
        SterlingSilver.addFlags(GENERATE_SWARM);
        FluxedElectrum.addFlags(GENERATE_SWARM);

        Osmiridium.addFlags(GENERATE_SWARM);

        AstralTitanium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        CelestialTungsten.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Astralium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        Solarium.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        LunaSilver.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        HeavyQuarkDegenerateMatter.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);
        QuantumchromodynamicallyConfinedMatter.addFlags(GENERATE_NANOTUBE, GENERATE_NANOSENSOR, GENERATE_SWARM);

        //  Singularity
        Iron.addFlags(GENERATE_SINGULARITY);
        Copper.addFlags(GENERATE_SINGULARITY);
        Tin.addFlags(GENERATE_SINGULARITY);
        Gold.addFlags(GENERATE_SINGULARITY);
        Silver.addFlags(GENERATE_SINGULARITY);
        Lead.addFlags(GENERATE_SINGULARITY);
        Zinc.addFlags(GENERATE_SINGULARITY);
        Titanium.addFlags(GENERATE_SINGULARITY);
        Tungsten.addFlags(GENERATE_SINGULARITY);
        Vanadium.addFlags(GENERATE_SINGULARITY);
        Platinum.addFlags(GENERATE_SINGULARITY);
        Palladium.addFlags(GENERATE_SINGULARITY);
        Ruthenium.addFlags(GENERATE_SINGULARITY);
        Rhodium.addFlags(GENERATE_SINGULARITY);
        Iridium.addFlags(GENERATE_SINGULARITY);
        Osmium.addFlags(GENERATE_SINGULARITY);

        Naquadah.addFlags(GENERATE_SINGULARITY);
        NaquadahEnriched.addFlags(GENERATE_SINGULARITY);
        Naquadria.addFlags(GENERATE_SINGULARITY);
        Orichalcum.addFlags(GENERATE_SINGULARITY);
        Adamantium.addFlags(GENERATE_SINGULARITY);
        Vibranium.addFlags(GENERATE_SINGULARITY);
        Taranium.addFlags(GENERATE_SINGULARITY);
        Mithril.addFlags(GENERATE_SINGULARITY);
        Solarium.addFlags(GENERATE_SINGULARITY);
        LunaSilver.addFlags(GENERATE_SINGULARITY);
        Neutronium.addFlags(GENERATE_SINGULARITY);
        NetherStar.addFlags(GENERATE_SINGULARITY);
        Glowstone.addFlags(GENERATE_SINGULARITY);
        Ichorium.addFlags(GENERATE_SINGULARITY);
        CrystalMatrix.addFlags(GENERATE_SINGULARITY);

        Duranium.addFlags(GENERATE_SINGULARITY);
        Tritanium.addFlags(GENERATE_SINGULARITY);
        AstralTitanium.addFlags(GENERATE_SINGULARITY);
        CelestialTungsten.addFlags(GENERATE_SINGULARITY);
        Bedrock.addFlags(GENERATE_SINGULARITY);
        Astralium.addFlags(GENERATE_SINGULARITY);
        Hikarium.addFlags(GENERATE_SINGULARITY);
        Rhugnor.addFlags(GENERATE_SINGULARITY);
        DragonBreath.addFlags(GENERATE_SINGULARITY);
        ConcentrateDragonBreath.addFlags(GENERATE_SINGULARITY);
        DragonBlood.addFlags(GENERATE_SINGULARITY);
        Hypogen.addFlags(GENERATE_SINGULARITY);
        CosmicNeutronium.addFlags(GENERATE_SINGULARITY);
        MagnetoHydrodynamicallyConstrainedStarMatter.addFlags(GENERATE_SINGULARITY);
        Spacetime.addFlags(GENERATE_SINGULARITY);
        TranscendentMetal.addFlags(GENERATE_SINGULARITY);

        Water.addFlags(GENERATE_SINGULARITY);
        Lava.addFlags(GENERATE_SINGULARITY);
        Wood.addFlags(GENERATE_SINGULARITY);
        Stone.addFlags(GENERATE_SINGULARITY);
        Netherrack.addFlags(GENERATE_SINGULARITY);
        Endstone.addFlags(GENERATE_SINGULARITY);
        Emerald.addFlags(GENERATE_SINGULARITY);
        Obsidian.addFlags(GENERATE_SINGULARITY);
        TreatedWood.addFlags(GENERATE_SINGULARITY);
        Gunpowder.addFlags(GENERATE_SINGULARITY);
        Diamond.addFlags(GENERATE_SINGULARITY);
        NetherQuartz.addFlags(GENERATE_SINGULARITY);
        Brick.addFlags(GENERATE_SINGULARITY);
        Coal.addFlags(GENERATE_SINGULARITY);
        Steam.addFlags(GENERATE_SINGULARITY);
        Clay.addFlags(GENERATE_SINGULARITY);

        Air.addFlags(GENERATE_SINGULARITY);
        NetherAir.addFlags(GENERATE_SINGULARITY);
        EnderAir.addFlags(GENERATE_SINGULARITY);
        Carbon.addFlags(GENERATE_SINGULARITY);
        Hydrogen.addFlags(GENERATE_SINGULARITY);
        Oxygen.addFlags(GENERATE_SINGULARITY);
        Chlorine.addFlags(GENERATE_SINGULARITY);
        Fluorine.addFlags(GENERATE_SINGULARITY);
        Helium.addFlags(GENERATE_SINGULARITY);
        Neon.addFlags(GENERATE_SINGULARITY);
        Argon.addFlags(GENERATE_SINGULARITY);
        Krypton.addFlags(GENERATE_SINGULARITY);
        Xenon.addFlags(GENERATE_SINGULARITY);
        Radon.addFlags(GENERATE_SINGULARITY);
        MetastableOganesson.addFlags(GENERATE_SINGULARITY);
        RainbowSap.addFlags(GENERATE_SINGULARITY);

        Bronze.addFlags(GENERATE_SINGULARITY);
        Steel.addFlags(GENERATE_SINGULARITY);
        Aluminium.addFlags(GENERATE_SINGULARITY);
        StainlessSteel.addFlags(GENERATE_SINGULARITY);
        CobaltBrass.addFlags(GENERATE_SINGULARITY);
        VanadiumSteel.addFlags(GENERATE_SINGULARITY);
        BlackSteel.addFlags(GENERATE_SINGULARITY);
        BlueSteel.addFlags(GENERATE_SINGULARITY);
        RedSteel.addFlags(GENERATE_SINGULARITY);
        TungstenSteel.addFlags(GENERATE_SINGULARITY);
        HSSG.addFlags(GENERATE_SINGULARITY);
        HSSE.addFlags(GENERATE_SINGULARITY);
        HSSS.addFlags(GENERATE_SINGULARITY);
        Ruridit.addFlags(GENERATE_SINGULARITY);
        Osmiridium.addFlags(GENERATE_SINGULARITY);
        NaquadahAlloy.addFlags(GENERATE_SINGULARITY);

        BlazingPyrotheum.addFlags(GENERATE_SINGULARITY);
        GelidCryotheum.addFlags(GENERATE_SINGULARITY);
        Tiberium.addFlags(GENERATE_SINGULARITY);
        QuarkGluonPlasma.addFlags(GENERATE_SINGULARITY);
        LightQuarks.addFlags(GENERATE_SINGULARITY);
        HeavyQuarks.addFlags(GENERATE_SINGULARITY);
        Gluons.addFlags(GENERATE_SINGULARITY);
        Instantons.addFlags(GENERATE_SINGULARITY);
        HiggsBosons.addFlags(GENERATE_SINGULARITY);
        HeavyLepton.addFlags(GENERATE_SINGULARITY);
        TemporalFluid.addFlags(GENERATE_SINGULARITY);
        HeavyQuarkDegenerateMatter.addFlags(GENERATE_SINGULARITY);
        QuantumchromodynamicallyConfinedMatter.addFlags(GENERATE_SINGULARITY);
        BlackDwarfMatter.addFlags(GENERATE_SINGULARITY);
        WhiteDwarfMatter.addFlags(GENERATE_SINGULARITY);
        DimensionallyTranscendentResidue.addFlags(GENERATE_SINGULARITY);

        Infinity.addFlags(GENERATE_SINGULARITY);
        Eternity.addFlags(GENERATE_SINGULARITY); // For end game

        //  Conflict Solutions
        RockSalt.addFlags(DISABLE_DECOMPOSITION); // Conflict between Potassium Hydroxide and Rock Salt Electrolysis
        Salt.addFlags(DISABLE_DECOMPOSITION); // Conflict between Sodium Chlorate and Salt Electrolysis

        //  Cable Properties
        WireProperties wireProp = RutheniumTriniumAmericiumNeutronate.getProperty(PropertyKey.WIRE);
        wireProp.setSuperconductor(false);
        wireProp.setLossPerBlock(32);
        wireProp.setVoltage((int) V[UIV]);

        if (GTLiteConfigHolder.recipes.enableHarderMolybdenumRheniumProcess) {
            Molybdenite.addFlags(DISABLE_DECOMPOSITION);
            OreProperty oreProp = Molybdenite.getProperty(PropertyKey.ORE);
            oreProp.setDirectSmeltResult(null);
            Powellite.addFlags(DISABLE_DECOMPOSITION);
            Wulfenite.addFlags(DISABLE_DECOMPOSITION);
        }

        if (GTLiteConfigHolder.recipes.enableHarderNiobiumTantalumProcess) {
            Pyrochlore.addFlags(DISABLE_DECOMPOSITION);
            Tantalite.addFlags(DISABLE_DECOMPOSITION);
        }

        BlueVitriol.addFlags(DISABLE_DECOMPOSITION);

        NetherStar.setFormula("PtDc?Ma", true);
    }
}
