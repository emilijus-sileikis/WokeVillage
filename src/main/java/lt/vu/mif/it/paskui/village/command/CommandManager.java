package lt.vu.mif.it.paskui.village.command;

import lt.vu.mif.it.paskui.village.util.Logging;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CommandManager {

    private final Map<String, Method> commands;
    private final Map<Method, Object> instances;

    public CommandManager() {
        commands = new HashMap<>();
        instances = new HashMap<>();
    }

    /** Command execution method. WIP */
    public void execute() {
        Logging.infoLog(String.valueOf(commands.size()));

        for (Method mth : commands.values()) {
            try {
                mth.invoke(instances.get(mth));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** To inspect given class and register Command methods contained inside.
     * @param clazz Class containing Command methods.
     */
    public void register(Class<?> clazz) {
        CommandInjector inj = new CommandInjector(clazz);

        //Checks whether Command methods where present in class:
        if (inj.getMethods().length == 0) return;

        for (Method mth : inj.getMethods()) {
            Command cmd = mth.getAnnotation(Command.class);

            // Registers each modifier of that command in commands map:
            Arrays.stream(cmd.mod()).forEach(mod -> commands.put(mod, mth));
            // Registers Object the method belongs to:
            instances.put(mth, inj.getInstance());
        }
    }

    public void dump() {
        final String logName = "WokeVillage";
        final Level lvl = Level.INFO;

        Logging.infoLog("Commands");

        commands.forEach((String key, Method mth) -> Logging.infoLog("    %s : %s ;", key, mth));

        Logging.infoLog("Instances");

        instances.forEach((Method key, Object obj) -> Logging.infoLog("    %s : %s ;", key, obj));
    }
}
