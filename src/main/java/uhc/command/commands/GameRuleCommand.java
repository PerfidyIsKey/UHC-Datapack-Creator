package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.resource.gameplay.GameRuleId;

import java.util.Objects;

/**
 * ⚙️ **GameRule Command Builder (GameRuleCommand)**
 * <p>
 * Provides a robust, type-safe API for constructing Minecraft {@code /gamerule} commands.
 * This class ensures that rules requiring boolean states cannot be assigned integer values
 * and vice-versa, adhering to Minecraft's strict internal data types.
 * </p>
 * <p>
 * <b>Logic Flow:</b>
 * <ul>
 * <li><b>Query:</b> {@code /gamerule <rule>}</li>
 * <li><b>Set:</b> {@code /gamerule <rule> <value>}</li>
 * </ul>
 * </p>
 */
public class GameRuleCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The specific identifier for the game rule being targeted.
     * This contains the rule's command name and its associated data type.
     */
    private final GameRuleId rule;

    /** * The value to be applied to the game rule.
     * This is stored as an {@link Object} to accommodate both {@link Boolean}
     * and {@link Integer} types. If null, the command is treated as a query.
     */
    private final Object value;

    // --- 🏗️ Private Constructor ---

    /**
     * Constructs a validated GameRuleCommand instance.
     * <p><b>Strict Validation:</b> Ensures the rule is never null.</p>
     * * @param rule  The non-null {@link GameRuleId}.
     * @param value The value to set, or null for a query command.
     * @throws NullPointerException if rule is null.
     */
    private GameRuleCommand(GameRuleId rule, Object value) {
        this.rule = Objects.requireNonNull(rule, "GameRule Error: Rule ID cannot be null.");
        this.value = value;
    }

    // --- 🚀 Static Entry Points ---

    /**
     * Creates a command to query the current value of a game rule.
     * <p>Example: {@code /gamerule doFireTick}</p>
     * * @param rule The rule to query.
     * @return A new GameRuleCommand instance in query mode.
     * @throws NullPointerException if rule is null.
     */
    public static GameRuleCommand query(GameRuleId rule) {
        return new GameRuleCommand(rule, null);
    }

    /**
     * Creates a command to set a boolean-based game rule.
     * <p><b>Type Guard:</b> Validates that the rule is registered as a Boolean type.</p>
     * * @param rule  The rule to modify.
     * @param state The boolean state to apply.
     * @return A new GameRuleCommand instance in set mode.
     * @throws IllegalArgumentException if the rule is actually an Integer-type rule.
     * @throws NullPointerException if rule is null.
     */
    public static GameRuleCommand set(GameRuleId rule, boolean state) {
        validateType(rule, GameRuleId.RuleType.BOOLEAN);
        return new GameRuleCommand(rule, state);
    }

    /**
     * Creates a command to set an integer-based game rule.
     * <p><b>Type Guard:</b> Validates that the rule is registered as an Integer type.</p>
     * * @param rule  The rule to modify.
     * @param value The integer value to apply.
     * @return A new GameRuleCommand instance in set mode.
     * @throws IllegalArgumentException if the rule is actually a Boolean-type rule.
     * @throws NullPointerException if rule is null.
     */
    public static GameRuleCommand set(GameRuleId rule, int value) {
        validateType(rule, GameRuleId.RuleType.INTEGER);
        return new GameRuleCommand(rule, value);
    }

    // --- 🛠️ Validation Logic ---

    /**
     * Internal validator to prevent cross-type pollution between Boolean and Integer rules.
     * <p>
     * If the provided {@link GameRuleId} does not match the {@link GameRuleId.RuleType}
     * expected by the setter, a clear error is thrown to prevent illegal commands.
     * </p>
     * * @param target   The rule being checked.
     * @param expected The type required by the specific factory method.
     * @throws IllegalArgumentException if types do not match.
     */
    private static void validateType(GameRuleId target, GameRuleId.RuleType expected) {
        Objects.requireNonNull(target, "Validation Error: Target rule is null.");
        if (target.getType() != expected) {
            throw new IllegalArgumentException(String.format(
                    "Gamerule Type Mismatch: Rule '%s' requires a %s value, but a %s was provided.",
                    target.getCommandName(), target.getType(), expected
            ));
        }
    }

    // --- 🔍 Accessors ---

    /** * @return The {@link GameRuleId} associated with this command.
     */
    public GameRuleId getRule() {
        return rule;
    }

    /** * @return The value being set, or {@code null} if this is a query command.
     */
    public Object getValue() {
        return value;
    }

    // --- ⚙️ Command Generation ---

    /**
     * Generates the final Minecraft command string for execution.
     * <p><b>No-Fallback:</b> If state is corrupted, a RuntimeException is thrown.</p>
     * * @return The formatted command string (e.g., "gamerule randomTickSpeed 3").
     * @throws RuntimeException if the internal components fail to append.
     */
    @Override
    public String generate() {
        final StringBuilder commandBuilder = new StringBuilder("gamerule ");

        try {
            // Append the lowerCamelCase name of the rule
            commandBuilder.append(this.rule.getCommandName());

            // If value is present, we are in 'Set' mode. Append the value.
            if (this.value != null) {
                commandBuilder.append(" ").append(this.value);
            }

            return commandBuilder.toString();
        } catch (Exception e) {
            // Error Catching: We throw a clear error instead of a default string.
            throw new RuntimeException("CRITICAL: Failed to generate GameRule command for rule: "
                    + (this.rule != null ? this.rule.name() : "UNKNOWN"), e);
        }
    }

    /**
     * Standard representation of the command.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}