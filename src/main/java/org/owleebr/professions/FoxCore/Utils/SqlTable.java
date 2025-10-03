package org.owleebr.professions.FoxCore.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlTable {
    private final String tableName;

    public SqlTable(Builder builder) {
        tableName = builder.tableName;
    }

    public void loadTable(File dbdfile, Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Builder{
        private String tableName;

        public Builder addTable(String name){
            this.tableName = "CREATE TABLE IF NOT EXISTS " + name + " " +
                    "(";
            return this;
        }

        public Builder addvalue(String name, String value){
            tableName = this.tableName + name + " " + value + ",";
            return this;
        }

        public SqlTable build(){
            tableName = tableName.substring(0, tableName.length()-1);
            tableName = tableName + "); ";
            return new SqlTable(this);
        }
    }
}
