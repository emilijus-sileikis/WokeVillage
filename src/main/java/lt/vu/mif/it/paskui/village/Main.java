package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.command.CommandManager;
import lt.vu.mif.it.paskui.village.command.Injector;
import lt.vu.mif.it.paskui.village.commands.NPCCommands;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public DataManager data;
    private NPCManager npcManager;
    private World overworld;
    private CommandManager cmdMgr;

    // JavaPlugin Overrides
    @Override
    public void onEnable() {
        Bukkit.getWorlds().forEach(
                (World world) -> {
                    if (world.getName().equals("world")) {
                        overworld = world;
                    }
                }
        );

        this.npcManager = new NPCManager();
        this.data = new DataManager(this);
        this.cmdMgr = new CommandManager();

        this.getServer().getPluginManager().registerEvents(new EventListen(npcManager),this);

        if(data.getConfig().contains("data"))
            spawnNPC();

        registerCommands();

        instance = this;
    }

    @Override
    public void onDisable() {
        despawnAllNPC();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args)
    {
        try {
            CommandContext context = new CommandContext(overworld, sender, command, args);
            cmdMgr.execute(context);
        } catch (CommandContext.MissingQuotesException | CommandContext.MissingArgumentDataException e) {
            Logging.infoLog(e.getMessage());
            return false;
        }

        return true;
    }

    // getters
    /** retrieves data.yml configuration
     * @return {@link FileConfiguration} ref of data.yml
     */
    public FileConfiguration getData() {
        return data.getConfig();
    }

    public NPCManager getNPCManager() {
        return npcManager;
    }

    // public
    public void saveData() {
        data.saveConfig();
    }

    /**
     * Spawns NPCs' from data.yml file
     */
    public void spawnNPC() {
        FileConfiguration file = data.getConfig();
        Objects.requireNonNull(data.getConfig().getConfigurationSection("data"))
                .getKeys(false)
                .forEach(npc -> {
                    Location location = new Location(
                            Bukkit.getWorld(
                                    Objects.requireNonNull(file.getString("data." + npc + ".world"))
                            ),
                            file.getInt("data." + npc + ".x"),
                            file.getInt("data." + npc + ".y"),
                            file.getInt("data." + npc + ".z")
                    );

                    location.setPitch((float) file.getDouble("data." + npc + ".p"));
                    location.setYaw((float) file.getDouble("data." + npc + ".yaw"));

                    String name = file.getString("data." + npc + ".name");

                    npcManager.loadNPC(location);
                }
        );
    }

    // static
    /** Retrieves a reference of plugin instanec
     * @return ref of WokeVillage.Main plugin
     */
    public static Main getInstsance() {
        return instance;
    }

    // private
    private void despawnAllNPC() {
        Collection<NPC> npcs = npcManager.getNPCs().values();

        for (NPC npc : npcs) {
            Logging.infoLog("Removed npc: %s", npc.toString());
            npc.remove();
        }
    }

    private void registerCommands() {
        cmdMgr.setInjector(new Injector(this));

        cmdMgr.register(NPCCommands.class);

        cmdMgr.dump();
    }
}
