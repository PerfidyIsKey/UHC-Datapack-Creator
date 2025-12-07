package uhc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Minecraft resource namespace (e.g., 'minecraft' or 'uhc').
 */
public class Namespace {
    private final String name;
    private final List<DatapackComponent> components = new ArrayList<>();

    public Namespace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds a component (like a Function or LootTable) to this namespace.
     * @param component The component to add.
     */
    public void addComponent(DatapackComponent component) {
        this.components.add(component);
    }

    public List<DatapackComponent> getComponents() {
        return components;
    }
}