package org.owleebr.professions.FoxBlock.API;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

public class CustomBlockClickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final String ID;
    private final Player player;
    private final Action action;

    public CustomBlockClickEvent(Block block, String ID, Player player, Action action) {
        this.block = block;
        this.ID = ID;
        this.player = player;
        this.action = action;
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

    public Action getAction() {
        return action;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
