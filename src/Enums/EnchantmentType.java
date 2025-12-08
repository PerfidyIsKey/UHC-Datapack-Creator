package Enums;

public enum EnchantmentType {
    EFFICIENCY,
    FIRE_ASPECT,
    IMPALING,
    LOYALTY,
    LURE,
    PIERCING,
    POWER,
    SHARPNESS,
    VANISHING_CURSE;

    private final String namespace;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    EnchantmentType(String namespace) {
        this.namespace = namespace;
    }

    EnchantmentType() {
        this.namespace = DEFAULT_NAMESPACE;
    }

    /**
     * Returns the full resource location string: {@code namespace:keyname}.
     * e.g., "minecraft:power".
     */
    @Override
    public String toString() {
        return namespace + ":" + this.name().toLowerCase();
    }
}

