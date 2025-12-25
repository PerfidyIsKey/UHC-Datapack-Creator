package uhc.resource.potion;

import uhc.core.DatapackConfig;
import uhc.resource.effect.EffectId;
import java.util.Objects;

/**
 * 🧪 **Namespace-Aware Potion Identifier**
 * <p>
 * This class handles the construction of valid Minecraft Potion Registry IDs.
 * It combines a base {@link EffectId} with a modifier (Strong or Long) to
 * produce strings compatible with the {@code potion_contents} component.
 * </p>
 * <p>
 * It features a "fail-fast" validation system that prevents the creation of
 * invalid vanilla potion combinations while allowing full flexibility for custom namespaces.
 * </p>
 */
public class PotionId {

    /**
     * Defines the tier or duration variant of a potion.
     */
    public enum PotionModifier {
        /** Default potency and duration (e.g., "regeneration"). */
        NORMAL(""),
        /** Increased duration, prefixed with "long_" (e.g., "long_regeneration"). */
        LONG("long_"),
        /** Increased amplifier/level, prefixed with "strong_" (e.g., "strong_regeneration"). */
        STRONG("strong_");

        private final String prefix;

        PotionModifier(String prefix) {
            this.prefix = prefix;
        }

        /** @return The NBT-compatible prefix string for this modifier. */
        public String getPrefix() {
            return prefix;
        }
    }

    // --- Private Fields ---

    /** The base effect mapped to this potion (e.g., REGENERATION). */
    private final EffectId effect;

    /** The potency or duration modifier (Normal, Long, or Strong). */
    private final PotionModifier modifier;

    // --- Constructor ---

    /**
     * Private constructor to enforce the use of static factory methods.
     * <p><b>Error Catching:</b> Validates that the effect is not null and
     * cross-references vanilla brewing rules if using the {@code minecraft} namespace.</p>
     * * @param effect   The base effect registry entry.
     * @param modifier The modifier variant. Defaults to {@link PotionModifier#NORMAL} if null.
     * @throws NullPointerException if effect is null.
     * @throws IllegalArgumentException if the combination is invalid in vanilla Minecraft.
     */
    private PotionId(EffectId effect, PotionModifier modifier) {
        this.effect = Objects.requireNonNull(effect, "Base effect for PotionId cannot be null.");
        this.modifier = modifier != null ? modifier : PotionModifier.NORMAL;

        // Validation: Only enforce strict rules if the namespace is 'minecraft'
        if (DatapackConfig.MINECRAFT_NAMESPACE.equals(effect.getNamespace())) {
            validateVanillaCombination();
        }
    }

    // --- Static Factory Methods ---

    /**
     * Creates a standard potion ID with no modifiers.
     * @param effect The base effect.
     * @return A new PotionId instance.
     */
    public static PotionId normal(EffectId effect) {
        return new PotionId(effect, PotionModifier.NORMAL);
    }

    /**
     * Creates an extended duration potion ID.
     * @param effect The base effect.
     * @return A new PotionId instance with the "long_" prefix.
     */
    public static PotionId longDuration(EffectId effect) {
        return new PotionId(effect, PotionModifier.LONG);
    }

    /**
     * Creates an enhanced level/potency potion ID.
     * @param effect The base effect.
     * @return A new PotionId instance with the "strong_" prefix.
     */
    public static PotionId strong(EffectId effect) {
        return new PotionId(effect, PotionModifier.STRONG);
    }

    // --- Internal Logic ---

    /**
     * Enforces the vanilla brewing table.
     * <p>This ensures impossible items like "strong_night_vision" cannot be created.</p>
     * @throws IllegalArgumentException if the combination is not supported by vanilla logic.
     */
    private void validateVanillaCombination() {
        if (modifier == PotionModifier.NORMAL) return;

        boolean isValid = switch (effect) {
            // Potions supporting both Strong and Long
            case LEAPING, SPEED, SLOWNESS, POISON, REGENERATION, STRENGTH, TURTLE_MASTER -> true;

            // Potions supporting ONLY Long (Extended)
            case NIGHT_VISION, INVISIBILITY, FIRE_RESISTANCE, WATER_BREATHING, WEAKNESS, SLOW_FALLING ->
                    modifier == PotionModifier.LONG;

            // Potions supporting ONLY Strong (Level II)
            case INSTANT_HEALTH, INSTANT_DAMAGE ->
                    modifier == PotionModifier.STRONG;

            // All others (Water, Awkward, Thick, Luck, 1.21 effects) only support Normal
            default -> false;
        };

        if (!isValid) {
            throw new IllegalArgumentException(String.format(
                    "Invalid vanilla combination: %s does not support the '%s' modifier.",
                    effect.name(), modifier.name()
            ));
        }
    }

    // --- Public API ---

    /**
     * Generates the full, namespaced resource location string.
     * <p><b>Error Catching:</b> Ensures that null or malformed paths do not result in broken NBT.</p>
     * * @return Formatted string, e.g., "minecraft:strong_regeneration" or "custom:long_ultra_speed".
     */
    public String getResourceLocation() {
        String path = effect.getPath();
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException("Effect path for " + effect.name() + " is null or empty.");
        }

        return effect.getNamespace() + ":" + modifier.getPrefix() + path;
    }

    /** @return The underlying {@link EffectId}. */
    public EffectId getEffect() {
        return effect;
    }

    /** @return The {@link PotionModifier} applied to this ID. */
    public PotionModifier getModifier() {
        return modifier;
    }

    /**
     * Returns the same result as {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}