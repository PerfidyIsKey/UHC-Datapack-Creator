package uhc.data.resource;

/**
 * Defines the top-level NBT key used directly under the Entity's root tag
 * when applying item filters to a player.
 * <p>
 * Converts to simple lowercase keys: {@code selected_item}.
 */
public enum ItemNbtKey implements NbtKey {
    /** * Key used to filter by the item held in the player's selected hotbar slot.
     * Maps to the NBT key {@code SelectedItem}.
     */
    SELECTED_ITEM("SelectedItem");

    private final String key;
    ItemNbtKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}