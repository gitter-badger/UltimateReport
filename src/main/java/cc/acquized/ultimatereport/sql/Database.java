package cc.acquized.ultimatereport.sql;

import cc.acquized.ultimatereport.Main;
import cc.acquized.ultimatereport.file.Config;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class Database {

    private static Connection connection;

    public static void connect() {
        if(!Main.getInstance().firstRun) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection("jdbc:mysql://" + Config.getConfig().getString("Database.Adress") + ":" + Config.getConfig().getInt("Database.Port")
                + "/" + Config.getConfig().getString("Database.Database") + "?autoReconnect=true", Config.getConfig().getString("Database.Username"), Config.getConfig().getString("Database.Password"));
            } catch (Exception ex) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not connect to MySQL Database. Message: " + ex.getMessage());
                ProxyServer.getInstance().stop("Your MySQL Informations in the config.yml from UltimateReport is incorrect! Correct it and restart the Proxy.");
            }
        } else {
            Main.getInstance().getLogger().log(Level.SEVERE, "It looks like this is the first time you run the Plugin. For that reason, i didn't connect to any database.");
            ProxyServer.getInstance().stop("UltimateReport has not configured a valid MySQL. Please validate your MySQL Informations.");
        }
    }

    public static void disconnect() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not connect to MySQL Database. Message: " + ex.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}
