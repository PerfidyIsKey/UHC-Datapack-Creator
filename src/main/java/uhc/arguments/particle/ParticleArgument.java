package uhc.arguments.particle;

import uhc.resource.particle.ParticleId;
import java.util.Objects;

/**
 * ✨ **Particle Argument**
 * <p>
 * Represents the particle identifier and its optional configuration metadata
 * for the Minecraft {@code /particle} command.
 * </p>
 * <p>
 * <b>Format:</b> {@code <particle_id>{<configuration_tags>}}
 * <br>
 * Examples: {@code minecraft:end_rod}, {@code minecraft:dust{color:[1,0,0],scale:2}}
 * </p>
 */
public class ParticleArgument {

    // --- ⚙️ State & Fields ---

    /** * The serialized string representing the particle and its parameters.
     * This is the final result sent to the command builder.
     */
    private final String particle;

    // --- 🏗️ Constructor & Factories ---

    /**
     * Private constructor to enforce controlled instantiation via factory methods.
     * <p><b>Error Catching:</b> Ensures the particle string is not null or empty
     * to prevent executing a broken command.</p>
     * * @param particle The fully formatted particle string.
     * @throws NullPointerException if the particle string is null.
     */
    private ParticleArgument(String particle) {
        this.particle = Objects.requireNonNull(particle, "Particle string cannot be null.");

        if (this.particle.trim().isEmpty()) {
            throw new IllegalArgumentException("Particle argument cannot be an empty string.");
        }
    }

    /**
     * Factory method for a simple particle without additional data.
     * <p><b>Logic:</b> Utilizes the {@link ParticleId}'s internal resource location.</p>
     * * @param id The type-safe {@link ParticleId} (e.g., FLAME).
     * @return A new {@link ParticleArgument} instance.
     * @throws NullPointerException if the ParticleId is null.
     */
    public static ParticleArgument create(ParticleId id) {
        Objects.requireNonNull(id, "ParticleId cannot be null.");
        return new ParticleArgument(id.getResourceLocation());
    }

    /**
     * Factory method for complex particles with configuration tags.
     * <p><b>Logic:</b> Combines the base ID with specific parameters like color,
     * scale, or block type via the builder.</p>
     * * @param id              The type-safe {@link ParticleId}.
     * @param argumentBuilder The builder containing the particle's configuration.
     * @return A new {@link ParticleArgument} instance.
     * @throws NullPointerException if the ParticleId or argumentBuilder is null.
     */
    public static ParticleArgument create(ParticleId id, ParticleArgumentBuilder argumentBuilder) {
        Objects.requireNonNull(id, "ParticleId cannot be null.");
        Objects.requireNonNull(argumentBuilder, "ParticleArgumentBuilder cannot be null.");

        return new ParticleArgument(id.getResourceLocation() + argumentBuilder.build());
    }

    // --- 🛰️ Serialization ---

    /**
     * Returns the finalized particle string for command generation.
     * * @return The formatted particle identifier (e.g., "minecraft:dust{color:[1.0,0.0,0.0],scale:1.0}").
     */
    @Override
    public String toString() {
        return particle;
    }

    /**
     * Accessor for the raw particle data string.
     * * @return The internal string representation.
     */
    public String getParticle() {
        return particle;
    }
}