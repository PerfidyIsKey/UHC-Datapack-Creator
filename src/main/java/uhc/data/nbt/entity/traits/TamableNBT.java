package uhc.data.nbt.entity.traits;

import uhc.data.nbt.tags.*;
import java.util.Objects;
import java.util.UUID;

/**
 * 🦴 **Tamable Trait**
 * <p>
 * Provides NBT support for mobs that can be owned by players,
 * such as Wolves, Cats, and Parrots.
 * </p>
 */
public interface TamableNBT<T extends TamableNBT<T>> {

    /**
     * Bridge method to access the entity's root {@link CompoundTag}.
     */
    CompoundTag root();

    /**
     * Sets the owner of the entity.
     * <p>
     * Note: In older Minecraft versions, this was stored as 'OwnerUUID' (String).
     * In modern versions (1.16+), it is 'Owner' (IntArray).
     * </p>
     * @param uuid The UUID of the player who owns this mob.
     */
    default T owner(UUID uuid) {
        Objects.requireNonNull(uuid, "Owner UUID cannot be null.");

        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        // Modern Minecraft uses the 4-int array format for 'Owner'
        root().put(new IntArrayTag("Owner", new int[]{
                (int) (most >> 32), (int) most,
                (int) (least >> 32), (int) least
        }));
        return (T) this;
    }

    /**
     * Sets whether the tamed animal is currently sitting.
     * @param sitting True to force the animal to sit.
     */
    default T sitting(boolean sitting) {
        root().put(new ByteTag("Sitting", (byte) (sitting ? 1 : 0)));
        return (T) this;
    }
}