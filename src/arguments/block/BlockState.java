package arguments.block;

import Enums.Direction;
import shared.StructureBlockMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the optional Block State arguments for a Minecraft block, formatted
 * as a bracketed, comma-separated list of key-value pairs.
 * <p>
 * Format: {@code [key1=value1,key2=value2,etc.]}
 * <p>
 * This class uses a fluent Builder pattern for configuration.
 */
public class BlockState {

    // Internal list to hold the individual state strings (e.g., "mode=load", "facing=north").
    private final List<String> states = new ArrayList<>();

    /**
     * Private constructor to enforce creation via the static factory method.
     */
    private BlockState() {}

    /**
     * Static factory method to begin building a new BlockState configuration.
     *
     * @return A new, empty BlockState instance.
     */
    public static BlockState create() {
        return new BlockState();
    }

    /**
     * Adds the 'mode' block state, typically used for Structure Blocks.
     *
     * @param mode The required structure block mode.
     * @return The current BlockState instance for method chaining.
     * @throws IllegalArgumentException if {@code mode} is null.
     */
    public BlockState mode(StructureBlockMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("StructureBlockMode cannot be null for the 'mode' state.");
        }
        // Assumes StructureBlockMode.getBlockStateValue() returns the correct lowercase string.
        this.states.add("mode=" + mode.getBlockStateValue());
        return this;
    }

    /**
     * Adds the 'facing' block state, used for many directional blocks like chests, pistons, or walls.
     *
     * @param direction The required direction (e.g., NORTH, SOUTH).
     * @return The current BlockState instance for method chaining.
     * @throws IllegalArgumentException if {@code direction} is null.
     */
    public BlockState facing(Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null for the 'facing' state.");
        }
        // Ensure the direction value is consistently lowercase, as required by Minecraft block states.
        this.states.add("facing=" + direction.toString().toLowerCase());
        return this;
    }

    /**
     * Adds the 'waterlogged' block state, applicable to slabs, stairs, and other non-full blocks.
     * The value must be the lowercase string "true" or "false".
     *
     * @param state The boolean state for waterlogging (true or false).
     * @return The current BlockState instance for method chaining.
     * @throws IllegalArgumentException if {@code state} is null.
     */
    public BlockState waterlogged(Boolean state) {
        if (state == null) {
            throw new IllegalArgumentException("Boolean state for 'waterlogged' cannot be null.");
        }
        // Convert Boolean object directly to the required lowercase string "true" or "false".
        this.states.add("waterlogged=" + state.toString());
        return this;
    }

    /**
     * Adds the 'has_record' block state, specific to Jukebox blocks.
     * The value must be the lowercase string "true" or "false".
     *
     * @param state The boolean state indicating if the Jukebox has a record.
     * @return The current BlockState instance for method chaining.
     * @throws IllegalArgumentException if {@code state} is null.
     */
    public BlockState hasRecord(Boolean state) {
        if (state == null) {
            throw new IllegalArgumentException("Boolean state for 'has_record' cannot be null.");
        }
        // Convert Boolean object directly to the required lowercase string "true" or "false".
        this.states.add("has_record=" + state.toString());
        return this;
    }

    // You can continue adding specialized builder methods here (e.g., 'axis', 'half', 'powered', etc.)

    /**
     * Constructs and returns the final formatted Block State string.
     * If no states were added, it returns an empty string.
     *
     * @return The block state string in the format {@code [key=value,...]} or an empty string.
     */
    public String build() {
        if (states.isEmpty()) {
            return "";
        }
        // Format: [arg1=val1,arg2=val2]
        String content = String.join(",", states);
        return "[" + content + "]";
    }
}