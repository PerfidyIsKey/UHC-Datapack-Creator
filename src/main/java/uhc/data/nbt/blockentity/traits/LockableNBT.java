package uhc.data.nbt.blockentity.traits;

import uhc.data.nbt.tags.CompoundTag;
import java.util.Objects;

/**
 * 🔒 **Lockable NBT Trait**
 * <p>
 * Applied to Block Entities that can be "locked" (Chests, Furnaces, etc.).
 * In 1.20.5+, the 'lock' tag uses a complex Item Predicate instead of a simple string.
 * </p>
 * @param <T> The type of the builder for fluent chaining.
 */
public interface LockableNBT<T extends LockableNBT<T>> {

    /** @return The underlying root tag of the builder. */
    CompoundTag root();

    /**
     * Sets a complex lock predicate on the container.
     * <p>
     * <b>NBT Key:</b> {@code lock}
     * </p>
     * @param lock The CompoundTag representing the item predicate.
     * @return {@code (T)} The current builder instance.
     */
    @SuppressWarnings("unchecked")
    default T lock(CompoundTag lock) {
        Objects.requireNonNull(lock, "Lock compound cannot be null. Use removeLock() to clear it.");
        lock.setName("lock");
        root().put(lock);
        return (T) this;
    }

    /**
     * Removes the lock from the container.
     * @return {@code (T)} The current builder instance.
     */
    @SuppressWarnings("unchecked")
    default T removeLock() {
        root().remove("lock");
        return (T) this;
    }
}