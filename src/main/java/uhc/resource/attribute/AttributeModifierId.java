package uhc.resource.attribute;

/**
 * 🆔 **Attribute Modifier Identifier**
 * <p>
 * Unique identifiers for attribute modifiers.
 * Prevents conflicts when multiple modifiers are applied to the same attribute.
 * </p>
 */
public enum AttributeModifierId {
    // --- ⚔️ Combat ---
    BASE_ATTACK_DAMAGE("base_attack_damage"),
    BASE_ATTACK_SPEED("base_attack_speed"),
    UHC_WEAPON_BONUS("uhc.weapon_bonus"),

    // --- ❤️ Survival ---
    BASE_MAX_HEALTH("base_health"),
    UHC_HEALTH_BOOST("uhc.health_boost"),

    // --- 🏃 Movement ---
    BASE_MOVEMENT_SPEED("base_movement_speed"),
    UHC_SPEED_BUFF("uhc.speed_buff"),

    // --- 🛡️ Protection ---
    BASE_ARMOR("base_armor"),
    BASE_ARMOR_TOUGHNESS("base_armor_toughness"),
    BASE_KNOCKBACK_RESISTANCE("base_knockback_resistance");

    private final String id;

    AttributeModifierId(String id) {
        this.id = id;
    }

    /**
     * Returns the raw ID string used in NBT.
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getId();
    }
}