package uhc.resource.attribute;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;

import java.util.Objects;

/**
 * 📊 **Attribute Resource Identifier**
 * <p>
 * Defines the types of attribute identifiers available in Minecraft, used primarily
 * in {@code attribute_modifiers} components or the {@code /attribute} command.
 * </p>
 * <p>
 * This registry merges legacy generic paths with modern player-specific attributes
 * and custom project-defined values.
 * </p>
 */
public enum AttributeId implements ResourceLocation {

    // --- ⚔️ Combat & Physical ---

    /** Increases or decreases the base damage dealt by attacks. */
    ATTACK_DAMAGE("generic.attack_damage"),

    /** Determines the recovery rate of the attack strength meter. */
    ATTACK_SPEED("generic.attack_speed"),

    /** Increases the distance entities are pushed back when hit. */
    ATTACK_KNOCKBACK("generic.attack_knockback"),

    /** Reduces the distance the holder is pushed back when hit. */
    KNOCKBACK_RESISTANCE("generic.knockback_resistance"),

    /** Modifies the physical size of the entity (Minecraft 1.20.5+). */
    SCALE("generic.scale"),

    // --- ❤️ Survival & Defense ---

    /** Determines the maximum health capacity of the entity. */
    MAX_HEALTH("generic.max_health"),

    /** Provides base protection against incoming damage. */
    ARMOR("generic.armor"),

    /** Increases armor effectiveness against high-damage attacks. */
    ARMOR_TOUGHNESS("generic.armor_toughness"),

    /** Influences the quality of loot from tables and fishing. */
    LUCK("generic.luck"),

    // --- 🏃 Movement ---

    /** Modifies the horizontal movement speed on land. */
    MOVEMENT_SPEED("generic.movement_speed"),

    /** Modifies movement speed while flying (e.g., Creative or Elytra). */
    FLYING_SPEED("generic.flying_speed"),

    /** Modifies movement speed while sneaking. */
    SNEAKING_SPEED("generic.sneaking_speed"),

    /** Determines the maximum height of blocks the entity can step over. */
    STEP_HEIGHT("generic.step_height"),

    /** Modifies the height/strength of a jump. */
    JUMP_STRENGTH("generic.jump_strength"),

    // --- 🛠️ Player-Specific Interaction ---

    /** The maximum distance a player can reach to break or place blocks. */
    BLOCK_INTERACTION_RANGE("player.block_interaction_range"),

    /** The maximum distance a player can reach to attack or interact with entities. */
    ENTITY_INTERACTION_RANGE("player.entity_interaction_range"),

    /** Modifies the speed at which a player breaks blocks. */
    MINING_EFFICIENCY("player.mining_efficiency"),

    /** Modifies mining speed while underwater. */
    SUBMERGED_MINING_SPEED("player.submerged_mining_speed"),

    /** Determines the percentage of damage dealt by sweeping attacks. */
    SWEEPING_DAMAGE_RATIO("player.sweeping_damage_ratio"),

    // --- 🛰️ Custom Project Attributes ---

    /** Custom attribute defining the range for waypoint data transmission. */
    WAYPOINT_TRANSMIT_RANGE("waypoint_transmit_range", DatapackConfig.CUSTOM_NAMESPACE);

    // --- ⚙️ State & Fields ---

    /** * The namespace part of the resource location (e.g., "minecraft" or "uhc_core_pack"). */
    private final String namespace;

    /** * The path part of the resource location (e.g., "generic.max_health"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Vanilla Constructor**
     * <p>Assigns the default Minecraft namespace to the provided path.</p>
     * * @param path The specific attribute path string (e.g., "generic.armor").
     */
    AttributeId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = path;
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Allows for custom namespaces while providing a specific path.</p>
     * <p><b>Error Catching:</b> Validates that the inputs are non-null and
     * adhere to the resource location syntax via {@link #validate()}.</p>
     * * @param path      The attribute path.
     * @param namespace The resource namespace to assign.
     * @throws NullPointerException if path or namespace is null.
     */
    AttributeId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = Objects.requireNonNull(path, "Path cannot be null");
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace component of this attribute.
     * @return The assigned namespace string.
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path component of this attribute.
     * @return The attribute path (e.g., "generic.attack_damage").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Generates the full namespaced identifier required for NBT and commands.
     * <p><b>Catch:</b> If either component is null, this will produce a malformed string;
     * however, constructor validation prevents this state.</p>
     * * @return The full resource location (e.g., "minecraft:generic.max_health").
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