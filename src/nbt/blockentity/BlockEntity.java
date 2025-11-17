package nbt.blockentity;

import nbt.NBTTag;

// Marker interface for a CompoundTag that represents a Minecraft Block Entity.
// It will typically be implemented by a concrete class that extends CompoundTag.
public interface BlockEntity extends NBTTag {
    // Optionally add common Block Entity methods here, e.g.,
    // String getId(); // to get the "id" tag value
    // int getX();    // to get the "x" tag value
}