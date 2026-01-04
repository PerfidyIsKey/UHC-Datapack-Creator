package uhc.command.commands;

import uhc.arguments.coordinate.Rotation;
import uhc.arguments.coordinate.Swizzle;
import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.EntityAnchor;
import uhc.command.MinecraftCommand;
import uhc.game.bossbar.BossbarData;
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
 * Provides a fluent API for constructing complex Minecraft {@code /execute} commands.
 * This class manages the command buffer and ensures that all sub-clauses are
 * syntactically correct and type-safe.
 * </p>
 *
 */
public class ExecuteCommand implements MinecraftCommand {

    // --- 📂 Constants & Fields ---

    /** * The starting keyword for all chains in this class. */
    private static final String BASE_KEYWORD = "execute";

    /** * The internal buffer accumulating the command components. */
    private final StringBuilder command;

    // --- 🏗️ Constructor & Factory ---

    /**
     * Private constructor to initialize the builder with the base keyword.
     */
    private ExecuteCommand() {
        this.command = new StringBuilder(BASE_KEYWORD);
    }

    /**
     * Initiates a new Minecraft execute command chain.
     * @return A new instance of {@code ExecuteCommand}.
     */
    public static ExecuteCommand create() {
        return new ExecuteCommand();
    }

    // --- 🔗 Contextual Modifiers ---

    /**
     * Aligns the execution position to the block grid on specified axes.
     * @param axes The {@link Swizzle} axes (e.g., "xyz").
     * @return This builder for chaining.
     * @throws NullPointerException if axes is null.
     */
    public ExecuteCommand align(Swizzle axes) {
        return appendModifier("align", axes);
    }

    /**
     * Sets the relative coordinate anchor to the entity's eyes or feet.
     * @param anchor The {@link EntityAnchor} position.
     * @return This builder for chaining.
     */
    public ExecuteCommand anchored(EntityAnchor anchor) {
        return appendModifier("anchored", anchor);
    }

    /**
     * Changes the executing entity (affects {@code @s}).
     * @param targets The {@link Entity} selector.
     * @return This builder for chaining.
     */
    public ExecuteCommand as(Entity targets) {
        return appendModifier("as", targets);
    }

    /**
     * Matches position, rotation, and dimension to the target entity.
     * @param targets The {@link Entity} reference.
     * @return This builder for chaining.
     */
    public ExecuteCommand at(Entity targets) {
        return appendModifier("at", targets);
    }

    /**
     * Rotates the context to face a specific 3D coordinate.
     * @param pos The {@link Vec3} target position.
     * @return This builder for chaining.
     */
    public ExecuteCommand facing(Vec3 pos) {
        return appendModifier("facing", pos);
    }

    /**
     * Shifts execution to a specific dimension.
     * @param dimension The {@link DimensionId} (e.g., THE_NETHER).
     * @return This builder for chaining.
     */
    public ExecuteCommand in(DimensionId dimension) {
        return appendModifier("in", dimension);
    }

    /**
     * Executes based on an entity related to the current executor (Minecraft 1.19.4+).
     * @param relation The {@link RelationId} (e.g., origin, owner).
     * @return This builder for chaining.
     */
    public ExecuteCommand on(RelationId relation) {
        return appendModifier("on", relation);
    }

    /**
     * Sets a specific global or relative execution position.
     * @param pos The {@link Vec3} coordinates.
     * @return This builder for chaining.
     */
    public ExecuteCommand positioned(Vec3 pos) {
        return appendModifier("positioned", pos);
    }

    /**
     * Sets the execution position to match a specific entity's location.
     * @param targets The {@link Entity} to match.
     * @return This builder for chaining.
     */
    public ExecuteCommand positionedAs(Entity targets) {
        return appendModifier("positioned as", targets);
    }

    /**
     * Sets the execution position to the top of the world using a heightmap.
     * @param heightmap The {@link HeightMap} logic (e.g., MOTION_BLOCKING).
     * @return This builder for chaining.
     */
    public ExecuteCommand positionedOver(HeightMap heightmap) {
        return appendModifier("positioned over", heightmap);
    }

    /**
     * Sets the exact rotation of the execution context.
     * @param rot The {@link Rotation} (pitch and yaw).
     * @return This builder for chaining.
     */
    public ExecuteCommand rotated(Rotation rot) {
        return appendModifier("rotated", rot);
    }

    /**
     * Matches the execution rotation to a specific entity.
     * @param targets The {@link Entity} to copy rotation from.
     * @return This builder for chaining.
     */
    public ExecuteCommand rotatedAs(Entity targets) {
        return appendModifier("rotated as", targets);
    }

    /**
     * Summons an entity temporarily to act as the execution context.
     * @param entity The {@link EntityId} type to summon.
     * @return This builder for chaining.
     */
    public ExecuteCommand summon(EntityId entity) {
        return appendModifier("summon", entity);
    }

    // --- 🚩 Conditional Modifiers ---

    /**
     * Continues only if the specified entity exists.
     * @param entities The {@link Entity} selector to check.
     * @return This builder for chaining.
     */
    public ExecuteCommand ifEntity(Entity entities) {
        return appendModifier("if entity", entities);
    }

    /**
     * Continues only if the specified entity does NOT exist.
     * @param entities The {@link Entity} selector to check.
     * @return This builder for chaining.
     */
    public ExecuteCommand unlessEntity(Entity entities) {
        return appendModifier("unless entity", entities);
    }

    /**
     * Logic branch based on a scoreboard value range.
     * @param target The entity owning the score.
     * @param objective The objective to check.
     * @param range The valid numerical range.
     * @return This builder for chaining.
     */
    public ExecuteCommand ifScore(Entity target, ScoreboardObjectiveId objective, Range range) {
        validateScoreParams(target, objective, range);
        command.append(" if score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /**
     * Logic branch comparing two scoreboard values.
     * @param target The target entity.
     * @param targetObj The target's objective.
     * @param operator The comparison operator (e.g., {@code >=}).
     * @param source The source entity for comparison.
     * @param sourceObj The source's objective.
     * @return This builder for chaining.
     */
    public ExecuteCommand ifScore(Entity target, ScoreboardObjectiveId targetObj, ComparatorType operator, Entity source, ScoreboardObjectiveId sourceObj) {
        validateScoreComparison(target, targetObj, operator, source, sourceObj, "if");
        command.append(" if score ").append(target).append(" ").append(targetObj)
                .append(" ").append(operator).append(" ")
                .append(source).append(" ").append(sourceObj);
        return this;
    }

    /**
     * Inverse logic branch based on a scoreboard range.
     * @param target The entity owning the score.
     * @param objective The objective to check.
     * @param range The range that causes failure if matched.
     * @return This builder for chaining.
     */
    public ExecuteCommand unlessScore(Entity target, ScoreboardObjectiveId objective, Range range) {
        validateScoreParams(target, objective, range);
        command.append(" unless score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /**
     * Inverse logic branch comparing two scoreboard values.
     * @param target The target entity.
     * @param targetObj The target's objective.
     * @param operator The comparison operator.
     * @param source The source entity.
     * @param sourceObj The source's objective.
     * @return This builder for chaining.
     */
    public ExecuteCommand unlessScore(Entity target, ScoreboardObjectiveId targetObj, ComparatorType operator, Entity source, ScoreboardObjectiveId sourceObj) {
        validateScoreComparison(target, targetObj, operator, source, sourceObj, "unless");
        command.append(" unless score ").append(target).append(" ").append(targetObj)
                .append(" ").append(operator).append(" ")
                .append(source).append(" ").append(sourceObj);
        return this;
    }

    // --- 📊 Storage Modifiers ---

    /**
     * Stores the numerical result of the execution into a scoreboard.
     * @param targets The entity to receive the score.
     * @param objective The objective to update.
     * @return This builder for chaining.
     */
    public ExecuteCommand storeResultScore(Entity targets, ScoreboardObjectiveId objective) {
        Objects.requireNonNull(targets, "Execute 'store result score' failed: Target entity is null.");
        Objects.requireNonNull(objective, "Execute 'store result score' failed: Objective is null.");
        command.append(" store result score ").append(targets).append(" ").append(objective);
        return this;
    }

    /**
     * Redirects the command output to a bossbar.
     * @param storeType RESULT (value) or SUCCESS (0/1).
     * @param bossbar The target {@link BossbarData}.
     * @param valueType VALUE or MAX.
     * @return This builder for chaining.
     */
    public ExecuteCommand storeBossbar(StoreType storeType, BossbarData bossbar, BossbarValueType valueType) {
        Objects.requireNonNull(storeType, "Execute 'store bossbar' failed: StoreType is null.");
        Objects.requireNonNull(bossbar, "Execute 'store bossbar' failed: BossbarData is null.");
        Objects.requireNonNull(valueType, "Execute 'store bossbar' failed: BossbarValueType is null.");

        command.append(" store ")
                .append(storeType.name().toLowerCase())
                .append(" bossbar ")
                .append(bossbar.getId())
                .append(" ")
                .append(valueType.name().toLowerCase());

        return this;
    }

    // --- ⚙️ Internal Helpers & Validation ---

    /**
     * Appends generic modifiers to the string buffer.
     */
    private ExecuteCommand appendModifier(String sub, Object argument) {
        Objects.requireNonNull(argument, "Execute construction failed: Modifier '" + sub + "' requires a non-null argument.");
        command.append(" ").append(sub).append(" ").append(argument);
        return this;
    }

    /**
     * Validates shared parameters for score-range checks.
     */
    private void validateScoreParams(Entity target, ScoreboardObjectiveId objective, Range range) {
        Objects.requireNonNull(target, "Execute score check failed: Target entity is null.");
        Objects.requireNonNull(objective, "Execute score check failed: Objective is null.");
        Objects.requireNonNull(range, "Execute score check failed: Range is null.");
    }

    /**
     * Validates shared parameters for score comparisons.
     */
    private void validateScoreComparison(Entity target, ScoreboardObjectiveId tObj, ComparatorType op, Entity source, ScoreboardObjectiveId sObj, String type) {
        Objects.requireNonNull(target, "Execute '" + type + " score' failed: Target entity is null.");
        Objects.requireNonNull(tObj, "Execute '" + type + " score' failed: Target objective is null.");
        Objects.requireNonNull(op, "Execute '" + type + " score' failed: Operator is null.");
        Objects.requireNonNull(source, "Execute '" + type + " score' failed: Source entity is null.");
        Objects.requireNonNull(sObj, "Execute '" + type + " score' failed: Source objective is null.");
    }

    // --- 🏁 Terminal Actions ---

    /**
     * Defines the return type of the command to be stored.
     */
    public enum StoreType { RESULT, SUCCESS }

    /**
     * Defines which attribute of the target bossbar to overwrite.
     */
    public enum BossbarValueType { VALUE, MAX }

    /**
     * Appends the final payload command to the chain.
     * @param runCommand The {@link MinecraftCommand} to execute.
     * @return This builder instance.
     * @throws RuntimeException if the runCommand is null or fails generation.
     */
    public ExecuteCommand run(MinecraftCommand runCommand) {
        Objects.requireNonNull(runCommand, "Execute 'run' failed: Payload command is null.");
        try {
            String subContent = runCommand.generate();
            if (subContent == null || subContent.isBlank()) {
                throw new IllegalArgumentException("The payload command produced an empty string.");
            }
            this.command.append(" run ").append(subContent);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to finalize ExecuteCommand: " + e.getMessage(), e);
        }
    }

    /**
     * Generates the raw string for the .mcfunction file.
     * @return The complete /execute string.
     * @throws IllegalStateException if no modifiers or run actions were added.
     */
    @Override
    public String generate() {
        String result = command.toString().trim();
        if (result.equals(BASE_KEYWORD)) {
            throw new IllegalStateException("Generation Error: 'execute' command is incomplete. Add modifiers or a 'run' action.");
        }
        return result;
    }

    @Override
    public String toString() {
        return generate();
    }
}