package uhc.score;

public enum OperationType {
    ASSIGNMENT("="),
    ADDITION("+="),
    SUBTRACTION("-="),
    MULTIPLICATION("*="),
    FLOOR_DIVISION("/="),
    MODULUS("%="),
    SWAPPING("><"),
    CHOOSING_MINIMUM("<"),
    CHOOSING_MAXIMUM(">");

    private final String operator;
    OperationType(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return operator;
    }
}
