package uhc.data.nbt.blockentity;

import uhc.data.nbt.blockentity.traits.LockableNBT;
import uhc.data.nbt.blockentity.traits.LootableNBT;
import uhc.data.nbt.blockentity.traits.RenamableNBT;
import uhc.data.nbt.item.data.StoredItem;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.ListTag;
import uhc.resource.loot_table.LootTableId;

import java.util.Objects;

/**
 * 🗄️ **Chest Block Entity NBT Builder**
 * <p>
 * Specialized builder for single-chest containers (Chests, Barrels, Shulker Boxes).
 * Manages inventory contents, custom names, locking mechanisms, and loot generation.
 * </p>
 * <p>
 * <b>Traits:</b>
 * <ul>
 * <li>{@link RenamableNBT} - Allows setting a GUI title via CustomName.</li>
 * <li>{@link LockableNBT} - Restricts access via Item Predicate locks.</li>
 * <li>{@link LootableNBT} - Supports dynamic content generation via Loot Tables.</li>
 * </ul>
 * </p>
 */
public class ChestNBT extends BlockEntityNBT<ChestNBT>
        implements RenamableNBT<ChestNBT>, LockableNBT<ChestNBT>, LootableNBT<ChestNBT> {

    private ChestNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Chest NBT builder with an empty root tag.
     * @return A new instance of ChestNBT.
     */
    public static ChestNBT create() {
        return new ChestNBT(CompoundTag.create());
    }

    /**
     * Required by traits to access the underlying NBT structure.
     */
    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 📦 Inventory Management ---

    /**
     * Adds a {@link StoredItem} to the chest's inventory list.
     * * <p><b>Validation Logic:</b>
     * This method verifies that the item's slot is within the valid chest range (0-26).
     * If a LootTable is present, these items may be overwritten when the chest is opened.
     * </p>
     * * @param item The positioned item to add. Must not be null.
     * @return This builder instance for fluent chaining.
     * @throws IllegalArgumentException if the item's slot is outside the 0-26 range.
     * @throws NullPointerException if the item is null.
     */
    public ChestNBT item(StoredItem item) {
        Objects.requireNonNull(item, "Cannot add a null StoredItem to ChestNBT.");

        // --- Error Catching: Slot Range Validation ---
        // Chests have 27 slots (3 rows of 9). Valid indices: 0 to 26.
        int slot = item.getSlot(); // Assuming StoredItem has a getSlot() method
        if (slot < 0 || slot > 26) {
            throw new IllegalArgumentException(
                    "Invalid chest slot: " + slot + ". Chest slots must be between 0 and 26."
            );
        }

        ListTag itemsList = (ListTag) root().get("Items");
        if (itemsList == null) {
            itemsList = new ListTag("Items");
        }

        // Add the serialized item (which includes its own 'Slot' tag)
        itemsList.add(item.toNbt());
        root().put(itemsList);

        return this;
    }

    /**
     * Clears all items currently defined in the 'Items' list.
     * Useful for resetting a builder before applying a new inventory set.
     * @return This builder instance.
     */
    public ChestNBT clearItems() {
        root().remove("Items");
        return this;
    }

    // --- 🛠️ Trait Overrides for Fluent Chaining ---
    // (Optional: Explicitly overriding ensures the return type remains ChestNBT)

    @Override
    public ChestNBT customName(uhc.text.TextComponent component) {
        return RenamableNBT.super.customName(component);
    }

    @Override
    public ChestNBT lock(CompoundTag lock) {
        return LockableNBT.super.lock(lock);
    }

    @Override
    public ChestNBT lootTable(LootTableId resourceLocation) {
        return LootableNBT.super.lootTable(resourceLocation);
    }
}