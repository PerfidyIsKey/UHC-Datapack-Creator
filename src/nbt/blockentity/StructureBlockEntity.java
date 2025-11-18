package nbt.blockentity;

import nbt.NBTTag;
import nbt.tags.*;
import shared.StructureBlockMode;

public class StructureBlockEntity extends CompoundTag implements BlockEntity {

    // --- Special Structure Block-only Enums ---

    public enum StructureDataKey {
        AUTHOR("author"), // Author of the structure [String]
        IGNORE_ENTITIES("ignoreEntities"), // Whether entities should be ignored in the structure [Byte]
        INTEGRITY("integrity"), // How complete the structure is that gets placed [Float]
        METADATA("metadata"), // Value of the data structure block field [String]
        MIRROR("mirror"), // How the structure is mirrored [String]
        MODE("mode"), // The current mode of this structure block [String]
        NAME("name"), // Name of the structure [String]
        POS_X("posX"), // X-position of the structure [Int]
        POS_Y("posY"), // Y-position of the structure [Int]
        POS_Z("posZ"), // Z-position of the structure [Int]
        POWERED("powered"), // Whether this structure block is being powered by redstone [Byte]
        ROTATION("rotation"), // Rotation of the structure [String]
        SEED("seed"), // The seed to use for the structure integrity, 0 means random [Long]
        SHOW_BOUNDING_BOX("showboundingbox"), // Whether to show the structure's bounding box to players in Creative mode [Byte]
        SIZE_X("sizeX"), // X-size of the structure, its length [Int]
        SIZE_Y("sizeY"), // Y-size of the structure, its height [Int]
        SIZE_Z("sizeZ"); // Z-size of the structure, its depth [Int]

        private final String nbtName;
        StructureDataKey(String nbtName) { this.nbtName = nbtName; }
        public String getNbtName() { return nbtName; }
    }
    // --------------------------------

    public StructureBlockEntity(String name) {
        super(name);
        initializeDefaultTags();
    }

    private void initializeDefaultTags() {

        // String tags
        this.put(new StringTag(StructureDataKey.METADATA.getNbtName(), ""));
        this.put(new StringTag(StructureDataKey.MIRROR.getNbtName(), "NONE"));
        this.put(new StringTag(StructureDataKey.AUTHOR.getNbtName(), "?"));
        this.put(new StringTag(StructureDataKey.ROTATION.getNbtName(), "NONE"));
        this.put(new StringTag(StructureDataKey.MODE.getNbtName(), "LOAD"));
        this.put(new StringTag(StructureDataKey.NAME.getNbtName(), ""));

        // Int tags
        this.put(new IntTag(StructureDataKey.POS_X.getNbtName(), 0));
        this.put(new IntTag(StructureDataKey.POS_Y.getNbtName(), 0));
        this.put(new IntTag(StructureDataKey.POS_Z.getNbtName(), 0));
        this.put(new IntTag(StructureDataKey.SIZE_X.getNbtName(), 0));
        this.put(new IntTag(StructureDataKey.SIZE_Y.getNbtName(), 0));
        this.put(new IntTag(StructureDataKey.SIZE_Z.getNbtName(), 0));

        // Byte tags
        this.put(new ByteTag(StructureDataKey.IGNORE_ENTITIES.getNbtName(), (byte)1));
        this.put(new ByteTag(StructureDataKey.POWERED.getNbtName(), (byte)0));
        this.put(new ByteTag(StructureDataKey.SHOW_BOUNDING_BOX.getNbtName(), (byte)1));

        // Long tags
        this.put(new LongTag(StructureDataKey.SEED.getNbtName(), 0L));

        // Float tags
        this.put(new FloatTag(StructureDataKey.INTEGRITY.getNbtName(), 1.0f));

    }

    /**
     * Convenience setter for the structure name.
     */
    public void setStructureName(String name) {
        ((StringTag) get(StructureDataKey.NAME.getNbtName())).setValue(name);
    }

    /**
     * Convenience setter for the structure size.
     */
    public void setStructureSize(int x, int y, int z) {
        ((IntTag) get(StructureDataKey.SIZE_X.getNbtName())).setValue(x);
        ((IntTag) get(StructureDataKey.SIZE_Y.getNbtName())).setValue(y);
        ((IntTag) get(StructureDataKey.SIZE_Z.getNbtName())).setValue(z);
    }

    /** Sets the value of a StringTag using its StructureDataKey and returns itself for chaining. */
    public StructureBlockEntity setString(StructureDataKey key, String value) {
        NBTTag tag = get(key.getNbtName());
        if (tag instanceof StringTag) {
            ((StringTag) tag).setValue(value);
        } else {
            throw new IllegalArgumentException("NBTTag '" + key.getNbtName() + "' is not a StringTag.");
        }
        return this;
    }

    /** * Sets the structure's rotation using the type-safe Rotation enum.
     */
    public StructureBlockEntity setRotation(StructureRotation rotation) {
        // Use the existing setString method, converting the enum to its required NBT string name.
        setString(StructureDataKey.ROTATION, rotation.getNbtName());
        return this;
    }

    /** * Sets the structure's mirror using the type-safe Mirror enum.
     */
    public StructureBlockEntity setMirror(StructureMirror mirror) {
        // Use the existing setString method, converting the enum to its required NBT string name.
        setString(StructureDataKey.MIRROR, mirror.getNbtName());
        return this;
    }

    /** * Sets the structure's mode using the type-safe Mode enum.
     */
    public StructureBlockEntity setMode(StructureBlockMode mode) {
        // Use the existing setString method, converting the enum to its required NBT string name.
        setString(StructureDataKey.MODE, mode.getNbtValue());
        return this;
    }

    /** Sets the value of an IntTag using its StructureDataKey and returns itself for chaining. */
    public StructureBlockEntity setInt(StructureDataKey key, int value) {
        NBTTag tag = get(key.getNbtName());
        if (tag instanceof IntTag) {
            ((IntTag) tag).setValue(value);
        } else {
            throw new IllegalArgumentException("NBTTag '" + key.getNbtName() + "' is not an IntTag.");
        }
        return this;
    }

    /** Sets the value of a ByteTag using its StructureDataKey and returns itself for chaining. */
    public StructureBlockEntity setByte(StructureDataKey key, byte value) {
        NBTTag tag = get(key.getNbtName());
        if (tag instanceof ByteTag) {
            ((ByteTag) tag).setValue(value);
        } else {
            throw new IllegalArgumentException("NBTTag '" + key.getNbtName() + "' is not a ByteTag.");
        }
        return this;
    }

    /** Sets the value of a LongTag using its StructureDataKey and returns itself for chaining. */
    public StructureBlockEntity setLong(StructureDataKey key, long value) {
        NBTTag tag = get(key.getNbtName());
        if (tag instanceof LongTag) {
            ((LongTag) tag).setValue(value);
        } else {
            throw new IllegalArgumentException("NBTTag '" + key.getNbtName() + "' is not a LongTag.");
        }
        return this;
    }

    /** Sets the value of a FloatTag using its StructureDataKey and returns itself for chaining. */
    public StructureBlockEntity setFloat(StructureDataKey key, float value) {
        NBTTag tag = get(key.getNbtName());
        if (tag instanceof FloatTag) {
            ((FloatTag) tag).setValue(value);
        } else {
            throw new IllegalArgumentException("NBTTag '" + key.getNbtName() + "' is not a FloatTag.");
        }
        return this;
    }
}