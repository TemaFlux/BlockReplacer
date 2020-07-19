package me.temaflex.blockreplacer;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.temaflex.blockreplacer.api.XMaterial;
import me.temaflex.blockreplacer.commands.BlockReplacer;
import me.temaflex.blockreplacer.listeners.BlockPlaceListener;

public class Main
extends JavaPlugin {
    private static Main instance;
    FileConfiguration config;
    BlockReplacer BlockReplacer = new BlockReplacer();
    private static HashMap<XMaterial, XMaterial> ReplaceBlocks = new HashMap<XMaterial, XMaterial>();
    
    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        registerListeners(new BlockPlaceListener());
        getCommand("blockreplacer").setExecutor(BlockReplacer);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
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
        if (!ReplaceBlocks.isEmpty()) {
            ReplaceBlocks.clear();
        }
        for (String key : getConfig().getKeys(false)) {
            if (key.equals("messages")) continue;
            String value = getConfig().getString(key);
            XMaterial replacer = Utils.parseMaterial(key);
            XMaterial puted = Utils.parseMaterial(value);
            ReplaceBlocks.put(replacer, puted);
        }
    }
    
    public static HashMap<XMaterial, XMaterial> getReplaceBlocks() {
        return ReplaceBlocks;
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    public void registerListeners(Listener... listener) {
        for (Listener l : listener) {
            this.getServer().getPluginManager().registerEvents(l, this);
        }
    }
}
