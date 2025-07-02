package Enums;

public enum Duration {
    DAYS("d"),
    SECONDS("s"),
    TICKS("t");

    private final String symbol;

    Duration(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return symbol;
    }
}
