package lt.vu.mif.it.paskui.village.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandInjector {

    private Object obj;
    private final Method[] commands;

    public CommandInjector(Class<?> clazz) {
        obj = objectInstantiate(clazz);

        List<Method> cmds = new ArrayList<>();
        for (Method mth : clazz.getMethods()) {
            if (!mth.isAnnotationPresent(Command.class)) continue;

            cmds.add(mth);
        }

        commands = new Method[cmds.size()];
        cmds.toArray(commands);
    }

    // Getters, setters
    /** Returns instance of class
     * @return Instantiated Object
     */
    public Object getInstance() {
        return obj;
    }

    /** Gives array of {@link Command}
     * @return Array of {@link Command} methods
     */
    public Method[] getMethods() {
        return commands;
    }

    // OTHER
    private Object objectInstantiate(Class<?> clazz) {
        try {
            Constructor<?> cnr = clazz.getConstructor();
            obj = cnr.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
