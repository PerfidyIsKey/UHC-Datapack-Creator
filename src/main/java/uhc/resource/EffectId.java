package uhc.resource;

public enum EffectId {
    ABSORPTION("absorption"),
    BLINDNESS("blindness"),
    FIRE_RESISTANCE("fire_resistance"),
    GLOWING("glowing"),
    HASTE("haste"),
    HEALTH_BOOST("health_boost"),
    INVISIBILITY("invisibility"),
    LUCK("luck"),
    NAUSEA("nausea"),
    POISON("poison"),
    REGENERATION("regeneration"),
    RESISTANCE("resistance"),
    SATURATION("saturation"),
    SLOW_FALLING("slow_falling"),
    SLOWNESS("slowness"),
    SPEED("speed"),
    STRENGTH("strength"),
    WEAKNESS("weakness");

    private final String id;
    private final String namespace;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    EffectId(String id) {
        this.id = id;
        this.namespace = DEFAULT_NAMESPACE;
    }

    EffectId(String id, String namespace) {
        this.id = id;
        this.namespace = namespace;
    }

    /**
     * Returns the potion tag string for the potion_contents component.
     *
     * @param strong   whether the effect is strong
     * @param extended whether the effect is long
     * @return potion tag string like "strong_speed" or "long_regeneration"
     */
    public String getPotionTag(boolean strong, boolean extended) {
        String prefix = "";
        if (strong) {
            prefix += "strong_";
        } else if (extended) {
            prefix += "long_";
        }
        return namespace + ":" + prefix + id;
    }

    public String getPotionTag() {
        return getPotionTag(false, false);
    }

    /**
     * Returns the full resource location for the effect ID (e.g., "minecraft:speed").
     */
    public String getResourceLocation() {
        return namespace + ":" + id;
    }

    @Override
    public String toString() {
        return getPotionTag();
    }
}
