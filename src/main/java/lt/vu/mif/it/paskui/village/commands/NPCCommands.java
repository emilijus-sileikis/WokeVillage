package lt.vu.mif.it.paskui.village.commands;

import lt.vu.mif.it.paskui.village.DataManager;
import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.command.Argument;
import lt.vu.mif.it.paskui.village.command.Command;
import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.util.Logging;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static lt.vu.mif.it.paskui.village.command.CommandFlag.*;

/**
 * Class for storing and implement NPC commands logic.
 */
public class NPCCommands {

    private final DataManager dataManager;
    private final NPCManager npcManager;

    public NPCCommands(final Main plugin) {
        npcManager = plugin.getNPCManager();
        dataManager = plugin.getDataManager();
    }

    // Commands
    @Command(
            roots = "npc",
            mod = { "create" },
            perm = "wokevillage.npc.create")
    public void create(final @NotNull CommandContext context) {
        CommandSender sender = context.getSender();
        Logging.infoLog("NPCCommands::create has been executed.");

        Logging.infoLog(context.toString());

        context.getArgs().forEach(
                (String key, Argument<?> val) -> Logging.infoLog("%s : %s", key, val)
        );

        Role role = context.hasArg(NPC_ROLE)
            ? Role.fromString((String) context.getArg(NPC_ROLE).value())
            : Role.getRandomRole();

        Personality personality = context.hasArg(NPC_PERSONALITY)
            ? Personality.fromString((String) context.getArg(NPC_PERSONALITY).value())
            : Personality.getRandomPersonality();

        String name = context.hasArg(NPC_NAME)
            ? (String) context.getArg(NPC_NAME).value()
            : NPCNames.getRandomName().getName();

        if (name.length() >= 20) {
            sender.sendMessage("The name of the NPC was too long!");
            sender.sendMessage("(Please use 20 or less characters)");
            return;
        }

        name += " The " + role.toStringWithCapInitial();

        Location loc;
        if (context.hasArg(NPC_LOCATION)) {
            loc = (Location) context.getArg(NPC_LOCATION).value();
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            loc = player.getLocation();
            if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
                player.sendMessage("Can not create the NPC in this world!");
                return;
            }
        } else {
            Logging.infoLog("Can not create the NPC!");
            Logging.infoLog("The console MUST use the -l argument!");
            return;
        }


        NPC npc = npcManager.createNPC(name, loc, role, personality);

        if (npc != null) {
            dataManager.writeData(npc, npc.getId());
            sender.sendMessage("NPC CREATED");
            Logging.infoLog("NPC CREATED");
        }
    }

    @Command(
            roots = "npc",
            mod = { "remove" },
            perm = "wokevillage.npc.remove")
    public void remove(@NotNull CommandContext context) {
        Logging.infoLog("NPCCommands::remove has been executed.");
        CommandSender sender = context.getSender();

        Logging.infoLog(context.toString());
        context.getArgs().forEach(
                (String key, Argument<?> val) -> Logging.infoLog("%s : %s", key, val)
        );

        if (!npcManager.npcsExist()) {
            //Bukkit.broadcast(Component.text("No npcs created"));
            sender.sendMessage("No NPCs created");
            return;
        }

        int id = npcManager.getLastId();

        if (context.hasDefaultArg(0)) {
            try {
                Argument<?> arg = context.getDefaultArg(0);
                id = Integer.parseInt((String) arg.clazz().cast(arg.value()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        npcManager.removeNPC(id);
        dataManager.getConfig().set("data." + id, null);
        dataManager.saveConfig();
    }

    @Command(
            roots = "npc",
            mod = { "removeAll" },
            perm = "wokevillage.npc.removeAll")
    public void removeAll(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();

        if (!(npcManager.npcsExist())) {
            //Bukkit.broadcast(Component.text("There are no NPCs to remove!"));
            sender.sendMessage("There are no NPCs to remove!");
            return;
        }

        //Bukkit.broadcast(Component.text("Total of " + npcs.size() + " NPCs were removed!"));
        sender.sendMessage("Total of " + npcManager.getNPCs().size() + " NPCs were removed!");

        npcManager.removeAllNPC();
        dataManager.getConfig().set("data", null);
        dataManager.saveConfig();
    }
}
