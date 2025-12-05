package commands;

import commands.data.DataAction;
import commands.data.DataModification;
import commands.data.DataTarget;
import commands.data.DataPath;

/**
 * Represents the base Minecraft /data command for manipulating NBT data.
 * This class uses factory methods to ensure the correct arguments are passed
 * for each action (GET, MODIFY, MERGE, REMOVE).
 */
public class Data {
    private final DataAction action;
    private final DataTarget target;
    /**
     * The NBT path string (e.g., "Pos[0] 1" or "CollarColor").
     * Optional for GET, mandatory for MODIFY, MERGE, and REMOVE (for MERGE/REMOVE,
     * this class assumes the path will be supplied here or in the DataModification).
     */
    private final DataPath path;
    /**
     * The specific modification operation (e.g., "set value 4b"). Required for MODIFY action.
     */
    private final DataModification modification;

    /**
     * Private constructor for the Data command.
     * All command components must be assembled using the static factory methods.
     * * @param action The primary data operation (get, modify, merge, remove).
     * @param target The target entity/block being manipulated.
     * @param path The specific NBT path to target, or null if getting all NBT.
     * @param modification The modification payload, or null if not a MODIFY action.
     */
    private Data(DataAction action, DataTarget target, DataPath path, DataModification modification) {
        this.action = action;
        this.target = target;
        this.path = path;
        this.modification = modification;

        // Ensure MODIFY actions are fully specified.
        if (action == DataAction.MODIFY && (path == null || modification == null)) {
            throw new IllegalArgumentException("DataAction.MODIFY requires both a DataPath and a DataModification.");
        }
    }

    // --- Static Creation Methods ---

    /**
     * Creates a Data command for the GET action.
     * Supports commands with or without a specific NBT path.
     * @param target The entity or block target.
     * @param path The NBT path and optional scale (e.g., DataPath.createWithScale("Pos[0]", 1)),
     * or {@code null} to get the entire NBT tag.
     * @return A new Data command instance.
     * Example: data get entity @s Pos[0] 1
     * Example: data get entity @s
     */
    public static Data createGet(DataTarget target, DataPath path) {
        // Path can be null here to support "data get <target>" (entire NBT dump)
        return new Data(DataAction.GET, target, path, null);
    }

    /**
     * Creates a Data command for the MODIFY action.
     * Requires both the specific path and the modification operation.
     * @param target The entity or block target.
     * @param path The mandatory NBT path (e.g., DataPath.create("CollarColor")).
     * @param modification The modification to apply (e.g., ModificationSetValue).
     * @return A new Data command instance.
     * Example: data modify entity @s CollarColor set value 4b
     */
    public static Data createModify(DataTarget target, DataPath path, DataModification modification) {
        return new Data(DataAction.MODIFY, target, path, modification);
    }

    /**
     * Creates a Data command for the MERGE or REMOVE actions targeting the target's root NBT.
     * NOTE: For full MERGE or REMOVE functionality on a specific path, the 'path' parameter
     * must be included in the constructor or a separate factory method.
     * @param action Must be MERGE or REMOVE.
     * @param target The entity or block target.
     * @return A new Data command instance.
     */
    public static Data createSimple(DataAction action, DataTarget target) {
        if (action != DataAction.MERGE && action != DataAction.REMOVE) {
            throw new IllegalArgumentException("Use a specific factory method for the " + action + " action.");
        }
        // MERGE and REMOVE without path are technically invalid in Minecraft.
        // A proper implementation should probably require a path for REMOVE
        // and a path/source for MERGE. Keeping it here for now based on your initial structure.
        return new Data(action, target, null, null);
    }

    // --- Command Builder ---

    /**
     * Builds the final, executable Minecraft data command string.
     * @return The complete Minecraft command string.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("data ");

        sb.append(action).append(" ").append(target);

        if (action == DataAction.GET) {
            // Path is optional for GET.
            if (path != null) {
                sb.append(" ").append(path);
            }
        } else if (action == DataAction.MODIFY) {
            // Path and modification are mandatory and are appended sequentially.
            sb.append(" ").append(path);
            sb.append(" ").append(modification);
        } else if (action == DataAction.REMOVE) {
            // REMOVE usually requires a path, which would be in the 'path' field.
            if (path != null) {
                sb.append(" ").append(path);
            }
        } else if (action == DataAction.MERGE) {
            // MERGE usually requires a source NBT, which would likely be stored in a 'modification' or 'source' field.
            // If a path is provided, it modifies NBT *at* that path.
            if (path != null) {
                sb.append(" ").append(path);
            }
        }

        return sb.toString();
    }
}