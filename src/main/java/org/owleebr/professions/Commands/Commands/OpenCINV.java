package org.owleebr.professions.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.owleebr.professions.Annotations.ACommand;
import org.owleebr.professions.FoxBlock.API.Utils.getItemBlock;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.Button;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.ClickType;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.GUI;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.InventoryListItems;
import org.owleebr.professions.FoxCore.Utils.ItemUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ACommand(name = "cinv")
public class OpenCINV implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player){
            final GUI[] gui = new GUI[1];

            List<String> categories = new ArrayList<>();
            categories.add("Blocks");
            List<Button> buttons = new ArrayList<>();
            for (String category : categories) {
                if (category.equalsIgnoreCase("Blocks")){
                    Button button = new Button.Builder()
                        .setItm(getItemBlock.getCustomItemBlockMarker(getItemBlock.getBlocksNames().get(0)), ChatColor.RESET + "" + ChatColor.GOLD + category, "")
                        .addClick(ClickType.LEFT_CLICK, ChatColor.GRAY + "Открыть категорию", new Runnable() {
                            @Override
                            public void run() {
                                List<Button> buttons1 = new ArrayList<>();
                                List<String> items = getItemBlock.getBlocksNames();
                                for (String itemname : items) {
                                    Button button = new Button.Builder()
                                        .setItm(getItemBlock.getCustomItemBlock(itemname), ChatColor.RESET + "" + ChatColor.GOLD + itemname, "")
                                        .addClick(ClickType.LEFT_CLICK, ChatColor.GRAY + "Получить предмет", new Runnable() {
                                            @Override
                                            public void run() {
                                                player.getInventory().addItem(getItemBlock.getCustomItemBlock(itemname));
                                            }
                                        }).build();
                                    buttons1.add(button);
                                }
                                gui[0].removeItemList("Items");
                                gui[0].addItemList(new InventoryListItems.Builder()
                                        .setItems(buttons1)
                                        .setSlots("0-44", "8v")
                                        .build()
                                    ,"Items");
                                gui[0].addButtonNxtPg(new Button.Builder()
                                        .setItm(ItemUtils.getHead("http://textures.minecraft.net/texture/d8aab6d9a0bdb07c135c97862e4edf3631943851efc545463d68e793ab45a3d3"), ChatColor.GOLD + "Следующая страница", "")
                                        .addClick(ClickType.LEFT_CLICK, ChatColor.GRAY + "Перелистать на следующую страницу", new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        }).build()
                                    , ClickType.LEFT_CLICK, "Items", 44, player);
                                gui[0].addButtonPrevPg(new Button.Builder()
                                        .setItm(ItemUtils.getHead("http://textures.minecraft.net/texture/4b221cb9607c8a9bf02fef5d7614e3eb169cc219bf4250fd5715d5d2d6045f7"), ChatColor.GOLD + "Предыдущая страница", "")
                                        .addClick(ClickType.LEFT_CLICK, ChatColor.GRAY + "Перелистать на предыдущую страницу", new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        }).build()
                                    , ClickType.LEFT_CLICK, "Items", 8, player);
                                player.closeInventory();
                                player.updateInventory();
                                gui[0].openGUI(player);
                            }
                        }).build();
                    buttons.add(button);
                }
            }
            gui[0] = new GUI.Builder()
                .createInventory("CINV", 9*6)
                .addItemList(new InventoryListItems.Builder()
                    .setItems(buttons)
                    .setSlots("46-52", "")
                    .build(), "Cattegories")
                .build();
            gui[0].addButtonNxtPg(new Button.Builder()
                    .setItm(ItemUtils.getHead("http://textures.minecraft.net/texture/6ff55f1b32c3435ac1ab3e5e535c50b527285da716e54fe701c9b59352afc1c"), ChatColor.GOLD + "Следующая страница", "")
                    .addClick(ClickType.LEFT_CLICK, ChatColor.GRAY + "Перелистать на следующую страницу", new Runnable() {
                        @Override
                        public void run() {

                        }
                    }).build()
                , ClickType.LEFT_CLICK, "Cattegories", 53, player);
            gui[0].addButtonPrevPg(new Button.Builder()
                    .setItm(ItemUtils.getHead("http://textures.minecraft.net/texture/6768edc28853c4244dbc6eeb63bd49ed568ca22a852a0a578b2f2f9fabe70"), ChatColor.GOLD + "Предыдущая страница", "")
                    .addClick(ClickType.LEFT_CLICK, ChatColor.GRAY + "Перелистать на предыдущую страницу", new Runnable() {
                        @Override
                        public void run() {

                        }
                    }).build()
                , ClickType.LEFT_CLICK, "Cattegories", 45, player);


            player.updateInventory();
            gui[0].openGUI(player);
        }
        return false;
    }
}
