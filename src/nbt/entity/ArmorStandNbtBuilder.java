package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.ArmorStandData;

/**
 * Factory/Builder for the Armor Stand Entity NBT structure.
 */
public class ArmorStandNbtBuilder implements EntityNbtBuilder {

    private final ArmorStandData data;

    public ArmorStandNbtBuilder(ArmorStandData data) {
        this.data = data;
    }

    @Override
    public CompoundTag buildNbt() {
        CompoundTag rootNbt = new CompoundTag("");

        // Helper to convert boolean to 1b
        final Byte TRUE_BYTE = (byte)1;

        // 1. Invulnerable:1b
        if (data.invulnerable()) {
            rootNbt.put(new ByteTag("Invulnerable", TRUE_BYTE));
        }

        // 2. Marker:1b
        if (data.marker()) {
            rootNbt.put(new ByteTag("Marker", TRUE_BYTE));
        }

        // 3. Invisible:1b
        if (data.invisible()) {
            rootNbt.put(new ByteTag("Invisible", TRUE_BYTE));
        }

        // 4. Tags:["CP1"]
        if (data.tags().length > 0) {
            ListTag tagsList = new ListTag("Tags");
            for (String tag : data.tags()) {
                // String tags inside a ListTag should have an empty name (key)
                tagsList.add(new StringTag("", tag));
            }
            rootNbt.put(tagsList);
        }

        return rootNbt;
    }
}