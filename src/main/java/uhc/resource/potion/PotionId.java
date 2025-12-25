package uhc.resource.potion;

import uhc.core.DatapackConfig;
import uhc.resource.effect.EffectId;
import java.util.EnumSet;
import java.util.Objects;

/**
 * 🧪 **Namespace-Aware Potion Identifier**
 * <p>
 * This class handles the construction of valid Minecraft Potion Registry IDs.
 * It combines a base {@link EffectId} with a {@link PotionModifier} (Strong or Long)
 * to produce strings compatible with the {@code potion_contents} NBT component.
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

        /** The NBT string prefix associated with this modifier. */
        private final String prefix;

        PotionModifier(String prefix) {
            this.prefix = prefix;
        }

        /** @return The NBT-compatible prefix string (e.g., "long_"). */
        public String getPrefix() {
            return prefix;
        }
    }

    // --- Private Static Constants ---

    /** * A high-performance set of effects that do not exist as base potions in the Vanilla registry.
     * Attempting to use these in a PotionId will trigger a fail-fast exception.
     */
    private static final EnumSet<EffectId> UNOBTAINABLE_POTIONS = EnumSet.of(
            EffectId.HASTE, EffectId.MINING_FATIGUE, EffectId.JUMP_BOOST, EffectId.RESISTANCE,
            EffectId.HEALTH_BOOST, EffectId.ABSORPTION, EffectId.SATURATION, EffectId.DOLPHINS_GRACE,
            EffectId.CONDUIT_POWER, EffectId.GLOWING, EffectId.NAUSEA, EffectId.BLINDNESS,
            EffectId.HUNGER, EffectId.WITHER, EffectId.LEVITATION, EffectId.DARKNESS,
            EffectId.BAD_LUCK, EffectId.BREATH_OF_THE_NAUTILUS, EffectId.BAD_OMEN,
            EffectId.TRIAL_OMEN, EffectId.RAID_OMEN, EffectId.HERO_OF_THE_VILLAGE
    );

    // --- Private Fields ---

    /** The underlying status effect associated with this potion. */
    private final EffectId effect;

    /** The potency or duration variant applied to the base effect. */
    private final PotionModifier modifier;

    // --- Constructor ---

    /**
     * Private constructor to enforce factory method usage.
     * <p><b>Error Catching:</b> Validates non-nullity and cross-references
     * vanilla registry availability and modifier compatibility.</p>
     * @param effect   The base effect.
     * @param modifier The modifier variant (defaults to NORMAL if null).
     * @throws NullPointerException if effect is null.
     * @throws IllegalArgumentException if the combination is invalid in vanilla Minecraft.
     */
    private PotionId(EffectId effect, PotionModifier modifier) {
        this.effect = Objects.requireNonNull(effect, "Base effect for PotionId cannot be null.");
        this.modifier = modifier != null ? modifier : PotionModifier.NORMAL;

        // Validation only applies to the 'minecraft' namespace to allow custom datapack flexibility
        if (DatapackConfig.MINECRAFT_NAMESPACE.equals(effect.getNamespace())) {
            validateVanillaAvailability();
            validateVanillaModifiers();
        }
    }

    // --- Static Factory Methods ---

    /** @return A PotionId with default duration and potency. */
    public static PotionId normal(EffectId effect) {
        return new PotionId(effect, PotionModifier.NORMAL);
    }

    /** @return A PotionId with extended duration (long_). */
    public static PotionId longDuration(EffectId effect) {
        return new PotionId(effect, PotionModifier.LONG);
    }

    /** @return A PotionId with enhanced potency (strong_). */
    public static PotionId strong(EffectId effect) {
        return new PotionId(effect, PotionModifier.STRONG);
    }

    // --- Internal Validation Logic ---

    /**
     * Checks if the effect exists in the Minecraft Potion Registry.
     * @throws IllegalArgumentException if the effect is status-only.
     */
    private void validateVanillaAvailability() {
        if (UNOBTAINABLE_POTIONS.contains(effect)) {
            throw new IllegalArgumentException(String.format(
                    "The effect '%s' exists as a status but cannot be used as a Potion Registry ID. " +
                            "Add this as a Custom Effect instead.", effect.name()
            ));
        }
    }

    /**
     * Ensures the modifier (Strong/Long) is valid for the specific vanilla effect.
     * @throws IllegalArgumentException if the modifier is unsupported.
     */
    private void validateVanillaModifiers() {
        if (modifier == PotionModifier.NORMAL) return;

        boolean isValid = switch (effect) {
            // Supports both variants
            case LEAPING, SPEED, SLOWNESS, POISON, REGENERATION, STRENGTH, TURTLE_MASTER -> true;

            // Supports ONLY Long
            case NIGHT_VISION, INVISIBILITY, FIRE_RESISTANCE, WATER_BREATHING, WEAKNESS, SLOW_FALLING ->
                    modifier == PotionModifier.LONG;

            // Supports ONLY Strong
            case INSTANT_HEALTH, INSTANT_DAMAGE ->
                    modifier == PotionModifier.STRONG;

            default -> false;
        };

        if (!isValid) {
            throw new IllegalArgumentException(String.format(
                    "Invalid vanilla combination: '%s' does not support the '%s' modifier.",
                    effect.name(), modifier.name()
            ));
        }
    }

    // --- Public API & Serialization ---

    /**
     * Builds the full resource location string for NBT storage.
     * @return Formatted string (e.g., "minecraft:long_fire_resistance").
     * @throws IllegalStateException if the effect path is corrupted.
     */
    public String getResourceLocation() {
        String path = effect.getPath();
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException("Effect path for " + effect.name() + " is missing.");
        }

        return effect.getNamespace() + ":" + modifier.getPrefix() + path;
    }

    /** @return The base {@link EffectId}. */
    public EffectId getEffect() {
        return effect;
    }

    /** @return The applied {@link PotionModifier}. */
    public PotionModifier getModifier() {
        return modifier;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}