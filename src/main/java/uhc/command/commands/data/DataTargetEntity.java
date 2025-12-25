package uhc.command.commands.data;

import uhc.arguments.entity.Entity;
import java.util.Objects;

/**
 * 👤 **Entity Data Target**
 * <p>
 * This class implements the {@link DataTarget} interface specifically for Minecraft
 * entities (mobs, players, and non-living entities like armor stands).
 * </p>
 * <p>
 * It acts as a wrapper for the {@link Entity} class, ensuring that when used in
 * a {@code /data} command, the required {@code entity} prefix is correctly applied
 * to the selector or UUID.
 * </p>
 */
public class DataTargetEntity implements DataTarget {

    // --- ⚙️ State & Fields ---

    /** * The underlying {@link Entity} object representing the target(s).
     * This holds the rich selector data (e.g., @e[type=zombie,limit=1]).
     */
    private final Entity target;

    // --- 🏗️ Constructor & Factory ---

    /**
     * Private constructor to enforce controlled instantiation via factory methods.
     * <p><b>Error Catching:</b> Utilizes {@link Objects#requireNonNull} to prevent
     * the creation of a target that would result in a null command fragment.</p>
     * * @param target The rich {@link Entity} selector object.
     * @throws NullPointerException if the target entity is null.
     */
    private DataTargetEntity(Entity target) {
        this.target = Objects.requireNonNull(target, "Entity target cannot be null for DataTargetEntity.");
    }

    /**
     * Static factory method to create a new Entity Data Target.
     * * @param target The {@link Entity} object to wrap.
     * @return A new validated instance of {@link DataTargetEntity}.
     * @throws NullPointerException if target is null.
     */
    public static DataTargetEntity create(Entity target) {
        return new DataTargetEntity(target);
    }

    // --- 🛰️ DataTarget Implementation ---

    /**
     * Retrieves the raw string representation of the entity selector.
     * <p><b>Logic:</b> Delegates the string conversion to the {@link Entity} object
     * to maintain consistency with the entity system's formatting rules.</p>
     * * @return The raw selector string (e.g., "@s" or "0-0-0-0-1").
     * @throws IllegalStateException if the underlying entity object produces a null/empty string.
     */
    @Override
    public String getTarget() {
        String selector = target.toString();
        if (selector == null || selector.trim().isEmpty()) {
            throw new IllegalStateException("The wrapped Entity object returned an invalid null or empty selector string.");
        }
        return selector;
    }

    /**
     * Builds the complete entity target segment for use in a {@code /data} command.
     * <p><b>Format:</b> {@code entity <selector>}</p>
     * <p><b>Error Catching:</b> Validates the selector before prepending the
     * "entity" keyword to ensure command syntax integrity.</p>
     * * @return The formatted command fragment (e.g., "entity @e[limit=1]").
     */
    @Override
    public String toString() {
        // Validation check before building the string
        String rawTarget = getTarget();
        return "entity " + rawTarget;
    }

    // --- 🔍 Metadata ---

    /**
     * Accessor for the internal Entity object.
     * <p>Useful for logic that needs to inspect selector arguments rather than
     * just the string representation.</p>
     * * @return The immutable {@link Entity} object.
     */
    public Entity getEntity() {
        return target;
    }
}