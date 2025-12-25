package uhc.resource.block;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🏷️ **Block Tag Identifier Registry**
 * <p>
 * Represents a fixed list of Minecraft Block Tags. Tags are used primarily in
 * predicates (e.g., {@code /fill replace #logs}) to match groups of blocks.
 * </p>
 * <p>
 * All identifiers in this registry are automatically prefixed with a hash symbol ({@code #})
 * as required by Minecraft's command syntax.
 * </p>
 */
public enum BlockTagId implements BlockResource {

    // --- 🌳 Built-in Minecraft Tags ---

    /** Matches all types of leaves. */
    LEAVES,
    /** Matches all types of planks. */
    PLANKS,
    /** Matches all types of log blocks. */
    LOGS,
    /** Matches all types of wool. */
    WOOL,
    /** Matches blocks that can be replaced by ores. */
    BASE_STONE_OVERWORLD,

    // --- 🛠️ Custom / UHC Tags ---

    /** A custom tag example in the 'uhc' namespace. */
    BLOCK_BEACON_LIGHT("block_beacon_light", "uhc");

    // --- ⚙️ State & Fields ---

    /** The namespace part of the tag (e.g., "minecraft"). */
    private final String namespace;

    /** The path part of the tag (e.g., "planks"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Constructor**
     * <p>Uses the default namespace and maps the enum name to lowercase.</p>
     */
    BlockTagId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    /**
     * 🟡 **Custom Path Constructor**
     * @param path The specific tag path.
     */
    BlockTagId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path, "Tag path cannot be null").toLowerCase();
    }

    /**
     * 🟠 **Full Custom Constructor**
     * @param path      The tag path.
     * @param namespace The custom namespace.
     */
    BlockTagId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
        this.path = Objects.requireNonNull(path, "Tag path cannot be null").toLowerCase();
    }

    // --- 🛰️ BlockResource Implementation ---

    /**
     * Constructs the fully qualified resource location string, prefixed with a hash symbol.
     * <p><b>Example:</b> {@code #minecraft:planks}</p>
     * @return The formatted block tag string.
     */
    @Override
    public String getResourceLocation() {
        return "#" + namespace + ":" + path;
    }

    /**
     * Validates the tag format.
     * <p>Note: The base {@link BlockResource#validate()} handles the '#' logic.</p>
     */
    @Override
    public void validate() throws IllegalStateException {
        BlockResource.super.validate();
    }

    /**
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}