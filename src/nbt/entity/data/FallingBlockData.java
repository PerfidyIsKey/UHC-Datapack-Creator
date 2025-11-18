package nbt.entity.data;

/**
 * Data Transfer Object for the Falling Block entity NBT structure.
 */
public record FallingBlockData(String blockName, String lootTable, String customName, int time, boolean dropItem,
                               String[] tags) {
}