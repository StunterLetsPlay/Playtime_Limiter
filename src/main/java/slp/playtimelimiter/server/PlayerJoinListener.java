package slp.playtimelimiter.server;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;
import slp.playtimelimiter.PlaytimeLimiter;
import slp.playtimelimiter.playtime.PlaytimeDataManager;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @since 26.10.2021
 */
public class PlayerJoinListener {

    @SubscribeEvent
    public void onMPPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!(e.player instanceof EntityPlayerMP))
            return;

        PlaytimeLimiter.logger.log(Level.INFO, "Loading Data for \"" + e.player.getName() + "\"");
        EntityPlayerMP player = (EntityPlayerMP) e.player;
        PlaytimeDataManager.loadPlayer(player);
    }

    @SubscribeEvent
    public void onMPPlayerLeave(PlayerEvent.PlayerLoggedOutEvent e) {
        if (!(e.player instanceof EntityPlayerMP))
            return;

        PlaytimeLimiter.logger.log(Level.INFO, "Saving Data for \"" + e.player.getName() + "\"");
        EntityPlayerMP player = (EntityPlayerMP) e.player;
        PlaytimeDataManager.savePlayer(player);
    }

    private int tick;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (!e.side.isServer() || e.phase != TickEvent.Phase.START || e.type != TickEvent.Type.SERVER)
            return;
        tick++;

        if (tick % 20 == 0) {
            PlaytimeDataManager.getTrackedPlayers().forEach(playerMP -> {
                if (playerMP.hasDisconnected()) {
                    PlaytimeDataManager.savePlayer(playerMP);
                    PlaytimeDataManager.stopTracking(playerMP);
                    return;
                }

                NBTTagCompound compound = playerMP.getEntityData();

                /*
                    Playtime Logic
                 */
                int newTime = PlaytimeDataManager.getRemainingTime(playerMP) - 1;
                PlaytimeDataManager.setRemainingTime(playerMP, newTime);

                if (!compound.hasKey("timeout")) {
                    if (newTime <= 0) {
                        int timeout = PlaytimeLimiter.getInstance().getConfigManager().playtimeTimeout;
                        kickPlayer(playerMP, Integer.toString(timeout));

                        if (!compound.hasKey("timeout"))
                            compound.setLong("timeout", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout));
                    }

                    return;
                }

                /*
                    Timeout Logic
                 */

                long timeLeft = compound.getLong("timeout");
                if (timeLeft >= System.currentTimeMillis()) {
                    // Add Delay to rightfully show the Kick Message each time
                    if (compound.hasKey("kick") && compound.getBoolean("kick")) {
                        kickPlayer(playerMP, new DecimalFormat("0.00").format(TimeUnit.MILLISECONDS.toSeconds(timeLeft - System.currentTimeMillis()) / 60.0));
                        compound.removeTag("kick");
                    } else {
                        compound.setBoolean("kick", true);
                    }
                    return;
                }

                compound.removeTag("timeout");
                compound.removeTag("playtime");
                PlaytimeDataManager.resetTime(playerMP);
            });
        }
    }

    private void kickPlayer(EntityPlayerMP playerMP, String duration){
        if (playerMP == null)
            return;

        playerMP.connection.disconnect(new TextComponentString(
                ChatFormatting.RED + "Your Playtime is over!"
                        + "\n\n"
                        + ChatFormatting.YELLOW + "Sadly you have used up all of your Playtime" +
                        "\n" +
                        ChatFormatting.YELLOW + "and thus have been kicked!"
                        + "\n\n"
                        + ChatFormatting.WHITE + "You will be able to join again in:"
                        + "\n"
                        + ChatFormatting.AQUA + ChatFormatting.BOLD
                        + duration + ChatFormatting.RESET + " Minute(s)!")
        );
    }

}
