package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.coordinate.Angle;
import uhc.command.MinecraftCommand;

/**
 * 📍 **SetWorldSpawn Command Builder**
 * <p>
 * Provides a fluent API for the {@code /setworldspawn} command.
 * This sets the default spawn point for new players and the center of the world's
 * loaded area (spawn chunks).
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /setworldspawn [<pos>] [<angle>]}
 * </p>
 */
public class SetWorldSpawnCommand implements MinecraftCommand {
    private BlockPos pos;
    private Angle angle;

    private SetWorldSpawnCommand() {}

    /**
     * Initializes a new SetWorldSpawn builder.
     * @return A new builder instance.
     */
    public static SetWorldSpawnCommand create() {
        return new SetWorldSpawnCommand();
    }

    /**
     * Sets the coordinates for the world spawn.
     * @param pos The target block position.
     * @return The current builder instance.
     */
    public SetWorldSpawnCommand pos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    /**
     * Sets the yaw angle for players spawning in.
     * <p>Note: This argument requires that {@link #pos(BlockPos)} is also set.</p>
     * @param angle The yaw rotation (0-360).
     * @return The current builder instance.
     */
    public SetWorldSpawnCommand angle(Angle angle) {
        this.angle = angle;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return The formatted command (e.g., "setworldspawn ~ ~ ~ 90").
     * @throws IllegalStateException if an angle is provided without a position.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("setworldspawn");

        // Append position if present
        if (pos != null) {
            sb.append(" ").append(pos);

            // Append angle only if position is present (Minecraft requirement)
            if (angle != null) {
                sb.append(" ").append(angle);
            }
        } else if (angle != null) {
            // Error Catching: Minecraft cannot parse an angle without coordinates first.
            throw new IllegalStateException("The 'angle' argument in /setworldspawn requires a 'pos' argument to be specified first.");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}