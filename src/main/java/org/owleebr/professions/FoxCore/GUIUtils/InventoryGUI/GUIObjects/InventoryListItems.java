package org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryListItems {
    private final List<Integer> slots;
    private List<?> objects;
    private Integer page = 1;

    private InventoryListItems(Builder builder) {
        this.slots = builder.slots;
        this.objects = builder.objects;
    }


    public static class Builder {
        private List<Integer> slots;
        private List<?> objects;



        public Builder setItems(List<?> items) {
            this.objects = items;
            return this;
        }

        public Builder setSlots(String slots, String lines) {
            String[] lns = lines.contains(".") ? lines.split(".") : !lines.isEmpty() ? new String[]{lines} : new String[]{};
            List<Integer> numbers = new ArrayList<Integer>();

            String[] parts = slots.split("-");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);

            // если start > end — поменять местами
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }

            for (int i = start; i <= end; i++) {
                numbers.add(i);
            }

            for (int i = 0; i < lns.length; i++) {
                String lastChar = lns[i].substring(lns[i].length() - 1);
                if (lastChar.equalsIgnoreCase("v")) {
                    Integer line = Integer.parseInt(lns[i].substring(0, lns[i].length() - 1));
                    List<Integer> nmb = numbers.stream().filter(n -> n % line == 0 || (n - 9) % line == 0).collect(Collectors.toList());
                    if(line != 0){
                        nmb.remove((Integer) 0);
                    }
                    if (line != 1) {
                        nmb.remove((Integer) 1);
                    }
                    numbers.removeAll(nmb);
                }else if (lastChar.equalsIgnoreCase("h")) {
                    Integer line = Integer.parseInt(lns[i].substring(0, lns[i].length() - 1));
                    List<Integer> nmb = numbers.stream().filter(n -> n / 9 == line).collect(Collectors.toList());
                    numbers.removeAll(nmb);
                }
            }
            this.slots = numbers;
            return this;
        }

        public InventoryListItems build() {
            return new InventoryListItems(this);
        }
    }

    public void addItems(GUI gui){
        int i = 0;
        if (objects == null) {return;}
        if (objects.isEmpty()) {return;}
        if (objects.stream().allMatch(e -> e instanceof ItemStack)) {
            for (Integer slot : slots) {
                int obj = ((page - 1) * slots.size()) + i;
                if (objects.size() <= obj) {break;}
                ItemStack itm = (ItemStack) objects.get(obj);
                gui.getInv().setItem(slot, itm);
                i++;
            }
        }else if (objects.stream().allMatch(e -> e instanceof Button)) {
            for (Integer slot : slots) {
                int obj = ((page - 1) * slots.size()) + i;
                if (objects.size() <= obj) {break;}
                Button b = (Button) objects.get(obj);
                gui.addButton(b, slot);
                i++;
            }
        }
    }

    public void removeItems(GUI gui){
        for (Integer slot : slots) {
            gui.getInv().setItem(slot, new ItemStack(Material.AIR));
        }
    }

   public void nextPage(){
        page = (objects.size() - page * slots.size()) > 0 ? page + 1 : page;
   }

   public void previousPage(){
        page = page > 1 ? page - 1 : page;
   }

   public void setObjects(List<?> objects) {
        this.objects = objects;
   }

}
