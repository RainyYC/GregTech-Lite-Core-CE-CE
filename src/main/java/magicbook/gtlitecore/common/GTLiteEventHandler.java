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
import magicbook.gtlitecore.common.metatileentities.multi.part.MetaTileEntityWirelessEnergyHatch;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
        private static final BigInteger MAX_10_HOUR = MAX_T.multiply(BigInteger.valueOf(10 * 60 * 60 * 20));

        private static final Map<UUID, BigInteger> lastEU = new HashMap<>();
        private static final Map<UUID, Integer> ticks = new HashMap<>();
        private static final int TICK_INTERVAL = 5 * GTLiteValues.MINUTE;
        private static final BigInteger BIG_INTEGER_TICK_INTERVAL = BigInteger.valueOf(TICK_INTERVAL);

        @SubscribeEvent
        public void onTicking(TickEvent.PlayerTickEvent event) {
            if (!GTLiteConfigHolder.misc.notifyWirelessEnergy || event.player.world.isRemote) return;
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
            BigInteger eu = WirelessEnergyNetworkManager.getUserEU(MetaTileEntityWirelessEnergyHatch.UNIVERSAL_UUID);
            if(eu.compareTo(BigInteger.ZERO) <= 0) return;

            player.sendMessage(formatEnergyContained(eu));

            if (lastEU.containsKey(player.getUniqueID())) {
                BigInteger delta = eu.subtract(lastEU.get(player.getUniqueID())).divide(BIG_INTEGER_TICK_INTERVAL);
                player.sendMessage(formatEnergyDelta(delta));

                if (delta.compareTo(BigInteger.ZERO) < 0) {
                    BigInteger remainTicks = eu.divide(delta.negate());
                    if (remainTicks.compareTo(BigInteger.valueOf(1000 * 60 * 60 * 20).multiply(MAX_T)) < 0) {
                        player.sendMessage(new TextComponentTranslation(
                                "gtlitecore.wirelessenergy.notify.part.5",
                                String.format("%.2f", remainTicks.doubleValue() / 1200)
                        ));
                    }
                }
            }
            lastEU.put(player.getUniqueID(), eu);
        }

        private static ITextComponent formatEnergyContained(BigInteger eu) {
            if (eu.compareTo(MAX_10_HOUR) >= 0) {
                String v = eu.multiply(BigInteger.valueOf(100)).divide(MAX_10_HOUR).toString();
                return new TextComponentTranslation(
                        "gtlitecore.wirelessenergy.notify.part.2",
                        makeGrouping(v.substring(0, v.length() - 2)) + "." + v.substring(v.length() - 2)
                );
            } else {
                String v = eu.toString();
                return new TextComponentTranslation(
                        "gtlitecore.wirelessenergy.notify.part.1",
                        makeGrouping(v)
                );
            }
        }

        private static ITextComponent formatEnergyDelta(BigInteger eu) {
            BigInteger absEu = eu.compareTo(BigInteger.ZERO) >= 0 ? eu : eu.negate();
            if (absEu.compareTo(MAX_T) >= 0) {
                String v = eu.multiply(BigInteger.valueOf(100)).divide(MAX_T).toString();
                return new TextComponentTranslation(
                        "gtlitecore.wirelessenergy.notify.part.4",
                        makeGrouping(v.substring(0, v.length() - 2), "+") + "." + v.substring(v.length() - 2)
                );
            } else {
                String v = eu.toString();
                return new TextComponentTranslation(
                        "gtlitecore.wirelessenergy.notify.part.3",
                        makeGrouping(v, "+")
                );
            }
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
