package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.coordinate.Rotation;
import uhc.arguments.coordinate.Swizzle;
import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.EntityAnchor;
import uhc.command.MinecraftCommand;
import uhc.arguments.block.BlockPredicate;
import uhc.resource.data.DataPath;
import uhc.resource.data.StoragePath;
import uhc.resource.bossbar.BossbarId;
import uhc.resource.coordinate.HeightMap;
import uhc.resource.data.DataType;
import uhc.resource.dimension.DimensionId;
import uhc.resource.entity.EntityId;
import uhc.resource.entity.RelationId;
import uhc.resource.predicate.PredicateId;
import uhc.score.ComparatorType;
import uhc.score.Range;
import uhc.score.ScoreboardObjective;

import java.util.Objects;

/**
 * 🛠️ **Execute Command Builder**
 * <p>
 * Constructs the complex {@code /execute} command. This class follows a fluent builder pattern,
 * allowing you to chain modifiers like {@code as}, {@code at}, and {@code if} before
 * finishing with a {@code run} action.
 * </p>
 */
public class ExecuteCommand implements MinecraftCommand {
    private final StringBuilder command;

    private ExecuteCommand() {
        this.command = new StringBuilder("execute");
    }

    /**
     * Initializes a new ExecuteCommand builder.
     */
    public static ExecuteCommand create() {
        return new ExecuteCommand();
    }

    // --- Basic Modifiers ---

    /** Aligns execution to the specified axes. */
    public ExecuteCommand align(Swizzle axes) {
        return appendModifier("align", axes);
    }

    /** Sets the anchor point (eyes/feet) for further relative offsets. */
    public ExecuteCommand anchored(EntityAnchor anchor) {
        return appendModifier("anchored", anchor);
    }

    /** Changes the execution entity (the context of '@s'). */
    public ExecuteCommand as(Entity targets) {
        return appendModifier("as", targets);
    }

    /** Changes the execution position and rotation to match the target. */
    public ExecuteCommand at(Entity targets) {
        return appendModifier("at", targets);
    }

    /** Rotates the execution context to face a specific coordinate. */
    public ExecuteCommand facing(Vec3 pos) {
        return appendModifier("facing", pos);
    }

    /** Rotates the execution context to face a specific entity's anchor. */
    public ExecuteCommand facingEntity(Entity targets, EntityAnchor anchor) {
        Objects.requireNonNull(targets);
        Objects.requireNonNull(anchor);
        command.append(" facing entity ").append(targets).append(" ").append(anchor);
        return this;
    }

    /** Changes the dimension (world) of execution. */
    public ExecuteCommand in(DimensionId dimension) {
        return appendModifier("in", dimension);
    }

    /** Changes execution context to an entity related to the current one (e.g., owner). */
    public ExecuteCommand on(RelationId relation) {
        return appendModifier("on", relation);
    }

    /** Shifts the execution position to specific coordinates. */
    public ExecuteCommand positioned(Vec3 pos) {
        return appendModifier("positioned", pos);
    }

    /** Shifts the execution position to match the target's position. */
    public ExecuteCommand positionedAs(Entity targets) {
        return appendModifier("positioned as", targets);
    }

    /** Shifts the execution position to the top of a specific heightmap. */
    public ExecuteCommand positionedOver(HeightMap heightmap) {
        return appendModifier("positioned over", heightmap);
    }

    /** Sets the execution rotation. */
    public ExecuteCommand rotated(Rotation rot) {
        return appendModifier("rotated", rot);
    }

    /** Matches the rotation of the target entity. */
    public ExecuteCommand rotatedAs(Entity targets) {
        return appendModifier("rotated as", targets);
    }

    /** Temporary summoned entity context for the command. */
    public ExecuteCommand summon(EntityId entity) {
        return appendModifier("summon", entity);
    }

    // --- Store Modifiers ---

    /** Stores the result of the command into a block's NBT data. */
    public ExecuteCommand storeResultBlock(BlockPos targetPos, DataPath path, DataType type, double scale) {
        Objects.requireNonNull(targetPos);
        Objects.requireNonNull(path);
        Objects.requireNonNull(type);
        command.append(" store result block ").append(targetPos).append(" ").append(path)
                .append(" ").append(type).append(" ").append(scale);
        return this;
    }

    /** Stores the success (0 or 1) of the command into a bossbar. */
    public ExecuteCommand storeSuccessBossbar(BossbarId id, BossbarStoreProperty property) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(property);
        command.append(" store success bossbar ").append(id).append(" ").append(property);
        return this;
    }

    /** Stores the result of the command into an entity's NBT data. */
    public ExecuteCommand storeResultEntity(Entity target, DataPath path, DataType type, double scale) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(path);
        Objects.requireNonNull(type);
        command.append(" store result entity ").append(target).append(" ").append(path)
                .append(" ").append(type).append(" ").append(scale);
        return this;
    }

    /** Stores the result of the command into a scoreboard objective. */
    public ExecuteCommand storeResultScore(Entity targets, ScoreboardObjective objective) {
        Objects.requireNonNull(targets);
        Objects.requireNonNull(objective);
        command.append(" store result score ").append(targets).append(" ").append(objective);
        return this;
    }

    // --- Conditional (If/Unless) Modifiers ---

    /** Proceeds only if the target entity exists. */
    public ExecuteCommand ifEntity(Entity entities) {
        return appendModifier("if entity", entities);
    }

    /** Proceeds only if the target entity does not exist. */
    public ExecuteCommand unlessEntity(Entity entities) {
        return appendModifier("unless entity", entities);
    }

    /** Proceeds only if the block at the position matches the predicate. */
    public ExecuteCommand ifBlock(BlockPos pos, BlockPredicate block) {
        Objects.requireNonNull(pos);
        Objects.requireNonNull(block);
        command.append(" if block ").append(pos).append(" ").append(block);
        return this;
    }

    /** Proceeds only if the score matches a certain range. */
    public ExecuteCommand ifScoreMatches(Entity target, ScoreboardObjective objective, Range range) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(objective);
        Objects.requireNonNull(range);
        command.append(" if score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /** Compares two scores using a mathematical operator. */
    public ExecuteCommand ifScoreCompare(Entity target, ScoreboardObjective targetObj, ComparatorType operator, Entity source, ScoreboardObjective sourceObj) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(targetObj);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(source);
        Objects.requireNonNull(sourceObj);
        command.append(" if score ").append(target).append(" ").append(targetObj)
                .append(" ").append(operator).append(" ")
                .append(source).append(" ").append(sourceObj);
        return this;
    }

    /** Checks NBT data in storage. */
    public ExecuteCommand ifDataStorage(StoragePath source, DataPath path) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(path);
        command.append(" if data storage ").append(source).append(" ").append(path);
        return this;
    }

    /** Checks a custom datapack predicate. */
    public ExecuteCommand ifPredicate(PredicateId predicate) {
        return appendModifier("if predicate", predicate);
    }

    /** Proceeds only if the chunk at the position is loaded. */
    public ExecuteCommand ifLoaded(BlockPos pos) {
        return appendModifier("if loaded", pos);
    }

    // --- Helper Logic ---

    private ExecuteCommand appendModifier(String sub, Object argument) {
        Objects.requireNonNull(argument, "Argument for sub-command '" + sub + "' cannot be null.");
        command.append(" ").append(sub).append(" ").append(argument);
        return this;
    }

    // --- Terminal Action ---

    /**
     * Ends the chain and adds the command to be executed.
     * @param command The {@link MinecraftCommand} to run.
     * @return The final command string.
     */
    public String run(MinecraftCommand command) {
        Objects.requireNonNull(command, "The command to run cannot be null.");
        this.command.append(" run ").append(command.generate());
        return this.generate();
    }

    @Override
    public String generate() {
        String result = command.toString().trim();
        if (result.endsWith("execute")) {
            throw new IllegalStateException("ExecuteCommand must have modifiers or a run action.");
        }
        return result;
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines which property of a bossbar is being targeted in a store command.
     */
    public enum BossbarStoreProperty {
        VALUE,
        MAX;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}