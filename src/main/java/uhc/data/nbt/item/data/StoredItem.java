package uhc.data.nbt.item.data;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.*;
import uhc.resource.item.ItemId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🎒 **Stored Item (Positioned)**
 * <p>
 * Represents a discrete item stack tied to a specific inventory slot.
 * This class serves as the bridge between raw {@link ItemId}s and the
 * complex NBT structures required for containers (Chests, Players, etc.).
 * </p>
 * <p>
 * <b>NBT Architecture (1.20.5+):</b>
 * <ul>
 * <li>{@code id}: Namespaced string (e.g., "minecraft:diamond").</li>
 * <li>{@code count}: Integer representing stack size.</li>
 * <li>{@code Slot}: Byte representing index in the inventory grid.</li>
 * <li>{@code components}: Compound map of Data Components.</li>
 * </ul>
 * </p>
 */
public class StoredItem {
    private final ItemId id;
    private int count;
    private int slot;
    private final List<ItemComponent> components = new ArrayList<>();

    /**
     * Private constructor to enforce use of static factory methods.
     * @param id The item type (must not be null or air).
     * @param count The quantity (clamped to 1+).
     * @param slot The inventory index.
     */
    private StoredItem(ItemId id, int count, int slot) {
        this.id = Objects.requireNonNull(id, "Item ID cannot be null.");

        // Catch: Minecraft inventory lists (like 'Items' in Chests) should not contain air.
        // Empty slots should simply be omitted from the NBT list.
        if (id.getResourceLocation().equals("minecraft:air")) {
            throw new IllegalArgumentException("StoredItem cannot be 'minecraft:air'. Omit the item from the list instead.");
        }

        this.count = Math.max(1, count);
        this.slot = slot;
    }

    /**
     * Creates a new StoredItem with a default count of 1.
     * @param id The item type.
     * @param slot The target slot index.
     * @return A new StoredItem instance.
     */
    public static StoredItem create(ItemId id, int slot) {
        return new StoredItem(id, 1, slot);
    }

    /**
     * Creates a new StoredItem with a specific count.
     * @param id The item type.
     * @param count The quantity.
     * @param slot The target slot index.
     * @return A new StoredItem instance.
     */
    public static StoredItem create(ItemId id, int count, int slot) {
        return new StoredItem(id, count, slot);
    }

    /**
     * Updates the stack size.
     * @param count The new count (clamped to 1).
     * @return This builder instance.
     */
    public StoredItem count(int count) {
        this.count = Math.max(1, count);
        return this;
    }

    /**
     * Updates the target inventory slot.
     * @param slot The new slot index.
     * @return This builder instance.
     */
    public StoredItem slot(int slot) {
        this.slot = slot;
        return this;
    }

    /**
     * Attaches a {@link ItemComponent} to this item.
     * <p><b>Catch:</b> Duplicate components of the same type will lead to
     * multiple entries in the NBT map, where the last one usually wins.</p>
     * @param component The component to add.
     * @return This builder instance.
     */
    public StoredItem addComponent(ItemComponent component) {
        if (component != null) {
            this.components.add(component);
        }
        return this;
    }

    /**
     * @return The currently assigned slot index.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Serializes this item into a {@link CompoundTag} compliant with
     * Minecraft's inventory item format.
     * <p>
     * <b>Warning:</b> Forces 'Slot' into a {@link ByteTag}. If 'Slot' is
     * saved as an Int, the item will be invisible in most GUIs.
     * </p>
     * @return The serialized item NBT.
     */
    public CompoundTag toNbt() {
        CompoundTag tag = CompoundTag.create("");
        tag.put(new StringTag("id", id.getResourceLocation()));

        // As of 1.20.5, 'count' is technically an Int in NBT, though it
        // usually stays within byte/short ranges (1-99).
        tag.put(new IntTag("count", count));

        // CRITICAL: Slot MUST be a Byte (Type 1).
        // Using IntTag here is a common cause for items not appearing.
        tag.put(new ByteTag("Slot", (byte) slot));

        if (!components.isEmpty()) {
            CompoundTag componentsMap = CompoundTag.create("components");
            for (ItemComponent component : components) {
                // toNbt() result is expected to have the component's ID as its name.
                componentsMap.put(component.toNbt());
            }
            tag.put(componentsMap);
        }
        return tag;
    }
}