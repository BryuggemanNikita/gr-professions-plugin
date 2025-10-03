package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.Crucible;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.mojang.math.Transformation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.owleebr.professions.Const.Keys;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.Main;
import org.owleebr.professions.NMS.SendPackets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ABlock(name = "Crucible")
public class Crucible extends FuncBlock {
   List<Player> hovered = new ArrayList<>();

    Location BlockLocation;
    public List<ItemStack> inventory = new ArrayList<>();
    ItemStack outputItem = null;

    public Crucible(Location location) {
        super(location);
        BlockLocation = location;
    }

    public void Regist(Location loc){
        CustomBlockData data = new CustomBlockData(loc.getBlock(), Main.getInstance());
        if (!data.has(Keys.Crucible)) {return;}
        inventory = data.get(Keys.Crucible, DataType.asList(DataType.ITEM_STACK));
        if (data.has(Keys.outputItem)){
            outputItem = data.get(Keys.outputItem, DataType.ITEM_STACK);
        }
    }

    @Override
    public void onClick(Player player, Action action) {
        if (action.isRightClick() && player.isSneaking()) {
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        if (!inventory.isEmpty()){
                            for (ItemStack item : inventory){
                                player.getInventory().addItem(item);
                            }
                            inventory.clear();
                            data.set(Keys.Crucible, DataType.asList(DataType.ITEM_STACK), inventory);
                        }
        }
    }

    @Override
    public void onTick() {
        if (outputItem == null) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                Location center = BlockLocation.clone().add(0.5, 0.3, 0.5);
                List<Entity> itm = center.getWorld().getNearbyEntities(center, 0.3, 0.3, 0.3).stream()
                    .filter(e -> e instanceof Item).toList();
                if (!itm.isEmpty()) {
                    if (itm.getFirst() instanceof Item itemm) {
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        if (inventory.isEmpty() ){
                            inventory.add(itemm.getItemStack());
                            itemm.remove();
                            data.set(Keys.Crucible, DataType.asList(DataType.ITEM_STACK), inventory);
                        }        else if (inventory.getLast().isSimilar(itemm.getItemStack())){
                            ItemStack lasItem = inventory.getLast();
                            inventory.remove(lasItem);
                            lasItem.setAmount(lasItem.getAmount() + itemm.getItemStack().getAmount());
                            inventory.add(lasItem);
                            itemm.remove();
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onBlockPlace(Block block, Player player) {
        if (player.getItemInHand().getPersistentDataContainer().has(Keys.Crucible)){
                    CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                    data.set(Keys.Crucible, DataType.asList(DataType.ITEM_STACK), player.getItemInHand().getPersistentDataContainer().get(Keys.Crucible, DataType.asList(DataType.ITEM_STACK)));
                    inventory = player.getItemInHand().getPersistentDataContainer().get(Keys.Crucible, DataType.asList(DataType.ITEM_STACK));
        }else {
            CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
            data.set(Keys.Crucible, DataType.asList(DataType.ITEM_STACK), new ArrayList<>());
        }
        Regist(block.getLocation());
    }

    @Override
    public void onRemove(ItemStack dropitem){
        dropitem.editMeta(m ->{
            m.getPersistentDataContainer().set(Keys.Crucible, DataType.asList(DataType.ITEM_STACK), inventory);
        });
        if (!hovered.isEmpty()){
            hovered.clear();
        }
    }

    @Override
    public void HoveredBlock(Player player){
        hovered.add(player);

        String face = GameUtils.getFacingDirection(player);
        Location loc = BlockLocation.clone().add(0.5, 1.5, 0.5);
        int i = 0;
        Vector v = MathUtils.moveVector(new Vector(), 0, -0.5f, face);
        BukkitRunnable runnable = new BukkitRunnable() {
            final List<Integer> IDs = new ArrayList<>();

            @Override
            public void run() {
                if (!hovered.contains(player)) {
                    for (Integer i : IDs){
                        SendPackets.stopVision(i, player);
                    }
                    this.cancel();
                    return;
                }
                for (Integer i : IDs){
                    SendPackets.stopVision(i, player);
                }

                for (ItemStack item : inventory) {
                    int z = i;
                    Vector v2 = v.clone();
                    if (i >= 2){
                        v2.setY(v2.getY() - 0.5);
                        z -= 2;
                    }
                    v2 = MathUtils.moveVector(v2, 0, z*0.8f, face);
                    int ID1 = SendPackets.showItemDisplay(player, loc.clone().add(v2), item, false, new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
                    int ID2 = SendPackets.showTextDisplay(player, loc.clone().add(v2).subtract(0, 0.3, 0), "" + item.getAmount(), false, new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
                    IDs.add(ID1);
                    IDs.add(ID2);
                }
            }
        };
        runnable.runTaskTimer(Main.getInstance(), 0, 2);
    }

    @Override
    public void StopHoveredBlock(Player player){
        hovered.remove(player);
    }



}
