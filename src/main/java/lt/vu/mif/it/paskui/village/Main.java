package lt.vu.mif.it.paskui.village;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    public static DataManager data;
    public NPCManager npcManager;
    public static Inventory inv;
    private static Main instance;

    // JavaPlugin Overrides
    @Override
    public void onEnable() {
        data = new DataManager(this);
        this.getServer().getPluginManager().registerEvents(new EventListen(),this);

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PacketReader reader = new PacketReader(player.getPlayer());
                reader.inject();
            }
        }

        if(data.getConfig().contains("data"))
            loadNPC();

        this.npcManager = new NPCManager();
        instance = this;
    }

    @Override
    public void onDisable() {}

    // Getters, setters
    public static FileConfiguration getData() {
        return data.getConfig();
    }

    public static Main getInstsance() {
        return instance;
    }

    // Others
    public static void saveData() {
        data.saveConfig();
    }

    public void loadNPC() {
        FileConfiguration file = data.getConfig();
        data.getConfig().getConfigurationSection("data").getKeys(false).forEach(npc -> {
            Location location = new Location(Bukkit.getWorld(file.getString("data." + npc + ".world")), file.getInt("data." + npc + ".x"), file.getInt("data." + npc + ".y"),
                    file.getInt("data." + npc + ".z"));
            location.setPitch((float) file.getDouble("data." + npc + ".p"));
            location.setYaw((float) file.getDouble("data." + npc + ".yaw"));

            String name = file.getString("data." + npc + ".name");
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "" + name);
            gameProfile.getProperties().put("textures",new Property("textures", file.getString("data." + npc +".tex"), file.getString("data." + npc + ".signature")));
            NPCManager.loadNPC(location, gameProfile);
        });
    }
}
