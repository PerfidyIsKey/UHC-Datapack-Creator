package arguments.particle;

import shared.ParticleId;

/**
 * Represents the particle argument in the Minecraft /particle command,
 * which can include optional configuration tags.
 * Format: particle_type_id{configuration tags}
 */
public class ParticleArgument {
    private String particle;

    private ParticleArgument(String particle) {
        this.particle = particle;
    }

    /**
     * Factory method for convenience.
     */
    public static ParticleArgument create(ParticleId id) {
        return new ParticleArgument(id.toString());
    }

    public static ParticleArgument create(ParticleId id, ParticleArgumentBuilder argumentBuilder) {
        return new ParticleArgument(id + argumentBuilder.build());
    }

    @Override
    public String toString() {
        return particle;
    }

}