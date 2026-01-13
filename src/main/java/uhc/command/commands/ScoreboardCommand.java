package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.score.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 📊 **Scoreboard Command Builder (ScoreboardCommand)**
 * <p>
 * A flattened, immutable API for constructing Minecraft {@code /scoreboard} commands.
 * This class provides direct static access to both <b>Objective</b> management and
 * <b>Player</b> score manipulation, ensuring type-safe command generation without
 * intermediate builder objects.
 * </p>
 * <p>
 * <b>Strict Policy:</b> This class does not provide default fallbacks for null or
 * invalid inputs; instead, it throws explicit exceptions to ensure command integrity.
 * </p>
 */
public class ScoreboardCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The internal sub-command string (e.g., "players set @p kills 5").
     * This string is combined with the "scoreboard" prefix during generation.
     */
    private final String command;

    // --- 🏗️ Private Constructor ---

    /**
     * Constructs an immutable ScoreboardCommand instance.
     * @param command The fully formatted scoreboard sub-action string.
     * @throws NullPointerException if the command string is null.
     */
    private ScoreboardCommand(String command) {
        this.command = Objects.requireNonNull(command, "Scoreboard Error: Internal command component cannot be null.");
    }

    // --- 🎯 Static Factory Methods: Objectives ---

    /**
     * Constructs a command to register a new objective in the scoreboard.
     * <p>Syntax: {@code /scoreboard objectives add <name> <criteria> [<displayName>]}</p>
     * @param obj The {@link ScoreboardObjective} data model; must not be null.
     * @return A new instance of ScoreboardCommand.
     * @throws NullPointerException if the objective model is null.
     * @throws RuntimeException if component building fails.
     */
    public static ScoreboardCommand addObjective(ScoreboardObjective obj) {
        Objects.requireNonNull(obj, "Objective Error: Data model is required for registration.");
        try {
            final StringBuilder builder = new StringBuilder("objectives add ")
                    .append(obj.getId().getObjectiveName())
                    .append(" ")
                    .append(obj.getCriteria().getCriteriaName());

            if (obj.getDisplayName() != null) {
                builder.append(" ").append(obj.getDisplayName().build());
            }
            return new ScoreboardCommand(builder.toString());
        } catch (Exception e) {
            throw new RuntimeException("Objective Error: Failed to build 'add' command for: " + obj.getId(), e);
        }
    }

    /**
     * Constructs a command to delete an existing objective.
     * <p>Syntax: {@code /scoreboard objectives remove <name>}</p>
     * @param id The {@link ScoreboardObjectiveId} to remove; must not be null.
     * @return A new instance of ScoreboardCommand.
     * @throws NullPointerException if the ID is null.
     */
    public static ScoreboardCommand removeObjective(ScoreboardObjectiveId id) {
        Objects.requireNonNull(id, "Objective Error: ID is required for removal.");
        return new ScoreboardCommand("objectives remove " + id.getObjectiveName());
    }

    /**
     * Constructs a command to define where an objective appears in the interface.
     * <p>Syntax: {@code /scoreboard objectives setdisplay <slot> [<objective>]}</p>
     * @param slot The {@link DisplaySlot} location; must not be null.
     * @param id   The objective ID to show, or null to clear the specified slot.
     * @return A new instance of ScoreboardCommand.
     * @throws NullPointerException if the display slot is null.
     */
    public static ScoreboardCommand setDisplay(DisplaySlot slot, ScoreboardObjectiveId id) {
        Objects.requireNonNull(slot, "Display Error: Slot must be specified.");
        final String action = "objectives setdisplay " + slot.getSlotName() +
                (id != null ? " " + id.getObjectiveName() : "");
        return new ScoreboardCommand(action);
    }

    // --- ⚔️ Static Factory Methods: Players ---

    /**
     * Constructs a command to retrieve a specific score value.
     * <p>Syntax: {@code /scoreboard players get <target> <objective>}</p>
     * @param target    The entity to query; must not be null.
     * @param objective The objective to read; must not be null.
     * @return A new instance of ScoreboardCommand.
     * @throws NullPointerException if target or objective is null.
     */
    public static ScoreboardCommand getScore(Entity target, ScoreboardObjectiveId objective) {
        Objects.requireNonNull(target, "Player Error: Target entity is required for queries.");
        Objects.requireNonNull(objective, "Player Error: Objective ID is required for queries.");
        return new ScoreboardCommand("players get " + target.toString() + " " + objective.getObjectiveName());
    }

    /**
     * Constructs a command to assign a specific integer value to a score.
     * <p>Syntax: {@code /scoreboard players set <target> <objective> <score>}</p>
     * @param target The entity to modify; must not be null.
     * @param id     The objective to update; must not be null.
     * @param score  The literal integer value to set.
     * @return A new instance of ScoreboardCommand.
     */
    public static ScoreboardCommand setScore(Entity target, ScoreboardObjectiveId id, int score) {
        Objects.requireNonNull(target, "Player Error: Target entity is required.");
        Objects.requireNonNull(id, "Player Error: Objective ID is required.");
        return new ScoreboardCommand("players set " + target.toString() + " " + id.getObjectiveName() + " " + score);
    }

    /**
     * Constructs a command to increment a score value.
     * <p>Syntax: {@code /scoreboard players add <target> <objective> <score>}</p>
     * @param target The entity to modify; must not be null.
     * @param id     The objective to update; must not be null.
     * @param score  The amount to add.
     * @return A new instance of ScoreboardCommand.
     */
    public static ScoreboardCommand addScore(Entity target, ScoreboardObjectiveId id, int score) {
        Objects.requireNonNull(target, "Player Error: Target entity is required.");
        Objects.requireNonNull(id, "Player Error: Objective ID is required.");
        return new ScoreboardCommand("players add " + target.toString() + " " + id.getObjectiveName() + " " + score);
    }

    /**
     * Constructs a command to clear ALL objective scores for a target.
     * <p>Syntax: {@code /scoreboard players reset <target>}</p>
     * @param target The entity to reset; must not be null.
     * @return A new instance of ScoreboardCommand.
     */
    public static ScoreboardCommand reset(Entity target) {
        Objects.requireNonNull(target, "Reset Error: Target entity is required.");
        return new ScoreboardCommand("players reset " + target.toString());
    }

    /**
     * Constructs a command to clear a specific objective score for a target.
     * <p>Syntax: {@code /scoreboard players reset <target> <objective>}</p>
     * @param target    The entity to reset; must not be null.
     * @param objective The objective to clear; must not be null.
     * @return A new instance of ScoreboardCommand.
     */
    public static ScoreboardCommand reset(Entity target, ScoreboardObjectiveId objective) {
        Objects.requireNonNull(target, "Reset Error: Target entity is required.");
        Objects.requireNonNull(objective, "Reset Error: Objective ID is required.");
        return new ScoreboardCommand("players reset " + target.toString() + " " + objective.getObjectiveName());
    }

    /**
     * Constructs a command to perform a mathematical operation between scores.
     * <p>Syntax: {@code /scoreboard players operation <target> <targetObj> <op> <source> <sourceObj>}</p>
     * @param target    The entity to be modified; must not be null.
     * @param targetObj The objective on the target entity; must not be null.
     * @param op        The operator (e.g., =, +=, *=); must not be null.
     * @param source    The source entity to read from; must not be null.
     * @param sourceObj The objective on the source entity; must not be null.
     * @return A new instance of ScoreboardCommand.
     * @throws NullPointerException if any argument is null.
     */
    public static ScoreboardCommand operation(Entity target, ScoreboardObjectiveId targetObj, OperationType op, Entity source, ScoreboardObjectiveId sourceObj) {
        Objects.requireNonNull(target, "Operation Error: Target entity is null.");
        Objects.requireNonNull(targetObj, "Operation Error: Target objective is null.");
        Objects.requireNonNull(op, "Operation Error: Operator is null.");
        Objects.requireNonNull(source, "Operation Error: Source entity is null.");
        Objects.requireNonNull(sourceObj, "Operation Error: Source objective is null.");

        final String action = String.format("players operation %s %s %s %s %s",
                target.toString(), targetObj.getObjectiveName(), op.getOperator(),
                source.toString(), sourceObj.getObjectiveName());

        return new ScoreboardCommand(action);
    }

    // --- ⚙️ Contract Implementation ---

    /**
     * Generates the final Minecraft-ready command string.
     * @return The full command (e.g., "scoreboard players get @p kills").
     */
    @Override
    public String generate() {
        return "scoreboard " + this.command;
    }

    /**
     * Returns the command string for logging or execution.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }

    // --- 📦 Inner Utility ---

    /**
     * Static utility for bulk scoreboard operations.
     */
    public static final class Batch {

        /**
         * Compiles a list of initialization commands for all objectives in the registry.
         * <p>This includes both 'add' commands and 'setdisplay' commands for sidebar objectives.</p>
         * @return A non-null list of ScoreboardCommand instances.
         * @throws RuntimeException if registration fails during the loop.
         */
        public static List<ScoreboardCommand> registerAll() {
            final List<ScoreboardCommand> commands = new ArrayList<>();
            try {
                for (uhc.score.ScoreboardObjective obj : uhc.score.ScoreboardObjectives.ALL) {
                    if (obj == null) continue;

                    commands.add(ScoreboardCommand.addObjective(obj));

                    if (obj.isDisplaySidebar()) {
                        commands.add(ScoreboardCommand.setDisplay(DisplaySlot.SIDEBAR, obj.getId()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("CRITICAL: Failed to compile global objective registration batch.", e);
            }
            return commands;
        }
    }
}