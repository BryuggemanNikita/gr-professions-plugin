package org.owleebr.professions.JsonObjects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.Const.Keys;

public class JSUtils {
    public static ItemStack getIron_Sword_Blank(boolean hot) {
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        item.editMeta(m ->{
            m.setDisplayName(ChatColor.RESET + "Заготовка железного меча");
            if (hot){
                m.getPersistentDataContainer().set(Keys.hot, PersistentDataType.BOOLEAN, true);
                m.addEnchant(Enchantment.SMITE, 1, true);
                m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else {
                m.getPersistentDataContainer().set(Keys.hot, PersistentDataType.BOOLEAN, false);
            }
        });
        return item;
    }

    public static ItemStack getIron_Pomel_Sword(boolean hot) {
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        item.editMeta(m ->{
            m.setDisplayName(ChatColor.RESET + "Оголовье железного меча");
            if (hot){
                m.getPersistentDataContainer().set(Keys.hot, PersistentDataType.BOOLEAN, true);
                m.addEnchant(Enchantment.SMITE, 1, true);
                m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else {
                m.getPersistentDataContainer().set(Keys.hot, PersistentDataType.BOOLEAN, false);
            }
        });
        return item;
    }

}
