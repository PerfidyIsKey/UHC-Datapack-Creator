package uhc.arguments.data;

import uhc.data.resource.DataPathId;

/**
 * Represents the NBT path used in a Minecraft data command.
 * This can be a simple path (e.g., "CollarColor"), a path with an index (e.g., "Owner[0]"),
 * or a path with an index and optional scale factor (e.g., "Pos[0] 1").
 */
public class DataPath {
    private final DataPathId pathId;
    private final Integer index; // Optional index (e.g., '0' for [0])
    private final Integer scale; // Optional scale factor for use with 'data get'

    // --- Constructors ---

    // Updated private constructor to handle all options
    private DataPath(DataPathId pathId, Integer index, Integer scale) {
        if (pathId == null) {
            throw new IllegalArgumentException("DataPathId cannot be null.");
        }
        if (scale != null && scale <= 0) {
            throw new IllegalArgumentException("Scale factor must be positive.");
        }
        if (index != null && index < 0) {
            throw new IllegalArgumentException("Index must be non-negative.");
        }
        this.pathId = pathId;
        this.index = index;
        this.scale = scale;
    }

    // --- Static Creation Methods ---

    /**
     * Factory method to create a DataPath without index or scale.
     * Used for simple paths like "CollarColor".
     */
    public static DataPath create(DataPathId pathId) {
        return new DataPath(pathId, null, null);
    }

    /**
     * Factory method to create a DataPath with an array index.
     * Used for paths like "Owner[0]".
     * @param pathId The NBT path identifier (e.g., DataPathId.OWNER).
     * @param index The integer index to access in the list (e.g., 0).
     * @return A new DataPath instance.
     */
    public static DataPath createWithIndex(DataPathId pathId, int index) {
        return new DataPath(pathId, index, null);
    }

    /**
     * Builds the complete NBT path string, including the index and scale factor if present.
     * @return The formatted path string for the Minecraft command.
     * Examples: "CollarColor", "Owner[0]", "Pos[0] 1"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(pathId.toString());

        if (index != null) {
            // Append the index part: "[0]"
            sb.append("[").append(index).append("]");
        }

        if (scale != null) {
            // Append the scale part: " 1"
            sb.append(" ").append(scale);
        }

        return sb.toString();
    }
}