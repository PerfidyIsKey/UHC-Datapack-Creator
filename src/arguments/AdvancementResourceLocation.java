package arguments;

import shared.advancement.AdvancementKey;
import shared.advancement.AdvancementFolder;

/**
 * Represents a type-safe Minecraft resource location for an advancement,
 * in the format <namespace>:<folder>/<key>.
 */
public final class AdvancementResourceLocation {
    private static final String DEFAULT_NAMESPACE = "minecraft";

    private final String namespace;
    private final String path; // The folder and key combined

    private AdvancementResourceLocation(String namespace, String path) {
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty.");
        }
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path (folder/key) cannot be null or empty.");
        }
        this.namespace = namespace;
        this.path = path;
    }

    // --- Static Factories for Minecraft Advancements (Convenience) ---

    public static AdvancementResourceLocation of(AdvancementFolder folder, AdvancementKey key) {
        return new AdvancementResourceLocation(
                DEFAULT_NAMESPACE,
                folder.toString() + "/" + key.toString()
        );
    }

    public static AdvancementResourceLocation story(String key) {
        return new AdvancementResourceLocation(DEFAULT_NAMESPACE, AdvancementFolder.STORY + "/" + key);
    }

    public static AdvancementResourceLocation nether(String key) {
        return new AdvancementResourceLocation(DEFAULT_NAMESPACE, AdvancementFolder.NETHER + "/" + key);
    }

    // --- Static Factories for Custom Advancements ---

    /**
     * Creates a custom advancement resource location using a specific namespace and path.
     * @param namespace The custom namespace (e.g., "my_mod").
     * @param path The full path including the folder and key (e.g., "quests/slay_boss").
     */
    public static AdvancementResourceLocation custom(String namespace, String path) {
        return new AdvancementResourceLocation(namespace, path);
    }

    /**
     * Creates a custom advancement resource location using the default 'minecraft' namespace
     * and a fully custom path.
     * @param path The full path including the folder and key (e.g., "custom_folder/my_advancement_id").
     */
    public static AdvancementResourceLocation custom(String path) {
        return new AdvancementResourceLocation(DEFAULT_NAMESPACE, path);
    }


    /**
     * Formats the resource location as a command argument string.
     * @return E.g., "minecraft:story/shiny_gear"
     */
    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}