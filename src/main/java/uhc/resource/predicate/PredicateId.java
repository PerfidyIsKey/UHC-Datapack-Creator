package uhc.resource.predicate;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🔍 **Predicate Identifier**
 * <p>
 * Represents a resource location for a custom condition (predicate) stored in a datapack.
 * Used in {@code /execute if predicate <id>}.
 * </p>
 */
public enum PredicateId {
    /** Example: A custom check to see if the UHC match has started. */
    IS_MATCH_ACTIVE("is_match_active", DatapackConfig.CUSTOM_NAMESPACE),

    /** Example: Checks if the player is currently within the shrinking world border. */
    INSIDE_BORDER("inside_border", DatapackConfig.CUSTOM_NAMESPACE),

    /** Example: Checks if the weather is currently a thunderstorm. */
    IS_THUNDERING("is_thundering", DatapackConfig.MINECRAFT_NAMESPACE);

    private final String path;
    private final String namespace;

    /**
     * Constructor for predicates within the default Minecraft namespace.
     */
    PredicateId(String path) {
        this(path, DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * Constructor for predicates within specific namespaces.
     */
    PredicateId(String path, String namespace) {
        this.path = Objects.requireNonNull(path, "Predicate path cannot be null.");
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.");
    }

    /**
     * Formats the predicate as a resource location.
     * @return {@code namespace:path}
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}