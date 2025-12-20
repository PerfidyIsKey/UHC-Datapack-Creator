package uhc.resource.entity.mobs.wolf;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🔊 **Wolf Sound Variant Enum**
 * <p>
 * Defines the specific sound sets available for wolves.
 * Uses {@link DatapackConfig#CUSTOM_NAMESPACE} for default ID generation.
 * </p>
 */
public enum WolfSoundVariant {
    CLASSIC,
    ANGRY,
    BIG,
    CUTE,
    GRUMPY,
    PUGLIN,
    SAD;

    /**
     * Returns the full namespaced ID using the project's custom namespace.
     * e.g., "uhc_core_pack:puglin"
     */
    public String getNamespaceId() {
        return getNamespaceId(DatapackConfig.CUSTOM_NAMESPACE);
    }

    /**
     * Returns the full namespaced ID for a specific namespace.
     * @param namespace The namespace string (e.g., "minecraft").
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
     * Safely retrieves a WolfSoundVariant from a string ID.
     * Handles both plain IDs and namespaced IDs efficiently.
     */
    public static WolfSoundVariant fromId(String id) {
        Objects.requireNonNull(id, "Sound variant ID cannot be null.");

        // Optimized substring over split to avoid unnecessary array allocation
        String cleanId = id.contains(":") ? id.substring(id.indexOf(":") + 1) : id;

        try {
            return WolfSoundVariant.valueOf(cleanId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown wolf sound variant: " + id);
        }
    }
}