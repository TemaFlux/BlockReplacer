package me.temaflex.blockreplacer;

import java.util.NoSuchElementException;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Events
implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();
        if (b.getType().equals(Material.AIR)) {
        	return;
        }
        XMaterial placed_m = parser(b);
        if (Main.getReplaceBlocks().containsKey(placed_m)) {
            XMaterial x = Main.getReplaceBlocks().get(placed_m);
            b.setType(x.parseMaterial(true).get());
            BlockState state = b.getState();
            state.setRawData(x.getData());
            state.update(true);
        }
    }
    
    @SuppressWarnings("deprecation")
	public XMaterial parser(Block b) {
        XMaterial placed_m = null;
        try {
            placed_m = XMaterial.matchXMaterial(b.getType().name()+":"+b.getState().getRawData()).get();
        }
        catch (NoSuchElementException ex) {
            placed_m = XMaterial.matchXMaterial(b.getType().name()+":0").get();
        }
        return placed_m;
    }
}
