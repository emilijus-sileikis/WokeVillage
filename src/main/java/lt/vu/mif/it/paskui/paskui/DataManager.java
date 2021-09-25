package lt.vu.mif.it.paskui.paskui;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main plugin) {
        this.plugin = plugin;
        // saves/initializes config
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");

            this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
            InputStream defaultStream = this.plugin.getResource("data.yml");
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                this.dataConfig.setDefaults(defaultConfig);

            }
        }
        public FileConfiguration getDataConfig() {
            if (this.dataConfig == null)
                reloadConfig();

            return  this.dataConfig;
        }

        public void saveConfig() {
            if (this.dataConfig == null || this.configFile == null)
                return;

            try {
                this.getDataConfig().save(this.configFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Can not save config to " + this.configFile, e);
            }
        }

        public void saveDefaultConfig() {
            if (this.configFile == null)
                this.configFile = new File(this.plugin.getDataFolder(), "data.yml");

            if (!this.configFile.exists()) {
                this.plugin.saveResource("data.yml", false);
            }
        }
    }
