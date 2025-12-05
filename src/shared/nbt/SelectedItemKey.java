package shared.nbt;

/**
 * Defines NBT keys used inside the 'SelectedItem' compound tag in the entity NBT filter.
 * <p>
 * Converts to simple lowercase keys: {@code id}, {@code count}, {@code components}.
 */
public enum SelectedItemKey implements NbtKey {
    /** * The item's Minecraft resource location (e.g., "minecraft:diamond_sword").
     * Maps to the NBT key {@code id}.
     */
    ID,

    /** * The number of items in the stack.
     * Maps to the NBT key {@code count}.
     */
    COUNT,

    /** * The container for version 1.20.5+ item component data.
     * Maps to the NBT key {@code components}.
     */
    COMPONENTS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}