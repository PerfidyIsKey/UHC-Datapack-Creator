package uhc.command.commands.item;

import uhc.arguments.entity.Entity;

/**
 * 👤 **Item Target: Entity**
 * <p>
 * Implements the {@code ItemTarget} interface specifically for targeting one or more entities
 * (players, mobs, etc.) in the {@code /item} command.
 * </p>
 * <p>
 * This class generates the command segment: {@code entity <targets>}
 * </p>
 */
public class ItemTargetEntity implements ItemTarget {

    /** The entity selector or name specifying the target(s) for the item operation. */
    private final Entity targets;

    /**
     * Private constructor to enforce use of the static factory method {@code create()}.
     * @param targets The internal {@code Entity} argument representing the target(s).
     */
    private ItemTargetEntity(Entity targets) {
        this.targets = targets;
    }

    /**
     * Factory method to create a new {@code ItemTargetEntity} instance.
     * @param targets The {@code Entity} argument defining the target selector (e.g., {@code @s}, {@code @a[tag=...]}, etc.).
     * @return A new instance of {@code ItemTargetEntity}.
     * @throws IllegalArgumentException if the provided {@code targets} object is null.
     */
    public static ItemTargetEntity create(Entity targets) {
        if (targets == null) {
            // CRITICAL ERROR CATCH: Ensure the Entity argument is not null.
            throw new IllegalArgumentException("The targets argument (Entity) cannot be null for ItemTargetEntity.");
        }
        return new ItemTargetEntity(targets);
    }

    /**
     * Generates the command segment required by the {@code /item} command for entity targets.
     * @return The command string segment: {@code entity <targets>}
     */
    @Override
    public String getCommandString() {
        // The actual arguments.Entity class is responsible for producing the raw selector string via its toString() method.
        return "entity " + targets.toString();
    }
}