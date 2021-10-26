package slp.playtimelimiter.playtime;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import slp.playtimelimiter.PlaytimeLimiter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @version 1.0
 * @since 26.10.2021
 */
public class PlaytimeDataManager {

    private static HashMap<EntityPlayerMP, Integer> data = new HashMap<>();

    public static void loadPlayer(EntityPlayerMP player) {
        NBTTagCompound nbt = player.getEntityData();

        int timeLeft;
        if (!PlaytimeLimiter.getInstance().getConfigManager().resetPlaytimeReconnect) {
            // If the Player joined on the same Day he last left or that Feature is disabled, just load Playtime normally
            if ((nbt.hasKey("dayLeft") && nbt.getInteger("dayLeft") == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    || !PlaytimeLimiter.getInstance().getConfigManager().resetPlaytimeMidnight) {
                timeLeft = nbt.hasKey("timeLeft") ? nbt.getInteger("timeLeft") : PlaytimeLimiter.getInstance().getConfigManager().playtimeLength;
            } else {
                timeLeft = PlaytimeLimiter.getInstance().getConfigManager().playtimeLength;
            }
        } else { // If the "Refresh on Reconnect" is enabled
            timeLeft = PlaytimeLimiter.getInstance().getConfigManager().playtimeLength;
        }

        setRemainingTime(player, timeLeft);
    }

    public static void savePlayer(EntityPlayerMP player) {
        NBTTagCompound nbt = player.getEntityData();
        nbt.setInteger("timeLeft", getRemainingTime(player));
        nbt.setInteger("dayLeft", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static void stopTracking(EntityPlayerMP player) {
        data.remove(player);
    }

    public static void setRemainingTime(EntityPlayerMP player, int timeLeft) {
        data.put(player, timeLeft);

        if (!PlaytimeLimiter.getInstance().getConfigManager().warnPlaytimeKick)
            return;

        switch (timeLeft) {
            case 60:
            case 60*3:
            case 60*5:
            case 60*10:
            case 60*15:
            case 60*30:
                player.sendMessage(
                        new TextComponentString(
                                ChatFormatting.YELLOW + "You have "
                                + ChatFormatting.GOLD + ChatFormatting.BOLD + (timeLeft / 60) +
                                        ChatFormatting.YELLOW + " Minutes of Playtime left!"
                        )
                );
                break;
        }
    }

    public static int getRemainingTime(EntityPlayerMP player) {
        return data.getOrDefault(player, PlaytimeLimiter.getInstance().getConfigManager().playtimeLength);
    }

    public static String getRemainingTimeInMintues(EntityPlayerMP player) {
        return new DecimalFormat("0.0#").format(getRemainingTime(player) / 60.0);
    }

    public static void resetTime(EntityPlayerMP playerMP) {
        stopTracking(playerMP);
        setRemainingTime(playerMP, PlaytimeDataManager.getRemainingTime(playerMP));
    }

    public static ArrayList<EntityPlayerMP> getTrackedPlayers() {
        return new ArrayList<>(data.keySet());
    }

}
