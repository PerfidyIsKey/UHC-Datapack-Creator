package uhc.score;

import uhc.core.DatapackConfig;
import java.util.Locale;

/**
 * 📈 **Scoreboard Custom Statistics Registry**
 */
public enum CustomStatistics {

    // --- 📋 Built-in Standard Statistics ---

    DAMAGE_TAKEN,
    DAMAGE_DEALT,
    JUMP,
    DEATHS,
    PLAYER_KILLS,
    TIME_SINCE_DEATH,
    PLAY_TIME,
    SNEAK_TIME,

    /** Example of an enum entry with an explicit custom namespace override */
    UHC_WINS(DatapackConfig.CUSTOM_NAMESPACE, "wins");

    private final String namespace;
    private final String path;

    /**
     * Standard Constructor: Uses the default DatapackConfig namespace
     * and the enum name as the path.
     */
    CustomStatistics() {
        this(null, null);
    }

    /**
     * Overloaded Constructor: Allows for a specific namespace and path.
     * @param namespace The custom namespace (e.g., "minecraft").
     * @param path The specific statistic path.
     */
    CustomStatistics(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * @return The namespace of the statistic. Defaults to config if not overridden.
     */
    public String getNamespace() {
        return (namespace != null) ? namespace : DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * @return The specific path of the statistic.
     */
    public String getPath() {
        return (path != null) ? path : this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return The full resource location (e.g., "minecraft:damage_taken").
     */
    public String getResourceLocation() {
        return getNamespace() + ":" + getPath();
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}