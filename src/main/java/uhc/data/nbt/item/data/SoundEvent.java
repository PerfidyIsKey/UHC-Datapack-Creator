package uhc.data.nbt.item.data;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.FloatTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.sound.SoundId;

/**
 * 🔊 **Sound Event Data Structure**
 * <p>
 * Represents a Minecraft sound event definition.
 * Can be used as a simple ID string or a complex definition with a range.
 * </p>
 */
public class SoundEvent {

    private final SoundId soundId;
    private final Float range;

    public SoundEvent(SoundId soundId) {
        this(soundId, null);
    }

    public SoundEvent(SoundId soundId, Float range) {
        this.soundId = soundId;
        this.range = range;
    }

    /**
     * Converts this data object into its NBT representation.
     * <p>
     * If no range is specified, it returns a {@link StringTag}.
     * If a range is specified, it returns a {@link CompoundTag}.
     * </p>
     */
    public Object toNbt() {
        if (range == null) {
            return new StringTag("", soundId.getResourceLocation());
        }

        CompoundTag tag = CompoundTag.create("");
        tag.put(new StringTag("sound_id", soundId.getResourceLocation()));
        tag.put(new FloatTag("range", range));
        return tag;
    }

    public SoundId getSoundId() {
        return soundId;
    }

    public Float getRange() {
        return range;
    }
}