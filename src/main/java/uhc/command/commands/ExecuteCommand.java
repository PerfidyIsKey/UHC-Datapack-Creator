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
 * This class provides a fluent API for constructing Minecraft {@code /execute} commands.
 * It follows the official Minecraft syntax structure, allowing for complex branching
 * and conditional logic.
 * </p>
 * <p>
 * <b>Logic Flow:</b>
 * <ol>
 * <li><b>Initiate:</b> Use {@link #create()} to start the buffer.</li>
 * <li><b>Modify:</b> Chain modifiers (e.g., {@code as}, {@code at}, {@code ifScore}).</li>
 * <li><b>Finalize:</b> Use {@link #run(MinecraftCommand)} to append the payload.</li>
 * </ol>
 * </p>
 */
public class ExecuteCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The internal buffer that accumulates the command string components.
     * Initialized with the base "execute" keyword.
     */
    private final StringBuilder command;

    // --- 🏗️ Constructor & Factory ---

    /**
     * Private constructor to enforce the use of the static factory method.
     */
    private ExecuteCommand() {
        this.command = new StringBuilder("execute");
    }

    /**
     * Initiates a new Minecraft execute command chain.
     * * @return A new instance of {@code ExecuteCommand} starting with the "execute" keyword.
     */
    public static ExecuteCommand create() {
        return new ExecuteCommand();
    }

    // --- 🔗 Contextual Modifiers ---

    /**
     * Aligns the execution position to the specified axes by rounding down coordinates.
     * * @param axes The {@link Swizzle} axes to align (e.g., X, Y, Z).
     * @return This builder for chaining.
     * @throws NullPointerException if {@code axes} is null.
     */
    public ExecuteCommand align(Swizzle axes) {
        return appendModifier("align", axes);
    }

    /**
     * Sets the anchor point for relative coordinates (eyes or feet) for the executor.
     * * @param anchor The {@link EntityAnchor} type.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code anchor} is null.
     */
    public ExecuteCommand anchored(EntityAnchor anchor) {
        return appendModifier("anchored", anchor);
    }

    /**
     * Sets the executing entity (the context for '@s').
     * * @param targets The {@link Entity} reference.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code targets} is null.
     */
    public ExecuteCommand as(Entity targets) {
        return appendModifier("as", targets);
    }

    /**
     * Updates the execution position, rotation, and dimension to match the target entity.
     * * @param targets The target {@link Entity}.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code targets} is null.
     */
    public ExecuteCommand at(Entity targets) {
        return appendModifier("at", targets);
    }

    /**
     * Rotates the execution context to face a specific global coordinate.
     * * @param pos The {@link Vec3} destination to face.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code pos} is null.
     */
    public ExecuteCommand facing(Vec3 pos) {
        return appendModifier("facing", pos);
    }

    /**
     * Transfers the execution context to a specific dimension.
     * * @param dimension The {@link DimensionId} target (e.g., OVERWORLD).
     * @return This builder for chaining.
     * @throws NullPointerException if {@code dimension} is null.
     */
    public ExecuteCommand in(DimensionId dimension) {
        return appendModifier("in", dimension);
    }

    /**
     * Executes the command based on an entity related to the current executor.
     * * @param relation The {@link RelationId} (e.g., vehicle, controller).
     * @return This builder for chaining.
     * @throws NullPointerException if {@code relation} is null.
     */
    public ExecuteCommand on(RelationId relation) {
        return appendModifier("on", relation);
    }

    /**
     * Shifts the execution position to the specified global or relative coordinates.
     * * @param pos The {@link Vec3} location.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code pos} is null.
     */
    public ExecuteCommand positioned(Vec3 pos) {
        return appendModifier("positioned", pos);
    }

    /**
     * Shifts the execution position to match the location of a target entity.
     * * @param targets The target {@link Entity}.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code targets} is null.
     */
    public ExecuteCommand positionedAs(Entity targets) {
        return appendModifier("positioned as", targets);
    }

    /**
     * Shifts the execution position to the top of a specific heightmap.
     * * @param heightmap The {@link HeightMap} algorithm to use.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code heightmap} is null.
     */
    public ExecuteCommand positionedOver(HeightMap heightmap) {
        return appendModifier("positioned over", heightmap);
    }

    /**
     * Sets the exact execution rotation using pitch and yaw.
     * * @param rot The {@link Rotation} value.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code rot} is null.
     */
    public ExecuteCommand rotated(Rotation rot) {
        return appendModifier("rotated", rot);
    }

    /**
     * Matches the execution rotation to the rotation of the target entity.
     * * @param targets The target {@link Entity}.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code targets} is null.
     */
    public ExecuteCommand rotatedAs(Entity targets) {
        return appendModifier("rotated as", targets);
    }

    /**
     * Temporarily summons an entity for the duration of the command execution.
     * * @param entity The {@link EntityId} to summon.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code entity} is null.
     */
    public ExecuteCommand summon(EntityId entity) {
        return appendModifier("summon", entity);
    }

    // --- 🚩 Conditional Modifiers ---

    /**
     * Continues execution only if the target entity exists.
     * * @param entities The {@link Entity} to check.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code entities} is null.
     */
    public ExecuteCommand ifEntity(Entity entities) {
        return appendModifier("if entity", entities);
    }

    /**
     * Continues execution only if the target entity does not exist.
     * * @param entities The {@link Entity} to check.
     * @return This builder for chaining.
     * @throws NullPointerException if {@code entities} is null.
     */
    public ExecuteCommand unlessEntity(Entity entities) {
        return appendModifier("unless entity", entities);
    }

    /**
     * Continues execution if a scoreboard value falls within a specific range.
     * * @param target    The entity owning the score.
     * @param objective The {@link ScoreboardObjectiveId} to check.
     * @param range     The {@link Range} of acceptable values.
     * @return This builder for chaining.
     * @throws NullPointerException if any parameter is null.
     */
    public ExecuteCommand ifScore(Entity target, ScoreboardObjectiveId objective, Range range) {
        Objects.requireNonNull(target, "Execute 'if score' failed: Target entity cannot be null.");
        Objects.requireNonNull(objective, "Execute 'if score' failed: Objective cannot be null.");
        Objects.requireNonNull(range, "Execute 'if score' failed: Range cannot be null.");
        command.append(" if score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /**
     * Continues execution if a specific scoreboard value is OUTSIDE the specified range.
     * * @param target    The entity owning the score.
     * @param objective The {@link ScoreboardObjectiveId} to check.
     * @param range     The {@link Range} to avoid.
     * @return This builder for chaining.
     * @throws NullPointerException if any parameter is null.
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
     * * @param target    The target entity for comparison.
     * @param targetObj The objective belonging to the target.
     * @param operator  The {@link ComparatorType} (e.g., =, >, <).
     * @param source    The source entity for comparison.
     * @param sourceObj The objective belonging to the source.
     * @return This builder for chaining.
     * @throws NullPointerException if any comparison component is null.
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
     * Stores the final result of the executed command into a scoreboard objective.
     * * @param targets   The target entity to receive the numerical value.
     * @param objective The {@link ScoreboardObjectiveId} to update.
     * @return This builder for chaining.
     * @throws NullPointerException if targets or objective is null.
     */
    public ExecuteCommand storeResultScore(Entity targets, ScoreboardObjectiveId objective) {
        Objects.requireNonNull(targets, "Execute 'store result score' failed: Target entity is null.");
        Objects.requireNonNull(objective, "Execute 'store result score' failed: Objective is null.");
        command.append(" store result score ").append(targets).append(" ").append(objective);
        return this;
    }

    /**
     * Stores the command outcome into a Minecraft bossbar.
     * * @param storeType Whether to store the numeric 'result' or boolean 'success'.
     * @param bossbar   The {@link BossbarData} providing the target ID.
     * @param valueType Whether to update the 'value' or the 'max' limit.
     * @return This builder for chaining.
     * @throws NullPointerException if any parameter is null.
     */
    public ExecuteCommand storeBossbar(StoreType storeType, BossbarData bossbar, BossbarValueType valueType) {
        Objects.requireNonNull(storeType, "Execute 'store bossbar' failed: StoreType cannot be null.");
        Objects.requireNonNull(bossbar, "Execute 'store bossbar' failed: BossbarData cannot be null.");
        Objects.requireNonNull(valueType, "Execute 'store bossbar' failed: BossbarValueType cannot be null.");

        command.append(" store ")
                .append(storeType.name().toLowerCase())
                .append(" bossbar ")
                .append(bossbar.getId())
                .append(" ")
                .append(valueType.name().toLowerCase());

        return this;
    }

    /**
     * Defines the return type of the command execution to be stored.
     */
    public enum StoreType {
        /** The actual numeric output of the command. */
        RESULT,
        /** 1 if the command executed successfully, 0 otherwise. */
        SUCCESS
    }

    /**
     * Defines which attribute of the target bossbar to overwrite.
     */
    public enum BossbarValueType {
        /** The current filled amount of the bar. */
        VALUE,
        /** The total capacity/limit of the bar. */
        MAX
    }

    // --- ⚙️ Internal Helpers ---

    /**
     * Appends a sub-command modifier to the builder's internal buffer.
     * * @param sub      The sub-command keyword (e.g., "as", "at").
     * @param argument The argument object which will have its toString() called.
     * @return This builder instance.
     * @throws NullPointerException if the argument is null.
     */
    private ExecuteCommand appendModifier(String sub, Object argument) {
        Objects.requireNonNull(argument, "Command construction failed: Modifier '" + sub + "' requires a non-null argument.");
        command.append(" ").append(sub).append(" ").append(argument);
        return this;
    }

    // --- 🏁 Terminal Action ---

    /**
     * Finalizes the execute chain by appending the command to be performed.
     * * @param runCommand The {@link MinecraftCommand} payload.
     * @return This builder instance for final generation.
     * @throws RuntimeException if the runCommand is null or serialization fails.
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
     * * @return A valid /execute string ready for a .mcfunction file.
     * @throws IllegalStateException if the command consists only of the base keyword.
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