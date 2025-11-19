package nbt.entity.data;

import java.util.Map;
import nbt.tags.CompoundTag;
import shared.ItemComponentType;
import shared.ItemId;

/**
 * Data Transfer Object for the content of the Item CompoundTag inside the Item entity NBT.
 * Item:{id:<string>, count:<int>, components:{...}}
 */
public record ItemData(
        ItemId id,
        int count,
        // Map of component key (e.g., "minecraft:profile") to its NBT data object
        Map<ItemComponentType, CompoundTag> components
) {
    public ItemData {
        if (count <= 0) {
            count = 1;
        }
    }
}