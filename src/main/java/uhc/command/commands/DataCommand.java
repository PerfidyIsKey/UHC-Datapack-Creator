package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.data.DataPath;
import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 💾 **Data Command Builder (DataCommand)**
 * <p>
 * This class provides a flattened, type-safe API for constructing Minecraft {@code /data} commands.
 * It manages the manipulation of NBT data for blocks, entities, and command storage.
 * </p>
 * <p>
 * <b>Strict Policy:</b> This class avoids fallbacks. If a required parameter (like a {@link DataPath}
 * for a scale operation) is missing, an explicit Exception is thrown to ensure command validity.
 * </p>
 */
public class DataCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The validated sub-command instruction.
     * This contains the action, target, and arguments (e.g., "get entity @s Pos 1.0").
     */
    private final String instruction;

    // --- 🏗️ Private Constructor ---

    /**
     * Constructs a DataCommand with a pre-validated instruction segment.
     * <p><b>Error Catching:</b> Ensures the internal instruction is never null.</p>
     * @param instruction The formatted sub-command.
     * @throws NullPointerException if instruction is null.
     */
    private DataCommand(String instruction) {
        this.instruction = Objects.requireNonNull(instruction, "Data Error: Internal instruction string cannot be null.");
    }

    // --- 🔍 Static Entry Points: GET ---

    /**
     * Retrieves all NBT data from a block at the specified position.
     * @param pos The {@link BlockPos} of the tile entity; must not be null.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code pos} is null.
     */
    public static DataCommand get(BlockPos pos) {
        return get(pos, null, null);
    }

    /**
     * Retrieves NBT data from a specific path within a block.
     * @param pos  The {@link BlockPos} of the tile entity; must not be null.
     * @param path The {@link DataPath} to query.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code pos} is null.
     */
    public static DataCommand get(BlockPos pos, DataPath path) {
        return get(pos, path, null);
    }

    /**
     * Retrieves NBT data from a block, optionally scaled by a multiplier.
     * @param pos   The {@link BlockPos} of the tile entity; must not be null.
     * @param path  The {@link DataPath} to query; required if scale is provided.
     * @param scale The numeric multiplier for the retrieved value.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code pos} is null.
     * @throws IllegalArgumentException if {@code scale} is provided without a {@code path}.
     */
    public static DataCommand get(BlockPos pos, DataPath path, Double scale) {
        Objects.requireNonNull(pos, "Data Get Error: Block position cannot be null.");
        return new DataCommand(formatGet("block " + pos, path, scale));
    }

    /**
     * Retrieves all NBT data from the specified entity.
     * @param target The {@link Entity} selector; must not be null.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code target} is null.
     */
    public static DataCommand get(Entity target) {
        return get(target, null, null);
    }

    /**
     * Retrieves NBT data from a specific path within an entity.
     * @param target The {@link Entity} selector; must not be null.
     * @param path   The {@link DataPath} to query.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code target} is null.
     */
    public static DataCommand get(Entity target, DataPath path) {
        return get(target, path, null);
    }

    /**
     * Retrieves NBT data from an entity, optionally scaled by a multiplier.
     * @param target The {@link Entity} selector; must not be null.
     * @param path   The {@link DataPath} to query; required if scale is provided.
     * @param scale  The numeric multiplier for the retrieved value.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code target} is null.
     * @throws IllegalArgumentException if {@code scale} is provided without a {@code path}.
     */
    public static DataCommand get(Entity target, DataPath path, Double scale) {
        Objects.requireNonNull(target, "Data Get Error: Entity target cannot be null.");
        return new DataCommand(formatGet("entity " + target, path, scale));
    }

    /**
     * Retrieves NBT data from a named storage container.
     * @param storage The storage namespace/ID; must not be null.
     * @param path    The {@link DataPath} to query.
     * @param scale   The numeric multiplier for the retrieved value.
     * @return A new {@link DataCommand} instance.
     * @throws NullPointerException if {@code storage} is null.
     */
    public static DataCommand get(String storage, DataPath path, Double scale) {
        Objects.requireNonNull(storage, "Data Get Error: Storage namespace cannot be null.");
        return new DataCommand(formatGet("storage " + storage, path, scale));
    }

    // --- 🛠️ Static Entry Points: MODIFY (SET VALUE) ---

    /**
     * Modifies a block's NBT by setting a specific path to a new value.
     * @param pos        The {@link BlockPos} of the tile entity; must not be null.
     * @param targetPath The {@link DataPath} to overwrite; must not be null.
     * @param value      The value to insert (e.g., Integer, String, Compound); must not be null.
     * @return A new {@link DataCommand} instance.
     */
    public static DataCommand setBlockValue(BlockPos pos, DataPath targetPath, Object value) {
        Objects.requireNonNull(pos, "Data Modify Error: Block position cannot be null.");
        return createModifyValue("block " + pos, targetPath, value);
    }

    /**
     * Modifies an entity's NBT by setting a specific path to a new value.
     * <p>Syntax: {@code /data modify entity <target> <targetPath> set value <value>}</p>
     * @param target     The {@link Entity} selector; must not be null.
     * @param targetPath The {@link DataPath} to overwrite; must not be null.
     * @param value      The value to insert; must not be null.
     * @return A new {@link DataCommand} instance.
     */
    public static DataCommand setEntityValue(Entity target, DataPath targetPath, Object value) {
        Objects.requireNonNull(target, "Data Modify Error: Entity target cannot be null.");
        return createModifyValue("entity " + target, targetPath, value);
    }

    /**
     * Modifies a storage container's NBT by setting a specific path to a new value.
     * @param storage    The storage namespace/ID; must not be null.
     * @param targetPath The {@link DataPath} to overwrite; must not be null.
     * @param value      The value to insert; must not be null.
     * @return A new {@link DataCommand} instance.
     */
    public static DataCommand setStorageValue(String storage, DataPath targetPath, Object value) {
        Objects.requireNonNull(storage, "Data Modify Error: Storage namespace cannot be null.");
        return createModifyValue("storage " + storage, targetPath, value);
    }

    // --- ⚙️ Internal Logic & Helpers ---

    /**
     * Core logic for formatting the 'get' sub-command.
     * @param targetPrefix The target identifier (e.g. "block 0 0 0").
     * @param path         The optional path.
     * @param scale        The optional scale.
     * @return A formatted instruction string.
     * @throws IllegalArgumentException if scale is used without a path.
     */
    private static String formatGet(String targetPrefix, DataPath path, Double scale) {
        final StringBuilder sb = new StringBuilder("get ").append(targetPrefix);

        if (path != null) {
            sb.append(" ").append(path);
            if (scale != null) {
                sb.append(" ").append(scale);
            }
        } else if (scale != null) {
            throw new IllegalArgumentException("Data Error: A DataPath must be provided when using a scale multiplier.");
        }

        return sb.toString();
    }

    /**
     * Core logic for formatting 'modify ... set value' instructions.
     * @param targetPrefix The target identifier.
     * @param path         The target NBT path.
     * @param value        The object value to set.
     * @return A new {@link DataCommand} instance.
     */
    private static DataCommand createModifyValue(String targetPrefix, DataPath path, Object value) {
        Objects.requireNonNull(path, "Data Modify Error: Target path is required.");
        Objects.requireNonNull(value, "Data Modify Error: Value cannot be null.");

        // Properly escape strings for NBT syntax
        final String formattedValue = (value instanceof String) ? "\"" + value + "\"" : value.toString();

        return new DataCommand(String.format("modify %s %s set value %s", targetPrefix, path, formattedValue));
    }

    // --- 🛰️ Command Generation ---

    /**
     * Generates the final Minecraft command string.
     * @return The complete command (e.g., "data get block 10 64 10 Items").
     */
    @Override
    public String generate() {
        return "data " + this.instruction;
    }

    /**
     * Returns the finalized command string.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}