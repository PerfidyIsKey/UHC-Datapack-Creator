package nbt.util;

import nbt.NBTTag;
import nbt.TagType;
import nbt.tags.*;

public final class TagFactory {
    public static NBTTag create(byte type) {
        return switch (type) {
            case TagType.TAG_BYTE -> new ByteTag();
            case TagType.TAG_SHORT -> new ShortTag();
            case TagType.TAG_INT -> new IntTag();
            case TagType.TAG_LONG -> new LongTag();
            case TagType.TAG_FLOAT -> new FloatTag();
            case TagType.TAG_DOUBLE -> new DoubleTag();
            case TagType.TAG_BYTE_ARRAY -> new ByteArrayTag();
            case TagType.TAG_STRING -> new StringTag();
            case TagType.TAG_LIST -> new ListTag();
            case TagType.TAG_COMPOUND -> new CompoundTag();
            case TagType.TAG_INT_ARRAY -> new IntArrayTag();
            case TagType.TAG_LONG_ARRAY -> new LongArrayTag();
            case TagType.TAG_END -> new EndTag();
            default -> throw new IllegalArgumentException("Unknown tag type: " + type);
        };
    }
}