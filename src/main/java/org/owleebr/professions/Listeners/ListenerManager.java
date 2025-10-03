package org.owleebr.professions.Listeners;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.owleebr.professions.Annotations.AListener;
import org.owleebr.professions.Const.Package;
import org.owleebr.professions.Main;
import org.reflections.Reflections;

public class ListenerManager {
    public static void registerListeners() {
        Reflections reflections = new Reflections(Package.PROF);
        Set<Class<?>> listenerClasses = reflections.getTypesAnnotatedWith(AListener.class);

        for (Class<?> listenerClass : listenerClasses) {
            if (Listener.class.isAssignableFrom(listenerClass)) {
                try {
                    Listener listener = (Listener) listenerClass.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
                } catch (Exception exception) {
                    Bukkit.getLogger().info("Cannot load listeners.");
                    Bukkit.getServer().shutdown();
                    return;
                }
            }
        }
    }
}
