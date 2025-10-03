package org.owleebr.professions.FoxCore.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

public class SqlBuilder {
    private final String sqll;
    private final List<Object> values;
    private final Connection connection;


    public SqlBuilder(SqlSelectBuilder builder) {
        this.sqll = builder.sql;
        this.values = builder.objectList;
        this.connection = builder.connection;
    }

    public Object getObj(String name) throws SQLException {
        Bukkit.getLogger().info(sqll);
        try (PreparedStatement stmt = connection.prepareStatement(sqll)) {
            int i = 1;
            for (Object o : values){
                stmt.setObject(i, o);
                i++;
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(name); // или передай имя как параметр
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Object> getObjs(String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sqll)) {
            int i = 1;
            for (Object o : values){
                stmt.setObject(i, o);
                i++;
            }
            List<Object> objs = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    objs.add(rs.getObject(name));
                }
                return objs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean contains(String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sqll)) {
            int i = 1;
            for (Object o : values){
                stmt.setObject(i, o);
                i++;
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static class SqlInsertBuilder{
        String sql = "INSERT INTO ";
        List<Object> objectList = new ArrayList<Object>();

        public SqlInsertBuilder addTable(String table){
            sql += table + " ";
            return this;
        }

        public SqlInsertBuilder addValue(Object obj){
            objectList.add(obj);
            return this;
        }

        public void build(Connection connection){
               String values = "VALUES (";
               for (Object o : objectList){
                   values += "?, ";
               }
               values = values.substring(0, values.length()-2);
               values += ")";
               sql += values;
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                int i = 1;
                for (Object o : objectList){
                    stmt.setObject(i, o);
                    i++;
                }
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public static class SqlRemoveBuilder{
        String sql = "DELETE FROM ";
        List<Object> objectList = new ArrayList<Object>();

        public SqlRemoveBuilder addTable(String table){
            sql += table + " ";
            return this;
        }

        public SqlRemoveBuilder addValue(String name, Object obj){
            if (sql.contains("WHERE")){
                sql += "AND " + name + " = ? ";
            }else {
                sql += "WHERE " + name + " = ? ";
            }
            objectList.add(obj);
            return this;
        }

        public void build(Connection connection){
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                int i = 1;
                for (Object o : objectList){
                    stmt.setObject(i, o);
                    i++;
                }
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public static class SqlUpdateBuilder {
        String sql = "UPDATE ";
        List<Object> sets = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();

        public SqlUpdateBuilder addTable(String table){
            sql += table + " ";
            return this;
        }

        public SqlUpdateBuilder addSet(String name, Object obj){
            if (!sql.contains("SET")){
                sql += "SET " + name + " = ? ";
            } else {
                sql += ", " + name + " = ? ";
            }
            sets.add(obj); // важно добавлять объект в список параметров
            return this;
        }

        public SqlUpdateBuilder addValue(String name, Object obj){
            if (sql.contains("WHERE")){
                sql += "AND " + name + " = ? ";
            } else {
                sql += "WHERE " + name + " = ? ";
            }
            objectList.add(obj);
            return this;
        }

        public void build(Connection connection){
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                int i = 1;
                for (Object o : sets){
                    Bukkit.getLogger().info("" + i);
                    stmt.setObject(i++, o);
                }
                for (Object o : objectList){
                    Bukkit.getLogger().info("" + i);
                    stmt.setObject(i++, o);
                }
                Bukkit.getLogger().info(sql);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class SqlSelectBuilder{
        String sql = "SELECT ";
        List<Object> objectList = new ArrayList<Object>();
        Connection connection;

        public SqlSelectBuilder addSelectValue(String name){
            sql += name + " ";
            return this;
        }

        public SqlSelectBuilder addTable(String table){
            sql += "FROM " + table + " ";
            return this;
        }

        public SqlSelectBuilder addValue(String name, Object obj){
            if (sql.contains("WHERE")){
                sql += "AND " + name + " = ? ";
            }else {
                sql += "WHERE " + name + " = ? ";
            }
            objectList.add(obj);
            return this;
        }

        public SqlSelectBuilder addLimint(Integer limit){
            sql += " LIMIT " + limit + " ";
            return this;
        }


        public SqlBuilder build(Connection connection){
            this.connection = connection;
            return new SqlBuilder(this);
        }

    }
}
