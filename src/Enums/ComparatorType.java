package Enums;

public enum ComparatorType {
    equal("="),
    greater(">"),
    less("<");

    private final String symbol;

    ComparatorType(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
            return symbol;
        }
}
