package uhc.data.nbt.entity.traits;

import uhc.data.nbt.tags.*;
import java.util.Objects;
import java.util.UUID;

/**
 * ❤️ **Breedable Trait**
 * <p>
 * Provides NBT support for mobs that can breed, grow from babies to adults,
 * and enter "love mode." Common for Cows, Sheep, Horses, and Wolves.
 * </p>
 */
public interface BreedableNBT<T extends BreedableNBT<T>> {

    /**
     * Bridge method to access the entity's root {@link CompoundTag}.
     */
    CompoundTag root();

    /**
     * Sets the age of the mob.
     * <p>
     * Negative values represent a baby (ticks until adulthood).
     * Positive values represent the breeding cooldown for adults.
     * </p>
     * @param ticks Age in game ticks.
     */
    default T age(int ticks) {
        root().put(new IntTag("Age", ticks));
        return (T) this;
    }

    /**
     * Sets the ForcedAge tag, used to prevent babies from growing or to
     * force a specific growth progress.
     */
    default T forcedAge(int ticks) {
        root().put(new IntTag("ForcedAge", ticks));
        return (T) this;
    }

    /**
     * Sets the time the mob remains in "Love Mode" (showing hearts).
     * @param ticks Remaining love ticks. Usually 600 (30 seconds) is default.
     */
    default T inLove(int ticks) {
        // Clamped to 0 because negative InLove can cause glitches
        root().put(new IntTag("InLove", Math.max(0, ticks)));
        return (T) this;
    }

    /**
     * Sets the UUID of the player who caused the mob to enter love mode.
     * Required for experience or achievement tracking when a baby is born.
     * @param uuid The player's UUID.
     */
    default T loveCause(UUID uuid) {
        Objects.requireNonNull(uuid, "LoveCause UUID cannot be null.");

        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        // Minecraft stores the player UUID responsible for breeding here
        root().put(new IntArrayTag("LoveCause", new int[]{
                (int) (most >> 32), (int) (most),
                (int) (least >> 32), (int) (least)
        }));
        return (T) this;
    }
}