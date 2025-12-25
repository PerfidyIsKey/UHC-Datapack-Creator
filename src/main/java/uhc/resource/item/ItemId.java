package uhc.resource.item;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 📦 **Item Identifier Registry**
 * <p>
 * This enum serves as the central source of truth for pure Minecraft item identifiers.
 * It maps Java constants to their corresponding Minecraft resource locations.
 * </p>
 * <p>
 * <b>Note:</b> Placeable blocks (like {@code CHEST} or {@code TNT}) are excluded from
 * this registry and reside in {@link uhc.resource.block.BlockId}.
 * </p>
 */
public enum ItemId implements ItemResource {

    // --- ⚔️ Combat & Tools ---

    /** Standard projectile used by bows and dispensers. */
    ARROW,
    /** Primary ranged weapon requiring arrows to fire. */
    BOW,
    /** High-velocity ranged weapon that can be pre-loaded. */
    CROSSBOW,
    /** Tool used for fishing and pulling entities. */
    FISHING_ROD,
    /** Portable defensive equipment used to negate damage. */
    SHIELD,
    /** Specialized arrow that outlines entities with a glow effect. */
    SPECTRAL_ARROW,
    /** Handheld device used to zoom in on distant objects. */
    SPYGLASS,
    /** Rare throwable weapon found in ocean ruins. */
    TRIDENT,
    /** Throwable projectile that creates a burst of kinetic energy. */
    WIND_CHARGE,

    // --- 🛡️ Armor & Equipment ---

    /** Specialized protection for tamed wolves. */
    WOLF_ARMOR,

    // --- 🍎 Food & Potions ---

    /** Basic fruit and crafting ingredient. */
    APPLE,
    /** Common food item crafted from wheat. */
    BREAD,
    /** Can be thrown or used in baking recipes like Cake. */
    EGG,
    /** Tiered food providing Absorption and Regeneration effects. */
    GOLDEN_APPLE,
    /** Sliced melon for fast consumption. */
    MELON_SLICE,
    /** The base container for all status-effect drinks. */
    POTION,
    /** Throwable version of a potion that applies effects in a radius. */
    SPLASH_POTION,
    /** Food item that grants a random/hidden status effect. */
    SUSPICIOUS_STEW,
    /** Throwable bottle that drops experience orbs upon impact. */
    EXPERIENCE_BOTTLE,
    /** Clears all active status effects when consumed. */
    MILK_BUCKET,
    /** Used for transporting lava or as a long-lasting fuel source. */
    LAVA_BUCKET,

    // --- 💎 Materials & Crafting ---

    /** Dropped by Blazes; used for brewing and Eye of Enders. */
    BLAZE_ROD,
    /** Dropped by skeletons; used for bone meal or taming. */
    BONE,
    /** Essential for crafting enchanting tables and bookshelves. */
    BOOK,
    /** Wooden container for stews, soups, and rabbit stew. */
    BOWL,
    /** Tool for organizing and compressing inventory space. */
    BUNDLE,
    /** Highly valuable gemstone for high-tier gear and tools. */
    DIAMOND,
    /** Dropped by Endermen; used for teleportation. */
    ENDER_PEARL,
    /** Used for celebrations or flight with Elytra. */
    FIREWORK_ROCKET,
    /** Empty container for water, honey, or brewing. */
    GLASS_BOTTLE,
    /** Dust from the Nether used to enhance potion potency. */
    GLOWSTONE_DUST,
    /** Refined gold used for bartering and golden food. */
    GOLD_INGOT,
    /** Fundamental ingredient for TNT and firework stars. */
    GUNPOWDER,
    /** The primary metal for mid-tier armor and machinery. */
    IRON_INGOT,
    /** Blue mineral used for enchanting and dyes. */
    LAPIS_LAZULI,
    /** Essential fungus for starting almost all brewing recipes. */
    NETHER_WART,
    /** Raw material required to craft Netherite Ingots. */
    NETHERITE_SCRAP,
    /** Required to upgrade Diamond gear to Netherite tier. */
    NETHERITE_UPGRADE_SMITHING_TEMPLATE,
    /** The core component of Minecraft's electrical logic. */
    REDSTONE,
    /** Required to ride and control horses, pigs, and striders. */
    SADDLE,
    /** Common crafting component for tools and torches. */
    STICK,
    /** A book containing user-written or system-generated text. */
    WRITTEN_BOOK,

    // --- 🎼 Rare & Collectibles ---

    /** Musical instrument obtained from goats. */
    GOAT_HORN,
    /** One of the many collectible music tracks. */
    MUSIC_DISC_STAL,

    // --- 🥚 Creative & Technical ---

    /** Spawns a horse entity when used. */
    HORSE_SPAWN_EGG,
    /** Spawns a wolf entity when used. */
    WOLF_SPAWN_EGG,

    /** Represents the absence of an item in an inventory slot. */
    AIR;

    // --- ⚙️ State & Fields ---

    /** The namespace part of the resource location (e.g., "minecraft"). */
    private final String namespace;

    /** The path part of the resource location (e.g., "iron_sword"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Item Constructor**
     * <p>Automatically uses the default namespace and converts the enum name
     * to a lowercase path.</p>
     */
    ItemId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Allows for custom namespaces (e.g., for mods) while auto-pathing the name.</p>
     * * @param namespace The resource namespace to assign.
     * @throws NullPointerException if the namespace is null.
     */
    ItemId(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = this.name().toLowerCase();
        this.validate();
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for a fully unique path and namespace combination.</p>
     * * @param path      The specific item path.
     * @param namespace The resource namespace.
     * @throws NullPointerException if path or namespace is null.
     */
    ItemId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase();
        this.validate();
    }

    // --- 🛰️ Contract Implementation ---

    /**
     * Constructs the fully qualified Minecraft resource location.
     * * @return The formatted string (e.g., "minecraft:diamond").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 🔍 Accessors ---

    /** @return The namespace string (e.g., "minecraft"). */
    public String getNamespace() { return namespace; }

    /** @return The item path string (e.g., "apple"). */
    public String getPath() { return path; }

    // --- 🛡️ Validation & Utility ---

    /**
     * Ensures the resource location follows Minecraft's naming conventions.
     * * @throws IllegalStateException if the location is syntactically invalid.
     */
    @Override
    public void validate() throws IllegalStateException {
        ItemResource.super.validate();
    }

    /**
     * Returns the resource location for use in command strings.
     * * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}