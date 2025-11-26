package shared.attributes;

/**
 * Defines valid attribute IDs (resource locations).
 */
public enum AttributeId {
    ARMOR("armor"),
    ATTACK_DAMAGE("attack_damage"),
    MAX_HEALTH("max_health"),
    SCALE("scale"),
    WAYPOINT_TRANSMIT_RANGE("waypoint_transmit_range");
    // ... add all other attributes as needed

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    AttributeId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public String toString() {
        return resourceLocation;
    }
}