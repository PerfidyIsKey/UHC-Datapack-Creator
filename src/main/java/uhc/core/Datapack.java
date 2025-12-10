package uhc.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 📦 **Minecraft Datapack Container**
 * <p>
 * This class represents the complete structure of the Minecraft Datapack. It acts as the
 * top-level container, holding all necessary metadata (sourced from {@code DatapackConfig})
 * and managing all subordinate {@code Namespace} objects.
 * </p>
 */
public class Datapack {

    /**
     * Internal map storing all namespaces (e.g., "minecraft", "uhc_core_pack")
     * associated with this datapack, keyed by their string name.
     */
    private final Map<String, Namespace> namespaces = new HashMap<>();

    /**
     * Constructs the main Datapack configuration container.
     * <p>
     * Note: This constructor is parameterless as all top-level metadata (format and description)
     * is sourced statically from the {@code DatapackConfig} class.
     * </p>
     */
    public Datapack() {
        // Initialization of the map is sufficient.
    }

    /**
     * Retrieves the static pack format version number, as defined in {@code DatapackConfig}.
     * * @return The required pack format version integer (e.g., 88).
     */
    public int getPackFormat() {
        return DatapackConfig.PACK_FORMAT;
    }

    /**
     * Retrieves the user-friendly description string, as defined in {@code DatapackConfig}.
     * * @return The description string for the {@code pack.mcmeta} file.
     */
    public String getDescription() {
        return DatapackConfig.PACK_DESCRIPTION;
    }

    /**
     * Retrieves an existing {@code Namespace} by its name, or atomically creates a new one
     * if a namespace with the given name does not yet exist.
     * * @param name The name of the namespace (must be lowercase, e.g., "minecraft" or "uhc_core_pack").
     * @return The existing or newly created {@code Namespace} object.
     * @throws IllegalArgumentException if the provided name is null or empty.
     */
    public Namespace getOrCreateNamespace(String name) {
        // --- Error Catching ---
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Namespace name cannot be null or empty.");
        }

        // Functionality maintained: uses Java 8's computeIfAbsent for thread-safe map access/creation.
        // Delegates Namespace construction to the Namespace::new constructor.
        return namespaces.computeIfAbsent(name, Namespace::new);
    }

    /**
     * Returns an unmodifiable view of all {@code Namespace} objects contained within this datapack.
     * This prevents external modification of the namespace list.
     * * @return An unmodifiable map where keys are namespace names (Strings) and values are {@code Namespace} objects.
     */
    public Map<String, Namespace> getNamespaces() {
        // Functionality maintained: wraps the internal map to ensure immutability from external calls.
        return Collections.unmodifiableMap(namespaces);
    }
}