package magicbook.gtlitecore.common;

import gregtech.api.unification.material.event.MaterialEvent;
import magicbook.gtlitecore.api.GTLiteValues;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.api.unification.OrePrefixAddition;
import magicbook.gtlitecore.api.unification.materials.GTLiteMaterialPropertyAddition;
import magicbook.gtlitecore.api.unification.materials.properties.GTLiteMaterialFlagAddition;
import magicbook.gtlitecore.common.items.GTLiteTools;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = GTLiteValues.MODID)
public class GTLiteEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        GTLiteMaterials.init();
        OrePrefixAddition.init();
        GTLiteMaterialPropertyAddition.init();
        GTLiteMaterialFlagAddition.init();
        GTLiteTools.init();
    }

    /**
     * Player Login Event Handler.
     *
     * @author Magic_Sweepy
     *
     * <p>
     *     This class is create a {@link PlayerEvent.PlayerLoggedInEvent},
     *     when player log in world, then send message of all useful Modpack infoes to chat.
     * </p>
     *
     * @see CommonProxy#preLoad()
     *
     * @version 2.8.8-beta
     */
    public static class PlayerLoginEventHandler {

        private static final String[] lines = {
                "gtlitecore.universal.login_event.split",
                "gtlitecore.universal.login_event.desc.1",
                "",
                "gtlitecore.universal.login_event.desc.2",
                "gtlitecore.universal.login_event.desc.3",
                "",
                "gtlitecore.universal.login_event.desc.4",
                "gtlitecore.universal.login_event.desc.5",
                "gtlitecore.universal.login_event.desc.6",
                "gtlitecore.universal.login_event.desc.7",
                "gtlitecore.universal.login_event.split"
        };

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            Arrays.stream(lines).map(TextComponentTranslation::new)
                    .forEach(event.player::sendMessage);
        }
    }
}
