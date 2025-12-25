package uhc.resource.item;

import uhc.core.DatapackConfig;
import uhc.text.DyeColor;

import java.util.Objects;

/**
 * 🛠️ **Dynamic Item Factory**
 * <p>
 * This class provides type-safe, static factory methods to generate {@link ItemResource}
 * identifiers for item combinations and custom-namespaced resources that are not
 * explicitly registered in the {@link ItemId} enum.
 * </p>
 * <p>
 * All generated resources are automatically validated against Minecraft's
 * namespaced identifier standards during construction via the private constructor.
 * </p>
 */
public final class DynamicItem implements ItemResource {

    // --- ⚙️ State & Fields ---

    /** * The immutable, full resource location string (e.g., "minecraft:diamond_sword").
     * This field represents the finalized state of the resource after validation.
     */
    private final String location;

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to enforce controlled instantiation through static factory methods.
     * <p><b>Error Catching:</b> Immediately triggers the {@link #validate()} method
     * to ensure the provided location string does not violate character constraints
     * or contains illegal symbols.</p>
     * * @param location The pre-formatted namespaced string.
     * @throws IllegalStateException if the location fails naming convention validation.
     */
    private DynamicItem(String location) {
        this.location = location;
        // Immediate fail-fast validation to prevent invalid IDs from entering the system
        this.validate();
    }

    // --- 🎨 Colorable Factories ---

    /**
     * Creates a colored item identifier (e.g., "minecraft:yellow_bundle") using registry templates.
     * <p><b>Logic:</b> Delegates the creation to the {@link ColorableItem} object to maintain
     * internal consistency with the template system.</p>
     * * @param color The {@link DyeColor} to apply (e.g., RED, YELLOW).
     * @param item  The {@link ColorableItem} template (e.g., BUNDLE).
     * @return A validated {@link ItemResource} for the colored item.
     * @throws NullPointerException if color or item template is null.
     */
    public static ItemResource color(DyeColor color, ColorableItem item) {
        Objects.requireNonNull(color, "DyeColor cannot be null for dynamic item generation.");
        Objects.requireNonNull(item, "ColorableItem template cannot be null for dynamic item generation.");

        // Delegates to the ColorableItem object directly
        return item.withColor(color);
    }

    // --- 🛡️ Armor Factories ---

    /**
     * Generates a type-safe armor resource using the default Minecraft namespace.
     * * @param material The {@link ArmorMaterial} object (e.g., IRON, DIAMOND).
     * @param piece    The {@link ArmorPiece} object (e.g., HELMET, CHESTPLATE).
     * @return A validated {@link ItemResource} for the armor piece.
     * @throws NullPointerException if material or piece is null.
     */
    public static ItemResource armor(ArmorMaterial material, ArmorPiece piece) {
        return armor(DatapackConfig.MINECRAFT_NAMESPACE, material, piece);
    }

    /**
     * Generates an armor resource with a custom namespace.
     * <p><b>Error Catching:</b> Validates that all parameters are non-null. It formats
     * the path by lowercasing the material and piece names safely.</p>
     * * @param namespace The custom namespace string (e.g., "minecraft").
     * @param material  The {@link ArmorMaterial} object.
     * @param piece     The {@link ArmorPiece} object.
     * @return A validated {@link ItemResource}.
     * @throws NullPointerException if any parameter is null.
     */
    public static ItemResource armor(String namespace, ArmorMaterial material, ArmorPiece piece) {
        Objects.requireNonNull(namespace, "Namespace cannot be null for armor generation.");
        Objects.requireNonNull(material, "Armor material cannot be null.");
        Objects.requireNonNull(piece, "Armor piece cannot be null.");

        // Construct path without converting the whole object to a string
        String path = material.name().toLowerCase() + "_" + piece.name().toLowerCase();
        return new DynamicItem(namespace.toLowerCase().trim() + ":" + path);
    }

    // --- ⚒️ Tool Factories ---

    /**
     * Generates a type-safe tool resource using the default Minecraft namespace.
     * * @param material The {@link ToolMaterial} object (e.g., WOODEN, NETHERITE).
     * @param piece    The {@link ToolPiece} object (e.g., PICKAXE, SHOVEL).
     * @return A validated {@link ItemResource} for the tool.
     * @throws NullPointerException if material or piece is null.
     */
    public static ItemResource tool(ToolMaterial material, ToolPiece piece) {
        return tool(DatapackConfig.MINECRAFT_NAMESPACE, material, piece);
    }

    /**
     * Generates a tool resource with a custom namespace.
     * <p><b>Error Catching:</b> Checks for nullity across all tool components.</p>
     * * @param namespace The custom namespace string.
     * @param material  The {@link ToolMaterial} object.
     * @param piece     The {@link ToolPiece} object.
     * @return A validated {@link ItemResource}.
     * @throws NullPointerException if any parameter is null.
     */
    public static ItemResource tool(String namespace, ToolMaterial material, ToolPiece piece) {
        Objects.requireNonNull(namespace, "Namespace cannot be null for tool generation.");
        Objects.requireNonNull(material, "Tool material cannot be null.");
        Objects.requireNonNull(piece, "Tool piece cannot be null.");

        String path = material.name().toLowerCase() + "_" + piece.name().toLowerCase();
        return new DynamicItem(namespace.toLowerCase().trim() + ":" + path);
    }

    // --- 🧩 General Custom Factory ---

    /**
     * Creates a fully custom ItemResource from a raw namespace and path components.
     * <p><b>Use case:</b> Handling modded items or resources not defined in local enums.</p>
     * <p><b>Error Catching:</b> Trims whitespace and enforces lowercase to ensure
     * compatibility with Minecraft's strict registry requirements.</p>
     * * @param namespace The resource namespace (e.g., "uhc_items").
     * @param path      The item path (e.g., "golden_skull").
     * @return A validated {@link ItemResource}.
     * @throws NullPointerException if namespace or path is null.
     */
    public static ItemResource custom(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null for custom item generation.");
        Objects.requireNonNull(path, "Path cannot be null for custom item generation.");

        return new DynamicItem(
                namespace.toLowerCase().trim() + ":" + path.toLowerCase().trim()
        );
    }

    // --- 🛰️ Contract Implementation ---

    /**
     * Retrieves the stored namespaced identifier.
     * @return The full identifier (e.g., "minecraft:iron_pickaxe").
     */
    @Override
    public String getResourceLocation() {
        return location;
    }

    /**
     * Performs a syntax check on the internal location string.
     * <p>Inherits standard validation logic from the {@link ItemResource} interface.</p>
     * @throws IllegalStateException if validation criteria are not met.
     */
    @Override
    public void validate() throws IllegalStateException {
        ItemResource.super.validate();
    }

    /**
     * Returns the finalized resource location as a string.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}