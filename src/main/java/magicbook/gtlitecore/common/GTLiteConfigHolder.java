package magicbook.gtlitecore.common;

import lombok.NoArgsConstructor;
import magicbook.gtlitecore.GTLiteCore;
import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

@Config(modid = GTLiteCore.MODID)
public class GTLiteConfigHolder {

    @Comment({"Config options for Mod Compatibility"})
    @Name("Compatibility Options")
    @RequiresMcRestart
    public static CompatibilityOptions compats = new CompatibilityOptions();

    @Comment({"Config options for GregTech Lite Machines"})
    @Name("Machine Options")
    @RequiresMcRestart
    public static MachineOptions machines = new MachineOptions();

    @Comment({"Config options for Miscellaneous Features"})
    @Name("Misc Options")
    @RequiresMcRestart
    public static MiscOptions misc = new MiscOptions();

    @Comment({"Config Options for GregTech Lite, GregTech and Vanilla Recipes"})
    @Name("Recipe Options")
    @RequiresMcRestart
    public static RecipeOptions recipes = new RecipeOptions();

    @Comment({"Config Options for Tools and Armor"})
    @Name("Tool and Armor Options")
    @RequiresMcRestart
    public static ToolOptions tools = new ToolOptions();

    public GTLiteConfigHolder() {}

    public static class CompatibilityOptions {

        @Comment({"Enable easier Large Circuit Assembler (GCYM) recipe, requires IV-tier materials and LuV-tier circuit.", "Default: true"})
        public boolean enableEasierLargeCircuitAssembler = true;

        @Comment({"Enable easier Mega Blast Furnace/Vacuum Freezer (GCYM) recipes, requires UV-tier materials.", "Default: true"})
        public boolean enableEasierMegaMachines = true;

        @Comment({"Enable high tier Tiered Hatch (UHV-MAX) recipes.", "Default: true"})
        public boolean enableHighTierTieredHatch = true;

        @Comment({"Enable some recipe tweaks of GTFO (such as Greenhouse Glass).", "Default: true"})
        public boolean enableGTFOTweaks = true;

        @Comment({"Enable Extended Renderer of Data Items (such as Data stick).", "Default: true"})
        public boolean enableDataItemRenderer = true;

        @Comment({"Enable Integration Module of The One Probe.", "Default: true"})
        @RequiresMcRestart
        public boolean enableTOPModule = true;

        @Comment({"Enable Recipe Output Info. [The One Probe Module]", "Default: true"})
        public boolean enableTOPRecipeOutputInfo = true;

        @Comment({"Enable Integration Module of Architecture Craft.", "Default: true"})
        @RequiresMcRestart
        public boolean enableArchitectureCraftModule = true;

        @Comment({"Enable Integration Module of Chisel.", "Default: true"})
        @RequiresMcRestart
        public boolean enableChiselModule = true;

        public CompatibilityOptions() {}
    }

    @NoArgsConstructor
    public static class MachineOptions {
        @Config.Comment("延迟巨构设备检测频率，可改善TPS性能(目前只在压缩聚变 光刻厂 加速器 PCB 锻炉测试实装，可能会造成未知后果！)")
        @Config.RequiresMcRestart
        @Config.Name("Delay Structure Check switch")
        public boolean DelayStructureCheckSwitch = true;

        @Comment({"Enable High Tier Item Buses (UEV-OpV) and its recipes,",
                  "these Advanced Item Buses have 121-196 slots.",
                  "Default: true"})
        @Name("Advanced Item Buses")
        @RequiresMcRestart
        public boolean enableHighTierItemBuses = true;

        @Comment({"Enable High Tier Fluid Hatches (UEV-OpV) and its recipes,",
                  "these Advanced Fluid Hatches have 8192kL-65536kL capacity.",
                  "Default: true"})
        @Name("Advanced Fluid Hatches")
        @RequiresMcRestart
        public boolean enableHighTierFluidHatches = true;

        @Comment({"Enable High Tier Multi Fluid Hatches (UEV-OpV) and its recipes,",
                  "these Multi Fluid Hatches have 4x and 9x two forms, just like vanilla:",
                  "for 4x Fluid Hatches, these Advanced Hatches have 2048kL-16384kL capacity;",
                  "for 9x Fluid Hatches, these Advanced hatches have 1024kL-8192kL capacity.",
                  "Default: true"})
        @Name("Advanced Multi Fluid Hatches")
        @RequiresMcRestart
        public boolean enableHighTierMultiFluidHatches = true;

        @Comment({"Enable High Tier Energy/Dynamo Hatches (UEV-OpV) and its recipes,",
                  "Warning: this option does not cause UEV-OpV Energy/Dynamo Hatches not registried,",
                  "only hidden them and not register recipes for them.",
                  "Default: true"})
        @Name("Advanced Energy/Dynamo Hatches")
        @RequiresMcRestart
        public boolean enableHighTierEnergyHatches = true;

        @Comment({"Enable High Tier Laser Target/Source Hatches and its recipes.",
                  "Warning: this option does not cause UEV-OpV Laser Target/Source Hatches not registried,",
                  "only hidden them and not register recipes for them."})
        @Name("Advanced Laser Target/Source Hatches")
        @RequiresMcRestart
        public boolean enableHighTierLaserHatches = true;

        @Comment({"Enable Hi-Amp Laser Target/Source Hatches (IV-OpV),",
                  "consists of: 16384A, 65536A, 262144A and 1048576A."})
        @Name("High-Amp Laser Target/Source Hatches")
        @RequiresMcRestart
        public boolean enableHiAmpLaserHatches = true;

        @Comment({"Enable High Tier 4A Energy/Dynamo Hatches (UEV-OpV) and its recipes.",
                "Default: true"})
        @Name("Advanced 4A Energy/Dynamo Hatches")
        @RequiresMcRestart
        public boolean enableHighTier4AEnergyHatches = true;

        @Comment({"Enable High Tier 16A Energy/Dynamo Hatches (UEV-OpV) and its recipes.",
                  "Default: true"})
        @Name("Advanced 16A Energy/Dynamo Hatches")
        @RequiresMcRestart
        public boolean enableHighTier16AEnergyHatches = true;

        @Comment({"Enable High Tier 64A Substation Energy/Dynamo Hatches (UEV-OpV) and its recipes.",
                  "Default: true"})
        @Name("Advanced 64A Energy/Dynamo Hatches")
        @RequiresMcRestart
        public boolean enableHighTier64AEnergyHatches = true;

        @Comment({"Enable High-Amp Energy Hatches for LV-EV and its recipes,",
                  "consists of 4A, 16A Dynamo Hatches and 64A Substation Dynamo Hatches.",
                  "Warning: This option will cause breaking change of vanilla GregTech games,",
                  "please tweak it carefully.",
                  "Default: true"
        })
        @Name("LV-EV High-Amp Energy Hatches")
        @RequiresMcRestart
        public boolean enableLowPowerHighAmpEnergyHatches = true;

        @Comment({"Enable Wireless Energy/Dynamo Hatches (ULV-MAX) and its recipes.",
                  "These Energy/Dynamo Hatches will interact energy via `Wireless Energy Network`,",
                  "which is a special World Saved Data in your saves, and its have many High-Amp versions,",
                  "consists of: 2A (common), 4A, 16A, 64A, 256A, 1024A, 4096A, 16384A, 65536A",
                  "262144A and 1048576A, each Wireless Energy/Dynamo Hatches has an owner.",
                  "Default: true"})
        @Name("Wireless Energy Hatches")
        @RequiresMcRestart
        public boolean enableWirelessEnergyHatches = true;

        @Comment({"Enable High Tier Transformers (UEV-OpV) and its recipes.",
                  "Warning: this option does not cause UEV-OpV Transformers not registried,",
                  "only hidden them and not register recipes for them.",
                  "Default: true"})
        @Name("Advanced Transformers")
        @RequiresMcRestart
        public boolean enableHighTierTransformer = true;

        @Comment({"Enable High Tier Power Transformers (UEV-OpV) and its recipes.",
                  "Warning: this option does not cause UEV-OpV Power Transformers not registried,",
                  "only hidden them and not register recipes for them.",
                  "Default: true"})
        @Name("Advanced Power Transformers")
        @RequiresMcRestart
        public boolean enableHighTierPowerTransformer = true;

        @Comment({"Enable High Tier Hi-Amp Transformers (UEV-OpV) and its recipes.",
                  "Warning: this option does not cause UEV-OpV Hi-Amp Transformers not registried,",
                  "only hidden them and not register recipes for them.",
                  "Default: true"})
        @Name("Advanced Hi-Amp Transformers")
        @RequiresMcRestart
        public boolean enableHighTierHiAmpTransformer = true;

        @Comment({"Enable ME Crafting Input Bus."})
        @Name("ME Crafting Input Bus")
        @RequiresMcRestart
        public boolean enableMECraftingInputBus = false;

        @Comment({"Enable ME Crafting Input Slave, required GregTech CEu version after 2.9.0."})
        @Name("ME Crafting Input Slave")
        @RequiresMcRestart
        public boolean enableMECraftingInputSlave = false;

        @Comment({"Enable Harder Steam Machine recipes, requires ULV components,",
                  "such as ULV Motor, Piston and Conveyor Module.",
                  "Default: true"})
        @Name("Harder Steam Machines")
        @RequiresMcRestart
        public boolean enableHarderSteamMachines = true;

        @Comment({"Enable LV-IV Simulators, Bioware Simulator and its recipes,",
                  "specific change and other datas can tweak via config too,",
                  "please see `outputChanceEachSimulate` (Output Chance of Simulator),",
                  "`outputChanceBoostEachSimulate` (Output Chance Boost of Simulator),",
                  "and `durationEachSimulate` (Duration of Simulator).",
                  "Default: true"})
        @Name("Simulator Chain")
        @RequiresMcRestart
        public boolean enableSimulatorChain = true;

        @Comment({})
        @Name("Output Chance of Simulator")
        @RangeInt(min = 1, max = 10000)
        @RequiresWorldRestart
        public int outputChanceEachSimulate = 1000;

        @Comment({})
        @Name("Output Chance Boost of Simulator")
        @RangeInt(min = 1, max = 10000)
        @RequiresWorldRestart
        public int outputChanceBoostEachSimulate = 100;

        @Comment({})
        @Name("Duration of Each Simulator recipes")
        @RangeInt(min = 1)
        @RequiresWorldRestart
        public int durationEachSimulate = 1200;

        @Comment({"Enable LV-OpV Mass Fabricator, Replicator, correspondence Multiblock structures,",
                  "and its recipes. Specific change and other datas can tweak via config too,",
                  "please see: "})
        @Name("UU Matter Chain")
        @RequiresMcRestart
        public boolean enableUUMatterChain = true;

        @Comment({"Basic Time Factor of Replicator, change this to set the Replication to be quicker or longer.",
                  "Default: 750"})
        @Name("Replicator Time Factor")
        @RangeInt(min = 1)
        @RequiresWorldRestart
        public int replicationTimeFactor = 750;

        @Comment({"Enable IV-ZPM Lightning Rods."})
        @Name("Lightning Rods")
        @RequiresMcRestart
        public boolean enableLightningRods = true;

        @Comment({"Enable Nuclear Fission and related chains, consists of Nuclear Reactor,",
                  "Isotope Material processing and recipes.",
                  "Default: false"})
        @Name("Nuclear Fission")
        @RequiresMcRestart
        public boolean enableNuclearFissionChain = false;
    }

    public static class MiscOptions {

        @Comment({"Allow combustion generator use Rocket Fuel.", "Default: false"})
        public boolean enableRocketFuelEngineRecipe = false;

        @Comment({"Rocket Fuel EU/t product in Rocket Engine.", "Default: 512"})
        @RangeInt(min = 1, max = 8192)
        public int heatValueRocketFuel = 512;

        @Comment({"RP-1 Rocket Fuel EU/t product in Rocket Engine.", "Default: 512"})
        @RangeInt(min = 1, max = 8192)
        public int heatValueRP1RocketFuel = 512;

        @Comment({"Dense Hydrazine Mixture Rocket Fuel EU/t product in Rocket Engine.", "Default: 2048"})
        @RangeInt(min = 1, max = 8192)
        public int heatValueDenseHydrazineMixtureRocketFuel = 2048;

        @Comment({"Methylhydrazine Nitrate Rocket Fuel EU/t product in Rocket Engine.", "Default: 2048"})
        @RangeInt(min = 1, max = 8192)
        public int heatValueMethylhydrazineNitrateRocketFuel = 2048;

        @Comment({"Naquadah Fuels EU/t product in Naquadah Reactor.", "Default: 2048"})
        @RangeInt(min = 1, max = 2097152)
        public int heatValueNaquadahFuel = 2048;

        @Comment({"Taranium Fuels EU/t product in Naquadah Reactor.", "Default: 4096"})
        @RangeInt(min = 1, max = 2097152)
        public int heatValueTaraniumFuel = 4096;

        @Comment({"Enriched Taranium Fuels EU/t product in Naquadah Reactor.", "Default: 8192"})
        @RangeInt(min = 1, max = 2097152)
        public int heatValueEnrichedTaraniumFuel = 8192;

        @Comment({"Allow Gas Turbine use Naquadah Gas to product EU/t.", "Default: true"})
        public boolean enableNaquadahGasTurbineRecipe = true;

        @Comment({"Naquadah Gas EU/t product in Gas Turbine.", "Default: 32"})
        @RangeInt(min = 1, max = 2048)
        public int heatValueNaquadahGas = 32;

        @Comment({"Allow Gas Turbine use Exotic Gases to product EU/t.", "Default: true"})
        public boolean enableExoticGasTurbineRecipe = true;

        @Comment({"Crude Exotic Gas EU/t product in Gas Turbine.", "Default: 512"})
        @RangeInt(min = 1, max = 1024)
        public int heatValueCrudeExoticGas = 512;

        @Comment({"Exotic Gases EU/t product in Gas Turbine.", "Default: 1024"})
        @RangeInt(min = 1, max = 2048)
        public int heatValueExoticGas = 1024;

        @Comment({"Hyper Fuels EU/t product in Hyper Reactor Mark 1.", "Default: 32768"})
        @RangeInt(min = 1, max = 8388608)
        public int heatValueHyperFuelsMark1 = 32768;

        @Comment({"Hyper Fuels EU/t product in Hyper Reactor Mark 2.", "Default: 131072"})
        @RangeInt(min = 1, max = 33554432)
        public int heatValueHyperFuelsMark2 = 131072;

        @Comment({"Hyper Fuels EU/t product in Hyper Reactor Mark 3.", "Default: 524288"})
        @RangeInt(min = 1, max = 134217728)
        public int heatValueHyperFuelsMark3 = 524288;

        @Comment({"Hyper Fuel Mark 2 EU/t product in Hyper Reactor Mark 1.", "Default: 131072"})
        @RangeInt(min = 1, max = 8388608)
        public int heatValueHyperFuelMark2 = 131072;

        @Comment({"Hyper Fuel Mark 3 EU/t product in Hyper Reactor Mark 2.", "Default: 524288"})
        @RangeInt(min = 1, max = 33554432)
        public int heatValueHyperFuelMark3 = 524288;

        @Comment({"Hyper Fuel Mark 4 EU/t product in Hyper Reactor Mark 3.", "Default: 2097152"})
        @RangeInt(min = 1, max = 134217728)
        public int heatValueHyperFuelMark4 = 2097152;

        @Comment({"Biomass EU/t product in Biomass Generator.", "Default: 120"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueBiomass = 120;

        @Comment({"Fermented Biomass EU/t product in Biomass Generator.", "Default: 120"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueFermentedBiomass = 120;

        @Comment({"Bacterial Sludge EU/t product in Biomass Generator.", "Default: 480"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueBacterialSludge = 480;

        @Comment({"Sterile Growth Medium EU/t product in Biomass Generator.", "Default: 1920"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueSterileGrowthMedium = 1920;

        @Comment({"Mutagen EU/t product in Biomass Generator.", "Default: 480"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueMutagen = 480;

        @Comment({"Exotic Mutagen EU/t product in Biomass Generator.", "Default: 1920"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueExoticMutagen = 1920;

        @Comment({"Thorium Based Liquid Fuel EU/t product in Combustion Generator (in Naquadah Reactor will be 4x).", "Default: 1620"})
        @RangeInt(min = 1, max = 32768)
        public int heatValueThoriumBasedLiquidFuel = 1620;

        @Comment({"Uranium Based Liquid Fuel EU/t product in Combustion Generator (in Naquadah Reactor will be 4x).", "Default: 6480"})
        @RangeInt(min = 1, max = 131072)
        public int heatValueUraniumBasedLiquidFuel = 6480;

        @Comment({"Plutonium Based Liquid Fuel EU/t product in Combustion Generator (in Naquadah Reactor will be 4x).", "Default: 25920"})
        @RangeInt(min = 1, max = 524288)
        public int heatValuePlutoniumBasedLiquidFuel = 25920;

        @Comment({"Allow Bio Reactor use some GregTech vanilla recipes in Chemical Reactor.", "Default: true"})
        public boolean enableBioReactorVanillaRecipe = true;

        @Comment({"Allow Creative items (e.g. Creative Quantum Tank) be makable in Survival mode.", "Default: true"})
        public boolean enableCreativeRecipe = true;

        @Comment({"定时通知无线电网状态。", "Default: true"})
        public boolean notifyWirelessEnergy = true;

        public MiscOptions() {}
    }

    public static class RecipeOptions {

        @Comment({"Enable magneto resonatic circuit and its recipes.", "Default: true"})
        public boolean enableMagnetoResonaticCircuit = true;

        @Comment({"Enable easier Processor Assembly recipes, now it products two per run.", "Default: true"})
        public boolean enableEasierProcessorAssembly = true;

        @Comment({"Enable harder Vacuum Tube recipes, requires Vacuum Chamber and more materials.", "Default: true"})
        public boolean enableHarderVacuumTube = true;

        @Comment({"Enable harder Crystal SoC recipes, requires Cubic Zirconium and more materials.", "Default: true"})
        public boolean enableHarderCrystalSoC = true;

        @Comment({"Enable harder Wetware Board recipes, requires Kapton-K.", "Default: true"})
        public boolean enableHarderWetwareBoard = true;

        @Comment({"Enable harder Platinum Group process.", "Default: true"})
        public boolean enableHarderPlatinumGroupProcess = true;

        @Comment({"Enable harder Rare Earth process.", "Default: true"})
        public boolean enableHarderRareEarthProcess = true;

        @Comment({"Enable harder Tungsten process.", "Default: true"})
        public boolean enableHarderTungstenProcess = true;

        @Comment({"Enable harder Molybdenum Rhenium process.", "Default: true"})
        public boolean enableHarderMolybdenumRheniumProcess = true;

        @Comment({"Enable harder Niobium Tantalum process.", "Default: true"})
        public boolean enableHarderNiobiumTantalumProcess = true;

        @Comment({"Enable harder Graphene process.", "Default: true"})
        public boolean enableHarderGrapheneProcess = true;

        @Comment({"Enable harder Ammonia process.", "Default: true"})
        public boolean enableHarderAmmoniaProcess = true;

        @Comment({"Enable easier Hydrogen Cyanide process.", "Default: true"})
        public boolean enableEasierHydrogenCyanideProcess = true;

        @Comment({"Enable harder Crucible recipes, requires Vacuum Chamber and some materials.", "Default: true"})
        public boolean enableHarderCrucible = true;

        public RecipeOptions() {}
    }

    public static class ToolOptions {

        @Comment({"Enable EV-tier Prospector.", "Default: true"})
        public boolean enableEVProspector = true;

        @Comment({"Enable ZPM-tier Prospector.", "Default: true"})
        public boolean enableZPMProspector = true;

        @Comment({"Enable high tier batteries (UHV-MAX).", "Default: true"})
        public boolean enableHighTierBattery = true;

        @Comment({"Enable more ultimate batteries (UEV-OpV).", "Default: true"})
        public boolean enableHighTierUltimateBattery = true;

        @Comment({"Enable high tier solar panels (UHV-MAX).", "Default: true"})
        public boolean enableHighTierSolarPanel = true;

        public ToolOptions() {}
    }
}
