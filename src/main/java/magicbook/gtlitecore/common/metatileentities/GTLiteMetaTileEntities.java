package magicbook.gtlitecore.common.metatileentities;

import gregicality.multiblocks.common.metatileentities.multiblock.standard.MetaTileEntityLargeMassFabricator;
import gregicality.multiblocks.common.metatileentities.multiblock.standard.MetaTileEntityLargeReplicator;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.SimpleGeneratorMetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import magicbook.gtlitecore.api.metatileentity.single.SimpleSteamMetaTileEntity;
import magicbook.gtlitecore.api.metatileentity.single.SteamProgressIndicators;
import magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.api.utils.GTLiteUtility;
import magicbook.gtlitecore.api.utils.Mods;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import magicbook.gtlitecore.common.GTLiteConfigHolder;
import magicbook.gtlitecore.common.blocks.BlockFusionCasing;
import magicbook.gtlitecore.common.blocks.BlockMetalCasing;
import magicbook.gtlitecore.common.blocks.GTLiteMetaBlocks;
import magicbook.gtlitecore.common.metatileentities.electric.MetaTileEntityLightningRod;
import magicbook.gtlitecore.common.metatileentities.multi.electric.*;
import magicbook.gtlitecore.common.metatileentities.multi.electric.adv.*;
import magicbook.gtlitecore.common.metatileentities.multi.electric.generator.*;
import magicbook.gtlitecore.common.metatileentities.multi.part.*;
import magicbook.gtlitecore.common.metatileentities.multi.steam.MetaTileEntityLargePrimitiveBlastFurnace;
import magicbook.gtlitecore.common.metatileentities.multi.steam.MetaTileEntityLargeSteamCompressor;
import magicbook.gtlitecore.common.metatileentities.multi.storage.MetaTileEntityYottaFluidTank;

import static gregtech.api.GTValues.*;
import static gregtech.common.metatileentities.MetaTileEntities.LASER_INPUT_HATCH_256;
import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;
import static gregtech.common.metatileentities.MetaTileEntities.registerSimpleMetaTileEntity;
import static magicbook.gtlitecore.api.utils.GTLiteUtility.gtliteId;
import static magicbook.gtlitecore.api.utils.MultiblockRegistryHelper.TE_REGISTRY_ID;
import static magicbook.gtlitecore.api.utils.MultiblockRegistryHelper.registerMultiMetaTileEntity;
import static magicbook.gtlitecore.api.utils.MultiblockRegistryHelper.registerPartMetaTileEntity;
import static magicbook.gtlitecore.api.utils.MultiblockRegistryHelper.registerSimpleSteamMetaTileEntity;

/**
 * Meta Tile Entities
 *
 * @author Magic_Sweepy
 *
 * <p>
 *     This class is the {@code MetaTileEntity} class of {@code gtlitecore},
 *     ID range of {@code gtlitecore} meta tile entities:
 *
 *     <ul>
 *         <li>14000-14999: Multiblock Parts,</li>
 *         <li>15000-16000: Single Machines,</li>
 *         <li>16001-20000: Multiblock Machines.</li>
 *     </ul>
 *
 *     Some ID is free in this range (but maybe add new machines in this free IDs).
 * </p>
 *
 * @since 2.8.7-beta
 */
public class GTLiteMetaTileEntities {

    /* ---------------------------------------------------------------- Multiblock Parts ---------------------------------------------------------------- */
    public static final MetaTileEntityAdvancedEnergyHatch[] INPUT_ENERGY_HATCH_4A = new MetaTileEntityAdvancedEnergyHatch[4];
    public static final MetaTileEntityAdvancedEnergyHatch[] INPUT_ENERGY_HATCH_16A = new MetaTileEntityAdvancedEnergyHatch[4];
    public static final MetaTileEntityAdvancedEnergyHatch[] OUTPUT_ENERGY_HATCH_4A = new MetaTileEntityAdvancedEnergyHatch[7];
    public static final MetaTileEntityAdvancedEnergyHatch[] OUTPUT_ENERGY_HATCH_16A = new MetaTileEntityAdvancedEnergyHatch[8];
    public static final MetaTileEntityAdvancedSubstationEnergyHatch[] SUBSTATION_INPUT_ENERGY_HATCH = new MetaTileEntityAdvancedSubstationEnergyHatch[4];
    public static final MetaTileEntityAdvancedSubstationEnergyHatch[] SUBSTATION_OUTPUT_ENERGY_HATCH = new MetaTileEntityAdvancedSubstationEnergyHatch[8];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_INPUT_HATCH_16384A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_INPUT_HATCH_65536A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_INPUT_HATCH_262144A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_INPUT_HATCH_1048576A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_OUTPUT_HATCH_16384A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_OUTPUT_HATCH_65536A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_OUTPUT_HATCH_262144A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityAdvancedLaserHatch[] LASER_OUTPUT_HATCH_1048576A = new MetaTileEntityAdvancedLaserHatch[14];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_4A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_4A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_16A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_16A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_64A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_64A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_256A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_256A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_1024A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_1024A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_4096A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_4096A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_16384A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_16384A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_65536A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_65536A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_262144A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_262144A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_INPUT_ENERGY_HATCH_1048576A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityWirelessEnergyHatch[] WIRELESS_OUTPUT_ENERGY_HATCH_1048576A = new MetaTileEntityWirelessEnergyHatch[15];
    public static final MetaTileEntityAdvancedFluidHatch[] IMPORT_FLUID_HATCH = new MetaTileEntityAdvancedFluidHatch[4];
    public static final MetaTileEntityAdvancedFluidHatch[] EXPORT_FLUID_HATCH = new MetaTileEntityAdvancedFluidHatch[4];
    public static final MetaTileEntityAdvancedItemBus[] IMPORT_ITEM_HATCH = new MetaTileEntityAdvancedItemBus[4];
    public static final MetaTileEntityAdvancedItemBus[] EXPORT_ITEM_HATCH = new MetaTileEntityAdvancedItemBus[4];
    public static final MetaTileEntityAdvancedMultiFluidHatch[] QUADRUPLE_IMPORT_FLUID_HATCH = new MetaTileEntityAdvancedMultiFluidHatch[4];
    public static final MetaTileEntityAdvancedMultiFluidHatch[] QUADRUPLE_EXPORT_FLUID_HATCH = new MetaTileEntityAdvancedMultiFluidHatch[4];
    public static final MetaTileEntityAdvancedMultiFluidHatch[] NONUPLE_IMPORT_FLUID_HATCH = new MetaTileEntityAdvancedMultiFluidHatch[4];
    public static final MetaTileEntityAdvancedMultiFluidHatch[] NONUPLE_EXPORT_FLUID_HATCH = new MetaTileEntityAdvancedMultiFluidHatch[4];
    public static final MetaTileEntityReinforcedRotorHolder[] REINFORCED_ROTOR_HOLDER = new MetaTileEntityReinforcedRotorHolder[14];
    public static MetaTileEntityGrindBallHatch MULTIPART_GRIND_BALL_HATCH;
    public static MetaTileEntityCatalystHatch CATALYST_HATCH;
    public static MetaTileEntityQCComponentEmpty QC_EMPTY_COMPONENT;
    public static final MetaTileEntityQCComponentComputation[] QC_COMPUTATION_COMPONENT = new MetaTileEntityQCComponentComputation[2];
    public static final MetaTileEntityQCComponentCooler[] QC_COOLER_COMPONENT = new MetaTileEntityQCComponentCooler[2];
    public static MetaTileEntityQCComponentBridge QC_BRIDGE_COMPONENT;
    public static MetaTileEntitySingleInputBus SINGLE_INPUT_BUS;
    public static MetaTileEntitySuperInputBus SUPER_INPUT_BUS;
    public static MetaTileEntityBudgetCRIBA4 BUDGET_CRIB_4;
    public static MetaTileEntityBudgetCRIBA8 BUDGET_CRIB_8;
    public static MetaTileEntityBudgetCRIBA12 BUDGET_CRIB_12;
    public static MetaTileEntityBudgetCRIBA16 BUDGET_CRIB_16;
    public static MetaTileEntityBudgetCRIBA20 BUDGET_CRIB_20;
    public static MetaTileEntityParallelHatch[] PARALLEL_HATCH = new MetaTileEntityParallelHatch[V.length - 1];

    /* ----------------------------------------------------------------- Single Machines ---------------------------------------------------------------- */
    public static SimpleMachineMetaTileEntity[] CHEMICAL_DEHYDRATOR = new SimpleMachineMetaTileEntity[V.length - 1];
    public static SimpleSteamMetaTileEntity[] STEAM_VACUUM_CHAMBER = new SimpleSteamMetaTileEntity[2];
    public static SimpleMachineMetaTileEntity[] VACUUM_CHAMBER = new SimpleMachineMetaTileEntity[V.length - 1];
    public static final SimpleGeneratorMetaTileEntity[] NAQUADAH_REACTOR = new SimpleGeneratorMetaTileEntity[4];
    public static final SimpleGeneratorMetaTileEntity[] ROCKET_ENGINE = new SimpleGeneratorMetaTileEntity[3];
    public static SimpleMachineMetaTileEntity[] COMPONENT_ASSEMBLER = new SimpleMachineMetaTileEntity[IV + 1];
    public static SimpleMachineMetaTileEntity[] BIO_REACTOR = new SimpleMachineMetaTileEntity[V.length - 1];
    public static SimpleMachineMetaTileEntity[] CONDENSER = new SimpleMachineMetaTileEntity[V.length - 1];
    public static SimpleMachineMetaTileEntity[] SIMULATOR = new SimpleMachineMetaTileEntity[IV + 1];
    public static final SimpleGeneratorMetaTileEntity[] BIOMASS_GENERATOR = new SimpleGeneratorMetaTileEntity[3];
    public static final MetaTileEntityLightningRod[] LIGHTNING_ROD = new MetaTileEntityLightningRod[3];
    public static SimpleMachineMetaTileEntity[] AUTO_CHISEL = new SimpleMachineMetaTileEntity[3];
    public static SimpleMachineMetaTileEntity[] MASS_FABRICATOR = new SimpleMachineMetaTileEntity[V.length - 1];
    public static SimpleMachineMetaTileEntity[] REPLICATOR = new SimpleMachineMetaTileEntity[V.length - 1];

    /* --------------------------------------------------------------- Multiblock Machines -------------------------------------------------------------- */
    public static MetaTileEntityIndustrialDrillingRig INDUSTRIAL_DRILLING_RIG;
    public static MetaTileEntityCatalyticReformer CATALYTIC_REFORMER;
    public static MetaTileEntitySonicator SONICATOR;
    public static MetaTileEntityHydraulicFracker HYDRAULIC_FRACKER;
    public static MetaTileEntityNanoscaleFabricator NANOSCALE_FABRICATOR;
    public static MetaTileEntityIndustrialRoaster INDUSTRIAL_ROASTER;
    public static MetaTileEntityCrystallizationCrucible CRYSTALLIZATION_CRUCIBLE;
    public static MetaTileEntityCVDUnit CVD_UNIT;
    public static MetaTileEntityPlasmaCVDUnit PLASMA_CVD_UNIT;
    public static MetaTileEntityLaserCVDUnit LASER_CVD_UNIT;
    public static MetaTileEntityBurnerReactor BURNER_REACTOR;
    public static MetaTileEntityCryogenicReactor CRYOGENIC_REACTOR;
    public static MetaTileEntityLargeNaquadahReactor LARGE_NAQUADAH_REACTOR;
    public static MetaTileEntityHyperReactorMkI HYPER_REACTOR_MK1;
    public static MetaTileEntityHyperReactorMkII HYPER_REACTOR_MK2;
    public static MetaTileEntityHyperReactorMkIII HYPER_REACTOR_MK3;
    public static MetaTileEntityIsaMill ISA_MILL;
    public static MetaTileEntityFlotationCellRegulator FLOTATION_CELL_REGULATOR;
    public static MetaTileEntityVacuumDryingFurnace VACUUM_DRYING_FURNACE;
    public static MetaTileEntityVolcanus VOLCANUS;
    public static MetaTileEntityCryogenicFreezer CRYOGENIC_FREEZER;
    public static MetaTileEntityFuelRefineFactory FUEL_REFINE_FACTORY;
    public static MetaTileEntityIonImplantator ION_IMPLANTATOR;
    public static MetaTileEntityUnmannedDroneAirport UNMANNED_DRONE_AIRPORT;
    public static MetaTileEntitySpaceElevator SPACE_ELEVATOR;
    public static MetaTileEntityVirtualCosmosSimulator VIRTUAL_COSMOS_SIMULATOR;
    public static MetaTileEntityCollider COLLIDER;
    public static MetaTileEntityDimensionalOscillator DIMENSIONAL_OSCILLATOR;
    public static MetaTileEntityDecayGenerator DECAY_GENERATOR;

    public static final MetaTileEntityAdvancedFusionReactor[] ADVANCED_FUSION_REACTOR = new MetaTileEntityAdvancedFusionReactor[2];
    public static MetaTileEntityPreciseAssembler PRECISE_ASSEMBLER;
    public static MetaTileEntityComponentAssemblyLine COMPONENT_ASSEMBLY_LINE;
    public static MetaTileEntityAdvancedAssemblyLine ADVANCED_ASSEMBLY_LINE;
    public static final MetaTileEntityCompressedFusionReactor[] COMPRESSED_FUSION_REACTOR = new MetaTileEntityCompressedFusionReactor[5];

    public static MetaTileEntityMegaChemicalReactor MEGA_CHEMICAL_REACTOR;
    public static MetaTileEntityMegaAlloyBlastSmelter MEGA_ALLOY_BLAST_SMELTER;
    public static MetaTileEntityMegaOilCrackingUnit MEGA_OIL_CRACKING_UNIT;
    public static MetaTileEntityStellarFurnace STELLAR_FURNACE;
    public static MetaTileEntityPlasmaCondenser PLASMA_CONDENSER;

    public static MetaTileEntityCosmicRayDetector COSMIC_RAY_DETECTOR;
    public static MetaTileEntitySuprachronalAssemblyLine SUPRACHRONAL_ASSEMBLY_LINE;
    public static MetaTileEntityMolecularTransformer MOLECULAR_TRANSFORMER;
    public static MetaTileEntityQuantumForceTransformer QUANTUM_FORCE_TRANSFORMER;
    public static MetaTileEntityExtremeIndustrialGreenhouse EXTREME_INDUSTRIAL_GREENHOUSE;
    public static MetaTileEntityLargeSteamCompressor LARGE_STEAM_COMPRESSOR;
    public static MetaTileEntityLargePrimitiveBlastFurnace LARGE_PRIMITIVE_BLAST_FURNACE;
    public static MetaTileEntityHeatExchanger HEAT_EXCHANGER;
    public static MetaTileEntityExtremeHeatExchanger EXTREME_HEAT_EXCHANGER;
    public static MetaTileEntityMegaHeatExchanger MEGA_HEAT_EXCHANGER;
    public static MetaTileEntityTreeGrowthFactory TREE_GROWTH_FACTORY;
    public static MetaTileEntityLargeProcessingFactory LARGE_PROCESSING_FACTORY;
    public static MetaTileEntityAdvancedProcessingArray EXTREME_PROCESSING_ARRAY;
    public static MetaTileEntityAdvancedProcessingArray ULTIMATE_PROCESSING_ARRAY;
    public static MetaTileEntityLargeAlloySmelter LARGE_ALLOY_SMELTER;
    public static MetaTileEntityMegaTurbine MEGA_STEAM_TURBINE;
    public static MetaTileEntityMegaTurbine MEGA_GAS_TURBINE;
    public static MetaTileEntityMegaTurbine MEGA_PLASMA_TURBINE;
    public static MetaTileEntityLargeHighPressueSteamTurbine HIGH_PRESSURE_STEAM_TURBINE;
    public static MetaTileEntityLargeSupercriticalSteamTurbine SUPERCRITICAL_STEAM_TURBINE;
    public static MetaTileEntityMegaTurbine MEGA_HIGH_PRESSURE_STEAM_TURBINE;
    public static MetaTileEntityMegaTurbine MEGA_SUPERCRITICAL_STEAM_TURBINE;
    public static MetaTileEntityLargeRocketEngine LARGE_ROCKET_ENGINE;
    public static MetaTileEntityLargeBiomassGenerator LARGE_BIOMASS_GENERATOR;
    public static MetaTileEntityNeutralNetworkNexus NEUTRAL_NETWORK_NEXUS;
    public static MetaTileEntityPCBFactory PCB_FACTORY;
    public static MetaTileEntityDangoteDistillery DANGOTE_DISTILLERY;
    public static MetaTileEntityAmazonWarehousingDepot AMAZON_WAREHOUSING_DEPOT;
    public static MetaTileEntityArcFurnaceArray ARC_FURNACE_ARRAY;
    public static MetaTileEntityElectrolyticTank ELECTROLYTIC_TANK;
    public static MetaTileEntityTurbineMixer TURBINE_MIXER;
    public static MetaTileEntityIndustrialCentrifuge INDUSTRIAL_CENTRIFUGE;
    public static MetaTileEntityZhuHaiFishingPond ZHUHAI_FISHING_POND;
    public static MetaTileEntityCokingTower COKING_TOWER;
    public static MetaTileEntityAdvancedLargeMiner EXTREME_LARGE_MINER;
    public static MetaTileEntityAdvancedLargeMiner ULTIMATE_LARGE_MINER;
    public static MetaTileEntityAdvancedLargeMiner INFINITY_LARGE_MINER;
    public static MetaTileEntityAdvancedFluidDrill ADVANCED_FLUID_DRILL_RIG;
    public static MetaTileEntityAdvancedFluidDrill EXTREME_FLUID_DRILL_RIG;
    public static MetaTileEntityAdvancedFluidDrill ULTIMATE_FLUID_DRILL_RIG;
    public static MetaTileEntityAdvancedFluidDrill INFINITY_FLUID_DRILL_RIG;
    public static MetaTileEntityLargeCircuitAssemblyLine LARGE_CIRCUIT_ASSEMBLY_LINE;
    public static MetaTileEntityElectromagneticSeparationPlant ELECTROMAGNETIC_SEPARATION_PLANT;
    public static MetaTileEntityLargeVacuumChamber LARGE_VACUUM_CHAMBER;
    public static MetaTileEntityLargeBioReactor LARGE_BIO_REACTOR;
    public static MetaTileEntityLargeEUVMaskAligner LARGE_EUV_MASK_ALIGNER;
    public static MetaTileEntityIonLithographyFactory ION_LITHOGRAPHY_FACTORY;
    public static MetaTileEntityLargeHighPressureFormingUnit LARGE_HIGH_PRESSURE_FORMING_UNIT;
    public static MetaTileEntityLargeTurrentLathingFactory LARGE_TURRENT_LATHING_FACTORY;
    public static MetaTileEntityDysonSwarm DYSON_SWARM;
    public static MetaTileEntityIntegratedOreProcessor INTEGRATED_ORE_PROCESSOR;
    public static MetaTileEntityLargeFluidPhaseTransformer LARGE_FLUID_PHASE_TRANSFORMER;
    public static MetaTileEntityIndustrialVacuumChamber INDUSTRIAL_VACUUM_CHAMBER;
    public static MetaTileEntityIndustrialBioReactor INDUSTRIAL_BIO_REACTOR;
    public static MetaTileEntityPlanetaryGasSiphon PLANETARY_GAS_SIPHON;
    public static MetaTileEntityBiowareSimulator BIOWARE_SIMULATOR;
    public static MetaTileEntityAlgaeCultureTank ALGAE_CULTURE_TANK;
    public static MetaTileEntityLargeGasCollector LARGE_GAS_COLLECTOR;
    public static MetaTileEntityUltimateCombustionEngine ULTIMATE_COMBUSTION_ENGINE;
    public static MetaTileEntityIndustrialInductionFurnace INDUSTRIAL_INDUCTION_FURNACE;
    public static MetaTileEntityHorizontalShaftImpactMacerator HORIZONTAL_SHAFT_IMPACT_MACERATOR;
    public static MetaTileEntityTroughTypeOreWasher TROUGH_TYPE_ORE_WASHER;
    public static MetaTileEntityFixedSiftingPlant FIXED_SIFTING_PLANT;
    public static MetaTileEntityLargeWiremillArray LARGE_WIREMILL_ARRAY;
    public static MetaTileEntityLargeChemicalComplex LARGE_CHEMICAL_COMPLEX;
    public static MetaTileEntityElectricImplosionCompressor ELECTRIC_IMPLOSION_COMPRESSOR;
    public static MetaTileEntityLargeRockBreaker LARGE_ROCK_BREAKER;
    public static MetaTileEntityIndustrialRockBreaker INDUSTRIAL_ROCK_BREAKER;
    public static MetaTileEntityLargeMassFabricator LARGE_MASS_FABRICATOR;
    public static MetaTileEntityLargeReplicator LARGE_REPLICATOR;
    public static MetaTileEntityDimensionalMixer DIMENSIONAL_MIXER;
    public static MetaTileEntityAlgaeFarm ALGAE_FARM;
    public static MetaTileEntityMegaBlastFurnace MEGA_BLAST_FURNACE;
    public static MetaTileEntityMegaVacuumFreezer MEGA_VACUUM_FREEZER;
    public static MetaTileEntityCirculativeCoolingTower CIRCULATIVE_COOLING_TOWER;
    public static MetaTileEntityLargeAutoChisel LARGE_AUTO_CHISEL;
    public static MetaTileEntityIndustrial3DPrinter INDUSTRIAL_3D_PRINTER;
    public static MetaTileEntityYottaFluidTank YOTTA_FLUID_TANK;
    public static MetaTileEntityEnergySubstation ENERGY_SUBSTATION;
    public static MetaTileEntityDimensionallyTranscendentPlasmaForge DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE;
    public static MetaTileEntityQuantumComputer QUANTUM_COMPUTER;
    public static MetaTileEntityNicollDysonBeamer NICOLL_DYSON_BEAMER;
    public static MetaTileEntitySuprachronalNeutroniumForge SUPRACHRONAL_NEUTRONIUM_FORGE;
    public static MetaTileEntityMassFabricatorCPU MASS_FABRICATOR_CPU;
    public static MetaTileEntityElementReplicator ELEMENT_REPLICATOR;

    public static void register() {
        //  Multiblock Parts
        //  ID Range: 14000-15000
        registerMultiblockParts();
        //  Simple Machines
        //  ID Range: 15000-16000
        registerSimpleMachines();
        //  Multiblock Machines
        //  Range: 16001-20000
        registerMultiblockMachines();

    }

    private static void registerMultiblockParts() {

        //  ID 14000-14003: UEV-OpV 4A Energy Hatches.
        if (GTLiteConfigHolder.machines.enableHighTier4AEnergyHatches) {
            INPUT_ENERGY_HATCH_4A[0] = registerPartMetaTileEntity(0, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_4a.uev"), UEV, 4, false));
            INPUT_ENERGY_HATCH_4A[1] = registerPartMetaTileEntity(1, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_4a.uiv"), UIV, 4, false));
            INPUT_ENERGY_HATCH_4A[2] = registerPartMetaTileEntity(2, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_4a.uxv"), UXV, 4, false));
            INPUT_ENERGY_HATCH_4A[3] = registerPartMetaTileEntity(3, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_4a.opv"), OpV, 4, false));
        }

        //  ID 14004-14007: UEV-OpV 16A Energy Hatches.
        if (GTLiteConfigHolder.machines.enableHighTier16AEnergyHatches) {
            INPUT_ENERGY_HATCH_16A[0] = registerPartMetaTileEntity(4, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_16a.uev"), UEV, 16, false));
            INPUT_ENERGY_HATCH_16A[1] = registerPartMetaTileEntity(5, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_16a.uiv"), UIV, 16, false));
            INPUT_ENERGY_HATCH_16A[2] = registerPartMetaTileEntity(6, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_16a.uxv"), UXV, 16, false));
            INPUT_ENERGY_HATCH_16A[3] = registerPartMetaTileEntity(7, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.input_16a.opv"), OpV, 16, false));
        }

        //  ID 14008-14014: LV-HV and UEV-OpV 4A Dynamo Hatches.
        if (GTLiteConfigHolder.machines.enableLowPowerHighAmpEnergyHatches) {
            OUTPUT_ENERGY_HATCH_4A[0] = registerPartMetaTileEntity(8, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.lv"), LV, 4, true));
            OUTPUT_ENERGY_HATCH_4A[1] = registerPartMetaTileEntity(9, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.mv"), MV, 4, true));
            OUTPUT_ENERGY_HATCH_4A[2] = registerPartMetaTileEntity(10, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.hv"), HV, 4, true));
        }
        if (GTLiteConfigHolder.machines.enableHighTier4AEnergyHatches) {
            OUTPUT_ENERGY_HATCH_4A[3] = registerPartMetaTileEntity(11, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.uev"), UEV, 4, true));
            OUTPUT_ENERGY_HATCH_4A[4] = registerPartMetaTileEntity(12, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.uiv"), UIV, 4, true));
            OUTPUT_ENERGY_HATCH_4A[5] = registerPartMetaTileEntity(13, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.uxv"), UXV, 4, true));
            OUTPUT_ENERGY_HATCH_4A[6] = registerPartMetaTileEntity(14, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_4a.opv"), OpV, 4, true));
        }

        //  ID 14015-14022: LV-EV and UEV-OpV 16A Dynamo Hatches.
        if (GTLiteConfigHolder.machines.enableLowPowerHighAmpEnergyHatches) {
            OUTPUT_ENERGY_HATCH_16A[0] = registerPartMetaTileEntity(15, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.lv"), LV, 16, true));
            OUTPUT_ENERGY_HATCH_16A[1] = registerPartMetaTileEntity(16, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.mv"), MV, 16, true));
            OUTPUT_ENERGY_HATCH_16A[2] = registerPartMetaTileEntity(17, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.hv"), HV, 16, true));
            OUTPUT_ENERGY_HATCH_16A[3] = registerPartMetaTileEntity(18, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.ev"), EV, 16, true));
        }
        if (GTLiteConfigHolder.machines.enableHighTier16AEnergyHatches) {
            OUTPUT_ENERGY_HATCH_16A[4] = registerPartMetaTileEntity(19, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.uev"), UEV, 16, true));
            OUTPUT_ENERGY_HATCH_16A[5] = registerPartMetaTileEntity(20, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.uiv"), UIV, 16, true));
            OUTPUT_ENERGY_HATCH_16A[6] = registerPartMetaTileEntity(21, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.uxv"), UXV, 16, true));
            OUTPUT_ENERGY_HATCH_16A[7] = registerPartMetaTileEntity(22, new MetaTileEntityAdvancedEnergyHatch(gtliteId("energy_hatch.output_16a.opv"), OpV, 16, true));
        }

        //  ID 14023-14026: UEV-OpV 64A Substation Energy Hatches.
        if (GTLiteConfigHolder.machines.enableHighTier64AEnergyHatches) {
            SUBSTATION_INPUT_ENERGY_HATCH[0] = registerPartMetaTileEntity(23, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.input_64a.uev"), UEV, 64, false));
            SUBSTATION_INPUT_ENERGY_HATCH[1] = registerPartMetaTileEntity(24, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.input_64a.uiv"), UIV, 64, false));
            SUBSTATION_INPUT_ENERGY_HATCH[2] = registerPartMetaTileEntity(25, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.input_64a.uxv"), UXV, 64, false));
            SUBSTATION_INPUT_ENERGY_HATCH[3] = registerPartMetaTileEntity(26, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.input_64a.opv"), OpV, 64, false));
        }

        //  ID 14027-14034: LV-EV and UEV-OpV 64A Substation Dynamo Hatches.
        if (GTLiteConfigHolder.machines.enableLowPowerHighAmpEnergyHatches) {
            SUBSTATION_OUTPUT_ENERGY_HATCH[0] = registerPartMetaTileEntity(27, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.lv"), LV, 64, true));
            SUBSTATION_OUTPUT_ENERGY_HATCH[1] = registerPartMetaTileEntity(28, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.mv"), MV, 64, true));
            SUBSTATION_OUTPUT_ENERGY_HATCH[2] = registerPartMetaTileEntity(29, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.hv"), HV, 64, true));
            SUBSTATION_OUTPUT_ENERGY_HATCH[3] = registerPartMetaTileEntity(30, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.ev"), EV, 64, true));
        }
        if (GTLiteConfigHolder.machines.enableHighTier64AEnergyHatches) {
            SUBSTATION_OUTPUT_ENERGY_HATCH[4] = registerPartMetaTileEntity(31, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.uev"), UEV, 64, true));
            SUBSTATION_OUTPUT_ENERGY_HATCH[5] = registerPartMetaTileEntity(32, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.uiv"), UIV, 64, true));
            SUBSTATION_OUTPUT_ENERGY_HATCH[6] = registerPartMetaTileEntity(33, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.uxv"), UXV, 64, true));
            SUBSTATION_OUTPUT_ENERGY_HATCH[7] = registerPartMetaTileEntity(34, new MetaTileEntityAdvancedSubstationEnergyHatch(gtliteId("substation_hatch.output_64a.opv"), OpV, 64, true));
        }

        //  ID 14035-14111: IV-OpV 16384A, 65536A, 262144A, 1048576A Laser Target/Source Hatches.
        if (GTLiteConfigHolder.machines.enableHiAmpLaserHatches) {
            for (int i = 0; i < (GregTechAPI.isHighTier() ? LASER_INPUT_HATCH_256.length - 1 : Math.min(LASER_INPUT_HATCH_256.length - 1, 4)); ++i) {
                String voltageName = VN[i + 5].toLowerCase();
                LASER_INPUT_HATCH_16384A[i] = registerPartMetaTileEntity(35 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.target_16384a." + voltageName), i + 5, 16384, false));
                LASER_INPUT_HATCH_65536A[i] = registerPartMetaTileEntity(44 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.target_65536a." + voltageName), i + 5, 65536, false));
                LASER_INPUT_HATCH_262144A[i] = registerPartMetaTileEntity(53 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.target_262144a." + voltageName), i + 5, 262144, false));
                LASER_INPUT_HATCH_1048576A[i] = registerPartMetaTileEntity(62 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.target_1048576a." + voltageName), i + 5, 1048576, false));
                LASER_OUTPUT_HATCH_16384A[i] = registerPartMetaTileEntity(71 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.source_16384a." + voltageName), i + 5, 16384, true));
                LASER_OUTPUT_HATCH_65536A[i] = registerPartMetaTileEntity(80 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.source_65536a." + voltageName), i + 5, 65536, true));
                LASER_OUTPUT_HATCH_262144A[i] = registerPartMetaTileEntity(89 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.source_262144a." + voltageName), i + 5, 262144, true));
                LASER_OUTPUT_HATCH_1048576A[i] = registerPartMetaTileEntity(98 + i, new MetaTileEntityAdvancedLaserHatch(gtliteId("laser_hatch.source_1048576a." + voltageName), i + 5, 1048576, true));
            }
        }

        //  ID 14112-14442 : ULV-MAX 2A, 4A, 16A, 64A, 256A, 1024A, 4096A, 16384A, 65536A, 262144A, 1048576A Wireless Energy/Dynamo Hatches.
        if (GTLiteConfigHolder.machines.enableWirelessEnergyHatches) {
            for (int i = 0; i < 15; i++) {
                String voltageName = VN[i].toLowerCase();
                WIRELESS_INPUT_ENERGY_HATCH[i] = registerPartMetaTileEntity(112 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input." + voltageName), i, 2, false));
                WIRELESS_INPUT_ENERGY_HATCH_4A[i] = registerPartMetaTileEntity(112 + 15 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_4a." + voltageName), i, 4, false));
                WIRELESS_INPUT_ENERGY_HATCH_16A[i] = registerPartMetaTileEntity(112 + 15 * 2 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_16a." + voltageName), i, 16, false));
                WIRELESS_INPUT_ENERGY_HATCH_64A[i] = registerPartMetaTileEntity(112 + 15 * 3 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_64a." + voltageName), i, 64, false));
                WIRELESS_INPUT_ENERGY_HATCH_256A[i] = registerPartMetaTileEntity(112 + 15 * 4 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_256a." + voltageName), i, 256, false));
                WIRELESS_INPUT_ENERGY_HATCH_1024A[i] = registerPartMetaTileEntity(112 + 15 * 5 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_1024a." + voltageName), i, 1024, false));
                WIRELESS_INPUT_ENERGY_HATCH_4096A[i] = registerPartMetaTileEntity(112 + 15 * 6 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_4096a." + voltageName), i, 4096, false));
                WIRELESS_INPUT_ENERGY_HATCH_16384A[i] = registerPartMetaTileEntity(112 + 15 * 7 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_16384a." + voltageName), i, 16384, false));
                WIRELESS_INPUT_ENERGY_HATCH_65536A[i] = registerPartMetaTileEntity(112 + 15 * 8 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_65536a." + voltageName), i, 65536, false));
                WIRELESS_INPUT_ENERGY_HATCH_262144A[i] = registerPartMetaTileEntity(112 + 15 * 9 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_262144a." + voltageName), i, 262144, false));
                WIRELESS_INPUT_ENERGY_HATCH_1048576A[i] = registerPartMetaTileEntity(112 + 15 * 10 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.input_1048576a." + voltageName), i, 1048576, false));
                WIRELESS_OUTPUT_ENERGY_HATCH[i] = registerPartMetaTileEntity(112 + 15 * 11 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output." + voltageName), i, 2, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_4A[i] = registerPartMetaTileEntity(112 + 15 * 12 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_4a." + voltageName), i, 4, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_16A[i] = registerPartMetaTileEntity(112 + 15 * 13 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_16a." + voltageName), i, 16, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_64A[i] = registerPartMetaTileEntity(112 + 15 * 14 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_64a." + voltageName), i, 64, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_256A[i] = registerPartMetaTileEntity(112 + 15 * 15 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_256a." + voltageName), i, 256, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_1024A[i] = registerPartMetaTileEntity(112 + 15 * 16 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_1024a." + voltageName), i, 1024, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_4096A[i] = registerPartMetaTileEntity(112 + 15 * 17 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_4096a." + voltageName), i, 4096, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_16384A[i] = registerPartMetaTileEntity(112 + 15 * 18 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_16384a." + voltageName), i, 16384, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_65536A[i] = registerPartMetaTileEntity(112 + 15 * 19 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_65536a." + voltageName), i, 65536, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_262144A[i] = registerPartMetaTileEntity(112 + 15 * 20 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_262144a." + voltageName), i, 262144, true));
                WIRELESS_OUTPUT_ENERGY_HATCH_1048576A[i] = registerPartMetaTileEntity(112 + 15 * 21 + i, new MetaTileEntityWirelessEnergyHatch(gtliteId("wireless_energy_hatch.output_1048576a." + voltageName), i, 1048576, true));
            }
        }

        //  ID 14443-14446: UEV-OpV Item Import Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierItemBuses) {
            IMPORT_ITEM_HATCH[0] = registerPartMetaTileEntity(443, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.import.uev"), UEV, false));
            IMPORT_ITEM_HATCH[1] = registerPartMetaTileEntity(444, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.import.uiv"), UIV, false));
            IMPORT_ITEM_HATCH[2] = registerPartMetaTileEntity(445, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.import.uxv"), UXV, false));
            IMPORT_ITEM_HATCH[3] = registerPartMetaTileEntity(446, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.import.opv"), OpV, false));
        }

        //  ID 14447-14450: UEV-OpV Item Export Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierItemBuses) {
            EXPORT_ITEM_HATCH[0] = registerPartMetaTileEntity(447, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.export.uev"), UEV, true));
            EXPORT_ITEM_HATCH[1] = registerPartMetaTileEntity(448, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.export.uiv"), UIV, true));
            EXPORT_ITEM_HATCH[2] = registerPartMetaTileEntity(449, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.export.uxv"), UXV, true));
            EXPORT_ITEM_HATCH[3] = registerPartMetaTileEntity(450, new MetaTileEntityAdvancedItemBus(gtliteId("item_hatch.export.opv"), OpV, true));
        }

        //  ID 14451-14454: UEV-OpV Fluid Import Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierFluidHatches) {
            IMPORT_FLUID_HATCH[0] = registerPartMetaTileEntity(451, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.import.uev"), UEV, false));
            IMPORT_FLUID_HATCH[1] = registerPartMetaTileEntity(452, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.import.uiv"), UIV, false));
            IMPORT_FLUID_HATCH[2] = registerPartMetaTileEntity(453, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.import.uxv"), UXV, false));
            IMPORT_FLUID_HATCH[3] = registerPartMetaTileEntity(454, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.import.opv"), OpV, false));
        }

        //  ID 14455-14458: UEV-OpV Fluid Export Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierFluidHatches) {
            EXPORT_FLUID_HATCH[0] = registerPartMetaTileEntity(455, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.export.uev"), UEV, true));
            EXPORT_FLUID_HATCH[1] = registerPartMetaTileEntity(456, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.export.uiv"), UIV, true));
            EXPORT_FLUID_HATCH[2] = registerPartMetaTileEntity(457, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.export.uxv"), UXV, true));
            EXPORT_FLUID_HATCH[3] = registerPartMetaTileEntity(458, new MetaTileEntityAdvancedFluidHatch(gtliteId("fluid_hatch.export.opv"), OpV, true));
        }

        //  ID 14459-14462: UEV-OpV Quadruple Fluid Import Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierMultiFluidHatches) {
            QUADRUPLE_IMPORT_FLUID_HATCH[0] = registerPartMetaTileEntity(459, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_4x.uev"), UEV, 4, false));
            QUADRUPLE_IMPORT_FLUID_HATCH[1] = registerPartMetaTileEntity(460, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_4x.uiv"), UIV, 4, false));
            QUADRUPLE_IMPORT_FLUID_HATCH[2] = registerPartMetaTileEntity(461, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_4x.uxv"), UXV, 4, false));
            QUADRUPLE_IMPORT_FLUID_HATCH[3] = registerPartMetaTileEntity(462, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_4x.opv"), OpV, 4, false));
        }

        //  ID 14463-14466: UEV-OpV Nonuple Fluid Import Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierMultiFluidHatches) {
            NONUPLE_IMPORT_FLUID_HATCH[0] = registerPartMetaTileEntity(463, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_9x.uev"), UEV, 9, false));
            NONUPLE_IMPORT_FLUID_HATCH[1] = registerPartMetaTileEntity(464, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_9x.uiv"), UIV, 9, false));
            NONUPLE_IMPORT_FLUID_HATCH[2] = registerPartMetaTileEntity(465, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_9x.uxv"), UXV, 9, false));
            NONUPLE_IMPORT_FLUID_HATCH[3] = registerPartMetaTileEntity(466, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.import_9x.opv"), OpV, 9, false));
        }

        //  ID 14467-14470: UEV-OpV Quadruple Fluid Export Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierMultiFluidHatches) {
            QUADRUPLE_EXPORT_FLUID_HATCH[0] = registerPartMetaTileEntity(467, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_4x.uev"), UEV, 4, true));
            QUADRUPLE_EXPORT_FLUID_HATCH[1] = registerPartMetaTileEntity(468, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_4x.uiv"), UIV, 4, true));
            QUADRUPLE_EXPORT_FLUID_HATCH[2] = registerPartMetaTileEntity(469, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_4x.uxv"), UXV, 4, true));
            QUADRUPLE_EXPORT_FLUID_HATCH[3] = registerPartMetaTileEntity(470, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_4x.opv"), OpV, 4, true));
        }

        //  ID 14471-14474: UEV-OpV Nonuple Fluid Export Hatches.
        if (GTLiteConfigHolder.machines.enableHighTierMultiFluidHatches) {
            NONUPLE_EXPORT_FLUID_HATCH[0] = registerPartMetaTileEntity(471, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_9x.uev"), UEV, 9, true));
            NONUPLE_EXPORT_FLUID_HATCH[1] = registerPartMetaTileEntity(472, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_9x.uiv"), UIV, 9, true));
            NONUPLE_EXPORT_FLUID_HATCH[2] = registerPartMetaTileEntity(473, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_9x.uxv"), UXV, 9, true));
            NONUPLE_EXPORT_FLUID_HATCH[3] = registerPartMetaTileEntity(474, new MetaTileEntityAdvancedMultiFluidHatch(gtliteId("fluid_hatch.export_9x.opv"), OpV, 9, true));
        }

        //  ID 14475-14490: LV-OpV Reinforced Rotor Holders.
        for (int i = 0; i < 13; i++) {
            String voltageName = VN[i + 1].toLowerCase();
            REINFORCED_ROTOR_HOLDER[i] = registerPartMetaTileEntity(475 + i, new MetaTileEntityReinforcedRotorHolder(gtliteId("reinforced_rotor_holder." + voltageName), i + 1));
        }


        //  ID 14491-14499: Special Hatches.
        MULTIPART_GRIND_BALL_HATCH = registerPartMetaTileEntity(491, new MetaTileEntityGrindBallHatch(gtliteId("grind_ball_hatch")));
        CATALYST_HATCH = registerPartMetaTileEntity(492, new MetaTileEntityCatalystHatch(gtliteId("catalyst_hatch")));

        //  ID 14500-14505: Quantum Computer Components
        QC_EMPTY_COMPONENT = registerPartMetaTileEntity(500, new MetaTileEntityQCComponentEmpty(gtliteId("qc_empty_component")));
        QC_COMPUTATION_COMPONENT[0] = registerPartMetaTileEntity(501, new MetaTileEntityQCComponentComputation(gtliteId("qc_computation_component"), false));
        QC_COMPUTATION_COMPONENT[1] = registerPartMetaTileEntity(502, new MetaTileEntityQCComponentComputation(gtliteId("qc_advanced_computation_component"), true));
        QC_COOLER_COMPONENT[0] = registerPartMetaTileEntity(503, new MetaTileEntityQCComponentCooler(gtliteId("qc_heat_sink_component"), false));
        QC_COOLER_COMPONENT[1] = registerPartMetaTileEntity(504, new MetaTileEntityQCComponentCooler(gtliteId("qc_active_cooler_component"), true));
        QC_BRIDGE_COMPONENT = registerPartMetaTileEntity(505, new MetaTileEntityQCComponentBridge(gtliteId("qc_bridge_component")));

        for (int i = 0; i < 14; i++) {
            int id = 15480 + i;
            String name = String.format("parallel_hatch.%s", GTValues.VN[i + 1]);
            PARALLEL_HATCH[i] = registerMetaTileEntity(id, new MetaTileEntityParallelHatch(gtliteId(name), i + 1));
        }
        SINGLE_INPUT_BUS = registerMetaTileEntity(15494, new MetaTileEntitySingleInputBus(gtliteId("single_input_bus")));
        SUPER_INPUT_BUS = registerMetaTileEntity(15495, new MetaTileEntitySuperInputBus(gtliteId("super_input_bus")));
        BUDGET_CRIB_4 = registerMetaTileEntity(15496, new MetaTileEntityBudgetCRIBA4(gtliteId("budget_crib_4")));
        BUDGET_CRIB_8 = registerMetaTileEntity(15497, new MetaTileEntityBudgetCRIBA8(gtliteId("budget_crib_8")));
        BUDGET_CRIB_12 = registerMetaTileEntity(15498, new MetaTileEntityBudgetCRIBA12(gtliteId("budget_crib_12")));
        BUDGET_CRIB_16 = registerMetaTileEntity(15499, new MetaTileEntityBudgetCRIBA16(gtliteId("budget_crib_16")));
        BUDGET_CRIB_20 = registerMetaTileEntity(15500, new MetaTileEntityBudgetCRIBA20(gtliteId("budget_crib_20")));
    }

    private static void registerSimpleMachines() {

        //  ID 15001-15015: LV-OpV Chemical Dehydrators.
        registerSimpleMetaTileEntity(CHEMICAL_DEHYDRATOR, TE_REGISTRY_ID, "chemical_dehydrator", GTLiteRecipeMaps.CHEMICAL_DEHYDRATOR_RECIPES, GTLiteTextures.CHEMICAL_DEHYDRATOR_OVERLAY, true, GTLiteUtility::gtliteId, (tier) -> 16000);

        //  ID 15016-15017: Steam/High Pressure Steam Vacuum Chambers.
        registerSimpleSteamMetaTileEntity(STEAM_VACUUM_CHAMBER, TE_REGISTRY_ID + 16, "vacuum_chamber", GTLiteRecipeMaps.VACUUM_CHAMBER_RECIPES, SteamProgressIndicators.COMPRESS, Textures.GAS_COLLECTOR_OVERLAY, false);

        //  ID 15018-15033: LV-OpV Vacuum Chambers.
        registerSimpleMetaTileEntity(VACUUM_CHAMBER, TE_REGISTRY_ID + 18, "vacuum_chamber", GTLiteRecipeMaps.VACUUM_CHAMBER_RECIPES, Textures.GAS_COLLECTOR_OVERLAY, true, GTLiteUtility::gtliteId, (tier) -> 16000);

        //  ID 15034-15037: IV-UV Naquadah Reactors.
        NAQUADAH_REACTOR[0] = registerMetaTileEntity(TE_REGISTRY_ID + 34, new SimpleGeneratorMetaTileEntity(gtliteId("naquadah_reactor.iv"), GTLiteRecipeMaps.NAQUADAH_REACTOR_RECIPES, GTLiteTextures.NAQUADAH_REACTOR_OVERLAY, IV, GTUtility.genericGeneratorTankSizeFunction));
        NAQUADAH_REACTOR[1] = registerMetaTileEntity(TE_REGISTRY_ID + 35, new SimpleGeneratorMetaTileEntity(gtliteId("naquadah_reactor.luv"), GTLiteRecipeMaps.NAQUADAH_REACTOR_RECIPES, GTLiteTextures.NAQUADAH_REACTOR_OVERLAY, LuV, GTUtility.genericGeneratorTankSizeFunction));
        NAQUADAH_REACTOR[2] = registerMetaTileEntity(TE_REGISTRY_ID + 36, new SimpleGeneratorMetaTileEntity(gtliteId("naquadah_reactor.zpm"), GTLiteRecipeMaps.NAQUADAH_REACTOR_RECIPES, GTLiteTextures.NAQUADAH_REACTOR_OVERLAY, ZPM, GTUtility.genericGeneratorTankSizeFunction));
        NAQUADAH_REACTOR[3] = registerMetaTileEntity(TE_REGISTRY_ID + 37, new SimpleGeneratorMetaTileEntity(gtliteId("naquadah_reactor.uv"), GTLiteRecipeMaps.NAQUADAH_REACTOR_RECIPES, GTLiteTextures.NAQUADAH_REACTOR_OVERLAY, UV, GTUtility.genericGeneratorTankSizeFunction));

        //  ID 15038-15040: HV-IV Rocket Engines.
        ROCKET_ENGINE[0] = registerMetaTileEntity(TE_REGISTRY_ID + 38, new SimpleGeneratorMetaTileEntity(gtliteId("rocket_engine.hv"), GTLiteRecipeMaps.ROCKET_ENGINE_RECIPES, GTLiteTextures.ROCKET_ENGINE_OVERLAY, HV, GTUtility.genericGeneratorTankSizeFunction));
        ROCKET_ENGINE[1] = registerMetaTileEntity(TE_REGISTRY_ID + 39, new SimpleGeneratorMetaTileEntity(gtliteId("rocket_engine.ev"), GTLiteRecipeMaps.ROCKET_ENGINE_RECIPES, GTLiteTextures.ROCKET_ENGINE_OVERLAY, EV, GTUtility.genericGeneratorTankSizeFunction));
        ROCKET_ENGINE[2] = registerMetaTileEntity(TE_REGISTRY_ID + 40, new SimpleGeneratorMetaTileEntity(gtliteId("rocket_engine.iv"), GTLiteRecipeMaps.ROCKET_ENGINE_RECIPES, GTLiteTextures.ROCKET_ENGINE_OVERLAY, IV, GTUtility.genericGeneratorTankSizeFunction));

        //  ID 15041-15056: LV-IV Component Assemblers.
        registerSimpleMetaTileEntity(COMPONENT_ASSEMBLER, TE_REGISTRY_ID + 41, "component_assembler", GTLiteRecipeMaps.COMPONENT_ASSEMBLER_RECIPES, Textures.ASSEMBLER_OVERLAY, true, GTLiteUtility::gtliteId, GTUtility.hvCappedTankSizeFunction);

        //  ID 15057-15072: LV-OpV Biological Reactors.
        registerSimpleMetaTileEntity(BIO_REACTOR, TE_REGISTRY_ID + 57, "bio_reactor", GTLiteRecipeMaps.BIO_REACTOR_RECIPES, GTLiteTextures.BIO_REACTOR_OVERLAY, true, GTLiteUtility::gtliteId, (tier) -> 16000);

        //  ID 15072-15087: LV-IV Simulators.
        if (GTLiteConfigHolder.machines.enableSimulatorChain) {
            registerSimpleMetaTileEntity(SIMULATOR, TE_REGISTRY_ID + 72, "simulator", GTLiteRecipeMaps.SIMULATOR_RECIPES, GTLiteTextures.SIMULATOR_OVERLAY, true, GTLiteUtility::gtliteId, GTUtility.hvCappedTankSizeFunction);
        }

        //  ID 15088-15103: EV-LuV Biomass Generators.
        BIOMASS_GENERATOR[0] = registerMetaTileEntity(TE_REGISTRY_ID + 88, new SimpleGeneratorMetaTileEntity(gtliteId("biomass_generator.ev"), GTLiteRecipeMaps.BIOMASS_GENERATOR_RECIPES, GTLiteTextures.BIOMASS_GENERATOR_OVERLAY, EV, GTUtility.genericGeneratorTankSizeFunction));
        BIOMASS_GENERATOR[1] = registerMetaTileEntity(TE_REGISTRY_ID + 89, new SimpleGeneratorMetaTileEntity(gtliteId("biomass_generator.iv"), GTLiteRecipeMaps.BIOMASS_GENERATOR_RECIPES, GTLiteTextures.BIOMASS_GENERATOR_OVERLAY, IV, GTUtility.genericGeneratorTankSizeFunction));
        BIOMASS_GENERATOR[2] = registerMetaTileEntity(TE_REGISTRY_ID + 90, new SimpleGeneratorMetaTileEntity(gtliteId("biomass_generator.luv"), GTLiteRecipeMaps.BIOMASS_GENERATOR_RECIPES, GTLiteTextures.BIOMASS_GENERATOR_OVERLAY, LuV, GTUtility.genericGeneratorTankSizeFunction));

        //  ID 15104-15119: HV-IV Lightning Rods.
        if (GTLiteConfigHolder.machines.enableLightningRods) {
            LIGHTNING_ROD[0] = registerMetaTileEntity(TE_REGISTRY_ID + 104, new MetaTileEntityLightningRod(gtliteId("lightning_rod.hv"), HV));
            LIGHTNING_ROD[1] = registerMetaTileEntity(TE_REGISTRY_ID + 105, new MetaTileEntityLightningRod(gtliteId("lightning_rod.ev"), EV));
            LIGHTNING_ROD[2] = registerMetaTileEntity(TE_REGISTRY_ID + 106, new MetaTileEntityLightningRod(gtliteId("lightning_rod.iv"), IV));
        }

        //  ID 15120-15135: LV-HV Auto Chisels.
        if (Mods.Chisel.isModLoaded() && GTLiteConfigHolder.compats.enableChiselModule) {
            AUTO_CHISEL[0] = registerMetaTileEntity(TE_REGISTRY_ID + 120, new SimpleMachineMetaTileEntity(gtliteId("auto_chisel.lv"), GTLiteRecipeMaps.AUTO_CHISEL_RECIPES, Textures.AUTOCLAVE_OVERLAY, LV, true, GTUtility.genericGeneratorTankSizeFunction));
            AUTO_CHISEL[1] = registerMetaTileEntity(TE_REGISTRY_ID + 121, new SimpleMachineMetaTileEntity(gtliteId("auto_chisel.mv"), GTLiteRecipeMaps.AUTO_CHISEL_RECIPES, Textures.AUTOCLAVE_OVERLAY, MV, true, GTUtility.genericGeneratorTankSizeFunction));
            AUTO_CHISEL[2] = registerMetaTileEntity(TE_REGISTRY_ID + 122, new SimpleMachineMetaTileEntity(gtliteId("auto_chisel.hv"), GTLiteRecipeMaps.AUTO_CHISEL_RECIPES, Textures.AUTOCLAVE_OVERLAY, HV, true, GTUtility.genericGeneratorTankSizeFunction));
        }

        //  ID 15136-15151: LV-OpV Mass Fabricators.
        if (GTLiteConfigHolder.machines.enableUUMatterChain) {
            registerSimpleMetaTileEntity(MASS_FABRICATOR, TE_REGISTRY_ID + 136, "mass_fabricator", RecipeMaps.MASS_FABRICATOR_RECIPES, Textures.MASS_FABRICATOR_OVERLAY, true, GTLiteUtility::gtliteId, GTUtility.hvCappedTankSizeFunction);
        }

        //  ID 15152-15167: LV-OpV Replicator
        if (GTLiteConfigHolder.machines.enableUUMatterChain) {
            registerSimpleMetaTileEntity(REPLICATOR, TE_REGISTRY_ID + 152, "replicator", RecipeMaps.REPLICATOR_RECIPES, Textures.REPLICATOR_OVERLAY, true, GTLiteUtility::gtliteId, GTUtility.hvCappedTankSizeFunction);
        }
        //  UV-OpV Condenser
        registerSimpleMetaTileEntity(CONDENSER, TE_REGISTRY_ID + 168, "condenser", GTLiteRecipeMaps.CONDENSER_RECIPES, GTLiteTextures.CONDENSER_OVERLAY, true, GTLiteUtility::gtliteId,
                v -> 1000000);

    }

    private static void registerMultiblockMachines() {

        // ID 16001-16012: Gregicality Science Multiblocks.
        INDUSTRIAL_DRILLING_RIG = registerMultiMetaTileEntity(1, new MetaTileEntityIndustrialDrillingRig(gtliteId("industrial_drilling_rig")));
        CATALYTIC_REFORMER = registerMultiMetaTileEntity(2, new MetaTileEntityCatalyticReformer(gtliteId("catalytic_reformer")));
        SONICATOR = registerMultiMetaTileEntity(3, new MetaTileEntitySonicator(gtliteId("sonicator")));
        HYDRAULIC_FRACKER = registerMultiMetaTileEntity(4, new MetaTileEntityHydraulicFracker(gtliteId("hydraulic_fracker"), ZPM));
        NANOSCALE_FABRICATOR = registerMultiMetaTileEntity(5, new MetaTileEntityNanoscaleFabricator(gtliteId("nanoscale_fabricator")));
        INDUSTRIAL_ROASTER = registerMultiMetaTileEntity(6, new MetaTileEntityIndustrialRoaster(gtliteId("industrial_roaster")));
        CRYSTALLIZATION_CRUCIBLE = registerMultiMetaTileEntity(7, new MetaTileEntityCrystallizationCrucible(gtliteId("crystallization_crucible")));
        CVD_UNIT = registerMultiMetaTileEntity(8, new MetaTileEntityCVDUnit(gtliteId("cvd_unit")));
        PLASMA_CVD_UNIT = registerMultiMetaTileEntity(9, new MetaTileEntityPlasmaCVDUnit(gtliteId("plasma_cvd_unit")));
        LASER_CVD_UNIT = registerMultiMetaTileEntity(10, new MetaTileEntityLaserCVDUnit(gtliteId("laser_cvd_unit")));
        BURNER_REACTOR = registerMultiMetaTileEntity(11, new MetaTileEntityBurnerReactor(gtliteId("burner_reactor")));
        CRYOGENIC_REACTOR = registerMultiMetaTileEntity(12, new MetaTileEntityCryogenicReactor(gtliteId("cryogenic_reactor")));

        //  ID 16013-16016: Naquadah Reactors.
        LARGE_NAQUADAH_REACTOR = registerMultiMetaTileEntity(13, new MetaTileEntityLargeNaquadahReactor(gtliteId("large_naquadah_reactor")));
        HYPER_REACTOR_MK1 = registerMultiMetaTileEntity(14, new MetaTileEntityHyperReactorMkI(gtliteId("hyper_reactor_mk1")));
        HYPER_REACTOR_MK2 = registerMultiMetaTileEntity(15, new MetaTileEntityHyperReactorMkII(gtliteId("hyper_reactor_mk2")));
        HYPER_REACTOR_MK3 = registerMultiMetaTileEntity(16, new MetaTileEntityHyperReactorMkIII(gtliteId("hyper_reactor_mk3")));

        //  ID 16017-16019: Isa Mill Ore Processing Machines.
        ISA_MILL = registerMultiMetaTileEntity(17, new MetaTileEntityIsaMill(gtliteId("isa_mill")));
        FLOTATION_CELL_REGULATOR = registerMultiMetaTileEntity(18, new MetaTileEntityFlotationCellRegulator(gtliteId("flotation_cell_regulator")));
        VACUUM_DRYING_FURNACE = registerMultiMetaTileEntity(19, new MetaTileEntityVacuumDryingFurnace(gtliteId("vacuum_drying_furnace")));

        //  ID 16020-16021: Advanced EBF/Vacuum Freezer.
        VOLCANUS = registerMultiMetaTileEntity(20, new MetaTileEntityVolcanus(gtliteId("volcanus")));
        CRYOGENIC_FREEZER = registerMultiMetaTileEntity(21, new MetaTileEntityCryogenicFreezer(gtliteId("cryogenic_freezer")));

        //  ID 16022-16023: My Addition Machines for Gregicality Science.
        FUEL_REFINE_FACTORY = registerMultiMetaTileEntity(22, new MetaTileEntityFuelRefineFactory(gtliteId("fuel_refine_factory")));
        ION_IMPLANTATOR = registerMultiMetaTileEntity(23, new MetaTileEntityIonImplantator(gtliteId("ion_implantator")));

        //  ID 16024-16026: Material Production Machines.
        UNMANNED_DRONE_AIRPORT = registerMultiMetaTileEntity(24, new MetaTileEntityUnmannedDroneAirport(gtliteId("unmanned_drone_airport")));
        SPACE_ELEVATOR = registerMultiMetaTileEntity(25, new MetaTileEntitySpaceElevator(gtliteId("space_elevator")));
        VIRTUAL_COSMOS_SIMULATOR = registerMultiMetaTileEntity(26, new MetaTileEntityVirtualCosmosSimulator(gtliteId("virtual_cosmos_simulator")));

        //  ID 16027-16029: High Energy Physics Machines.
        COLLIDER = registerMultiMetaTileEntity(27, new MetaTileEntityCollider(gtliteId("collider")));
        DIMENSIONAL_OSCILLATOR = registerMultiMetaTileEntity(28, new MetaTileEntityDimensionalOscillator(gtliteId("dimensional_oscillator")));
        DECAY_GENERATOR = registerMultiMetaTileEntity(29, new MetaTileEntityDecayGenerator(gtliteId("decay_generator")));

        //  ID 16030-16031: Advanced Fusion Reactors.
        ADVANCED_FUSION_REACTOR[0] = registerMultiMetaTileEntity(30, new MetaTileEntityAdvancedFusionReactor(gtliteId("fusion_reactor_mk4"), UHV, GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_CASING_MK4), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_COIL_MK2), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.CRYOSTAT), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.DIVERTOR), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.VACUUM)));
        ADVANCED_FUSION_REACTOR[1] = registerMultiMetaTileEntity(31, new MetaTileEntityAdvancedFusionReactor(gtliteId("fusion_reactor_mk5"), UEV, GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_CASING_MK5), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_COIL_MK3), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.ADVANCED_CRYOSTAT), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.ADVANCED_DIVERTOR), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.ADVANCED_VACUUM)));

        //  ID 16032-16034: Advanced Assembling Machines.
        PRECISE_ASSEMBLER = registerMultiMetaTileEntity(32, new MetaTileEntityPreciseAssembler(gtliteId("precise_assembler")));
        COMPONENT_ASSEMBLY_LINE = registerMultiMetaTileEntity(33, new MetaTileEntityComponentAssemblyLine(gtliteId("component_assembly_line")));
        ADVANCED_ASSEMBLY_LINE = registerMultiMetaTileEntity(34, new MetaTileEntityAdvancedAssemblyLine(gtliteId("advanced_assembly_line")));

        //  ID 16035-16039: Compressed Fusion Reactors.
        COMPRESSED_FUSION_REACTOR[0] = registerMultiMetaTileEntity(35, new MetaTileEntityCompressedFusionReactor(gtliteId("compressed_fusion_reactor.luv"), LuV, MetaBlocks.FUSION_CASING.getState(gregtech.common.blocks.BlockFusionCasing.CasingType.FUSION_CASING), MetaBlocks.FUSION_CASING.getState(gregtech.common.blocks.BlockFusionCasing.CasingType.SUPERCONDUCTOR_COIL), MetaBlocks.FRAMES.get(Materials.Naquadah).getBlock(Materials.Naquadah)));
        COMPRESSED_FUSION_REACTOR[1] = registerMultiMetaTileEntity(36, new MetaTileEntityCompressedFusionReactor(gtliteId("compressed_fusion_reactor.zpm"), ZPM, MetaBlocks.FUSION_CASING.getState(gregtech.common.blocks.BlockFusionCasing.CasingType.FUSION_CASING_MK2), MetaBlocks.FUSION_CASING.getState(gregtech.common.blocks.BlockFusionCasing.CasingType.FUSION_COIL), MetaBlocks.FRAMES.get(Materials.NaquadahEnriched).getBlock(Materials.NaquadahEnriched)));
        COMPRESSED_FUSION_REACTOR[2] = registerMultiMetaTileEntity(37, new MetaTileEntityCompressedFusionReactor(gtliteId("compressed_fusion_reactor.uv"), UV, MetaBlocks.FUSION_CASING.getState(gregtech.common.blocks.BlockFusionCasing.CasingType.FUSION_CASING_MK3), MetaBlocks.FUSION_CASING.getState(gregtech.common.blocks.BlockFusionCasing.CasingType.FUSION_COIL), MetaBlocks.FRAMES.get(Materials.Naquadria).getBlock(Materials.Naquadria)));
        COMPRESSED_FUSION_REACTOR[3] = registerMultiMetaTileEntity(38, new MetaTileEntityCompressedFusionReactor(gtliteId("compressed_fusion_reactor.uhv"), UHV, GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_CASING_MK4), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_COIL_MK2), MetaBlocks.FRAMES.get(Materials.Tritanium).getBlock(Materials.Tritanium)));
        COMPRESSED_FUSION_REACTOR[4] = registerMultiMetaTileEntity(39, new MetaTileEntityCompressedFusionReactor(gtliteId("compressed_fusion_reactor.uev"), UEV, GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_CASING_MK5), GTLiteMetaBlocks.FUSION_CASING.getState(BlockFusionCasing.FusionCasingType.FUSION_COIL_MK3), MetaBlocks.FRAMES.get(GTLiteMaterials.Orichalcum).getBlock(GTLiteMaterials.Orichalcum)));

        //  ID 16040-16042: Mega Machines from Bartworks.
        MEGA_ALLOY_BLAST_SMELTER = registerMultiMetaTileEntity(40, new MetaTileEntityMegaAlloyBlastSmelter(gtliteId("mega_alloy_blast_smelter")));
        MEGA_CHEMICAL_REACTOR = registerMultiMetaTileEntity(41, new MetaTileEntityMegaChemicalReactor(gtliteId("mega_chemical_reactor")));
        MEGA_OIL_CRACKING_UNIT = registerMultiMetaTileEntity(42, new MetaTileEntityMegaOilCrackingUnit(gtliteId("mega_oil_cracking_unit")));

        //  ID 16043-16045: Gregicality Legacy Machines.
        STELLAR_FURNACE = registerMultiMetaTileEntity(43, new MetaTileEntityStellarFurnace(gtliteId("stellar_furnace")));
        PLASMA_CONDENSER = registerMultiMetaTileEntity(44, new MetaTileEntityPlasmaCondenser(gtliteId("plasma_condenser")));
        COSMIC_RAY_DETECTOR = registerMultiMetaTileEntity(45, new MetaTileEntityCosmicRayDetector(gtliteId("cosmic_ray_detector")));

        //  ID 16046-16048: High Energy Physics Machines.
        SUPRACHRONAL_ASSEMBLY_LINE = registerMultiMetaTileEntity(46, new MetaTileEntitySuprachronalAssemblyLine(gtliteId("suprachronal_assembly_line")));
        MOLECULAR_TRANSFORMER = registerMultiMetaTileEntity(47, new MetaTileEntityMolecularTransformer(gtliteId("molecular_transformer")));
        QUANTUM_FORCE_TRANSFORMER = registerMultiMetaTileEntity(48, new MetaTileEntityQuantumForceTransformer(gtliteId("quantum_force_transformer")));

        EXTREME_INDUSTRIAL_GREENHOUSE = registerMultiMetaTileEntity(49, new MetaTileEntityExtremeIndustrialGreenhouse(gtliteId("extreme_industrial_greenhouse")));
        LARGE_STEAM_COMPRESSOR = registerMultiMetaTileEntity(50, new MetaTileEntityLargeSteamCompressor(gtliteId("large_steam_compressor")));
        LARGE_PRIMITIVE_BLAST_FURNACE = registerMultiMetaTileEntity(51, new MetaTileEntityLargePrimitiveBlastFurnace(gtliteId("large_primitive_blast_furnace")));
        HEAT_EXCHANGER = registerMultiMetaTileEntity(52, new MetaTileEntityHeatExchanger(gtliteId("heat_exchanger")));
        EXTREME_HEAT_EXCHANGER = registerMultiMetaTileEntity(53, new MetaTileEntityExtremeHeatExchanger(gtliteId("extreme_heat_exchanger")));
        MEGA_HEAT_EXCHANGER = registerMultiMetaTileEntity(54, new MetaTileEntityMegaHeatExchanger(gtliteId("mega_heat_exchanger")));
        TREE_GROWTH_FACTORY = registerMultiMetaTileEntity(55, new MetaTileEntityTreeGrowthFactory(gtliteId("tree_growth_factory")));
        LARGE_PROCESSING_FACTORY = registerMultiMetaTileEntity(56, new MetaTileEntityLargeProcessingFactory(gtliteId("large_processing_factory")));
        EXTREME_PROCESSING_ARRAY = registerMultiMetaTileEntity(57, new MetaTileEntityAdvancedProcessingArray(gtliteId("extreme_processing_array"), 0));
        ULTIMATE_PROCESSING_ARRAY = registerMultiMetaTileEntity(58, new MetaTileEntityAdvancedProcessingArray(gtliteId("ultimate_processing_array"), 1));
        LARGE_ALLOY_SMELTER = registerMultiMetaTileEntity(59, new MetaTileEntityLargeAlloySmelter(gtliteId("large_alloy_smelter")));
        MEGA_STEAM_TURBINE = registerMultiMetaTileEntity(60, new MetaTileEntityMegaTurbine(gtliteId("mega_turbine.steam"), RecipeMaps.STEAM_TURBINE_FUELS, HV, MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STEEL_TURBINE_CASING), MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STEEL_GEARBOX), Textures.SOLID_STEEL_CASING, false, GTLiteTextures.MEGA_TURBINE_OVERLAY));
        MEGA_GAS_TURBINE = registerMultiMetaTileEntity(61, new MetaTileEntityMegaTurbine(gtliteId("mega_turbine.gas"), RecipeMaps.GAS_TURBINE_FUELS, EV, MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STAINLESS_TURBINE_CASING), MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.STAINLESS_STEEL_GEARBOX), Textures.CLEAN_STAINLESS_STEEL_CASING, true, GTLiteTextures.MEGA_TURBINE_OVERLAY));
        MEGA_PLASMA_TURBINE = registerMultiMetaTileEntity(62, new MetaTileEntityMegaTurbine(gtliteId("mega_turbine.plasma"), RecipeMaps.PLASMA_GENERATOR_FUELS, IV, MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TUNGSTENSTEEL_TURBINE_CASING), MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TUNGSTENSTEEL_GEARBOX), Textures.ROBUST_TUNGSTENSTEEL_CASING, false, GTLiteTextures.MEGA_TURBINE_OVERLAY));
        HIGH_PRESSURE_STEAM_TURBINE = registerMultiMetaTileEntity(63, new MetaTileEntityLargeHighPressueSteamTurbine(gtliteId("high_pressure_steam_turbine")));
        SUPERCRITICAL_STEAM_TURBINE = registerMultiMetaTileEntity(64, new MetaTileEntityLargeSupercriticalSteamTurbine(gtliteId("supercritical_steam_turbine")));
        MEGA_HIGH_PRESSURE_STEAM_TURBINE = registerMultiMetaTileEntity(65, new MetaTileEntityMegaTurbine(gtliteId("mega_high_pressure_steam_turbine"), GTLiteRecipeMaps.HIGH_PRESSURE_STEAM_TURBINE_RECIPES, EV, MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TITANIUM_TURBINE_CASING), MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TITANIUM_GEARBOX), Textures.STABLE_TITANIUM_CASING, false, GTLiteTextures.MEGA_TURBINE_OVERLAY));
        MEGA_SUPERCRITICAL_STEAM_TURBINE = registerMultiMetaTileEntity(66, new MetaTileEntityMegaTurbine(gtliteId("mega_supercritical_steam_turbine"), GTLiteRecipeMaps.SUPERCRITICAL_STEAM_TURBINE_RECIPES, LuV, GTLiteMetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.MAR_M200_CASING), MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TUNGSTENSTEEL_GEARBOX), GTLiteTextures.MAR_M200_STEEL_CASING, false, GTLiteTextures.MEGA_TURBINE_OVERLAY));
        LARGE_ROCKET_ENGINE = registerMultiMetaTileEntity(67, new MetaTileEntityLargeRocketEngine(gtliteId("large_rocket_engine")));
        LARGE_BIOMASS_GENERATOR = registerMultiMetaTileEntity(68, new MetaTileEntityLargeBiomassGenerator(gtliteId("large_biomass_generator")));
        NEUTRAL_NETWORK_NEXUS = registerMultiMetaTileEntity(69, new MetaTileEntityNeutralNetworkNexus(gtliteId("neutral_network_nexus")));
        PCB_FACTORY = registerMultiMetaTileEntity(70, new MetaTileEntityPCBFactory(gtliteId("pcb_factory")));
        DANGOTE_DISTILLERY = registerMultiMetaTileEntity(71, new MetaTileEntityDangoteDistillery(gtliteId("dangote_distillery")));
        AMAZON_WAREHOUSING_DEPOT = registerMultiMetaTileEntity(72, new MetaTileEntityAmazonWarehousingDepot(gtliteId("amazon_warehousing_depot")));
        ARC_FURNACE_ARRAY = registerMultiMetaTileEntity(73, new MetaTileEntityArcFurnaceArray(gtliteId("arc_furnace_array")));
        ELECTROLYTIC_TANK = registerMultiMetaTileEntity(74, new MetaTileEntityElectrolyticTank(gtliteId("electrolytic_tank")));
        TURBINE_MIXER = registerMultiMetaTileEntity(75, new MetaTileEntityTurbineMixer(gtliteId("turbine_mixer")));
        INDUSTRIAL_CENTRIFUGE = registerMultiMetaTileEntity(76, new MetaTileEntityIndustrialCentrifuge(gtliteId("industrial_centrifuge")));
        ZHUHAI_FISHING_POND = registerMultiMetaTileEntity(77, new MetaTileEntityZhuHaiFishingPond(gtliteId("zhuhai_fishing_pond")));
        COKING_TOWER = registerMultiMetaTileEntity(78, new MetaTileEntityCokingTower(gtliteId("coking_tower")));
        EXTREME_LARGE_MINER = registerMultiMetaTileEntity(79, new MetaTileEntityAdvancedLargeMiner(gtliteId("large_miner.zpm"), 7, 1, 9, 7, Materials.Protactinium, 64));
        ULTIMATE_LARGE_MINER = registerMultiMetaTileEntity(80, new MetaTileEntityAdvancedLargeMiner(gtliteId("large_miner.uv"), 8, 1, 18, 8, Materials.Einsteinium, 128));
        INFINITY_LARGE_MINER = registerMultiMetaTileEntity(81, new MetaTileEntityAdvancedLargeMiner(gtliteId("large_miner.uhv"), 9, 1, 36, 9, Materials.Mendelevium, 256));
        ADVANCED_FLUID_DRILL_RIG = registerMultiMetaTileEntity(82, new MetaTileEntityAdvancedFluidDrill(gtliteId("fluid_drill_rig.iv"), 5));
        EXTREME_FLUID_DRILL_RIG = registerMultiMetaTileEntity(83, new MetaTileEntityAdvancedFluidDrill(gtliteId("fluid_drill_rig.luv"), 6));
        ULTIMATE_FLUID_DRILL_RIG = registerMultiMetaTileEntity(84, new MetaTileEntityAdvancedFluidDrill(gtliteId("fluid_drill_rig.zpm"), 7));
        INFINITY_FLUID_DRILL_RIG = registerMultiMetaTileEntity(85, new MetaTileEntityAdvancedFluidDrill(gtliteId("fluid_drill_rig.uv"), 8));
        LARGE_CIRCUIT_ASSEMBLY_LINE = registerMultiMetaTileEntity(86, new MetaTileEntityLargeCircuitAssemblyLine(gtliteId("large_circuit_assembly_line")));
        ELECTROMAGNETIC_SEPARATION_PLANT = registerMultiMetaTileEntity(87, new MetaTileEntityElectromagneticSeparationPlant(gtliteId("electromagnetic_separation_plant")));
        LARGE_VACUUM_CHAMBER = registerMultiMetaTileEntity(88, new MetaTileEntityLargeVacuumChamber(gtliteId("large_vacuum_chamber")));
        LARGE_BIO_REACTOR = registerMultiMetaTileEntity(89, new MetaTileEntityLargeBioReactor(gtliteId("large_bio_reactor")));
        LARGE_EUV_MASK_ALIGNER = registerMultiMetaTileEntity(90, new MetaTileEntityLargeEUVMaskAligner(gtliteId("large_euv_mask_aligner")));
        ION_LITHOGRAPHY_FACTORY = registerMultiMetaTileEntity(91, new MetaTileEntityIonLithographyFactory(gtliteId("ion_lithography_factory")));
        LARGE_HIGH_PRESSURE_FORMING_UNIT = registerMultiMetaTileEntity(92, new MetaTileEntityLargeHighPressureFormingUnit(gtliteId("large_high_pressure_forming_unit")));
        LARGE_TURRENT_LATHING_FACTORY = registerMultiMetaTileEntity(93, new MetaTileEntityLargeTurrentLathingFactory(gtliteId("large_turrent_lathing_factory")));
        DYSON_SWARM = registerMultiMetaTileEntity(94, new MetaTileEntityDysonSwarm(gtliteId("dyson_swarm")));
        INTEGRATED_ORE_PROCESSOR = registerMultiMetaTileEntity(95, new MetaTileEntityIntegratedOreProcessor(gtliteId("integrated_ore_processor")));
        LARGE_FLUID_PHASE_TRANSFORMER = registerMultiMetaTileEntity(96, new MetaTileEntityLargeFluidPhaseTransformer(gtliteId("large_fluid_phase_transformer")));
        INDUSTRIAL_VACUUM_CHAMBER = registerMultiMetaTileEntity(97, new MetaTileEntityIndustrialVacuumChamber(gtliteId("industrial_vacuum_chamber")));
        INDUSTRIAL_BIO_REACTOR = registerMultiMetaTileEntity(98, new MetaTileEntityIndustrialBioReactor(gtliteId("industrial_bio_reactor")));
        PLANETARY_GAS_SIPHON = registerMultiMetaTileEntity(99, new MetaTileEntityPlanetaryGasSiphon(gtliteId("planetary_gas_siphon")));
        if (GTLiteConfigHolder.machines.enableSimulatorChain) {
            BIOWARE_SIMULATOR = registerMultiMetaTileEntity(100, new MetaTileEntityBiowareSimulator(gtliteId("bioware_simulator")));
        }
        ALGAE_CULTURE_TANK = registerMultiMetaTileEntity(101, new MetaTileEntityAlgaeCultureTank(gtliteId("algae_culture_tank")));
        LARGE_GAS_COLLECTOR = registerMultiMetaTileEntity(102, new MetaTileEntityLargeGasCollector(gtliteId("large_gas_collector")));
        ULTIMATE_COMBUSTION_ENGINE = registerMultiMetaTileEntity(103, new MetaTileEntityUltimateCombustionEngine(gtliteId("ultimate_combustion_engine"), LuV));
        INDUSTRIAL_INDUCTION_FURNACE = registerMultiMetaTileEntity(104, new MetaTileEntityIndustrialInductionFurnace(gtliteId("industrial_induction_furnace")));
        HORIZONTAL_SHAFT_IMPACT_MACERATOR = registerMultiMetaTileEntity(105, new MetaTileEntityHorizontalShaftImpactMacerator(gtliteId("horizontal_shaft_impact_macerator")));
        TROUGH_TYPE_ORE_WASHER = registerMultiMetaTileEntity(106, new MetaTileEntityTroughTypeOreWasher(gtliteId("trough_type_ore_washer")));
        FIXED_SIFTING_PLANT = registerMultiMetaTileEntity(107, new MetaTileEntityFixedSiftingPlant(gtliteId("fixed_sifting_plant")));
        LARGE_WIREMILL_ARRAY = registerMultiMetaTileEntity(108, new MetaTileEntityLargeWiremillArray(gtliteId("large_wiremill_array")));
        LARGE_CHEMICAL_COMPLEX = registerMultiMetaTileEntity(109, new MetaTileEntityLargeChemicalComplex(gtliteId("large_chemical_complex")));

        //  Free ID: 109
        ELECTRIC_IMPLOSION_COMPRESSOR = registerMultiMetaTileEntity(110, new MetaTileEntityElectricImplosionCompressor(gtliteId("electric_implosion_compressor")));
        //  111 MATERIAL_TRANSMUTATION_ARRAY
        //  112
        //  113
        //  114
        LARGE_ROCK_BREAKER = registerMultiMetaTileEntity(115, new MetaTileEntityLargeRockBreaker(gtliteId("large_rock_breaker")));
        INDUSTRIAL_ROCK_BREAKER = registerMultiMetaTileEntity(116, new MetaTileEntityIndustrialRockBreaker(gtliteId("industrial_rock_breaker")));
        if (GTLiteConfigHolder.machines.enableUUMatterChain) {
            LARGE_MASS_FABRICATOR = registerMultiMetaTileEntity(117, new MetaTileEntityLargeMassFabricator(gtliteId("large_mass_fabricator")));
            LARGE_REPLICATOR = registerMultiMetaTileEntity(118, new MetaTileEntityLargeReplicator(gtliteId("large_replicator")));
        }
        //  119
        DIMENSIONAL_MIXER = registerMultiMetaTileEntity(120, new MetaTileEntityDimensionalMixer(gtliteId("dimensional_mixer")));
        ALGAE_FARM = registerMultiMetaTileEntity(121, new MetaTileEntityAlgaeFarm(gtliteId("algae_farm")));
        MEGA_BLAST_FURNACE = registerMultiMetaTileEntity(122, new MetaTileEntityMegaBlastFurnace(gtliteId("mega_blast_furnace")));
        MEGA_VACUUM_FREEZER = registerMultiMetaTileEntity(123, new MetaTileEntityMegaVacuumFreezer(gtliteId("mega_vacuum_freezer")));
        //  Free ID: 123-129
        CIRCULATIVE_COOLING_TOWER = registerMultiMetaTileEntity(130, new MetaTileEntityCirculativeCoolingTower(gtliteId("circulative_cooling_tower")));
        //  Free ID: 131-139
        if (Mods.Chisel.isModLoaded()) {
            if (GTLiteConfigHolder.compats.enableChiselModule) {
                LARGE_AUTO_CHISEL = registerMultiMetaTileEntity(140, new MetaTileEntityLargeAutoChisel(gtliteId("large_auto_chisel")));
            }
        }
        //  141
        if (Mods.Chisel.isModLoaded()) {
            if (GTLiteConfigHolder.compats.enableChiselModule) {
                INDUSTRIAL_3D_PRINTER = registerMultiMetaTileEntity(142, new MetaTileEntityIndustrial3DPrinter(gtliteId("industrial_3d_printer")));
            }
        }
        //  Free ID: 143-149
        YOTTA_FLUID_TANK = registerMultiMetaTileEntity(150, new MetaTileEntityYottaFluidTank(gtliteId("yotta_fluid_tank")));
        //  151 TWENTY_FIVE_FLUID_TANK
        ENERGY_SUBSTATION = registerMultiMetaTileEntity(152, new MetaTileEntityEnergySubstation(gtliteId("energy_substation")));
        //  Free ID: 153-199
        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE = registerMultiMetaTileEntity(200, new MetaTileEntityDimensionallyTranscendentPlasmaForge(gtliteId("dimensionally_transcendent_plasma_forge")));
        QUANTUM_COMPUTER = registerMultiMetaTileEntity(201, new MetaTileEntityQuantumComputer(gtliteId("quantum_computer")));
        NICOLL_DYSON_BEAMER = registerMultiMetaTileEntity(202, new MetaTileEntityNicollDysonBeamer(gtliteId("nicoll_dyson_beamer")));
        SUPRACHRONAL_NEUTRONIUM_FORGE = registerMultiMetaTileEntity(203, new MetaTileEntitySuprachronalNeutroniumForge(gtliteId("suprachronal_neutronium_forge")));
        MASS_FABRICATOR_CPU = registerMultiMetaTileEntity(204, new MetaTileEntityMassFabricatorCPU(gtliteId("mass_fabricator_cpu")));
        ELEMENT_REPLICATOR = registerMultiMetaTileEntity(205, new MetaTileEntityElementReplicator(gtliteId("element_replicator")));
    }
}
