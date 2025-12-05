package commands;

import arguments.Entity;
import arguments.particle.ParticleArgument;
import arguments.coordinate.Vec3;
import shared.DisplayType;
import shared.ParticleId;

/**
 * Fluent builder for the Minecraft /particle command.
 * Command syntax: /particle <name> [pos] [delta] [speed] [count] [mode] [viewers]
 */
public class Particle {
    // Stores the particle ID and its configuration/argument (e.g., "dust{...}" or "block <state>")
    private final ParticleArgument nameAndConfig;
    private Vec3 pos;
    private Vec3 delta;
    private float speed;
    private int count;
    private DisplayType display;
    private Entity viewers;

    private Particle(ParticleArgument nameAndConfig) {
        this.nameAndConfig = nameAndConfig;
    }

    /**
     * Creates a Particle command builder using a type-safe particle argument.
     * @param nameAndConfig The particle type argument, including optional configuration tags.
     */
    public static Particle create(ParticleArgument nameAndConfig) {
        return new Particle(nameAndConfig);
    }

    /**
     * Convenience method to create a Particle command builder from just a ParticleId (no configuration).
     * @param name The simple particle ID.
     */
    public static Particle create(ParticleId name) {
        return new Particle(ParticleArgument.create(name));
    }


    public Particle pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public Particle delta(Vec3 delta) {
        this.delta = delta;
        return this;
    }

    public Particle speed(float speed) {
        this.speed = speed;
        return this;
    }

    public Particle count(int count) {
        if (count < 0) throw new IllegalArgumentException("Particle count cannot be negative.");
        this.count = count;
        return this;
    }

    public Particle display(DisplayType display) {
        this.display = display;
        return this;
    }

    public Particle viewers(Entity viewers) {
        this.viewers = viewers;
        return this;
    }

    /**
     * Builds the final particle command string.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("particle ");

        // 1. Particle Name Argument (mandatory, includes config tags/external argument)
        // nameAndConfig.toString() handles "dust{...}" or "block <state>"
        sb.append(nameAndConfig);

        // 2. Full arguments only if pos is set (or required by the user).
        if (pos != null) {

            // Pos
            sb.append(" ").append(pos);

            // Delta, Speed, Count (mandatory if pos is provided)
            sb.append(" ").append(delta);
            sb.append(" ").append(speed);
            sb.append(" ").append(count);

            // Display Type (optional)
            if (display != null) {
                sb.append(" ").append(display);

                // Viewers (optional, requires display type)
                if (viewers != null) {
                    sb.append(" ").append(viewers);
                }
            }
        }

        return sb.toString();
    }
}