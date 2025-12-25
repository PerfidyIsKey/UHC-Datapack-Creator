package uhc.resource.item;

import uhc.core.DatapackConfig;
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
 * namespaced identifier standards during construction.
 * </p>
 */
public final class DynamicItem implements ItemResource {

    // --- ⚙️ State & Fields ---

    /** * The immutable, full resource location string (e.g., "minecraft:diamond_sword").
     */
    private final String location;

    // --- 🏗️ Constructors ---

    /**
     * Private constructor to prevent direct instantiation.
     * <p><b>Error Catching:</b> Triggers the {@link #validate()} method inherited from
     * {@link ItemResource} to ensure the constructed string is syntactically valid.</p>
     * * @param location The pre-formatted namespaced string.
     * @throws IllegalStateException if the location fails naming convention validation.
     */
    private DynamicItem(String location) {
        this.location = location;
        // Immediate fail-fast validation
        this.validate();
    }

    // --- 🛡️ Static Factory Methods (Armor) ---

    /**
     * Generates a type-safe armor resource using the default Minecraft namespace.
     * * @param material The material type (e.g., IRON, DIAMOND).
     * @param piece    The specific armor slot (e.g., HELMET, CHESTPLATE).
     * @return A validated {@link ItemResource} for the armor piece.
     */
    public static ItemResource armor(ArmorMaterial material, ArmorPiece piece) {
        return armor(DatapackConfig.MINECRAFT_NAMESPACE, material, piece);
    }

    /**
     * Generates an armor resource with a custom namespace.
     * <p><b>Error Catching:</b> Validates that the namespace, material, and piece
     * are non-null to prevent malformed string generation.</p>
     * * @param namespace The custom namespace (e.g., "twilightforest").
     * @param material  The material type.
     * @param piece     The armor slot.
     * @return A validated {@link ItemResource}.
     * @throws NullPointerException if any parameter is null.
     */
    public static ItemResource armor(String namespace, ArmorMaterial material, ArmorPiece piece) {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        Objects.requireNonNull(material, "Armor material cannot be null");
        Objects.requireNonNull(piece, "Armor piece cannot be null");

        return new DynamicItem(namespace.toLowerCase() + ":"
                + material.name().toLowerCase() + "_" + piece.name().toLowerCase());
    }

    // --- 🛠️ Static Factory Methods (Tools) ---

    /**
     * Generates a type-safe tool resource using the default Minecraft namespace.
     * * @param material The material type (e.g., STONE, NETHERITE).
     * @param piece    The tool type (e.g., PICKAXE, AXE).
     * @return A validated {@link ItemResource} for the tool.
     */
    public static ItemResource tool(ToolMaterial material, ToolPiece piece) {
        return tool(DatapackConfig.MINECRAFT_NAMESPACE, material, piece);
    }

    /**
     * Generates a tool resource with a custom namespace.
     * <p><b>Error Catching:</b> Validates all parameters for nullity.</p>
     * * @param namespace The custom namespace.
     * @param material  The material type.
     * @param piece     The tool type.
     * @return A validated {@link ItemResource}.
     * @throws NullPointerException if any parameter is null.
     */
    public static ItemResource tool(String namespace, ToolMaterial material, ToolPiece piece) {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        Objects.requireNonNull(material, "Tool material cannot be null");
        Objects.requireNonNull(piece, "Tool piece cannot be null");

        return new DynamicItem(namespace.toLowerCase() + ":"
                + material.name().toLowerCase() + "_" + piece.name().toLowerCase());
    }

    // --- 🧩 Static Factory Methods (General) ---

    /**
     * Creates a fully custom ItemResource from a raw namespace and path.
     * <p><b>Use case:</b> Handling modded items or items not yet present in the registry.</p>
     * <p><b>Error Catching:</b> Ensures both namespace and path are not null before trimming
     * and converting to lowercase.</p>
     * * @param namespace The resource namespace (e.g., "uhc_core").
     * @param path      The item path (e.g., "blood_diamond").
     * @return A validated {@link ItemResource}.
     * @throws NullPointerException if namespace or path is null.
     */
    public static ItemResource custom(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        Objects.requireNonNull(path, "Path cannot be null");
        return new DynamicItem(namespace.toLowerCase().trim() + ":" + path.toLowerCase().trim());
    }

    // --- 🔍 Accessors & Overrides ---

    /**
     * Retrieves the full formatted resource location.
     * @return The formatted string (e.g., "namespace:path").
     */
    @Override
    public String getResourceLocation() {
        return location;
    }

    /**
     * Returns the resource location as a string.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}