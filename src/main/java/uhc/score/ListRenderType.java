package uhc.score;

import java.util.Objects;

/**
 * 📊 **Scoreboard List Render Type**
 * <p>
 * Defines how an objective's value is rendered when displayed in the
 * {@code LIST} (Tab) slot. This allows for numerical values to be
 * represented as either raw digits or visual icons.
 * </p>
 */
public enum ListRenderType {

    // --- 🎨 Render Definitions ---

    /**
     * **Visual Heart Icons**
     * <p>Renders the score as a row of health hearts. Each heart represents 2 points.</p>
     * <p><b>Common Use:</b> Player health in UHC matches.</p>
     */
    HEARTS,

    /**
     * **Raw Numerical Value**
     * <p>Renders the score as a simple integer number.</p>
     * <p><b>Common Use:</b> Kills, Ping, or Level tracking.</p>
     */
    INTEGER;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase identifier used by Minecraft's scoreboard commands.
     * <p><b>Example:</b> {@code ListRenderType.HEARTS.getRenderName()} returns {@code "hearts"}.</p>
     * @return The lowercase string representation of the render type.
     */
    public String getRenderName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a ListRenderType from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. If the input is
     * null or unrecognized, it defaults to {@link #INTEGER} to ensure data
     * visibility is maintained even if the icons fail.</p>
     * @param input The raw type name (e.g., "hearts", "INTEGER").
     * @return The matching {@link ListRenderType}, or {@link #INTEGER} as a fail-safe.
     */
    public static ListRenderType fromString(String input) {
        if (input == null || input.isBlank()) {
            return INTEGER;
        }

        String target = input.toUpperCase().trim();
        try {
            return ListRenderType.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped render types from configs
            return INTEGER;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase identifier for direct use in command building.
     * <p><b>Implementation:</b> Delegates to {@link #getRenderName()}.</p>
     * @return The result of {@link #getRenderName()}.
     */
    @Override
    public String toString() {
        return getRenderName();
    }
}