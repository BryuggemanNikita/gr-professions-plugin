package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.IngotMold;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.owleebr.professions.Const.Keys;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.IngotMold.Const.MoldKeys;
import org.owleebr.professions.Main;
import javax.xml.crypto.Data;
import java.util.ArrayList;

@ABlock(name = "IngotMold")
public class IngotMold extends FuncBlock {
    Location BlockLocation;

    public ItemStack input = null;
    public ItemStack output = null;


    public int coolingTime = Main.getInstance().getConfig().getInt("MoldcoolingTime"); //Время остывание слитка


    public IngotMold(Location location) {
        super(location);
        BlockLocation = location;
        Regist(location);
    }

    public void Regist(Location loc) {
        CustomBlockData data = new CustomBlockData(loc.getBlock(), Main.getInstance());
        if (!data.has(MoldKeys.MoldIngot)) {return;}
        if (data.has(MoldKeys.input)){
            input = data.get(MoldKeys.input, DataType.ITEM_STACK);
        }
        if (data.has(MoldKeys.Output)){
            output = data.get(MoldKeys.Output, DataType.ITEM_STACK);
        }
    }

    @Override
    public void onBlockPlace(Block block, Player player) {
        CustomBlockData data = new CustomBlockData(block, Main.getInstance());
        data.set(MoldKeys.MoldIngot, PersistentDataType.BOOLEAN, true);
        Regist(block.getLocation());
    }


    @Override
    public void onClick(Player player, Action action) {
        if (action.isRightClick()){
            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                ItemStack hand = player.getItemInHand();
                if (hand.getPersistentDataContainer().has(Keys.forceps) && hand.getPersistentDataContainer().has(Keys.input)){
                    if (input == null && output == null){
                        ItemStack cruc = hand.getPersistentDataContainer().get(Keys.input,
                            DataType.ITEM_STACK);
                        input = cruc.getPersistentDataContainer().get(Keys.outputItem, DataType.ITEM_STACK);
                        cruc.editMeta(m ->{
                           m.getPersistentDataContainer().remove(Keys.outputItem);
                        });
                        player.getInventory().addItem(cruc);
                        hand.editMeta(m ->{
                           m.lore(new ArrayList<>());
                           m.removeEnchantments();
                           m.setDisplayName(ChatColor.RESET + "Щипцы");
                           m.getPersistentDataContainer().remove(Keys.input);
                        });
                        player.setItemInHand(hand);
                        CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                        data.set(MoldKeys.input, DataType.ITEM_STACK, input);
                    }
                }
            }else if (output != null){
                player.getInventory().addItem(output);
                output = null;
                CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                data.remove(MoldKeys.Output);
            }
        }else if (action.isLeftClick() && player.isSneaking()){
            if (input == null && output == null) {
                player.sendMessage(ChatColor.RESET + "Форма пуста");
            }else if (input == null && output != null) {
                player.sendMessage("Форма готова");
            }else if (input != null && output == null) {
                player.sendMessage("Слиток остывает");
                player.sendMessage("Осталось: " + coolingTime);
            }
        }
    }

    @Override
    public void onTick() {
        if (input != null){
            if (coolingTime == 0){
                output = input;
                input = null;
                coolingTime = 500;
                CustomBlockData data = new CustomBlockData(BlockLocation.getBlock(), Main.getInstance());
                data.set(MoldKeys.Output, DataType.ITEM_STACK, output);
                data.remove(MoldKeys.input);
            }else {
                coolingTime--;
            }
        }
    }


}
