package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.Anvils;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.owleebr.professions.Const.Keys;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.FoxCore.Utils.ItemUtils;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.Anvils.Const.AnvilKeys;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmithingWorkBench.Const.SWBKeys;
import org.owleebr.professions.JsonObjects.RecipeAnvil;
import org.owleebr.professions.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ABlock(name = "StoneAnvil")
public class StoneAnvill extends FuncBlock {
    Location BlockLocation;
    List<RecipeAnvil> recipes = Main.AnvilRecipeList.values().stream().collect(Collectors.toList());
    int level = 1;
    int hits = 0;

    List<ItemStack> InventoryItems = new ArrayList<>();
    Vector vector;
    List<Quaternionf> rotations = new ArrayList<>();
    List<UUID> DisplayEntities;
    String face;

    public StoneAnvill(Location location) {
        super(location);
        BlockLocation = location;
        rotations.add(new Quaternionf());
        rotations.add(new Quaternionf().rotateX((float)Math.toRadians(90)));
        Regist(location);
    }

    public void Regist(Location loc) {
        Block block = loc.getBlock();
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        if (!data.has(AnvilKeys.Anvil)) return;
        this.InventoryItems = data.get(AnvilKeys.Inventory, DataType.asList(DataType.ITEM_STACK));
        this.DisplayEntities = data.get(AnvilKeys.Displays, DataType.asList(DataType.UUID));
        face = data.get(AnvilKeys.Face, PersistentDataType.STRING);
        rotations.get(0).rotateY((float)Math.toRadians(MathUtils.getDegrees(face)));
        Vector vc = MathUtils.rotateAroundY(new Vector(0.2, 0, -0.1), MathUtils.getDegreesForVec(face));
        vector = vc;

    }

    @Override
    public void onBlockPlace(Block block, Player player) {
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        data.set(AnvilKeys.Anvil, PersistentDataType.BOOLEAN, true);
        List<UUID> uuids = new ArrayList<>();
        data.set(AnvilKeys.Displays, DataType.asList(DataType.UUID), uuids);
        List<ItemStack> items = new ArrayList<>();
        data.set(AnvilKeys.Inventory, DataType.asList(DataType.ITEM_STACK), items);
        String direction = GameUtils.getFacingDirection(player);
        face = direction;
        data.set(AnvilKeys.Face, PersistentDataType.STRING, direction);

        Regist(block.getLocation());
    }
    @Override
    public void onClick(Player player, Action action) {
        if (action.isRightClick()){
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand.getPersistentDataContainer().has(Keys.input)){
                ItemStack itm = hand.getPersistentDataContainer().get(Keys.input, DataType.ITEM_STACK);
                if (itm.getPersistentDataContainer().has(Keys.hot)){
                    hand.editMeta(m ->{
                        m.lore(new ArrayList<>());
                        m.removeEnchantments();
                        m.setDisplayName(ChatColor.RESET + "Щипцы");
                        m.getPersistentDataContainer().remove(Keys.input);
                    });
                    player.setItemInHand(hand);
                    AddItem(itm);
                }
            }else if (hand.getPersistentDataContainer().has(Keys.forceps)){
                if (!InventoryItems.isEmpty()) {
                    if (InventoryItems.stream().anyMatch(i -> i.getPersistentDataContainer().has(Keys.hot))){
                        ItemStack itm = InventoryItems.stream().filter(i -> i.getPersistentDataContainer().has(Keys.hot)).findFirst().get();
                        hand.editMeta(m ->{
                            m.setDisplayName(ChatColor.RESET + "Щипцы с заготовкой");
                            m.addEnchant(Enchantment.SMITE, 1, true);
                            m.getPersistentDataContainer().set(Keys.input, DataType.ITEM_STACK, itm);
                            Component c = Component.text(ChatColor.GRAY + "Содержит: ");
                            c = c.append(itm.displayName());
                            List<Component> components = new ArrayList<>();
                            components.add(c);
                            m.lore(components);
                        });
                        InventoryItems.remove(itm);
                        clearItems();
                    }
                }
            }
        }else if (action.isLeftClick()){
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand.getPersistentDataContainer().has(Keys.hammer)){
                if (player.getAttackCooldown() < 1) return;
                if (recipes.stream().filter(s -> s.getItems().equals(InventoryItems)).findFirst().isPresent()) {
                    ItemUtils.damageItem(player.getItemInHand(), 15, true);
                    RecipeAnvil recipe = (recipes.stream().filter(s -> s.getItems().equals(InventoryItems)).findFirst()).get();
                    if (recipe.level <= level) {
                        if (hits == recipe.hits){
                            ItemStack itemStack = recipe.getOuItem();
                            hits = 0;
                            clearItems();
                            AddItem(itemStack);
                            location.getWorld().playSound(location, Sound.BLOCK_ANVIL_USE, 1, 1);
                        }else {
                            location.getWorld().playSound(location, Sound.BLOCK_ANVIL_PLACE, 1, 1);
                            hits++;
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onTick() {

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
