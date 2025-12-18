package uhc.score;

import java.util.Objects;

/**
 * ⚖️ **Scoreboard Comparator**
 * <p>
 * Defines the mathematical symbols used to compare two scores in
 * {@code /execute if score} commands.
 * </p>
 */
public enum ComparatorType {
    /** Matches if the target score is exactly equal to the source score. */
    EQUAL("="),

    /** Matches if the target score is strictly greater than the source score. */
    GREATER(">"),

    /** Matches if the target score is greater than or equal to the source score. */
    GREATEREQUAL(">="),

    /** Matches if the target score is strictly less than the source score. */
    LESS("<"),

    /** Matches if the target score is less than or equal to the source score. */
    LESSEQUAL("<=");

    private final String symbol;

    ComparatorType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Safely retrieves a ComparatorType based on its mathematical symbol.
     * @param symbol The symbol to check (e.g., ">=").
     * @return The matching {@link ComparatorType}, or {@link #EQUAL} as a safe fallback.
     */
    public static ComparatorType fromSymbol(String symbol) {
        if (symbol == null) return EQUAL;
        for (ComparatorType type : values()) {
            if (type.symbol.equals(symbol.trim())) {
                return type;
            }
        }
        return EQUAL;
    }

    /**
     * Returns the symbol required for Minecraft command syntax.
     */
    @Override
    public String toString() {
        return symbol;
    }
}