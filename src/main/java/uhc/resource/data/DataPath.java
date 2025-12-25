package uhc.resource.data;

/**
 * 📂 **Type-Safe NBT Data Paths**
 * <p>
 * Defines common NBT keys used for navigating data structures in {@code /data}
 * or {@code /function ... with} commands.
 * </p>
 */
public enum DataPath {
    /** Accesses the base of the NBT compound (empty string). */
    ROOT(""),

    /** Targets the inventory or item list. Represents the 'Items' list tag. */
    ITEMS("Items"),

    /** Targets coordinate data. Represents the 'Pos' list tag (usually 3 doubles). */
    POS("Pos"),

    /** Targets custom metadata or nested data objects. Often used in custom markers. */
    DATA("data"),

    /** Targets the result of a calculation or command execution. */
    RESULT("result");

    /** The actual string key used in the Minecraft NBT path syntax. */
    private final String nbtPath;

    /**
     * Constructor for NBT path mapping.
     * @param nbtPath The raw NBT key (e.g., "Items").
     */
    DataPath(String nbtPath) {
        this.nbtPath = nbtPath;
    }

    /**
     * Returns the NBT path as a string.
     * <p>Note: An empty string represents the root of the NBT object.</p>
     * @return The raw NBT key.
     */
    @Override
    public String toString() {
        return nbtPath;
    }
}