package org.owleebr.professions.FoxCore.Utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

public class ItemUtils {
    public static ItemStack getHead(String value){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        head.editMeta(m ->{
           if (m instanceof SkullMeta meta){
               PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
               PlayerTextures textures = profile.getTextures();
               try {
                   textures.setSkin(new URL(value));
               } catch (MalformedURLException e) {
                   throw new RuntimeException(e);
               }
               profile.setTextures(textures);
               meta.setPlayerProfile(profile);
           }
        });
        return head;
    }
}
