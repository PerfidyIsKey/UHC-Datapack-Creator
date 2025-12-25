package uhc.score;

import java.util.Objects;

/**
 * 🧮 **Scoreboard Operation Registry**
 * <p>
 * Defines the mathematical and logical operations used to modify scores
 * between two objectives or players. These operators are used primarily
 * in the {@code /scoreboard players operation} command.
 * </p>
 */
public enum OperationType {

    // --- 🔢 Arithmetic Operations ---

    /**
     * **Assignment (=)**
     * <p>Sets the target score to be equal to the source score.</p>
     */
    ASSIGNMENT("="),

    /**
     * **Addition (+=)**
     * <p>Adds the source score to the target score.</p>
     */
    ADDITION("+="),

    /**
     * **Subtraction (-=)**
     * <p>Subtracts the source score from the target score.</p>
     */
    SUBTRACTION("-="),

    /**
     * **Multiplication (*=)**
     * <p>Multiplies the target score by the source score.</p>
     */
    MULTIPLICATION("*="),

    /**
     * **Floor Division (/=)**
     * <p>Divides the target score by the source score and rounds down to the nearest integer.</p>
     */
    FLOOR_DIVISION("/="),

    /**
     * **Modulus (%=)**
     * <p>Sets the target score to the remainder of the division between the target and source.</p>
     */
    MODULUS("%="),

    // --- ⚖️ Logical & Comparison Operations ---

    /**
     * **Value Swapping (><)**
     * <p>Swaps the scores of the target and the source.</p>
     */
    SWAPPING("><"),

    /**
     * **Minimum Selection (<)**
     * <p>Updates the target score only if the source score is smaller than the current target score.</p>
     */
    CHOOSING_MINIMUM("<"),

    /**
     * **Maximum Selection (>)**
     * <p>Updates the target score only if the source score is larger than the current target score.</p>
     */
    CHOOSING_MAXIMUM(">");

    // --- ⚙️ Internal State ---

    /** The literal symbol used by Minecraft's command engine for this operation. */
    private final String operator;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for the operation types.
     * @param operator The exact string operator (e.g., "+=").
     */
    OperationType(String operator) {
        this.operator = Objects.requireNonNull(operator, "Operator symbol cannot be null.");
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the raw operator symbol.
     * <p><b>Example:</b> {@code OperationType.ADDITION.getOperator()} returns {@code "+="}.</p>
     * @return The operator string.
     */
    public String getOperator() {
        return this.operator;
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves an OperationType based on its symbol.
     * <p><b>Error Catching:</b> Iterates through the registry to find a symbol match.
     * If the input is null or the symbol is unrecognized, it returns {@link #ASSIGNMENT}
     * as a safe default to prevent command syntax errors.</p>
     * @param symbol The raw symbol (e.g., "*=", "><").
     * @return The matching {@link OperationType}, or {@link #ASSIGNMENT} as a fallback.
     */
    public static OperationType fromSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return ASSIGNMENT;
        }

        String target = symbol.trim();
        for (OperationType type : values()) {
            if (type.operator.equals(target)) {
                return type;
            }
        }

        return ASSIGNMENT;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the operator symbol for direct use in command concatenation.
     * <p><b>Implementation:</b> Returns the value of {@link #getOperator()}.</p>
     * @return The symbol string.
     */
    @Override
    public String toString() {
        return operator;
    }
}