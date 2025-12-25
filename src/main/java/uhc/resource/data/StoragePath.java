package uhc.resource.data;

import uhc.core.DatapackConfig;

/**
 * 🗄️ **Type-Safe Storage Identifiers**
 * <p>
 * Defines all valid namespaced storage locations used in the datapack.
 * Represented in-game as {@code namespace:path}.
 * </p>
 */
public enum StoragePath {

    /** Global game state storage (e.g., timers, game phase) */
    GAME_STATE("state"),

    /** Player-specific persistent data */
    PLAYER_DATA("players"),

    /** Team scores and statistics */
    TEAM_DATA("teams"),

    /** Configuration and toggleable settings */
    SETTINGS("settings"),

    /** Temporary calculation buffer */
    BUFFER("temp");

    private final String path;
    private final String namespace;

    /**
     * Constructor using the vanilla namespace from DatapackConfig.
     * @param path The path within the namespace.
     */
    StoragePath(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = path;
    }

    /**
     * Constructor for specific/vanilla namespaces.
     * @param namespace The namespace (e.g., "minecraft").
     * @param path The path within that namespace.
     */
    StoragePath(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Returns the full namespaced identifier.
     * @return Example: "uhc_core:state"
     */
    public String getIdentifier() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}