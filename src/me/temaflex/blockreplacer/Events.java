package me.temaflex.blockreplacer;

import java.util.NoSuchElementException;

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
		XMaterial placed_m;
		try {
			placed_m = XMaterial.matchXMaterial(b.getType().name()+":"+b.getState().getRawData()).get();
		}
		catch (NoSuchElementException ex) {
			placed_m = XMaterial.matchXMaterial(b.getType().name()+":0").get();
		}
	    if (Main.getReplaceBlocks().containsKey(placed_m)) {
	    	XMaterial x = Main.getReplaceBlocks().get(placed_m);
	    	b.setType(x.parseMaterial(true).get());
	    	BlockState state = b.getState();
	    	state.setRawData(x.getData());
	    	state.update(true);
	    }
	}
}
