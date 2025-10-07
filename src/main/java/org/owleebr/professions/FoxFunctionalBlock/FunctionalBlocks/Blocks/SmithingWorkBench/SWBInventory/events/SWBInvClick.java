package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.SWBInventory.events;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.SWBInventory.Const.SWBInvKeys;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.SmitingWorkBench;
import org.owleebr.professions.Main;

@AListener
public class SWBInvClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Кузнечный стол")){
            event.setCancelled(true);
        }
        ItemStack item = event.getCurrentItem();
        if (item == null) {return;}

        if (event.getClick().isLeftClick() || event.getClick().isRightClick()){
            if (item.getPersistentDataContainer().has(SWBInvKeys.SWBItem)){
                ItemStack itm = item.getPersistentDataContainer().get(SWBInvKeys.SWBItem, DataType.ITEM_STACK);
                Location loc = item.getPersistentDataContainer().get(SWBInvKeys.SWBBlock, DataType.LOCATION);
                SmitingWorkBench bench = (SmitingWorkBench) Main.blockManager.blocks.get(loc);
                bench.CraftItem(itm);
                event.getWhoClicked().closeInventory();
            }
        }
    }
}
