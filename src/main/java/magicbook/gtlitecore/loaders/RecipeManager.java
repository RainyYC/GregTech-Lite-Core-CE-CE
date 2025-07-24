package magicbook.gtlitecore.loaders;

import magicbook.gtlitecore.loaders.blocks.Crucibles;
import magicbook.gtlitecore.loaders.blocks.DecorativeBlocks;
import magicbook.gtlitecore.loaders.blocks.Explosives;
import magicbook.gtlitecore.loaders.blocks.WireCoils;
import magicbook.gtlitecore.loaders.chains.*;
import magicbook.gtlitecore.loaders.circuits.*;
import magicbook.gtlitecore.loaders.components.MachineComponents;
import magicbook.gtlitecore.loaders.multiblock.*;
import magicbook.gtlitecore.loaders.oreprocessing.*;

public class RecipeManager {

    private RecipeManager() {}

    public static void registerRecipes() {
        initBlocks();
        MachineComponents.init();
        MachineRecipeLoader.init();
        MiscRecipes.init();
        RecipeConflicts.init();
      
        initChains();
        initOreProcessings();
        initCircuits();
        initMultiRecipes();

        FusionLoader.init();
        OverrideRecipeLoader.init();
    }

    private static void initBlocks() {
        WireCoils.init();
        Crucibles.init();
        Explosives.init();
        DecorativeBlocks.init();
    }

    private static void initChains() {
        AcetyleneChain.init();
        AlgaeChain.init();
        AmmoniaChain.init();
        BoronNitrideChain.init();
        BrineChain.init();
        BPAPolycarbonateChain.init();
        BZMediumChain.init();
        CatChain.init();
        CBDOPolycarbonateChain.init();
        ChlorineChain.init();
        CosmicFabricChain.init();
        CyanogenChain.init();
        DimethylformamideChain.init();
        DragonChain.init();
        EDTAChain.init();
        EtchingMaterialsChain.init();
        EthyleneGlycolChain.init();
        FEPChain.init();
        FullereneChain.init();
        FullerenePolymerMatrixChain.init();
        GalliumNitrideChain.init();
        GrapheneChain.init();
        HexanitrohexaaxaisowurtzitaneChain.init();
        HydrogenPeroxideChain.init();
        //  TODO IsotopesChain.init();
        KaptonChain.init();
        KevlarChain.init();
        MagneticsChain.init();
        MagnetoResonaticChain.init();
        MethylamineChain.init();
        NanotubesChain.init();
        NdYAGChain.init();
        OilChain.init();
        PEDOTChain.init();
        PEEKChain.init();
        PETChain.init();
        PhosphorusChain.init();
        PhotoresistivesChain.init();
        PMMAChain.init();
        PowerIntCircuitChain.init();
        RocketFuelChain.init();
        RubberChain.init();
        SeleniumTelluriumChain.init();
        SuperconductorsChain.init();
        TeflonChain.init();
        TurpentineChain.init();
        ZirconiumChain.init();
        ZylonChain.init();
    }

    private static void initOreProcessings() {
        GemJuiceProcessing.init();
        GermaniumProcessing.init();
        IsaMillOreProcessing.init();
        MolybdenumProcessing.init();
        NiobiumTantalumProcessing.init();
        PlatinumGroupProcessing.init();
        PoloniumRadiumProcessing.init();
        RareEarthProcessing.init();
        RubidiumProcessing.init();
        StrontiumProcessing.init();
        TaraniumProcessing.init();
        ThalliumProcessing.init();
        TungstenProcessing.init();
        VanadiumProcessing.init();
        ZirconiumHafniumProcessing.init();
    }
  
    private static void initCircuits() {
        PrimitiveCircuits.init();
        ProcessorCircuits.init();
        NanoCircuits.init();
        QuantumCircuits.init();
        CrystalCircuits.init();
        WetwareCircuits.init();
        GoowareCircuits.init();
        OpticalCircuits.init();
        SpintronicCircuits.init();
        CosmicCircuits.init();
        SupracausalCircuits.init();
        MagnetoResonaticCircuits.init();
    }

    private static void initMultiRecipes() {
        ComponentAssembler.init();
        ComponentAssemblyLine.init();
        DroneAirport.init();
        TreeGrowthFactory.init();
        Collider.init();
        DecayGenerator.init();
        PlasmaCondenser.init();
        SuprachronalAssemblyLine.init();
        SpaceElevator.init();
        MolecularTransformer.init();
        StellarFurnace.init();
        CosmicRayDetector.init();
        Condenser.init();
        DimensionalOscillator.init();
        PCBFactory.init();
        NeutralNetworkNexus.init();
        QuantumForceTransformer.init();
        TurbineMixer.init();
        BioReactor.init();
        Simulator.init();
        HeatExchanger.init();
        LargeGasCollector.init();
        VirtualCosmosSimulator.init();
        LargeCircuitAssemblyLine.init();
        DysonSwarm.init();
        PlanetaryGasSiphon.init();
        NicollDysonBeamer.init();
        EternityGarden.init();
        SuprachronalNeutroniumForge.init();
        LargeChemicalComplex.init();
    }
}