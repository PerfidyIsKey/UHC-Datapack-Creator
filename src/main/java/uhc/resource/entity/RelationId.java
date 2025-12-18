package uhc.resource.entity;

/**
 * 🔗 **Entity Relation Identifier**
 * <p>
 * Defines the relationship between entities for the {@code /execute on} command.
 * This allows a command to shift its execution context from one entity to a related one.
 * </p>
 */
public enum RelationId {
    /** The entity that most recently attacked the current entity. */
    ATTACKER,

    /** The entity currently controlling the movement of this entity. */
    CONTROLLER,

    /** The entity holding this entity on a lead. */
    LEASHER,

    /** The entity that shot the projectile (if the current entity is a projectile). */
    ORIGIN,

    /** The owner of the entity (e.g., the player who tamed a wolf). */
    OWNER,

    /** All entities currently riding the current entity. */
    PASSENGERS,

    /** The entity the current entity is currently targeting/attacking. */
    TARGET,

    /** The entity the current entity is currently riding. */
    VEHICLE;

    /**
     * Safely retrieves a RelationId from a string.
     * @param value The string name of the relation (e.g., "attacker", "OWNER").
     * @return The matching {@link RelationId}, or {@link #ORIGIN} as a fallback if invalid.
     */
    public static RelationId fromString(String value) {
        if (value == null) return ORIGIN;
        try {
            return valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Fallback to ORIGIN as a safe default to prevent command syntax failure
            return ORIGIN;
        }
    }

    /**
     * Returns the lowercase name for use in Minecraft command syntax.
     * @return The relation name (e.g., "passengers").
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}