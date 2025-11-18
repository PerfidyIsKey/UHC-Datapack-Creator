package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.FallingBlockData;

/**
 * Factory/Builder for the Falling Block Entity NBT structure (used for Care Packages).
 */
public class FallingBlockNbtBuilder implements EntityNbtBuilder {

    private final FallingBlockData data;

    public FallingBlockNbtBuilder(FallingBlockData data) {
        this.data = data;
    }

    @Override
    public CompoundTag buildNbt() {
        CompoundTag rootNbt = new CompoundTag("");

        // 1. BlockState:{Name:"minecraft:chest"}
        CompoundTag blockState = new CompoundTag("BlockState");
        // Accessor updated: data.getBlockName() -> data.blockName()
        blockState.put(new StringTag("Name", data.blockName()));
        rootNbt.put(blockState);

        // 2. TileEntityData:{LootTable:"uhc:supply_drop",CustomName:"Care Package"}
        CompoundTag tileEntityData = new CompoundTag("TileEntityData");

        // LootTable
        // Accessor updated: data.getLootTable() -> data.lootTable()
        tileEntityData.put(new StringTag("LootTable", data.lootTable()));

        // CustomName
        // Accessor updated: data.getCustomName() -> data.customName()
        tileEntityData.put(new StringTag("CustomName", data.customName()));

        rootNbt.put(tileEntityData);

        // 3. Time:1
        // Accessor updated: data.getTime() -> data.time()
        rootNbt.put(new IntTag("Time", data.time()));

        // 4. DropItem:0b
        // Accessor updated: data.getDropItem() -> data.dropItem()
        rootNbt.put(new ByteTag("DropItem", data.dropItem() ? (byte)1 : (byte)0));

        // 5. Tags:["CarePackage"]
        ListTag tagsList = new ListTag("Tags");
        // Accessor updated: data.getTags() -> data.tags()
        for (String tag : data.tags()) {
            // String tags inside a ListTag should have an empty name (key)
            tagsList.add(new StringTag("", tag));
        }
        rootNbt.put(tagsList);

        return rootNbt;
    }
}