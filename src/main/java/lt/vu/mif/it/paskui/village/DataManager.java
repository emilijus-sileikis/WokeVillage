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

    public void writeData(NPC npc, int id) {
        String npcData = "data." + id;
        dataConfig.set(npcData + ".id", id);
        dataConfig.set(npcData + ".uuid", npc.getUUID().toString());
        dataConfig.set(npcData + ".name", npc.getName());
        dataConfig.set(npcData + ".x", (int) npc.getLoc().getX());
        dataConfig.set(npcData + ".y", (int) npc.getLoc().getY());
        dataConfig.set(npcData + ".z", (int) npc.getLoc().getZ());
        dataConfig.set(npcData + ".p", npc.getLoc().getPitch());
        dataConfig.set(npcData + ".yaw", npc.getLoc().getYaw());
        dataConfig.set(npcData + ".world", npc.getLoc().getWorld().getName());
        dataConfig.set(npcData + ".role", getRandomRole());
        dataConfig.set(npcData + ".personality", getRandomPersonality());
//        this.dataConfig.set(npcData + ".tex", "");
//        this.dataConfig.set(npcData + ".signature", "");
        saveConfig();
    }

    private Object getRandomPersonality() {
        int a = getRandomNumber(1, 7);
        switch (a)
        {
            case 1:
                return "Hardworking";
            case 2:
                return "Lazy";
            case 3:
                return "Reliable";
            case 4:
                return "Clumsy";
            case 5:
                return "Generous";
            case 6:
                return "Greedy";
            default:
                return "Error";
        }
    }

    private String getRandomRole() {
        int a = getRandomNumber(1, 4);
        switch (a)
        {
            case 1:
                return "LumberJack";
            case 2:
                return "Miner";
            case 3:
                return "Fisher";
            default:
                return "Error";
        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}