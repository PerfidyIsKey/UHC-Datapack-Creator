package uhc.resource.potion;

import uhc.resource.effect.EffectId;
import java.util.Objects;

/**
 * ⚗️ **Potion Registry ID Builder**
 * <p>
 * Specifically handles the creation and validation of Potion Registry strings
 * used in Minecraft's {@code potion_contents} component.
 * </p>
 * <p>
 * This class serves as a bridge between the effect registry and the potion registry,
 * ensuring that discrepancies (like the "speed" effect mapping to the "swiftness" potion)
 * are resolved correctly via {@link EffectId#getPotionPath()}.
 * </p>
 */
public class PotionId {

    // --- ⚙️ Nested Types ---

    /**
     * Defines the tier or duration variant of a potion registry entry.
     */
    public enum PotionModifier {
        /** The default version of the potion. */
        NORMAL(""),
        /** The extended duration version, prefixed with {@code long_}. */
        LONG("long_"),
        /** The enhanced potency version (Level II), prefixed with {@code strong_}. */
        STRONG("strong_");

        /** The technical prefix used in the Minecraft registry path. */
        private final String prefix;

        /**
         * @param prefix The SNBT-compatible prefix string.
         */
        PotionModifier(String prefix) {
            this.prefix = prefix;
        }

        /**
         * @return The prefix string for path construction.
         */
        public String getPrefix() {
            return prefix;
        }
    }

    // --- ⚙️ State & Fields ---

    /** * The underlying status effect associated with this potion identifier.
     */
    private final EffectId effect;

    /** * The duration or potency modifier applied to the base potion.
     */
    private final PotionModifier modifier;

    // --- 🏗️ Constructors ---

    /**
     * Private constructor to enforce usage of static factory methods.
     * <p><b>Error Catching:</b> Validates that the base effect is not null.
     * Defaults the modifier to {@link PotionModifier#NORMAL} if null is provided.</p>
     * * @param effect   The status effect to map to a potion.
     * @param modifier The variant (Normal, Long, Strong).
     * @throws NullPointerException if {@code effect} is null.
     */
    private PotionId(EffectId effect, PotionModifier modifier) {
        this.effect = Objects.requireNonNull(effect, "Base effect for PotionId cannot be null.");
        this.modifier = modifier != null ? modifier : PotionModifier.NORMAL;
    }

    // --- 🏭 Static Factory Methods ---

    /**
     * Creates a standard potion identifier.
     * @param effect The base effect.
     * @return A new {@link PotionId} instance.
     */
    public static PotionId normal(EffectId effect) {
        return new PotionId(effect, PotionModifier.NORMAL);
    }

    /**
     * Creates an extended duration potion identifier.
     * @param effect The base effect.
     * @return A new {@link PotionId} instance.
     */
    public static PotionId longDuration(EffectId effect) {
        return new PotionId(effect, PotionModifier.LONG);
    }

    /**
     * Creates an enhanced potency (Level II) potion identifier.
     * @param effect The base effect.
     * @return A new {@link PotionId} instance.
     */
    public static PotionId strong(EffectId effect) {
        return new PotionId(effect, PotionModifier.STRONG);
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Builds the final resource location string for the potion registry.
     * <p><b>Error Catching:</b> This method dynamically fetches the {@code potionPath}
     * rather than the {@code effectPath} to handle internal naming discrepancies
     * (e.g., speed vs swiftness).</p>
     * * @return The full identifier (e.g., {@code "minecraft:long_swiftness"}).
     * @throws IllegalStateException if the calculated path is blank or malformed.
     */
    public String getResourceLocation() {
        String path = effect.getPotionPath();

        // Logical verification: Ensure the path is valid before concatenation
        if (path == null || path.isBlank()) {
            throw new IllegalStateException("Effect " + effect.name() + " does not have a valid potion registry path mapping.");
        }

        String namespace = effect.getNamespace();
        String fullPath = modifier.getPrefix() + path;

        return namespace + ":" + fullPath;
    }

    /**
     * @return The base {@link EffectId} associated with this potion.
     */
    public EffectId getEffect() {
        return effect;
    }

    /**
     * @return The {@link PotionModifier} defining the potion's tier.
     */
    public PotionModifier getModifier() {
        return modifier;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the full resource location string.
     * <p><b>Implementation:</b> Delegates to {@link #getResourceLocation()}.</p>
     * @return The potion registry string.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}