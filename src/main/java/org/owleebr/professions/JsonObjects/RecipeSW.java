package org.owleebr.professions.JsonObjects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class RecipeSW {
    public List<String> input;
    public String output;

    @JsonIgnore
    public List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (String s : input) {
            String[] strings = s.split("\\*");
            if (strings[0].startsWith("Minecraft.")){
                ItemStack itm = new ItemStack(Material.valueOf(strings[0].substring("Minecraft.".length())));
                itm.setAmount(Integer.parseInt(strings[1]));
                items.add(itm);
            }else if (strings[0].equalsIgnoreCase("Iron_Sword_Blank")){
                ItemStack itm = JSUtils.getIron_Sword_Blank(false);
                itm.setAmount(Integer.parseInt(strings[1]));
                items.add(itm);
            }else if (strings[0].equalsIgnoreCase("Iron_Pomel_Sword")){
                ItemStack itm = JSUtils.getIron_Pomel_Sword(false);
                itm.setAmount(Integer.parseInt(strings[1]));
                items.add(itm);
            }
        }
        return items;
    }

    @JsonIgnore
    public ItemStack getOuItem(){
        String[] strings = output.split("\\*");
        if (strings[0].startsWith("Minecraft.")){
            ItemStack itm = new ItemStack(Material.valueOf(strings[0].substring("Minecraft.".length())));
            itm.setAmount(Integer.parseInt(strings[1]));
            return itm;
        }else if (strings[0].equalsIgnoreCase("Iron_Sword_Blank")){
            ItemStack itm = JSUtils.getIron_Sword_Blank(false);
            itm.setAmount(Integer.parseInt(strings[1]));
            return itm;
        }else if (strings[0].equalsIgnoreCase("Iron_Pomel_Sword")){
            ItemStack itm = JSUtils.getIron_Pomel_Sword(false);
            itm.setAmount(Integer.parseInt(strings[1]));
            return itm;
        }
        return null;
    }
}
