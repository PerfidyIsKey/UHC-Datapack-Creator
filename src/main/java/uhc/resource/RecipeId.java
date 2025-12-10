package uhc.resource;

/**
 * An enumeration representing unique identifiers for Minecraft recipes.
 * <p>
 * This ID is a required argument for the {@code /recipe} command when targeting
 * a specific recipe, and must be in the format {@code namespace:path}.
 */
public enum RecipeId {

    // Example using a custom namespace (UHC)
    /** A custom recipe for a dragon head. Result: uhc:dragon_head */
    DRAGON_HEAD("uhc", "dragon_head");

    // The final, immutable string representing the recipe's full resource location.
    private final String resourceLocation;

    // Default namespace constant for use in constructors.
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Private constructor for recipes defined by a custom namespace and path string.
     *
     * @param namespace The custom namespace (e.g., "uhc").
     * @param path The path component (e.g., "dragon_head").
     */
    RecipeId(String namespace, String path) {
        // Error Check: Ensure neither component is null/empty, though this is primarily for safety
        if (namespace == null || path == null || namespace.isEmpty() || path.isEmpty()) {
            throw new IllegalArgumentException("RecipeId namespace and path must be non-empty strings.");
        }
        this.resourceLocation = namespace + ":" + path;
    }

    /**
     * Private constructor for recipes defined by a path string using the default 'minecraft' namespace.
     *
     * @param path The path component (e.g., "bread").
     */
    RecipeId(String path) {
        this(DEFAULT_NAMESPACE, path);
    }

    /*
     * NOTE: The constructors taking an ItemId argument have been omitted for simplicity
     * since they are redundant (an ItemId's resource location is just a string),
     * but they could be reintroduced if strong typing on the input is desired.
     * If reintroduced, they should call the string-based constructor above.

    RecipeId(ItemId id) {
        this(DEFAULT_NAMESPACE, id.getResourceLocation());
    }

    RecipeId(String namespace, ItemId id) {
        this(namespace, id.getResourceLocation());
    }
    */

    /**
     * Retrieves the complete, fully qualified resource location string for the recipe.
     *
     * @return The immutable recipe ID string (e.g., {@code uhc:dragon_head}).
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Returns the resource location, suitable for direct insertion into a command.
     *
     * @return The immutable recipe ID string.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}