package uhc.resource.attribute;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;

import java.util.Objects;

/**
 * 🆔 **Attribute Modifier Identifier**
 * <p>
 * Unique resource identifiers for attribute modifiers. These prevent conflicts
 * when multiple modifiers (e.g., a weapon bonus and a potion effect) are
 * applied to the same attribute on a single entity.
 * </p>
 * <p>
 * <b>Note:</b> As of Minecraft 1.20.5, these must be valid Resource Locations
 * rather than the legacy UUID format.
 * </p>
 */
public enum AttributeModifierId implements ResourceLocation {

    // --- ⚔️ Combat Modifiers ---

    /** The standard modifier for base weapon damage. */
    BASE_ATTACK_DAMAGE("base_attack_damage"),

    /** The standard modifier for base tool/weapon swing speed. */
    BASE_ATTACK_SPEED("base_attack_speed"),

    /** A custom bonus applied to UHC-specific weaponry. */
    UHC_WEAPON_BONUS("uhc.weapon_bonus", DatapackConfig.CUSTOM_NAMESPACE),

    // --- ❤️ Survival & Health ---

    /** The standard modifier for an entity's base health pool. */
    BASE_MAX_HEALTH("base_health"),

    /** A custom health increase specifically for UHC mechanics. */
    UHC_HEALTH_BOOST("uhc.health_boost", DatapackConfig.CUSTOM_NAMESPACE),

    // --- 🏃 Movement & Utility ---

    /** The standard modifier for base walking/sprinting speed. */
    BASE_MOVEMENT_SPEED("base_movement_speed"),

    /** A generic speed boost typically used for temporary buffs. */
    GENERIC_SPEED_BOOST("generic.speed_boost"),

    /** A custom speed increase for UHC events. */
    UHC_SPEED_BUFF("uhc.speed_buff", DatapackConfig.CUSTOM_NAMESPACE),

    // --- 🛡️ Protection & Gear ---

    /** Base armor value provided by items or natural scaling. */
    BASE_ARMOR("base_armor"),

    /** Modifier for the body slot specifically (e.g., horse armor or custom gear). */
    ARMOR_BODY("armor.body"),

    /** Base armor toughness for high-damage mitigation. */
    BASE_ARMOR_TOUGHNESS("base_armor_toughness"),

    /** Base resistance to being moved by attacks. */
    BASE_KNOCKBACK_RESISTANCE("base_knockback_resistance");

    // --- ⚙️ State & Fields ---

    /** * The namespace part of the resource location (e.g., "minecraft"). */
    private final String namespace;

    /** * The path part of the resource location (e.g., "base_attack_damage"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Vanilla Constructor**
     * <p>Assigns the "minecraft" namespace to the provided modifier path.</p>
     * @param path The specific modifier path string.
     */
    AttributeModifierId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = path;
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Allows for custom namespaces (e.g., for UHC-specific logic).</p>
     * @param path      The modifier path.
     * @param namespace The resource namespace to assign.
     * @throws NullPointerException if path or namespace is null.
     */
    AttributeModifierId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = Objects.requireNonNull(path, "Path cannot be null");
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace component.
     * @return The assigned namespace string (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path component.
     * @return The modifier path string (e.g., "base_health").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Generates the full namespaced identifier required for NBT.
     * @return The full resource location (e.g., "minecraft:base_health").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 📝 Serialization ---

    /**
     * Returns the full resource location string.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}