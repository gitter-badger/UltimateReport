package cc.acquized.ultimatereport.file;

import cc.acquized.ultimatereport.Main;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class Messages {

    private static File directory = new File(Main.getInstance().getDataFolder() + File.separator, "messages");
    private static ResourceBundle bundle;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadFile() {
        try {
            if(!directory.isDirectory()) {
                directory.mkdirs();
                Files.copy(Main.getInstance().getResourceAsStream("messages_de.properties"), new File(directory, "messages_de.properties").toPath());
                Files.copy(Main.getInstance().getResourceAsStream("messages_en.properties"), new File(directory, "messages_en.properties").toPath());
            }
            bundle = ResourceBundle.getBundle("messages", new Locale(Config.getConfig().getString("UltimateReport.Language")), new URLClassLoader(new URL[]{directory.toURI().toURL()}));
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] The messages_" + Config.getConfig().getString("UltimateReport.Language") + ".yml File has been loaded.");
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not load the config.yml File. Stacktrace:");
            ex.printStackTrace();
        }
    }

    public static String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', bundle.getString(path));
    }

    public static File getDirectory() {
        return directory;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

}
