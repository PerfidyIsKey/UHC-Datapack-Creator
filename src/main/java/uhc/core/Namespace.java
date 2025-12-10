package uhc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * 📁 **Minecraft Resource Namespace**
 * <p>
 * Represents a Minecraft resource namespace (e.g., 'minecraft' or 'uhc_core_pack').
 * This object is a container that organizes all files (components) that belong
 * under its folder structure: {@code <datapack>/data/<namespace>/}.
 * </p>
 */
public class Namespace {

    private final String name;

    /**
     * Map storing all {@code DatapackComponent} objects, grouped and keyed by their category path
     * (e.g., "function", "tags/function").
     * The value is a List because multiple files can belong to the same category
     * (e.g., many {@code .mcfunction} files all fall under the "function" category).
     */
    private final Map<String, List<DatapackComponent>> componentsByCategory = new HashMap<>();

    /**
     * Constructs a new namespace.
     * @param name The name of the namespace (must be lowercase, e.g., "uhc_core_pack").
     * @throws IllegalArgumentException if the provided name is null, empty, or only whitespace.
     */
    public Namespace(String name) {
        // --- Input Validation (Error Catching) ---
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Namespace name cannot be null or empty.");
        }
        // NOTE: Although Minecraft names must be lowercase, the functionality does not change the case here,
        // relying on upstream logic (Datapack.getOrCreateNamespace) to provide the correct format.
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
     * Adds a component (Function, Tag, Advancement, etc.) to this namespace, organizing it by its resource category.
     * <p>
     * The method ensures a {@code List} exists for the component's category before adding the component.
     * </p>
     * @param component The {@code DatapackComponent} to add.
     * @throws IllegalArgumentException if the provided component object is null.
     */
    public void addComponent(DatapackComponent component) {
        // --- Input Validation (Error Catching) ---
        if (component == null) {
            throw new IllegalArgumentException("Cannot add a null DatapackComponent to the namespace '" + this.name + "'.");
        }

        String category = component.getCategory();

        // computeIfAbsent is used to efficiently create the list if the category is new.
        // Functionality maintained: component is added to the list for its category.
        componentsByCategory
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(component);
    }

    /**
     * Retrieves a map of all components grouped by their category folder.
     * This map is primarily used by the {@code Generator} class when writing files to the disk.
     * @return An unmodifiable map of category paths (String) to their list of components (List<DatapackComponent>).
     */
    public Map<String, List<DatapackComponent>> getComponentsByCategory() {
        // Functionality maintained: Wrap the map to prevent external modification, protecting the namespace integrity.
        return Collections.unmodifiableMap(componentsByCategory);
    }
}