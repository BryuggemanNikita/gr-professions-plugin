package org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Button {
    private final ItemStack itm;
    private final Map<ClickType, Runnable> run;

    public ItemStack getItm() {
        return itm;
    }

    public Runnable getRun(ClickType type) {
        return run.get(type);
    }

    private Button(Builder builder) {
        this.itm = builder.itm;
        this.run = builder.run;
    }

    public static class Builder {
       private ItemStack itm;
        Map<ClickType, Runnable> run = new HashMap<>();
        List<String> displays = new ArrayList<>();

       public Builder addClick(ClickType type, String Display ,Runnable onClick) {
           run.put(type, onClick);
           displays.add(ChatColor.YELLOW + type.getTextDisplay() + " " + ChatColor.RESET + "" +  Display);
           return this;
       }

       public Builder setItm(Object item, String Display, String Lore) {
           ItemStack itm = null;
           if (item instanceof ItemStack) {
               itm = (ItemStack) item;
           }else if (item instanceof Material) {
               itm = new ItemStack((Material) item);
           }
           itm.editMeta(m ->{
               m.setDisplayName(Display);
               List<String> strings = Arrays.stream(Lore.split("\n")).toList();
                m.setLore(strings);
           });
           this.itm = itm;
           return this;
       }

       public Button build() {
           this.itm.editMeta(m ->{
               List<String> lore = m.getLore();
               lore.add(" ");
               lore.addAll(displays);
               m.setLore(lore);
           });
           return new Button(this);
       }
    }

    public void runButton(ClickType type) {
        if (run.get(type) != null) {run.get(type).run();}
        else if (run.get(ClickType.ANY) != null) {run.get(ClickType.ANY).run();}
    }

    public void editRunButton(ClickType type, Runnable onClick) {
        run.put(type, onClick);
    }
}
