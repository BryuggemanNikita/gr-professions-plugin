package org.owleebr.professions.JsonObjects;

import net.minidev.json.annotate.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.owleebr.professions.Main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CTRecipe {
    public String Slot_0;
    public String Slot_1;
    public String Slot_2;
    public String Slot_3;
    public String Slot_4;
    public String Slot_5;
    public String Slot_6;
    public String Slot_7;
    public String Slot_8;

    public String Output;

    @JsonIgnore
    public ShapedRecipe getRecipe() {
        Bukkit.getLogger().info("Recipe");
        String keyy = getOuItem(Output).getType().name().toLowerCase(Locale.ROOT) + "_" + UUID.randomUUID().toString().substring(0, 8);
        Bukkit.getLogger().info(keyy);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(), keyy), getOuItem(Output));
        Map<String, String> Symbols = new HashMap<>();
        List<String> allItems = new ArrayList<String>();
        allItems.add(Slot_0);
        allItems.add(Slot_1);
        allItems.add(Slot_2);
        allItems.add(Slot_3);
        allItems.add(Slot_4);
        allItems.add(Slot_5);
        allItems.add(Slot_6);
        allItems.add(Slot_7);
        allItems.add(Slot_8);

        String[] keys = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        Set<String> allUnike = new HashSet<>(allItems);
        int i = 0;
        for (String item : allUnike) {
            if (item.equalsIgnoreCase(" ")) continue;
            Symbols.put(item, keys[i]);
            i++;
        }
        List<String> RecipeShp = new ArrayList<>();
        String rp = "";
        for (String key : allItems) {
            if (key.equalsIgnoreCase(" ")){ rp = rp + key;
                if (rp.length() == 3){
                    RecipeShp.add(rp);
                    rp = "";
                }
                continue;}
            rp = rp + Symbols.get(key);
            if (rp.length() == 3){
                RecipeShp.add(rp);
                rp = "";
            }
        }
        String[] shape = new String[]{RecipeShp.get(0), RecipeShp.get(1), RecipeShp.get(2)};
        recipe.shape(shape);

        int k = 0;
        for (String item : allUnike) {
            if (item.equalsIgnoreCase(" ")) continue;
            String[] parts = item.split("\\*");
            String id = parts[0];
            Bukkit.getLogger().info(id);
            int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            if (id.startsWith("Minecraft.")){
                String name = id.substring("Minecraft.".length());
                ItemStack itemStack = new ItemStack(Material.getMaterial(name), amount);
                recipe.setIngredient(keys[k].charAt(0), itemStack);
            }else if (id.equalsIgnoreCase("Iron_Sword_Blank")){
                ItemStack itm = JSUtils.getIron_Sword_Blank(false);
                recipe.setIngredient(keys[k].charAt(0), itm);
            }else if (id.equalsIgnoreCase("Iron_Pomel_Sword")){
                ItemStack itm = JSUtils.getIron_Pomel_Sword(false);
                recipe.setIngredient(keys[k].charAt(0), itm);
            }
            k++;
        }
        return recipe;
    };

    @JsonIgnore
    private ItemStack getOuItem(String item) {
        String[] parts = item.split("\\*");
        String id = parts[0];
        int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
        if (id.startsWith("Minecraft.")){
            String name = id.substring("Minecraft.".length());
            ItemStack itemStack = new ItemStack(Material.getMaterial(name), amount);
            return itemStack;
        }
        return null;
    }
}
