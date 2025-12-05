package shared.attributes;

/**
 * Defines valid Modifier IDs (resource locations).
 * These IDs are used to uniquely identify an attribute modifier added to an entity.
 * They must be a resource location for an unregistered content, meaning you can
 * define your own IDs (e.g., 'mymod:special_speed_boost'), but using the standard
 * 'minecraft:' namespace is common for simple custom modifiers.
 */
public enum ModifierId {
    /** An example custom modifier ID for armor. */
    ARMOR_BODY("armor.body"),

    /** Example of a UUID-style ID for temporary modifiers */
    GENERIC_SPEED_BOOST("generic.speed_boost_uuid");
    // Add other common or custom modifier IDs here

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    ModifierId(String path) {
        // Constructs the full resource location, e.g., 'minecraft:armor.body'
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Overrides toString() to return the required resource location for the command string.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}