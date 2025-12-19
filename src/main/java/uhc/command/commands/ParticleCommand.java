package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.particle.ParticleArgument;
import uhc.arguments.coordinate.Vec3;
import uhc.command.MinecraftCommand;
import uhc.game.DisplayType;
import uhc.resource.ParticleId;

import java.util.Objects;

/**
 * ✨ **Particle Command Builder**
 * <p>
 * Provides a fluent API for the {@code /particle} command.
 * This command displays visual effects at specific locations.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /particle <name> [<pos>] [<delta>] [<speed>] [<count>] [<force|normal>] [<viewers>]}
 * </p>
 */
public class ParticleCommand implements MinecraftCommand {
    private final ParticleArgument nameAndConfig;
    private Vec3 pos;
    private Vec3 delta;
    private float speed;
    private int count;
    private DisplayType display;
    private Entity viewers;

    private ParticleCommand(ParticleArgument nameAndConfig) {
        this.nameAndConfig = Objects.requireNonNull(nameAndConfig, "Particle argument cannot be null.");
    }

    /**
     * Creates a Particle command builder using a type-safe particle argument.
     * Use this for complex particles like {@code dust}, {@code block}, or {@code item}.
     */
    public static ParticleCommand create(ParticleArgument nameAndConfig) {
        return new ParticleCommand(nameAndConfig);
    }

    /**
     * Creates a Particle command builder from a simple ParticleId.
     */
    public static ParticleCommand create(ParticleId name) {
        return new ParticleCommand(ParticleArgument.create(name));
    }

    /** Sets the world position to spawn the particle. */
    public ParticleCommand pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    /** * Sets the spread or "delta" of the particles on the X, Y, and Z axes.
     * For some particles, these values control RGB color or motion.
     */
    public ParticleCommand delta(Vec3 delta) {
        this.delta = delta;
        return this;
    }

    /** Sets the particle speed/motion multiplier. */
    public ParticleCommand speed(float speed) {
        this.speed = speed;
        return this;
    }

    /** Sets the number of particle particles to spawn. */
    public ParticleCommand count(int count) {
        if (count < 0) throw new IllegalArgumentException("Particle count cannot be negative.");
        this.count = count;
        return this;
    }

    /** Sets the display mode (force or normal). */
    public ParticleCommand display(DisplayType display) {
        this.display = display;
        return this;
    }

    /** Restricts the visibility of particles to specific entities. */
    public ParticleCommand viewers(Entity viewers) {
        this.viewers = viewers;
        return this;
    }

    /**
     * Generates the final command string.
     * <p>Note: Minecraft requires all preceding arguments if a later one is provided.
     * E.g., if 'count' is provided, 'pos', 'delta', and 'speed' must also be present.</p>
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("particle ");

        // 1. Mandatory Name/Config
        sb.append(nameAndConfig);

        // 2. Positional logic: If pos is present, we must append subsequent arguments
        // through 'count' to maintain valid command syntax.
        if (pos != null) {
            sb.append(" ").append(pos);

            // Minecraft requires delta, speed, and count if pos is provided.
            // We use default values (0 0 0 0 0) if they aren't explicitly set.
            sb.append(" ").append(delta != null ? delta : "0 0 0");
            sb.append(" ").append(speed);
            sb.append(" ").append(count);

            // 3. Display Type (Optional, but required if viewers is set)
            if (display != null) {
                sb.append(" ").append(display);

                if (viewers != null) {
                    sb.append(" ").append(viewers);
                }
            } else if (viewers != null) {
                // Error Catching: Minecraft cannot parse viewers without a display mode.
                throw new IllegalStateException("DisplayType (force/normal) must be set to specify viewers.");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}