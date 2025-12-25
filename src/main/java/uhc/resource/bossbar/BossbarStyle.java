package uhc.resource.bossbar;

import java.util.Objects;

/**
 * 📊 **Bossbar Style Registry**
 * <p>
 * Defines the visual segmentation of a boss health bar. Styles determine how
 * many "notches" or segments the bar is divided into, or if it appears as
 * a single, continuous progress bar.
 * </p>
 */
public enum BossbarStyle {

    // --- 🏗️ Style Definitions ---

    /**
     * A continuous bar without any visual segments.
     */
    PROGRESS,

    /**
     * The bar is divided into 6 distinct segments.
     */
    NOTCHED_6,

    /**
     * The bar is divided into 10 distinct segments.
     */
    NOTCHED_10,

    /**
     * The bar is divided into 12 distinct segments.
     */
    NOTCHED_12,

    /**
     * The bar is divided into 20 distinct segments.
     */
    NOTCHED_20;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase identifier required for Minecraft commands and packets.
     * <p><b>Example:</b> {@code BossbarStyle.NOTCHED_6.getIdentifier()} returns {@code "notched_6"}.</p>
     * @return The lowercase string representation of the style.
     */
    public String getIdentifier() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a BossbarStyle from a raw string identifier.
     * <p><b>Error Catching:</b> Handles null, blank, or unrecognized strings by
     * returning {@link #PROGRESS} as a fail-safe default. This ensures the
     * bossbar remains visible even if configuration data is malformed.</p>
     * @param input The raw input string (e.g., "notched_20" or "PROGRESS").
     * @return The matching {@link BossbarStyle}, or {@link #PROGRESS} if invalid.
     */
    public static BossbarStyle fromString(String input) {
        if (input == null || input.isBlank()) {
            return PROGRESS;
        }

        String target = input.toUpperCase().trim();
        try {
            return BossbarStyle.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped styles and fallback to a smooth bar
            return PROGRESS;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase identifier for direct use in command builders.
     * <p><b>Implementation:</b> Delegates to {@link #getIdentifier()}.</p>
     * @return The result of {@link #getIdentifier()}.
     */
    @Override
    public String toString() {
        return getIdentifier();
    }
}