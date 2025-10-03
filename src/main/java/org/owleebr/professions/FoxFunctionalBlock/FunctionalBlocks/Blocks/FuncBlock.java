package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.owleebr.professions.Main;

public abstract class FuncBlock  {
    protected final Location location;
    private @NotNull BukkitTask tickTask;

    public FuncBlock(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public abstract void onClick(Player player, Action action);

    public void onClickWithItem(Player player, Action action, ItemStack item) {};

    public abstract void onTick();

    public abstract void onBlockPlace(Block block, Player player);

    public void onRemove(ItemStack dropItem) {}

    public void onEntityClick(Player player, Entity entity) {};

    public void startTicking() {
        tickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), this::onTick, 1L, 1L);
    }

    public void stopTicking() {
        if (tickTask != null && !tickTask.isCancelled()) {
            tickTask.cancel();
        }
    }

    public void HoveredBlock(Player player) {}

    public void StopHoveredBlock(Player player) {}
}
