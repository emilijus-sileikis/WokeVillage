package lt.vu.mif.it.paskui.village.commands;

import lt.vu.mif.it.paskui.village.DataManager;
import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.command.CommandFlag;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.NPCManager.NPCTuple;
import lt.vu.mif.it.paskui.village.command.Argument;
import lt.vu.mif.it.paskui.village.command.Command;
import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class for storing and implement NPC commands logic.
 */
public class NPCCommands {

    private final DataManager dataManager;
    private final NPCManager npcManager;

    public NPCCommands(Main plugin) {
        npcManager = plugin.getNPCManager();
        dataManager = plugin.getDataManager();
    }

    // Commands
    @Command(
            roots = "npc",
            mod = { "create" },
            perm = "wokevillage.npc.create")
    public void create(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();
        Logging.infoLog("NPCCommands::create has been executed.");

        Logging.infoLog(context.toString());

        context.getArgs().forEach(
                (String key, Argument<?> val) -> Logging.infoLog("%s : %s", key, val)
        );

        if (sender instanceof Player) {
            Player player = (Player) sender;
            // NOTE: These will be needed if we are going to implement skins
            NPCTuple tuple = npcManager.createNPC(player, player.getLocation(), EntityType.PLAYER);

            if (tuple != null) {
                dataManager.writeData(tuple.npc(), tuple.id());
                player.sendMessage("NPC CREATED");
                Logging.infoLog("NPC CREATED");
            }
        }
        else if (context.getArg(CommandFlag.NPC_LOCATION.getFlag()) == null) {
                Logging.infoLog("Can not create the NPC!");
                Logging.infoLog("The console MUST use the -l argument!");
        }
    }

    @Command(
            roots = "npc",
            mod = { "remove" },
            perm = "wokevillage.npc.remove")
    public void remove(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();
        Logging.infoLog("NPCCommands::remove has been executed.");

        Logging.infoLog(context.toString());

        context.getArgs().forEach(
                (String key, Argument<?> val) -> Logging.infoLog("%s : %s", key, val)
        );

        if (!npcManager.npcsExist()) {
            Bukkit.broadcast(Component.text("No npcs created"));
            return;
        }

        int id = npcManager.getLastId();

        if (context.getArg("ARG0") != null) {
            try {
                Argument<?> arg = context.getArg("ARG0");
                id = Integer.parseInt((String) arg.clazz().cast(arg.value()));
            } catch (Exception e) {
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
        Logging.infoLog("NPCCommands::removeAll has been executed.");

        Logging.infoLog(context.toString());

        context.getArgs().forEach(
                (String key, Argument<?> val) -> Logging.infoLog("%s : %s", key, val)
        );

        npcManager.removeAllNPC();
    }
}
