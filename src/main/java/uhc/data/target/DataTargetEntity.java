package uhc.data.target;

import uhc.arguments.entity.Entity;

/**
 * Implements the DataTarget interface for entities.
 * This class builds the 'entity <selector>' command fragment.
 */
public class DataTargetEntity implements DataTarget {
    private final Entity target;

    /**
     * Private constructor. Use the static 'create' factory method.
     * @param target The Entity object representing the target selector (e.g., @s).
     */
    private DataTargetEntity(Entity target) {
        if (target == null) {
            throw new IllegalArgumentException("Entity target cannot be null.");
        }
        this.target = target;
    }

    /**
     * Factory method to create a DataTargetEntity instance.
     * @param target The Entity object.
     * @return A new DataTargetEntity instance.
     */
    public static DataTargetEntity create(Entity target) {
        return new DataTargetEntity(target);
    }

    /**
     * Retrieves the raw entity selector string.
     * NOTE: This implementation assumes the Entity class returns the raw selector (e.g., "@s").
     * @return The raw entity selector string.
     */
    @Override
    public String getTarget() {
        return target.toString();
    }

    /**
     * Builds the complete entity target command fragment.
     * @return The command fragment formatted as "entity <selector>".
     * Example: "entity @s"
     */
    @Override
    public String toString() {
        // Correctly prepends the required 'entity' type to the selector.
        return "entity " + target.toString();
    }
}