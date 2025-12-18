package uhc.resource.bossbar;

import uhc.core.DatapackConfig;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 🆔 **Bossbar Identifier**
 * <p>
 * Represents a unique namespaced ID for a Minecraft bossbar.
 * Validates that the ID follows Minecraft's resource location rules (snake_case).
 * </p>
 */
public class BossbarId {
    private static final Pattern VALID_PATH = Pattern.compile("^[a-z0-9/._-]+$");

    private final String namespace;
    private final String path;

    private BossbarId(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Creates a BossbarId using the default UHC namespace.
     * @param path The unique path for the bar (e.g., "game_timer").
     * @return A validated BossbarId.
     */
    public static BossbarId of(String path) {
        return of(DatapackConfig.CUSTOM_NAMESPACE, path);
    }

    /**
     * Creates a BossbarId with a custom namespace.
     * @param namespace The namespace (e.g., "minecraft" or "events").
     * @param path The path name.
     * @return A validated BossbarId.
     * @throws IllegalArgumentException if the path contains invalid characters.
     */
    public static BossbarId of(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null.");
        Objects.requireNonNull(path, "Path cannot be null.");

        String cleanNamespace = namespace.toLowerCase().trim();
        String cleanPath = path.toLowerCase().trim();

        if (!VALID_PATH.matcher(cleanPath).matches()) {
            throw new IllegalArgumentException("Invalid Bossbar ID path: " + path +
                    ". Use only lowercase letters, numbers, underscores, dots, or hyphens.");
        }

        return new BossbarId(cleanNamespace, cleanPath);
    }

    /**
     * Returns the full resource location string (e.g., "uhc:game_timer").
     */
    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BossbarId bossbarId = (BossbarId) o;
        return Objects.equals(namespace, bossbarId.namespace) && Objects.equals(path, bossbarId.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }
}