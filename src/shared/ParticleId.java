package shared;

/**
 * Defines the types of particle identifiers available, each with a corresponding
 * lowercase name used for data identification.
 */
public enum ParticleId {
    DUST("dust");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructs a ParticleId enum member with its corresponding string value.
     * Note: Enum constants are capitalized (e.g., DUST), but the string value is lowercase (e.g., "dust").
     * @param path The lowercase name used for the particle identifier.
     */
    ParticleId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    ParticleId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    /**
     * Returns the lowercase name of the particle identifier used in data storage or commands.
     */
    @Override
    public String toString() {
        return resourceLocation;
    }
}