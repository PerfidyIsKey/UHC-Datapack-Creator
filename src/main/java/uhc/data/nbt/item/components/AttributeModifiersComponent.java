package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.item.components.ComponentId;
import uhc.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ⚔️ **Attribute Modifiers Component Implementation**
 * <p>
 * Manages the "minecraft:attribute_modifiers" component.
 * Allows items to grant stats like Attack Damage, Movement Speed, or Max Health when equipped.
 * </p>
 */
public class AttributeModifiersComponent implements ItemComponent {

    private final List<Entry> modifiers = new ArrayList<>();
    private boolean showInTooltip = true;

    public AttributeModifiersComponent() {}

    /**
     * Adds a modifier to the list.
     * @param entry A constructed {@link Entry} object.
     */
    public AttributeModifiersComponent add(Entry entry) {
        this.modifiers.add(Objects.requireNonNull(entry));
        return this;
    }

    /**
     * Toggles the global visibility of these modifiers.
     * Setting this to false is the modern equivalent of the "Hide Modifiers" flag.
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
        // In 1.20.5+, this can be a list of modifiers OR a compound containing 'modifiers' and 'show_in_tooltip'
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
     * Represents a single Attribute Modifier entry.
     */
    public static class Entry {
        private final String type;
        private final String id;
        private final double amount;
        private final String operation;
        private String slot = "any";

        // Display fields
        private String displayType = "default";
        private TextComponent overrideValue;

        public Entry(String type, String id, double amount, String operation) {
            this.type = type;
            this.id = id;
            this.amount = amount;
            this.operation = operation;
        }

        public Entry slot(String slot) {
            this.slot = slot;
            return this;
        }

        public Entry display(String type) {
            this.displayType = type;
            return this;
        }

        public Entry overrideDisplay(TextComponent text) {
            this.displayType = "override";
            this.overrideValue = text;
            return this;
        }

        protected CompoundTag toNbt() {
            CompoundTag tag = CompoundTag.create(""); // Entries in a list are usually unnamed
            tag.put(new StringTag("type", type));
            tag.put(new StringTag("id", id));
            tag.put(new DoubleTag("amount", amount));
            tag.put(new StringTag("operation", operation));
            tag.put(new StringTag("slot", slot));

            CompoundTag display = CompoundTag.create("display");
            display.put(new StringTag("type", displayType));
            if (displayType.equals("override") && overrideValue != null) {
                display.put(new StringTag("value", overrideValue.toString()));
            }
            tag.put(display);

            return tag;
        }
    }
}