package uhc.resource.advancement;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏆 **Advancement Resource Location**
 * <p>
 * Represents a type-safe identifier for a Minecraft advancement.
 * Follows the specific advancement path format: {@code <namespace>:<folder>/<key>}.
 * </p>
 * <p>
 * This class implements {@link ResourceLocation}, ensuring it can be used
 * interchangeably with other Minecraft registry identifiers throughout the engine.
 * </p>
 */
public final class AdvancementId implements ResourceLocation {

    // --- ⚙️ State & Fields ---

    /** * The resource namespace (e.g., "minecraft" or a custom project namespace).
     * This field is used to categorize resources by the datapack that defines them.
     */
    private final String namespace;

    /** * The path component within the namespace, typically formatted as {@code "folder/key"}.
     * This corresponds to the file path within the {@code advancements} directory.
     */
    private final String path;

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to enforce the use of static factories and rigorous validation.
     * <p><b>Error Catching:</b> Trims and lowercases inputs to resolve common formatting
     * issues and calls {@link #validate()} to catch regex violations immediately.</p>
     * * @param namespace The resource namespace (e.g., "minecraft").
     * @param path      The full path within that namespace (e.g., "story/root").
     * @throws NullPointerException if any argument is null.
     * @throws IllegalStateException if the components fail regex validation.
     */
    private AdvancementId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();
        this.path = Objects.requireNonNull(path, "Path (folder/key) cannot be null").toLowerCase().trim();

        // Validate the final string against Minecraft's naming conventions
        this.validate();
    }

    // --- 🏭 Static Factories (Vanilla) ---

    /**
     * Creates a vanilla advancement location using type-safe enums.
     * <p><b>Source:</b> Uses {@link DatapackConfig#MINECRAFT_NAMESPACE} for the namespace.</p>
     * * @param folder The advancement tab (e.g., {@link AdvancementFolder#STORY}).
     * @param key    The specific advancement key (e.g., {@link AdvancementKey#ROOT}).
     * @return A validated and immutable {@link AdvancementId}.
     * @throws NullPointerException if folder or key is null.
     */
    public static AdvancementId of(AdvancementFolder folder, AdvancementKey key) {
        Objects.requireNonNull(folder, "AdvancementFolder cannot be null");
        Objects.requireNonNull(key, "AdvancementKey cannot be null");
        return new AdvancementId(DatapackConfig.MINECRAFT_NAMESPACE, folder.toString() + "/" + key.toString());
    }

    /**
     * Convenience factory for advancements located in the standard Story tab.
     * * @param key The specific advancement key string.
     * @return A validated {@link AdvancementId} under {@code minecraft:story/}.
     */
    public static AdvancementId story(String key) {
        return new AdvancementId(DatapackConfig.MINECRAFT_NAMESPACE, AdvancementFolder.STORY + "/" + key);
    }

    /**
     * Convenience factory for advancements located in the standard Nether tab.
     * * @param key The specific advancement key string.
     * @return A validated {@link AdvancementId} under {@code minecraft:nether/}.
     */
    public static AdvancementId nether(String key) {
        return new AdvancementId(DatapackConfig.MINECRAFT_NAMESPACE, AdvancementFolder.NETHER + "/" + key);
    }

    // --- 🏭 Static Factories (Custom) ---

    /**
     * Creates a custom advancement identifier using the project's default custom namespace.
     * <p><b>Source:</b> Uses {@link DatapackConfig#CUSTOM_NAMESPACE}.</p>
     * * @param path The full path including the folder and key (e.g., "challenges/winner").
     * @return A validated {@link AdvancementId} in the project's custom namespace.
     */
    public static AdvancementId custom(String path) {
        return new AdvancementId(DatapackConfig.CUSTOM_NAMESPACE, path);
    }

    /**
     * Creates a fully external advancement resource location using a specific namespace and path.
     * * @param namespace The custom namespace (e.g., "external_mod").
     * @param path      The full path including the folder and key.
     * @return A validated {@link AdvancementId}.
     */
    public static AdvancementId external(String namespace, String path) {
        return new AdvancementId(namespace, path);
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace component.
     * @return The lowercase namespace (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path component.
     * @return The combined folder and key path (e.g., "story/root").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Returns the full namespaced identifier required by Minecraft.
     * @return The formatted string (e.g., "minecraft:story/root").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Validates the integrity of the resource location.
     * <p><b>Error Catching:</b> Utilizes the {@code VALID_PATTERN} regex from the
     * {@link ResourceLocation} interface to catch illegal characters like spaces,
     * uppercase letters, or unsupported symbols.</p>
     * * @throws IllegalStateException if the identifier violates naming conventions.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Invalid Advancement Resource Format: " + getResourceLocation());
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the resource location as a string for use in commands and serialization.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}