package org.owleebr.professions.Commands;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.owleebr.professions.Annotations.ACommand;
import org.owleebr.professions.Const.Package;
import org.owleebr.professions.Main;
import org.reflections.Reflections;

public class CommandManager {
    public static void registerCommands() {
        Reflections reflections = new Reflections(Package.PROF);
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(ACommand.class);

        for (Class<?> commandClass : commandClasses) {
            if (CommandExecutor.class.isAssignableFrom(commandClass)) {
                try {
                    ACommand annotation = commandClass.getAnnotation(ACommand.class);
                    CommandExecutor executor = (CommandExecutor) commandClass.getDeclaredConstructor().newInstance();
                    PluginCommand command = Main.getInstance().getCommand(annotation.name());
                    if(command != null) {
                        command.setExecutor(executor);
                    } else {
                        throw new Exception();
                    }
                } catch (Exception exception) {
                    Bukkit.getLogger().info ("Cannot load commands.");
                    Bukkit.getServer().shutdown();
                    return;
                }
            }
        }
    }
}
