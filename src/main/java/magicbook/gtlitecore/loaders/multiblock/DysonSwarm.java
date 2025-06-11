package magicbook.gtlitecore.loaders.multiblock;

import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.unification.material.MarkerMaterials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static gregtech.api.GTValues.*;
import static gregtech.api.recipes.RecipeMaps.ASSEMBLER_RECIPES;
import static gregtech.api.unification.ore.OrePrefix.circuit;
import static gregtech.api.unification.ore.OrePrefix.plate;
import static gregtech.api.unification.ore.OrePrefix.ring;
import static gregtech.api.unification.ore.OrePrefix.wireFine;
import static gregtech.common.items.MetaItems.CARBON_MESH;
import static gregtech.common.items.MetaItems.POWER_THRUSTER_ADVANCED;
import static magicbook.gtlitecore.api.recipe.GTLiteRecipeMaps.DYSON_SWARM_RECIPES;
import static magicbook.gtlitecore.api.unification.GTLiteMaterials.*;
import static magicbook.gtlitecore.api.unification.materials.info.GTLiteOrePrefix.swarm;
import static magicbook.gtlitecore.common.items.GTLiteMetaItems.DYSON_SWARM_MODULE;
import static magicbook.gtlitecore.common.items.GTLiteMetaItems.ETERNAL_DYSON_SWARM_MODULE;

public class DysonSwarm {

    private static final Log log = LogFactory.getLog(DysonSwarm.class);

    public static void init() {

        //  Dyson Swarm Module
        ASSEMBLER_RECIPES.recipeBuilder()
                .input(plate, HY1301, 2)
                .input(circuit, MarkerMaterials.Tier.ZPM)
                .input(CARBON_MESH, 8)
                .input(ring, ActiniumGroupHAlloy, 16)
                .input(POWER_THRUSTER_ADVANCED, 2)
                .input(wireFine, CarbonNanotube, 4)
                .fluidInputs(Polyetheretherketone.getFluid(L * 4))
                .output(DYSON_SWARM_MODULE, 64)
                .EUt(VA[UHV])
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();

        // Unbreakable Swarm Module

        ASSEMBLER_RECIPES.recipeBuilder()
                .input(plate, Periodicium, 16)
                .input(circuit, MarkerMaterials.Tier.OpV, 4)
                .input(swarm, Eternity, 32)
                .input(DYSON_SWARM_MODULE, 16)
                .output(ETERNAL_DYSON_SWARM_MODULE)
                .EUt(VA[UXV])
                .duration(42 * 20)
                .buildAndRegister();

        DYSON_SWARM_RECIPES.recipeBuilder()
                .input(DYSON_SWARM_MODULE)
                .chancedOutput(DYSON_SWARM_MODULE, 5000, 0)
                .EUt(10000000)
                .duration(200)
                .buildAndRegister();

        DYSON_SWARM_RECIPES.recipeBuilder()
                .input(ETERNAL_DYSON_SWARM_MODULE)
                .output(ETERNAL_DYSON_SWARM_MODULE)
                .EUt(100000000)
                .duration(200)
                .buildAndRegister();
    }
}
