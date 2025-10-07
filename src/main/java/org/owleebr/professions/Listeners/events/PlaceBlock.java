package org.owleebr.professions.Listeners.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.FoxBlock.JsonObjects.Block;
import org.owleebr.professions.FoxBlock.Utils.Utils;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.Main;

@AListener
public class PlaceBlock implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getPersistentDataContainer().has(Keys.Block)){
            String block = item.getPersistentDataContainer().get(Keys.Block, PersistentDataType.STRING);
            Block block1 = Main.blockList.get(block);
            String size = block1.SizeBlock;
            if (size.equals("2")){
                if (!Utils.checkSpace2Block(event.getBlock(), GameUtils.getFacingDirection(event.getPlayer()))){event.setCancelled(true); return;};
            }else if (size.equals("3")){
                if (!Utils.checkSpace4Block(event.getBlock(), GameUtils.getFacingDirection(event.getPlayer()))){event.setCancelled(true); return;};
            }else if (size.equals("8")){
                if (!Utils.checkSpace8Block(event.getBlock(), GameUtils.getFacingDirection(event.getPlayer()))){event.setCancelled(true); return;};
            }else if (size.equals("12")){
                if (!Utils.checkSpace12Block(event.getBlock(), GameUtils.getFacingDirection(event.getPlayer()))){event.setCancelled(true); return;};
            }else if (size.equals("13")){
                if (!Utils.checkSpace13Block(event.getBlock(), GameUtils.getFacingDirection(event.getPlayer()))){event.setCancelled(true); return;};
            }else if (size.equals("2up")){
                if (!event.getBlock().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)){event.setCancelled(true); return;};
            }
            block1.setBlock(event.getBlock().getLocation(), GameUtils.getFacingDirection(event.getPlayer()), event.getPlayer());
        }
    }
}
