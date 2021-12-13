package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.command.CommandManager;
import lt.vu.mif.it.paskui.village.command.Injector;
import lt.vu.mif.it.paskui.village.commands.NPCCommands;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener, ManagerContainer {

    private static Main instance;
    private CommandManager cmdMgr;
    private DataManager data;
    private NPCManager npcManager;
    private World overworld;

    // JavaPlugin
    @Override
    public void onEnable() {
        Bukkit.getWorlds().forEach(
                (World world) -> {
                    if (world.getName().equals("world")) {
                        overworld = world;
                    }
                }
        );

        this.cmdMgr = new CommandManager();
        this.data = new DataManager(this);
        this.npcManager = new NPCManager();

        this.getServer().getPluginManager().registerEvents(new EventListen(npcManager, data),this);

        if(data.getConfig().contains("data")) {
            spawnNPC();
        }

        registerCommands();

        instance = this;
    }

    @Override
    public void onDisable() {

        for (NPC npc : npcManager.getNPCs().values()) {
            data.writeData(npc, npc.getId());
        }
        npcManager.removeAllNPC();
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

    // ManagerController
    @Override
    public NPCManager getNPCManager() {
        return npcManager;
    }

    @Override
    public DataManager getDataManager() {
        return data;
    }

    // public
    /**
     * Spawns NPCs' from data.yml file
     */
    private void spawnNPC() {
        // TODO: move npc reading to DataManager
        FileConfiguration file = data.getConfig();
        Objects.requireNonNull(data.getConfig().getConfigurationSection("data"))
                .getKeys(false)
                .forEach(npc -> {
                    String npcData = "data." + npc;

                    int id = file.getInt(npcData + ".id");

                    UUID npcUUID = UUID.fromString(
                            Objects.requireNonNull(file.getString(npcData + ".uuid"))
                    );

                    String name = file.getString(npcData + ".name");

                    Location location = new Location(
                            Bukkit.getWorld(
                                    Objects.requireNonNull(file.getString(npcData + ".world"))
                            ),
                            file.getInt(npcData + ".x"),
                            file.getInt(npcData + ".y"),
                            file.getInt(npcData + ".z")
                    );

                    location.setPitch((float) file.getDouble(npcData + ".p"));
                    location.setYaw((float) file.getDouble(npcData + ".yaw"));

                    Role role = Role.fromString( file.getString(npcData + ".role") );
                    Personality personality = Personality.fromString(
                            file.getString(npcData + ".personality")
                    );

                    npcManager.loadNPC(id, name, location, npcUUID, role, personality);
                }
        );
    }

    Location[] path;
    Material[] mat;
    BlockData[] dat;

    public void test()
    {
        Location start = new Location(this.overworld, -199, 67, -111);
        Location end = new Location(this.overworld, -174, 70, -103);

        AStar a = new AStar(start, end, 10000, true, 5);
        path = a.getPath();
        //Bukkit.broadcast(Component.text("Path: " + path.length));

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

    // static
    /** Retrieves a reference of plugin instanec
     * @return ref of WokeVillage.Main plugin
     */
    public static Main getInstance() {
        return instance;
    }

    // private
    private void registerCommands() {
        cmdMgr.setInjector(new Injector(this));

        cmdMgr.register(NPCCommands.class);

        cmdMgr.dump();
    }
}
