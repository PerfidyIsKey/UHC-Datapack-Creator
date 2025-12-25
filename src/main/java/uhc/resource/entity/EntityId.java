package uhc.resource.entity;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🐾 **Entity Identifier Registry**
 * <p>
 * Defines a fixed list of Minecraft entity types. This registry maps Java constants
 * to their corresponding namespaced identifiers used in summoning, predicates,
 * and NBT data.
 * </p>
 */
public enum EntityId implements EntityResource {

    // --- 🏗️ Technical & Utility Entities ---

    /** Used for area-of-effect potions and lingering clouds. */
    AREA_EFFECT_CLOUD,
    /** A gravity-affected entity used to display armor and items. */
    ARMOR_STAND,
    /** Represents an item dropped on the ground. */
    ITEM,
    /** A server-side only entity used for marking locations without client overhead. */
    MARKER,
    /** A technical entity representing a block currently falling through the air. */
    FALLING_BLOCK,
    /** The entity form of a firework launched into the sky. */
    FIREWORK_ROCKET,

    // --- 🐎 Passive & Tameable Mobs ---

    /** A tameable aquatic mammal. */
    DOLPHIN,
    /** A mountable mob used for transport. */
    HORSE,
    /** A tameable mob that can be equipped with wolf armor. */
    WOLF;

    // --- ⚙️ State & Fields ---

    /** The namespace of the entity (e.g., "minecraft"). */
    private final String namespace;

    /** The path component of the entity (e.g., "armor_stand"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Constructor**
     * <p>Automatically maps the enum name to lowercase and applies the
     * default Minecraft namespace from {@link DatapackConfig}.</p>
     */
    EntityId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    /**
     * 🟡 **Custom Path Constructor**
     * <p>Uses the default namespace but allows a manual path string.</p>
     * @param path The specific entity path.
     */
    EntityId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path, "Entity path cannot be null").toLowerCase();
        this.validate();
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for custom namespaces (e.g., for modded entities).</p>
     * @param path      The entity path.
     * @param namespace The custom namespace.
     */
    EntityId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
        this.path = Objects.requireNonNull(path, "Entity path cannot be null").toLowerCase();
        this.validate();
    }

    // --- 🛰️ EntityResource Implementation ---

    /**
     * Retrieves the fully qualified resource location for the entity.
     * @return The formatted location string (e.g., "minecraft:wolf").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 🔍 Accessors ---

    /** @return The namespace string (e.g., "minecraft"). */
    public String getNamespace() {
        return namespace;
    }

    /** @return The entity path string (e.g., "armor_stand"). */
    public String getPath() {
        return path;
    }

    // --- 🛡️ Validation & Overrides ---

    /**
     * Validates that the entity identifier follows Minecraft's naming rules.
     * @throws IllegalStateException if the ID is syntactically incorrect.
     */
    @Override
    public void validate() throws IllegalStateException {
        EntityResource.super.validate();
    }

    /**
     * Returns the identifier as a string for use in commands or logging.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}