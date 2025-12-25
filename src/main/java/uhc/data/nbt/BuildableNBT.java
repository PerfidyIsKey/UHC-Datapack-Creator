package uhc.data.nbt;

import uhc.data.nbt.tags.CompoundTag;

/**
 * 🏗️ **NBT Builder Contract**
 * <p>
 * Defines a standard interface for classes that construct complex NBT structures.
 * This allows different builder types (Items, Entities, Block Entities) to be
 * handled polymorphically within the UHC framework.
 * </p>
 */
public interface BuildableNBT {

    /**
     * Finalizes the construction and returns the root NBT tag.
     * <p><b>Implementation Note:</b> This should return the base {@link CompoundTag}
     * that Minecraft expects for the specific object type.</p>
     *
     * @return The complete {@link CompoundTag} representation of the object.
     */
    CompoundTag build();

    /**
     * Returns the string representation of the underlying NBT.
     * Useful for debugging and command generation.
     *
     * @return The SNBT (Stringified NBT) representation.
     */
    @Override
    String toString();
}