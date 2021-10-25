package lt.vu.mif.it.paskui.village.commands;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.command.Argument;
import lt.vu.mif.it.paskui.village.command.Command;
import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.util.Logging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class for storing and implement NPC commands logic.
 */
public class NPCCommands {

    private final NPCManager npcManager;

    public NPCCommands(Main plugin) {
        npcManager = plugin.getNPCManager();
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
            npcManager.createNPC(player, player.getLocation(), EntityType.PLAYER);
            player.sendMessage("NPC CREATED");
            Logging.infoLog("NPC CREATED");

        }

//         TODO: Implement commented code below as seperate Command Method.
//            if (command.getName().equalsIgnoreCase("remnpc")) {
//                NPCManager.removeNPC(player, NPCManager.npcs.get(1));
//                player.sendMessage("NPC REMOVED");
//                return true;
//            }
    }
}
