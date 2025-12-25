package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.resource.gameplay.GameRuleId;

import java.util.Objects;

/**
 * ⚙️ **GameRule Command Builder**
 * <p>
 * Provides a structured way to build {@code /gamerule} commands.
 * This builder supports two main modes:
 * <ul>
 * <li><b>Query:</b> {@code /gamerule <rule>} - Returns the current value.</li>
 * <li><b>Set:</b> {@code /gamerule <rule> <value>} - Updates the rule.</li>
 * </ul>
 * </p>
 */
public class GameRuleCommand implements MinecraftCommand {
    private final GameRuleId name;

    // Stored as Object to accommodate Integer or Boolean (Minecraft's two supported types)
    private Object value;

    private GameRuleCommand(GameRuleId name) {
        this.name = Objects.requireNonNull(name, "GameRule name cannot be null.");
    }

    /**
     * Initializes a new GameRule builder for a specific rule.
     * @param name The namespaced ID of the rule (e.g., randomTickSpeed).
     * @return A new GameRuleCommand instance.
     */
    public static GameRuleCommand create(GameRuleId name) {
        return new GameRuleCommand(name);
    }

    // --- Action Methods ---

    /**
     * Sets the value for boolean rules.
     * <p>Examples: {@code doDaylightCycle}, {@code keepInventory}, {@code doFireTick}.</p>
     * @param value The boolean state to set.
     */
    public GameRuleCommand booleanValue(boolean value) {
        this.value = value;
        return this;
    }

    /**
     * Sets the value for integer rules.
     * <p>Examples: {@code randomTickSpeed}, {@code spawnRadius}, {@code maxEntityCramming}.</p>
     * @param value The integer value to set.
     */
    public GameRuleCommand intValue(int value) {
        this.value = value;
        return this;
    }

    /**
     * Marks the command as a query.
     * <p>Calling this removes any previously set value, resulting in {@code /gamerule <name>}.</p>
     */
    public GameRuleCommand query() {
        this.value = null;
        return this;
    }

    // --- Build Method ---

    /**
     * Generates the command string.
     * <p>Note: Minecraft game rules are case-sensitive and usually lower-camelCase.</p>
     * @return The formatted command (e.g., "gamerule doMobSpawning false").
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("gamerule ");

        // Append the rule name (Assumes GameRuleId.toString() returns the correct camelCase name)
        sb.append(name);

        // If value is null, this remains a query command.
        if (value != null) {
            // Validation check to ensure internal state hasn't been corrupted.
            if (!(value instanceof Boolean || value instanceof Integer)) {
                throw new IllegalStateException("Invalid value type: " + value.getClass().getSimpleName() +
                        ". Game rules only support Boolean or Integer.");
            }

            // Append a space and the value (e.g., " true" or " 3")
            sb.append(" ").append(value);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}