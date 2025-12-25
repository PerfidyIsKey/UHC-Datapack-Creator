package uhc.command.commands;

import uhc.arguments.data.DataPath;
import uhc.command.MinecraftCommand;
import uhc.command.commands.data.DataModification;
import uhc.command.commands.data.DataTarget;

import java.util.Objects;

/**
 * 💾 **Data Command Builder**
 * <p>
 * This class is the primary orchestrator for generating Minecraft {@code /data} commands.
 * It manages the four main sub-commands: {@code get}, {@code merge}, {@code modify},
 * and {@code remove}.
 * </p>
 * <p>
 * The builder enforces structural integrity by validating that specific operations
 * (like {@code MODIFY}) have their required components (like {@code DataPath} and
 * {@code DataModification}) before the command can be generated.
 * </p>
 */
public class DataCommand implements MinecraftCommand {

    // --- ⚙️ State & Fields ---

    /** * The high-level action to perform on the NBT data.
     * Defines the command branch (get, merge, modify, or remove).
     */
    private final DataAction action;

    /** * The provider for the target identification (entity, block, or storage).
     * Handles the {@code <type> <identifier>} segment of the command.
     */
    private final DataTarget target;

    /** * The NBT path used to pinpoint specific tags within the target's data structure.
     * Optional for {@code GET}, required for {@code MODIFY} and {@code REMOVE}.
     */
    private final DataPath path;

    /** * The modification logic defining what changes to apply to the NBT.
     * Required for {@code MODIFY} and {@code MERGE} operations.
     */
    private final DataModification modification;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for the DataCommand.
     * <p><b>Error Catching:</b> Strictly validates that the core components (action and target)
     * are present and executes {@link #validateState()} to check for branch-specific requirements.</p>
     * * @param action       The {@link DataAction} to execute.
     * @param target       The {@link DataTarget} containing the target data.
     * @param path         The optional {@link DataPath} to access.
     * @param modification The optional {@link DataModification} logic.
     * @throws NullPointerException if action or target is null.
     */
    private DataCommand(DataAction action, DataTarget target, DataPath path, DataModification modification) {
        this.action = Objects.requireNonNull(action, "DataAction cannot be null.");
        this.target = Objects.requireNonNull(target, "DataTarget cannot be null.");
        this.path = path;
        this.modification = modification;

        validateState();
    }

    /**
     * Internal validation logic to enforce Minecraft command syntax rules.
     * <p><b>Catching Errors:</b> This method prevents the construction of commands
     * that would be rejected by the Minecraft server for missing parameters.</p>
     * * @throws IllegalArgumentException if required fields for a specific action are null.
     */
    private void validateState() {
        if (action == DataAction.MODIFY) {
            if (path == null) throw new IllegalArgumentException("Action 'MODIFY' requires a DataPath.");
            if (modification == null) throw new IllegalArgumentException("Action 'MODIFY' requires a DataModification.");
        }
        if (action == DataAction.REMOVE && path == null) {
            throw new IllegalArgumentException("Action 'REMOVE' must target a specific DataPath.");
        }
        if (action == DataAction.MERGE && modification == null) {
            throw new IllegalArgumentException("Action 'MERGE' requires a source NBT modification.");
        }
    }

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates a command to retrieve NBT data.
     * <p>Syntax: {@code /data get <target> [<path>]}</p>
     * @param target The data holder.
     * @param path   The optional NBT path.
     * @return A new DataCommand instance.
     */
    public static DataCommand createGet(DataTarget target, DataPath path) {
        return new DataCommand(DataAction.GET, target, path, null);
    }

    /**
     * Creates a command to modify existing NBT data.
     * <p>Syntax: {@code /data modify <target> <path> <modification>}</p>
     * @param target       The data holder.
     * @param path         The specific path to modify.
     * @param modification The modification logic.
     * @return A new DataCommand instance.
     */
    public static DataCommand createModify(DataTarget target, DataPath path, DataModification modification) {
        return new DataCommand(DataAction.MODIFY, target, path, modification);
    }

    /**
     * Creates a command to remove NBT data at a path.
     * <p>Syntax: {@code /data remove <target> <path>}</p>
     * @param target The data holder.
     * @param path   The path to the tag to be deleted.
     * @return A new DataCommand instance.
     */
    public static DataCommand createRemove(DataTarget target, DataPath path) {
        return new DataCommand(DataAction.REMOVE, target, Objects.requireNonNull(path, "Path required for REMOVE."), null);
    }

    /**
     * Creates a command to merge new NBT into the target's root.
     * <p>Syntax: {@code /data merge <target> <nbt>}</p>
     * @param target    The data holder.
     * @param nbtSource The modification representing the NBT source.
     * @return A new DataCommand instance.
     */
    public static DataCommand createMerge(DataTarget target, DataModification nbtSource) {
        return new DataCommand(DataAction.MERGE, target, null, Objects.requireNonNull(nbtSource, "NBT source required for MERGE."));
    }

    // --- 🛰️ Generation Logic ---

    /**
     * Generates the finalized Minecraft command string.
     * <p><b>Error Catching:</b> Utilizes the {@code toString()} implementations of
     * rich objects (Target, Path, Modification) to assemble the command without
     * manual string casting.</p>
     * * @return The command (e.g., "data get entity @s Pos").
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("data ");
        sb.append(action).append(" ").append(target);

        switch (action) {
            case GET:
                if (path != null) sb.append(" ").append(path);
                break;
            case MODIFY:
                sb.append(" ").append(path).append(" ").append(modification);
                break;
            case REMOVE:
                sb.append(" ").append(path);
                break;
            case MERGE:
                sb.append(" ").append(modification);
                break;
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    // --- 🏷️ Nested Enums ---

    /**
     * Defines the primary branches of the {@code /data} command.
     */
    public enum DataAction {
        GET, MERGE, MODIFY, REMOVE;

        /** @return The lowercase command keyword (e.g., "merge"). */
        @Override
        public String toString() { return this.name().toLowerCase(); }
    }
}