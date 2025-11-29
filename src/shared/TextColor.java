package shared;

/**
 * Defines standard Minecraft text colors used in JSON text components (1.16+ format).
 * The values correspond directly to the official lowercase color names expected by the game's JSON parser.
 */
public enum TextColor {
    DARK_RED("dark_red"),
    RED("red"),
    GOLD("gold"),
    YELLOW("yellow"),
    DARK_GREEN("dark_green"),
    GREEN("green"),
    AQUA("aqua"),
    DARK_AQUA("dark_aqua"),
    DARK_BLUE("dark_blue"),
    BLUE("blue"),
    LIGHT_PURPLE("light_purple"),
    DARK_PURPLE("dark_purple"),
    WHITE("white"),
    GRAY("gray"),
    DARK_GRAY("dark_gray"),
    BLACK("black");

    private final String minecraftName;

    /**
     * Private constructor to associate the enum constant with its official Minecraft name.
     * @param minecraftName The lowercase string name used in JSON color fields.
     */
    TextColor(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    /**
     * Returns the lowercase name expected by Minecraft JSON components.
     * This is the value used in the "color" field of a text component object.
     * * @return The Minecraft-specific color name (e.g., "dark_red").
     */
    public String getMinecraftName() {
        return minecraftName;
    }

    /**
     * Overrides the default toString to return the official Minecraft color name.
     * This makes it easy to use the enum directly in methods that accept a String color name.
     * * @return The Minecraft-specific color name.
     */
    @Override
    public String toString() {
        return minecraftName;
    }
}