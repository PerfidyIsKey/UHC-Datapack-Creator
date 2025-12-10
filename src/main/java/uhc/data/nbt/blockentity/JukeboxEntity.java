package uhc.data.nbt.blockentity;

import uhc.data.nbt.NBTTag;
import uhc.resource.ItemId;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.StringTag;
import uhc.data.nbt.tags.ByteTag;

// Jukebox Block Entity: Contains a single CompoundTag for the inserted record.
public class JukeboxEntity extends CompoundTag implements BlockEntity {

    // Unique key for the Jukebox
    public enum JukeboxDataKey {
        RECORD_ITEM("RecordItem");

        private final String nbtName;
        JukeboxDataKey(String nbtName) { this.nbtName = nbtName; }
        public String getNbtName() { return nbtName; }
    }

    private JukeboxEntity() {
        initializeDefaultTags();
    }

    public static JukeboxEntity create() {
        return new JukeboxEntity();
    }

    private void initializeDefaultTags() {
        // Initialize with a default, empty RecordItem compound tag
        CompoundTag recordItem = CompoundTag.create(JukeboxDataKey.RECORD_ITEM.getNbtName());
        this.put(recordItem);
    }

    /**
     * Gets the RecordItem CompoundTag.
     */
    public CompoundTag getRecordItem() {
        NBTTag recordTag = get(JukeboxDataKey.RECORD_ITEM.getNbtName());
        if (recordTag instanceof CompoundTag) {
            return (CompoundTag) recordTag;
        }
        // Should not happen if initialized correctly, but good practice to handle.
        return CompoundTag.create(JukeboxDataKey.RECORD_ITEM.getNbtName());
    }

    /**
     * Sets the contents of the RecordItem CompoundTag using a chained API.
     */
    public JukeboxEntity setRecord(ItemId id, byte count) {
        CompoundTag record = getRecordItem();

        // Use the chained put() method from CompoundTag
        record.put(new ByteTag("Count", count))
                .put(new StringTag("id", id.getResourceLocation()));

        return this; // Return this for chaining on the JukeboxEntity itself
    }
}