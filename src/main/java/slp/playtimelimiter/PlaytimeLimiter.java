package slp.playtimelimiter;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slp.playtimelimiter.commands.CommandPlaytime;
import slp.playtimelimiter.config.ConfigManager;
import slp.playtimelimiter.server.PlayerJoinListener;

@Mod(modid = "playtimelimiter", useMetadata = true, serverSideOnly = true, acceptableRemoteVersions = "*")
public class PlaytimeLimiter {

    public static String modid = "playtimelimiter";

    @Mod.Instance
    private static PlaytimeLimiter instance;
    public static Logger logger = LogManager.getLogger();
    private ConfigManager configManager;

    public static PlaytimeLimiter getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    private static PlayerJoinListener playerJoinListener;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){
        playerJoinListener = new PlayerJoinListener();

        // Config
        configManager = new ConfigManager();
        logger.log(Level.INFO, "Loading Config File ...");
        configManager.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(playerJoinListener);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){}

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandPlaytime());
    }
}
