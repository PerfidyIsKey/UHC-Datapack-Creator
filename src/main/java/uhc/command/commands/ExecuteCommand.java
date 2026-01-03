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
 * This class facilitates the construction of complex Minecraft {@code /execute} commands.
 * It employs a static factory entry-point pattern to initiate a fluent builder chain.
 * </p>
 * <p>
 * Every modifier method validates its inputs strictly; if a required argument is null,
 * a {@link NullPointerException} is thrown with a descriptive message to prevent
 * the generation of invalid datapack functions.
 * </p>
 */
public class ExecuteCommand implements MinecraftCommand {

    /** * The internal buffer holding the evolving command string.
     */
    private final StringBuilder command;

    /**
     * Private constructor used by static factory methods to begin the command chain.
     * Initializes the buffer with the base "execute" keyword.
     */
    private ExecuteCommand() {
        this.command = new StringBuilder("execute");
    }

    // --- 🚀 Static Entry Points ---
    // These methods allow starting a command with: ExecuteCommand.as(player)...

    /** @return A new builder starting with 'align'. */
    public static ExecuteCommand align(Swizzle axes) { return new ExecuteCommand().alignModifier(axes); }

    /** @return A new builder starting with 'anchored'. */
    public static ExecuteCommand anchored(EntityAnchor anchor) { return new ExecuteCommand().anchoredModifier(anchor); }

    /** @return A new builder starting with 'as'. */
    public static ExecuteCommand as(Entity targets) { return new ExecuteCommand().asModifier(targets); }

    /** @return A new builder starting with 'at'. */
    public static ExecuteCommand at(Entity targets) { return new ExecuteCommand().atModifier(targets); }

    /** @return A new builder starting with 'facing'. */
    public static ExecuteCommand facing(Vec3 pos) { return new ExecuteCommand().facingModifier(pos); }

    /** @return A new builder starting with 'in'. */
    public static ExecuteCommand in(DimensionId dimension) { return new ExecuteCommand().inModifier(dimension); }

    /** @return A new builder starting with 'on'. */
    public static ExecuteCommand on(RelationId relation) { return new ExecuteCommand().onModifier(relation); }

    /** @return A new builder starting with 'positioned'. */
    public static ExecuteCommand positioned(Vec3 pos) { return new ExecuteCommand().positionedModifier(pos); }

    /** @return A new builder starting with 'positioned as'. */
    public static ExecuteCommand positionedAs(Entity targets) { return new ExecuteCommand().positionedAsModifier(targets); }

    /** @return A new builder starting with 'positioned over'. */
    public static ExecuteCommand positionedOver(HeightMap heightmap) { return new ExecuteCommand().positionedOverModifier(heightmap); }

    /** @return A new builder starting with 'rotated'. */
    public static ExecuteCommand rotated(Rotation rot) { return new ExecuteCommand().rotatedModifier(rot); }

    /** @return A new builder starting with 'rotated as'. */
    public static ExecuteCommand rotatedAs(Entity targets) { return new ExecuteCommand().rotatedAsModifier(targets); }

    /** @return A new builder starting with 'summon'. */
    public static ExecuteCommand summon(EntityId entity) { return new ExecuteCommand().summonModifier(entity); }

    // --- 🚩 Conditional Entry Points ---

    /** Begins a builder with 'unless score <target> <obj> matches <range>'. */
    public static ExecuteCommand unlessScore(Entity target, ScoreboardObjectiveId objective, Range range) {
        return new ExecuteCommand().unlessScoreMatches(target, objective, range);
    }

    /** Begins a builder with 'unless score <target> <obj> <op> <source> <obj>'. */
    public static ExecuteCommand unlessScore(Entity target, ScoreboardObjectiveId targetObj, ComparatorType operator, Entity source, ScoreboardObjectiveId sourceObj) {
        return new ExecuteCommand().unlessScoreCompare(target, targetObj, operator, source, sourceObj);
    }

    /** Begins a builder with 'if entity'. */
    public static ExecuteCommand ifEntity(Entity entities) { return new ExecuteCommand().ifEntityModifier(entities); }

    /** Begins a builder with 'unless entity'. */
    public static ExecuteCommand unlessEntity(Entity entities) { return new ExecuteCommand().unlessEntityModifier(entities); }

    /** Begins a builder with 'if score ... matches'. */
    public static ExecuteCommand ifScoreMatches(Entity target, ScoreboardObjectiveId objective, Range range) { return new ExecuteCommand().ifScoreMatchesModifier(target, objective, range); }

    // --- 🔗 Instance Modifiers (Chaining) ---

    public ExecuteCommand alignModifier(Swizzle axes) { return appendModifier("align", axes); }
    public ExecuteCommand anchoredModifier(EntityAnchor anchor) { return appendModifier("anchored", anchor); }
    public ExecuteCommand asModifier(Entity targets) { return appendModifier("as", targets); }
    public ExecuteCommand atModifier(Entity targets) { return appendModifier("at", targets); }
    public ExecuteCommand facingModifier(Vec3 pos) { return appendModifier("facing", pos); }
    public ExecuteCommand inModifier(DimensionId dimension) { return appendModifier("in", dimension); }
    public ExecuteCommand onModifier(RelationId relation) { return appendModifier("on", relation); }
    public ExecuteCommand positionedModifier(Vec3 pos) { return appendModifier("positioned", pos); }
    public ExecuteCommand positionedAsModifier(Entity targets) { return appendModifier("positioned as", targets); }
    public ExecuteCommand positionedOverModifier(HeightMap heightmap) { return appendModifier("positioned over", heightmap); }
    public ExecuteCommand rotatedModifier(Rotation rot) { return appendModifier("rotated", rot); }
    public ExecuteCommand rotatedAsModifier(Entity targets) { return appendModifier("rotated as", targets); }
    public ExecuteCommand summonModifier(EntityId entity) { return appendModifier("summon", entity); }

    // --- 📊 Score Logic ---

    /**
     * Appends an 'unless score ... matches' check.
     * @throws NullPointerException if any argument is null.
     */
    public ExecuteCommand unlessScoreMatches(Entity target, ScoreboardObjectiveId objective, Range range) {
        Objects.requireNonNull(target, "UnlessScore logic failed: Target entity cannot be null.");
        Objects.requireNonNull(objective, "UnlessScore logic failed: Scoreboard objective cannot be null.");
        Objects.requireNonNull(range, "UnlessScore logic failed: Comparison range cannot be null.");
        command.append(" unless score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    /**
     * Appends an 'unless score' comparison between two entities.
     * @throws NullPointerException if any argument is null.
     */
    public ExecuteCommand unlessScoreCompare(Entity target, ScoreboardObjectiveId targetObj, ComparatorType operator, Entity source, ScoreboardObjectiveId sourceObj) {
        Objects.requireNonNull(target, "UnlessScore comparison failed: Target entity cannot be null.");
        Objects.requireNonNull(targetObj, "UnlessScore comparison failed: Target objective cannot be null.");
        Objects.requireNonNull(operator, "UnlessScore comparison failed: Comparator operator cannot be null.");
        Objects.requireNonNull(source, "UnlessScore comparison failed: Source entity cannot be null.");
        Objects.requireNonNull(sourceObj, "UnlessScore comparison failed: Source objective cannot be null.");
        command.append(" unless score ").append(target).append(" ").append(targetObj)
                .append(" ").append(operator).append(" ")
                .append(source).append(" ").append(sourceObj);
        return this;
    }

    /**
     * Appends an 'if score ... matches' check.
     */
    public ExecuteCommand ifScoreMatchesModifier(Entity target, ScoreboardObjectiveId objective, Range range) {
        Objects.requireNonNull(target, "IfScore logic failed: Target entity cannot be null.");
        Objects.requireNonNull(objective, "IfScore logic failed: Objective cannot be null.");
        Objects.requireNonNull(range, "IfScore logic failed: Range cannot be null.");
        command.append(" if score ").append(target).append(" ").append(objective).append(" matches ").append(range);
        return this;
    }

    public ExecuteCommand ifEntityModifier(Entity entities) { return appendModifier("if entity", entities); }
    public ExecuteCommand unlessEntityModifier(Entity entities) { return appendModifier("unless entity", entities); }

    // --- 🛠️ Store Modifiers ---

    /** * Stores the command result into a scoreboard.
     */
    public ExecuteCommand storeResultScore(Entity targets, ScoreboardObjectiveId objective) {
        Objects.requireNonNull(targets, "Store Result failed: Target entity cannot be null.");
        Objects.requireNonNull(objective, "Store Result failed: Objective cannot be null.");
        command.append(" store result score ").append(targets).append(" ").append(objective);
        return this;
    }

    // --- ⚙️ Internal Helper ---

    /**
     * Internal utility to safely append modifiers to the command string.
     * @param sub The execute subcommand (e.g., "as").
     * @param argument The argument for the subcommand.
     * @return This builder instance.
     * @throws NullPointerException if the argument is null.
     */
    private ExecuteCommand appendModifier(String sub, Object argument) {
        Objects.requireNonNull(argument, "Execution modifier '" + sub + "' failed: Argument cannot be null.");
        command.append(" ").append(sub).append(" ").append(argument);
        return this;
    }

    // --- 🏁 Terminal Action ---

    /**
     * Finalizes the execute chain by attaching the command to run.
     * @param runCommand The {@link MinecraftCommand} to execute under the built context.
     * @return This ExecuteCommand instance.
     * @throws RuntimeException if the runCommand is null or internal generation fails.
     */
    public ExecuteCommand run(MinecraftCommand runCommand) {
        try {
            Objects.requireNonNull(runCommand, "Run action failed: The command to execute cannot be null.");
            this.command.append(" run ").append(runCommand.generate());
            return this;
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected failure in ExecuteCommand.run(): " + e.getMessage(), e);
        }
    }

    /**
     * Generates the final string representation of the command.
     * @return The complete /execute string.
     * @throws IllegalStateException if no modifiers or run action were added.
     */
    @Override
    public String generate() {
        String result = command.toString().trim();
        if (result.equals("execute")) {
            throw new IllegalStateException("Command generation failed: ExecuteCommand must contain at least one modifier or a 'run' action.");
        }
        return result;
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines the targeted property of a bossbar for 'store' operations.
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