package org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.CustomEvents.GuiOpened;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.Button;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.ClickType;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.GUI;

public class GUIManager implements Listener {
    List<GUI> guis = new ArrayList<GUI>();

    @EventHandler
    public void onGuiOpened(GuiOpened event){
        guis.add(event.getGui());
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){
        if (guis.stream().filter(g -> g.getInv().equals(event.getInventory())).findFirst().isPresent()){
            guis.remove(guis.stream().filter(g -> g.getInv().equals(event.getInventory())).findFirst());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (guis.stream().filter(g -> g.getInv().equals(event.getInventory())).findFirst().isPresent()){
            event.setCancelled(true);
            if (event.getCurrentItem() == null){ return; }
            GUI gui = guis.stream().filter(g -> g.getInv().equals(event.getInventory())).findFirst().get();
            if (gui.isButton(event.getCurrentItem())){
                Button b = gui.getButton(event.getCurrentItem());
                ClickType tp = null;
                if (event.getClick().isRightClick() && !event.getClick().isShiftClick()){
                    tp = ClickType.RIGHT_CLICK;
                }else if (event.getClick().isLeftClick() && !event.getClick().isShiftClick()){
                    tp = ClickType.LEFT_CLICK;
                }else if (event.getClick().isRightClick() && event.getClick().isShiftClick()){
                    tp = ClickType.SHIFT_RIGHT_CLICK;
                }else if (event.getClick().isLeftClick() && event.getClick().isShiftClick()){
                    tp = ClickType.SHIFT_LEFT_CLICK;
                }
                b.runButton(tp);
            }
        }

    }

}
