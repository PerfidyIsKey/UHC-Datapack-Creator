package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.score.*;
import uhc.text.TextComponent;
import uhc.text.TextStyle;

/**
 * 📊 **Scoreboard Command Builder**
 * <p>
 * Implements the fluent API for constructing the Minecraft {@code /scoreboard} command,
 * covering both {@code objectives} and {@code players} sub-commands with their various options.
 * </p>
 */
public class ScoreboardCommand implements MinecraftCommand {
    private final ScoreboardAction action;
    private ScoreboardObjective objective;
    private ScoreboardCriteria criteria;

    // --- Core Fields ---
    private String displayName; // Used for OBJECTIVES ADD <objective> <criteria> [<displayName>]
    private TextComponent fixedNumberFormatComponent;
    private ScoreValueSubAction scoreActionType;
    private NumberFormatSubAction numberFormatType;
    private PlayerDisplayNameSubAction playerDisplayNameAction;
    private PlayerSubActionType playerSubActionType;
    private Boolean autoUpdateValue;
    private Boolean enable;

    // NEW FIELD to trigger the default numberformat display command
    private Boolean displayNumberFormat;

    // --- Existing Fields ---
    private DisplaySlot slot;
    private TextComponent component; // Used for OBJECTIVES MODIFY displayname and PLAYERS DISPLAY NAME <text>
    private TextStyle style;
    private ListRenderType renderType;
    private Entity target;
    private Integer score;
    private OperationType operation;
    private Entity source;
    private ScoreboardObjective sourceObjective;

    /**
     * Private constructor to enforce use of the static factory method.
     * @param action The required {@code ScoreboardAction} (OBJECTIVES or PLAYERS).
     */
    private ScoreboardCommand(ScoreboardAction action) {
        this.action = action;
        this.scoreActionType = ScoreValueSubAction.SET;
        this.numberFormatType = NumberFormatSubAction.DEFAULT;
        this.playerDisplayNameAction = PlayerDisplayNameSubAction.DEFAULT;
        this.playerSubActionType = PlayerSubActionType.NONE;
        this.displayNumberFormat = false; // Initialize the new flag
    }

    // --- Factory and Basic Setters ---

    public static ScoreboardCommand create(ScoreboardAction action) {
        return new ScoreboardCommand(action);
    }

    public ScoreboardCommand target(Entity target) {
        this.target = target;
        return this;
    }

    public ScoreboardCommand objective(ScoreboardObjective objective) {
        this.objective = objective;
        return this;
    }

    public ScoreboardCommand criteria(ScoreboardCriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    // --- Custom Setters for New Functionality ---

    /**
     * Sets the display name for a new objective (OBJECTIVES ADD).
     * The input String is internally wrapped by the command logic if needed.
     */
    public ScoreboardCommand displayName(String name) {
        this.displayName = name;
        return this;
    }

    /**
     * Sets the display slot for the objective (e.g., {@code sidebar}, {@code list}).
     */
    public ScoreboardCommand slot(DisplaySlot slot) {
        this.slot = slot;
        return this;
    }

    /**
     * Sets the rich JSON text component for the objective title (used with {@code OBJECTIVES MODIFY displayname}).
     */
    public ScoreboardCommand component(TextComponent component) {
        this.component = component;
        return this;
    }

    /**
     * Sets the score action type to ADD. Requires {@code score(int)} to be called next.
     */
    public ScoreboardCommand add() {
        this.scoreActionType = ScoreValueSubAction.ADD;
        return this;
    }

    /**
     * Sets the score action type to REMOVE. Requires {@code score(int)} to be called next.
     */
    public ScoreboardCommand remove() {
        this.scoreActionType = ScoreValueSubAction.REMOVE;
        return this;
    }

    /**
     * Explicitly sets the intended player sub-action to GET.
     */
    public ScoreboardCommand getScore() {
        this.playerSubActionType = PlayerSubActionType.GET;
        return this;
    }

    /**
     * Explicitly sets the intended player sub-action to RESET.
     */
    public ScoreboardCommand reset() {
        this.playerSubActionType = PlayerSubActionType.RESET;
        return this;
    }

    /**
     * Enables a player's ability to trigger an objective (used with {@code PLAYERS ENABLE}).
     */
    public ScoreboardCommand enable() {
        this.enable = true;
        return this;
    }

    /**
     * Sets the specific score value (used with {@code PLAYERS SET/ADD/REMOVE}).
     */
    public ScoreboardCommand score(int score) {
        this.score = score;
        return this;
    }

    /**
     * Sets the operation type and sources (used with {@code PLAYERS OPERATION}).
     */
    public ScoreboardCommand operation(OperationType operation, Entity source, ScoreboardObjective sourceObjective) {
        this.operation = operation;
        this.source = source;
        this.sourceObjective = sourceObjective;
        return this;
    }

    // --- OBJECTIVES MODIFY Sub-commands ---

    /**
     * Sets the value for {@code OBJECTIVES MODIFY displayautoupdate <value>}.
     */
    public ScoreboardCommand displayAutoUpdate(boolean value) {
        this.autoUpdateValue = value;
        return this;
    }

    /**
     * Sets the list render type (e.g., {@code hearts}, {@code integer}).
     */
    public ScoreboardCommand renderType(ListRenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    // --- Number Format Sub-commands ---

    /**
     * Triggers the default/reset display number format command for players:
     * {@code scoreboard players display numberformat <targets> <objective>} (no modifiers).
     */
    public ScoreboardCommand displayNumberFormat() {
        this.displayNumberFormat = true;
        this.numberFormatType = NumberFormatSubAction.DEFAULT; // Ensure this is default if called alone
        return this;
    }

    /**
     * Sets the number format to default/reset (used with {@code OBJECTIVES MODIFY}).
     */
    public ScoreboardCommand numberFormatDefault() {
        this.numberFormatType = NumberFormatSubAction.DEFAULT;
        return this;
    }

    /**
     * Sets the number format to blank (used with {@code OBJECTIVES MODIFY} or {@code PLAYERS DISPLAY}).
     */
    public ScoreboardCommand numberFormatBlank() {
        this.numberFormatType = NumberFormatSubAction.BLANK;
        this.displayNumberFormat = true;
        return this;
    }

    /**
     * Sets the number format to fixed, using a custom component (used with {@code OBJECTIVES MODIFY} or {@code PLAYERS DISPLAY}).
     */
    public ScoreboardCommand numberFormatFixed(TextComponent component) {
        this.numberFormatType = NumberFormatSubAction.FIXED;
        this.fixedNumberFormatComponent = component;
        this.displayNumberFormat = true;
        return this;
    }

    /**
     * Sets the number format to styled, using a custom style (used with {@code OBJECTIVES MODIFY} or {@code PLAYERS DISPLAY}).
     */
    public ScoreboardCommand numberFormatStyled(TextStyle style) {
        this.numberFormatType = NumberFormatSubAction.STYLED;
        this.style = style;
        this.displayNumberFormat = true;
        return this;
    }

    // --- Player Display Name Sub-commands ---

    /**
     * Sets the scoreholder's name format to the default player name (used with {@code PLAYERS DISPLAY NAME}).
     */
    public ScoreboardCommand playerDisplayNameDefault() {
        this.playerDisplayNameAction = PlayerDisplayNameSubAction.DEFAULT;
        return this;
    }

    /**
     * Sets the scoreholder's name format to a custom component (used with {@code PLAYERS DISPLAY NAME <text>}).
     */
    public ScoreboardCommand playerDisplayName(TextComponent customText) {
        this.playerDisplayNameAction = PlayerDisplayNameSubAction.CUSTOM_TEXT;
        this.component = customText; // Reuse 'component' field for the custom text
        return this;
    }


    // --- Command Generation ---

    /**
     * Generates the final Minecraft command string based on the set parameters.
     * @return A valid Minecraft command string.
     * @throws IllegalStateException If mandatory parameters are missing or incorrect combinations are used.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("scoreboard ");

        // Convert enum to lowercase command word (objectives or players)
        sb.append(action).append(" ");

        switch (action) {
            case OBJECTIVES:
                if (objective == null && slot == null) {
                    // 1. scoreboard objectives list
                    sb.append("list");
                } else if (criteria != null) {
                    // 2. scoreboard objectives add <objective> <criteria> [<displayName>]
                    if (objective == null) throw new IllegalStateException("Objective must be set for 'OBJECTIVES ADD'.");

                    sb.append("add ").append(objective).append(" ").append(criteria.toString().toLowerCase());

                    if (displayName != null) {
                        // Wrapping String in quotes for command syntax
                        sb.append(" \"").append(displayName).append("\"");
                    }
                } else if (slot != null) {
                    // 4. scoreboard objectives setdisplay <slot> [<objective>]
                    sb.append("setdisplay ").append(slot.toString().toLowerCase());
                    if (objective != null) {
                        sb.append(" ").append(objective); // Objective name
                    }
                } else {
                    // Sub-actions: REMOVE or MODIFY

                    if (objective == null) {
                        throw new IllegalStateException("'OBJECTIVES' command requires a sub-action (list, add, remove, modify, setdisplay).");
                    }

                    // Check for MODIFY sub-actions
                    if (autoUpdateValue != null || component != null || renderType != null || numberFormatType != NumberFormatSubAction.DEFAULT) {
                        // OBJECTIVES MODIFY <name> <property> <value>
                        sb.append("modify ").append(objective);

                        // Only append ONE modification property
                        if (autoUpdateValue != null) {
                            // 5. modify <objective> displayautoupdate <value>
                            sb.append(" displayautoupdate ").append(autoUpdateValue);
                        } else if (component != null) {
                            // 6. modify <objective> displayname <displayName>
                            sb.append(" displayname ").append(component); // TextComponent.toString() is expected to return quoted JSON
                        } else if (renderType != null) {
                            // 11. modify <objective> rendertype (hearts|integer)
                            sb.append(" rendertype ").append(renderType.toString().toLowerCase());
                        } else if (numberFormatType != NumberFormatSubAction.DEFAULT) {
                            // 7, 8, 9, 10. modify <objective> numberformat...
                            sb.append(" numberformat");

                            switch (numberFormatType) {
                                case BLANK: // 8. numberformat blank
                                    sb.append(" blank");
                                    break;
                                case FIXED: // 9. numberformat fixed <component>
                                    if (fixedNumberFormatComponent == null) throw new IllegalStateException("Component must be set for 'numberformat fixed'.");
                                    sb.append(" fixed ").append(fixedNumberFormatComponent);
                                    break;
                                case STYLED: // 10. numberformat styled <style>
                                    if (style == null) throw new IllegalStateException("Style must be set for 'numberformat styled'.");
                                    sb.append(" styled ").append(style.generate());
                                    break;
                                case DEFAULT: // 7. numberformat (default, no args)
                                    // No arguments are added for the default modification
                                    break;
                            }
                        }
                    } else {
                        // 3. scoreboard objectives remove <objective>
                        sb.append("remove ").append(objective);
                    }
                }
                break;

            case PLAYERS:
                // Precedence: OPERATION > ENABLE > DISPLAY > GET/RESET > SCORE MANIPULATION > LIST

                if (operation != null) {
                    // scoreboard players operation <targets> <targetObjective> <operation> <source> <sourceObjective>
                    if (target == null || objective == null || source == null || sourceObjective == null) {
                        throw new IllegalStateException("All target, objective, source, and sourceObjective must be set for 'PLAYERS OPERATION'.");
                    }
                    sb.append("operation ").append(target).append(" ").append(objective)
                            .append(" ").append(operation)
                            .append(" ").append(source).append(" ").append(sourceObjective);
                } else if (enable != null) {
                    // scoreboard players enable <targets> <objective>
                    if (target == null || objective == null) {
                        throw new IllegalStateException("Target and objective must be set for 'PLAYERS ENABLE'.");
                    }
                    sb.append("enable ").append(target).append(" ").append(objective);
                } else if (playerDisplayNameAction != PlayerDisplayNameSubAction.DEFAULT || displayNumberFormat) {
                    // PLAYER DISPLAY sub-commands (name/numberformat)

                    if (target == null || objective == null) {
                        throw new IllegalStateException("Target and objective must be set for 'PLAYERS DISPLAY' commands.");
                    }

                    if (playerDisplayNameAction != PlayerDisplayNameSubAction.DEFAULT) {
                        // scoreboard players display name <targets> <objective> [<text>]
                        sb.append("display name ").append(target).append(" ").append(objective);

                        if (playerDisplayNameAction == PlayerDisplayNameSubAction.CUSTOM_TEXT) {
                            // scoreboard players display name <targets> <objective> <text>
                            if (component == null) throw new IllegalStateException("TextComponent must be set for custom player display name.");
                            sb.append(" ").append(component);
                        }
                    } else {
                        // scoreboard players display numberformat...
                        sb.append("display numberformat ").append(target).append(" ").append(objective);

                        switch (numberFormatType) {
                            case BLANK: // numberformat blank
                                sb.append(" blank");
                                break;
                            case FIXED: // numberformat fixed <contents>
                                if (fixedNumberFormatComponent == null) throw new IllegalStateException("Component must be set for 'numberformat fixed'.");
                                sb.append(" fixed ").append(fixedNumberFormatComponent);
                                break;
                            case STYLED: // numberformat styled <style>
                                if (style == null) throw new IllegalStateException("Style must be set for 'numberformat styled'.");
                                sb.append(" styled ").append(style.generate());
                                break;
                            case DEFAULT:
                                // scoreboard players display numberformat <targets> <objective> (Base command, no extra args)
                                break;
                        }
                    }

                } else if (playerSubActionType == PlayerSubActionType.GET) {
                    // scoreboard players get <target> <objective>
                    if (target == null || objective == null) {
                        throw new IllegalStateException("Target and objective must be set for 'PLAYERS GET'.");
                    }
                    sb.append("get ").append(target).append(" ").append(objective);

                } else if (score != null) {
                    // scoreboard players set/add/remove <targets> <objective> <score>
                    if (target == null || objective == null) {
                        throw new IllegalStateException("Target and objective must be set for 'PLAYERS SET/ADD/REMOVE'.");
                    }
                    sb.append(scoreActionType.toString().toLowerCase()).append(" ").append(target).append(" ").append(objective).append(" ").append(score);

                } else if (playerSubActionType == PlayerSubActionType.RESET) {
                    // scoreboard players reset <targets> [<objective>]
                    if (target == null) {
                        throw new IllegalStateException("Target must be set for 'PLAYERS RESET'.");
                    }
                    sb.append("reset ").append(target);
                    if (objective != null) {
                        sb.append(" ").append(objective);
                    }

                } else if (target != null) {
                    // scoreboard players list [<target>] (list specific scores for one target)
                    sb.append("list ").append(target);
                } else {
                    // scoreboard players list (list all players)
                    sb.append("list");
                }
                break;
        }

        // Final safety check for missing parameters leading to incomplete commands
        String command = sb.toString().trim();
        if (command.endsWith("scoreboard") || command.endsWith("objectives") || command.endsWith("players") || command.matches(".* (display|numberformat|name|operation|list|add|remove|set|reset|enable|get)")) {
            throw new IllegalStateException("Incomplete Scoreboard command structure. Command generated: " + command);
        }

        return command;
    }

    /**
     * Generates and returns the final Minecraft command string.
     */
    @Override
    public String toString() {
        return generate();
    }


    /**
     * Defines the primary actions for the {@code /scoreboard} command.
     */
    public enum ScoreboardAction {
        OBJECTIVES,
        PLAYERS;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * Internal enum to distinguish between score manipulation actions (SET, ADD, REMOVE).
     */
    private enum ScoreValueSubAction {
        SET, ADD, REMOVE
    }

    /**
     * Internal enum to distinguish between explicit player sub-actions (GET, RESET).
     */
    private enum PlayerSubActionType {
        NONE, GET, RESET
    }

    /**
     * Internal enum to distinguish between number format options.
     */
    private enum NumberFormatSubAction {
        DEFAULT, BLANK, FIXED, STYLED
    }

    /**
     * Internal enum to distinguish between player display name options.
     */
    private enum PlayerDisplayNameSubAction {
        DEFAULT, CUSTOM_TEXT
    }
}