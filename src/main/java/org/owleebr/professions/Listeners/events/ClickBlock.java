package org.owleebr.professions.Listeners.events;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.FoxBlock.API.CustomBlockClickEvent;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.Main;

@AListener
public class ClickBlock implements Listener {
    @EventHandler
    public void onClick(final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (block == null) return;
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        if (data.has(Keys.MainBlock)) {
            CustomBlockData data2 = new CustomBlockData(data.get(Keys.MainBlock, DataType.LOCATION).getBlock(), Main.getInstance());
            if (data2.has(Keys.Type) && data2.get(Keys.Type, PersistentDataType.STRING).equalsIgnoreCase("functional")) {
                Bukkit.getPluginManager().callEvent(new CustomBlockClickEvent(data.get(Keys.MainBlock, DataType.LOCATION).getBlock(), data2.get(Keys.Block, PersistentDataType.STRING), event.getPlayer(), event.getAction()));
            }
        }else {
            CustomBlockData data2 = new CustomBlockData(event.getClickedBlock(), Main.getInstance());
            if (data2.has(Keys.Type) && data2.get(Keys.Type, PersistentDataType.STRING).equalsIgnoreCase("functional")) {
                Bukkit.getPluginManager().callEvent(new CustomBlockClickEvent(event.getClickedBlock(), data2.get(Keys.Block, PersistentDataType.STRING), event.getPlayer(), event.getAction()));
            }
        }
    }
}
