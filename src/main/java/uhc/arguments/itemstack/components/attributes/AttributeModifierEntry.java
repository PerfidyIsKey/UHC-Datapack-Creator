package uhc.arguments.itemstack.components.attributes;

import uhc.attribute.AttributeId;
import uhc.attribute.AttributeSlot;
import uhc.attribute.AttributeOperation;

import java.util.StringJoiner;

/**
 * Represents a single type-safe attribute modifier entry.
 */
public class AttributeModifierEntry {
    // 1. CHANGE: Fields now use Enums
    private final AttributeId id;
    private final AttributeId type;
    private final double amount;
    private final AttributeOperation operation;
    private final AttributeSlot slot;
    private final AttributeDisplayTag displayTag;

    // 2. CHANGE: Constructor now accepts Enums
    private AttributeModifierEntry(
            AttributeId id,
            AttributeId type,
            double amount,
            AttributeOperation operation,
            AttributeSlot slot,
            AttributeDisplayTag displayTag)
    {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
        this.displayTag = displayTag;
    }

    // 3. CHANGE: Factory method now accepts Enums
    public static AttributeModifierEntry create(
            AttributeId id,
            AttributeId type,
            double amount,
            AttributeOperation operation,
            AttributeSlot slot,
            AttributeDisplayTag displayTag) {
        return new AttributeModifierEntry(id, type, amount, operation, slot, displayTag);
    }

    /**
     * Builds the SNBT compound tag string for this single modifier.
     */
    public String buildSnbt() {
        StringJoiner snbt = new StringJoiner(",", "{", "}");

        // id:"minecraft:armor"
        snbt.add("id:\"" + id.getResourceLocation() + "\"");

        // type:"armor" - NOTE: The type argument often uses the same value as the ID path
        snbt.add("type:\"" + type.getResourceLocation().split(":")[1] + "\"");

        // amount:1000.0
        snbt.add("amount:" + amount);

        // operation:"add_value"
        snbt.add("operation:\"" + operation.getCommandString() + "\"");

        // slot:"armor"
        snbt.add("slot:\"" + slot.getCommandString() + "\"");

        // display:{type:"hidden"}
        if (displayTag != null) {
            snbt.add("display:" + displayTag.buildSnbt());
        }

        return snbt.toString();
    }
}