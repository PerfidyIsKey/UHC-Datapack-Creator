package uhc.data.nbt.item;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.ItemId;

import java.util.Objects;

/**
 * 📦 **Item NBT Builder**
 * <p>
 * Represents an Item Stack. Manages the core identity (ID, Count) and
 * provides an internal {@link ComponentBuilder} for data components.
 * </p>
 */
public class ItemNBT {

    private final CompoundTag root;
    private final ComponentBuilder componentBuilder;

    private ItemNBT(CompoundTag root) {
        this.root = root;
        this.componentBuilder = ComponentBuilder.create();
    }

    /**
     * Initializes a new Item Stack NBT builder.
     * @param id The resource location of the item.
     * @return A new instance of ItemNBT.
     */
    public static ItemNBT create(ItemId id) {
        Objects.requireNonNull(id, "Item ID cannot be null.");
        if (id.equals(ItemId.AIR)) {
            throw new IllegalArgumentException("Item ID cannot be minecraft:air.");
        }

        CompoundTag tag = CompoundTag.create();
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", 1));
        return new ItemNBT(tag);
    }

    /**
     * Sets the stack size.
     */
    public ItemNBT count(int count) {
        root.put(new IntTag("count", count));
        return this;
    }

    /**
     * Directly adds a component to this item's internal component builder.
     * @param component The {@link ItemComponent} to add.
     * @return This builder instance.
     */
    public ItemNBT add(ItemComponent component) {
        this.componentBuilder.add(component);
        return this;
    }

    /**
     * Overwrites the internal components with a pre-built CompoundTag.
     * @param components The NBT compound representing the components map.
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

    /**
     * Finalizes the item NBT by building the internal components
     * and injecting them into the root tag.
     * @return The complete NBT Compound for the item stack.
     */
    public CompoundTag build() {
        CompoundTag componentsNbt = componentBuilder.build();

        // Only attach the 'components' tag if it actually contains data
        if (!componentsNbt.getValue().isEmpty()) {
            componentsNbt.setName("components");
            root.put(componentsNbt);
        }

        return root;
    }
}