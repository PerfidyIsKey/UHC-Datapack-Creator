package Enums;

public enum ComparatorType {
    add("+="),
    equal("="),
    greater(">"),
    less("<"),
    multiply("*="),
    subtract("-=");

    private final String symbol;

    ComparatorType(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
            return symbol;
        }
}
