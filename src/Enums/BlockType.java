package Enums;

public enum BlockType {
    air("air"),
    amethyst_block("amethyst_block"),
    anvil("anvil"),
    apple("apple"),
    arrow("arrow"),
    barrier("barrier"),
    beacon("beacon"),
    bedrock("bedrock"),
    blaze_rod("blaze_rod"),
    bone("bone"),
    book("book"),
    bow("bow"),
    bread("bread"),
    bundle("bundle"),
    cave_air("cave_air"),
    cherry_wall_sign("cherry_wall_sign"),
    cobweb("cobweb"),
    copper_block("copper_block"),
    crossbow("crossbow"),
    diamond("diamond"),
    diamond_horse_armor("diamond_horse_armor"),
    diorite("diorite"),
    dragon_head("dragon_head"),
    egg("egg"),
    emerald_block("emerald_block"),
    ender_pearl("ender_pearl"),
    experience_bottle("experience_bottle"),
    fishing_rod("fishing_rod"),
    glass("glass"),
    glowstone_dust("glowstone_dust"),
    goat_horn("goat_horn"),
    gold_ingot("gold_ingot"),
    golden_apple("golden_apple"),
    gunpowder("gunpowder"),
    horse_spawn_egg("horse_spawn_egg"),
    ice("ice"),
    iron_axe("iron_axe"),
    iron_boots("iron_boots"),
    iron_chestplate("iron_chestplate"),
    iron_helmet("iron_helmet"),
    iron_ingot("iron_ingot"),
    iron_leggings("iron_leggings"),
    iron_sword("iron_sword"),
    jukebox("jukebox"),
    ladder("ladder"),
    lapis_lazuli("lapis_lazuli"),
    lava("lava"),
    lava_bucket("lava_bucket"),
    leather_horse_armor("leather_horse_armor"),
    melon_slice("melon_slice"),
    nether_wart("nether_wart"),
    netherite_hoe("netherite_hoe"),
    netherite_scrap("netherite_scrap"),
    netherite_upgrade_smithing_template("netherite_upgrade_smithing_template"),
    obsidian("obsidian"),
    player_head("player_head"),
    redstone("redstone"),
    redstone_block("redstone_block"),
    saddle("saddle"),
    shield("shield"),
    spectral_arrow("spectral_arrow"),
    splash_potion("splash_potion"),
    spyglass("spyglass"),
    stick("stick"),
    structure_block("structure_block"),
    tnt("tnt"),
    trident("trident"),
    void_air("void_air"),
    wind_charge("wind_charge"),
    wolf_armor("wolf_armor"),
    wolf_spawn_egg("wolf_spawn_egg"),
    written_book("written_book");

    private final String symbol;

    BlockType(String symbol) {
        this.symbol = symbol;
    }

    public String setNamespace(Namespace namespace) {
        return namespace + ":" + symbol;
    }

    public String extendColor(String color) {
        return color + "_" + symbol;
    }
}
