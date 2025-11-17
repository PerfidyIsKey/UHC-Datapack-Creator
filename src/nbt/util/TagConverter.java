package nbt.util;

import nbt.NBTTag;
import nbt.tags.CompoundTag;
import nbt.tags.ListTag;
import nbt.tags.StringTag;
import nbt.tags.IntTag;
import nbt.tags.ByteTag;
import nbt.tags.ShortTag;
import nbt.tags.LongTag;
import nbt.tags.FloatTag;
import nbt.tags.DoubleTag;
import nbt.tags.ByteArrayTag;
import nbt.tags.IntArrayTag;
import nbt.tags.LongArrayTag;
import java.util.stream.Collectors;
import java.util.stream.IntStream; // Required for byte[] iteration
import java.util.Arrays;

public final class TagConverter {

    /**
     * Checks if a string starts and ends with brackets/braces, indicating it is raw JSON.
     * This is required for complex sign text components to avoid double-quoting.
     */
    private static boolean isRawJsonString(String s) {
        if (s == null || s.isEmpty()) return false;
        s = s.trim();
        return (s.startsWith("{") && s.endsWith("}")) || (s.startsWith("[") && s.endsWith("]"));
    }

    /**
     * Special conversion method for the root BlockState NBT to remove
     * the outermost braces, as they are added by the caller (BlockState.toString).
     * This method assumes the input is a CompoundTag.
     */
    public static String toJsonRoot(CompoundTag compound) {
        // Re-uses the logic from the standard toJson for CompoundTag, but returns only the content
        return compound.getValue().entrySet().stream()
                .map(entry -> String.format("%s:%s", entry.getKey(), toJson(entry.getValue())))
                .collect(Collectors.joining(","));
    }

    /**
     * Converts an NBT NBTTag into the compact SNBT (Stringified NBT) representation.
     */
    public static String toJson(NBTTag tag) {
        if (tag instanceof CompoundTag compound) {
            String content = compound.getValue().entrySet().stream()
                    .map(entry -> String.format("%s:%s", entry.getKey(), toJson(entry.getValue())))
                    .collect(Collectors.joining(","));
            return "{" + content + "}";
        }
        else if (tag instanceof ListTag list) {
            String content = list.getElements().stream()
                    .map(TagConverter::toJson)
                    .collect(Collectors.joining(","));
            return "[" + content + "]";
        }
        else if (tag instanceof StringTag) {
            String value = ((StringTag) tag).getValue();

            if (isRawJsonString(value)) {
                // If the string value is already a JSON object/array, print it UNQUOTED.
                return value;
            } else {
                // Otherwise, it is a standard string and must be quoted.
                return String.format("\"%s\"", value);
            }
        }
        else if (tag instanceof IntTag) {
            return String.valueOf(((IntTag) tag).getValue());
        }
        else if (tag instanceof ByteTag) {
            return String.valueOf(((ByteTag) tag).getValue()) + "b";
        }
        else if (tag instanceof ShortTag) {
            return String.valueOf(((ShortTag) tag).getValue());
        }
        else if (tag instanceof LongTag) {
            return String.valueOf(((LongTag) tag).getValue()) + "L";
        }
        else if (tag instanceof FloatTag) {
            return String.valueOf(((FloatTag) tag).getValue()) + "f";
        }
        else if (tag instanceof DoubleTag) {
            return String.valueOf(((DoubleTag) tag).getValue());
        }
        else if (tag instanceof ByteArrayTag) {
            byte[] arr = ((ByteArrayTag) tag).getValue();
            return "[" + IntStream.range(0, arr.length)
                    .mapToObj(i -> arr[i] + "b")
                    .collect(Collectors.joining(",")) + "]";
        }
        else if (tag instanceof IntArrayTag) {
            int[] arr = ((IntArrayTag) tag).getValue();
            return "[" + Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(",")) + "]";
        }
        else if (tag instanceof LongArrayTag) {
            long[] arr = ((LongArrayTag) tag).getValue();
            return "[" + Arrays.stream(arr).mapToObj(v -> v + "L").collect(Collectors.joining(",")) + "]";
        }

        return "null";
    }
}