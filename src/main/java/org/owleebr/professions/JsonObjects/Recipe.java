package org.owleebr.professions.JsonObjects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    public List<String> input;
    public String output;
    public int time;
    public int temp;
    public int range;

    @JsonIgnore
    public List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (String s : input) {
            String[] strings = s.split("\\*");
            ItemStack itm = new ItemStack(Material.valueOf(strings[0]));
            itm.setAmount(Integer.parseInt(strings[1]));
            items.add(itm);
        }
        return items;
    }
}
