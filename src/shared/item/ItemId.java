package shared.item;

/**
 * Defines item IDs (resource locations) used in NBT data,
 * automatically applying the 'minecraft' namespace by default.
 */
public enum ItemId {
    AIR("air"),
    CHEST("chest"),
    GOAT_HORN("goat_horn"),
    IRON_AXE("iron_axe"),
    MUSIC_DISC_STAL("music_disc_stal"),
    PLAYER_HEAD("player_head"),
    SHIELD("shield"),
    TRIDENT("trident"),
    AMETHYST_BLOCK("amethyst_block"),
    ANVIL("anvil"),
    APPLE("apple"),
    ARROW("arrow"),
    BARRIER("barrier"),
    BEACON("beacon"),
    BEDROCK("bedrock"),
    BLAZE_ROD("blaze_rod"),
    BONE("bone"),
    BOOK("book"),
    BOW("bow"),
    BOWL("bowl"),
    BREAD("bread"),
    BRICKS("bricks"),
    BUNDLE("bundle"),
    CAVE_AIR("cave_air"),
    CHERRY_WALL_SIGN("cherry_wall_sign"),
    COBWEB("cobweb"),
    COPPER_BLOCK("copper_block"),
    CROSSBOW("crossbow"),
    DIAMOND("diamond"),
    DIAMOND_HORSE_ARMOR("diamond_horse_armor"),
    DIORITE("diorite"),
    DRAGON_HEAD("dragon_head"),
    EGG("egg"),
    EMERALD_BLOCK("emerald_block"),
    ENDER_PEARL("ender_pearl"),
    EXPERIENCE_BOTTLE("experience_bottle"),
    FISHING_ROD("fishing_rod"),
    GLASS("glass"),
    GLASS_BOTTLE("glass_bottle"),
    GLOWSTONE_DUST("glowstone_dust"),
    GOLD_INGOT("gold_ingot"),
    GOLDEN_APPLE("golden_apple"),
    GUNPOWDER("gunpowder"),
    HORSE_SPAWN_EGG("horse_spawn_egg"),
    ICE("ice"),
    IRON_BOOTS("iron_boots"),
    IRON_CHESTPLATE("iron_chestplate"),
    IRON_HELMET("iron_helmet"),
    IRON_INGOT("iron_ingot"),
    IRON_LEGGINGS("iron_leggings"),
    IRON_SWORD("iron_sword"),
    JUKEBOX("jukebox"),
    LADDER("ladder"),
    LAPIS_LAZULI("lapis_lazuli"),
    LAVA("lava"),
    LAVA_BUCKET("lava_bucket"),
    LEATHER_HORSE_ARMOR("leather_horse_armor"),
    MELON_SLICE("melon_slice"),
    NETHER_WART("nether_wart"),
    NETHERITE_HOE("netherite_hoe"),
    NETHERITE_SCRAP("netherite_scrap"),
    NETHERITE_UPGRADE_SMITHING_TEMPLATE("netherite_upgrade_smithing_template"),
    OBSIDIAN("obsidian"),
    POTION("potion"),
    REDSTONE("redstone"),
    REDSTONE_BLOCK("redstone_block"),
    REINFORCED_DEEPSLATE("reinforced_deepslate"),
    SADDLE("saddle"),
    SPECTRAL_ARROW("spectral_arrow"),
    SPLASH_POTION("splash_potion"),
    SPYGLASS("spyglass"),
    STAINED_GLASS("stained_glass"),
    STICK("stick"),
    STRUCTURE_BLOCK("structure_block"),
    SUSPICIOUS_STEW("suspicious_stew"),
    TNT("tnt"),
    VOID_AIR("void_air"),
    WATER("water"),
    WIND_CHARGE("wind_charge"),
    WOLF_ARMOR("wolf_armor"),
    WOLF_SPAWN_EGG("wolf_spawn_egg"),
    WRITTEN_BOOK("written_book");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    ItemId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    ItemId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant item ID.
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Generates the ItemId resource location for a specific armor combination.
     * This avoids having to list all armor combinations explicitly in the enum.
     * @param material The armor material (e.g., DIAMOND).
     * @param piece The armor piece (e.g., HELMET).
     * @return The full resource location string (e.g., "minecraft:diamond_helmet").
     */
    public static String getArmorResourceLocation(ArmorMaterial material, ArmorPiece piece) {
        // Uses the existing DEFAULT_NAMESPACE constant for the prefix.
        // Format: minecraft:<material>_<piece>
        return DEFAULT_NAMESPACE + ":" + material.toString() + "_" + piece.toString();
    }

    /**
     * Generates the ItemId resource location for a specific tool combination.
     * This avoids having to list all tool combinations explicitly in the enum.
     * @param material The tool material (e.g., DIAMOND).
     * @param piece The tool piece (e.g., AXE).
     * @return The full resource location string (e.g., "minecraft:diamond_axe").
     */
    public static String getToolResourceLocation(ToolMaterial material, ToolPiece piece) {
        // Uses the existing DEFAULT_NAMESPACE constant for the prefix.
        // Format: minecraft:<material>_<piece>
        return DEFAULT_NAMESPACE + ":" + material.toString() + "_" + piece.toString();
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}