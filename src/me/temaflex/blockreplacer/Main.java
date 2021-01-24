package me.temaflex.blockreplacer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.temaflex.blockreplacer.api.XMaterial;
import me.temaflex.blockreplacer.cmds.BlockReplacer;

public class Main
extends JavaPlugin {
    private static Main instance;
    private FileConfiguration config;
    private BlockReplacer BlockReplacer = new BlockReplacer();
    private List<Listener> listeners = new ArrayList<Listener>();
    private static HashMap<XMaterial, XMaterial> ReplaceBlocks = new HashMap<XMaterial, XMaterial>();
    // Settings
    // Disable
    public boolean d_listeners = false;
    public boolean d_command = false;
    // Enable
    public boolean e_blockplace = true;
    public boolean e_dropitem = false;
    public boolean e_dropblock = false;
    
    @Override
    public void onEnable() {
        instance = this;
        this.loadConfig();
        if (this.d_listeners) this.registerListeners(new Listeners());
        if (this.d_command) this.getCommand("blockreplacer").setExecutor(BlockReplacer);
    }
    
    @Override
    public void onDisable() {
    	this.listeners.forEach(l -> HandlerList.unregisterAll(l));
        HandlerList.unregisterAll(this);
        super.onDisable();
    }
    
    public void loadConfig() {
    	// Config
        if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir())
            throw new RuntimeException("Can't create data folder!");
        File file = new File(this.getDataFolder(), "config.yml");
        if (file.exists()) config = super.getConfig();
        else {
            this.saveDefaultConfig();
            this.config = this.getConfig();
        }
        // Settings
        // Disable
        this.d_listeners = this.getSetting("disable.listeners", false);
        this.d_command = this.getSetting("disable.command", false);
        // Enable
        this.e_blockplace = this.getSetting("enable.BlockPlace", false);
        this.e_dropitem = this.getSetting("enable.DropItem", false);
        this.e_dropblock = this.getSetting("enable.DropBlock", false);
        // Blocks
        if (!ReplaceBlocks.isEmpty()) ReplaceBlocks.clear();
        for (String key : getConfig().getKeys(false)) {
            if (key.equals("messages") || key.equals("settings")) continue;
            String value = getConfig().getString(key);
            try {
	            XMaterial replacer = Utils.parseXMaterial(key);
	            XMaterial puted = Utils.parseXMaterial(value);
	            if (replacer == null || puted == null) {
	            	this.getLogger().info("Item: \"" + key + "\" or \"" + value + "\" not parsed");
	            	continue;
	            }
	            ReplaceBlocks.put(replacer, puted);
            }
            catch (Exception e) {
            	this.getLogger().info("Item: \"" + key + "\" or \"" + value + "\" not parsed");
            }
        }
    }
    
    public static HashMap<XMaterial, XMaterial> getReplaceBlocks() {
        return ReplaceBlocks;
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    @Override
    public FileConfiguration getConfig() {
        return config == null ? super.getConfig() : config;
    }
    
    public boolean getSetting(String key, boolean def) {
    	return this.getConfig().getBoolean(key, def);
    }
    
    public void registerListeners(Listener... listener) {
        for (Listener l : listener) {
        	this.listeners.add(l);
            this.getServer().getPluginManager().registerEvents(l, this);
        }
    }
}
