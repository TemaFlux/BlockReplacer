package me.temaflex.blockreplacer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.temaflex.blockreplacer.Main;
import me.temaflex.blockreplacer.Utils;

public class BlockReplacer
implements CommandExecutor {
    Main main = Main.getInstance();
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
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
