package lt.vu.mif.it.paskui.village.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Represents context required for command executions (sender, command and necessary arguments/flags)
 * TODO: Current implementation is missing arguments/flags processing
 */
public class CommandContext {

    private final CommandSender sender;
    private final String cmd;

    public CommandContext(CommandSender sender, Command cmd, String[] args) {
        this.sender = sender;
        this.cmd = cmd.getName() + "." + args[0];
    }

    // Getters, Setters
    public CommandSender getSender() {
        return sender;
    }

    public String getCmd() {
        return cmd;
    }

    // Other

    @Override
    public String toString() {
        return String.format(
                "CommandContext{ s: %s ; cmd: %s }",
                sender,
                cmd
        );
    }
}
