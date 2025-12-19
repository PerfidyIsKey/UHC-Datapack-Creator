package uhc.command.commands;

import uhc.arguments.block.ColumnPos;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🛠️ **ForceLoad Command Builder**
 * <p>
 * Provides a fluent API for the {@code /forceload} command, which controls chunk
 * persistence in a dimension. Note that Minecraft forceloading operates on
 * <b>chunk coordinates</b> or <b>column positions</b> (X and Z).
 * </p>
 * <p>
 * <b>Syntax Variants:</b>
 * <ul>
 * <li>{@code /forceload add <from> [<to>]}</li>
 * <li>{@code /forceload remove <from> [<to>] | all}</li>
 * <li>{@code /forceload query [<pos>]}</li>
 * </ul>
 * </p>
 */
public class ForceLoadCommand implements MinecraftCommand {

    private final ForceLoadAction action;

    // Position/range arguments.
    private ColumnPos from;
    private ColumnPos to;
    private ColumnPos pos; // Used exclusively by QUERY

    private ForceLoadCommand(ForceLoadAction action) {
        this.action = action;
    }

    /**
     * Initializes a new ForceLoad command builder.
     * @param action The required action (ADD, REMOVE, or QUERY).
     * @return A new builder instance.
     */
    public static ForceLoadCommand create(ForceLoadAction action) {
        return new ForceLoadCommand(Objects.requireNonNull(action, "ForceLoadAction cannot be null."));
    }

    // --- Fluent Setters ---

    /**
     * Sets the starting chunk position for ADD or REMOVE actions.
     * <p>Calling this clears any previously set 'pos' argument.</p>
     */
    public ForceLoadCommand from(ColumnPos from) {
        this.from = from;
        this.pos = null;
        return this;
    }

    /**
     * Sets the ending chunk position for ADD or REMOVE actions.
     * @param to The end chunk of the range.
     */
    public ForceLoadCommand to(ColumnPos to) {
        this.to = to;
        this.pos = null;
        return this;
    }

    /**
     * Sets a specific chunk position to check for the QUERY action.
     * <p>Calling this clears any previously set 'from' or 'to' arguments.</p>
     */
    public ForceLoadCommand pos(ColumnPos pos) {
        this.pos = pos;
        this.from = null;
        this.to = null;
        return this;
    }

    /**
     * Validates and generates the final command string.
     * @return The formatted Minecraft command.
     * @throws IllegalStateException if required positions are missing for the selected action.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("forceload ");

        sb.append(action); // enum.toString() handles lowercase conversion

        switch (action) {
            case ADD:
                // Minecraft 'add' requires at least one position.
                if (from == null) {
                    throw new IllegalStateException("Action 'add' requires at least a 'from' ColumnPos.");
                }
                sb.append(" ").append(from);
                if (to != null) {
                    sb.append(" ").append(to);
                }
                break;

            case REMOVE:
                // 'remove all' is used if no range is specified.
                if (from == null) {
                    sb.append(" all");
                } else {
                    sb.append(" ").append(from);
                    if (to != null) {
                        sb.append(" ").append(to);
                    }
                }
                break;

            case QUERY:
                // 'query' lists all, 'query <pos>' checks a specific chunk.
                if (pos != null) {
                    sb.append(" ").append(pos);
                }
                break;
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    public enum ForceLoadAction {
        ADD, REMOVE, QUERY;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}