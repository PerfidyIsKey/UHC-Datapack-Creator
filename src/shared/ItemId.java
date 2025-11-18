package shared;

/**
 * Defines item IDs (resource locations) used in NBT data,
 * automatically applying the 'minecraft' namespace by default.
 */
public enum ItemId {
    STONE("stone"),
    MUSIC_DISC_STAL("music_disc_stal"),
    DIAMOND_SWORD("diamond_sword");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    ItemId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    ItemId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant item ID.
     */
    public String getResourceLocation() {
        return resourceLocation;
    }
}