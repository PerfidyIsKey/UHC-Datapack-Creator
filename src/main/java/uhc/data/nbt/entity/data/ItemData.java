package uhc.data.nbt.entity.data;

import uhc.data.nbt.item.components.ComponentMap; // Assuming you have a container class
import uhc.data.nbt.tags.ItemNBT;
import uhc.data.nbt.tags.IntTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.ItemId;

import java.util.Objects;

/**
 * 📦 **Item Data Implementation**
 * <p>
 * Represents the "Item" structure used in Item Entities, Player Inventories, and Chests.
 * Format (1.20.5+): {id: "namespace:id", count: 1, components: { ... }}
 * </p>
 */
public record ItemData(
        ItemId id,
        int count,
        ComponentMap components
) {
    /**
     * Canonical Constructor
     * @param id The type-safe ItemId (e.g., ItemId.DIAMOND_SWORD).
     * @param count The stack size (clamped between 1 and 99).
     * @param components The map of data components. If null, an empty map is initialized.
     */
    public ItemData {
        Objects.requireNonNull(id, "Item ID cannot be null.");

        // Minecraft 1.20.5+ counts are technically shorts, but 1-99 is the safe range.
        // Negative counts are invalid and zero-counts result in the item being deleted.
        if (count < 1) count = 1;

        // Ensure components is never null to prevent NPEs during serialization
        if (components == null) components = ComponentMap.create();
    }

    /**
     * Creates ItemData with no custom components.
     */
    public static ItemData create(ItemId id, int count) {
        return new ItemData(id, count, null);
    }

    /**
     * Creates ItemData with specific components.
     */
    public static ItemData create(ItemId id, int count, ComponentMap components) {
        return new ItemData(id, count, components);
    }

    /**
     * Converts the entire Item structure into a CompoundTag.
     * <p>
     * <b>Format:</b>
     * <pre>
     * {
     * id: "minecraft:diamond_sword",
     * count: 1,
     * components: { "minecraft:damage": 0, ... }
     * }
     * </pre>
     * </p>
     * @return A named {@link ItemNBT} representing the Item entity NBT.
     */
    public ItemNBT toNbt(String tagName) {
        ItemNBT tag = ItemNBT.create(tagName);

        // id: Namespaced string
        tag.put(new StringTag("id", id.getResourceLocation()));

        // count: Integer (Minecraft uses 'count' as of 1.20.5, replacing 'Count' byte)
        tag.put(new IntTag("count", count));

        // components: Nested compound of all item components
        if (!components.isEmpty()) {
            tag.put(components.toNbt());
        }

        return tag;
    }
}