package cz.mattheo.storage;

import cz.mattheo.Main;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SQLiteLogger {

    private final Main plugin;
    private Connection connection;

    public SQLiteLogger(){
        this.plugin = Main.getInstance();
        setupDatabase();
    }

    private void setupDatabase(){
        try{
            File dbFile = new File(plugin.getDataFolder(), "maintenance.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            try(Statement statement = connection.createStatement()){
                statement.executeUpdate(
                  "CREATE TABLE IF NOT EXISTS maintenance_log (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "datetime TEXT NOT NULL," +
                          "player TEXT NOT NULL," +
                          "action TEXT NOT NULL,"+
                          "reason TEXT)"
                );
            }
            plugin.getLogger().info("SQLite database is ready!");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void logAction(String player, String action, String reason){
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        try(PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO maintenance_log(datetime, player, action, reason) VALUES (?, ?, ?, ?)"
        )){
            statement.setString(1, now);
            statement.setString(2, player);
            statement.setString(3, action);
            statement.setString(4, reason != null ? reason : "");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            if(connection != null) connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
