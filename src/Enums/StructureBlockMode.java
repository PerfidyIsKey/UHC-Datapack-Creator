package Enums;

public enum StructureBlockMode {
    CORNER("corner"),
    DATA("data"),
    LOAD("load"),
    SAVE("save");

    private final String symbol;

    StructureBlockMode(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
