package uhc.arguments.entity;

/**
 * ⚓ **Entity Anchor**
 * <p>
 * Defines the anchor point for an entity's position. Used primarily in
 * {@code /execute anchored} and {@code /execute facing} commands to
 * determine the vertical reference point.
 * </p>
 */
public enum EntityAnchor {
    /** The position of the entity's eyes (useful for line-of-sight). */
    EYES,

    /** The position of the entity's feet (the base coordinate). */
    FEET;

    /**
     * Safely retrieves an EntityAnchor from a string name.
     * * @param value The string to parse (e.g., "eyes", "FEET").
     * @return The matching {@link EntityAnchor}, or {@link #FEET} as a safe default if invalid.
     */
    public static EntityAnchor fromString(String value) {
        if (value == null) return FEET;
        try {
            return valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Fallback to FEET prevents the command builder from throwing an exception
            return FEET;
        }
    }

    /**
     * Returns the lowercase name required by Minecraft's command syntax.
     * @return "eyes" or "feet".
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}