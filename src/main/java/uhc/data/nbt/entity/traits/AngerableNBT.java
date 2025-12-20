package uhc.data.nbt.entity.traits;

import uhc.data.nbt.tags.*;
import java.util.Objects;
import java.util.UUID;

/**
 * 💢 **Angerable Trait**
 * <p>
 * Provides NBT support for mobs that can become aggressive toward specific targets,
 * such as Wolves, Bees, and Endermen.
 * </p>
 */
public interface AngerableNBT<T extends AngerableNBT<T>> {

    /**
     * Bridge method to access the entity's root {@link CompoundTag}.
     */
    CompoundTag root();

    /**
     * Sets the remaining time (in ticks) that the mob will remain angry.
     * <p>
     * Usually, a value of 0 means the mob is calm.
     * </p>
     * @param ticks Duration of anger in game ticks.
     */
    @SuppressWarnings("unchecked")
    default T angerEndTime(int ticks) {
        // Minecraft typically uses "AngerTime" (Int) for most neutral mobs
        root().put(new IntTag("AngerTime", ticks));
        return (T) this;
    }

    /**
     * Sets the specific entity the mob is currently angry at using its UUID.
     * @param uuid The UUID of the target.
     */
    @SuppressWarnings("unchecked")
    default T angryAt(UUID uuid) {
        Objects.requireNonNull(uuid, "Target UUID cannot be null for angryAt.");

        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        // Minecraft stores this as an IntArray of 4 integers
        root().put(new IntArrayTag("AngryAt", new int[]{
                (int) (most >> 32), (int) most,
                (int) (least >> 32), (int) least
        }));
        return (T) this;
    }
}