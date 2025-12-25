package uhc.data.nbt.blockentity;

import uhc.arguments.item.SingleItemStack;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.LongTag;
import uhc.resource.block.BlockId;

import java.util.Objects;

/**
 * 🎵 **Jukebox NBT Builder**
 * <p>
 * Specialized builder for Jukebox Block Entities.
 * Manages the music disc currently inserted and tracks playback timing
 * to synchronize sound effects and particle spawning.
 * </p>
 * <p>
 * <b>NBT Structure:</b>
 * <ul>
 * <li>{@code RecordItem}: A {@link SingleItemStack} (No Slot tag).</li>
 * <li>{@code ticks_since_song_started}: A Long tag used for song synchronization.</li>
 * </ul>
 * </p>
 */
public class JukeboxNBT extends BlockEntityNBT<JukeboxNBT> {

    /**
     * Private constructor.
     * @param root The root CompoundTag for the block entity.
     */
    private JukeboxNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Jukebox NBT builder with the default Jukebox ID.
     * @return A new instance of JukeboxNBT.
     */
    public static JukeboxNBT create() {
        return new JukeboxNBT(CompoundTag.create())
                .id(BlockId.JUKEBOX);
    }

    /**
     * Provides access to the internal NBT storage.
     * <p>
     * <b>Note:</b> We use {@code super.build()} here to retrieve the
     * underlying compound from the {@link BlockEntityNBT} base.
     * </p>
     * @return The root {@link CompoundTag}.
     */
    public CompoundTag root() {
        return super.build();
    }

    // --- 💿 Playback & Content ---

    /**
     * Sets the music disc currently residing inside the jukebox.
     * <p>
     * <b>NBT Key:</b> {@code RecordItem}
     * </p>
     * @param item The {@link SingleItemStack} representing the music disc.
     * @throws NullPointerException if the item is null.
     * @return This builder instance for chaining.
     */
    public JukeboxNBT recordItem(SingleItemStack item) {
        Objects.requireNonNull(item, "RecordItem cannot be null. Use removeRecord() to clear.");

        CompoundTag itemNbt = item.toNbt();
        // The tag MUST be named RecordItem for the jukebox to recognize it.
        itemNbt.setName("RecordItem");
        root().put(itemNbt);

        return this;
    }

    /**
     * Removes the music disc and resets all playback metadata.
     * <p>
     * Per Vanilla logic, if no record is present, the playback timer
     * must also be removed to prevent particle/sound glitches.
     * </p>
     * @return This builder instance for chaining.
     */
    public JukeboxNBT removeRecord() {
        root().remove("RecordItem");
        root().remove("ticks_since_song_started");
        return this;
    }

    /**
     * Sets the number of ticks elapsed since the music disc started playing.
     * <p>
     * <b>Vanilla Logic:</b> This tag determines the current position in the
     * sound file. If this exceeds the track length, music stops.
     * </p>
     * <p>
     * <b>Error Catching:</b> This method will automatically remove the tag
     * if the jukebox contains no record, as the game will otherwise ignore
     * or clear it on the next tick.
     * </p>
     * @param ticks Total ticks since playback began (must be positive).
     * @return This builder instance for chaining.
     */
    public JukeboxNBT ticksSinceSongStarted(long ticks) {
        // Validation: A song cannot play if there is no record.
        if (ticks < 0 || !root().contains("RecordItem")) {
            root().remove("ticks_since_song_started");
        } else {
            root().put(new LongTag("ticks_since_song_started", ticks));
        }
        return this;
    }
}