package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.particle.ParticleArgument;
import uhc.arguments.coordinate.Vec3;
import uhc.command.MinecraftCommand;
import uhc.resource.particle.ParticleId;

import java.util.Objects;

/**
 * ✨ **Particle Command Builder**
 * <p>
 * Provides a fluent API for constructing the {@code /particle} command.
 * This builder manages the complex positional requirements of the command syntax.
 * </p>
 */
public class ParticleCommand implements MinecraftCommand {

    // --- 🏷️ Nested Types ---

    /**
     * 👁️ **Particle Display Mode**
     * <p>
     * Defines the visibility behavior for particles.
     * Uses the enum constant name directly as the command argument.
     * </p>
     */
    public enum DisplayType {
        /** Forces visibility regardless of client settings. Maps to {@code "force"}. */
        FORCE,

        /** Respects client-side particle settings. Maps to {@code "normal"}. */
        NORMAL;

        /**
         * Safely parses a display type from a string.
         * @param input The raw input string.
         * @return The matching {@link DisplayType}, or {@link #NORMAL} as fallback.
         */
        public static DisplayType fromString(String input) {
            if (input == null || input.isBlank()) return NORMAL;
            try {
                return DisplayType.valueOf(input.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                return NORMAL;
            }
        }

        /**
         * Returns the lowercase name of the constant for Minecraft command compatibility.
         * @return The lowercase identifier (e.g., "force").
         */
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    // --- ⚙️ State & Fields ---

    /** * The particle identifier and its extra configuration. */
    private final ParticleArgument nameAndConfig;

    /** * The world location where the particle should spawn. */
    private Vec3 pos;

    /** * The size of the particle cloud on X, Y, and Z axes. */
    private Vec3 delta;

    /** * The speed or motion intensity of the particles. */
    private float speed = 0.0f;

    /** * The number of individual particles to generate. */
    private int count = 0;

    /** * The visibility mode (force or normal). */
    private DisplayType display;

    /** * The selector restricting who can see these particles. */
    private Entity viewers;

    // --- 🏗️ Constructors & Factories ---

    private ParticleCommand(ParticleArgument nameAndConfig) {
        this.nameAndConfig = Objects.requireNonNull(nameAndConfig, "Particle argument cannot be null.");
    }

    public static ParticleCommand create(ParticleArgument nameAndConfig) {
        return new ParticleCommand(nameAndConfig);
    }

    public static ParticleCommand create(ParticleId name) {
        return new ParticleCommand(ParticleArgument.create(name));
    }

    // --- 🛰️ Builder Methods ---

    public ParticleCommand pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public ParticleCommand delta(Vec3 delta) {
        this.delta = delta;
        return this;
    }

    public ParticleCommand speed(float speed) {
        this.speed = speed;
        return this;
    }

    public ParticleCommand count(int count) {
        if (count < 0) throw new IllegalArgumentException("Particle count cannot be negative.");
        this.count = count;
        return this;
    }

    public ParticleCommand display(DisplayType display) {
        this.display = display;
        return this;
    }

    public ParticleCommand viewers(Entity viewers) {
        this.viewers = viewers;
        return this;
    }

    // --- 🛡️ Generation & Logic ---

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("particle ");

        // 1. Particle Name/Configuration
        sb.append(nameAndConfig);

        // 2. Positional Logic
        // If any argument beyond the name is provided, we must fill the chain.
        if (pos != null || delta != null || speed != 0 || count != 0 || display != null) {
            sb.append(" ").append(pos != null ? pos : "0 0 0");
            sb.append(" ").append(delta != null ? delta : "0 0 0");
            sb.append(" ").append(speed);
            sb.append(" ").append(count);

            // 3. Display Type & Viewers logic
            if (display != null) {
                sb.append(" ").append(display); // Calls toString() -> name().toLowerCase()
                if (viewers != null) {
                    sb.append(" ").append(viewers);
                }
            } else if (viewers != null) {
                throw new IllegalStateException("DisplayType must be defined if a 'viewers' selector is provided.");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}