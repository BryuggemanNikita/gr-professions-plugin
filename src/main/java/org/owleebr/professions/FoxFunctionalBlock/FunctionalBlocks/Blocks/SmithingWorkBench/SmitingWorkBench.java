package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.Const.SWBKeys;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.SWBInventory.CreateInv;
import org.owleebr.professions.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ABlock(name = "SmitingWorkBench")
public class SmitingWorkBench extends FuncBlock {
    Location BlockLocation;

    List<ItemStack> InventoryItems = new ArrayList<>();
    Vector vector;
    List<Quaternionf> rotations = new ArrayList<>();
    List<UUID> DisplayEntities;
    String face;

    public SmitingWorkBench(Location location) {
        super(location);
        BlockLocation = location;
        rotations.add(new Quaternionf());
        rotations.add(new Quaternionf().rotateX((float)Math.toRadians(90)));
        Regist(location);
    }

    public void Regist(Location loc) {
        Block block = loc.getBlock();
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        if (!data.has(SWBKeys.SmitingWB)) return;
        this.InventoryItems = data.get(SWBKeys.Inventory, DataType.asList(DataType.ITEM_STACK));
        this.DisplayEntities = data.get(SWBKeys.Displays, DataType.asList(DataType.UUID));
        face = data.get(SWBKeys.Face, PersistentDataType.STRING);
        rotations.get(0).rotateY((float)Math.toRadians(MathUtils.getDegrees(face)));
        Vector vc = MathUtils.rotateAroundY(new Vector(0.2, 0, -0.1), MathUtils.getDegreesForVec(face));
        vector = vc;

    }

    @Override
    public void onBlockPlace(Block block, Player player) {
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        data.set(SWBKeys.SmitingWB, PersistentDataType.BOOLEAN, true);
        List<UUID> uuids = new ArrayList<>();
        data.set(SWBKeys.Displays, DataType.asList(DataType.UUID), uuids);
        List<ItemStack> items = new ArrayList<>();
        data.set(SWBKeys.Inventory, DataType.asList(DataType.ITEM_STACK), items);
        String direction = GameUtils.getFacingDirection(player);
        face = direction;
        data.set(SWBKeys.Face, PersistentDataType.STRING, direction);

        Regist(block.getLocation());
    }

    @Override
    public void onClick(Player player, Action action) {
        if (action.isRightClick()){
            if (player.isSneaking()){
                RemoveItem(player);
            }else {
                Inventory inv = CreateInv.getSWBInv(InventoryItems, BlockLocation);
                player.openInventory(inv);
            }
        }
    }

    @Override
    public void onTick() {
        Location center = location.clone().add(0.5, 1.1, 0.5);
        Bukkit.getScheduler().runTask(Main.getInstance(), b ->{
            List<Entity> itm = center.getWorld().getNearbyEntities(center, 0.3, 0.3, 0.3).stream()
                .filter(e -> e instanceof Item).toList();
            if (!itm.isEmpty()) {
                if (itm.getFirst() instanceof Item) {
                    if (InventoryItems.size() < 6){
                        AddItem(((Item) itm.getFirst()).getItemStack());
                        itm.getFirst().remove();
                    }
                }
            }
        });
    }

    public void CraftItem(ItemStack item) {
        clearItems();
        AddItem(item);
    }



    private void AddItem(ItemStack item) {
        if (!InventoryItems.isEmpty()) {
            if (InventoryItems.getLast().isSimilar(item)) {
                ItemStack itm = InventoryItems.getLast();
                InventoryItems.removeLast();
                itm.setAmount(itm.getAmount() + item.getAmount());
                InventoryItems.add(itm);
            }else {
                InventoryItems.add(item);
                UpdateDisplay();
            }
        }else {
            InventoryItems.add(item);
            UpdateDisplay();
        }
        CustomBlockData data = new CustomBlockData(location.getBlock(), Main.getInstance());
        data.set(SWBKeys.Inventory, DataType.asList(DataType.ITEM_STACK), InventoryItems);
    }

    private void RemoveItem(Player player){
        if (InventoryItems.isEmpty()) return;
        for (ItemStack item : InventoryItems) {
            player.getInventory().addItem(item);
        }
        InventoryItems.clear();
        CustomBlockData data = new CustomBlockData(location.getBlock(), Main.getInstance());
        List<ItemStack> items = new ArrayList<>();
        List<UUID> uuids = new ArrayList<>();
        if (!DisplayEntities.isEmpty()) {
            for (UUID entity : DisplayEntities) {
                Bukkit.getEntity(entity).remove();
            }
        }
        DisplayEntities.clear();
        data.set(SWBKeys.Inventory, DataType.asList(DataType.ITEM_STACK), items);
        data.set(SWBKeys.Displays, DataType.asList(DataType.UUID), uuids);
    }


    public void UpdateDisplay(){
        if (InventoryItems.size() <= 3){
            Vector vc = MathUtils.moveVector(vector, 0, ((float) -0.2 * (InventoryItems.size() - 1)), face);
            CreateDisplay(vc, InventoryItems.getLast());
        }else {
            int i = InventoryItems.size() - 3;
            Vector vc = MathUtils.moveVector(vector, -0.2f, (float)( -0.2 * (i - 1)), face);
            CreateDisplay(vc, InventoryItems.getLast());
        }
        CustomBlockData data = new CustomBlockData(location.getBlock(), Main.getInstance());
        data.set(SWBKeys.Displays, DataType.asList(DataType.UUID), DisplayEntities);
    }

    private void CreateDisplay(Vector vector, ItemStack itm){
        Location center = location.clone().add(0.5, 1, 0.5);

        ItemDisplay item = (ItemDisplay) center.getWorld().spawnEntity(center.clone().add(vector), EntityType.ITEM_DISPLAY);;
        Transformation Trans = new Transformation(new Vector3f(0, 0, 0), rotations.get(0), new Vector3f(0.25F, 0.25f, 0.25f), rotations.get(1));
        item.setItemStack(itm);
        item.setTransformation(Trans);
        DisplayEntities.add(item.getUniqueId());
    }

    private void clearItems(){
        InventoryItems.clear();
        for (UUID entity : DisplayEntities) {
            Bukkit.getEntity(entity).remove();
        }
        DisplayEntities.clear();
        CustomBlockData data = new CustomBlockData(location.getBlock(), Main.getInstance());
        data.set(SWBKeys.Displays, DataType.asList(DataType.UUID), DisplayEntities);
        data.set(SWBKeys.Inventory, DataType.asList(DataType.ITEM_STACK), InventoryItems);
    }

    @Override
    public void onRemove(ItemStack item){
        if (!DisplayEntities.isEmpty()) {
            for (UUID entity : DisplayEntities) {
                Bukkit.getEntity(entity).remove();
            }
        }
        CustomBlockData data = new CustomBlockData(location.getBlock(), Main.getInstance());
        data.clear();
    }
}
