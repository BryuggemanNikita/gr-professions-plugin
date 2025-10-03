package org.owleebr.professions.FoxBlock.JsonObjects;



import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import org.owleebr.professions.FoxBlock.API.CustomBlockPlaceEvent;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.FoxBlock.Utils.BlockPlaceUtil;
import org.owleebr.professions.Main;

import static org.owleebr.professions.FoxBlock.Const.Pos.Rotations;

public class Block {
    public String Name;
    public String ID;
    public String Item;
    public String CMD;
    public String type;
    public String Size;

    public void setBlock(Location location, String Direction, Player player) {

        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5, 0.5, 0.5), EntityType.ITEM_DISPLAY);
        ItemStack item = new ItemStack(Material.valueOf(Item));
        item.editMeta(m -> {
            List<String> stringList = new ArrayList<>();
            stringList.add(CMD);
            CustomModelDataComponent component = m.getCustomModelDataComponent();
            component.setStrings(stringList);
           m.setCustomModelDataComponent(component);
        });
        display.setItemStack(item);
        Transformation trans = new Transformation(new Vector3f(0, 0, 0), Rotations.get(Direction).get(0), new Vector3f(1f, 1f, 1f), Rotations.get(Direction).get(1));
        display.setTransformation(trans);
        CustomBlockData data = new CustomBlockData(location.getBlock(), Main.getInstance());
        data.set(Keys.Block, PersistentDataType.STRING, ID);
        data.set(Keys.Type, PersistentDataType.STRING, type);
        data.set(Keys.Display, DataType.UUID, display.getUniqueId());
        if (Size.equals("0.5")){
            BlockPlaceUtil.PlaceHalfBlock(location);
        }else if (Size.equals("1")){
            BlockPlaceUtil.PlaceBlock(location);
        }else if (Size.equals("2")){
            if (type.equalsIgnoreCase("sit")){
                BlockPlaceUtil.Place2Block(location, Direction, type);
            }else {
                BlockPlaceUtil.Place2Block(location, Direction);
            }
        }else if (Size.equals("3")){
            BlockPlaceUtil.Place3Block(location, Direction);
        }else if (Size.equals("8")){
            BlockPlaceUtil.Place8Block(location, Direction);
        }else if (Size.equals("12")){
            BlockPlaceUtil.Place12Block(location, Direction);
        }else if (Size.equals("2up")){
            BlockPlaceUtil.Place2BlockUp(location);
        }
        if (type.equalsIgnoreCase("functional")){
            Bukkit.getPluginManager().callEvent(new CustomBlockPlaceEvent(location.getBlock(), ID, player));
        }
    }

    public String getName() {
        return Name;
    }
}
