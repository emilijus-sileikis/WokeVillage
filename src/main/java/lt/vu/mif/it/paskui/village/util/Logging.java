package lt.vu.mif.it.paskui.village.util;

import org.bukkit.plugin.PluginLogger;

import java.util.logging.Level;

public class Logging {

    /** Sends minecraft server LOG for INFO level
     * @param msg String message to log
     */
    public static void infoLog(String msg) {
        PluginLogger.getLogger("WokeVillage").log(Level.INFO, msg);
    }

    /** Formats log entry and logs in INFO channel
     * @param fmt Format of String message to log
     * @param args Array of various data type arguments
     */
    public static void infoLog(String fmt, Object... args) {
        infoLog(String.format(fmt, args));
    }

    /** Sends minecraft server LOG for SEVERE level
     * @param msg String message to log
     */
    public static void severeLog(String msg) {
        PluginLogger.getLogger("WokeVillage").log(Level.SEVERE, msg);
    }

    /** Formats log entry and logs in SEVERE channel
     * @param fmt Format of String message to log
     * @param args Array of various data type arguments
     */
    public static void severeLog(String fmt, Object... args) {
        severeLog(String.format(fmt, args));
    }
}
