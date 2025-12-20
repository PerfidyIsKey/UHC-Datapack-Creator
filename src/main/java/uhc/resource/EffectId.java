package uhc.resource;

import uhc.core.DatapackConfig;

/**
 * 🧪 **Effect and Potion Registry ID Mapper**
 * <p>
 * This enum maps status effects to their respective resource locations
 * and provides logic to generate valid Potion Registry IDs.
 * </p>
 */
public enum EffectId {
    ABSORPTION,
    BLINDNESS,
    FIRE_RESISTANCE,
    GLOWING,
    HASTE, // Note: No vanilla potion for Haste
    HEALTH_BOOST,
    INVISIBILITY,
    LUCK,
    NAUSEA,
    POISON,
    REGENERATION,
    RESISTANCE,
    SATURATION,
    SLOW_FALLING,
    SLOWNESS,
    SPEED("swiftness"), // Fix: Potion ID is 'swiftness', Effect ID is 'speed'
    STRENGTH,
    WEAKNESS;

    private final String path;
    private final String namespace;

    EffectId() {
        this.path = name().toLowerCase();
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * @param override Use this if the Potion Registry ID differs from the Effect name.
     */
    EffectId(String override) {
        this.path = override;
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Generates a Potion Registry ID.
     * <p>
     * <b>Example:</b> SPEED.getPotionTag(true, false) -> "minecraft:strong_swiftness"
     * </p>
     * @param strong   Increases amplifier (e.g., Strength II)
     * @param extended Increases duration (e.g., 8:00 vs 3:00)
     * @return Valid Minecraft potion resource location.
     */
    public String getPotionTag(boolean strong, boolean extended) {
        StringBuilder sb = new StringBuilder(namespace).append(":");

        if (strong) {
            sb.append("strong_");
        } else if (extended) {
            sb.append("long_");
        }

        return sb.append(path).toString();
    }

    public String getPotionTag() {
        return getPotionTag(false, false);
    }

    /**
     * Returns the Status Effect ID (for custom_effects).
     * <p>
     * <b>Warning:</b> This assumes the Effect ID matches the Enum name.
     * Speed is 'speed', not 'swiftness'.
     * </p>
     */
    public String getResourceLocation() {
        String effectPath = name().toLowerCase();
        return namespace + ":" + effectPath;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}