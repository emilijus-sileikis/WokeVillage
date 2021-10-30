package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main plugin) {
        this.plugin = plugin;
        // saves/initializes config
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("data.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream)
            );
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();

        return  this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
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

    public void clearConfig() {
        this.configFile.delete();
        saveDefaultConfig();
    }

    public void writeData(Location loc, NPC npc, int id) {
        Main ref = Main.getInstsance();
        String npcData = "data." + id;
        ref.getData().set(npcData + ".id", id);
        ref.getData().set(npcData + ".uuid", npc.getUUID().toString());
        ref.getData().set(npcData + ".name", npc.getName());
        ref.getData().set(npcData + ".x", (int) npc.getLoc().getX());
        ref.getData().set(npcData + ".y", (int) npc.getLoc().getY());
        ref.getData().set(npcData + ".z", (int) npc.getLoc().getZ());
        ref.getData().set(npcData + ".p", npc.getLoc().getPitch());
        ref.getData().set(npcData + ".yaw", npc.getLoc().getYaw());
        ref.getData().set(npcData + ".world", npc.getLoc().getWorld().getName());
//        ref.getData().set(npcData + ".tex", "");
//        ref.getData().set(npcData + ".signature", "");
        ref.saveData();
    }
}