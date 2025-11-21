package nbt.entity.data;

import java.util.Collections;
import java.util.Map;
import nbt.tags.CompoundTag;
import shared.ItemComponentType;
import shared.item.ItemId;

/**
 * Data Transfer Object for the content of the Item CompoundTag inside the Item entity NBT.
 * Item:{id:<string>, count:<int>, components:{...}}
 */
public record ItemData(
        ItemId id,
        int count,
        // Canonical constructor field
        Map<ItemComponentType, CompoundTag> components
) {
    // 1. Canonical Constructor Body (Implicitly run after any constructor delegates to it)
    public ItemData {
        if (count <= 0) {
            count = 1;
        }
        if (components == null) throw new IllegalArgumentException("Components map cannot be null");
    }

    // 2. Secondary/Overloaded Constructor (The fix!)
    /**
     * Secondary constructor for simple items that have no custom components.
     * Delegates to the main constructor, supplying an empty map for components.
     */
    public ItemData(ItemId id, int count) {
        // Delegate to the canonical constructor (the three-argument version)
        this(id, count, Collections.emptyMap());
    }
}