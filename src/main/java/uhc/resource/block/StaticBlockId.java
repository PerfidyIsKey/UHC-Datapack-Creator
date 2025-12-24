package uhc.resource.block;

/**
 * An enumeration representing a fixed list of common Minecraft block IDs
 * (resource locations) that do not require runtime modifications (like color
 * or internal state) to determine their basic identifier string.
 * <p>
 * This class implements the {@link BlockId} interface to provide the fully
 * qualified resource location string (e.g., {@code minecraft:chest}).
 */
public enum StaticBlockId implements BlockId {

    // Enum constants using the default 'minecraft' namespace
    AIR("air"),
    AMETHYST_BLOCK("amethyst_block"),
    ANVIL("anvil"),
    BARRIER("barrier"),
    BEACON("beacon"),
    BEDROCK("bedrock"),
    BRICKS("bricks"),
    CAVE_AIR("cave_air"),
    CHEST("chest"),
    COBWEB("cobweb"),
    CONCRETE_POWDER("concrete_powder"),
    COPPER_BLOCK("copper_block"),
    DIORITE("diorite"),
    DRAGON_HEAD("dragon_head"),
    EMERALD_BLOCK("emerald_block"),
    GLASS("glass"),
    ICE("ice"),
    JUKEBOX("jukebox"),
    LADDER("ladder"),
    LAVA("lava"),
    OBSIDIAN("obsidian"),
    PLAYER_HEAD("player_head"),
    REDSTONE_BLOCK("redstone_block"),
    REINFORCED_DEEPSLATE("reinforced_deepslate"),
    STRUCTURE_BLOCK("structure_block"),
    TNT("tnt"),
    VOID_AIR("void_air"),
    WATER("water");

    // The block name part of the resource location (e.g., "amethyst_block").
    private final String path;
    // The namespace part of the resource location (e.g., "minecraft").
    private final String namespace;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Private constructor for blocks that require a custom namespace (e.g., modded blocks).
     *
     * @param path The block name (path) component.
     * @param namespace The custom namespace component.
     */
    StaticBlockId(String path, String namespace) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Private constructor for blocks within the default {@code minecraft} namespace.
     *
     * @param path The block name (path) component.
     */
    StaticBlockId(String path) {
        this.namespace = DEFAULT_NAMESPACE;
        this.path = path;
    }

    /**
     * Returns the fully qualified resource location of the block.
     * This combines the namespace and path with a colon separator.
     *
     * @return The immutable resource location string (e.g., {@code minecraft:tnt}).
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Returns the fully qualified resource location of the block, allowing the
     * enum constant to be seamlessly concatenated into command strings.
     *
     * @return The immutable resource location string.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}