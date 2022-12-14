package lt.vu.mif.it.paskui.village.command;

import lt.vu.mif.it.paskui.village.util.Logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class Injector {

    private final Object[] args;
    private final Class<?>[] argClasses;

    public Injector(Object... args) {
        this.args = args;
        this.argClasses = new Class<?>[args.length];

        for (int i = 0; i < args.length; ++i) {
            this.argClasses[i] = args[i].getClass();
        }
    }

    // Getters, setters

    /** Returns instance of class that stores {@link Command} annotation methods
     * Suggested using when method is non-static
     * @param clazz Class with {@link Command} annotation methods
     * @return Instantiated Object
     */
    public Object getInstance(Class<?> clazz) {
        final String errFmt = "ERROR initializing: %s";
        Constructor<?> cns;

        try {
            cns = clazz.getConstructor(argClasses);
            return cns.newInstance(args);
        } catch (NoSuchMethodException e) {
            try {
                cns = clazz.getConstructor();
                return cns.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException
                    | InstantiationException | InvocationTargetException ex) {
                Logging.severeLog(errFmt, clazz);
                ex.printStackTrace();
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Logging.severeLog(errFmt, clazz);
            e.printStackTrace();
        }

        return null;
    }
}
