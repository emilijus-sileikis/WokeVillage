package lt.vu.mif.it.paskui.village.command;

/**
 * @param <T> Type of value to be stored inside the class
 */
public record Argument<T>(T value, Class<T> clazz) {

    @Override
    public String toString() {
        return String.format(
                "Argument( value: %s ; clazz: %s )",
                value, clazz
        );
    }
}
