package org.owleebr.professions;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.owleebr.items.listeners.ItemListener;
import org.owleebr.items.ui.ItemsUI;
import org.owleebr.items.commands.ItemsCoomand;
import org.owleebr.rpg.listeners.PlayerJoinListener;

public class Main extends JavaPlugin {

    @Getter
    private static RPGPlugin instance;

    @Getter
    private static UserCacheStorage userCache;

    @Override
    public void onEnable() {
        init();
        registerCommands();
        registerListeners();
    }

    private void registerCommands(){
        
    }

    private void registerListeners(){

    }
    private void init(){
        instance = this;
        userCache = new UserCacheStorage(this);
        ProfessionsConfig.loadConfig();
    }
}