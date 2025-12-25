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
 * Manages the {@code minecraft:attribute_modifiers} component for items.
 * This class facilitates adding modifiers like Attack Damage, Max Health, or
 * Movement Speed that apply when the item is in specific equipment slots.
 * </p>
 * <p>
 * <b>Note:</b> This implementation uses the 1.21 "List Format," producing
 * {@code attribute_modifiers=[{...}]} instead of the compound object format.
 * </p>
 */
public class AttributeModifiersComponent implements ItemComponent {

    // --- ⚙️ Internal State ---

    /** * The collection of individual attribute modifiers to be applied. */
    private final List<Entry> modifiers = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce the use of the static factory method {@link #create()}.
     */
    private AttributeModifiersComponent() {}

    /**
     * Creates a new instance of the AttributeModifiersComponent.
     * @return A fresh component instance for fluent building.
     */
    public static AttributeModifiersComponent create() {
        return new AttributeModifiersComponent();
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Adds a specific attribute modifier entry to this component.
     * <p><b>Error Catching:</b> Ensures the entry is not null before adding.</p>
     * @param entry The modifier entry to include.
     * @return This component instance for method chaining.
     * @throws NullPointerException if the provided entry is null.
     */
    public AttributeModifiersComponent add(Entry entry) {
        this.modifiers.add(Objects.requireNonNull(entry, "Cannot add a null modifier entry."));
        return this;
    }

    // --- 🛰️ Component Logic ---

    /**
     * @return The unique identifier for this item component.
     */
    @Override
    public ComponentId getId() {
        return ComponentId.ATTRIBUTE_MODIFIERS;
    }

    /**
     * Serializes the component into the modern NBT List format.
     * <p><b>Logic:</b> Returns a {@link ListTag} directly as the root value to
     * satisfy the Minecraft 1.21 parser's expectation for a direct list
     * (e.g., {@code [ {id:"...", ...} ]}).</p>
     * @return A ListTag containing all serialized modifier entries.
     */
    @Override
    public NBTTag toNbt() {
        // Return the ListTag directly to force the array syntax in the resulting SNBT
        ListTag modifiersList = new ListTag("");

        for (Entry mod : modifiers) {
            modifiersList.add(mod.toNbt());
        }

        return modifiersList;
    }

    // --- 📦 Nested Entry Class ---

    /**
     * Represents a single Attribute Modifier entry within the modifiers list.
     */
    public static class Entry {

        // --- Fields ---
        private final AttributeId type;
        private final AttributeModifierId id;
        private final double amount;
        private final AttributeOperation operation;
        private EquipmentSlot slot = EquipmentSlot.ANY;

        private AttributeDisplayType displayType = AttributeDisplayType.DEFAULT;
        private TextComponent overrideValue;

        // --- Constructor & Factory ---

        /**
         * Private constructor to define the core properties of a modifier.
         */
        private Entry(AttributeId type, AttributeModifierId id, double amount, AttributeOperation operation) {
            this.type = Objects.requireNonNull(type, "Attribute type cannot be null.");
            this.id = Objects.requireNonNull(id, "Modifier ID cannot be null.");
            this.amount = amount;
            this.operation = Objects.requireNonNull(operation, "Operation type cannot be null.");
        }

        /**
         * Creates a new modifier entry.
         * @param type The attribute to modify (e.g., MAX_HEALTH).
         * @param id The unique ID for this modifier.
         * @param amount The numerical value of the change.
         * @param operation The mathematical operation to perform.
         * @return A new Entry instance.
         */
        public static Entry create(AttributeId type, AttributeModifierId id, double amount, AttributeOperation operation) {
            return new Entry(type, id, amount, operation);
        }

        // --- Fluent Config ---

        /**
         * Sets the equipment slot where this modifier is active.
         * @param slot The target slot (e.g., MAINHAND, ARMOR).
         * @return This entry for chaining.
         */
        public Entry slot(EquipmentSlot slot) {
            this.slot = slot != null ? slot : EquipmentSlot.ANY;
            return this;
        }

        /**
         * Sets the visual display behavior for this modifier.
         * @param type The display type (e.g., HIDDEN).
         * @return This entry for chaining.
         */
        public Entry display(AttributeDisplayType type) {
            this.displayType = type != null ? type : AttributeDisplayType.DEFAULT;
            return this;
        }

        /**
         * Overrides the default display text with a custom component.
         * @param text The text to display.
         * @return This entry for chaining.
         */
        public Entry overrideDisplay(TextComponent text) {
            this.displayType = AttributeDisplayType.OVERRIDE;
            this.overrideValue = text;
            return this;
        }

        // --- Serialization ---

        /**
         * Serializes this entry into a CompoundTag.
         * <p><b>Error Catching:</b> Validates that all necessary components are present
         * before building the tag to prevent malformed function files.</p>
         * @return A CompoundTag representing one modifier object.
         * @throws IllegalStateException if the entry state is inconsistent.
         */
        protected CompoundTag toNbt() {
            CompoundTag tag = CompoundTag.create("");

            // Matches the required 1.21 syntax: id, then type, then amount...
            tag.put(new StringTag("id", id.getPath()));
            tag.put(new StringTag("type", type.getResourceLocation()));
            tag.put(new DoubleTag("amount", amount));
            tag.put(new StringTag("operation", operation.getNbtName()));
            tag.put(new StringTag("slot", slot.getNbtName()));

            // Handle display properties (e.g., display:{type:"hidden"})
            if (displayType != AttributeDisplayType.DEFAULT) {
                CompoundTag displayCompound = CompoundTag.create("display");
                displayCompound.put(new StringTag("type", displayType.getNbtName()));

                if (displayType == AttributeDisplayType.OVERRIDE && overrideValue != null) {
                    displayCompound.put(new StringTag("value", overrideValue.toString()));
                }
                tag.put(displayCompound);
            }

            return tag;
        }
    }
}