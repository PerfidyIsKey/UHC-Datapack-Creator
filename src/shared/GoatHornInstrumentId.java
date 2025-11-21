package shared;

/**
 * Defines the resource locations for goat horn instrument IDs.
 */
public enum GoatHornInstrumentId {
    PONDER_GOAT_HORN("ponder_goat_horn");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    GoatHornInstrumentId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }
}