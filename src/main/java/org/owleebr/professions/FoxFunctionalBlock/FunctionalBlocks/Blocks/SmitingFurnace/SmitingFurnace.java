package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmitingFurnace;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.mojang.math.Transformation;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.owleebr.professions.Const.Keys;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.FoxCore.Utils.ItemUtils;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmitingFurnace.Const.FurKeys;
import org.owleebr.professions.JsonObjects.Recipe;
import org.owleebr.professions.Main;
import org.owleebr.professions.NMS.SendPackets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.owleebr.professions.FoxBlock.Utils.Utils.DIR2;

@ABlock(name = "Smiting_furnace")
public class SmitingFurnace extends FuncBlock {
    List<Player> hovered = new ArrayList<>();
    Map<Player, List<Integer>> displays = new HashMap<>();
    Location BlockLocation;

    @Getter
    public String face;

    @Getter @Setter
    int Temp = 0;
    ItemStack Crucible = null;
    ItemStack Fuel = null;
    boolean isLit = false;

    int timeCook = 0;
    boolean isCooking = false;

    int burnTime = Main.getInstance().getConfig().getInt("burnTime");; //время горения топлива
    int minBurnTemp = Main.getInstance().getConfig().getInt("minBurnTemp"); //минимальная температура печи
    int CoolingTime = 0; //время остывания печи

    int SoundTime = 250;
    int SmokeTime = 50;


    public SmitingFurnace(Location location) {
        super(location);
        BlockLocation = location;
        Regist(location);
    }

    public void Regist(Location loc){
        CustomBlockData data = new CustomBlockData(loc.getBlock(), Main.getInstance());
        if (!data.has(FurKeys.SmitingFurnace)) {return;}
        face = data.get(FurKeys.Face, PersistentDataType.STRING);
        if (data.has(FurKeys.CrucibleFur)){
            Crucible = data.get(FurKeys.CrucibleFur, DataType.ITEM_STACK);
        }
        if (data.has(FurKeys.Fuel)){
            Fuel = data.get(FurKeys.Fuel, DataType.ITEM_STACK);
        }
    }

    @Override
    public void onBlockPlace(Block block, Player player) {
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        data.set(FurKeys.SmitingFurnace, PersistentDataType.BOOLEAN, true);
        face = GameUtils.getFacingDirection(player);
        data.set(FurKeys.Face, PersistentDataType.STRING, face);
        Regist(block.getLocation());
    }

    @Override
    public void onClick(Player player, Action action) {
        if (action.isRightClick()){
            if (Crucible != null) {
                if ( player.isSneaking()) {
                    if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                        if (!Crucible.getPersistentDataContainer().has(Keys.outputItem)) {
                            if (!Crucible.getPersistentDataContainer().getOrDefault(Keys.hot, PersistentDataType.BOOLEAN, false)) {
                                player.getInventory().addItem(Crucible);
                                Crucible = null;
                                CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                                data.remove(FurKeys.CrucibleFur);
                            }else {
                                player.damage(3);
                            }
                        }else {
                            player.damage(3);
                        }
                        return;
                    }
                }
            }
            if (player.getItemInHand().getType().isFuel()) {
                if (Fuel == null) {
                    if (player.isSneaking()) {
                        ItemStack itm = new ItemStack(Material.AIR);
                        Fuel = player.getItemInHand().clone();
                        player.setItemInHand(itm);
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
                        UpdateDisplays();
                    }else {
                        int Amount = player.getItemInHand().getAmount();
                        if (Amount - 1 > 0){
                            ItemStack itm = player.getItemInHand();
                            itm.setAmount(Amount - 1);
                            player.setItemInHand(itm);
                        }else {
                            ItemStack itm = new ItemStack(Material.AIR);
                            player.setItemInHand(itm);
                        }
                        Fuel = player.getItemInHand().clone();
                        Fuel.setAmount(1);
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
                        UpdateDisplays();
                    }
                }else if(Fuel.isSimilar(player.getItemInHand())){
                    int MaxAmount = Fuel.getType().getMaxStackSize();
                    if (player.isSneaking()) {
                        int Amount = Math.min(player.getItemInHand().getAmount() + Fuel.getAmount(), MaxAmount);
                        int AmountLeft = Math.min(player.getItemInHand().getAmount() - (MaxAmount - Fuel.getAmount()), 0);
                        if (AmountLeft > 0) {
                            ItemStack itm = player.getItemInHand();
                            itm.setAmount(AmountLeft);
                            player.setItemInHand(itm);
                            Fuel.setAmount(Amount);
                            CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                            data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
                            UpdateDisplays();
                        }else {
                            Fuel.setAmount(Amount);
                            player.setItemInHand(new ItemStack(Material.AIR));
                            CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                            data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
                            UpdateDisplays();
                        }
                    }else {
                        if (Fuel.getAmount() + 1 <= MaxAmount) {
                            int Amount = player.getItemInHand().getAmount();
                            if (Amount - 1 > 0){
                                ItemStack itm = player.getItemInHand();
                                itm.setAmount(Amount - 1);
                                player.setItemInHand(itm);
                            }else {
                                ItemStack itm = new ItemStack(Material.AIR);
                                player.setItemInHand(itm);
                            }
                            Fuel.setAmount(Fuel.getAmount() + 1);
                            CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                            data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
                            UpdateDisplays();
                        }
                    }
                }
            }else if (player.getItemInHand().getPersistentDataContainer().has(Keys.Crucible)) {
                if (Crucible == null) {
                    Crucible = player.getItemInHand();
                    player.setItemInHand(new ItemStack(Material.AIR));
                    CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                    data.set(FurKeys.CrucibleFur, DataType.ITEM_STACK, Crucible);
                }
            }else if (player.getItemInHand().getPersistentDataContainer().has(Keys.hot)) {
                if (Crucible == null) {
                    Crucible = player.getItemInHand();
                    player.setItemInHand(new ItemStack(Material.AIR));
                    CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                    data.set(FurKeys.CrucibleFur, DataType.ITEM_STACK, Crucible);
                }
            }else if (player.getItemInHand().getType().equals(Material.FLINT_AND_STEEL)) {
                if (Fuel != null) {
                    isLit = true;
                    player.playSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
                    ItemUtils.damageItem(player.getItemInHand(), 1, true);
                }
            }else if (player.getItemInHand().getPersistentDataContainer().has(Keys.forceps)){
                if (Crucible != null) {
                    if (Crucible.getPersistentDataContainer().has(Keys.outputItem)) {
                        if (!player.getItemInHand().getPersistentDataContainer().has(Keys.input)) {
                            ItemStack itm = player.getItemInHand();
                            itm.editMeta(m ->{
                               m.setDisplayName(ChatColor.RESET + "Щипцы с тиглем");
                               m.addEnchant(Enchantment.SMITE, 1, true);
                               m.getPersistentDataContainer().set(Keys.input, DataType.ITEM_STACK, Crucible);
                               Component c = Component.text(ChatColor.GRAY + "Содержит: ");
                               c = c.append(Component.translatable(Crucible.getPersistentDataContainer().get(Keys.outputItem, DataType.ITEM_STACK).getType().getItemTranslationKey()));
                               List<Component> components = new ArrayList<>();
                               components.add(c);
                               m.lore(components);
                            });
                            Crucible = null;
                            player.setItemInHand(itm);
                            CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                            data.remove(FurKeys.CrucibleFur);
                        }
                    }else if (Crucible.getPersistentDataContainer().has(Keys.hot)) {
                        ItemStack itm = player.getItemInHand();
                        itm.editMeta(m ->{
                            m.setDisplayName(ChatColor.RESET + "Щипцы с заготовкой");
                            m.addEnchant(Enchantment.SMITE, 1, true);
                            m.getPersistentDataContainer().set(Keys.input, DataType.ITEM_STACK, Crucible);
                            Component c = Component.text(ChatColor.GRAY + "Содержит: ");
                            c = c.append(Crucible.getItemMeta().displayName());
                            c.color(TextColor.color(211, 211, 211));
                            List<Component> components = new ArrayList<>();
                            components.add(c);
                            m.lore(components);
                        });
                        Crucible = null;
                        player.setItemInHand(itm);
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.remove(FurKeys.CrucibleFur);
                    }
                }
            }
        }else if (action.isLeftClick() && player.isSneaking()) {
            player.sendMessage("Температура: " + Temp);
            String status = "";
            if (Crucible == null) {
                status = "Тигель отсутствует";
            }else if (!Crucible.getPersistentDataContainer().has(Keys.outputItem)) {
                status = "Не готово";
            }else {
                status = "Тигель полон";
            }
            player.sendMessage("Состояние тигля: " + status);
            if (isCooking){
                player.sendMessage("Осталось до готовки: " + timeCook);
            }
        }

    }

    @Override
    public void onRemove(ItemStack stack) {
        if (!hovered.isEmpty()){
            if (hovered != null){
                for (Player player : hovered) {
                    if (displays.containsKey(player)){
                        List<Integer> IDs = displays.get(player);
                        for (Integer ID : IDs){
                            SendPackets.stopVision(ID, player);
                        }
                    }
                }
            }
            hovered.clear();
        }
        if (Fuel != null){
            BlockLocation.getWorld().dropItemNaturally(BlockLocation, Fuel);
        }
        if (Crucible != null){
            BlockLocation.getWorld().dropItemNaturally(BlockLocation, Crucible);
        }
    }

    @Override
    public void onTick() {
        if (isLit){
            if (burnTime == 0){
                if (Fuel == null){
                    isLit = false;
                }else {
                    int amount = Fuel.getAmount();
                    if ((amount - 1) > 0){
                        amount = amount - 1;
                        Fuel.setAmount(amount);
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
                        UpdateDisplays();
                    }else {
                        Fuel = null;
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.remove(FurKeys.Fuel);
                        UpdateDisplays();
                    }
                    burnTime = Main.getInstance().getConfig().getInt("burnTime");
                }
            }else {
                burnTime--;
            }
            if (CoolingTime == 0){
                CoolingTime = Main.getInstance().getConfig().getInt("CoolingTime");;
                Temp = Math.max(Temp - 1, minBurnTemp);
            }else {
                CoolingTime--;
            }
            if (Crucible != null){
                if (Crucible.getPersistentDataContainer().has(Keys.Crucible)){
                    List<ItemStack> items = Crucible.getPersistentDataContainer().get(Keys.Crucible,
                        DataType.asList(DataType.ITEM_STACK));
                    if (Main.recipeList.values().stream().anyMatch(r -> r.getItems().equals(items))){
                        Recipe recipe = Main.recipeList.values().stream().filter(r -> r.getItems().equals(items)).findFirst().get();
                        if (!isCooking){
                            timeCook = recipe.time;
                            isCooking = true;
                        }else {
                            if (Math.abs(Temp - recipe.temp) <= recipe.range){
                                if (timeCook == 0){
                                    items.clear();
                                    Crucible.editMeta(m ->{
                                        String[] strings = recipe.output.split("\\*");
                                        m.getPersistentDataContainer().set(Keys.Crucible, DataType.asList(DataType.ITEM_STACK), items);
                                        ItemStack itm = recipe.getOuItem();
                                        m.getPersistentDataContainer().set(Keys.outputItem, DataType.ITEM_STACK, itm);
                                    });
                                    isCooking = false;
                                }else {
                                    timeCook--;
                                }
                            }else {
                                timeCook = recipe.time;
                            }
                        }
                    }
                }else if (Crucible.getPersistentDataContainer().has(Keys.hot)){
                    if (Crucible.getPersistentDataContainer().get(Keys.hot, PersistentDataType.BOOLEAN) == false ){
                        if (Main.recipeList.values().stream().anyMatch(r -> r.getItems().contains(Crucible))){
                            Recipe recipe = Main.recipeList.values().stream().filter(r -> r.getItems().contains(Crucible)).findFirst().get();
                            if (!isCooking){
                                timeCook = recipe.time;
                                isCooking = true;
                            }else {
                                if (Math.abs(Temp - recipe.temp) <= recipe.range){
                                    if (timeCook == 0){
                                        Crucible = recipe.getOuItem();
                                        isCooking = false;
                                    }else {
                                        timeCook--;
                                    }
                                }else {
                                    timeCook = recipe.time;
                                }
                            }
                        }
                    }
                }

            }
            if (SoundTime == 0){
                SoundTime = 250;
                Bukkit.getScheduler().runTask(Main.getInstance(), () ->{
                    BlockLocation.getWorld().playSound(BlockLocation, Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 2, 0);
                });
            }else {
                SoundTime--;
            }
            Random rnd = new Random();
            int chance = rnd.nextInt(100);
            if (chance > 50){
                int count = rnd.nextInt(3) + 1;
                Vector v = MathUtils.moveVector(new Vector(), -1f, -0.5f, face);
                Bukkit.getScheduler().runTask(Main.getInstance(), () ->{
                    BlockLocation.getWorld().spawnParticle(Particle.FLAME, BlockLocation.clone().add(DIR2.get(face)).add(0, 1, 0).add(v), count, 0.125, 0.0625, 0.125, 0.01);
                });

            }
        }else {
            if (Temp > 0){
                Temp = 0;
            }
        }
    }

    @Override
    public void HoveredBlock(Player player){
        hovered.add(player);
        int i = 0;
        Location blockDisplay = BlockLocation.clone().add(DIR2.get(face)).add(0.5, 0.5, 0.5);
        final List<Integer> IDs = new ArrayList<>();
        if (Fuel != null){
            Vector v = MathUtils.moveVector(new Vector(), -0.6f, 0, face);
            Quaternionf rotation = MathUtils.rotateWithQuaternion(face);
            int ID1 = SendPackets.showItemDisplay(player, blockDisplay.clone().add(v).add(0, 0.2, 0), Fuel, true, new Transformation(new Vector3f(), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
            int ID2 = SendPackets.showTextDisplay(player, blockDisplay.clone().add(v).subtract(0, 0.4, 0), "" + Fuel.getAmount(), true, new Transformation(new Vector3f(), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
            IDs.add(ID1);
            IDs.add(ID2);
        }
        displays.put(player, IDs);
    }

    public void UpdateDisplays(){
        for (Player player : hovered) {
            if (!displays.containsKey(player)) {return;}
            List<Integer> IDs = displays.get(player);
            displays.remove(player);
            for (Integer ID : IDs){
                SendPackets.stopVision(ID, player);
            }
            Location blockDisplay = BlockLocation.clone().add(DIR2.get(face)).add(0.5, 0.5, 0.5);
            IDs.clear();
            if (Fuel != null){
                Vector v = MathUtils.moveVector(new Vector(), -0.6f, 0, face);
                Quaternionf rotation = MathUtils.rotateWithQuaternion(face);
                int ID1 = SendPackets.showItemDisplay(player, blockDisplay.clone().add(v).add(0, 0.2, 0), Fuel, true, new Transformation(new Vector3f(), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
                int ID2 = SendPackets.showTextDisplay(player, blockDisplay.clone().add(v).subtract(0, 0.4, 0), "" + Fuel.getAmount(), true, new Transformation(new Vector3f(), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
                IDs.add(ID1);
                IDs.add(ID2);
            }
            displays.put(player, IDs);

        }
    }

    @Override
    public void StopHoveredBlock(Player player){
        if (displays.containsKey(player)){
            List<Integer> IDs = displays.get(player);
            for (Integer ID : IDs){
                SendPackets.stopVision(ID, player);
            }
        }
        hovered.remove(player);
    }

}
