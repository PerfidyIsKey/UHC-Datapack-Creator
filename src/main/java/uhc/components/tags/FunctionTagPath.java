package uhc.components.tags;

import uhc.components.FunctionName;
import uhc.core.DatapackConfig;

/**
 * 🏷️ **Type-Safe Minecraft Function Tag Paths**
 * <p>
 * Defines references to function tags located in {@code tags/functions/}.
 * Unlike standard functions, these are prefixed with '#' and can trigger multiple
 * .mcfunction files at once.
 * </p>
 */
public enum FunctionTagPath implements FunctionName {

    /** Mandatory vanilla load tag. Location: {@code minecraft:tags/functions/load.json} */
    LOAD,

    /** Mandatory vanilla tick tag. Location: {@code minecraft:tags/functions/tick.json} */
    TICK,

    /** Custom loop tag, defaults to minecraft namespace. */
    CUSTOM_LOOP,

    /** * Initial setup tag for the custom pack.
     * Uses the namespace defined in {@link DatapackConfig#CUSTOM_NAMESPACE}.
     */
    UHC_INIT(DatapackConfig.CUSTOM_NAMESPACE);

    /** The namespace where the tag JSON file is physically stored. */
    private final String targetNamespace;

    /**
     * Constructor for tags in a specific namespace.
     * @param targetNamespace The namespace (e.g., "minecraft" or "uhc_core").
     */
    FunctionTagPath(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    /**
     * Default constructor.
     * Defaults to {@link DatapackConfig#MINECRAFT_NAMESPACE}.
     */
    FunctionTagPath() {
        this(DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * @return The lowercase name of the tag file (e.g., "load").
     */
    public String getPath() {
        return this.name().toLowerCase();
    }

    /**
     * Generates the full Minecraft identifier for use in commands.
     * <p>Example: {@code #minecraft:load} or {@code #uhc:uhc_init}</p>
     * @return The formatted tag identifier starting with '#'.
     */
    @Override
    public String getIdentifier() {
        // Prefixes with # for command syntax compliance
        return "#" + targetNamespace + ":" + getPath();
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}