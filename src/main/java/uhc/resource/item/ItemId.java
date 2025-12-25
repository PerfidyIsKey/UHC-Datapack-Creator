package uhc.resource.item;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 📦 **Item Identifier Registry**
 * <p>
 * This enum acts as a central registry for Minecraft item identifiers. It implements
 * {@link ItemResource}, allowing it to be used in any method requiring a type-safe
 * item reference.
 * </p>
 * <p>
 * Constant names are automatically converted to lowercase paths (e.g., {@code IRON_AXE}
 * becomes {@code "iron_axe"}).
 * </p>
 */
public enum ItemId implements ItemResource {

    // --- 🏗️ Building & Natural Blocks ---

    /** A decorative block of purple crystals. */
    AMETHYST_BLOCK,
    /** Used for repairing and renaming items. */
    ANVIL,
    /** An invisible, unbreakable block. */
    BARRIER,
    /** The unbreakable bottom layer of the world. */
    BEDROCK,
    /** Standard red construction bricks. */
    BRICKS,
    /** A wooden storage container. */
    CHEST,
    /** Slows down entities; can be harvested for string. */
    COBWEB,
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
    /** Colored transparent block. */
    STAINED_GLASS,
    /** Used for saving and loading structures in-game. */
    STRUCTURE_BLOCK,
    /** An explosive block triggered by fire or redstone. */
    TNT,
    /** Essential liquid block. */
    WATER,

    // --- ⚔️ Tools, Armor & Combat ---

    /** Standard projectile for bows. */
    ARROW,
    /** Ranged weapon using arrows. */
    BOW,
    /** High-damage ranged weapon with a loading phase. */
    CROSSBOW,
    /** Used for catching fish and other items. */
    FISHING_ROD,
    /** Woodcutting tool and heavy weapon. */
    IRON_AXE,
    /** Standard melee weapon. */
    IRON_SWORD,
    /** High-tier farming and tilling tool. */
    NETHERITE_HOE,
    /** Defensive equipment used to block attacks. */
    SHIELD,
    /** Highlights hit targets with an outline. */
    SPECTRAL_ARROW,
    /** Used for magnifying distant views. */
    SPYGLASS,
    /** Throwable weapon found in underwater ruins. */
    TRIDENT,
    /** Projectile that creates a burst of wind upon impact. */
    WIND_CHARGE,

    // --- 🛡️ Armor Sets ---

    /** Iron protection for the head. */
    IRON_HELMET,
    /** Iron protection for the torso. */
    IRON_CHESTPLATE,
    /** Iron protection for the legs. */
    IRON_LEGGINGS,
    /** Iron protection for the feet. */
    IRON_BOOTS,
    /** Heavy protection for tamed horses. */
    DIAMOND_HORSE_ARMOR,
    /** Basic protection for tamed horses. */
    LEATHER_HORSE_ARMOR,
    /** Specialized protection for tamed wolves. */
    WOLF_ARMOR,

    // --- 🍎 Food & Consumables ---

    /** Basic fruit food item. */
    APPLE,
    /** Baked wheat food item. */
    BREAD,
    /** Can be thrown or used in baking recipes. */
    EGG,
    /** Powerful food item providing absorption and regeneration. */
    GOLDEN_APPLE,
    /** Sliced fruit for quick consumption. */
    MELON_SLICE,
    /** Standard status-effect drink. */
    POTION,
    /** Throwable status-effect drink. */
    SPLASH_POTION,
    /** Food providing a hidden status effect. */
    SUSPICIOUS_STEW,
    /** Provides experience points when thrown. */
    EXPERIENCE_BOTTLE,
    /** Removes all active status effects. */
    MILK_BUCKET,
    /** Item used to transport lava. */
    LAVA_BUCKET,

    // --- 💎 Materials & Crafting ---

    /** Core material for brewing and fire charges. */
    BLAZE_ROD,
    /** Dropped by skeletons; used for bone meal or taming. */
    BONE,
    /** Used for enchanting tables and bookshelves. */
    BOOK,
    /** Container for stews and soups. */
    BOWL,
    /** Early-game inventory organization tool. */
    BUNDLE,
    /** Rare gemstone used for high-tier gear. */
    DIAMOND,
    /** Used for teleportation. */
    ENDER_PEARL,
    /** Used for flight with Elytra or celebrations. */
    FIREWORK_ROCKET,
    /** Container for water, honey, or potions. */
    GLASS_BOTTLE,
    /** Material for crafting lights and enhancing potions. */
    GLOWSTONE_DUST,
    /** Precious metal used for trade and tools. */
    GOLD_INGOT,
    /** The primary ingredient in explosives. */
    GUNPOWDER,
    /** The most common material for mid-tier gear. */
    IRON_INGOT,
    /** Mineral used for blue dye and enchanting. */
    LAPIS_LAZULI,
    /** Fungus required for almost all brewing. */
    NETHER_WART,
    /** Material used to forge Netherite Ingots. */
    NETHERITE_SCRAP,
    /** Required to upgrade diamond gear at a smithing table. */
    NETHERITE_UPGRADE_SMITHING_TEMPLATE,
    /** Fundamental logic and power component. */
    REDSTONE,
    /** Required to ride certain animals. */
    SADDLE,
    /** Basic crafting component for tools. */
    STICK,
    /** A book containing player-written text. */
    WRITTEN_BOOK,

    // --- 🎼 Miscellaneous & Rare ---

    /** An instrument dropped by goats. */
    GOAT_HORN,
    /** A specific music disc track. */
    MUSIC_DISC_STAL,
    /** A trophy or decorative head of a player. */
    PLAYER_HEAD,
    /** A rare decorative head found on End Ships. */
    DRAGON_HEAD,

    // --- 🥚 Spawn Eggs ---

    /** Creative item to spawn a horse. */
    HORSE_SPAWN_EGG,
    /** Creative item to spawn a wolf. */
    WOLF_SPAWN_EGG,

    // --- 🌌 Technical & Environment ---

    /** Represents the absence of an item. */
    AIR,
    /** Air found specifically within cave biomes. */
    CAVE_AIR,
    /** Air found within the void or technical areas. */
    VOID_AIR,

    // --- 🧩 Custom Blocks/Modded ---

    /** Placeholder for custom or mod-added blocks. */
    CUSTOM_BLOCK;

    // --- ⚙️ State & Internal Fields ---

    /** The namespace of the resource (e.g., "minecraft"). */
    private final String namespace;

    /** The unique path of the resource (e.g., "iron_sword"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * Default constructor for standard Minecraft items.
     * <p>Automatically pulls the default namespace from {@link DatapackConfig#MINECRAFT_NAMESPACE}.</p>
     */
    ItemId() {
        this(DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * Constructor for items requiring a custom namespace.
     * <p><b>Error Catching:</b> Validates that the namespace is not null and triggers
     * a validation check to ensure the resulting resource location is syntactically valid.</p>
     * * @param namespace The resource namespace to use.
     * @throws NullPointerException if the provided namespace is null.
     */
    ItemId(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = this.name().toLowerCase();

        // Ensure the derived ID follows Minecraft's naming rules immediately upon creation.
        this.validate();
    }

    // --- 🔍 Accessors ---

    /** * @return The resource namespace (e.g., "minecraft").
     */
    public String getNamespace() {
        return namespace;
    }

    /** * @return The resource path derived from the enum name (e.g., "apple").
     */
    public String getPath() {
        return path;
    }

    /** * Combines namespace and path into a full Minecraft resource location string.
     * @return The formatted location string (e.g., "minecraft:iron_sword").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 🛠️ Static Utility Methods ---

    /**
     * Dynamically generates a resource location for armor pieces.
     * <p><b>Error Catching:</b> Validates that both parameters are non-null to prevent
     * generating malformed strings like "minecraft:null_null".</p>
     * * @param material The armor material (e.g., DIAMOND, IRON).
     * @param piece The armor slot (e.g., HELMET, BOOTS).
     * @return A full resource location string.
     * @throws NullPointerException if material or piece is null.
     */
    public static String getArmorResourceLocation(ArmorMaterial material, ArmorPiece piece) {
        Objects.requireNonNull(material, "Armor material cannot be null");
        Objects.requireNonNull(piece, "Armor piece cannot be null");
        return DatapackConfig.MINECRAFT_NAMESPACE + ":"
                + material.name().toLowerCase() + "_"
                + piece.name().toLowerCase();
    }

    /**
     * Dynamically generates a resource location for tool types.
     * <p><b>Error Catching:</b> Validates that both parameters are non-null.</p>
     * * @param material The tool material (e.g., WOOD, DIAMOND).
     * @param piece The tool type (e.g., SWORD, AXE).
     * @return A full resource location string.
     * @throws NullPointerException if material or piece is null.
     */
    public static String getToolResourceLocation(ToolMaterial material, ToolPiece piece) {
        Objects.requireNonNull(material, "Tool material cannot be null");
        Objects.requireNonNull(piece, "Tool piece cannot be null");
        return DatapackConfig.MINECRAFT_NAMESPACE + ":"
                + material.name().toLowerCase() + "_"
                + piece.name().toLowerCase();
    }

    /**
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}