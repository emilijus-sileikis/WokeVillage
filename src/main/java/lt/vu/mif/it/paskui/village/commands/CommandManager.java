package lt.vu.mif.it.paskui.village.commands;

import org.bukkit.plugin.PluginLogger;

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
        PluginLogger.getLogger("Test").log(Level.INFO, String.valueOf(commands.size()));

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
}
