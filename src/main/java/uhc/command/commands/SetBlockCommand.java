package uhc.command.commands;

import uhc.arguments.block.Block;
import uhc.arguments.block.BlockPos;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🧱 **SetBlock Command Builder**
 * <p>
 * Provides a fluent API for the {@code /setblock} command.
 * This command changes a single block at a specific coordinate.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /setblock <pos> <block> [destroy|keep|replace]}
 * </p>
 */
public class SetBlockCommand implements MinecraftCommand {
    private final BlockPos pos;
    private final Block block;
    private SetMode mode;

    private SetBlockCommand(BlockPos pos, Block block) {
        // Enforce non-nullability for mandatory command components
        this.pos = Objects.requireNonNull(pos, "Block position cannot be null.");
        this.block = Objects.requireNonNull(block, "Block type cannot be null.");
    }

    /**
     * Initializes a new SetBlock command builder.
     * @param pos The coordinates where the block should be placed.
     * @param block The block definition (ID and optional states/NBT).
     * @return A new SetBlockCommand instance.
     */
    public static SetBlockCommand create(BlockPos pos, Block block) {
        return new SetBlockCommand(pos, block);
    }

    /**
     * Sets the handling mode for the block placement.
     * @param mode The desired mode (e.g., DESTROY, KEEP).
     * @return The current builder instance.
     */
    public SetBlockCommand mode(SetMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return The formatted command (e.g., "setblock ~ ~ ~ air destroy").
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("setblock ");

        // Append mandatory position and block
        sb.append(pos).append(" ").append(block);

        // Append optional mode (Minecraft defaults to 'replace' if omitted)
        if (mode != null) {
            sb.append(" ").append(mode);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines how the existing block at the target location is handled.
     */
    public enum SetMode {
        /** Drops the old block and its contents as items; plays sound. */
        DESTROY,

        /** Only places the block if the current block is air. */
        KEEP,

        /** Default: Replaces the block without dropping items or playing sound. */
        REPLACE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}