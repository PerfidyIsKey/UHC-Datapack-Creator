package uhc.data.nbt.item;

import uhc.data.nbt.tags.*;
import uhc.resource.BlockId;
import uhc.resource.StaticBlockId;

import java.util.Objects;

/**
 * 📦 **Item NBT Builder**
 * <p>
 * Represents an Item Stack in Minecraft. This structure is used for items
 * in inventories, equipment, or stored within Block Entities.
 * </p>
 */
public class ItemNBT {

    private final CompoundTag root;

    private ItemNBT(CompoundTag root) {
        this.root = root;
    }

    /**
     * Initializes a new Item Stack NBT builder.
     * @param id The resource location of the item (e.g., "minecraft:diamond_sword").
     * Must not be "minecraft:air".
     * @return A new instance of ItemNBT.
     */
    public static ItemNBT create(BlockId id) {
        Objects.requireNonNull(id, "Item ID cannot be null.");
        if (id.equals(StaticBlockId.AIR)) {
            throw new IllegalArgumentException("Item ID cannot be minecraft:air in an Item Stack NBT.");
        }

        CompoundTag tag = CompoundTag.create();
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", 1)); // Default count
        return new ItemNBT(tag);
    }

    /**
     * Returns the underlying CompoundTag representing the Item Stack.
     */
    public CompoundTag build() {
        return root;
    }

    /**
     * Sets the number of items in this stack.
     * <p>
     * <b>Note:</b> In NBT, you can stack items beyond their normal limits (e.g., 64 swords).
     * </p>
     * @param count The stack size.
     * @return This builder instance.
     */
    public ItemNBT count(int count) {
        root.put(new IntTag("count", count));
        return this;
    }

    /**
     * Sets the data components for this item.
     * <p>
     * Components store information like custom names, enchantments,
     * durability, and specialized tool properties.
     * </p>
     * @param components The CompoundTag representing the components map.
     * @return This builder instance.
     */
    public ItemNBT components(CompoundTag components) {
        if (components == null) {
            root.remove("components");
        } else {
            components.setName("components");
            root.put(components);
        }
        return this;
    }
}