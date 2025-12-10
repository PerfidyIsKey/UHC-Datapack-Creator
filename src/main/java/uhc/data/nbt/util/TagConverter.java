package uhc.data.nbt.util;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;
import uhc.data.nbt.tags.*;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class TagConverter {
    // FIX: Removed ':' from the allowed characters. This ensures namespaced keys
    // (like "minecraft:potion_contents") fail the match and are correctly quoted.
    private static final Pattern SIMPLE_KEY = Pattern.compile("^[a-zA-Z0-9_]+$");

    /**
     * Checks if a string starts and ends with brackets/braces, indicating it is raw JSON.
     * This is required for complex sign text components to avoid double-quoting.
     */
    private static boolean isRawJsonString(String s) {
        if (s == null || s.isEmpty()) return false;
        s = s.trim();
        // Check for compound {} or list [] structures
        return (s.startsWith("{") && s.endsWith("}")) || (s.startsWith("[") && s.endsWith("]"));
    }

    /**
     * Formats the key string by quoting it if it contains complex characters or a namespace.
     * @param key The NBT key name.
     * @return The formatted key (quoted or unquoted).
     */
    public static String formatKey(String key) {
        // Only quote keys that contain spaces or special characters not allowed in SNBT keys
        if (SIMPLE_KEY.matcher(key).matches()) {
            return key; // No quotes needed for keys like "id" or "Count"
        }
        // Quote complex keys like "minecraft:potion_contents" or keys with spaces.
        // Escape existing quotes within the key (if any)
        return "\"" + key.replace("\"", "\\\"") + "\"";
    }

    /**
     * Special conversion method for the root BlockState NBT to remove
     * the outermost braces, as they are added by the caller (BlockState.toString).
     */
    public static String toJsonRoot(CompoundTag compound) {
        // Re-uses the logic from the standard toJson for CompoundTag, but returns only the content
        return compound.getValue().entrySet().stream()
                // Note: Does NOT call formatKey on the top-level keys in this specific helper method.
                .map(entry -> String.format("%s:%s", entry.getKey(), toJson(entry.getValue())))
                .collect(Collectors.joining(","));
    }

    /**
     * Converts an NBTTag into the compact SNBT (Stringified NBT) representation,
     * suitable for use in Minecraft commands.
     */
    public static String toJson(NBTTag tag) {
        if (tag == null) {
            return "null";
        }

        return switch (tag.getTypeId()) {
            case TagType.TAG_COMPOUND -> {
                CompoundTag compound = (CompoundTag) tag;
                String content = compound.getValue().entrySet().stream()
                        // Calls formatKey() here to ensure inner keys are quoted correctly.
                        .map(entry -> String.format("%s:%s", formatKey(entry.getKey()), toJson(entry.getValue())))
                        .collect(Collectors.joining(","));
                yield "{" + content + "}";
            }
            case TagType.TAG_LIST -> {
                ListTag list = (ListTag) tag;
                String content = list.getElements().stream()
                        .map(TagConverter::toJson)
                        .collect(Collectors.joining(","));
                yield "[" + content + "]";
            }
            case TagType.TAG_STRING -> {
                String value = ((StringTag) tag).getValue();
                if (isRawJsonString(value)) {
                    // If the string value is already a JSON object/array, print it UNQUOTED.
                    yield value;
                } else {
                    // Standard strings must be double-quoted.
                    yield String.format("\"%s\"", value.replace("\"", "\\\""));
                }
            }
            // --- Numeric Tags ---
            case TagType.TAG_INT -> String.valueOf(((IntTag) tag).getValue());
            case TagType.TAG_BYTE -> ((ByteTag) tag).getValue() + "b";
            case TagType.TAG_SHORT -> ((ShortTag) tag).getValue() + "s";
            case TagType.TAG_LONG -> ((LongTag) tag).getValue() + "L";
            case TagType.TAG_FLOAT -> ((FloatTag) tag).getValue() + "f";
            case TagType.TAG_DOUBLE -> String.valueOf(((DoubleTag) tag).getValue());

            // --- Array Tags ---
            case TagType.TAG_BYTE_ARRAY -> {
                byte[] arr = ((ByteArrayTag) tag).getValue();
                String content = IntStream.range(0, arr.length)
                        .mapToObj(i -> arr[i] + "b")
                        .collect(Collectors.joining(","));
                yield "[B;" + content + "]";
            }
            case TagType.TAG_INT_ARRAY -> {
                int[] arr = ((IntArrayTag) tag).getValue();
                String content = Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(","));
                yield "[I;" + content + "]";
            }
            case TagType.TAG_LONG_ARRAY -> {
                long[] arr = ((LongArrayTag) tag).getValue();
                String content = Arrays.stream(arr).mapToObj(v -> v + "L").collect(Collectors.joining(","));
                yield "[L;" + content + "]";
            }

            default -> "null";
        };
    }
}