package uhc.resource.attribute;

import uhc.core.DatapackConfig;

/**
 * 📊 **Attribute Type Registry**
 * <p>
 * Defines the valid attribute types available in vanilla Minecraft.
 * used in "minecraft:attribute_modifiers".
 * </p>
 */
public enum AttributeType {
    // --- ⚔️ Player Combat ---
    ATTACK_DAMAGE("generic.attack_damage"),
    ATTACK_SPEED("generic.attack_speed"),
    ATTACK_KNOCKBACK("generic.attack_knockback"),

    // --- ❤️ Player Survival ---
    MAX_HEALTH("generic.max_health"),
    ARMOR("generic.armor"),
    ARMOR_TOUGHNESS("generic.armor_toughness"),
    KNOCKBACK_RESISTANCE("generic.knockback_resistance"),

    // --- 🏃 Player Movement ---
    MOVEMENT_SPEED("generic.movement_speed"),
    FLYING_SPEED("generic.flying_speed"),
    STEP_HEIGHT("generic.step_height"),
    JUMP_STRENGTH("generic.jump_strength"),

    // --- 🛠️ Utility & Interaction ---
    BLOCK_INTERACTION_RANGE("player.block_interaction_range"),
    ENTITY_INTERACTION_RANGE("player.entity_interaction_range"),
    LUCK("generic.luck"),
    MINING_EFFICIENCY("player.mining_efficiency"),
    SNEAKING_SPEED("generic.sneaking_speed"),
    SUBMERGED_MINING_SPEED("player.submerged_mining_speed"),
    SWEEPING_DAMAGE_RATIO("player.sweeping_damage_ratio");

    private final String path;
    private final String namespace;

    AttributeType(String path) {
        this.path = path;
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}