package lt.vu.mif.it.paskui.village.command;

/**
 * Enum for storing necessary information about command flags
 */
enum CommandFlag {
    CMD_ARGUMENT("ARG", 0),
    NPC_LOCATION("-l", 3),
    NPC_NAME("--name", "-n", 1);

    private final String flag;
    private final String alias;
    private final int argCount;

    CommandFlag(String flag, int argCount) {
        this.flag = flag;
        this.alias = flag;
        this.argCount = argCount;
    }

    CommandFlag(String flag, String alias, int argCount) {
        this.flag = flag;
        this.alias = alias;
        this.argCount = argCount;
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

    public static CommandFlag fromString(String val) {
        for (CommandFlag cFlag : CommandFlag.values()) {
            if (cFlag.flag.equals(val) || cFlag.alias.equals(val))
                return cFlag;
        }

        return CommandFlag.CMD_ARGUMENT;
    }
}