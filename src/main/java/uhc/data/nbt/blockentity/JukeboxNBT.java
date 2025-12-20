package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.*;
import uhc.resource.StaticBlockId;
import java.util.Objects;

/**
 * 🎵 **Jukebox NBT Builder**
 * <p>
 * Specialized builder for Jukebox block entities.
 * Manages the music disc currently inserted and tracks playback timing
 * to synchronize sound effects and particles.
 * </p>
 */
public class JukeboxNBT extends BlockEntityNBT<JukeboxNBT> {

    /**
     * Private constructor for the JukeboxNBT builder.
     * @param root The root CompoundTag for the block entity.
     */
    private JukeboxNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Jukebox NBT builder at the specified coordinates.
     * Automatically assigns the Jukebox ID via {@link StaticBlockId}.
     * * @param x The X coordinate of the jukebox.
     * @param y The Y coordinate of the jukebox.
     * @param z The Z coordinate of the jukebox.
     * @return A new instance of JukeboxNBT.
     */
    public static JukeboxNBT create(int x, int y, int z) {
        return new JukeboxNBT(CompoundTag.create())
                .id(StaticBlockId.JUKEBOX)
                .pos(x, y, z);
    }

    // --- 💿 Playback & Content ---

    /**
     * Sets the music disc item currently residing inside the jukebox.
     * <p>
     * <b>Note:</b> This should be a full item stack compound without the "Slot" tag.
     * </p>
     * * @param itemData The CompoundTag representing the record item stack.
     * @throws NullPointerException if itemData is null.
     * @return This builder instance for chaining.
     */
    public JukeboxNBT recordItem(CompoundTag itemData) {
        Objects.requireNonNull(itemData, "RecordItem compound cannot be null. Use removeRecord() to clear the jukebox.");
        itemData.setName("RecordItem");
        build().put(itemData);
        return this;
    }

    /**
     * Removes the music disc and resets the playback timer.
     * <p>
     * Per vanilla behavior, the {@code ticks_since_song_started} tag is
     * deleted when no item is present.
     * </p>
     * * @return This builder instance for chaining.
     */
    public JukeboxNBT removeRecord() {
        build().remove("RecordItem");
        build().remove("ticks_since_song_started");
        return this;
    }

    /**
     * Sets the number of ticks elapsed since the music disc started playing.
     * <p>
     * <b>Vanilla Logic:</b> This tag is used to determine when particles should spawn
     * and when the sound event should terminate. This tag will be automatically removed
     * if no record is currently inside the jukebox.
     * </p>
     * * @param ticks Total ticks since playback began.
     * @return This builder instance for chaining.
     */
    public JukeboxNBT ticksSinceSongStarted(long ticks) {
        // Validation: The tag should not exist if the jukebox is empty or value is invalid.
        if (ticks < 0 || !build().contains("RecordItem")) {
            build().remove("ticks_since_song_started");
        } else {
            build().put(new LongTag("ticks_since_song_started", ticks));
        }
        return this;
    }
}