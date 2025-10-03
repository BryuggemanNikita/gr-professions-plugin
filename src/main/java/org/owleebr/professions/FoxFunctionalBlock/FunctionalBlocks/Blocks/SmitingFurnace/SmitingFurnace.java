package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmitingFurnace;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.mojang.math.Transformation;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.owleebr.professions.Const.Keys;
import org.owleebr.professions.FoxBlock.Utils.Utils;
import org.owleebr.professions.FoxCore.Utils.GameUtils;
import org.owleebr.professions.FoxCore.Utils.MathUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmitingFurnace.Const.FurKeys;
import org.owleebr.professions.JsonObjects.Recipe;
import org.owleebr.professions.Main;
import org.owleebr.professions.NMS.SendPackets;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@ABlock(name = "Smiting_furnace")
public class SmitingFurnace extends FuncBlock {
    List<Player> hovered = new ArrayList<>();
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
    int CoolingTime = Main.getInstance().getConfig().getInt("CoolingTime"); //время остывания печи


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
        if (action.isRightClick() && player.isSneaking()) {
            if (Crucible != null) {
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    if (!Crucible.getPersistentDataContainer().has(Keys.outputItem)) {
                        player.getInventory().addItem(Crucible);
                        Crucible = null;
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.remove(FurKeys.CrucibleFur);
                    }
                }
            }
        }else if (action.isRightClick()){
            if (player.getItemInHand().getType().isFuel()) {
                if (Fuel == null) {
                    if (player.isSneaking()) {
                        ItemStack itm = new ItemStack(Material.AIR);
                        Fuel = player.getItemInHand().clone();
                        player.setItemInHand(itm);
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
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
                        }else {
                            Fuel.setAmount(Amount);
                            player.setItemInHand(new ItemStack(Material.AIR));
                            CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                            data.set(FurKeys.Fuel, DataType.ITEM_STACK, Fuel);
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
            }else if (player.getItemInHand().getType().equals(Material.FLINT_AND_STEEL)) {
                if (Fuel != null) {
                    isLit = true;
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
            hovered.clear();
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
                        data.set(FurKeys.CrucibleFur, DataType.ITEM_STACK, Crucible);
                    }else {
                        Fuel = null;
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.remove(FurKeys.Fuel);
                    }
                    burnTime = 500;
                }
            }else {
                burnTime--;
            }
            if (Temp < minBurnTemp){
                Temp = minBurnTemp;
            }else if (Temp > burnTime){
                if (CoolingTime == 0){
                    CoolingTime = 500;
                    Temp = Math.min(Temp - 1, minBurnTemp);
                }else {
                    CoolingTime--;
                }
            }
            if (Crucible != null){
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
                                    ItemStack itm = new ItemStack(
                                        Material.valueOf(strings[0].toUpperCase()));
                                    itm.setAmount(Integer.parseInt(strings[1]));
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
        Location blockDisplay = BlockLocation.clone().add(Utils.DIR2.get(face)).add(0.5, 0.5, 0.5);
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
                if (Fuel != null){
                    Vector v = MathUtils.moveVector(new Vector(), -0.6f, 0, face);
                    Quaternionf rotation = MathUtils.rotateWithQuaternion(face);
                    int ID1 = SendPackets.showItemDisplay(player, blockDisplay.clone().add(v).add(0, 0.2, 0), Fuel, true, new Transformation(new Vector3f(), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
                    int ID2 = SendPackets.showTextDisplay(player, blockDisplay.clone().add(v).subtract(0, 0.4, 0), "" + Fuel.getAmount(), true, new Transformation(new Vector3f(), rotation, new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf()));
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
