package org.owleebr.professions.CustomEvents;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHoverBlockEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player Player;
    private final Block block;

    public PlayerHoverBlockEvent(Player player, Block block) {
        this.Player = player;
        this.block = block;
    }

    public Player getPlayer() {
        return Player;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
