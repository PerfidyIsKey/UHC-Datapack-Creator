package uhc.resource.recipe;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;

import java.util.Objects;

/**
 * 🍳 **Recipe Identifier Registry**
 * <p>
 * Represents the unique resource location for a Minecraft crafting, smelting,
 * or smithing recipe. This ID is required for the {@code /recipe} command
 * and for managing player recipe unlocks.
 * </p>
 */
public enum RecipeId implements ResourceLocation {

    // --- 🛠️ Custom UHC Recipes ---

    /** * A custom recipe for obtaining a Dragon Head.
     * <p>Location: {@code uhc_core_pack:dragon_head}</p>
     */
    DRAGON_HEAD(DatapackConfig.CUSTOM_NAMESPACE),

    /** * A custom recipe for a Golden Head (standard UHC item).
     * <p>Location: {@code uhc_core_pack:golden_head}</p>
     */
    GOLDEN_HEAD(DatapackConfig.CUSTOM_NAMESPACE),

    // --- 🌍 Vanilla Minecraft Recipes ---

    /** * The standard recipe for crafting Bread.
     * <p>Location: {@code minecraft:bread}</p>
     */
    BREAD(DatapackConfig.MINECRAFT_NAMESPACE),

    /** * The standard recipe for an Enchanted Golden Apple (Notch Apple).
     * <p>Location: {@code minecraft:enchanted_golden_apple}</p>
     */
    ENCHANTED_GOLDEN_APPLE(DatapackConfig.MINECRAFT_NAMESPACE);

    // --- ⚙️ Internal State ---

    /** * The registry namespace (e.g., "minecraft" or "uhc_core_pack"). */
    private final String namespace;

    /** * The unique path component (e.g., "dragon_head"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Minecraft Constructor**
     * <p>Uses the default Minecraft namespace. The path is automatically
     * derived from the enum constant name in lowercase.</p>
     */
    RecipeId() {
        this(DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * 🟠 **Namespace-Specific Constructor**
     * <p>Allows for a specific namespace while automatically generating
     * the path from the enum name.</p>
     * <p><b>Error Catching:</b> Validates that the namespace is not null or blank
     * and triggers {@link #validate()} to ensure the generated ID is legal.</p>
     * @param namespace The resource namespace to use.
     * @throws NullPointerException if namespace is null.
     * @throws IllegalStateException if the final identifier contains illegal characters.
     */
    RecipeId(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();

        // Directly use name().toLowerCase() as requested
        this.path = this.name().toLowerCase();

        if (this.namespace.isEmpty()) {
            throw new IllegalStateException("Recipe namespace cannot be empty.");
        }

        // Validate the final identifier against the ResourceLocation regex.
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace associated with this recipe.
     * @return The namespace string (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path associated with this recipe.
     * @return The path string derived from {@link #name()}.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Combines the namespace and path into a valid namespaced key.
     * @return The full identifier (e.g., "uhc_core_pack:dragon_head").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Performs a syntax check on the identifier.
     * <p><b>Error Catching:</b> Uses the regex pattern from {@link ResourceLocation}
     * to ensure the identifier is compatible with Minecraft command parsing.</p>
     * @throws IllegalStateException if the identifier is malformed.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Malformed RecipeId: " + getResourceLocation());
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the full resource location for direct use in commands.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}