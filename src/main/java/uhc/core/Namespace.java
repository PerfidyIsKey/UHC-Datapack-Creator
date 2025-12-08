package uhc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections; // Added for returning an unmodifiable map

/**
 * Represents a Minecraft resource namespace (e.g., 'minecraft' or 'uhc_core_pack').
 * <p>
 * A namespace corresponds to a top-level folder inside the data directory:
 * {@code <datapack>/data/<namespace>/}.
 */
public class Namespace {
    private final String name;

    /**
     * Map storing all components grouped by their category path (e.g., "function", "tags/function").
     * The value is a List because multiple files can belong to the same category (e.g., many .mcfunction files).
     */
    private final Map<String, List<DatapackComponent>> componentsByCategory = new HashMap<>();

    /**
     * Constructs a new namespace.
     * @param name The name of the namespace (e.g., "uhc_core_pack").
     */
    public Namespace(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the namespace.
     * @return The namespace name string.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a component (Function, LootTable, etc.) to this namespace, organizing it by category.
     * <p>
     * The method ensures a List exists for the component's category before adding the component.
     * @param component The DatapackComponent to add.
     */
    public void addComponent(DatapackComponent component) {
        String category = component.getCategory();

        // computeIfAbsent is used to efficiently create the list if the category is new.
        componentsByCategory
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(component);
    }

    /**
     * Retrieves a map of all components grouped by their category folder.
     * This map is primarily used by the {@code Generator} class.
     * @return An unmodifiable map of category paths to their list of components.
     */
    public Map<String, List<DatapackComponent>> getComponentsByCategory() {
        // Wrap the map to prevent external modification, protecting the namespace integrity.
        return Collections.unmodifiableMap(componentsByCategory);
    }
}