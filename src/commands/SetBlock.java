package commands;

import arguments.Block;
import shared.SetMode;
import arguments.BlockPos;

/**
 * Represents the Minecraft 'setblock' command structure in Java Edition.
 * This command changes a single block at a specified position to a new block type,
 * with optional handling for the existing block (destroy, keep, replace, strict).
 * <p>
 * Command syntax: {@code setblock <pos> <block> [destroy|keep|replace|strict]}
 */
public class SetBlock {
    // The target position of the block to be changed. Must be a valid BlockPos.
    private final BlockPos pos;
    // The new block to be placed, including its ID, block states, and data tags. Must be a valid Block.
    private final Block block;
    // The handling mode for the block placement (defaults to 'replace' if not specified).
    private SetMode mode;

    /**
     * Private constructor to ensure instantiation is only done via the static factory method.
     *
     * @param pos The position of the block.
     * @param block The new block definition.
     */
    private SetBlock(BlockPos pos, Block block) {
        this.pos = pos;
        this.block = block;
    }

    /**
     * Static factory method to create a new SetBlock command instance.
     * This method ensures that the core required arguments are not null.
     *
     * @param pos The required position argument (BlockPos).
     * @param block The required new block argument (Block).
     * @return A new SetBlock instance ready for optional mode configuration.
     * @throws IllegalArgumentException if {@code pos} or {@code block} is null.
     */
    public static SetBlock create(BlockPos pos, Block block) {
        if (pos == null) {
            throw new IllegalArgumentException("The position argument (<pos>) for the setblock command cannot be null.");
        }
        if (block == null) {
            throw new IllegalArgumentException("The block argument (<block>) for the setblock command cannot be null.");
        }
        return new SetBlock(pos, block);
    }

    /**
     * Sets the optional mode for the block placement, which determines how the
     * existing block is handled.
     *
     * @param mode The desired SetMode (e.g., DESTROY, KEEP, REPLACE, STRICT).
     * @return The current SetBlock instance for method chaining.
     */
    public SetBlock mode(SetMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Constructs and returns the final string representation of the 'setblock' command.
     *
     * @return The complete, formatted Minecraft command string.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("setblock ");

        // Append mandatory position and block arguments
        sb.append(pos).append(" ").append(block);

        // Append optional mode argument
        if (mode != null) {
            sb.append(" ").append(mode.toString().toLowerCase()); // Ensure mode is lowercase for command syntax
        }

        return sb.toString();
    }

}