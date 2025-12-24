package uhc.data.nbt.item.data;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.ItemId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 📦 **Single Item Stack**
 * <p>
 * Represents an item stack without a slot index. Used for NBT fields like
 * {@code RecordItem} in Jukeboxes or {@code Item} in Item Entities.
 * </p>
 */
public class SingleItemStack {
    private final ItemId id;
    private int count;
    private final List<ItemComponent> components = new ArrayList<>();

    private SingleItemStack(ItemId id, int count) {
        this.id = Objects.requireNonNull(id, "Item ID cannot be null.");
        if (id.getResourceLocation().equals("minecraft:air")) {
            throw new IllegalArgumentException("SingleItemStack cannot be air.");
        }
        this.count = Math.max(1, count);
    }

    public static SingleItemStack create(ItemId id) {
        return new SingleItemStack(id, 1);
    }

    public static SingleItemStack create(ItemId id, int count) {
        return new SingleItemStack(id, count);
    }

    public SingleItemStack addComponent(ItemComponent component) {
        if (component != null) {
            this.components.add(component);
        }
        return this;
    }

    /**
     * Converts the stack into a CompoundTag.
     * Note: No "Slot" tag is included.
     */
    public CompoundTag toNbt() {
        CompoundTag tag = CompoundTag.create("");
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", count));

        if (!components.isEmpty()) {
            CompoundTag componentsMap = CompoundTag.create("components");
            for (ItemComponent component : components) {
                componentsMap.put(component.toNbt());
            }
            tag.put(componentsMap);
        }
        return tag;
    }
}