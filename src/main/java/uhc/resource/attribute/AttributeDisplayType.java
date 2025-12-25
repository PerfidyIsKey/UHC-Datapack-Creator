package uhc.resource.attribute;

import java.util.Objects;

/**
 * 👁️ **Attribute Display Type Registry**
 * <p>
 * Defines the rendering behavior for attribute modifiers within item tooltips.
 * This enum determines whether a modifier (such as Speed, Strength, or Health)
 * is visible to the player or replaced by custom text components.
 * </p>
 */
public enum AttributeDisplayType {

    // --- 🏷️ Display Constants ---

    /**
     * Shows the standard Minecraft stat line.
     * <p>Example: {@code +5 Attack Damage} (Green) or {@code -1.2 Attack Speed} (Red).</p>
     */
    DEFAULT,

    /**
     * The modifier remains functional and active on the entity, but it is
     * completely omitted from the item's tooltip.
     * <p>Useful for "hidden" buffs or clean UI designs.</p>
     */
    HIDDEN,

    /**
     * Suppresses the standard stat line and indicates that the display
     * logic should be handled by a custom TextComponent override.
     */
    OVERRIDE;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase NBT-compatible name for the display type.
     * <p><b>Example:</b> {@code AttributeDisplayType.HIDDEN.getNbtName()} returns {@code "hidden"}.</p>
     * @return The lowercase identifier string.
     */
    public String getNbtName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves an AttributeDisplayType from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. If the input is null,
     * empty, or unrecognized, it defaults to {@link #DEFAULT} to ensure tooltips
     * continue to render correctly.</p>
     * @param input The raw display name (e.g., "hidden", "OVERRIDE").
     * @return The matching {@link AttributeDisplayType}, or {@link #DEFAULT} as a fail-safe.
     */
    public static AttributeDisplayType fromString(String input) {
        if (input == null || input.isBlank()) {
            return DEFAULT;
        }

        String target = input.toUpperCase().trim();
        try {
            return AttributeDisplayType.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped display types and fallback to default visibility
            return DEFAULT;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase NBT name for direct use in NBT compound building or command strings.
     * <p><b>Implementation:</b> Delegates to {@link #getNbtName()}.</p>
     * @return The lowercase identifier (e.g., "override").
     */
    @Override
    public String toString() {
        return getNbtName();
    }
}