package uhc.core;

/**
 * 🧱 **Minecraft Game Constants**
 * <p>
 * A final, non-instantiable class containing hardcoded, unchangeable values
 * from the standard Minecraft environment.
 * </p>
 */
public final class MinecraftConstants {

    // Prevent instantiation of this utility class
    private MinecraftConstants() {
        throw new UnsupportedOperationException("This is a constants utility class and cannot be instantiated.");
    }

    /**
     * The total number of item slots in a standard, single chest block.
     * (3 rows * 9 columns = 27 slots)
     */
    public static final int CHEST_SLOTS = 27;

    // ... other constants like MAX_WORLD_HEIGHT, TICKS_PER_SECOND, etc.
}