package lt.vu.mif.it.paskui.village.command;

import lt.vu.mif.it.paskui.village.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Represents context required for command executions (sender, command and necessary arguments/flags)
 */
public class CommandContext {

    private final CommandSender sender;
    private final String cmd;
    private final HashMap<String, Argument<?>> args;

    public CommandContext(CommandSender sender, @NotNull Command cmd, String @NotNull [] args)
        throws MissingQuotesException
    {
        this.sender = sender;
        this.cmd = cmd.getName() + "." + args[0];
        this.args = new HashMap<>();

        parseArgs(args);
    }

    // Getters, Setters
    public CommandSender getSender() {
        return sender;
    }

    public String getCmd() {
        return cmd;
    }

    public HashMap<String, Argument<?>> getArgs() {
        return args;
    }

    public Argument<?> getArg(String flag) {
        return args.get(flag);
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

    /** Parses "{@code String[] args}" and stores data contextually inside {@link #args}
     * @param args array of String to parse flags from
     * @throws MissingQuotesException missing ' " ' at end of argument.
     */
    private void parseArgs(String @NotNull [] args) throws MissingQuotesException {
        int argCount = 0; // Counts number of CommandFlag.CMD_ARGUMENT

        for (int i = 1; i < args.length; ++i) {
            CommandFlag flag = CommandFlag.fromString(args[i]);

            switch (flag) {
                case NPC_NAME: ++i;
                case CMD_ARGUMENT: {
                    i = parseStringArgument(argCount, flag, i, args);
                    break;
                }
                case NPC_LOCATION:
                    // TODO: Implement location parsing
                    Logging.infoLog("NPC_LOCATION : detected");
                    break;
            }
        }
    }

    /** Parses additional argument from "{@code String[] args}" as string and adds it to {@link #args}.
     * Should only be used when argument is believed to be a string.
     * @param argCount count of occurrences CommandFlag.CMD_ARGUMENT has appeared
     * @param flag parsed flag type
     * @param offset current position of "{@code args}" array
     * @param args array that stores additional raw arguments for commands
     * @return updated position of args array
     * @throws MissingQuotesException missing ' " ' at end of argument.
     */
    private int parseStringArgument(int argCount, CommandFlag flag, int offset, String @NotNull [] args)
            throws MissingQuotesException
    {
        final String ARG_KEY = (flag == CommandFlag.CMD_ARGUMENT) ?
                flag.getFlag() + argCount :  // ARG_KEY = "ARG" + argCount (ex: ARG0)
                flag.getFlag();

        Argument<String> arg;

        if (args[offset].charAt(0) == '"' && args[offset].endsWith("\"")) {
            // Checks whether argument is encapsulated in "" and stores the argument with removed ""
            arg = new Argument<>(
                    args[offset].substring(1, args[offset].length() - 1),
                    String.class
            );
            this.args.put(ARG_KEY, arg);
        }
        else if (args[offset].charAt(0) == '"') {
            // Only checks for argument starting with "
            StringBuilder argStr = new StringBuilder(args[offset].substring(1));

            try {
                for (++offset; args[offset].endsWith("\""); ++offset) {
                    argStr.append(" ").append(args[offset]);
                }
            } catch (Exception e) {
                /* this exception is called when for loop above tries accessing
                 * out of bounds of args array. This is to be expected if the user
                 * typed a command using " but forgetting to end them.
                 */
                throw new MissingQuotesException();
            }

            argStr.append(args[offset], 0, args[offset].length() - 1);
            arg = new Argument<>(
                    argStr.toString(),
                    String.class
            );
            this.args.put(ARG_KEY, arg);
        }
        else {
            // Case for when no " are used
            arg = new Argument<>(args[offset], String.class);
            this.args.put(ARG_KEY, arg);
        }

        return offset;
    }

    // Classes
    public static class MissingQuotesException extends Exception {
        MissingQuotesException() {
            super("One of arguments is missing closing \".");
        }
    }
}
