package Enums;

public enum ComparatorType {
    ADD("+="),
    EQUAL("="),
    GREATER(">"),
    GREATEREQUAL(">="),
    LESS("<"),
    MULTIPLY("*="),
    SUBTRACT("-=");

    private final String symbol;

    ComparatorType(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
            return symbol;
        }
}
