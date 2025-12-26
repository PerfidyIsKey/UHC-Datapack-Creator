package uhc.resource.color;

import uhc.text.color.ColorType;

/**
 * 🎨 **Bossbar Color Registry**
 * <p>
 * Defines the specific color palette available for Minecraft Bossbars.
 * Unlike standard chat colors, Bossbars are restricted to this specific
 * set of seven colors defined by the game engine.
 * </p>
 * <p>
 * Implements {@link ColorType} to remain compatible with general color
 * handling systems in the UHC engine.
 * </p>
 */
public enum BossbarColor implements ColorType {

    // --- 🌈 Color Definitions ---

    /** A deep blue bossbar. */
    BLUE,

    /** A vibrant green bossbar. */
    GREEN,

    /** A bright pink bossbar. */
    PINK,

    /** A royal purple bossbar. */
    PURPLE,

    /** A bright red bossbar, often used for high-intensity phases or low health. */
    RED,

    /** A clean white bossbar. */
    WHITE,

    /** A bright yellow bossbar. */
    YELLOW;

    // --- 🛰️ ColorType Implementation ---

    /**
     * Retrieves the lowercase color identifier required for Minecraft bossbar packets and commands.
     * <p><b>Example:</b> {@code BossbarColor.PURPLE.getColor()} returns {@code "purple"}.</p>
     * @return The lowercase string representation of the color.
     */
    @Override
    public String getColor() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a BossbarColor from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. If the input is null,
     * blank, or an invalid color name, it returns {@link #WHITE} as a neutral
     * fail-safe default to ensure the bossbar remains visible.</p>
     * @param input The raw input string (e.g., "RED", "pink", "blue").
     * @return The matching {@link BossbarColor}, or {@link #WHITE} if invalid.
     */
    public static BossbarColor fromString(String input) {
        if (input == null || input.isBlank()) {
            return WHITE;
        }

        String target = input.toUpperCase().trim();
        try {
            return BossbarColor.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped colors and fallback to a visible default
            return WHITE;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase color name for direct insertion into command builders or JSON.
     * <p><b>Implementation:</b> Delegates to {@link #getColor()}.</p>
     * @return The result of {@link #getColor()}.
     */
    @Override
    public String toString() {
        return getColor();
    }
}