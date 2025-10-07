package org.owleebr.professions.Listeners.events;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.Const.Keys;
import java.util.ArrayList;

@AListener
public class ForceappsCool implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().isRightClick()) {
            ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();
            if (hand.getPersistentDataContainer().has(Keys.input)){
                RayTraceResult result = player.rayTraceBlocks(100, FluidCollisionMode.ALWAYS);
                if (result.getHitBlock() != null && result.getHitBlock().getType() == Material.WATER) {
                    ItemStack item = hand.getPersistentDataContainer().get(Keys.input, DataType.ITEM_STACK);
                    if (item.getPersistentDataContainer().has(Keys.hot)){
                        Boolean hot = item.getPersistentDataContainer().get(Keys.hot, DataType.BOOLEAN);
                        hand.editMeta(m ->{
                            m.lore(new ArrayList<>());
                            m.removeEnchantments();
                            m.setDisplayName(ChatColor.RESET + "Щипцы");
                            m.getPersistentDataContainer().remove(Keys.input);
                        });
                        player.setItemInHand(hand);
                        if (hot) {
                            Location loc = result.getHitPosition().toLocation(player.getWorld());
                            loc.getWorld().spawnParticle(Particle.POOF, loc, 10, 0.125, 0.125, 0.125, 0.02);
                            player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                            item.editMeta(m ->{
                                m.getPersistentDataContainer().set(Keys.hot, DataType.BOOLEAN, false);
                                m.removeEnchantments();
                                m.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                            });
                        }
                        player.getInventory().addItem(item);
                    }
                }
            }
        }
    }

}
