package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.attribute.AttributeDisplayType;
import uhc.resource.attribute.AttributeOperation;
import uhc.resource.attribute.AttributeId;
import uhc.resource.item.EquipmentSlot;
import uhc.resource.item.components.ComponentId;
import uhc.resource.attribute.AttributeModifierId;
import uhc.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ⚔️ **Attribute Modifiers Component Implementation**
 * <p>
 * Manages the "minecraft:attribute_modifiers" component.
 * Allows items to grant stats like Attack Damage, Movement Speed, or Max Health
 * when equipped in specific slots.
 * </p>
 */
public class AttributeModifiersComponent implements ItemComponent {

    private final List<Entry> modifiers = new ArrayList<>();
    private boolean showInTooltip = true;

    /**
     * Private constructor to enforce static factory usage.
     */
    private AttributeModifiersComponent() {}

    /**
     * Static factory method to create a new AttributeModifiersComponent.
     * @return A new instance for fluent building.
     */
    public static AttributeModifiersComponent create() {
        return new AttributeModifiersComponent();
    }

    /**
     * Adds a modifier to the list.
     * @param entry A constructed {@link Entry} object.
     */
    public AttributeModifiersComponent add(Entry entry) {
        this.modifiers.add(Objects.requireNonNull(entry));
        return this;
    }

    /**
     * Toggles the global visibility of these modifiers in the item tooltip.
     * @param show True to show (default), false to hide.
     */
    public AttributeModifiersComponent showInTooltip(boolean show) {
        this.showInTooltip = show;
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.ATTRIBUTE_MODIFIERS;
    }

    @Override
    public NBTTag toNbt() {
        CompoundTag root = CompoundTag.create(getId().getResourceLocation());

        ListTag modifiersList = new ListTag("modifiers");
        for (Entry mod : modifiers) {
            modifiersList.add(mod.toNbt());
        }

        root.put(modifiersList);
        root.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));

        return root;
    }

    /**
     * Represents a single Attribute Modifier entry within the list.
     */
    public static class Entry {
        private final AttributeId type;
        private final AttributeModifierId id;
        private final double amount;
        private final AttributeOperation operation;
        private EquipmentSlot slot = EquipmentSlot.ANY;

        // Display fields
        private AttributeDisplayType displayType = AttributeDisplayType.DEFAULT;
        private TextComponent overrideValue;

        /**
         * @param type The attribute ID (e.g. "minecraft:generic.attack_damage")
         * @param id A unique namespaced ID for this modifier.
         * @param amount The value of the modifier.
         * @param operation The math type: "add_value", "add_multiplied_base", "add_multiplied_total".
         */
        private Entry(AttributeId type, AttributeModifierId id, double amount, AttributeOperation operation) {
            this.type = Objects.requireNonNull(type);
            this.id = Objects.requireNonNull(id);
            this.amount = amount;
            this.operation = Objects.requireNonNull(operation);
        }

        public static Entry create(AttributeId type, AttributeModifierId id, double amount, AttributeOperation operation) {
            return new Entry(type, id, amount, operation);
        }

        public Entry slot(EquipmentSlot slot) {
            this.slot = slot;
            return this;
        }

        public Entry display(AttributeDisplayType type) {
            this.displayType = type;
            return this;
        }

        public Entry overrideDisplay(TextComponent text) {
            this.displayType = AttributeDisplayType.OVERRIDE;
            this.overrideValue = text;
            return this;
        }

        protected CompoundTag toNbt() {
            CompoundTag tag = CompoundTag.create(""); // Unnamed compound for List entry
            tag.put(new StringTag("type", type.getResourceLocation()));
            tag.put(new StringTag("id", id.getPath()));
            tag.put(new DoubleTag("amount", amount));
            tag.put(new StringTag("operation", operation.getNbtName()));
            tag.put(new StringTag("slot", slot.getNbtName()));

            CompoundTag display = CompoundTag.create("display");
            display.put(new StringTag("type", displayType.getNbtName()));
            if (displayType == AttributeDisplayType.OVERRIDE && overrideValue != null) {
                display.put(new StringTag("value", overrideValue.toString()));
            }
            tag.put(display);

            return tag;
        }
    }
}