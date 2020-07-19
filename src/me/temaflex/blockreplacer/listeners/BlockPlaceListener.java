package me.temaflex.blockreplacer.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.temaflex.blockreplacer.Main;
import me.temaflex.blockreplacer.Utils;
import me.temaflex.blockreplacer.api.XMaterial;

public class BlockPlaceListener
implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();
        XMaterial placed_m = Utils.BlocktoXMaterial(b);
        if (Main.getReplaceBlocks().containsKey(placed_m) && placed_m != XMaterial.AIR) {
            XMaterial x = Main.getReplaceBlocks().get(placed_m);
            b.setType(x.parseMaterial(true).get());
            BlockState state = b.getState();
            state.setRawData(x.getData());
            state.update(true);
        }
        return;
    }
}
