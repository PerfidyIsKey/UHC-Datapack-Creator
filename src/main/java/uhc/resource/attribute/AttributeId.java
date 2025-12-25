package uhc.resource.attribute;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;

import java.util.Objects;

/**
 * 📊 **Attribute Resource Identifier**
 * <p>
 * Defines the types of attribute identifiers available in modern Minecraft (1.20.5+).
 * Used primarily in {@code attribute_modifiers} components or the {@code /attribute} command.
 * </p>
 * <p>
 * <b>Note:</b> Legacy prefixes such as {@code generic.} and {@code player.} have been removed
 * in favor of the flattened registry names.
 * </p>
 */
public enum AttributeId implements ResourceLocation {

    // --- ⚔️ Combat & Physical ---

    /** Increases or decreases the base damage dealt by attacks. */
    ATTACK_DAMAGE,

    /** Determines the recovery rate of the attack strength meter. */
    ATTACK_SPEED,

    /** Increases the distance entities are pushed back when hit. */
    ATTACK_KNOCKBACK,

    /** Reduces the distance the holder is pushed back when hit. */
    KNOCKBACK_RESISTANCE,

    /** Modifies the physical size of the entity. */
    SCALE,

    // --- ❤️ Survival & Defense ---

    /** Determines the maximum health capacity of the entity. */
    MAX_HEALTH,

    /** Provides base protection against incoming damage. */
    ARMOR,

    /** Increases armor effectiveness against high-damage attacks. */
    ARMOR_TOUGHNESS,

    /** Influences the quality of loot from tables and fishing. */
    LUCK,

    // --- 🏃 Movement ---

    /** Modifies the horizontal movement speed on land. */
    MOVEMENT_SPEED,

    /** Modifies movement speed while flying (e.g., Creative or Elytra). */
    FLYING_SPEED,

    /** Modifies movement speed while sneaking. */
    SNEAKING_SPEED,

    /** Determines the maximum height of blocks the entity can step over. */
    STEP_HEIGHT,

    /** Modifies the height/strength of a jump. */
    JUMP_STRENGTH,

    // --- 🛠️ Player-Specific Interaction ---

    /** The maximum distance a player can reach to break or place blocks. */
    BLOCK_INTERACTION_RANGE,

    /** The maximum distance a player can reach to attack or interact with entities. */
    ENTITY_INTERACTION_RANGE,

    /** Modifies the speed at which a player breaks blocks. */
    MINING_EFFICIENCY,

    /** Modifies mining speed while underwater. */
    SUBMERGED_MINING_SPEED,

    /** Determines the percentage of damage dealt by sweeping attacks. */
    SWEEPING_DAMAGE_RATIO,

    // --- 🛰️ Custom Project Attributes ---

    /** Custom attribute defining the range for waypoint data transmission. */
    WAYPOINT_TRANSMIT_RANGE(DatapackConfig.CUSTOM_NAMESPACE);

    // --- ⚙️ State & Fields ---

    /** * The registry namespace (e.g., "minecraft" or a custom project ID). */
    private final String namespace;

    /** * The registry path (e.g., "max_health"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * **Standard Vanilla Constructor**
     * <p>Immediately uses the enum name converted to lowercase as the path
     * and assigns the default Minecraft namespace.</p>
     */
    AttributeId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
        this.validate();
    }

    /**
     * **Custom Namespace Constructor**
     * <p>Allows for custom namespaces while still deriving the path from the enum name.</p>
     * @param namespace The resource namespace to assign.
     * @throws NullPointerException if namespace is null.
     */
    AttributeId(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = this.name().toLowerCase();
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
     * @return The flattened attribute path (e.g., "attack_damage").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Generates the full namespaced identifier required for NBT components.
     * @return The full resource location (e.g., "minecraft:max_health").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * **Proactive Error Catching**
     * <p>Ensures the generated identifier matches the strict lowercase and
     * underscore pattern required by Minecraft's resource system.</p>
     * @throws IllegalStateException if the identifier is malformed.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (namespace == null || path == null) {
            throw new IllegalStateException("AttributeId components for " + this.name() + " cannot be null.");
        }
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Malformed Attribute ResourceLocation: " + getResourceLocation());
        }
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