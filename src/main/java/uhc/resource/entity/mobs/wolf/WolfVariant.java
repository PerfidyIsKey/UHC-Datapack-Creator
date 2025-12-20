package uhc.resource.entity.mobs.wolf;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🐺 **Wolf Variant Enum**
 * <p>
 * Defines the different breeds of wolves.
 * Uses {@link DatapackConfig#CUSTOM_NAMESPACE} as the default for ID generation.
 * </p>
 */
public enum WolfVariant {
    PALE, ASHEN, BLACK, CHESTNUT, RUSTY, SNOWY, SPOTTED, STRIPED, WOODS;

    /**
     * Returns the ID with the default custom namespace from configuration.
     * e.g., "uhc_core_pack:ashen"
     */
    public String getNamespaceId() {
        return getNamespaceId(DatapackConfig.CUSTOM_NAMESPACE);
    }

    /**
     * Returns the ID with a specific custom namespace.
     * @param namespace The namespace to use (e.g., DatapackConfig.MINECRAFT_NAMESPACE)
     */
    public String getNamespaceId(String namespace) {
        String ns = (namespace == null || namespace.isEmpty()) ? DatapackConfig.MINECRAFT_NAMESPACE : namespace;
        return ns + ":" + name().toLowerCase();
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Safely retrieves a WolfVariant from a string ID, stripping any namespace.
     */
    public static WolfVariant fromId(String id) {
        Objects.requireNonNull(id, "Variant ID cannot be null.");
        String cleanId = id.contains(":") ? id.substring(id.indexOf(":") + 1) : id;
        try {
            return WolfVariant.valueOf(cleanId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown wolf variant: " + id);
        }
    }
}