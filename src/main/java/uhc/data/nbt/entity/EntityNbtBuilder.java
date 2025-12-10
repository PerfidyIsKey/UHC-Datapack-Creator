package uhc.data.nbt.entity;

import uhc.data.nbt.tags.CompoundTag;

/**
 * Defines the contract for any class responsible for constructing the
 * root CompoundTag for a complex Minecraft entity's NBT data.
 */
public interface EntityNbtBuilder {
    /**
     * Builds and returns the complete, finalized NBT CompoundTag for the entity.
     * @return The root NBT CompoundTag.
     */
    CompoundTag buildNbt();
}