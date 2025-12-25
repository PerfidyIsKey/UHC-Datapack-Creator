package uhc.score;

/**
 * 📊 **Scoreboard Display Slot Registry**
 * <p>
 * Defines the valid locations where a scoreboard objective can be displayed
 * on the Minecraft client. Each slot serves a specific UI purpose in UHC,
 * such as tracking health in the tab list or game stats on the sidebar.
 * </p>
 */
public enum DisplaySlot {

    // --- 📋 Slot Definitions ---

    /**
     * **The Player List (Tab)**
     * <p>Displays the score next to player names in the multiplayer tab menu.</p>
     * <p><b>Common Use:</b> Current health or ping.</p>
     */
    LIST,

    /**
     * **The Sidebar**
     * <p>Displays the objective on the right-hand side of the screen.</p>
     * <p><b>Common Use:</b> Match timer, player kills, or border distance.</p>
     */
    SIDEBAR,

    /**
     * **Below Name**
     * <p>Displays the score directly under a player's nameplate in the 3D world.</p>
     * <p><b>Common Use:</b> Visual health indicators (e.g., "20 HP").</p>
     */
    BELOW_NAME;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase NBT-compatible name for the display slot.
     * <p><b>Example:</b> {@code DisplaySlot.BELOW_NAME.getSlotName()} returns {@code "below_name"}.</p>
     * @return The lowercase identifier string used by Minecraft commands and packets.
     */
    public String getSlotName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a DisplaySlot from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup and handles
     * common formatting issues. If the input is null or unrecognized, it
     * defaults to {@link #SIDEBAR} to ensure the UI remains visible.</p>
     * @param input The raw slot name (e.g., "list", "belowName", "SIDEBAR").
     * @return The matching {@link DisplaySlot}, or {@link #SIDEBAR} as a fail-safe.
     */
    public static DisplaySlot fromString(String input) {
        if (input == null || input.isBlank()) {
            return SIDEBAR;
        }

        // Clean the input and handle camelCase or snake_case conversion if necessary
        String target = input.toUpperCase().trim().replace("-", "_");

        try {
            return DisplaySlot.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped slots and fallback to Sidebar
            return SIDEBAR;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase slot name for direct use in command strings.
     * <p><b>Implementation:</b> Delegates to {@link #getSlotName()}.</p>
     * @return The lowercase identifier (e.g., "list").
     */
    @Override
    public String toString() {
        return getSlotName();
    }
}