package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.arguments.block.BlockPredicate;
import uhc.data.nbt.DataPath;
import uhc.data.storage.StoragePath;
import uhc.score.ScoreboardObjective;

/**
 * 🛠️ **Execute Command Builder**
 * <p>
 * Provides a fluent API to construct complex {@code /execute} commands.
 * This class supports chaining multiple modifiers before executing a final command.
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

    public ExecuteCommand align(String axes) {
        command.append(" align ").append(axes);
        return this;
    }

    public ExecuteCommand anchored(String anchor) {
        command.append(" anchored ").append(anchor);
        return this;
    }

    public ExecuteCommand as(Entity targets) {
        command.append(" as ").append(targets);
        return this;
    }

    public ExecuteCommand at(Entity targets) {
        command.append(" at ").append(targets);
        return this;
    }

    public ExecuteCommand facing(Vec3 pos) {
        command.append(" facing ").append(pos);
        return this;
    }

    public ExecuteCommand facingEntity(Entity targets, String anchor) {
        command.append(" facing entity ").append(targets).append(" ").append(anchor);
        return this;
    }

    public ExecuteCommand in(String dimension) {
        command.append(" in ").append(dimension);
        return this;
    }

    public ExecuteCommand on(String relation) {
        command.append(" on ").append(relation);
        return this;
    }

    public ExecuteCommand positioned(Vec3 pos) {
        command.append(" positioned ").append(pos);
        return this;
    }

    public ExecuteCommand positionedAs(Entity targets) {
        command.append(" positioned as ").append(targets);
        return this;
    }

    public ExecuteCommand positionedOver(String heightmap) {
        command.append(" positioned over ").append(heightmap);
        return this;
    }

    public ExecuteCommand rotated(String rot) {
        command.append(" rotated ").append(rot);
        return this;
    }

    public ExecuteCommand rotatedAs(Entity targets) {
        command.append(" rotated as ").append(targets);
        return this;
    }

    public ExecuteCommand summon(String entity) {
        command.append(" summon ").append(entity);
        return this;
    }

    // --- Store Modifiers ---

    public ExecuteCommand storeResultBlock(BlockPos targetPos, DataPath path, String type, double scale) {
        command.append(" store result block ").append(targetPos).append(" ").append(path).append(" ").append(type).append(" ").append(scale);
        return this;
    }

    public ExecuteCommand storeSuccessBossbar(String id, String property) {
        command.append(" store success bossbar ").append(id).append(" ").append(property);
        return this;
    }

    public ExecuteCommand storeResultEntity(Entity target, DataPath path, String type, double scale) {
        command.append(" store result entity ").append(target).append(" ").append(path).append(" ").append(type).append(" ").append(scale);
        return this;
    }

    public ExecuteCommand storeResultScore(Entity targets, ScoreboardObjective objective) {
        command.append(" store result score ").append(targets).append(" ").append(objective);
        return this;
    }

    // --- Conditional (If/Unless) Modifiers ---

    public ExecuteCommand ifEntity(Entity entities) {
        command.append(" if entity ").append(entities);
        return this;
    }

    public ExecuteCommand unlessEntity(Entity entities) {
        command.append(" unless entity ").append(entities);
        return this;
    }

    public ExecuteCommand ifBlock(BlockPos pos, BlockPredicate block) {
        command.append(" if block ").append(pos).append(" ").append(block);
        return this;
    }

    public ExecuteCommand ifScoreMatches(Entity target, ScoreboardObjective objective, String range) {
        command.append(" if score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    public ExecuteCommand ifScoreCompare(Entity target, ScoreboardObjective targetObj, String operator, Entity source, ScoreboardObjective sourceObj) {
        command.append(" if score ").append(target).append(" ").append(targetObj)
                .append(" ").append(operator).append(" ")
                .append(source).append(" ").append(sourceObj);
        return this;
    }

    public ExecuteCommand ifDataStorage(StoragePath source, DataPath path) {
        command.append(" if data storage ").append(source).append(" ").append(path);
        return this;
    }

    public ExecuteCommand ifPredicate(String predicate) {
        command.append(" if predicate ").append(predicate);
        return this;
    }

    public ExecuteCommand ifLoaded(BlockPos pos) {
        command.append(" if loaded ").append(pos);
        return this;
    }

    // --- Terminal Action ---

    /**
     * Completes the execution chain by defining the command to run.
     * @param command The command to execute after all modifiers are applied.
     * @return The full generated command string.
     */
    public String run(MinecraftCommand command) {
        this.command.append(" run ").append(command.generate());
        return this.generate();
    }

    /**
     * Generates the command string built so far.
     */
    @Override
    public String generate() {
        return command.toString().trim();
    }

    @Override
    public String toString() {
        return generate();
    }
}