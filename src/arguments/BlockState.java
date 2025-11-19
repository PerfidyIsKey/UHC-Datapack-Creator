package arguments;

import Enums.*;
import java.util.*;

// Import the necessary NBT classes
import nbt.blockentity.BlockEntity;
import nbt.tags.CompoundTag;
import nbt.util.TagConverter;

// Import the centralized enum for the BlockState argument
import shared.StructureBlockMode;

public class BlockState {

    private final Block block;
    private final Map<BlockProperty, Object> properties = new EnumMap<>(BlockProperty.class);
    private final Map<String, BlockEntity> blockEntityData = new HashMap<>();

    public BlockState(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public <T> BlockState with(BlockProperty property, T value) {
        if (!block.getValidProperties().contains(property)) {
            throw new IllegalArgumentException("Property " + property + " is not valid for block " + block);
        }

        if (value == null) {
            throw new IllegalArgumentException("Null value not allowed for property " + property.getName());
        }

        if (!property.getType().isInstance(value)) {
            throw new IllegalArgumentException("Invalid type for property " + property.getName() +
                    ": expected " + property.getType().getSimpleName() + ", got " + value.getClass().getSimpleName());
        }

        properties.put(property, value);
        return this;
    }

    /** * Adds a specialized Block Entity (NBT data) to the block state.
     * The Block Entity's name (if set) is used as the key.
     * If the Block Entity name is missing, an empty string is used as the key
     * to indicate it should be treated as the root NBT compound without a key wrapper.
     */
    public BlockState with(BlockEntity entity) {
        String key = entity.getName() != null && !entity.getName().isEmpty()
                ? entity.getName()
                : "";

        blockEntityData.put(key, entity);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(block.getId());

        // Standard properties output
        if (!properties.isEmpty()) {
            sb.append("[");
            sb.append(properties.entrySet().stream()
                    .map(e -> e.getKey().getName() + "=" + formatValue(e.getValue()))
                    .reduce((a, b) -> a + "," + b).orElse(""));
            sb.append("]");
        }

        // Block Entity (NBT) data output
        if (!blockEntityData.isEmpty()) {
            sb.append("{");
            sb.append(blockEntityData.entrySet().stream()
                    .map(e -> {
                        // All BlockEntity implementations extend CompoundTag, so this cast is safe.
                        CompoundTag entityTag = (CompoundTag) e.getValue();

                        if (e.getKey().isEmpty()) {
                            // If it's the root entity (key == ""), use toJsonRoot to skip inner braces.
                            return TagConverter.toJsonRoot(entityTag);
                        } else {
                            // For named NBT (e.g., custom data), use standard toJson (which includes braces).
                            return e.getKey() + ":" + TagConverter.toJson(entityTag);
                        }
                    })
                    .reduce((a, b) -> a + "," + b).orElse(""));
            sb.append("}");
        }

        return sb.toString();
    }

    private String formatValue(Object v) {
        if (v instanceof StructureBlockMode) {
            // Use the method designed to return the lowercase BlockState value ("save", "load", etc.)
            return ((StructureBlockMode) v).getBlockStateValue();
        }

        if (v instanceof String) return "\"" + v + "\"";
        if (v instanceof Enum) return ((Enum<?>) v).name().toLowerCase();
        return v.toString();
    }
}