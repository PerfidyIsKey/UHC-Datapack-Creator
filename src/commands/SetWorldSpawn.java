package commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.number.Angle;

/**
 * Builds the Minecraft /setworldspawn command.
 * Syntax: setworldspawn [<pos>] [<angle>]
 */
public class SetWorldSpawn {
    private BlockPos pos;
    private Angle angle;

    private SetWorldSpawn() {}

    public static SetWorldSpawn create () {
        return new SetWorldSpawn();
    }

    /**
     * Sets the optional block position argument.
     * @param pos The coordinates of the world spawn.
     * @return The builder instance for chaining.
     */
    public SetWorldSpawn pos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    /**
     * Sets the optional yaw angle argument.
     * @param angle The yaw angle to spawn with.
     * @return The builder instance for chaining.
     */
    public SetWorldSpawn angle(Angle angle) {
        this.angle = angle;
        return this;
    }

    /**
     * Builds the final /setworldspawn command string.
     * @return The complete command string.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("setworldspawn");

        // The 'pos' argument comes first, if present.
        if (pos != null) {
            sb.append(" ").append(pos);
        }

        // The 'angle' argument is optional and comes after 'pos'.
        // NOTE: The 'angle' argument REQUIRES 'pos' to be present if you want to set a specific angle
        // but keep the position default. Since the specification is [<pos>] [<angle>], setting angle
        // without pos means pos defaults to current location, but the angle argument is still
        // syntactically after the (now defaulted) pos. In the command builder, if 'angle' is set,
        // 'pos' MUST be included in the command, even if it's just the default relative position (~ ~ ~).
        // Since 'pos' defaulting to execution location is handled by the game when 'pos' is omitted,
        // we can simplify the builder: if angle is present, the user must have provided a position
        // or accept that the angle won't be applied correctly.
        // For type safety, we'll allow angle to be set only if pos is also set, or if the user
        // explicitly sets a default position (e.g., `pos(BlockPos.relative(0, 0, 0))`).

        if (angle != null) {
            // Error check: Angle cannot be specified without a position argument
            // following the official command structure.
            if (pos == null) {
                throw new IllegalStateException("Cannot specify 'angle' without also specifying 'pos' for /setworldspawn.");
            }
            sb.append(" ").append(angle);
        }

        return sb.toString();
    }
}