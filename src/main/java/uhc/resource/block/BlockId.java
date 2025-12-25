package uhc.resource.block;

import uhc.core.DatapackConfig;
import uhc.resource.item.ItemResource;
import java.util.Objects;

/**
 * 🧱 **Block Identifier Registry**
 * <p>
 * This registry contains fixed Minecraft block identifiers. It implements both
 * {@link BlockResource} and {@link ItemResource} because these blocks typically
 * have a corresponding item form in the inventory.
 * </p>
 */
public enum BlockId implements BlockResource, ItemResource {

    // --- 🏗️ Building & Natural Blocks ---

    /** A decorative block of purple crystals. */
    AMETHYST_BLOCK,
    /** Used for repairing and renaming items. */
    ANVIL,
    /** An invisible, unbreakable block. */
    BARRIER,
    /** Standard beacon block for status effects. */
    BEACON,
    /** The unbreakable bottom layer of the world. */
    BEDROCK,
    /** Standard red construction bricks. */
    BRICKS,
    /** A wooden storage container. */
    CHEST,
    /** Slows down entities; can be harvested for string. */
    COBWEB,
    /** Gravity-affected block used for making concrete. */
    CONCRETE_POWDER,
    /** A metallic block that oxidizes over time. */
    COPPER_BLOCK,
    /** A light-colored igneous rock. */
    DIORITE,
    /** A block of pure emerald, often used for storage or trade. */
    EMERALD_BLOCK,
    /** Transparent decorative block. */
    GLASS,
    /** Frozen water block; slippery to walk on. */
    ICE,
    /** Plays music discs. */
    JUKEBOX,
    /** Allows for vertical climbing. */
    LADDER,
    /** Hazardous fluid block. */
    LAVA,
    /** Hard, dark volcanic glass used for portals. */
    OBSIDIAN,
    /** A block providing constant redstone power. */
    REDSTONE_BLOCK,
    /** A highly blast-resistant deepslate variant. */
    REINFORCED_DEEPSLATE,
    /** Used for saving and loading structures in-game. */
    STRUCTURE_BLOCK,
    /** An explosive block triggered by fire or redstone. */
    TNT,
    /** Essential liquid block. */
    WATER,

    // --- 🎼 Heads & Decorative ---

    /** A trophy or decorative head of a player. */
    PLAYER_HEAD,
    /** A rare decorative head found on End Ships. */
    DRAGON_HEAD,

    // --- 🌌 Technical & Environment ---

    /** Represents the absence of an item/block. */
    AIR,
    /** Air found specifically within cave biomes. */
    CAVE_AIR,
    /** Air found within the void or technical areas. */
    VOID_AIR,

    // --- 🧩 Custom / Modded Blocks ---

    /** Placeholder for custom or mod-added blocks. */
    CUSTOM_BLOCK,
    /** Example of a block in a custom namespace. */
    LUCKY_BLOCK("lucky_block", "uhc");

    // --- ⚙️ Internal State ---

    private final String namespace;
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Constructor**
     * <p>Assigns the lowercase enum name as the path and uses the default
     * Minecraft namespace.</p>
     */
    BlockId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    /**
     * 🟡 **Custom Path Constructor**
     */
    BlockId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase();
    }

    /**
     * 🟠 **Full Custom Constructor**
     */
    BlockId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase();
    }

    // --- 🛰️ Resource Contract Methods ---

    public String getNamespace() { return namespace; }

    public String getPath() { return path; }

    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 🛡️ Overrides & Validation ---

    @Override
    public void validate() throws IllegalStateException {
        BlockResource.super.validate();
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}