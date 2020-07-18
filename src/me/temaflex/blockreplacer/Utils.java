package me.temaflex.blockreplacer;

import java.util.List;
import java.util.NoSuchElementException;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Utils {
    public static XMaterial parseMaterial(String name) {
        XMaterial material = XMaterial.AIR;
        String[] xx = name.split(":");
        String id = xx[0];
        String durability = "0";
        if (xx.length > 1) {
            durability = xx[1];
        }
        if (isInteger(id)) {
            try {
                id = Material.values()[Integer.valueOf(id)].name();
            }
            catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {}
        }
        try {
            material = XMaterial.matchXMaterial(id+":"+durability).get();
        }
        catch (Exception e) {
            if (e instanceof NoSuchElementException || e instanceof NumberFormatException) {
                if (!durability.equals("0")) {
                    material = XMaterial.matchXMaterial(id+":0").get();
                }
            }
        }
        return material;
    }
    
    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    
    public static void sendM(CommandSender sender, Object msg, String cmd) {
        if (msg instanceof List) {
            for (Object str : ((List<?>)msg)) {
                String out = String.valueOf(str).replace("{cmd}", cmd);
                sender.sendMessage(parseColor(out));
            }
        }
        if (msg instanceof String) {
            String out = String.valueOf(msg).replace("{cmd}", cmd);
            sender.sendMessage(parseColor(out));
        }
    }
    
    public static String parseColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
