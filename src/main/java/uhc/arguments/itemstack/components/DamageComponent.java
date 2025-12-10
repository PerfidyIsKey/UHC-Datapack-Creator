package uhc.arguments.itemstack.components;

import uhc.arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'damage' component.
 * Format: damage=0
 */
public class DamageComponent implements ItemComponentTag {
    private final int value;

    private DamageComponent(int value) {
        this.value = value;
    }

    public static DamageComponent create(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Damage value cannot be negative.");
        }
        return new DamageComponent(value);
    }

    @Override
    public String buildComponentString() {
        return "damage=" + value;
    }
}