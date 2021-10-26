package slp.playtimelimiter.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.text.TextComponentString;
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
        EntityPlayerMP player = null;
        switch (args.length) {
            case 0:
                if (!(sender instanceof EntityPlayerMP)) {
                    sender.sendMessage(new TextComponentString("You don't look like a Player, get the heck out of here!"));
                    return;
                }

                sender.sendMessage(
                        new TextComponentString(
                                ChatFormatting.GREEN + "You have "
                                        + ChatFormatting.AQUA + ChatFormatting.BOLD + PlaytimeDataManager.getRemainingTimeInMintues((EntityPlayerMP) sender)
                                        + ChatFormatting.GREEN + " Minutes remaining!"
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
                                    ChatFormatting.GREEN + "Reloading Config ..."
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
                                            ChatFormatting.RED + "No Player by the Name of "
                                                    + ChatFormatting.YELLOW + "\"" + args[1] + "\""
                                                    + ChatFormatting.RED + " has joined the Server before!"
                                                    + ChatFormatting.GRAY + " (Maybe they Namechanged?)"
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
                                    ChatFormatting.GREEN + "You have successfully reset the Playtime and Timeout for "
                                            + ChatFormatting.AQUA + "\"" + targetPlayer.getName() + "\""
                            )
                    );
                }

                getUsage(sender);
                break;
        }
    }

    private boolean checkPermission(ICommandSender sender) {
        if (!sender.canUseCommand(2, "")) {
            sender.sendMessage(new TextComponentString(
                            ChatFormatting.RED + "I'm sorry, but you cannot use this Command!"
                    )
            );
            return false;
        }

        return true;
    }
}
