package uhc.command.commands;

import uhc.arguments.block.Block;
import uhc.arguments.block.BlockPos;
import uhc.arguments.block.BlockPredicate;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🧱 **Fill Command Builder**
 * <p>
 * A fluent API for the {@code /fill} command, used to fill a volume between two
 * coordinates with a specific block.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code fill <from> <to> <block> [destroy|hollow|keep|outline|replace] [<filter>]}
 * </p>
 */
public class FillCommand implements MinecraftCommand {
    private final BlockPos from;
    private final BlockPos to;
    private final Block block;

    private FillMode mode;
    private BlockPredicate filter;

    private FillCommand(BlockPos from, BlockPos to, Block block) {
        this.from = Objects.requireNonNull(from, "The 'from' position cannot be null.");
        this.to = Objects.requireNonNull(to, "The 'to' position cannot be null.");
        this.block = Objects.requireNonNull(block, "The 'block' argument cannot be null.");
    }

    /**
     * Initializes a new Fill command builder.
     * @param from The first corner of the cuboid.
     * @param to The second corner of the cuboid.
     * @param block The block to fill the area with.
     * @return A new FillCommand instance.
     */
    public static FillCommand create(BlockPos from, BlockPos to, Block block) {
        return new FillCommand(from, to, block);
    }

    /**
     * Sets the fill behavior mode.
     * @param mode The mode (e.g., HOLLOW, OUTLINE, DESTROY).
     */
    public FillCommand mode(FillMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Sets an optional block filter.
     * <p>Note: Providing a filter automatically triggers the {@code replace} syntax.</p>
     * @param filter The predicate matching blocks that should be replaced.
     */
    public FillCommand filter(BlockPredicate filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * <p>Handles the positional logic for the 'replace' sub-argument.</p>
     * @throws IllegalArgumentException if mode/filter combinations are syntactically invalid.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("fill ");

        // Positional coordinates and the source block
        sb.append(from).append(" ").append(to).append(" ").append(block);

        // Minecraft Fill Command Logic:
        // 1. If a filter exists, it MUST use 'replace' mode.
        // 2. If 'replace' is used without a filter, it replaces all blocks.
        if (filter != null) {
            sb.append(" replace ").append(filter);

            if (mode != null && mode != FillMode.REPLACE) {
                // In vanilla, you cannot combine 'replace <filter>' with 'hollow/outline/destroy'.
                // The filter only works with the 'replace' action.
                throw new IllegalArgumentException(
                        "FillMode '" + mode + "' cannot be combined with a filter. " +
                                "Filters are only compatible with the 'replace' action."
                );
            }
        } else {
            // Standard mode handling without a specific filter
            if (mode != null) {
                sb.append(" ").append(mode);
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines the behavior of the fill operation.
     */
    public enum FillMode {
        /** Default: Replaces all blocks in the region. */
        REPLACE,
        /** Replaces only the outer edge blocks; inside is filled with the block. */
        OUTLINE,
        /** Replaces only the outer edge blocks; inside becomes air. */
        HOLLOW,
        /** Breaks existing blocks (dropping items) before placing new ones. */
        DESTROY,
        /** Only replaces air blocks. */
        KEEP;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}