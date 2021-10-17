package lt.vu.mif.it.paskui.village.command;

import lt.vu.mif.it.paskui.village.util.Logging;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CommandManager {

    private final Map<String, Method> commands;
    private final Map<Method, Object> instances;
    private Injector injector;

    public CommandManager() {
        commands = new HashMap<>();
        instances = new HashMap<>();
    }

    // Getters, setters
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    // Other
    /** Command execution method. WIP */
    public void execute(@NotNull CommandContext context) {
        try {
            Method mth = commands.get(context.getCmd()); // gets command method (
            mth.invoke(instances.get(mth), context);     // invokes the method
        } catch (NullPointerException e) {
            // TODO: Implement sending usage message back to CommandSender
            Logging.severeLog("Command ( wokevillage.%s ) does not exist.", context.getCmd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** To inspect given class and register Command methods contained inside.
     * @param clazz Class containing Command methods.
     */
    public void register(Class<?> clazz) {
        Object obj = injector.getInstance(clazz);

        for (Method mth : clazz.getMethods()) {
            if (!mth.isAnnotationPresent(Command.class)) continue;

            // Gets data stored in Command annotation
            Command cmd = mth.getAnnotation(Command.class);

            // Registers each modifier of that command in commands map:
            Arrays.stream(cmd.mod()).forEach(mod -> commands.put(cmd.roots() + "." + mod, mth));
            // Registers Object the method belongs to:
            instances.put(mth, obj);
        }
    }

    public void dump() {
        Logging.infoLog("Commands");

        commands.forEach((String key, Method mth) -> Logging.infoLog("    %s : %s ;", key, mth));

        Logging.infoLog("Instances");

        instances.forEach((Method key, Object obj) -> Logging.infoLog("    %s : %s ;", key, obj));
    }
}
