package commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.coordinate.Vec3;

public class Teleport {
    // We use objects to track which argument is set. Only one of 'destination' or 'location' can be set.
    private Entity destination;
    private Entity targets;
    private Vec3 location;

    private Teleport() {}

    public static Teleport create() {
        return new Teleport();
    }

    /**
     * Sets the entity destination to teleport to (Syntax 1 & 2).
     * This mode disables location teleportation.
     * @param destination The entity to teleport to and match rotation with.
     * @throws IllegalStateException if a location has already been set.
     */
    public Teleport destination(Entity destination) {
        if (this.location != null) {
            throw new IllegalStateException("Cannot set both a 'destination' entity and a 'location' coordinate.");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination entity cannot be null.");
        }
        this.destination = destination;
        return this;
    }

    /**
     * Sets the coordinates to teleport to (Syntax 3 & 4).
     * This mode disables entity destination teleportation.
     * @param location The coordinates to teleport the targets/executor to.
     * @throws IllegalStateException if a destination entity has already been set.
     */
    public Teleport location(Vec3 location) {
        if (this.destination != null) {
            throw new IllegalStateException("Cannot set both a 'location' coordinate and a 'destination' entity.");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location coordinates cannot be null.");
        }
        this.location = location;
        return this;
    }

    /**
     * Specifies the entity (or entities) to be teleported (Optional).
     * If not set, the command executor is teleported.
     */
    public Teleport targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    /**
     * Builds the final /teleport (or /tp) command string.
     * @throws IllegalStateException if neither a destination entity nor a location coordinate is set.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("tp ");

        // Safety check: Must specify *something* to teleport to.
        if (destination == null && location == null) {
            throw new IllegalStateException("Teleport command requires either a 'destination' entity or a 'location' coordinate.");
        }

        // --- Determine Syntax based on set arguments ---

        // Case 1 & 2: Teleport to Entity Destination
        if (destination != null) {
            if (targets != null) {
                // tp <targets> <destination>
                sb.append(targets).append(" ").append(destination);
            } else {
                // tp <destination>
                sb.append(destination);
            }
        }

        // Case 3 & 4: Teleport to Location Coordinates
        else if (location != null) {
            if (targets != null) {
                // tp <targets> <location>
                sb.append(targets).append(" ").append(location);
            } else {
                // tp <location>
                sb.append(location);
            }
        }

        return sb.toString();
    }
}