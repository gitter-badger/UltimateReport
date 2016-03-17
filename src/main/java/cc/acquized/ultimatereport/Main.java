package cc.acquized.ultimatereport;

import cc.acquized.ultimatereport.file.Config;
import cc.acquized.ultimatereport.sql.Database;
import cc.acquized.ultimatereport.utilities.Note;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.logging.Level;

@Note("Early Developement State: Release holded.")
public class Main extends Plugin {

    public static String pr = "§7[§9UltimateReport§7] §r";
    public boolean firstRun = false;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        Config.loadFile();
        Database.connect();
        getLogger().log(Level.SEVERE, "[UltimateReport] UltimateReport v" + getDescription().getVersion() + " was enabled.");
    }

    @Override
    public void onDisable() {
        Config.saveFile();
        instance = null;
        getLogger().log(Level.SEVERE, "[UltimateReport] UltimateReport v" + getDescription().getVersion() + " was disabled.");
    }
    
    private void registerListeners() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
    }
    
    private void registerCommands() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
    }

    public static Main getInstance() {
        return instance;
    }
    
}
