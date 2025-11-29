package shared.nbt;

/**
 * Defines NBT component keys (resource locations) used inside the 'components' compound tag (1.20.5+).
 * <p>
 * These keys are always namespace-prefixed, e.g., {@code minecraft:potion_contents} or {@code minecraft:enchantments}.
 */
public enum ComponentsKey implements NbtKey {

    /**
     * Component key for defining the enchantments applied to an item.
     * Maps to the NBT key {@code minecraft:enchantments}.
     */
    ENCHANTMENTS,

    /**
     * Component key for defining the type, color, and extended effects of a potion item.
     * Maps to the NBT key {@code minecraft:potion_contents}.
     */
    POTION_CONTENTS;

    private final String namespace;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    ComponentsKey(String namespace) {
        this.namespace = namespace;
    }

    ComponentsKey() {
        this.namespace = DEFAULT_NAMESPACE;
    }

    /**
     * Returns the full resource location string: {@code namespace:keyname}.
     * e.g., "minecraft:potion_contents".
     */
    @Override
    public String toString() {
        // Converts the constant name (e.g., POTION_CONTENTS) to lowercase (potion_contents)
        // and prefixes it with the namespace (minecraft:).
        return namespace + ":" + this.name().toLowerCase();
    }
}