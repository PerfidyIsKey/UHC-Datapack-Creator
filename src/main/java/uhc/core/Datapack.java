package uhc.core;

import java.util.HashMap;
import java.util.Map;
// No need to import DatapackConfig if it's in the same package, but good practice if structure changes

/**
 * Represents the complete Minecraft Datapack object.
 * This class holds the top-level metadata and acts as a container for all {@code Namespace} objects.
 */
public class Datapack {

    // PACK_FORMAT is now sourced from DatapackConfig
    // private static final int PACK_FORMAT = 88; // REMOVED

    // description is now sourced from DatapackConfig
    // private final String description; // REMOVED

    /**
     * Map storing all namespaces (e.g., "minecraft", "uhc_core_pack")
     * within this datapack, keyed by their string name.
     */
    private final Map<String, Namespace> namespaces = new HashMap<>();

    /**
     * Constructs the main Datapack configuration. No arguments needed as metadata is static.
     */
    public Datapack() {
        // Constructor is now parameterless
    }

    /**
     * Retrieves the static pack format version number from DatapackConfig.
     * @return The format version (e.g., 88).
     */
    public int getPackFormat() {
        return DatapackConfig.PACK_FORMAT;
    }

    /**
     * Gets the description string from DatapackConfig.
     * @return The description.
     */
    public String getDescription() {
        return DatapackConfig.PACK_DESCRIPTION;
    }

    /**
     * Retrieves an existing namespace by name, or creates a new one if it does not exist.
     * @param name The name of the namespace (e.g., "minecraft" or "uhc_core_pack").
     * @return The existing or newly created Namespace object.
     */
    public Namespace getOrCreateNamespace(String name) {
        return namespaces.computeIfAbsent(name, Namespace::new);
    }

    /**
     * Returns an unmodifiable map of all namespaces in the datapack.
     * @return The map of namespaces.
     */
    public Map<String, Namespace> getNamespaces() {
        // ... (implementation remains the same)
        return java.util.Collections.unmodifiableMap(namespaces);
    }
}