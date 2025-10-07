package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.SWBInventory;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.SWBInventory.Const.SWBInvKeys;
import org.owleebr.professions.JsonObjects.RecipeSW;
import org.owleebr.professions.Main;
import java.util.List;
import java.util.stream.Collectors;

public class CreateInv {

    public static Inventory getSWBInv(List<ItemStack> invs, Location block){
        Inventory inv = Bukkit.createInventory(null, 9*6, "Кузнечный стол");
        List<RecipeSW> recipes = Main.SWBRecipeList.values().stream().filter(r -> r.getItems().equals(invs)).collect(
            Collectors.toList());
        int slot = 0;
        for (RecipeSW recipe : recipes) {
            Bukkit.getLogger().info("TST");
            ItemStack item = recipe.getOuItem().clone();
            item.editMeta(m ->{
               for (NamespacedKey k : m.getPersistentDataContainer().getKeys()){
                   m.getPersistentDataContainer().remove(k);
               }
               m.getPersistentDataContainer().set(SWBInvKeys.SWBItem, DataType.ITEM_STACK, recipe.getOuItem());
               m.getPersistentDataContainer().set(SWBInvKeys.SWBBlock, DataType.LOCATION, block);
            });
            inv.setItem(slot, item);
            slot++;
        }
        return inv;
    }

}
