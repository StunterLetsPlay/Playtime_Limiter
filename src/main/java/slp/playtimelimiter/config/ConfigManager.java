package slp.playtimelimiter.config;

import net.minecraftforge.common.config.Configuration;
import slp.playtimelimiter.PlaytimeLimiter;

import java.io.File;

/**
 * @version 1.0
 * @since 26.10.2021
 */
public class ConfigManager {

    /*
        Configuration Setup
     */
    private final String filePath = "config/" + PlaytimeLimiter.modid + ".cfg";
    private Configuration config;

    /*
        Configs
     */
    private final String CATEGORY__PLAYTIME = "Config.Playtime";
    private final String NAME__PLAYTIME_LENGTH = "Playtime Length";
    private final String NAME__PLAYTIME_TIMEOUT = "Playtime Timeout";
    private final String NAME__PLAYTIME_WARN_KICK = "Warn low Playtime Kick";
    private final String NAME__PLAYTIME_RESET_RECONNECT = "Reset Playtime on Reconnect";
    private final String NAME__PLAYTIME_RESET_MIDNIGHT = "Reset Playtime at Midnight";

    public int playtimeLength;
    public int playtimeTimeout;
    public boolean warnPlaytimeKick;
    public boolean resetPlaytimeReconnect;
    public boolean resetPlaytimeMidnight;

    public void init() {
        config = new Configuration(new File(filePath));

        config.addCustomCategoryComment(CATEGORY__PLAYTIME, "General Settings for Playtime");
        playtimeLength = config.getInt(NAME__PLAYTIME_LENGTH, CATEGORY__PLAYTIME,
                60 * 60 * 3, 1, 60 * 60 * 24 * 7,
                "The Length (IN SECONDS) which a Player can play on your Server before getting Kicked with a Timeout"
        + "\n\n"
        + "Default is 3 Hours");
        playtimeTimeout = config.getInt(NAME__PLAYTIME_TIMEOUT, CATEGORY__PLAYTIME,
                60 * 60 * 12, 1, 60 * 60 * 24 * 7,
                "The Length (IN SECONDS) which a Player has to wait, after being kicked from the server"
                        + "\n"
                        + "to be able to join again."
                        + "\n\n"
                        + "Default is 12 Hours");
        warnPlaytimeKick = config.getBoolean(NAME__PLAYTIME_WARN_KICK, CATEGORY__PLAYTIME,
                true,
                "If Players should get warned before they get kicked"
                + "\n"
                + "They would get warned: {30, 15, 10, 5, 3, 1} Minute(s) before being Kicked!");
        resetPlaytimeReconnect = config.getBoolean(NAME__PLAYTIME_RESET_RECONNECT, CATEGORY__PLAYTIME,
                false,
                "If the Playtime should be reset after the Player Reconnects");
        resetPlaytimeMidnight = config.getBoolean(NAME__PLAYTIME_RESET_MIDNIGHT, CATEGORY__PLAYTIME,
                true,
                "If the Playtime should be reset after once the Server detects"
                        + "\n"
                        + "that a new (IRL) Day has started since the last time the Player has joined");

        config.save();
    }

}
