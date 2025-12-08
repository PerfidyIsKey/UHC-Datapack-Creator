package uhc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the complete Datapack, holding all namespaces and metadata.
 */
public class Datapack {

    // Use a single integer for the format number (88)
    private static final int PACK_FORMAT = 88;

    private final String description;
    private final Map<String, Namespace> namespaces = new HashMap<>();

    // Constructor now takes the description string again
    public Datapack(String description) {
        this.description = description;
    }

    public int getPackFormat() {
        return PACK_FORMAT;
    }

    public String getDescription() {
        return description;
    }

    public Namespace getOrCreateNamespace(String name) {
        return namespaces.computeIfAbsent(name, Namespace::new);
    }

    public Map<String, Namespace> getNamespaces() {
        return namespaces;
    }
}