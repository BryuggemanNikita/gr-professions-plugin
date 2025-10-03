package org.owleebr.professions.FoxBlock.API;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomBlockPlaceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final String ID;
    private final Player player;

    public CustomBlockPlaceEvent(Block block, String ID, Player player) {
        this.block = block;
        this.ID = ID;
        this.player = player;
    }

    public Block getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }

    public String getID() {
        return ID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
