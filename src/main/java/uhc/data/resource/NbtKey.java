package uhc.data.resource;

/**
 * Marker interface for all NBT key enums to enforce that they provide
 * a consistent string representation of the NBT key name.
 */
public interface NbtKey {
    /**
     * Returns the exact string required for the NBT key in the command structure.
     */
    @Override
    String toString();
}