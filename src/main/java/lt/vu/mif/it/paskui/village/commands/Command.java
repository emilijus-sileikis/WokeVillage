package lt.vu.mif.it.paskui.village.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String[] roots();

    /** Modifiers
     * Root command modifier: /{@link #roots()} mod
     */
    String[] mod();

    String desc() default "";

    String usage() default "";

    String perm();
}