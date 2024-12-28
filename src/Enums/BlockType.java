package Enums;

public enum BlockType {
    air,
    barrier,
    beacon,
    bedrock,
    bow,
    bundle,
    cave_air,
    cherry_wall_sign,
    crossbow,
    emerald_block,
    dragon_head,
    glass,
    golden_apple,
    ice,
    iron_axe,
    iron_boots,
    iron_chestplate,
    iron_helmet,
    iron_leggings,
    iron_sword,
    jukebox,
    lava,
    leather_horse_armor,
    player_head,
    redstone_block,
    shield,
    splash_potion,
    structure_block,
    trident,
    void_air,
    wolf_armor;

    public static String setNamespace(Namespace namespace, BlockType block) {
        return namespace + ":" + block;
    }
}
