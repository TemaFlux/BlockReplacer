package me.temaflex.blockreplacer;

import java.util.List;
import java.util.NoSuchElementException;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import me.temaflex.blockreplacer.api.IDLibrary;
import me.temaflex.blockreplacer.api.MinecraftVersion;
import me.temaflex.blockreplacer.api.MinecraftVersion.V;
import me.temaflex.blockreplacer.api.XMaterial;
import net.md_5.bungee.api.ChatColor;

public class Utils {
    private static IDLibrary idlibrary;
    
    @SuppressWarnings("deprecation")
	public static XMaterial parseXMaterial(ItemStack i) {
    	int mdurab = i.getType().getMaxDurability();
    	int data = i.getDurability();
    	if (mdurab > 0) data = 0;
    	return parseXMaterial(i.getType().name(), data);
    }
    
    @SuppressWarnings("deprecation")
    public static XMaterial parseXMaterial(Block b) {
        return parseXMaterial(b.getType().name(), b.getState().getRawData());
    }
    
    public static XMaterial parseXMaterial(String name) {
        String[] xx = name.split(":");
        String id = xx[0];
        byte data = 0;
        if (xx.length > 1) {
        	try {
        		data = Byte.valueOf(xx[1]);
        	}
        	catch (Exception e) {}
        }
    	return parseXMaterial(id, data);
    }
    
    public static XMaterial parseXMaterial(String id, int data) {
        XMaterial m = XMaterial.AIR;
        if (!MinecraftVersion.atLeast(V.v1_13)) {
            if (isInteger(id)) {
                try {
                    id = Material.values()[Integer.valueOf(id)].name();
                }
                catch (Exception e) {}
            }
            try {
                m = XMaterial.matchXMaterial(id+":"+data).get();
            }
            catch (Exception e) {
                if (e instanceof NoSuchElementException || e instanceof NumberFormatException) {
                    if (data != 0 ) {
                        m = XMaterial.matchXMaterial(id).get();
                    }
                }
            }
        }
        else {
            if (idlibrary == null) {
                idlibrary = new IDLibrary();
            }
            try {
            	m = XMaterial.matchXMaterial(idlibrary.getMaterial(id+":"+data));
            }
            catch (Exception e) {}
        }
        return m;
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
    
    public static void sendMessage(CommandSender sender, Object msg, String cmd) {
        if (msg instanceof List) {
            for (Object str : ((List<?>) msg)) {
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
