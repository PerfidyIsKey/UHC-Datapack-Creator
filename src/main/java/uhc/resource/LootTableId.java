package uhc.resource;

/**
 * Defines item IDs (resource locations) used in NBT data,
 * automatically applying the 'minecraft' namespace by default.
 */
public enum LootTableId {
    SUPPLY_DROP("supply_drop", "uhc");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    LootTableId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    LootTableId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant item ID.
     */
    public String getResourceLocation() {
        return resourceLocation;
    }
}