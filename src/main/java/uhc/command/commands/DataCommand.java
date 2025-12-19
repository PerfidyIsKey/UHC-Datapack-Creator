package uhc.command.commands;

import uhc.data.nbt.DataModification;
import uhc.data.target.DataTarget;
import uhc.arguments.data.DataPath;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 💾 **Data Command Builder**
 * <p>
 * Provides a structured way to build {@code /data} commands.
 * This class handles the branching logic for NBT access: GET, MERGE, MODIFY, and REMOVE.
 * </p>
 */
public class DataCommand implements MinecraftCommand {
    private final DataAction action;
    private final DataTarget target;

    /**
     * The NBT path string (e.g., "Pos[0]" or "CollarColor").
     * Optional for GET, but required for MODIFY and REMOVE.
     */
    private final DataPath path;

    /**
     * The specific modification operation (e.g., "set value 4b").
     * Mandatory for MODIFY and MERGE.
     */
    private final DataModification modification;

    private DataCommand(DataAction action, DataTarget target, DataPath path, DataModification modification) {
        this.action = Objects.requireNonNull(action, "DataAction cannot be null.");
        this.target = Objects.requireNonNull(target, "DataTarget cannot be null.");
        this.path = path;
        this.modification = modification;

        // Validation logic for Minecraft syntax rules
        validateState();
    }

    private void validateState() {
        if (action == DataAction.MODIFY) {
            if (path == null) throw new IllegalArgumentException("MODIFY requires a DataPath.");
            if (modification == null) throw new IllegalArgumentException("MODIFY requires a DataModification.");
        }
        if (action == DataAction.REMOVE && path == null) {
            throw new IllegalArgumentException("REMOVE action must target a specific DataPath.");
        }
        if (action == DataAction.MERGE && modification == null) {
            throw new IllegalArgumentException("MERGE action requires a source NBT modification.");
        }
    }

    // --- Static Creation Methods ---

    /**
     * Creates a command to query NBT.
     * <p>Syntax: {@code /data get <target> [<path>] [<scale>]}</p>
     */
    public static DataCommand createGet(DataTarget target, DataPath path) {
        return new DataCommand(DataAction.GET, target, path, null);
    }

    /**
     * Creates a command to manipulate NBT.
     * <p>Syntax: {@code /data modify <target> <path> <operation> ...}</p>
     */
    public static DataCommand createModify(DataTarget target, DataPath path, DataModification modification) {
        return new DataCommand(DataAction.MODIFY, target, path, modification);
    }

    /**
     * Creates a command to remove a specific NBT tag.
     * <p>Syntax: {@code /data remove <target> <path>}</p>
     */
    public static DataCommand createRemove(DataTarget target, DataPath path) {
        return new DataCommand(DataAction.REMOVE, target, Objects.requireNonNull(path), null);
    }

    /**
     * Creates a command to merge NBT data into a target.
     * <p>Syntax: {@code /data merge <target> <nbt>}</p>
     */
    public static DataCommand createMerge(DataTarget target, DataModification nbtSource) {
        return new DataCommand(DataAction.MERGE, target, null, Objects.requireNonNull(nbtSource));
    }

    /**
     * Internal legacy factory.
     * @deprecated Use specific creators for better validation.
     */
    @Deprecated
    public static DataCommand createSimple(DataAction action, DataTarget target) {
        if (action == DataAction.REMOVE || action == DataAction.MODIFY) {
            throw new IllegalArgumentException(action + " requires more parameters than createSimple provides.");
        }
        return new DataCommand(action, target, null, null);
    }

    // --- Generator ---

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
                // Minecraft REMOVE requires a path.
                sb.append(" ").append(path);
                break;
            case MERGE:
                // MERGE usually looks like: merge <target> {nbt_object}
                // modification toString() should handle the NBT object/source syntax.
                sb.append(" ").append(modification);
                break;
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    public enum DataAction {
        GET, MERGE, MODIFY, REMOVE;
        @Override
        public String toString() { return this.name().toLowerCase(); }
    }
}