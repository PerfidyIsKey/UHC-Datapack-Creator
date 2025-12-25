package uhc.resource.particle;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;

import java.util.Objects;

/**
 * ✨ **Particle Resource Identifier**
 * <p>
 * Defines the types of particle identifiers available in Minecraft.
 * This enum implements {@link ResourceLocation} to integrate with the
 * command and NBT serialization systems.
 * </p>
 */
public enum ParticleId implements ResourceLocation {

    // --- 🏷️ Enum Constants ---

    /** The standard vanilla dust particle. */
    DUST,

    /** The standard vanilla flame particle. */
    FLAME,

    /** A custom sparkling effect belonging to the project namespace. */
    CUSTOM_SPARKLE(DatapackConfig.CUSTOM_NAMESPACE);

    // --- ⚙️ State & Fields ---

    /** * The namespace part of the resource location (e.g., "minecraft"). */
    private final String namespace;

    /** * The path part of the resource location (e.g., "dust"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Particle Constructor**
     * <p>Automatically uses the vanilla namespace and converts the enum name
     * to a lowercase path.</p>
     */
    ParticleId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Allows for custom namespaces (e.g., {@code uhc_core_pack}) while
     * auto-pathing the name based on the enum constant.</p>
     * * @param namespace The resource namespace to assign.
     * @throws NullPointerException if the namespace is null.
     */
    ParticleId(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = this.name().toLowerCase();
        this.validate();
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for a fully unique path and namespace combination.</p>
     * * @param path      The specific particle path.
     * @param namespace The resource namespace.
     * @throws NullPointerException if path or namespace is null.
     */
    ParticleId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase();
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace component.
     * @return The assigned namespace string.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path component.
     * @return The lowercase path string.
     */
    public String getPath() {
        return path;
    }

    /**
     * Generates the full, formatted namespaced identifier.
     * <p><b>Format:</b> {@code namespace:path}</p>
     * * @return The full resource location (e.g., "minecraft:flame").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    // --- 📝 Serialization ---

    /**
     * Returns the full resource location string for command assembly.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}