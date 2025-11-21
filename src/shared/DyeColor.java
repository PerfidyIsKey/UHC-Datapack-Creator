package shared;

/**
 * Represents all 16 standard Minecraft dye colors.
 * Used for building item resource locations (e.g., 'yellow_bundle', 'blue_wool')
 * and for other color-related command arguments.
 */
public enum DyeColor {
    // 16 standard Minecraft dyes
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    /**
     * Returns the lowercase name of the color, required for constructing
     * the Minecraft item resource location path (e.g., "yellow").
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}