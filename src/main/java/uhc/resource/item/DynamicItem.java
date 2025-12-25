package uhc.resource.item;

import uhc.core.DatapackConfig;
import uhc.resource.color.DyeColor;

import java.util.Objects;

/**
 * 🛠️ **Dynamic Item Factory**
 * <p>
 * This class provides type-safe, static factory methods to generate {@link ItemResource}
 * identifiers for item combinations and custom-namespaced resources that are not
 * explicitly registered in the {@link ItemId} enum.
 * </p>
 */
public final class DynamicItem implements ItemResource {

    // --- ⚙️ State & Fields ---

    /** * The namespace part of the resource location (e.g., "minecraft"). */
    private final String namespace;

    /** * The path part of the resource location (e.g., "diamond_sword"). */
    private final String path;

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to enforce controlled instantiation through static factory methods.
     * <p><b>Error Catching:</b> Immediately triggers {@link #validate()} to ensure
     * the components form a syntactically valid Minecraft resource location.</p>
     * * @param namespace The resource namespace.
     * @param path      The resource path.
     * @throws IllegalStateException if the location fails naming convention validation.
     */
    private DynamicItem(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase().trim();

        // Immediate fail-fast validation to prevent invalid IDs from entering the system
        this.validate();
    }

    // --- 🎨 Colorable Factories ---

    /**
     * Creates a colored item identifier (e.g., "minecraft:yellow_bundle") using registry templates.
     * * @param color The {@link DyeColor} to apply.
     * @param item  The {@link ColorableItem} template.
     * @return A validated {@link ItemResource} for the colored item.
     * @throws NullPointerException if color or item template is null.
     */
    public static ItemResource color(DyeColor color, ColorableItem item) {
        Objects.requireNonNull(color, "DyeColor cannot be null.");
        Objects.requireNonNull(item, "ColorableItem template cannot be null.");

        return item.withColor(color);
    }

    // --- 🛡️ Armor Factories ---

    /**
     * Generates a type-safe armor resource using the default Minecraft namespace.
     * * @param material The {@link ArmorMaterial} object.
     * @param piece    The {@link ArmorPiece} object.
     * @return A validated {@link ItemResource} for the armor piece.
     */
    public static ItemResource armor(ArmorMaterial material, ArmorPiece piece) {
        return armor(DatapackConfig.MINECRAFT_NAMESPACE, material, piece);
    }

    /**
     * Generates an armor resource with a custom namespace.
     * * @param namespace The custom namespace string.
     * @param material  The {@link ArmorMaterial} object.
     * @param piece     The {@link ArmorPiece} object.
     * @return A validated {@link ItemResource}.
     */
    public static ItemResource armor(String namespace, ArmorMaterial material, ArmorPiece piece) {
        Objects.requireNonNull(material, "Armor material cannot be null.");
        Objects.requireNonNull(piece, "Armor piece cannot be null.");

        String path = material.name().toLowerCase() + "_" + piece.name().toLowerCase();
        return new DynamicItem(namespace, path);
    }

    // --- ⚒️ Tool Factories ---

    /**
     * Generates a type-safe tool resource using the default Minecraft namespace.
     * * @param material The {@link ToolMaterial} object.
     * @param piece    The {@link ToolPiece} object.
     * @return A validated {@link ItemResource} for the tool.
     */
    public static ItemResource tool(ToolMaterial material, ToolPiece piece) {
        return tool(DatapackConfig.MINECRAFT_NAMESPACE, material, piece);
    }

    /**
     * Generates a tool resource with a custom namespace.
     * * @param namespace The custom namespace string.
     * @param material  The {@link ToolMaterial} object.
     * @param piece     The {@link ToolPiece} object.
     * @return A validated {@link ItemResource}.
     */
    public static ItemResource tool(String namespace, ToolMaterial material, ToolPiece piece) {
        Objects.requireNonNull(material, "Tool material cannot be null.");
        Objects.requireNonNull(piece, "Tool piece cannot be null.");

        String path = material.name().toLowerCase() + "_" + piece.name().toLowerCase();
        return new DynamicItem(namespace, path);
    }

    // --- 🧩 General Custom Factory ---

    /**
     * Creates a fully custom ItemResource from raw namespace and path components.
     * * @param namespace The resource namespace.
     * @param path      The item path.
     * @return A validated {@link ItemResource}.
     */
    public static ItemResource custom(String namespace, String path) {
        return new DynamicItem(namespace, path);
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace component of the item.
     * @return The namespace string (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path component of the item.
     * @return The path string (e.g., "iron_helmet").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the full identifier in {@code namespace:path} format.
     * @return The immutable resource location string.
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Performs a syntax check on the internal components.
     * @throws IllegalStateException if validation criteria are not met.
     */
    @Override
    public void validate() throws IllegalStateException {
        ItemResource.super.validate();
    }

    // --- 📝 Overrides ---

    /**
     * Returns the finalized resource location as a string.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}