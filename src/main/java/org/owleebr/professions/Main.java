package org.owleebr.professions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.owleebr.professions.Annotations.AFile;
import org.owleebr.professions.Annotations.AJFile;
import org.owleebr.professions.Commands.CommandManager;
import org.owleebr.professions.FoxBlock.JsonObjects.Block;
import org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIManager;
import org.owleebr.professions.FoxCore.Utils.JsonUtils;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.BlockManager;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.JsonObjects.Recipe;
import org.owleebr.professions.Listeners.ListenerManager;
import org.owleebr.professions.Listeners.events.HoverManager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    @Getter
    private static JavaPlugin instance;

    @Getter
    public static Connection connection;

    @AFile
    File dbFile;

    @AFile
    File Blocks;

    @AFile
    File Recipes;

    @AJFile(file = "Blocks")
    public static Map<String, Block> blockList = new HashMap<>();

    @AJFile(file = "Recipes")
    public static Map<String, Recipe> recipeList = new HashMap<>();


    public static BlockManager blockManager = new BlockManager();


    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        saveDefaultConfig();
        Blocks = new File(getDataFolder(), "Blocks.json");
        Recipes = new File(getDataFolder(), "Recipes.json");
        dbFile = new File(getDataFolder(), "database.db");

        init();
        ListenerManager.registerListeners();
        CommandManager.registerCommands();
        HoverManager.registEvent();
        for (Recipe r : recipeList.values()) {
            Bukkit.getLogger().info("Recipe: " + r.getItems().get(0).getDisplayName() + " " + r.getItems().get(0).getAmount());
        }

    }


    private void init(){
        instance = this;
        ObjectMapper mapper = new ObjectMapper();
        GUIManager guiManager = new GUIManager();

        getServer().getPluginManager().registerEvents(blockManager, this);
        getServer().getPluginManager().registerEvents(guiManager, this);

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(AFile.class)) {
                field.setAccessible(true); // для private полей
                try {
                    File value = (File) field.get(this);
                    if (!value.exists()) {
                        try {
                            value.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(AJFile.class)) {
                //field.setAccessible(true); // для private полей
                try {
                    Object value = field.get(this);
                    value.getClass();
                    Field fiel = this.getClass().getDeclaredField(field.getAnnotation(AJFile.class).file());
                    fiel.setAccessible(true);
                    File file = (File) fiel.get(this);
                    Supplier<?> supplier = null;
                    Class<?> fieldType = field.getType();

                    if (List.class.isAssignableFrom(fieldType)) {
                        supplier = ArrayList::new;
                    } else if (Map.class.isAssignableFrom(fieldType)) {
                        supplier = HashMap::new;
                    } else {
                        // можно задать дефолтный Supplier или null
                        supplier = () -> null;
                    }


                    Object result = JsonUtils.readJsonOrCreate(
                        file,
                        getTypeReference(field),
                        mapper,
                        supplier
                    );

                    field.set(this, result);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        // ///SQL
        String sqlm = "CREATE TABLE IF NOT EXISTS blocks " +
            "(" +
            "class_name TEXT," +
            "world TEXT," +
            "x integer, y integer, z integer" +
            "); " +
            "";

        try {
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            getLogger().info("SQLite подключен!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dbFile.length() > 0){
            try {
                registerAllBlocks(connection, getLogger(), this);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> TypeReference<T> getTypeReference(Field field) {
        Type genericType = field.getGenericType();
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return genericType;
            }
        };
    }

    private static void registerAllBlocks(Connection con, Logger logger, JavaPlugin plugin) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String sql = "SELECT * FROM blocks";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String className = rs.getString("class_name");
            String world = rs.getString("world");
            double x = rs.getInt("x");
            double y = rs.getInt("y");
            double z = rs.getInt("z");

            World bukkitWorld = Bukkit.getWorld(world);
            Location location = new Location(bukkitWorld, x, y, z);

            // // Загружаем класс и создаем объект (если нужно)
            Class<?> clazz = Class.forName(className);
            // //            // Получаем конструктор с параметром Location
            Constructor<?> constructor = clazz.getConstructor(Location.class);

            // //            //            // Создаём экземпляр
            FuncBlock block = (FuncBlock) constructor.newInstance(location);
            // //            //            // Регистрируем
            blockManager.ERegister(block);

        }
    }
}