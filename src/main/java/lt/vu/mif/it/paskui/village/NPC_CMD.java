package lt.vu.mif.it.paskui.village;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NPC_CMD implements CommandExecutor {

    private Main plugin = Main.getInstsance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("npc")) {
                //Todo:These will be needed if we are going to implement skins
                if (args.length == 0) {

                    NPCManager.createNPC(player); //, player.getName()
                    //NPCManager.createNPC(player);
                    player.sendMessage("NPC CREATED");
                    return true;
                }
                NPCManager.createNPC(player); //, args[0]
                //NPCManager.createNPC(player);
                player.sendMessage("NPC CREATED");
                return true;
            }
            //Todo: Possible way to remove npc?
            /*
            if (command.getName().equalsIgnoreCase("remnpc")) {
                NPCManager.removeNPC(player, NPCManager.npcs.get(1));
                player.sendMessage("NPC REMOVED");
                return true;
            }*/
        }
        return true;
    }
}
