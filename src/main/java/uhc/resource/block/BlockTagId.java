package uhc.resource.block;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🏷️ **Block Tag Identifier Registry**
 * <p>
 * Represents a fixed list of Minecraft Block Tags.
 * </p>
 */
public enum BlockTagId implements BlockResource {

    LEAVES,
    PLANKS,
    LOGS,
    WOOL,
    BASE_STONE_OVERWORLD,

    /** A custom tag example in the project namespace. */
    BLOCK_BEACON_LIGHT("block_beacon_light", DatapackConfig.CUSTOM_NAMESPACE);

    // --- ⚙️ State & Fields ---

    private final String namespace;
    private final String path;

    // --- 🏗️ Constructors ---

    BlockTagId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    BlockTagId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path).toLowerCase();
        this.validate();
    }

    BlockTagId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace).toLowerCase();
        this.path = Objects.requireNonNull(path).toLowerCase();
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    @Override public String getNamespace() { return namespace; }
    @Override public String getPath() { return path; }

    @Override
    public String getResourceLocation() {
        return "#" + namespace + ":" + path;
    }

    // --- 🛡️ Validation & Overrides ---

    @Override
    public void validate() throws IllegalStateException {
        // Correctly call the immediate parent interface's default method
        BlockResource.super.validate();
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}