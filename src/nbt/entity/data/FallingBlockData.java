package nbt.entity.data;

import shared.StaticEntityTag;
import shared.ItemId;
import shared.LootTableId;

/**
 * Data Transfer Object for the Falling Block entity NBT structure.
 * * NOTE: The canonical constructor requires all fields. Auxiliary constructors
 * use null/default values to signal the builder what kind of NBT structure
 * to generate (Full, Simple Filter, or Empty).
 */
public record FallingBlockData(ItemId blockName, LootTableId lootTable, String customName, int time, boolean dropItem,
                               StaticEntityTag[] tags) {

    // Canonical constructor is implicitly defined.

    // --- Auxiliary Constructor for Minimal Filtering (e.g., "{Tags:["CarePackage"]}") ---
    /**
     * Creates a minimal FallingBlockData instance for entity selection/filtering.
     * All non-tag properties are set to null/default to signal the builder
     * to only create the {Tags:[...]} structure.
     */
    public FallingBlockData(StaticEntityTag[] tags) {
        // Calls the canonical constructor with nulls and defaults for unused fields.
        this(null, null, null, 0, false, tags);
    }

    // --- Auxiliary Constructor for Empty NBT ("{}") ---
    /**
     * Creates an empty FallingBlockData instance to signal the builder
     * to return an empty CompoundTag ({}).
     */
    public FallingBlockData() {
        // Calls the canonical constructor with all nulls/defaults.
        this(null, null, null, 0, false, null);
    }
}