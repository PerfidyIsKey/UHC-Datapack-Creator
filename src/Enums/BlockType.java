package Enums;

public enum BlockType {
    air("air"),
    barrier("barrier"),
    beacon("beacon"),
    bedrock("bedrock"),
    bow("bow"),
    bundle("bundle"),
    cave_air("cave_air"),
    cherry_wall_sign("cherry_wall_sign"),
    crossbow("crossbow"),
    emerald_block("emerald_block"),
    dragon_head("dragon_head"),
    glass("glass"),
    goat_horn("goat_horn"),
    golden_apple("golden_apple"),
    ice("ice"),
    iron_axe("iron_axe"),
    iron_boots("iron_boots"),
    iron_chestplate("iron_chestplate"),
    iron_helmet("iron_helmet"),
    iron_leggings("iron_leggings"),
    iron_sword("iron_sword"),
    jukebox("jukebox"),
    lava("lava"),
    leather_horse_armor("leather_horse_armor"),
    player_head("player_head"),
    redstone_block("redstone_block"),
    shield("shield"),
    splash_potion("splash_potion"),
    structure_block("structure_block"),
    trident("trident"),
    void_air("void_air"),
    wolf_armor("wolf_armor");

    private final String symbol;

    BlockType(String symbol) {
        this.symbol = symbol;
    }

    public String setNamespace(Namespace namespace) {
        return namespace + ":" + symbol;
    }
}
