package uhc.resource.entity;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🐾 **Entity Identifier Registry**
 * <p>
 * Defines a fixed list of Minecraft entity types.
 * </p>
 */
public enum EntityId implements EntityResource {

    // --- 🏗️ Technical & Utility Entities ---
    AREA_EFFECT_CLOUD,
    ARMOR_STAND,
    ITEM,
    MARKER,
    FALLING_BLOCK,
    FIREWORK_ROCKET,

    // --- 🐎 Passive & Tameable Mobs ---
    DOLPHIN,
    HORSE,
    WOLF;

    // --- ⚙️ State & Fields ---

    private final String namespace;
    private final String path;

    // --- 🏗️ Constructors ---

    EntityId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    EntityId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase().trim();
        this.validate();
    }

    EntityId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase().trim();
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    @Override
    public String getNamespace() { return namespace; }

    @Override
    public String getPath() { return path; }

    @Override
    public String getResourceLocation() { return namespace + ":" + path; }

    // --- 🛡️ Validation & Overrides ---

    /**
     * Validates character compliance and entity-specific syntax.
     * <p><b>Error Catching:</b> Invokes the validation logic from {@link EntityResource}
     * to prevent NBT or selector characters in registry constants.</p>
     */
    @Override
    public void validate() throws IllegalStateException {
        // Corrected reference to the immediate parent interface
        EntityResource.super.validate();
    }

    @Override
    public String toString() { return getResourceLocation(); }
}