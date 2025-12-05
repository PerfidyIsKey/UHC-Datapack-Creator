package shared;

/**
 * Defines the four possible modes for the Minecraft {@code /setblock} command,
 * which specify how the existing block at the target location should be handled
 * during the replacement process.
 */
public enum SetMode {

    /** * The old block drops both itself and its contents (as if destroyed by a player).
     * Plays the appropriate block breaking noise.
     */
    DESTROY("destroy"),

    /** * Only air blocks are changed (non-air blocks are unchanged).
     * Equivalent to "only place if air".
     */
    KEEP("keep"),

    /** * The default mode. The old block drops neither itself nor any contents.
     * Plays no sound.
     */
    REPLACE("replace"),

    /** * Places the new block as-is without triggering block updates and shape updates
     * on surrounding blocks.
     */
    STRICT("strict");

    // The lowercase string representation used in the Minecraft command syntax.
    private final String name;

    /**
     * Private constructor that assigns the command-line name to the enum constant.
     *
     * @param name The lowercase mode name required by the Minecraft command.
     */
    SetMode(String name) {
        this.name = name;
    }

    /**
     * Returns the lowercase command argument string for the mode.
     *
     * @return The mode name (e.g., "destroy", "keep").
     */
    @Override
    public String toString() {
        return name;
    }
}