package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.command.CommandManager;
import lt.vu.mif.it.paskui.village.command.Injector;
import lt.vu.mif.it.paskui.village.commands.NPCCommands;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.pathfinding.AStar;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private NPCManager npcManager;
    public static DataManager data;
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

        this.getServer().getPluginManager().registerEvents(new EventListen(this),this);

        this.npcManager = new NPCManager();
        Main.data = new DataManager(this);

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

    // Getters
    public static FileConfiguration getData() {
        return data.getConfig();
    }

    public static Main getInstsance() {
        return instance;
    }

    public NPCManager getNPCManager() {
        return npcManager;
    }

    // public
    public static void saveData() {
        data.saveConfig();
    }

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

    Location[] path;
    Material[] mat;
    BlockData[] dat;
    
    public void test()
    {
        Location start = new Location(this.overworld, -199, 67, -111);
        Location end = new Location(this.overworld, -180, 67, -111);

        AStar a = new AStar(start, end, 10000, true, 5);
        path = a.getPath();
        Bukkit.broadcast(Component.text("Path: " + path.length));

        mat = new Material[path.length];
        dat = new BlockData[path.length];

        for(int i = 0; i < path.length; i++)
        {
            mat[i] = path[i].getBlock().getType();
            dat[i] = path[i].getBlock().getBlockData();

            path[i].getBlock().setType(Material.GLASS);
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run()
            {
                resetTest();
            }
        }, 80L);
    }

    public void resetTest()
    {
        for(int i = 0; i < path.length; i++)
        {
            path[i].getBlock().setType(mat[i]);
            path[i].getBlock().setBlockData(dat[i]);
        }
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
        cmdMgr = new CommandManager();
        cmdMgr.setInjector(new Injector(this));

        cmdMgr.register(NPCCommands.class);

        cmdMgr.dump();
    }
}
