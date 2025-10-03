package org.owleebr.professions.FoxBlock.API.Utils;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.Main;

public class BlockUtils {
    public static void changeModel(Location loc, String CMD){
        Block block = loc.getBlock();
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        ItemDisplay display = (ItemDisplay) Bukkit.getEntity(data.get(Keys.Display, DataType.UUID));
        ItemStack itm = display.getItemStack();
        itm.editMeta(m ->{
            CustomModelDataComponent cmd = m.getCustomModelDataComponent();
            List<String> stringList = new ArrayList<>();
            stringList.add(CMD);
            cmd.setStrings(stringList);
            m.setCustomModelDataComponent(cmd);
        });
        display.setItemStack(itm);
    }
}
