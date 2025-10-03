package org.owleebr.professions.FoxBlock.API;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class CustomBlockRemoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final String ID;
    private final Player player;
    private final ItemStack item;

    public CustomBlockRemoveEvent(Block block, String ID, Player player, ItemStack dropItem) {
        this.block = block;
        this.ID = ID;
        this.player = player;
        this.item = dropItem;
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

    public ItemStack getDropItem() {return item;}

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
