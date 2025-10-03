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

@ACommand(name = "getforceps")
public class getforceps implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player){
            ItemStack forceps = new ItemStack(Material.STONE_SWORD);
            forceps.editMeta(m ->{
               m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
               m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
               m.setDisplayName(ChatColor.RESET + "Щипцы");
               m.getPersistentDataContainer().set(Keys.forceps, PersistentDataType.BOOLEAN, true);
            });
            player.getInventory().addItem(forceps);
            return true;
        }
        return false;
    }
}
