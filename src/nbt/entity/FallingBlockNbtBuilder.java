package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.FallingBlockData;
import shared.StaticEntityTag;
import nbt.TagType;

/**
 * Factory/Builder for the Falling Block Entity NBT structure.
 * This implementation dynamically constructs the CompoundTag, including only
 * the fields that have been explicitly set in the FallingBlockData record.
 */
public class FallingBlockNbtBuilder implements EntityNbtBuilder {

    private final FallingBlockData data;

    private FallingBlockNbtBuilder(FallingBlockData data) {
        this.data = data;
    }

    public static FallingBlockNbtBuilder create(FallingBlockData data) {
        return new FallingBlockNbtBuilder(data);
    }

    @Override
    public CompoundTag buildNbt() {
        CompoundTag rootNbt = new CompoundTag("");

        // --- 1. BlockState (Requires blockName) ---
        if (data.blockName() != null) {
            CompoundTag blockState = new CompoundTag("BlockState");
            blockState.put(new StringTag("Name", data.blockName().getResourceLocation()));
            rootNbt.put(blockState);
        }

        // --- 2. TileEntityData (Requires lootTable or customName) ---
        if (data.lootTable() != null || data.customName() != null) {
            CompoundTag tileEntityData = new CompoundTag("TileEntityData");

            if (data.lootTable() != null) {
                tileEntityData.put(new StringTag("LootTable", data.lootTable().getResourceLocation()));
            }
            // CustomName can be a plain String, so we don't check for null if it's the only one.
            if (data.customName() != null) {
                tileEntityData.put(new StringTag("CustomName", data.customName()));
            }

            // Only add TileEntityData if it contains elements
            if (!tileEntityData.getValue().isEmpty()) {
                rootNbt.put(tileEntityData);
            }
        }

        // --- 3. Time (If set to non-default, which is 0) ---
        // Note: Time is usually omitted if 0, but included if 1 or more.
        if (data.time() != 0) {
            rootNbt.put(new IntTag("Time", data.time()));
        }

        // --- 4. DropItem (If set to true) ---
        if (data.dropItem()) {
            // DropItem:0b is the default state and can usually be omitted in a filter.
            // We only explicitly include it if true (1b). If false, it's 0b, which is default.
            // Since the user explicitly included '0b' in the request, we include it if set to false for filtering flexibility.
            rootNbt.put(new ByteTag("DropItem", (byte) (data.dropItem() ? 1 : 0)));
        }

        // --- 5. Tags ---
        if (data.tags() != null && data.tags().length > 0) {
            ListTag tagsList = new ListTag("Tags");
            // The list element type must be set by the first element's type
            tagsList.setElementType(TagType.TAG_STRING);

            for (StaticEntityTag tag : data.tags()) {
                // String tags inside a ListTag should have an empty name (key)
                tagsList.add(new StringTag("", tag.getTagName()));
            }
            rootNbt.put(tagsList);
        }

        return rootNbt;
    }
}