package nbt.entity.data;

import shared.StaticEntityTag;
import shared.ItemId;
import shared.LootTableId;

/**
 * Data Transfer Object for the Falling Block entity NBT structure.
 */
public record FallingBlockData(ItemId blockName, LootTableId lootTable, String customName, int time, boolean dropItem,
                               StaticEntityTag[] tags) {
}