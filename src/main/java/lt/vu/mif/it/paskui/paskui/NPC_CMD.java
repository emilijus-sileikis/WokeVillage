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

                plugin.npcManager.createNPC(player,"");

            }
        }

        return true;
    }

}
