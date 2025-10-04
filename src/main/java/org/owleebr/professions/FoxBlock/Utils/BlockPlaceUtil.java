package org.owleebr.professions.FoxBlock.Utils;

import static org.owleebr.professions.FoxBlock.Utils.Utils.DIR;
import static org.owleebr.professions.FoxBlock.Utils.Utils.DIR2;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.Main;

public class BlockPlaceUtil {
    public static void PlaceHalfBlock(Location location) {
        location.getBlock().setType(Material.SOUL_CAMPFIRE);
        Campfire campfire = (Campfire) location.getBlock().getBlockData();
        campfire.setLit(false);
        location.getBlock().setBlockData(campfire);
        location.getBlock().getState().update(true);
    }
    public static void PlaceBlock(Location location) {
        location.getBlock().setType(Material.RED_SHULKER_BOX);
        CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
        //data1.set(Keys.MainBlock, DataType.LOCATION, location);
    }

    public static void Place2Block(Location location, String Direction) {
        if (Utils.checkSpace2Block(location.getBlock(), Direction)){
            location.getBlock().setType(Material.RED_SHULKER_BOX);
            Block block = location.clone().add(Utils.DIR2.get(Direction)).getBlock();
            block.setType(Material.RED_SHULKER_BOX);
            CustomBlockData data = new CustomBlockData(block, Main.getInstance());
            data.set(Keys.MainBlock, DataType.LOCATION, location);
            List<Location> locations = new ArrayList<>();
            locations.add(block.getLocation());
            CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
            data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
            data1.set(Keys.MainBlock, DataType.LOCATION, location);
        }
    }
    public static void Place2Block(Location location, String Direction, String Key) {
        if (Utils.checkSpace2Block(location.getBlock(), Direction)){
            PlaceHalfBlock(location);
            Block block = location.clone().add(Utils.DIR2.get(Direction)).getBlock();
            PlaceHalfBlock(block.getLocation());
            CustomBlockData data = new CustomBlockData(block, Main.getInstance());
            data.set(Keys.MainBlock, DataType.LOCATION, location);
            data.set(Keys.Type, PersistentDataType.STRING, Key);
            List<Location> locations = new ArrayList<>();
            locations.add(block.getLocation());
            CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
            data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
            data1.set(Keys.MainBlock, DataType.LOCATION, location);
        }
    }

    public static void Place3Block(Location location, String Direction) {
        if (Utils.checkSpace4Block(location.getBlock(), Direction)){
            location.clone().getBlock().setType(Material.RED_SHULKER_BOX);
            Block block = location.clone().add(Utils.DIR2.get(Direction)).getBlock();
            block.setType(Material.RED_SHULKER_BOX);
            CustomBlockData data = new CustomBlockData(block, Main.getInstance());
            data.set(Keys.MainBlock, DataType.LOCATION, location);
            List<Location> locations = new ArrayList<>();
            locations.add(block.getLocation());

            Location uppedBlock = location.clone().add(Utils.DIR2.get(Direction)).add(0, 1, 0);
            PlaceHalfBlock(uppedBlock);
            CustomBlockData data2 = new CustomBlockData(uppedBlock.getBlock(), Main.getInstance());
            data2.set(Keys.MainBlock, DataType.LOCATION, location);
            locations.add(uppedBlock);

            CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
            data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
            data1.set(Keys.MainBlock, DataType.LOCATION, location);
        }
    }
    public static void Place3Block(Location location, String Direction, String Key) {
        if (Utils.checkSpace4Block(location.getBlock(), Direction)){
            location.clone().getBlock().setType(Material.RED_SHULKER_BOX);
            Block block = location.clone().add(Utils.DIR2.get(Direction)).getBlock();
            block.setType(Material.RED_SHULKER_BOX);
            CustomBlockData data = new CustomBlockData(block, Main.getInstance());
            data.set(Keys.MainBlock, DataType.LOCATION, location);
            data.set(Keys.Block, PersistentDataType.STRING, Key);
            List<Location> locations = new ArrayList<>();
            locations.add(block.getLocation());

            Location uppedBlock = location.clone().add(Utils.DIR2.get(Direction)).add(0, 1, 0);
            PlaceHalfBlock(uppedBlock);
            CustomBlockData data2 = new CustomBlockData(uppedBlock.getBlock(), Main.getInstance());
            data2.set(Keys.MainBlock, DataType.LOCATION, location);
            data2.set(Keys.Block, PersistentDataType.STRING, Key);
            locations.add(uppedBlock);

            CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
            data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
            data1.set(Keys.MainBlock, DataType.LOCATION, location);
        }
    }

    public static void Place8Block(Location location, String Direction) {
        if (!Utils.checkSpace8Block(location.getBlock(), Direction)){return;}
        List<Location> locations = new ArrayList<>();
        Vector frame = DIR.get(Direction);
        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1; dz++) {
                    Block block = location.clone().add(frame.clone().setX(frame.getX() * dx).setY(frame.getY() * dy).setZ(frame.getZ() * dz)).getBlock();
                    block.setType(Material.RED_SHULKER_BOX);
                    CustomBlockData data = new CustomBlockData(block, Main.getInstance());
                    data.set(Keys.MainBlock, DataType.LOCATION, location);
                    locations.add(block.getLocation());
                }
            }
        }


        CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
        data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
        data1.set(Keys.MainBlock, DataType.LOCATION, location);
    }



    public static void Place12Block(Location location, String Direction) {
        if (!Utils.checkSpace12Block(location.getBlock(), Direction)){return;}
        List<Location> locations = new ArrayList<>();
        Vector frame = DIR.get(Direction);
        for (int dx = 0; dx <= 1 + Math.abs(DIR2.get(Direction).getX()); dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1 + Math.abs(DIR2.get(Direction).getZ()); dz++) {
                    Block block = location.clone().add(frame.clone().setX(frame.getX() * dx).setY(frame.getY() * dy).setZ(frame.getZ() * dz)).getBlock();
                    block.setType(Material.RED_SHULKER_BOX);
                    CustomBlockData data = new CustomBlockData(block, Main.getInstance());
                    data.set(Keys.MainBlock, DataType.LOCATION, location);
                    locations.add(block.getLocation());
                }
            }
        }


        CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
        data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
        data1.set(Keys.MainBlock, DataType.LOCATION, location);
    }

    public static void Place13Block(Location location, String Direction) {
        if (!Utils.checkSpace13Block(location.getBlock(), Direction)){return;}
        List<Location> locations = new ArrayList<>();
        Vector frame = DIR.get(Direction).clone();
        for (int dx = 0; dx <= 1 + Math.abs(DIR2.get(Direction).getX()); dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1 + Math.abs(DIR2.get(Direction).getZ()); dz++) {
                    Block block = location.clone().add(frame.clone().setX(frame.getX() * dx).setY(frame.getY() * dy).setZ(frame.getZ() * dz)).getBlock();
                    block.setType(Material.RED_SHULKER_BOX);
                    CustomBlockData data = new CustomBlockData(block, Main.getInstance());
                    data.set(Keys.MainBlock, DataType.LOCATION, location);
                    locations.add(block.getLocation());
                }
            }
        }

        Vector v = MathUtils.moveVector(new Vector(), 1, 0, Direction);
        Block block = location.clone().add(DIR2.get(Direction)).add(0, 2, 0).add(v).getBlock();
        block.setType(Material.RED_SHULKER_BOX);
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        data.set(Keys.MainBlock, DataType.LOCATION, location);
        locations.add(block.getLocation());


        CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
        data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
        data1.set(Keys.MainBlock, DataType.LOCATION, location);
    }

    public static void Place2BlockUp(Location location) {
            Block block = location.clone().add(0, 1, 0).getBlock();
            if (!block.getType().equals(Material.AIR)){return;}
            location.getBlock().setType(Material.RED_SHULKER_BOX);
            block.setType(Material.RED_SHULKER_BOX);
            CustomBlockData data = new CustomBlockData(block, Main.getInstance());
            data.set(Keys.MainBlock, DataType.LOCATION, location);
            List<Location> locations = new ArrayList<>();
            locations.add(block.getLocation());
            CustomBlockData data1 = new CustomBlockData(location.getBlock(), Main.getInstance());
            data1.set(Keys.Blocks, DataType.asList(DataType.LOCATION), locations);
            data1.set(Keys.MainBlock, DataType.LOCATION, location);
    }
}
