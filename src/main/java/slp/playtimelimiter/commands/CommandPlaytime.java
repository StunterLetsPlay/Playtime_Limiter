package slp.playtimelimiter.commands;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import slp.playtimelimiter.PlaytimeLimiter;
import slp.playtimelimiter.playtime.PlaytimeDataManager;

/**
 * @version 1.0
 * @since 26.10.2021
 */
public class CommandPlaytime extends CommandBase {

    @Override
    public String getName() {
        return "playtime";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        sender.sendMessage(
                new TextComponentString(
                        "/playtime // Shows ur remaining Playtime"
                                + "\n"
                                + "/playtime reload // Reloads the Config"
                                + "\n"
                                + "/playtime reset <PlayerName> // Resets Playtime and Timeout of a Player"
                )
        );
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        switch (args.length) {
            case 0:
                if (!(sender instanceof EntityPlayerMP)) {
                    sender.sendMessage(new TextComponentString("You don't look like a Player, get the heck out of here!"));
                    return;
                }

                sender.sendMessage(
                        new TextComponentString(
                                TextFormatting.GREEN + "You have "
                                        + TextFormatting.AQUA + TextFormatting.BOLD + PlaytimeDataManager.getRemainingTimeInMintues((EntityPlayerMP) sender)
                                        + TextFormatting.GREEN + " Minutes remaining!"
                        )
                );
                break;
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!checkPermission(sender))
                        return;

                    PlaytimeLimiter.getInstance().getConfigManager().init();
                    sender.sendMessage(
                            new TextComponentString(
                                    TextFormatting.GREEN + "Reloading Config ..."
                            )
                    );
                    break;
                }

                getUsage(sender);
                break;
            case 2:
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!checkPermission(sender))
                        return;

                    EntityPlayerMP targetPlayer;
                    targetPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args[1]);

                    if (targetPlayer == null) {
                        GameProfile target = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getGameProfileForUsername(args[1]);
                        if (target == null) {
                            sender.sendMessage(
                                    new TextComponentString(
                                            TextFormatting.RED + "No Player by the Name of "
                                                    + TextFormatting.YELLOW + "\"" + args[1] + "\""
                                                    + TextFormatting.RED + " has joined the Server before!"
                                                    + TextFormatting.GRAY + " (Maybe they Namechanged?)"
                                    )
                            );
                            return;
                        }

                        WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
                        targetPlayer = new EntityPlayerMP(FMLCommonHandler.instance().getMinecraftServerInstance(), world, target, new PlayerInteractionManager(world));
                    }

                    targetPlayer.getEntityData().removeTag("timeout");
                    targetPlayer.getEntityData().removeTag("timeLeft");
                    PlaytimeDataManager.resetTime(targetPlayer);
                    sender.sendMessage(
                            new TextComponentString(
                                    TextFormatting.GREEN + "You have successfully reset the Playtime and Timeout for "
                                            + TextFormatting.AQUA + "\"" + targetPlayer.getName() + "\""
                            )
                    );
                    break;
                }

                getUsage(sender);
                break;
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    private boolean checkPermission(ICommandSender sender) {
        if (!sender.canUseCommand(2, "")) {
            sender.sendMessage(new TextComponentString(
                    TextFormatting.RED + "I'm sorry, but you do not have Permissions to use this Command!"
                    )
            );
            return false;
        }

        return true;
    }
}
