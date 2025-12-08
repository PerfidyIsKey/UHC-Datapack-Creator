package uhc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the complete Minecraft Datapack object.
 * This class holds the top-level metadata (pack format, description)
 * and acts as a container for all {@code Namespace} objects within the pack.
 */
public class Datapack {

    /**
     * The resource pack format version number required by Minecraft.
     * This value is used for the "min_format" and "max_format" fields in pack.mcmeta.
     * Current value: 88 (for specific 1.21.9+ versions).
     */
    private static final int PACK_FORMAT = 88;

    private final String description;

    /**
     * Map storing all namespaces (e.g., "minecraft", "uhc_core_pack")
     * within this datapack, keyed by their string name.
     */
    private final Map<String, Namespace> namespaces = new HashMap<>();

    /**
     * Constructs the main Datapack configuration.
     * @param description A brief, user-friendly description displayed in the Minecraft resource list.
     */
    public Datapack(String description) {
        this.description = description;
    }

    /**
     * Retrieves the static pack format version number.
     * @return The format version (e.g., 88).
     */
    public int getPackFormat() {
        return PACK_FORMAT;
    }

    /**
     * Gets the description string for the pack.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves an existing namespace by name, or creates a new one if it does not exist.
     * This is the primary way to interact with the datapack structure.
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
        return namespaces;
    }
}