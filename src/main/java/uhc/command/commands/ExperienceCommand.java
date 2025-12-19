package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.game.ExperienceType;
import uhc.arguments.entity.Entity;

import java.util.Objects;

/**
 * ✨ **Experience Command Builder**
 * <p>
 * Provides a fluent API for the {@code /experience} (or {@code /xp}) command.
 * This class handles adding, setting, and querying both experience points and levels.
 * </p>
 */
public class ExperienceCommand implements MinecraftCommand {
    private final ExperienceAction action;
    private final Entity targets;
    private int amount;
    private ExperienceType type;

    private ExperienceCommand(ExperienceAction action, Entity targets) {
        // Enforce non-nullability for the core components of the command
        this.action = Objects.requireNonNull(action, "ExperienceAction cannot be null.");
        this.targets = Objects.requireNonNull(targets, "Targets entity cannot be null.");
    }

    /**
     * Initializes a new ExperienceCommand builder.
     * @param action The operation to perform (ADD, SET, or QUERY).
     * @param targets The target entities (usually players).
     * @return A new builder instance.
     */
    public static ExperienceCommand create(ExperienceAction action, Entity targets) {
        return new ExperienceCommand(action, targets);
    }

    /**
     * Sets the amount of experience or levels to add or set.
     * <p>Note: This is ignored when the action is set to {@code QUERY}.</p>
     * @param amount The numeric amount.
     * @return The current builder instance.
     */
    public ExperienceCommand amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the type of experience being targeted (e.g., points or levels).
     * <p>Required for {@code QUERY} actions.</p>
     * @param type The experience type.
     * @return The current builder instance.
     */
    public ExperienceCommand type(ExperienceType type) {
        this.type = type;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * <p>Syntax (Add/Set): {@code experience <add|set> <targets> <amount> [<type>]}</p>
     * <p>Syntax (Query): {@code experience query <targets> <type>}</p>
     * @return The formatted command string.
     * @throws IllegalStateException if the configuration is invalid for the chosen action.
     */
    @Override
    public String generate() {
        // 'experience' is the preferred canonical name in modern versions (1.13+)
        StringBuilder sb = new StringBuilder("experience ");

        sb.append(action).append(" ").append(targets).append(" ");

        if (action == ExperienceAction.ADD || action == ExperienceAction.SET) {
            // Amount is mandatory for ADD and SET.
            sb.append(amount);

            // Type is optional (defaults to points in Minecraft), but appended if specified.
            if (type != null) {
                sb.append(" ").append(type);
            }
        } else if (action == ExperienceAction.QUERY) {
            // For QUERY, the amount argument MUST NOT be present.
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

    /**
     * Defines the specific operation for the experience command.
     */
    public enum ExperienceAction {
        ADD,
        SET,
        QUERY;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}