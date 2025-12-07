package uhc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the complete Datapack, holding all namespaces and metadata.
 */
public class Datapack {
    // Current pack format for Minecraft 1.21.10 is 48.
    // This makes it easy to update for newer versions.
    private static final int PACK_FORMAT = 48;

    private final String description;
    private final Map<String, Namespace> namespaces = new HashMap<>();

    public Datapack(String description) {
        this.description = description;
    }

    public int getPackFormat() {
        return PACK_FORMAT;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Adds or retrieves a Namespace.
     * @param name The name of the namespace (e.g., "uhc").
     * @return The existing or newly created Namespace object.
     */
    public Namespace getOrCreateNamespace(String name) {
        return namespaces.computeIfAbsent(name, Namespace::new);
    }

    public Map<String, Namespace> getNamespaces() {
        return namespaces;
    }
}