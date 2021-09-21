package lt.vu.mif.it.paskui.paskui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NPC_CMD implements CommandExecutor {

    private Main plugin = Main.getInstsance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("npc")) {

                if (args.length == 0) {

                    NPCManager.createNPC(player, player.getName());
                    player.sendMessage("NPC CREATED");
                    return true;
                }
                NPCManager.createNPC(player, args[0]);
                player.sendMessage("NPC CREATED");
                return true;
            }
        }
        return true;
    }
}
