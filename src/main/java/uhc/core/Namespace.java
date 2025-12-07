package uhc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Minecraft resource namespace (e.g., 'minecraft' or 'uhc_core_pack').
 */
public class Namespace {
    private final String name;

    // Map to hold components organized by their category (e.g., "function", "tags/function")
    private final Map<String, List<DatapackComponent>> componentsByCategory = new HashMap<>();

    public Namespace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds a component (Function, LootTable, etc.) to this namespace.
     * The component's path is used to determine its category.
     * @param component The component to add.
     */
    public void addComponent(DatapackComponent component) {
        // Use the first segment of the path (e.g., "function", or "tags/function")
        String category = component.getCategory();

        componentsByCategory
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(component);
    }

    public Map<String, List<DatapackComponent>> getComponentsByCategory() {
        return componentsByCategory;
    }
}