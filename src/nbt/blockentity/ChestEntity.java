package nbt.blockentity;

import nbt.NBTTag;
import nbt.TagType;
import nbt.tags.CompoundTag;
import nbt.tags.ListTag;
import nbt.tags.StringTag;
import nbt.tags.IntTag;


// A specialized CompoundTag for the Chest Block Entity
public class ChestEntity extends CompoundTag implements BlockEntity {

    // --- Special Chest-only Enums ---
    public enum ChestDataKey {
        ITEMS("Items"),
        X_COORD("x"),
        Y_COORD("y"),
        Z_COORD("z");

        private final String nbtName;
        ChestDataKey(String nbtName) { this.nbtName = nbtName; }
        public String getNbtName() { return nbtName; }
    }
    // --------------------------------

    public ChestEntity(String name) {
        super(name);
        initializeChestTags();
    }

    private void initializeChestTags() {
        // Ensure the ID tag is present
        this.put(new StringTag("id", BlockEntityType.CHEST.getNbtId()));

        // Ensure the mandatory 'Items' ListTag is present, initialized for Compounds (TagType 10)
        ListTag itemsTag = new ListTag(ChestDataKey.ITEMS.getNbtName());
        itemsTag.setElementType(TagType.TAG_COMPOUND); // TagType 10
        this.put(itemsTag);

        // Initialize coordinates as IntTags
        this.put(new IntTag(ChestDataKey.X_COORD.getNbtName(), 0));
        this.put(new IntTag(ChestDataKey.Y_COORD.getNbtName(), 0));
        this.put(new IntTag(ChestDataKey.Z_COORD.getNbtName(), 0));
    }

    /** Specialized getter for the 'Items' ListTag. */
    public ListTag getItems() {
        return (ListTag) get(ChestDataKey.ITEMS.getNbtName());
    }

    // --- NEW CHAINED METHODS ---

    /** * Sets the value of an IntTag (like coordinates) using its key and returns itself for chaining.
     */
    public ChestEntity setInt(ChestDataKey key, int value) {
        NBTTag tag = get(key.getNbtName());
        if (tag instanceof IntTag) {
            ((IntTag) tag).setValue(value);
        } else {
            throw new IllegalArgumentException("NBTTag '" + key.getNbtName() + "' is not an IntTag.");
        }
        return this; // RETURN 'this' for chaining
    }

    /** * Adds an item CompoundTag to the Items list and returns itself for chaining.
     */
    public ChestEntity addItem(CompoundTag item) {
        // List elements must be unnamed
        item.setName(null);
        getItems().add(item);
        return this; // RETURN 'this' for chaining
    }
}