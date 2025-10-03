package org.owleebr.professions.FoxCore.GUIUtils;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookGUI {
    private final ItemStack book;


    public BookGUI(Builder b) {
        this.book = b.book;
    }

    public void open(Player player){
        player.openBook(book);
    }

    public static class Builder {
        private ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        List<Component> pages = new ArrayList<>();
        List<Component> components = new ArrayList<>();

        public Builder addLine(Component component){
            component = component.append(Component.text("\n"));
            components.add(component);
            return this;
        }

        public Builder addLine(String line){ components.add(Component.text(line + "\n")); return this; }


        public Builder nextPage(){
            if (components.isEmpty()) return this;
            Component component = components.get(0);
            for (Component comp : components) {
                if (comp.equals(component)) {continue;}
                //comp = comp.append(Component.text("\n"));
                component = component.append(comp);
            }
            components.clear();
            pages.add(component);
            return this;
        }

        public BookGUI build(){
            nextPage();
            book.editMeta(m -> {
                if (m instanceof BookMeta meta) {
                    for (Component component : pages) {
                        meta.addPages(component);
                    }
                }
            });
            return new BookGUI(this);
        }
    }
}
