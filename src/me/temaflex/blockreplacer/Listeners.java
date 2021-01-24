package me.temaflex.blockreplacer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.temaflex.blockreplacer.api.XMaterial;

public class Listeners
implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
    	if (!Main.getInstance().e_blockplace) return;
        Block b = e.getBlockPlaced();
        XMaterial xm = Utils.parseXMaterial(b);
        if (Main.getReplaceBlocks().containsKey(xm) && xm != XMaterial.AIR) {
            XMaterial x = Main.getReplaceBlocks().get(xm);
            b.setType(x.parseMaterial());
            BlockState state = b.getState();
            state.setRawData(x.getData());
            state.update(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent e) {
    	if (!Main.getInstance().e_dropitem) return;
    	this.onDrop(e.getItemDrop().getItemStack());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDropBlock(BlockDropItemEvent e) {
    	if (!Main.getInstance().e_dropblock) return;
    	e.getItems().forEach(i -> this.onDrop(i.getItemStack()));
    }
    
    @SuppressWarnings("deprecation")
	public void onDrop(ItemStack item) {
    	try {
    		XMaterial xm = Utils.parseXMaterial(item);
            if (Main.getReplaceBlocks().containsKey(xm) && xm != XMaterial.AIR) {
                Material m = Main.getReplaceBlocks().get(xm).parseMaterial();
                item.setType(m);
                int mdurab = m.getMaxDurability();
                int data = item.getDurability();
                if (mdurab > 0 && data >= mdurab) item.setDurability((short) mdurab);
            }
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
