package commands;

import uhc.arguments.block.Block;
import uhc.arguments.block.BlockPos;
import uhc.arguments.block.BlockPredicate;
import uhc.command.util.FillMode;

/**
 * Represents the Minecraft 'fill' command structure.
 * This command fills a volume defined by two positions with a specified block.
 * <p>
 * Command syntax: {@code fill <from> <to> <block> [mode|replace [filter] [mode]]}
 */
public class Fill {
    // The first corner of the cuboid volume. Required.
    private final BlockPos from;
    // The second corner of the cuboid volume. Required.
    private final BlockPos to;
    // The block to be placed in the volume. Required.
    private final Block block;

    // Optional mode argument (e.g., outline, hollow, destroy, strict, keep).
    private FillMode mode;

    // Optional filter, only used when the primary action is 'replace <filter>'.
    private BlockPredicate filter;

    /**
     * Private constructor to ensure instantiation is only done via the static factory method.
     *
     * @param from The starting position.
     * @param to The ending position.
     * @param block The block to place.
     */
    private Fill(BlockPos from, BlockPos to, Block block) {
        this.from = from;
        this.to = to;
        this.block = block;
    }

    /**
     * Static factory method to create a new Fill command instance.
     * Ensures all mandatory coordinate and block arguments are non-null.
     *
     * @param from The required starting position (BlockPos).
     * @param to The required ending position (BlockPos).
     * @param block The required block argument (Block).
     * @return A new Fill instance ready for optional mode/filter configuration.
     * @throws IllegalArgumentException if {@code from}, {@code to}, or {@code block} is null.
     */
    public static Fill create(BlockPos from, BlockPos to, Block block) {
        if (from == null) {
            throw new IllegalArgumentException("The 'from' position for the fill command cannot be null.");
        }
        if (to == null) {
            throw new IllegalArgumentException("The 'to' position for the fill command cannot be null.");
        }
        if (block == null) {
            throw new IllegalArgumentException("The 'block' argument for the fill command cannot be null.");
        }
        return new Fill(from, to, block);
    }

    /**
     * Sets the optional {@link FillMode} for the command (e.g., outline, hollow, destroy, keep).
     *
     * @param mode The desired FillMode.
     * @return The current Fill instance for method chaining.
     */
    public Fill mode(FillMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Sets the optional {@link BlockPredicate} filter.
     * Setting a filter implies the command uses the {@code replace <filter>} syntax.
     *
     * @param filter The block predicate used to filter which blocks are replaced.
     * @return The current Fill instance for method chaining.
     */
    public Fill filter(BlockPredicate filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Constructs and returns the final string representation of the 'fill' command.
     *
     * @return The complete, formatted Minecraft command string.
     * @throws IllegalArgumentException if an invalid combination of mode and filter is used.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("fill ");

        // Append mandatory arguments
        sb.append(from).append(" ").append(to).append(" ").append(block);

        // --- Logic for Optional Arguments ---

        if (filter != null) {
            // If a filter is present, the syntax must be: ... <block> replace <filter> [mode]
            sb.append(" replace ").append(filter);

            if (mode != null) {
                // When using 'replace <filter>', the mode must be one of: [outline|hollow|destroy|strict].
                // 'KEEP' and 'REPLACE' (no filter) are redundant or invalid here.
                if (mode == FillMode.REPLACE || mode == FillMode.KEEP) {
                    throw new IllegalArgumentException(
                            "Invalid FillMode (" + mode.toString().toLowerCase() +
                                    ") when a filter is specified. Valid modes are: outline, hollow, destroy, or strict."
                    );
                }

                // Append the valid sub-mode
                sb.append(" ").append(mode.toString().toLowerCase());

            }
        } else {
            // If no filter is present, the syntax is: ... <block> [mode]
            if (mode != null) {
                // Append the mode (can be any, including KEEP, REPLACE, etc.)
                sb.append(" ").append(mode.toString().toLowerCase());
            }
        }

        return sb.toString();
    }
}