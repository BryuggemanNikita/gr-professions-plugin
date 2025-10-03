package org.owleebr.professions.FoxBlock.API.Utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.FoxBlock.JsonObjects.Block;
import org.owleebr.professions.Main;

public class getItemBlock {
    public static ItemStack getCustomItemBlock(String blockName){
        org.owleebr.professions.FoxBlock.JsonObjects.Block bloc = Main.blockList.get(blockName);
        ItemStack item = new ItemStack(Material.STRUCTURE_VOID);
        item.editMeta(m -> {
            String name = bloc.Name;
            m.setDisplayName(ChatColor.RESET + "" + name);
            CustomModelDataComponent component = m.getCustomModelDataComponent();
            List<String> stringList = new ArrayList<>();
            stringList.add(bloc.CMD);
            component.setStrings(stringList);
            m.setCustomModelDataComponent(component);
            m.getPersistentDataContainer().set(Keys.Block, PersistentDataType.STRING, bloc.ID);
        });
        return item;
    }

    public static ItemStack getCustomItemBlockMarker(String blockName){
        Block bloc = Main.blockList.get(blockName);
        ItemStack item = new ItemStack(Material.STRUCTURE_VOID);
        item.editMeta(m -> {
            String name = bloc.Name;
            m.setDisplayName(ChatColor.RESET + "" + name);
            CustomModelDataComponent component = m.getCustomModelDataComponent();
            List<String> stringList = new ArrayList<>();
            stringList.add(bloc.CMD);
            component.setStrings(stringList);
            m.setCustomModelDataComponent(component);
        });
        return item;
    }

    public static List<String> getBlocksNames(){
        List<String> list = new ArrayList<>();
        for (String name : Main.blockList.keySet()) {
            list.add(name);
        }
        return list;
    }
}
