package uhc.data.nbt.blockentity.traits;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.LongTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.loot_table.LootTableId;

import java.util.Objects;

/**
 * 💰 **Lootable NBT Trait**
 * <p>
 * This trait is applied to Block Entities that generate their contents dynamically
 * via Loot Tables (e.g., Chests, Barrels, Dispensers).
 * </p>
 * <p>
 * <b>NBT Lifecycle:</b> When a player (or world-gen) first triggers the container,
 * Minecraft reads the {@code LootTable} and {@code LootTableSeed} tags, generates
 * the items, and then <b>permanently deletes</b> these tags from the block entity.
 * </p>
 * @param <T> The type of the builder for fluent chaining.
 */
public interface LootableNBT<T extends LootableNBT<T>> {

    /** * @return The underlying root tag of the builder where NBT data is stored.
     */
    CompoundTag root();

    /**
     * Sets the Loot Table that will populate this container.
     * <p>
     * <b>NBT Key:</b> {@code LootTable} <br>
     * <b>NBT Type:</b> {@link StringTag}
     * </p>
     * @param lootTable The {@link LootTableId} representing the table's resource location.
     * @return {@code (T)} The current builder instance.
     * @throws NullPointerException if the lootTable is null.
     */
    @SuppressWarnings("unchecked")
    default T lootTable(LootTableId lootTable) {
        Objects.requireNonNull(lootTable, "LootTableId cannot be null.");
        root().put(new StringTag("LootTable", lootTable.getResourceLocation()));
        return (T) this;
    }

    /**
     * Sets a specific seed for the Loot Table generation to ensure deterministic results.
     * <p>
     * <b>NBT Key:</b> {@code LootTableSeed} <br>
     * <b>NBT Type:</b> {@link LongTag}
     * </p>
     * @param seed The long seed. Use {@code 0} or omit for random generation.
     * @return {@code (T)} The current builder instance.
     */
    @SuppressWarnings("unchecked")
    default T lootTableSeed(long seed) {
        // Minecraft uses 0 as 'no seed' (random), but explicitly providing
        // 0 is safer for NBT consistency.
        root().put(new LongTag("LootTableSeed", seed));
        return (T) this;
    }

    /**
     * Removes all loot table related data from this builder.
     * This is useful if you decide to fill the container with manual items instead.
     * @return {@code (T)} The current builder instance.
     */
    @SuppressWarnings("unchecked")
    default T removeLootTable() {
        root().remove("LootTable");
        root().remove("LootTableSeed");
        return (T) this;
    }
}