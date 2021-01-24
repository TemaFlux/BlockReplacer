package me.temaflex.blockreplacer.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.temaflex.blockreplacer.Main;
import me.temaflex.blockreplacer.Utils;

public class BlockReplacer
implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Main main = Main.getInstance();
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("blockreplacer.reload")) {
                    main.reloadConfig();
                    main.loadConfig();
                    Utils.sendMessage(sender, main.getConfig().get("messages.reload"), cmd.getName());
                    return true;
                }
                else {
                    Utils.sendMessage(sender, main.getConfig().get("messages.noperm"), cmd.getName());
                    return true;
                }
            }
        }
        Utils.sendMessage(sender, main.getConfig().get("messages.help"), cmd.getName());
        return false;
    }
}
