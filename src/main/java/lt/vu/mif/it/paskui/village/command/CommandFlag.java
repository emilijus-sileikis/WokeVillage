package lt.vu.mif.it.paskui.village.command;

import org.bukkit.Location;

/**
 * Enum for storing necessary information about command flags
 */
public enum CommandFlag {
    CMD_ARGUMENT("ARG", 0, String.class),
    NPC_LOCATION("-l", 3, Location.class),
    NPC_NAME("--name", "-n", 1, String.class);

    private final String flag;
    private final String alias;
    private final int argCount;
    private final Class<?> clazz;

    CommandFlag(String flag, int argCount, Class<?> clazz) {
        this.flag = flag;
        this.alias = flag;
        this.argCount = argCount;
        this.clazz = clazz;
    }

    CommandFlag(String flag, String alias, int argCount, Class<?> clazz) {
        this.flag = flag;
        this.alias = alias;
        this.argCount = argCount;
        this.clazz = clazz;
    }

    public String getFlag() {
        return flag;
    }

    public String getAlias() {
        return alias;
    }

    public int getArgCount() {
        return argCount;
    }

    public Class<?> getArgClass() {
        return clazz;
    }

    public static CommandFlag fromString(String val) {
        for (CommandFlag cFlag : CommandFlag.values()) {
            if (cFlag.flag.equals(val) || cFlag.alias.equals(val))
                return cFlag;
        }

        return CommandFlag.CMD_ARGUMENT;
    }
}