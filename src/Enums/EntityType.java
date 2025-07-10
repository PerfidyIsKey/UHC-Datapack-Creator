package Enums;

public enum EntityType {
    AREA_EFFECT_CLOUD("area_effect_cloud"),
    ARMOR_STAND("armor_stand"),
    DOLPHIN("dolphin"),
    FALLING_BLOCK("falling_block"),
    FIREWORK_ROCKET("firework_rocket"),
    HORSE("horse"),
    ITEM("item"),
    MARKER("marker"),
    WOLF("wolf");

    private final String symbol;

    EntityType(String symbol) {
        this.symbol = symbol;
    }

    public String setNamespace(Namespace namespace) {
        return namespace + ":" + symbol;
    }

    @Override
    public String toString() {
        return "minecraft:" + symbol;
    }
}