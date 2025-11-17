package Enums;

import java.util.EnumSet;

public enum Block {
    AIR("air"),
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
    CHERRY_WALL_SIGN(
            "cherry_wall_sign",
            EnumSet.of(
                    BlockProperty.FACING,
                    BlockProperty.WATERLOGGED
                    )
    ),
    CHEST("chest"),
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
    GOAT_HORN("goat_horn"),
    GOLD_INGOT("gold_ingot"),
    GOLDEN_APPLE("golden_apple"),
    GUNPOWDER("gunpowder"),
    HORSE_SPAWN_EGG("horse_spawn_egg"),
    ICE("ice"),
    IRON_AXE("iron_axe"),
    IRON_BOOTS("iron_boots"),
    IRON_CHESTPLATE("iron_chestplate"),
    IRON_HELMET("iron_helmet"),
    IRON_INGOT("iron_ingot"),
    IRON_LEGGINGS("iron_leggings"),
    IRON_SWORD("iron_sword"),
    JUKEBOX(
            "jukebox",
            EnumSet.of(BlockProperty.HAS_RECORD)
    ),
    LADDER("ladder"),
    LAPIS_LAZULI("lapis_lazuli"),
    LAVA("lava"),
    LAVA_BUCKET("lava_bucket"),
    LEATHER_HORSE_ARMOR("leather_horse_armor"),
    MELON_SLICE("melon_slice"),
    MUSIC_DISC_STAL("music_disc_stal"),
    NETHER_WART("nether_wart"),
    NETHERITE_HOE("netherite_hoe"),
    NETHERITE_SCRAP("netherite_scrap"),
    NETHERITE_UPGRADE_SMITHING_TEMPLATE("netherite_upgrade_smithing_template"),
    OBSIDIAN("obsidian"),
    PLAYER_HEAD("player_head"),
    POTION("potion"),
    REDSTONE("redstone"),
    REDSTONE_BLOCK("redstone_block"),
    REINFORCED_DEEPSLATE("reinforced_deepslate"),
    SADDLE("saddle"),
    SHIELD("shield"),
    SPECTRAL_ARROW("spectral_arrow"),
    SPLASH_POTION("splash_potion"),
    SPYGLASS("spyglass"),
    STAINED_GLASS("stained_glass"),
    STICK("stick"),
    STRUCTURE_BLOCK(
            "structure_block",
            EnumSet.of(BlockProperty.MODE)
            ),
    SUSPICIOUS_STEW("suspicious_stew"),
    TNT("tnt"),
    TRIDENT("trident"),
    VOID_AIR("void_air"),
    WATER("water"),
    WIND_CHARGE("wind_charge"),
    WOLF_ARMOR("wolf_armor"),
    WOLF_SPAWN_EGG("wolf_spawn_egg"),
    WRITTEN_BOOK("written_book");

    private final String id;
    private EnumSet<BlockProperty> validProperties;
    private EnumSet<BlockTag> validTags;

    Block(String id) {
        this.id = id;
    }

    Block(String id, EnumSet<BlockProperty> validProperties, EnumSet<BlockTag> validTags) {
        this.id = id;
        this.validProperties = validProperties;
        this.validTags = validTags;
    }

    Block(String id, EnumSet<BlockProperty> validProperties) {
        this.id = id;
        this.validProperties = validProperties;
    }

    public String getId() {
        return id;
    }

    public EnumSet<BlockProperty> getValidProperties() {
        return validProperties;
    }

    public EnumSet<BlockTag> getValidTags() {
        return validTags;
    }

    public String setNamespace(Namespace namespace) {
        return namespace + ":" + id;
    }

    public String extendColor(String color) {
        return "minecraft:" + color + "_" + id;
    }

    public String addNBT(String nbt) { return "minecraft:" + id + nbt; }

    @Override
    public String toString() {
        return "minecraft:" + id;
    }
}
