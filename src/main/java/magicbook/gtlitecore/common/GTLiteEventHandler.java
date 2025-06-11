package magicbook.gtlitecore.common;

import gregtech.api.unification.material.event.MaterialEvent;
import magicbook.gtlitecore.api.GTLiteValues;
import magicbook.gtlitecore.api.misc.WirelessEnergyNetworkManager;
import magicbook.gtlitecore.api.unification.GTLiteMaterials;
import magicbook.gtlitecore.api.unification.OrePrefixAddition;
import magicbook.gtlitecore.api.unification.materials.GTLiteMaterialPropertyAddition;
import magicbook.gtlitecore.api.unification.materials.helper.MaterialHelperManager;
import magicbook.gtlitecore.api.unification.materials.properties.GTLiteMaterialFlagAddition;
import magicbook.gtlitecore.common.items.GTLiteTools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.Int;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = GTLiteValues.MODID)
public class GTLiteEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerMaterials(MaterialEvent event) {
        //  Materials
        GTLiteMaterials.init();
        OrePrefixAddition.init();
        GTLiteMaterialPropertyAddition.init();
        GTLiteMaterialFlagAddition.init();
        MaterialHelperManager.init();
        //  Tools
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

    public static class TickingHandler {
        private static final BigInteger MAX_T = BigInteger.valueOf(2147483647L);
        private static final Map<UUID, BigInteger> lastEU = new HashMap<>();
        private static final Map<UUID, Integer> ticks = new HashMap<>();
        private static final int TICK_INTERVAL = 5 * GTLiteValues.MINUTE;
        private static final BigInteger BIG_INTEGER_TICK_INTERVAL = BigInteger.valueOf(TICK_INTERVAL);

        @SubscribeEvent
        public void onTicking(TickEvent.PlayerTickEvent event) {
            if (!GTLiteConfigHolder.misc.notifyWirelessEnergy) return;
            if (event.phase == TickEvent.Phase.END) {
                int tick = ticks.getOrDefault(event.player.getUniqueID(), 0) + 1;
                if(tick >= TICK_INTERVAL) {
                    tick = 0;
                    showWirelessEnergy(event.player);
                }
                ticks.put(event.player.getUniqueID(), tick);
            }
        }

        private void showWirelessEnergy(EntityPlayer player) {
            BigInteger eu = WirelessEnergyNetworkManager.getUserEU(player.getUniqueID());
            if(eu.compareTo(BigInteger.ZERO) <= 0) return;

            ITextComponent line1 = new TextComponentTranslation(
                    "gtlitecore.wirelessenergy.notify.part.1",
                    makeGrouping(eu.toString())
            );
            ITextComponent part2 = new TextComponentTranslation(
                    "gtlitecore.wirelessenergy.notify.part.2",
                    makeGrouping(eu.divide(MAX_T).toString())
            );
            if (eu.compareTo(MAX_T.multiply(BigInteger.TEN)) > 0) line1 = line1.appendSibling(part2);
            player.sendMessage(line1);

            if(lastEU.containsKey(player.getUniqueID())) {
                BigInteger delta = eu.subtract(lastEU.get(player.getUniqueID())).divide(BIG_INTEGER_TICK_INTERVAL);
                ITextComponent line2 = new TextComponentTranslation(
                        "gtlitecore.wirelessenergy.notify.part.3",
                        makeGrouping(delta.toString(), "+")
                );
                ITextComponent part4 = new TextComponentTranslation(
                        "gtlitecore.wirelessenergy.notify.part.4",
                        makeGrouping(delta.divide(MAX_T).toString(), "+")
                );
                if(delta.compareTo(MAX_T) > 0) line2 = line2.appendSibling(part4);
                player.sendMessage(line2);
            }
            lastEU.put(player.getUniqueID(), eu);
        }
    }

    private static String makeGrouping(String val, String posSig) {
        boolean isNeg = val.charAt(0) == '-';
        if (isNeg) val = val.substring(1);

        int length = val.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(val.charAt(i));
            if((length - i) % 3 == 1 && i != length - 1) sb.append(',');
        }
        return (isNeg ? "-" : posSig) + sb;
    }

    private static String makeGrouping(String val) {
        return makeGrouping(val, "");
    }
}
