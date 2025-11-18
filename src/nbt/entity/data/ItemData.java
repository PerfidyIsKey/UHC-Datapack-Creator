package nbt.entity.data;

import java.util.Map;
import nbt.tags.CompoundTag;

/**
 * Data Transfer Object for the content of the Item CompoundTag inside the Item entity NBT.
 * Item:{id:<string>, count:<int>, components:{...}}
 */
public record ItemData(
        String itemId,
        int count,
        // Map of component key (e.g., "minecraft:profile") to its NBT data object
        Map<String, CompoundTag> components
) {
    public ItemData {
        if (itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID must be specified.");
        }
        if (count <= 0) {
            count = 1;
        }
    }
}