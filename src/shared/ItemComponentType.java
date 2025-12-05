package shared;

/**
 * Enum for type-safe representation of Minecraft Item Component resource locations.
 * Using an enum prevents string typos when building ItemData.
 */
public enum ItemComponentType {
    PROFILE("profile");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    ItemComponentType(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    ItemComponentType(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant item ID.
     */
    public String getResourceLocation() {
        return resourceLocation;
    }
}