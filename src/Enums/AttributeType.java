package Enums;

public enum AttributeType {
    ARMOR("armor"),
    ATTACK_DAMAGE("attack_damage"),
    JUMP_STRENGTH("jump_strength"),
    MAX_HEALTH("max_health"),
    MOVEMENT_SPEED("movement_speed"),
    SCALE("scale"),
    WAYPOINT_TRANSMIT_RANGE("waypoint_transmit_range");

    private final String symbol;

    AttributeType(String symbol) {
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
