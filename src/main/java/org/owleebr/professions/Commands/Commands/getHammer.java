package org.owleebr.professions.Commands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.owleebr.professions.Annotations.ACommand;
import org.owleebr.professions.Const.Keys;

@ACommand(name = "gethammer")
public class getHammer implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player){
            ItemStack hammer = new ItemStack(Material.STONE_AXE);
            hammer.editMeta(m ->{
                m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                m.setDisplayName(ChatColor.RESET + "Молот");
                m.getPersistentDataContainer().set(Keys.hammer, PersistentDataType.BOOLEAN, true);
            });
            player.getInventory().addItem(hammer);
            return true;
        }
        return false;
    }
}
