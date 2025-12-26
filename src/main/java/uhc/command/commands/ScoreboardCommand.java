package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.score.*;
import uhc.text.TextComponent;
import uhc.text.TextStyle;

import java.util.Objects;

/**
 * 📊 **Scoreboard Command Builder**
 * <p>
 * Implements a fluent API for constructing Minecraft {@code /scoreboard} commands.
 * This builder covers both the {@code objectives} and {@code players} sub-commands,
 * supporting modern features like number formatting, custom display names, and
 * score manipulation.
 * </p>
 */
public class ScoreboardCommand implements MinecraftCommand {

    // --- ⚙️ Core Configuration Fields ---

    /** The primary action of the command: either 'objectives' or 'players'. */
    private final ScoreboardAction action;

    /** The specific scoreboard objective to act upon. */
    private ScoreboardObjective objective;

    /** The criteria used when creating a new objective (e.g., 'dummy', 'health'). */
    private ScoreboardCriteria criteria;

    /** The plain-text display name used during objective creation (OBJECTIVES ADD). */
    private String displayName;

    /** The target entity or selector for player-based actions. */
    private Entity target;

    // --- 🎨 Display & Formatting Fields ---

    /** The display slot for the objective (e.g., 'sidebar', 'list', 'below_name'). */
    private DisplaySlot slot;

    /** A rich JSON text component used for objective titles or custom player names. */
    private TextComponent component;

    /** A text component specifically used for 'fixed' number formats. */
    private TextComponent fixedNumberFormatComponent;

    /** The visual style (color, bold, etc.) used for 'styled' number formats. */
    private TextStyle style;

    /** Defines how lists of scores are rendered (e.g., 'hearts' or 'integer'). */
    private ListRenderType renderType;

    // --- 🔢 Value & Logic Fields ---

    /** The numerical score value to set, add, or remove. */
    private Integer score;

    /** The mathematical operation to perform between two scores (e.g., +=, -=). */
    private OperationType operation;

    /** The source entity used for score operations. */
    private Entity source;

    /** The source objective used for score operations. */
    private ScoreboardObjective sourceObjective;

    /** Determines if an objective title updates automatically. */
    private Boolean autoUpdateValue;

    /** Used for the 'PLAYERS ENABLE' sub-command. */
    private Boolean enable;

    /** Flag to trigger the 'PLAYERS DISPLAY NUMBERFORMAT' sub-command. */
    private Boolean displayNumberFormat;

    // --- 🚦 Internal Sub-Action States ---

    private ScoreValueSubAction scoreActionType = ScoreValueSubAction.SET;
    private NumberFormatSubAction numberFormatType = NumberFormatSubAction.DEFAULT;
    private PlayerDisplayNameSubAction playerDisplayNameAction = PlayerDisplayNameSubAction.DEFAULT;
    private PlayerSubActionType playerSubActionType = PlayerSubActionType.NONE;

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce use of the static factory method.
     * @param action The required {@link ScoreboardAction}.
     */
    private ScoreboardCommand(ScoreboardAction action) {
        this.action = Objects.requireNonNull(action, "ScoreboardAction cannot be null.");
        this.displayNumberFormat = false;
    }

    /**
     * Creates a new fluent builder for a scoreboard command.
     * @param action The root action (OBJECTIVES or PLAYERS).
     * @return A new builder instance.
     */
    public static ScoreboardCommand create(ScoreboardAction action) {
        return new ScoreboardCommand(action);
    }

    // --- 🛠️ Primary Builder Methods ---

    /**
     * Sets the primary target entity for the command.
     * @param target The entity or selector.
     * @return This builder instance.
     */
    public ScoreboardCommand target(Entity target) {
        this.target = target;
        return this;
    }

    /**
     * Sets the objective to be modified or referenced.
     * @param objective The objective object.
     * @return This builder instance.
     */
    public ScoreboardCommand objective(ScoreboardObjective objective) {
        this.objective = objective;
        return this;
    }

    /**
     * Sets the criteria for objective creation.
     * @param criteria The scoreboard criteria.
     * @return This builder instance.
     */
    public ScoreboardCommand criteria(ScoreboardCriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    /**
     * Sets the display name for a new objective (used in OBJECTIVES ADD).
     * @param name The display name string.
     * @return This builder instance.
     */
    public ScoreboardCommand displayName(String name) {
        this.displayName = name;
        return this;
    }

    /**
     * Sets the display slot (e.g. sidebar) for an objective.
     * @param slot The type-safe display slot.
     * @return This builder instance.
     */
    public ScoreboardCommand slot(DisplaySlot slot) {
        this.slot = slot;
        return this;
    }

    /**
     * Sets the JSON TextComponent for names or titles.
     * @param component The rich text component.
     * @return This builder instance.
     */
    public ScoreboardCommand component(TextComponent component) {
        this.component = component;
        return this;
    }

    // --- ⚔️ Player Score Manipulation ---

    /** Sets the sub-action to ADD a score value. */
    public ScoreboardCommand add() {
        this.scoreActionType = ScoreValueSubAction.ADD;
        return this;
    }

    /** Sets the sub-action to REMOVE a score value. */
    public ScoreboardCommand remove() {
        this.scoreActionType = ScoreValueSubAction.REMOVE;
        return this;
    }

    /** Sets the sub-action to GET a player's score. */
    public ScoreboardCommand getScore() {
        this.playerSubActionType = PlayerSubActionType.GET;
        return this;
    }

    /** Sets the sub-action to RESET a player's score. */
    public ScoreboardCommand reset() {
        this.playerSubActionType = PlayerSubActionType.RESET;
        return this;
    }

    /** Sets the sub-action to ENABLE a trigger objective for a player. */
    public ScoreboardCommand enable() {
        this.enable = true;
        return this;
    }

    /** Sets the numerical score for modification. */
    public ScoreboardCommand score(int score) {
        this.score = score;
        return this;
    }

    /** Performs a math operation between two objectives. */
    public ScoreboardCommand operation(OperationType operation, Entity source, ScoreboardObjective sourceObjective) {
        this.operation = operation;
        this.source = source;
        this.sourceObjective = sourceObjective;
        return this;
    }

    // --- ⚙️ Objectives Modification ---

    /** Sets whether objective scores update automatically. */
    public ScoreboardCommand displayAutoUpdate(boolean value) {
        this.autoUpdateValue = value;
        return this;
    }

    /** Sets the render type (HEARTS vs INTEGER) for the player list. */
    public ScoreboardCommand renderType(ListRenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    // --- 🔢 Number Formatting ---

    /** Resets the number format to the vanilla default. */
    public ScoreboardCommand numberFormatDefault() {
        this.numberFormatType = NumberFormatSubAction.DEFAULT;
        this.displayNumberFormat = true;
        return this;
    }

    /** Hides the numbers on the scoreboard. */
    public ScoreboardCommand numberFormatBlank() {
        this.numberFormatType = NumberFormatSubAction.BLANK;
        this.displayNumberFormat = true;
        return this;
    }

    /** Sets the numbers to a fixed custom text. */
    public ScoreboardCommand numberFormatFixed(TextComponent component) {
        this.numberFormatType = NumberFormatSubAction.FIXED;
        this.fixedNumberFormatComponent = component;
        this.displayNumberFormat = true;
        return this;
    }

    /** Applies a specific style to the scoreboard numbers. */
    public ScoreboardCommand numberFormatStyled(TextStyle style) {
        this.numberFormatType = NumberFormatSubAction.STYLED;
        this.style = style;
        this.displayNumberFormat = true;
        return this;
    }

    // --- 👤 Player Display Customization ---

    /** Resets the player's displayed name to default. */
    public ScoreboardCommand playerDisplayNameDefault() {
        this.playerDisplayNameAction = PlayerDisplayNameSubAction.DEFAULT;
        return this;
    }

    /** Sets a custom display name for a specific scoreholder. */
    public ScoreboardCommand playerDisplayName(TextComponent customText) {
        this.playerDisplayNameAction = PlayerDisplayNameSubAction.CUSTOM_TEXT;
        this.component = customText;
        return this;
    }

    // --- 🛰️ Command Generation Logic ---

    /**
     * Generates the final Minecraft command string.
     * <p><b>Error Catching:</b> Validates that all required fields for specific
     * sub-commands are present to prevent invalid command execution.</p>
     * @return The formatted command string.
     * @throws IllegalStateException if the command structure is invalid or missing data.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("scoreboard ");
        sb.append(action).append(" ");

        switch (action) {
            case OBJECTIVES -> handleObjectivesAction(sb);
            case PLAYERS -> handlePlayersAction(sb);
        }

        String command = sb.toString().trim();
        validateFinalCommand(command);
        return command;
    }

    /**
     * Internal logic for /scoreboard objectives.
     */
    private void handleObjectivesAction(StringBuilder sb) {
        if (objective == null && slot == null) {
            sb.append("list");
        } else if (criteria != null) {
            if (objective == null) throw new IllegalStateException("Objective is required for 'add'.");
            sb.append("add ").append(objective).append(" ").append(criteria.toString().toLowerCase());
            if (displayName != null) sb.append(" \"").append(displayName).append("\"");
        } else if (slot != null) {
            sb.append("setdisplay ").append(slot.getSlotName());
            if (objective != null) sb.append(" ").append(objective);
        } else {
            if (objective == null) throw new IllegalStateException("Objective name is required for modify/remove.");

            // Handle OBJECTIVES MODIFY
            if (isObjectivesModify()) {
                sb.append("modify ").append(objective);
                if (autoUpdateValue != null) sb.append(" displayautoupdate ").append(autoUpdateValue);
                else if (component != null) sb.append(" displayname ").append(component);
                else if (renderType != null) sb.append(" rendertype ").append(renderType.toString().toLowerCase());
                else handleNumberFormat(sb);
            } else {
                sb.append("remove ").append(objective);
            }
        }
    }

    /**
     * Internal logic for /scoreboard players.
     */
    private void handlePlayersAction(StringBuilder sb) {
        if (operation != null) {
            validatePlayerParams(true);
            if (source == null || sourceObjective == null) throw new IllegalStateException("Operation source/objective missing.");
            sb.append("operation ").append(target).append(" ").append(objective).append(" ").append(operation)
                    .append(" ").append(source).append(" ").append(sourceObjective);
        } else if (enable != null) {
            validatePlayerParams(true);
            sb.append("enable ").append(target).append(" ").append(objective);
        } else if (playerDisplayNameAction != PlayerDisplayNameSubAction.DEFAULT || displayNumberFormat) {
            validatePlayerParams(true);
            handlePlayerDisplay(sb);
        } else if (playerSubActionType == PlayerSubActionType.GET) {
            validatePlayerParams(true);
            sb.append("get ").append(target).append(" ").append(objective);
        } else if (score != null) {
            validatePlayerParams(true);
            sb.append(scoreActionType.toString().toLowerCase()).append(" ").append(target).append(" ").append(objective).append(" ").append(score);
        } else if (playerSubActionType == PlayerSubActionType.RESET) {
            validatePlayerParams(false);
            sb.append("reset ").append(target);
            if (objective != null) sb.append(" ").append(objective);
        } else {
            sb.append("list");
            if (target != null) sb.append(" ").append(target);
        }
    }

    // --- 🔧 Helper Validation & Formatting ---

    private boolean isObjectivesModify() {
        return autoUpdateValue != null || component != null || renderType != null || displayNumberFormat;
    }

    private void handleNumberFormat(StringBuilder sb) {
        sb.append(" numberformat");
        switch (numberFormatType) {
            case BLANK -> sb.append(" blank");
            case FIXED -> {
                if (fixedNumberFormatComponent == null) throw new IllegalStateException("Fixed component missing.");
                sb.append(" fixed ").append(fixedNumberFormatComponent);
            }
            case STYLED -> {
                if (style == null) throw new IllegalStateException("Style missing for styled format.");
                sb.append(" styled ").append(style.generate());
            }
            case DEFAULT -> {}
        }
    }

    private void handlePlayerDisplay(StringBuilder sb) {
        if (playerDisplayNameAction != PlayerDisplayNameSubAction.DEFAULT) {
            sb.append("display name ").append(target).append(" ").append(objective);
            if (playerDisplayNameAction == PlayerDisplayNameSubAction.CUSTOM_TEXT) {
                if (component == null) throw new IllegalStateException("Custom name text missing.");
                sb.append(" ").append(component);
            }
        } else {
            sb.append("display numberformat ").append(target).append(" ").append(objective);
            handleNumberFormat(sb);
        }
    }

    private void validatePlayerParams(boolean requireObjective) {
        if (target == null) throw new IllegalStateException("Target is required for this player action.");
        if (requireObjective && objective == null) throw new IllegalStateException("Objective is required for this player action.");
    }

    private void validateFinalCommand(String command) {
        if (command.matches(".* (display|numberformat|name|operation|list|add|remove|set|reset|enable|get)$")) {
            throw new IllegalStateException("Incomplete command sequence generated: " + command);
        }
    }

    @Override
    public String toString() {
        return generate();
    }

    // --- 🗂️ Internal Enums ---

    public enum ScoreboardAction { OBJECTIVES, PLAYERS; @Override public String toString() { return name().toLowerCase(); } }
    private enum ScoreValueSubAction { SET, ADD, REMOVE }
    private enum PlayerSubActionType { NONE, GET, RESET }
    private enum NumberFormatSubAction { DEFAULT, BLANK, FIXED, STYLED }
    private enum PlayerDisplayNameSubAction { DEFAULT, CUSTOM_TEXT }
}