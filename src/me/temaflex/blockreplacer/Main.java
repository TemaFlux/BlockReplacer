package me.temaflex.blockreplacer;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    private static Main instance;
    FileConfiguration config;
    Cmd Cmd = new Cmd();
    Events Events = new Events();
    private static HashMap<XMaterial, XMaterial> replaceblocks = new HashMap<XMaterial, XMaterial>();
    
    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        getServer().getPluginManager().registerEvents(Events, this);
        getCommand("blockreplacer").setExecutor(Cmd);
    }
    
    @Override
    public void onDisable() {
        if (Events != null) {
            HandlerList.unregisterAll(Events);
        }
        HandlerList.unregisterAll(this);
    }
    
    public void loadConfig() {
        if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
            throw new RuntimeException("Can't create data folder!");
        }
        File file = new File(this.getDataFolder(), "config.yml");
        if (file.exists()) {
            config = this.getConfig();
        } else {
            this.saveDefaultConfig();
            config = this.getConfig();
        }
        if (!replaceblocks.isEmpty()) {
            replaceblocks.clear();
        }
        for (String key : getConfig().getKeys(false)) {
        	if (key.equals("messages")) continue;
            String value = getConfig().getString(key);
            XMaterial replacer = Utils.parseMaterial(key);
            XMaterial puted = Utils.parseMaterial(value);
            replaceblocks.put(replacer, puted);
        }
    }
    
    public static HashMap<XMaterial, XMaterial> getReplaceBlocks() {
        return replaceblocks;
    }
    
    public static Main getI() {
        return instance;
    }
}
