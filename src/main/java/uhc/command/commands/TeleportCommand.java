package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.coordinate.Vec3;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🌀 **Teleport Command Builder**
 * <p>
 * Provides a fluent API for the {@code /tp} (teleport) command.
 * This builder supports four primary syntaxes:
 * <ul>
 * <li>{@code /tp <destination>} - Executor to entity.</li>
 * <li>{@code /tp <targets> <destination>} - Entities to entity.</li>
 * <li>{@code /tp <location>} - Executor to coordinates.</li>
 * <li>{@code /tp <targets> <location>} - Entities to coordinates.</li>
 * </ul>
 * </p>
 */
public class TeleportCommand implements MinecraftCommand {
    private Entity destination;
    private Entity targets;
    private Vec3 location;

    private TeleportCommand() {}

    /**
     * Initializes a new TeleportCommand builder.
     * @return A new builder instance.
     */
    public static TeleportCommand create() {
        return new TeleportCommand();
    }

    /**
     * Sets the entity destination to teleport to.
     * <p>This mode is mutually exclusive with {@link #location(Vec3)}.</p>
     * @param destination The entity to move towards.
     * @throws IllegalStateException if a coordinate location is already set.
     */
    public TeleportCommand destination(Entity destination) {
        if (this.location != null) {
            throw new IllegalStateException("Ambiguous teleport: Cannot set both 'destination' entity and 'location' coordinates.");
        }
        this.destination = Objects.requireNonNull(destination, "Destination entity cannot be null.");
        return this;
    }

    /**
     * Sets the coordinate location to teleport to.
     * <p>This mode is mutually exclusive with {@link #destination(Entity)}.</p>
     * @param location The Vec3 coordinates (absolute or relative).
     * @throws IllegalStateException if a destination entity is already set.
     */
    public TeleportCommand location(Vec3 location) {
        if (this.destination != null) {
            throw new IllegalStateException("Ambiguous teleport: Cannot set both 'location' coordinates and 'destination' entity.");
        }
        this.location = Objects.requireNonNull(location, "Location coordinates cannot be null.");
        return this;
    }

    /**
     * Specifies the entity or entities to be teleported.
     * <p>If omitted, the command executor (e.g., the console or the player running the function) is moved.</p>
     * @param targets The target selector (e.g., @a, @s, or a specific player).
     * @return The current builder instance.
     */
    public TeleportCommand targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return The formatted command (e.g., "tp @a 100 64 100" or "tp player1 player2").
     * @throws IllegalStateException if neither a destination nor a location has been provided.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("tp ");

        // Validation: Minecraft requires a destination of some kind.
        if (destination == null && location == null) {
            throw new IllegalStateException("Teleportation failed: No destination entity or coordinate location was provided.");
        }

        // --- Logic Branching for Syntax Construction ---

        if (destination != null) {
            // Syntax: tp [targets] <destination>
            if (targets != null) {
                sb.append(targets).append(" ");
            }
            sb.append(destination);
        } else {
            // Syntax: tp [targets] <location>
            // Note: 'else' is safe here because the null-check above ensures location isn't null if destination is.
            if (targets != null) {
                sb.append(targets).append(" ");
            }
            sb.append(location);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}