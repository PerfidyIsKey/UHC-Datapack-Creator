package uhc.arguments.item;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.*;
import uhc.resource.item.ItemId;
import uhc.resource.item.ItemResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🎒 **Stored Item (Positioned)**
 * <p>
 * Represents a discrete item stack tied to a specific inventory slot.
 * This class serves as the bridge between raw {@link ItemResource}s and the
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

    // --- ⚙️ State & Fields ---

    /** * The unique namespaced identifier for the item.
     * Held as a rich {@link ItemResource} to maintain registry integrity.
     */
    private final ItemResource id;

    /** * The quantity of items in this stack.
     * Clamped to 1+ to prevent "ghost" item stacks in containers.
     */
    private int count;

    /** * The inventory index where this item resides.
     * Note: Serialized as a Byte in NBT (0-127).
     */
    private int slot;

    /** * A collection of {@link ItemComponent} objects defining modern Minecraft data components.
     */
    private final List<ItemComponent> components = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce use of static factory methods and ensure initial validation.
     * <p><b>Error Catching:</b> Rejects {@link ItemId#AIR} because empty slots in NBT
     * are represented by the absence of an entry, not an entry of type air.</p>
     * * @param id The item type (must not be null or air).
     * @param count The quantity (clamped to 1+).
     * @param slot The inventory index.
     * @throws NullPointerException if ID is null.
     * @throws IllegalArgumentException if ID is AIR.
     */
    private StoredItem(ItemResource id, int count, int slot) {
        this.id = Objects.requireNonNull(id, "Item ID cannot be null.");

        if (id.equals(ItemId.AIR)) {
            throw new IllegalArgumentException("StoredItem cannot be 'minecraft:air'. Omit the item from the list instead.");
        }

        this.count = Math.max(1, count);
        this.slot = slot;
    }

    /**
     * Creates a new StoredItem with a default count of 1.
     * @param id The item type {@link ItemResource}.
     * @param slot The target slot index.
     * @return A new StoredItem instance.
     */
    public static StoredItem create(ItemResource id, int slot) {
        return new StoredItem(id, 1, slot);
    }

    /**
     * Creates a new StoredItem with a specific quantity.
     * @param id The item type {@link ItemResource}.
     * @param count The quantity.
     * @param slot The target slot index.
     * @return A new StoredItem instance.
     */
    public static StoredItem create(ItemResource id, int count, int slot) {
        return new StoredItem(id, count, slot);
    }

    // --- 🛠️ Builder & Mutator Methods ---

    /**
     * Updates the stack size.
     * @param count The new count (automatically clamped to a minimum of 1).
     * @return This builder instance for chaining.
     */
    public StoredItem count(int count) {
        this.count = Math.max(1, count);
        return this;
    }

    /**
     * Updates the target inventory slot.
     * @param slot The new slot index.
     * @return This builder instance for chaining.
     */
    public StoredItem slot(int slot) {
        this.slot = slot;
        return this;
    }

    /**
     * Attaches a {@link ItemComponent} to this item.
     * <p><b>Error Catching:</b> Null components are ignored to prevent
     * NullPointerExceptions during serialization.</p>
     * * @param component The data component to add.
     * @return This builder instance for chaining.
     */
    public StoredItem addComponent(ItemComponent component) {
        if (component != null) {
            this.components.add(component);
        }
        return this;
    }

    // --- 🛰️ Accessors ---

    /**
     * Retrieves the current inventory slot index.
     * @return The slot index as an integer.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Retrieves the underlying item resource.
     * @return The {@link ItemResource} identifier.
     */
    public ItemResource getId() {
        return id;
    }

    // --- 📝 Serialization Logic ---

    /**
     * Serializes this item into a {@link CompoundTag} compliant with
     * modern Minecraft inventory formats.
     * <p>
     * <b>NBT Criticality:</b>
     * <ul>
     * <li>The {@code Slot} tag <b>MUST</b> be a {@link ByteTag}. If saved as an
     * {@code IntTag}, the item will exist in NBT but remain invisible in the game UI.</li>
     * <li>The {@code count} tag is stored as an {@link IntTag} (1.20.5+ standard).</li>
     * </ul>
     * </p>
     * <b>Error Catching:</b> Validates the resource location and ensures component
     * serialization does not fail the entire stack.
     * * @return A {@link CompoundTag} containing the item data.
     * @throws IllegalStateException if the item ID fails validation.
     */
    public CompoundTag toNbt() {
        // Validate the resource location before attempting to write to NBT
        id.validate();

        CompoundTag tag = CompoundTag.create("");
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", count));

        // CRITICAL: Slot conversion to Byte.
        // We cast to byte to match Minecraft's expected NBT Type ID 1.
        tag.put(new ByteTag("Slot", (byte) slot));

        if (!components.isEmpty()) {
            CompoundTag componentsMap = CompoundTag.create("components");
            boolean addedAny = false;

            for (ItemComponent component : components) {
                try {
                    NBTTag componentData = component.toNbt();
                    if (componentData != null) {
                        componentsMap.put(componentData);
                        addedAny = true;
                    }
                } catch (Exception e) {
                    // Catch: Prevent a single malformed component from breaking
                    // the entire inventory serialization.
                }
            }

            if (addedAny) {
                tag.put(componentsMap);
            }
        }
        return tag;
    }
}