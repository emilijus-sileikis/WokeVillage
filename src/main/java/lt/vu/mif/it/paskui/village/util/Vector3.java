package lt.vu.mif.it.paskui.village.util;

/**
 * Immutable Vector of 3 axis (X, Y, Z).
 */
public record Vector3(int x, int y, int z) {

    /**
     * Creates new Vector3 after (X, Y, Z) addition.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param z Z axis coordinate.
     * @return new {@link Vector3} after adding (X, Y, Z).
     */
    public Vector3 add(int x, int y, int z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Creates new Vector3 after adding D from (X, Y, Z).
     * @param d distance
     * @return new Vector3 after adding D from (X, Y, Z).
     */
    public Vector3 add(int d) {
        return this.add(d, d, d);
    }

    /**
     * Creates new Vector3 after (X, Y, Z) subtraction.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param z Z axis coordinate.
     * @return new Vector3 after subtracting (X, Y, Z).
     */
    public Vector3 subtract(int x, int y, int z) {
        return this.add(-x, -y, -z);
    }

    /**
     * Creates new Vector3 after subtracting D from (X, Y, Z).
     * @param d distance.
     * @return new Vector3 after subtracting D from (X, Y, Z).
     */
    public Vector3 subtract(int d) {
        return this.subtract(d, d, d);
    }
}
