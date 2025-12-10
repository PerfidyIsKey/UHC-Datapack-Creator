package uhc.arguments.itemstack.components;

import uhc.arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'unbreakable' component.
 * Format: unbreakable={}
 */
public class UnbreakableComponent implements ItemComponentTag {

    private UnbreakableComponent() {
        // Enforce singleton-like creation via the static method
    }

    public static UnbreakableComponent create() {
        return new UnbreakableComponent();
    }

    @Override
    public String buildComponentString() {
        return "unbreakable={}";
    }
}