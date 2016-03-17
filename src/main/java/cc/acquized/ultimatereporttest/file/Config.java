package cc.acquized.ultimatereporttest.file;

import cc.acquized.ultimatereporttest.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class Config {

    private static File file = new File(Main.getInstance().getDataFolder(), "config.yml");
    private static Configuration config;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadFile() {
        try {
            if(Main.getInstance().getDataFolder().isDirectory()) {
                Main.getInstance().getDataFolder().mkdirs();
                Main.getInstance().firstRun = true;
            }
            if(!file.exists()) {
                Files.copy(Main.getInstance().getResourceAsStream("config.yml"), file.toPath());
            }
            if(config == null) {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }
            if(!config.getString("UltimateReport.Version").equalsIgnoreCase(Main.getInstance().getDescription().getVersion())) {
                file.delete();
                Files.copy(Main.getInstance().getResourceAsStream("config.yml"), file.toPath());
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] The Config.yml File has been loaded.");
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not load the config.yml File. Stacktrace:");
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveFile() {
        try {
            if((file.exists()) && (config != null)) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            }
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not save the config.yml File. Stacktrace:");
            ex.printStackTrace();
        }
    }

    public static File getFile() {
        return file;
    }

    public static Configuration getConfig() {
        return config;
    }

}
