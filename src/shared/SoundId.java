package shared;

public enum SoundId {
    BASALT("ambient.basalt_deltas.mood"),
    CRIMSON("ambient.crimson_forest.mood"),
    WARPED("ambient.warped_forest.mood"),
    WITHER("entity.wither.spawn"),
    THUNDER("entity.lightning_bolt.thunder")
    ;

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    SoundId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    SoundId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant item ID.
     */
    public String getResourceLocation() {
        return resourceLocation;
    }
}

