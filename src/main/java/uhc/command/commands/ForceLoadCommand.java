package uhc.command.commands;

import uhc.arguments.block.ColumnPos;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🛠️ **ForceLoad Command Builder**
 * <p>
 * Provides a semantic, fluent API for the {@code /forceload} command.
 * Used to manage which chunks are kept loaded in memory within a dimension.
 * </p>
 */
public class ForceLoadCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The specific sub-action of this command (add, remove, or query). */
    private final ForceLoadAction action;

    /** * The primary chunk coordinate or start of a range. */
    private ColumnPos from;

    /** * The optional end coordinate of a chunk range. */
    private ColumnPos to;

    /** * Flag indicating if all forceloaded chunks should be removed. */
    private boolean removeAll = false;

    // --- 🏗️ Constructors & Static Entry Points ---

    /**
     * Private constructor to enforce entry through semantic static methods.
     * @param action The non-null action type.
     */
    private ForceLoadCommand(ForceLoadAction action) {
        this.action = action;
    }

    /**
     * Entry point for the {@code /forceload add} command.
     * @param from The required starting chunk position.
     * @return A builder instance for the add action.
     * @throws NullPointerException if from is null.
     */
    public static ForceLoadCommand add(ColumnPos from) {
        Objects.requireNonNull(from, "ForceLoad Error: 'from' position cannot be null for 'add'.");
        return new ForceLoadCommand(ForceLoadAction.ADD).from(from);
    }

    /**
     * Entry point for the {@code /forceload add <from> <to>} command.
     * @param from The starting chunk position.
     * @param to   The ending chunk position.
     * @return A builder instance for the add action.
     */
    public static ForceLoadCommand add(ColumnPos from, ColumnPos to) {
        return add(from).to(to);
    }

    /**
     * Entry point for the {@code /forceload remove} command.
     * @param from The required starting chunk position to stop loading.
     * @return A builder instance for the remove action.
     */
    public static ForceLoadCommand remove(ColumnPos from) {
        Objects.requireNonNull(from, "ForceLoad Error: 'from' position cannot be null for 'remove'.");
        return new ForceLoadCommand(ForceLoadAction.REMOVE).from(from);
    }

    /**
     * Entry point for the {@code /forceload remove <from> <to>} command.
     * @param from The starting chunk position.
     * @param to   The ending chunk position.
     * @return A builder instance for the remove action.
     */
    public static ForceLoadCommand remove(ColumnPos from, ColumnPos to) {
        return remove(from).to(to);
    }

    /**
     * Entry point for the {@code /forceload remove all} command.
     * @return A builder instance for removing all chunks.
     */
    public static ForceLoadCommand removeAll() {
        ForceLoadCommand cmd = new ForceLoadCommand(ForceLoadAction.REMOVE);
        cmd.removeAll = true;
        return cmd;
    }

    /**
     * Entry point for the {@code /forceload query} command (global).
     * @return A builder instance for global query.
     */
    public static ForceLoadCommand query() {
        return new ForceLoadCommand(ForceLoadAction.QUERY);
    }

    /**
     * Entry point for the {@code /forceload query <pos>} command.
     * @param pos The specific chunk position to query.
     * @return A builder instance for a specific chunk query.
     */
    public static ForceLoadCommand query(ColumnPos pos) {
        return query().pos(pos);
    }

    // --- 🛠️ Internal Fluent Setters ---

    private ForceLoadCommand from(ColumnPos from) {
        this.from = from;
        return this;
    }

    private ForceLoadCommand to(ColumnPos to) {
        this.to = Objects.requireNonNull(to, "ForceLoad Error: 'to' position cannot be null if specified.");
        return this;
    }

    private ForceLoadCommand pos(ColumnPos pos) {
        this.from = Objects.requireNonNull(pos, "ForceLoad Error: Query position cannot be null.");
        return this;
    }

    // --- ⚙️ Command Generation ---

    /**
     * Validates and generates the final Minecraft command string.
     * <p><b>Strict Error Policy:</b> Throws exceptions for missing required arguments.</p>
     * @return The formatted command string.
     * @throws IllegalStateException if the command state is invalid for the chosen action.
     */
    @Override
    public String generate() {
        final StringBuilder sb = new StringBuilder("forceload ");
        sb.append(action.toString());

        try {
            switch (action) {
                case ADD -> this.buildAddLogic(sb);
                case REMOVE -> this.buildRemoveLogic(sb);
                case QUERY -> this.buildQueryLogic(sb);
            }
        } catch (Exception e) {
            throw new IllegalStateException("ForceLoad Generation Error: " + e.getMessage(), e);
        }

        return sb.toString();
    }

    private void buildAddLogic(StringBuilder sb) {
        if (from == null) {
            throw new IllegalStateException("Action 'add' requires at least one ColumnPos.");
        }
        sb.append(" ").append(from);
        if (to != null) {
            sb.append(" ").append(to);
        }
    }

    private void buildRemoveLogic(StringBuilder sb) {
        if (removeAll) {
            sb.append(" all");
            return;
        }
        if (from == null) {
            throw new IllegalStateException("Action 'remove' requires a ColumnPos or the 'all' flag.");
        }
        sb.append(" ").append(from);
        if (to != null) {
            sb.append(" ").append(to);
        }
    }

    private void buildQueryLogic(StringBuilder sb) {
        if (from != null) {
            sb.append(" ").append(from);
        }
        if (to != null) {
            throw new IllegalStateException("'query' does not support chunk ranges (found 'to' argument).");
        }
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Internal identifier for the /forceload sub-commands.
     */
    public enum ForceLoadAction {
        ADD, REMOVE, QUERY;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}