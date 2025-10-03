package org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects.GUI;

public class GuiOpened extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final GUI gui;

    public GuiOpened(GUI gui) {
        this.gui = gui;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GUI getGui() {
        return gui;
    }
}
