package commands;

import uhc.game.GameRuleId;

/**
 * Builds the Minecraft /gamerule command.
 * Allows for querying (no value) or setting the value (boolean or integer).
 */
public class GameRule {
    private final GameRuleId name;
    private Object value; // Stored as Object to accommodate Integer or Boolean

    private GameRule(GameRuleId name) {
        this.name = name;
    }

    public static GameRule create(GameRuleId name) {
        return new GameRule(name);
    }

    // --- Action Methods ---

    /**
     * Sets the game rule value to a boolean (true/false).
     * This is the appropriate setter for most game rules (e.g., doDaylightCycle).
     */
    public GameRule booleanValue(boolean value) {
        this.value = value;
        return this;
    }

    /**
     * Sets the game rule value to an integer.
     * This is the appropriate setter for specific rules (e.g., maxEntityCramming, randomTickSpeed).
     * @param value Must be within the standard integer range (-2147483648 to 2147483647).
     */
    public GameRule intValue(int value) {
        // Integer range is guaranteed by the 'int' type, but we could add boundary checks if needed.
        this.value = value;
        return this;
    }

    /**
     * Specifies that the command is a query, not a set operation.
     * This ensures no value is appended to the command.
     */
    public GameRule query() {
        this.value = null;
        return this;
    }

    // --- Build Method ---

    public String build() {
        StringBuilder sb = new StringBuilder("gamerule ");

        // Append the rule name (which uses the overridden toString() for lower-camel-case)
        sb.append(name);

        // Append the value if present
        if (value != null) {
            // Error Checking (Example of run-time validation, though type safety is preferred)
            if (!(value instanceof Boolean || value instanceof Integer)) {
                throw new IllegalStateException("Game rule value must be a Boolean or an Integer.");
            }

            sb.append(" ").append(value);
        }

        return sb.toString();
    }

    // The previous generic 'value' method is removed to enforce type safety
}