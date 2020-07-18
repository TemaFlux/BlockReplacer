package me.temaflex.blockreplacer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmd
implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			switch (args[0]) {
				case "reload": {
					if (sender.hasPermission("blockreplacer.reload")) {
						Main.getI().reloadConfig();
						Main.getI().loadConfig();
						Utils.sendM(sender, Main.getI().getConfig().get("messages.reload"), cmd.getName());
						return true;
					}
					else {
						Utils.sendM(sender, Main.getI().getConfig().get("messages.noperm"), cmd.getName());
						return true;
					}
				}
			}
		}
		Utils.sendM(sender, Main.getI().getConfig().get("messages.help"), cmd.getName());
		return false;
	}
}
