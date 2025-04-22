package Enums;

public enum Duration {
    days("d"),
    seconds("s"),
    ticks("t");

    private final String symbol;

    Duration(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return symbol;
    }
}
