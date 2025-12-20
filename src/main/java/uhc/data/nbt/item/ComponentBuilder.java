package uhc.data.nbt.item;

import uhc.data.nbt.tags.*;
import uhc.resource.item.components.ComponentId;
import uhc.text.TextComponent;

/**
 * 🛠️ **Component Builder**
 * <p>
 * Helps build the 'components' NBT map for items.
 * Each method follows the modern Minecraft component specification.
 * </p>
 */
public class ComponentBuilder {
    private final CompoundTag root = CompoundTag.create();

    public static ComponentBuilder create() {
        return new ComponentBuilder();
    }

    /**
     * Sets the custom name of the item.
     * Component: minecraft:custom_name
     */
    public ComponentBuilder customName(TextComponent name) {
        root.put(new StringTag(ComponentId.CUSTOM_NAME.getResourceLocation(), name.toString()));
        return this;
    }

    /**
     * Sets the lore (description lines) of the item.
     * Component: minecraft:lore
     */
    public ComponentBuilder lore(TextComponent... lines) {
        // 1. Create the ListTag with the correct ComponentId resource location as the name
        ListTag list = new ListTag(ComponentId.LORE.getResourceLocation());

        for (TextComponent line : lines) {
            if (line != null) {
                // 2. Add the JSON string to the list.
                // In a ListTag, the internal elements usually have empty names.
                list.add(new StringTag("", line.toString()));
            }
        }

        // 3. Put the ListTag directly into the root compound.
        root.put(list);
        return this;
    }

    /**
     * Sets the enchantments on the item.
     * Component: minecraft:enchantments
     */
    public ComponentBuilder enchantments(CompoundTag levels, boolean showInTooltip) {
        CompoundTag enchantTag = CompoundTag.create(ComponentId.ENCHANTMENTS.getResourceLocation());
        enchantTag.put(levels); // levels is a map of enchantment ID -> int
        enchantTag.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));
        root.put(enchantTag);
        return this;
    }

    /**
     * Makes the item unbreakable.
     * Component: minecraft:unbreakable
     */
    public ComponentBuilder unbreakable(boolean showInTooltip) {
        CompoundTag tag = CompoundTag.create(ComponentId.UNBREAKABLE.getResourceLocation());
        tag.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));
        root.put(tag);
        return this;
    }

    public CompoundTag build() {
        return root;
    }
}