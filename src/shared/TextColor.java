package shared;

/**
 * Defines standard Minecraft text colors used in JSON components.
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

    TextColor(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    /**
     * Returns the lowercase name expected by Minecraft JSON components.
     */
    public String getMinecraftName() {
        return minecraftName;
    }

    public String toString() {
        return minecraftName;
    }
}