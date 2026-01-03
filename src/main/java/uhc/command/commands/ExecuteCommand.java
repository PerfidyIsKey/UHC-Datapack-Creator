package uhc.command.commands;

import uhc.arguments.coordinate.Rotation;
import uhc.arguments.coordinate.Swizzle;
import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.EntityAnchor;
import uhc.command.MinecraftCommand;
import uhc.resource.coordinate.HeightMap;
import uhc.resource.world.DimensionId;
import uhc.resource.entity.EntityId;
import uhc.resource.entity.RelationId;
import uhc.score.ComparatorType;
import uhc.score.Range;
import uhc.score.ScoreboardObjectiveId;

import java.util.Objects;

/**
 * 🛠️ **Execute Command Builder**
 * <p>
 * This class provides a fluent API for constructing Minecraft {@code /execute} commands.
 * Commands are initiated via the static {@link #create()} method and finalized using
 * the {@link #run(MinecraftCommand)} method.
 * </p>
 * <p>
 * Constraints:
 * <ul>
 * <li>All modifiers are instance-based to avoid static-access warnings.</li>
 * <li>Strict null-checking is enforced; no default or fallback values are provided.</li>
 * <li>Validation occurs both during the build process and at the final generation.</li>
 * </ul>
 * </p>
 */
public class ExecuteCommand implements MinecraftCommand {

    /** * The internal buffer that accumulates the command string.
     * Initialized with the base "execute" keyword.
     */
    private final StringBuilder command;

    /**
     * Private constructor to enforce the use of the static factory method {@link #create()}.
     */
    private ExecuteCommand() {
        this.command = new StringBuilder("execute");
    }

    // --- 🚀 Static Factory ---

    /**
     * Initiates a new Minecraft execute command chain.
     * * @return A new instance of {@code ExecuteCommand}.
     */
    public static ExecuteCommand create() {
        return new ExecuteCommand();
    }

    // --- 🔗 Contextual Modifiers ---

    /**
     * Aligns the execution position to the specified axes by rounding down coordinates.
     * @param axes The {@link Swizzle} axes (e.g., xyz).
     * @return This builder for chaining.
     * @throws NullPointerException if axes is null.
     */
    public ExecuteCommand align(Swizzle axes) {
        return appendModifier("align", axes);
    }

    /**
     * Sets the anchor point for relative coordinates to eyes or feet.
     * @param anchor The {@link EntityAnchor} type.
     * @return This builder for chaining.
     */
    public ExecuteCommand anchored(EntityAnchor anchor) {
        return appendModifier("anchored", anchor);
    }

    /**
     * Changes the executing entity (sets the context for '@s').
     * @param targets The {@link Entity} selector or reference.
     * @return This builder for chaining.
     */
    public ExecuteCommand as(Entity targets) {
        return appendModifier("as", targets);
    }

    /**
     * Updates the execution position and rotation to match the target entity.
     * @param targets The target {@link Entity}.
     * @return This builder for chaining.
     */
    public ExecuteCommand at(Entity targets) {
        return appendModifier("at", targets);
    }

    /**
     * Rotates the execution context to face a specific coordinate.
     * @param pos The {@link Vec3} destination.
     * @return This builder for chaining.
     */
    public ExecuteCommand facing(Vec3 pos) {
        return appendModifier("facing", pos);
    }

    /**
     * Transfers execution to a specific dimension.
     * @param dimension The {@link DimensionId} target.
     * @return This builder for chaining.
     */
    public ExecuteCommand in(DimensionId dimension) {
        return appendModifier("in", dimension);
    }

    /**
     * Executes the command based on an entity related to the current executor.
     * @param relation The {@link RelationId} (e.g., vehicle, attacker).
     * @return This builder for chaining.
     */
    public ExecuteCommand on(RelationId relation) {
        return appendModifier("on", relation);
    }

    /**
     * Shifts the execution position to specific coordinates.
     * @param pos The {@link Vec3} location.
     * @return This builder for chaining.
     */
    public ExecuteCommand positioned(Vec3 pos) {
        return appendModifier("positioned", pos);
    }

    /**
     * Shifts the execution position to match the location of a target entity.
     * @param targets The target {@link Entity}.
     * @return This builder for chaining.
     */
    public ExecuteCommand positionedAs(Entity targets) {
        return appendModifier("positioned as", targets);
    }

    /**
     * Shifts the execution position to the top of a specific heightmap.
     * @param heightmap The {@link HeightMap} type to use.
     * @return This builder for chaining.
     */
    public ExecuteCommand positionedOver(HeightMap heightmap) {
        return appendModifier("positioned over", heightmap);
    }

    /**
     * Sets the exact execution rotation.
     * @param rot The {@link Rotation} value.
     * @return This builder for chaining.
     */
    public ExecuteCommand rotated(Rotation rot) {
        return appendModifier("rotated", rot);
    }

    /**
     * Matches the execution rotation to the rotation of the target entity.
     * @param targets The target {@link Entity}.
     * @return This builder for chaining.
     */
    public ExecuteCommand rotatedAs(Entity targets) {
        return appendModifier("rotated as", targets);
    }

    /**
     * Contextually summons an entity for the command execution duration.
     * @param entity The {@link EntityId} to summon.
     * @return This builder for chaining.
     */
    public ExecuteCommand summon(EntityId entity) {
        return appendModifier("summon", entity);
    }

    // --- 🚩 Conditional Modifiers ---

    /**
     * Continues execution if the target entity exists.
     * @param entities The {@link Entity} to check.
     * @return This builder for chaining.
     */
    public ExecuteCommand ifEntity(Entity entities) {
        return appendModifier("if entity", entities);
    }

    /**
     * Continues execution if the target entity does not exist.
     * @param entities The {@link Entity} to check.
     * @return This builder for chaining.
     */
    public ExecuteCommand unlessEntity(Entity entities) {
        return appendModifier("unless entity", entities);
    }

    /**
     * Continues execution if a specific scoreboard value falls within a range.
     * @param target The entity owning the score.
     * @param objective The {@link ScoreboardObjectiveId} to check.
     * @param range The {@link Range} of acceptable values.
     * @return This builder for chaining.
     */
    public ExecuteCommand ifScore(Entity target, ScoreboardObjectiveId objective, Range range) {
        Objects.requireNonNull(target, "Execute 'if score' failed: Target entity cannot be null.");
        Objects.requireNonNull(objective, "Execute 'if score' failed: Objective cannot be null.");
        Objects.requireNonNull(range, "Execute 'if score' failed: Range cannot be null.");
        command.append(" if score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /**
     * Continues execution if a specific scoreboard value is OUTSIDE the range.
     * @param target The entity owning the score.
     * @param objective The {@link ScoreboardObjectiveId} to check.
     * @param range The {@link Range} to avoid.
     * @return This builder for chaining.
     */
    public ExecuteCommand unlessScore(Entity target, ScoreboardObjectiveId objective, Range range) {
        Objects.requireNonNull(target, "Execute 'unless score' failed: Target entity cannot be null.");
        Objects.requireNonNull(objective, "Execute 'unless score' failed: Objective cannot be null.");
        Objects.requireNonNull(range, "Execute 'unless score' failed: Range cannot be null.");
        command.append(" unless score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /**
     * Compares two scoreboard values; continues if the comparison is false.
     * @param target The target entity.
     * @param targetObj The target's objective.
     * @param operator The {@link ComparatorType} (e.g., =, >, <).
     * @param source The source entity.
     * @param sourceObj The source's objective.
     * @return This builder for chaining.
     */
    public ExecuteCommand unlessScore(Entity target, ScoreboardObjectiveId targetObj, ComparatorType operator, Entity source, ScoreboardObjectiveId sourceObj) {
        Objects.requireNonNull(target, "Execute 'unless score' comparison failed: Target entity is null.");
        Objects.requireNonNull(targetObj, "Execute 'unless score' comparison failed: Target objective is null.");
        Objects.requireNonNull(operator, "Execute 'unless score' comparison failed: Operator is null.");
        Objects.requireNonNull(source, "Execute 'unless score' comparison failed: Source entity is null.");
        Objects.requireNonNull(sourceObj, "Execute 'unless score' comparison failed: Source objective is null.");
        command.append(" unless score ").append(target).append(" ").append(targetObj)
                .append(" ").append(operator).append(" ")
                .append(source).append(" ").append(sourceObj);
        return this;
    }

    // --- 📊 Storage Modifiers ---

    /**
     * Stores the result of the executed command into a scoreboard objective.
     * @param targets The target entity to receive the value.
     * @param objective The {@link ScoreboardObjectiveId} to update.
     * @return This builder for chaining.
     */
    public ExecuteCommand storeResultScore(Entity targets, ScoreboardObjectiveId objective) {
        Objects.requireNonNull(targets, "Execute 'store result score' failed: Target entity is null.");
        Objects.requireNonNull(objective, "Execute 'store result score' failed: Objective is null.");
        command.append(" store result score ").append(targets).append(" ").append(objective);
        return this;
    }

    // --- ⚙️ Internal Helpers ---

    /**
     * Appends a sub-command modifier to the command string.
     * @param sub The sub-command name.
     * @param argument The argument to append.
     * @return This builder.
     * @throws NullPointerException if the argument is null.
     */
    private ExecuteCommand appendModifier(String sub, Object argument) {
        Objects.requireNonNull(argument, "Command construction failed: Modifier '" + sub + "' requires a non-null argument.");
        command.append(" ").append(sub).append(" ").append(argument);
        return this;
    }

    // --- 🏁 Terminal Action ---

    /**
     * Finalizes the execute chain by defining the command to be run.
     * @param runCommand The {@link MinecraftCommand} to execute under the built context.
     * @return The finalized ExecuteCommand instance.
     * @throws RuntimeException if an error occurs during string generation.
     */
    public ExecuteCommand run(MinecraftCommand runCommand) {
        try {
            Objects.requireNonNull(runCommand, "Execute 'run' failed: The command to execute cannot be null.");
            this.command.append(" run ").append(runCommand.generate());
            return this;
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to finalize ExecuteCommand: " + e.getMessage(), e);
        }
    }

    /**
     * Generates the final Minecraft command string.
     * @return A valid /execute command string.
     * @throws IllegalStateException if the command has no modifiers or run action.
     */
    @Override
    public String generate() {
        String result = command.toString().trim();
        if (result.equals("execute")) {
            throw new IllegalStateException("Generation Error: An 'execute' command must have at least one modifier or a 'run' action.");
        }
        return result;
    }

    @Override
    public String toString() {
        return generate();
    }
}