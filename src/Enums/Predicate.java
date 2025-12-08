package Enums;

public enum Predicate {
    LEAVES("leaves");

    private final String symbol;

    Predicate(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "#minecraft:" + symbol;
    }
}