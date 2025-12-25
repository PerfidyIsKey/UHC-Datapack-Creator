package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.arguments.entity.Entity;

import java.util.Objects;

/**
 * ✨ **Experience Command Builder**
 * <p>
 * Provides a fluent API for the {@code /experience} (or {@code /xp}) command.
 * This class handles adding, setting, and querying both experience points and levels.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /experience <add|set|query> <targets> [<amount>] [<type>]}
 * </p>
 */
public class ExperienceCommand implements MinecraftCommand {

    // --- 🏷️ Nested Types ---

    /**
     * 📊 **Experience Metric Type**
     * <p>Defines whether the command targets raw experience points or whole levels.</p>
     */
    public enum ExperienceType {
        /** Represents the individual XP points toward the next level. */
        POINTS,

        /** Represents the total level count of the player. */
        LEVELS;

        /**
         * Returns the lowercase name for command compatibility.
         * @return "points" or "levels".
         */
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * ⚙️ **Experience Operation**
     * <p>Defines the specific action to perform on the target's experience.</p>
     */
    public enum ExperienceAction {
        /** Adds the specified amount to the target's total. */
        ADD,

        /** Sets the target's total to the specified amount. */
        SET,

        /** Queries the target's current amount for conditional logic. */
        QUERY;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    // --- ⚙️ State & Fields ---

    /** * The operation to perform (ADD, SET, QUERY). */
    private final ExperienceAction action;

    /** * The target entities (players). */
    private final Entity targets;

    /** * The numeric amount to add or set. Ignored in QUERY. */
    private int amount = 0;

    /** * The metric type (points or levels). Required for QUERY. */
    private ExperienceType type;

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor for the fluent builder.
     * @param action The operation mode.
     * @param targets The target entities.
     */
    private ExperienceCommand(ExperienceAction action, Entity targets) {
        this.action = Objects.requireNonNull(action, "ExperienceAction cannot be null.");
        this.targets = Objects.requireNonNull(targets, "Targets entity cannot be null.");
    }

    /**
     * Initializes a new ExperienceCommand builder.
     * @param action The operation to perform.
     * @param targets The target entities.
     * @return A new builder instance.
     */
    public static ExperienceCommand create(ExperienceAction action, Entity targets) {
        return new ExperienceCommand(action, targets);
    }

    // --- 🛰️ Builder Methods ---

    /**
     * Sets the amount of experience or levels.
     * @param amount The numeric amount.
     * @return The current builder instance.
     */
    public ExperienceCommand amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the type of experience metric being targeted.
     * @param type The experience type (POINTS/LEVELS).
     * @return The current builder instance.
     */
    public ExperienceCommand type(ExperienceType type) {
        this.type = type;
        return this;
    }

    // --- 🛡️ Generation & Logic ---

    /**
     * Generates the final Minecraft command string.
     * <p><b>Error Catching:</b> Validates that a type is provided when querying,
     * as Minecraft requires the metric to be specified for the QUERY action.</p>
     * @return The formatted command string.
     * @throws IllegalStateException if a type is missing during a QUERY.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("experience ");

        // Common prefix: /experience <action> <targets>
        sb.append(action).append(" ").append(targets).append(" ");

        if (action == ExperienceAction.ADD || action == ExperienceAction.SET) {
            // Amount is mandatory for ADD and SET.
            sb.append(amount);

            // Type is optional (Minecraft defaults to points), but appended if specified.
            if (type != null) {
                sb.append(" ").append(type);
            }
        } else if (action == ExperienceAction.QUERY) {
            // For QUERY, the amount is omitted.
            // Syntax: /experience query <target> <levels|points>
            if (type == null) {
                throw new IllegalStateException("ExperienceType (levels/points) must be specified when using QUERY.");
            }
            sb.append(type);
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return generate();
    }
}