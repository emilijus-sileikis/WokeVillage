package lt.vu.mif.it.paskui.paskui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class NPC_CMD implements CommandExecutor {

    private Main plugin = Main.getInstsance();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args)
    {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("npc")) {

                plugin.npcManager.createNPC(player,"");

            }
        }

        return true;
    }

}
