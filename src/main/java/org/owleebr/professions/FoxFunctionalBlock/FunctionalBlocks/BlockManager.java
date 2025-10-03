package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks;


import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.mojang.math.Transformation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.owleebr.professions.Const.Package;
import org.owleebr.professions.CustomEvents.PlayerHoverBlockEvent;
import org.owleebr.professions.CustomEvents.PlayerStopHoverBlockEvent;
import org.owleebr.professions.FoxBlock.API.CustomBlockClickEvent;
import org.owleebr.professions.FoxBlock.API.CustomBlockPlaceEvent;
import org.owleebr.professions.FoxBlock.API.CustomBlockRemoveEvent;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.FoxBlock.Utils.Utils;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.Crucible.Crucible;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmitingFurnace.SmitingFurnace;
import org.owleebr.professions.Main;
import org.owleebr.professions.NMS.SendPackets;
import org.reflections.Reflections;
import java.util.List;

public class BlockManager implements Listener {
    Reflections reflections = new Reflections(Package.PROF);
    Set<Class<?>> blockClasses = reflections.getTypesAnnotatedWith(ABlock.class);
    public final Map<Location, FuncBlock> blocks = new HashMap<>();
    public Map<Player, List<Integer>> showDisplays = new HashMap<>();
    public Map<Player, Location> hoveredBlocks = new HashMap<>();

    public void newBlock(FuncBlock block) {
        String sql = "INSERT INTO blocks(class_name, world, x, y, z) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = Main.connection.prepareStatement(sql)) {
            stmt.setString(1, block.getClass().getName());
            stmt.setString(2, block.getLocation().getWorld().getName());
            stmt.setInt(3, block.getLocation().getBlockX());
            stmt.setInt(4, block.getLocation().getBlockY());
            stmt.setInt(5, block.getLocation().getBlockZ());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        blocks.put(block.getLocation(), block);
        block.startTicking();
    }

    public void ERegister(FuncBlock block) {
        blocks.put(block.getLocation(), block);
        block.startTicking();
    }


    @EventHandler
    public void onCustomBlockClick(CustomBlockClickEvent event) {
        Location loc = event.getBlock().getLocation();
        FuncBlock block = blocks.get(loc);
        if (block != null) {
            block.onClick(event.getPlayer(), event.getAction());
        }
    }

    @EventHandler
    public void onCustomBlockBreak(CustomBlockRemoveEvent event){
        Location loc = event.getBlock().getLocation();
        FuncBlock block = blocks.remove(loc);
        if (block != null) {
            String sql = "DELETE FROM blocks WHERE world = ? AND x = ? AND y = ? AND z = ?";

            try (PreparedStatement stmt = Main.connection.prepareStatement(sql)) {
                stmt.setString(1, loc.getWorld().getName());
                stmt.setInt(2, (int) loc.getX());
                stmt.setInt(3, (int) loc.getY());
                stmt.setInt(4, (int) loc.getZ());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            block.stopTicking();
            block.onRemove(event.getDropItem());
        }
    }

    @EventHandler
    public void onCustomBlockPlace(CustomBlockPlaceEvent event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> Cls = blockClasses.stream().filter(c ->
                c.getAnnotation(ABlock.class).name().equals(event.getID())).findFirst().orElse(null);
        if (Cls != null) {
            Constructor<?> constructor = Cls.getDeclaredConstructor(Location.class);
            FuncBlock block = (FuncBlock) constructor.newInstance(event.getBlock().getLocation());
            block.onBlockPlace(event.getBlock(), event.getPlayer());
            newBlock(block);
        }
    }

    @EventHandler
    public void BlockHovered(PlayerHoverBlockEvent event){
        CustomBlockData data = new CustomBlockData(event.getBlock(), Main.getInstance());

        if (data.has(Keys.MainBlock)){
            Location locc = data.get(Keys.MainBlock, DataType.LOCATION);
            if (blocks.containsKey(locc)) {
                blocks.get(locc).HoveredBlock(event.getPlayer());
            }
        }else if (blocks.containsKey(event.getBlock().getLocation())) {
            blocks.get(event.getBlock().getLocation()).HoveredBlock(event.getPlayer());
        }
    }

    @EventHandler
    public void StopBlockHovered(PlayerStopHoverBlockEvent event){
        CustomBlockData data = new CustomBlockData(event.getBlock(), Main.getInstance());

        if (data.has(Keys.MainBlock)){
            Location locc = data.get(Keys.MainBlock, DataType.LOCATION);
            if (blocks.containsKey(locc)) {
                blocks.get(locc).StopHoveredBlock(event.getPlayer());
            }
        }else if (blocks.containsKey(event.getBlock().getLocation())) {
            blocks.get(event.getBlock().getLocation()).StopHoveredBlock(event.getPlayer());
        }
    }
}
