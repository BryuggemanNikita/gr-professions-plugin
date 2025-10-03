package org.owleebr.professions.Listeners.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.owleebr.professions.Annotations.AListener;

@AListener
public class DontOpenShulker implements Listener {
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.RED_SHULKER_BOX) {
                event.setCancelled(true);
            }
        }
    }
}
