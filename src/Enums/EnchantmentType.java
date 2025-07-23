package Enums;

public enum EnchantmentType {
    EFFICIENCY("efficiency"),
    FIRE_ASPECT("fire_aspect"),
    IMPALING("impaling"),
    LOYALTY("loyalty"),
    LURE("lure"),
    PIERCING("piercing"),
    POWER("power"),
    SHARPNESS("sharpness"),
    VANISHING_CURSE("vanishing_curse");

    private final String symbol;

    EnchantmentType(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "minecraft:" + symbol;
    }
}

