package uhc.data.resource;

/**
 * Defines NBT keys used inside the {@code minecraft:potion_contents} component compound tag.
 * <p>
 * Converts to simple lowercase keys: {@code potion}.
 */
public enum PotionContentsKey implements NbtKey {
    /** * Specifies the base effect/tag of the potion (e.g., "minecraft:regeneration").
     * Maps to the NBT key {@code potion}.
     */
    POTION,
    CUSTOM_COLOR,
    CUSTOM_EFFECTS,
    LORE,
    CUSTOM_NAME;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}