package lt.vu.mif.it.paskui.village.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Represents context required for command executions (sender, command and
 * necessary arguments/flags)
 */
public class CommandContext {

    private final World overworld;
    private final CommandSender sender;
    private final String cmd;
    private final HashMap<String, Argument<?>> args;

    public CommandContext(World overworld, CommandSender sender, @NotNull Command cmd, String @NotNull [] args)
            throws MissingQuotesException, MissingArgumentDataException
    {
        this.overworld = overworld;
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

    public Argument<?> getArg(CommandFlag flag) {
        return this.getArg(flag.getFlag());
    }

    public Argument<?> getDefaultArg(int num) {
        return this.getArg(CommandFlag.CMD_ARGUMENT.getFlag() + num);
    }

    public boolean hasArg(CommandFlag flag) {
        return this.hasArg(flag.getFlag());
    }

    public boolean hasDefaultArg(int num) {
        return this.hasArg(CommandFlag.CMD_ARGUMENT.getFlag() + num);
    }

    // private
    private Argument<?> getArg(String flag) {
        return args.get(flag);
    }

    private boolean hasArg(String flag) {
        return args.containsKey(flag);
    }

    // other
    @Override
    public String toString() {
        return String.format(
                "CommandContext{ s: %s ; cmd: %s }",
                sender,
                cmd
        );
    }

    /**
     * Parses "{@code String[] args}" and stores data contextually inside {@link #args}
     *
     * @param args array of String to parse flags from
     * @throws MissingQuotesException missing ' " ' at end of argument.
     * @throws MissingArgumentDataException missing argument for flag.
     */
    private void parseArgs(String @NotNull [] args)
            throws MissingQuotesException, MissingArgumentDataException
    {
        int argCount = 0; // Counts number of CommandFlag.CMD_ARGUMENT

        for (int i = 1; i < args.length; ++i) {
            CommandFlag flag = CommandFlag.fromString(args[i]);

            switch (flag) {
                case NPC_NAME:
                    ++i;
                case CMD_ARGUMENT: {
                    i = parseStringArgument(argCount, flag, i, args);
                    break;
                }
                case NPC_LOCATION:
                    i = parseLocationArgument(flag, i, args);
                    break;
            }

            if (this.args.containsKey(CommandFlag.CMD_ARGUMENT.getFlag() + argCount)) {
                ++argCount;
            }
        }
    }

    /**
     * Parses additional argument from "{@code String[] args}" as string and adds
     * it to {@link #args}. Should only be used when argument is believed to be a string.
     *
     * @param argCount count of occurrences CommandFlag.CMD_ARGUMENT has appeared
     * @param flag     parsed flag type
     * @param offset   current position of "{@code args}" array
     * @param args     array that stores additional raw arguments for commands
     * @return updated position of args array
     * @throws MissingQuotesException missing ' " ' at end of argument.
     * @throws MissingArgumentDataException missing argument for flag.
     */
    private int parseStringArgument(int argCount, CommandFlag flag, int offset, String @NotNull [] args)
            throws MissingQuotesException, MissingArgumentDataException {
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
        } else if (args[offset].charAt(0) == '"') {
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
        } else {
            // Case for when no " are used
            if (args[offset].startsWith("-") || args[offset].startsWith("--")){
                throw new MissingArgumentDataException(flag, CommandFlag.class.toString());
            }

            arg = new Argument<>(args[offset], String.class);
            this.args.put(ARG_KEY, arg);
        }

        return offset;
    }

    private int parseLocationArgument(CommandFlag flag, int offset, String @NotNull [] args) {
        double[] xyz = new double[3];

        ++offset;
        for (int i = 0; i < flag.getArgCount() && offset < args.length; ++offset, ++i) {
            String val = args[offset];

            try {
                xyz[i] = Double.parseDouble(val);
            } catch (NumberFormatException e) {
                Player parg = Bukkit.getPlayer(val);
                if (parg != null && parg.getLocation().getWorld().equals(overworld)) {
                    xyz = new double[]{
                            parg.getLocation().getX(),
                            parg.getLocation().getY(),
                            parg.getLocation().getZ()
                    };
                    break;
                } else if (val.equals("~")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        switch (i) {
                            case 0 -> xyz[0] = p.getLocation().getX();
                            case 1 -> xyz[1] = p.getLocation().getY();
                            case 2 -> xyz[2] = p.getLocation().getZ();
                        }
                    } else {
                        xyz[i] = 0;
                    }

                }
            }
        }

        Argument<Location> arg = new Argument<>(
                new Location(overworld, xyz[0], xyz[1], xyz[2]),
                Location.class
        );
        this.args.put(flag.getFlag(), arg);

        return offset;
    }

    // Classes
    /**
     * Exception for when argument for a flag is missing.
     */
    public static class MissingArgumentDataException extends Exception {
        MissingArgumentDataException(CommandFlag flag, String wasGiven) {
            super(String.format("Expected argument for %s, was given: %s", flag.getFlag(), wasGiven));
        }
    }

    /**
     * Exception for when ending quotes ('"') are missing.
     */
    public static class MissingQuotesException extends Exception {
        MissingQuotesException() {
            super("One of arguments is missing closing \".");
        }
    }
}
