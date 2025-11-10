package ArgumentTypes;

import Enums.*;
import java.util.*;

public class BlockState {

    private final Block block;
    private final Map<BlockProperty, Object> properties = new EnumMap<>(BlockProperty.class);
    private final Map<BlockTag, Object> dataTags = new EnumMap<>(BlockTag.class);

    public BlockState(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    /** Add or update a block state property (type-checked at runtime). */
    public <T> BlockState with(BlockProperty property, T value) {
        if (!block.getValidProperties().contains(property)) {
            throw new IllegalArgumentException("Property " + property + " is not valid for block " + block);
        }

        // Null-check if you don't allow null values
        if (value == null) {
            throw new IllegalArgumentException("Null value not allowed for property " + property.getName());
        }

        // Type check at runtime
        if (!property.getType().isInstance(value)) {
            throw new IllegalArgumentException("Invalid type for property " + property.getName() +
                    ": expected " + property.getType().getSimpleName() + ", got " + value.getClass().getSimpleName());
        }

        properties.put(property, value);
        return this;
    }

    /** Add or update a data tag. */
    public BlockState with(BlockTag tag, Object value) {
        if (!block.getValidTags().contains(tag)) {
            throw new IllegalArgumentException("Tag " + tag + " is not valid for block " + block);
        }
        dataTags.put(tag, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(block.getId());

        if (!properties.isEmpty()) {
            sb.append("[");
            sb.append(properties.entrySet().stream()
                    .map(e -> e.getKey().getName() + "=" + formatValue(e.getValue()))
                    .reduce((a, b) -> a + "," + b).orElse(""));
            sb.append("]");
        }

        if (!dataTags.isEmpty()) {
            sb.append("{");
            sb.append(dataTags.entrySet().stream()
                    .map(e -> e.getKey().getName() + ":" + e.getValue())
                    .reduce((a, b) -> a + "," + b).orElse(""));
            sb.append("}");
        }

        return sb.toString();
    }

    private String formatValue(Object v) {
        // customize formatting if needed (e.g. strings quoted, enums lowercased)
        if (v instanceof String) return "\"" + v + "\"";
        if (v instanceof Enum) return ((Enum<?>) v).name().toLowerCase();
        return v.toString();
    }
}
