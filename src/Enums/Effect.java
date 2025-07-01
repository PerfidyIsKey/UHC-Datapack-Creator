package Enums;

public enum Effect {
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

    private final String symbol;

    Effect(String symbol) {
        this.symbol = symbol;
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
        return "minecraft:" + prefix + symbol;
    }

    public String getPotionTag() {
        return getPotionTag(false, false);
    }

    @Override
    public String toString() {
        return "minecraft:" + symbol;
    }
}
