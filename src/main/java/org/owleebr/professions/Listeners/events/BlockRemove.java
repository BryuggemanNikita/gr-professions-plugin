package org.owleebr.professions.Listeners.events;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.FoxBlock.API.CustomBlockRemoveEvent;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.Main;

@AListener
public class BlockRemove implements Listener {
    @EventHandler
    public void onBlockRemove(BlockBreakEvent event) {
        Block block = event.getBlock();
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        if (data.has(Keys.MainBlock)){
            Block mainBlock = data.get(Keys.MainBlock, DataType.LOCATION).getBlock();
            CustomBlockData data1 = new CustomBlockData(mainBlock, Main.getInstance());

            ItemStack item = new ItemStack(Material.STRUCTURE_VOID);
            item.editMeta(m -> {
                String name = Main.blockList.get(data1.get(Keys.Block, PersistentDataType.STRING)).Name;
                m.setDisplayName(ChatColor.RESET + "" + name);
                m.getPersistentDataContainer().set(Keys.Block, PersistentDataType.STRING, data1.get(Keys.Block, PersistentDataType.STRING));
                m.setDisplayName(ChatColor.RESET + "" + name);
                CustomModelDataComponent component = m.getCustomModelDataComponent();
                List<String> stringList = new ArrayList<>();
                stringList.add(Main.blockList.get(data1.get(Keys.Block, PersistentDataType.STRING)).CMD);
                component.setStrings(stringList);
                m.setCustomModelDataComponent(component);
            });

            if (data1.get(Keys.Type, PersistentDataType.STRING).equalsIgnoreCase("functional")){
                Bukkit.getPluginManager().callEvent(new CustomBlockRemoveEvent(data.get(Keys.MainBlock, DataType.LOCATION).getBlock(), data1.get(Keys.Block, PersistentDataType.STRING), event.getPlayer(), item));
            }
            List<Location> locations = data1.get(Keys.Blocks, DataType.asList(DataType.LOCATION));
            for (Location location : locations) {
                Block block1 = location.getBlock();
                block1.setType(Material.AIR);
                CustomBlockData data2 = new CustomBlockData(block1, Main.getInstance());
                data2.clear();
            }
            Entity display = Bukkit.getEntity(data1.get(Keys.Display, DataType.UUID));
            display.remove();

            event.setDropItems(false);

            data1.clear();
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item);
            mainBlock.setType(Material.AIR);
        }
        else if (data.has(Keys.Block)){
            CustomBlockData data1 = new CustomBlockData(event.getBlock(), Main.getInstance());
            ItemStack item = new ItemStack(Material.STRUCTURE_VOID);
            item.editMeta(m -> {
                String name = Main.blockList.get(data.get(Keys.Block, PersistentDataType.STRING)).Name;
                m.setDisplayName(ChatColor.RESET + "" + name);
                m.getPersistentDataContainer().set(Keys.Block, PersistentDataType.STRING, data.get(Keys.Block, PersistentDataType.STRING));
                m.setDisplayName(ChatColor.RESET + "" + name);
                CustomModelDataComponent component = m.getCustomModelDataComponent();
                List<String> stringList = new ArrayList<>();
                stringList.add(Main.blockList.get(data.get(Keys.Block, PersistentDataType.STRING)).CMD);
                component.setStrings(stringList);
                m.setCustomModelDataComponent(component);
            });
            if (data1.get(Keys.Type, PersistentDataType.STRING).equalsIgnoreCase("functional")){
                Bukkit.getPluginManager().callEvent(new CustomBlockRemoveEvent(event.getBlock(), data1.get(Keys.Block, PersistentDataType.STRING), event.getPlayer(), item));
            }
            //if (data.get(Keys.Type, PersistentDataType.STRING).equalsIgnoreCase("Light")){
                //    Block block1 = block.getLocation().clone().add(0, 1, 0).getBlock();
                //    block1.setType(Material.AIR);
                //}

            Entity display = Bukkit.getEntity(data.get(Keys.Display, DataType.UUID));
            display.remove();

            event.setDropItems(false);
            data.clear();
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item);
        }
    }
}
