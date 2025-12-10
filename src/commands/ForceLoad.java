package commands;

import uhc.arguments.block.ColumnPos;
import commands.forceload.ForceLoadAction;

/**
 * Represents the Minecraft 'forceload' command structure.
 * This command toggles the forced loading of chunks in the dimension.
 * <p>
 * Command syntax variants:
 * <ul>
 * <li>{@code /forceload add <from> [to]}</li>
 * <li>{@code /forceload remove <from> [to] | all}</li>
 * <li>{@code /forceload query [pos]}</li>
 * </ul>
 */
public class ForceLoad {

    // The required action (ADD, REMOVE, QUERY).
    private final ForceLoadAction action;

    // Position/range arguments. Only one set should be used based on action.
    private ColumnPos from;
    private ColumnPos to;
    private ColumnPos pos; // Used exclusively by QUERY

    /**
     * Private constructor to enforce object creation via the static factory method.
     *
     * @param action The required forceload action.
     */
    private ForceLoad(ForceLoadAction action) {
        this.action = action;
    }

    /**
     * Static factory method to create a new ForceLoad command instance.
     *
     * @param action The required {@link ForceLoadAction}.
     * @return A new ForceLoad instance.
     * @throws IllegalArgumentException if {@code action} is null.
     */
    public static ForceLoad create(ForceLoadAction action) {
        if (action == null) {
            throw new IllegalArgumentException("ForceLoadAction cannot be null for the forceload command.");
        }
        return new ForceLoad(action);
    }

    // --- Fluent Setters for Configuration ---

    /**
     * Sets the starting chunk position for ADD/REMOVE actions.
     * Clears any single position set for QUERY.
     */
    public ForceLoad from(ColumnPos from) {
        this.from = from;
        this.pos = null; // Clear pos if setting range
        return this;
    }

    /**
     * Sets the ending chunk position for ADD/REMOVE actions (optional).
     */
    public ForceLoad to(ColumnPos to) {
        this.to = to;
        return this;
    }

    /**
     * Sets a single chunk position for the QUERY action (optional).
     * Clears any range set for ADD/REMOVE.
     */
    public ForceLoad pos(ColumnPos pos) {
        this.pos = pos;
        this.from = null; // Clear range if setting pos
        this.to = null;
        return this;
    }

    /**
     * Constructs and returns the final string representation of the 'forceload' command.
     *
     * @return The complete, formatted Minecraft command string.
     * @throws IllegalStateException if mandatory position arguments are missing for the ADD action.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("forceload ");

        // 1. Append Action
        sb.append(action.toString().toLowerCase());

        // 2. Handle Arguments based on Action

        if (action == ForceLoadAction.ADD) {
            // ADD requires a starting position.
            if (from == null) {
                throw new IllegalStateException("The 'add' action requires a starting chunk position (from).");
            }

            sb.append(" ").append(from);
            if (to != null) {
                sb.append(" ").append(to);
            }

        } else if (action == ForceLoadAction.REMOVE) {
            // REMOVE can be 'remove all' or a specific range.
            if (from == null) {
                // If no starting position is set, default to "all"
                sb.append(" ").append("all");
            } else {
                sb.append(" ").append(from);
                if (to != null) {
                    sb.append(" ").append(to);
                }
            }

        } else if (action == ForceLoadAction.QUERY) {
            // QUERY can be 'query' (all) or 'query <pos>'
            if (pos != null) {
                sb.append(" ").append(pos);
            }
        }

        return sb.toString();
    }
}