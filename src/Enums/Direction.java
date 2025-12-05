package Enums;

public enum Direction {
    DOWN("down"),
    EAST("east"),
    NORTH("north"),
    SOUTH("south"),
    UP("up"),
    WEST("west");

    private final String symbol;

    Direction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
