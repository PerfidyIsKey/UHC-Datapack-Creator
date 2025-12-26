package uhc.resource.block;

import uhc.core.DatapackConfig;
import uhc.resource.item.ItemResource;
import java.util.Objects;

/**
 * 🧱 **Block Identifier Registry**
 * <p>
 * This registry contains a fixed list of unique Minecraft block identifiers.
 * It implements {@link BlockIdentifier} to signify it represents specific,
 * placeable blocks, and {@link ItemResource} because most blocks possess
 * a corresponding item form in player inventories.
 * </p>
 */
public enum BlockId implements BlockIdentifier, ItemResource {

    // --- 🏗️ Building & Natural Blocks ---

    /** A decorative block of purple crystals. */
    AMETHYST_BLOCK,
    /** Used for repairing and renaming items via an interface. */
    ANVIL,
    /** An invisible, unbreakable block used for world boundaries. */
    BARRIER,
    /** High-tier block used for status effects and light beams. */
    BEACON,
    /** The unbreakable bottom layer of the world. */
    BEDROCK,
    /** Standard red construction bricks crafted from clay. */
    BRICKS,
    /** A wooden storage container for items. */
    CHEST,
    /** Sticky web that slows entities; harvestable for string. */
    COBWEB,
    /** Gravity-affected block that hardens into concrete when touched by water. */
    CONCRETE_POWDER,
    /** A metallic block that changes color as it oxidizes. */
    COPPER_BLOCK,
    /** A light-colored, speckled igneous rock. */
    DIORITE,
    /** A block of pure emerald, primarily used for storage or high-value trade. */
    EMERALD_BLOCK,
    /** Transparent decorative block that allows light to pass. */
    GLASS,
    /** Frozen water block; provides a low-friction surface. */
    ICE,
    /** Redstone-interactive block used to play music discs. */
    JUKEBOX,
    /** Vertical wooden structure used for climbing. */
    LADDER,
    /** Hazardous fluid block that causes fire damage and slow movement. */
    LAVA,
    /** Extremely hard, dark volcanic glass required for Nether portals. */
    OBSIDIAN,
    /** A block providing a constant, maximum-strength redstone signal. */
    REDSTONE_BLOCK,
    /** A deepslate variant with extreme blast resistance. */
    REINFORCED_DEEPSLATE,
    /** Technical block used for saving/loading NBT-based structures. */
    STRUCTURE_BLOCK,
    /** Highly explosive block triggered by redstone, fire, or explosions. */
    TNT,
    /** Essential liquid block supporting life and farming. */
    WATER,
    STONE,
    ANDESITE,
    GRANITE,
    DEEPSLATE,

    // --- 🎼 Decorative & Heads ---

    /** A decorative head block representing a player. */
    PLAYER_HEAD,
    /** A rare trophy block found on the prow of End Ships. */
    DRAGON_HEAD,

    // --- 🌌 Technical & Environment ---

    /** Represents the total absence of a block or item. */
    AIR,
    /** Specialized air found within subterranean cave biomes. */
    CAVE_AIR,
    /** Technical air found in the void or outside world boundaries. */
    VOID_AIR,

    // --- 🧩 Custom & Modded Blocks ---

    /** General placeholder for dynamic or mod-injected blocks. */
    CUSTOM_BLOCK,
    /** Example of a block utilizing a custom namespace for UHC features. */
    LUCKY_BLOCK("lucky_block", "uhc");

    // --- ⚙️ Internal State ---

    /** The resource namespace (e.g., "minecraft" or "uhc"). */
    private final String namespace;

    /** The resource path derived from the enum name (e.g., "obsidian"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Minecraft Constructor**
     * <p>Assigns the lowercase enum name as the path and applies the standard
     * Minecraft namespace from the project configuration.</p>
     */
    BlockId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
        // Validation is handled via implementation of BlockIdentifier
    }

    /**
     * 🟡 **Custom Path Constructor**
     * <p>Uses the default namespace but allows for a specific path string
     * that may differ from the enum constant name.</p>
     * @param path The specific block path to use.
     * @throws NullPointerException if the path is null.
     */
    BlockId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase();
        this.validate();
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for a fully unique namespace and path combination,
     * useful for cross-mod compatibility or specific datapack IDs.</p>
     * @param path      The block path component.
     * @param namespace The custom namespace component.
     * @throws NullPointerException if path or namespace is null.
     */
    BlockId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase();
        this.validate();
    }

    // --- 🛰️ Resource Contract Implementation ---

    /**
     * Retrieves the namespace associated with this block.
     * @return The namespace string.
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path associated with this block.
     * @return The path string.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Combines the namespace and path into a valid Minecraft identifier.
     * @return The full identifier string (e.g., "minecraft:tnt").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 🛡️ Overrides & Validation ---

    /**
     * Performs a syntax and type check on the resource location.
     * <p><b>Error Catching:</b> Utilizes the logic in {@link BlockIdentifier}
     * to ensure this resource is not prefixed with a '#' (tag indicator).</p>
     * @throws IllegalStateException if naming conventions are violated.
     */
    @Override
    public void validate() throws IllegalStateException {
        // Triggers BlockIdentifier.super.validate() which checks for '#' tags
        BlockIdentifier.super.validate();
    }

    /**
     * Returns the identifier as a string for use in command builders.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}