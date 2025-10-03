package org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.CustomEvents.GuiOpened;

public class GUI {
    private final Inventory inv;
    private final List<Button> buttonList;
    private final Map<String, InventoryListItems> itemsMap;

    private GUI(Builder builder) {
        this.inv = builder.inventory;
        this.buttonList = builder.buttonList;
        this.itemsMap = builder.itemsMap;
        for (InventoryListItems items : builder.itemsMap.values()){
            items.addItems(this);
        }
    }


    public static class Builder {
        private Inventory inventory;
        private List<Button> buttonList = new ArrayList<>();
        private Map<String, InventoryListItems> itemsMap = new HashMap<>();

        public Builder createInventory(String title, int size) {
            inventory = Bukkit.createInventory(null, size, title);
            return this;
        }

        public Builder addGrayPanels(){
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                item.editMeta(m ->{
                   m.setHideTooltip(true);
                });
                inventory.setItem(i, item);
            }
            return this;
        }
        public Builder addFrame(){
            for (int i = 0; i < inventory.getSize(); i++) {
                if (i < 9 || (i - 9) % 8 == 0 || i % 9 == 0 || i >= (inventory.getSize() - 9)) {}
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                item.editMeta(m ->{
                    m.setHideTooltip(true);
                });
                inventory.setItem(i, item);
            }
            return this;
        }

        public Builder addIcon(Object o, String display, String lore, int slot){
            ItemStack itm = null;
            if (o instanceof ItemStack){
                itm = (ItemStack) o;
            }else if (o instanceof Material){
                itm = new ItemStack((Material) o);
            }

            itm.editMeta(m ->{
               m.setDisplayName(display);
               List<String> loreArr = Arrays.stream(lore.split("\n")).toList();
               m.setLore(loreArr);
            });

            inventory.setItem(slot, itm);
            return this;
        }

        public Builder addButton(Button button, int slot){
            inventory.setItem(slot, button.getItm());
            buttonList.add(button);
            return this;
        }

        public Builder addItemList(InventoryListItems items, String name){
            itemsMap.put(name, items);
            return this;
        }

        public GUI build(){
            return new GUI(this);
        }
    }

    public void addButton(Button button, int slot){
        buttonList.add(button);
        inv.setItem(slot, button.getItm());
    }

    public void addButtonNxtPg(Button button, ClickType type, String name, int slot, Player player){
        button.editRunButton(type, new Runnable() {
            @Override
            public void run() {
                player.closeInventory();
                nextPage(name);
                openGUI(player);
            }
        });
        buttonList.add(button);
        inv.setItem(slot, button.getItm());
    }

    public void addButtonPrevPg(Button button, ClickType type, String name, int slot, Player player){
        button.editRunButton(type, new Runnable() {
            @Override
            public void run() {
                player.closeInventory();
                previosPage(name);
                openGUI(player);
            }
        });
        buttonList.add(button);
        inv.setItem(slot, button.getItm());
    }

    public Inventory getInv() {
        return inv;
    }

    public void openGUI(Player player){
        player.openInventory(inv);
        Bukkit.getServer().getPluginManager().callEvent(new GuiOpened(this));
    }

    public boolean isButton(ItemStack item){
        return buttonList.stream().filter(b -> b.getItm().equals(item)).findFirst().isPresent();
    }

    public Button getButton(ItemStack item){
        return buttonList.stream().filter(b -> b.getItm().equals(item)).findFirst().get();
    }

    public void removeItemList(String name){
        if (itemsMap.containsKey(name)){
            itemsMap.get(name).removeItems(this);
            itemsMap.remove(name);
        }
    }

    public void addItemList(InventoryListItems items, String name){
        itemsMap.put(name, items);
        items.addItems(this);
    }

    public void nextPage(String nameList){
        InventoryListItems listItems = itemsMap.get(nameList);
        listItems.nextPage();
        listItems.removeItems(this);
        listItems.addItems(this);
        itemsMap.put(nameList, listItems);
    }

    public void previosPage(String nameList){
        InventoryListItems listItems = itemsMap.get(nameList);
        listItems.previousPage();
        listItems.removeItems(this);
        listItems.addItems(this);
        itemsMap.put(nameList, listItems);
    }

    public void updateItemList(String nameList, List<?> items){
        InventoryListItems listItems = itemsMap.get(nameList);
        listItems.setObjects(items);
        listItems.addItems(this);
    }
}
