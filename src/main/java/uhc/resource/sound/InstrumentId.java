package uhc.resource.sound;

import uhc.core.DatapackConfig;

/**
 * 🎺 **Instrument Registry ID Mapper**
 * <p>
 * This enum maps standard Minecraft goat horn instruments to their
 * respective registry resource locations.
 * </p>
 */
public enum InstrumentId {
    PONDER_GOAT_HORN,
    SING_GOAT_HORN,
    SEEK_GOAT_HORN,
    FEEL_GOAT_HORN,
    ADMIRE_GOAT_HORN,
    CALL_GOAT_HORN,
    YEARN_GOAT_HORN,
    DREAM_GOAT_HORN;

    private final String path;
    private final String namespace;

    /**
     * Default constructor using the standard Minecraft namespace.
     */
    InstrumentId() {
        this.path = name().toLowerCase();
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Constructor for custom paths or namespaces.
     * @param namespace The namespace (e.g., "minecraft" or "my_datapack")
     * @param path The specific registry path.
     */
    InstrumentId(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Returns the full namespaced ID used in NBT.
     * @return String like "minecraft:ponder_goat_horn"
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}