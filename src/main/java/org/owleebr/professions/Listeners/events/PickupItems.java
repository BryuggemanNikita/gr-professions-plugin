package org.owleebr.professions.Listeners.events;

import com.jeff_media.morepersistentdatatypes.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.Const.Keys;
import java.util.ArrayList;
import java.util.List;

@AListener
public class PickupItems implements Listener {
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player){
            ItemStack item = event.getItem().getItemStack();
            if (item.getPersistentDataContainer().has(Keys.Crucible)){
                if (item.getPersistentDataContainer().has(Keys.outputItem)){
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    if (hand.getPersistentDataContainer().has(Keys.forceps) && !hand.getPersistentDataContainer().has(Keys.input)){
                        hand.editMeta(m ->{
                            m.setDisplayName(ChatColor.RESET + "Щипцы с тиглем");
                            m.addEnchant(Enchantment.SMITE, 1, true);
                            m.getPersistentDataContainer().set(Keys.input, DataType.ITEM_STACK, item.clone());
                            Component c = Component.text(ChatColor.GRAY + "Содержит: ");
                            c = c.append(Component.translatable(item.getPersistentDataContainer().get(Keys.outputItem, DataType.ITEM_STACK).getType().getItemTranslationKey()));
                            List<Component> components = new ArrayList<>();
                            components.add(c);
                            m.lore(components);
                        });
                        event.getItem().setItemStack(new ItemStack(Material.AIR));
                    }else {
                        event.setCancelled(true);
                        player.damage(3);
                    }

                }
            }
        }
    }
}
