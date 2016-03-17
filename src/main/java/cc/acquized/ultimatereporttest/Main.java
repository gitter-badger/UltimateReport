package cc.acquized.ultimatereporttest;

import cc.acquized.ultimatereporttest.file.Config;
import cc.acquized.ultimatereporttest.file.Messages;
import cc.acquized.ultimatereporttest.sql.Database;
import cc.acquized.ultimatereporttest.utilities.Note;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.logging.Level;

@Note("Early Developement State: Release currently holded")
public class Main extends Plugin {

    public static String pr = "§7[§9UltimateReport§7] §r";
    public boolean firstRun = false;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        Config.loadFile();
        Messages.loadFile();
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
