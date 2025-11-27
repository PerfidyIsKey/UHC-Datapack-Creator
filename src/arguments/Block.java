package arguments;

/**
 * Defines the contract for any object representing a Minecraft block argument
 * in a command, such as the 'setblock' command.
 * <p>
 * This interface mandates the ability to return a fully qualified block string
 * in the format: {@code block_id[block_states]{data_tags}}.
 * <p>
 * In Java Edition, this format must include the block's required namespace (e.g., {@code minecraft:stone}).
 */
public interface Block {

    /**
     * Returns the raw string representation of the block, including its ID,
     * optional block states, and optional NBT data tags, in the exact format
     * required by the Minecraft command parser.
     * <p>
     * Example outputs:
     * <ul>
     * <li>{@code minecraft:stone}</li>
     * <li>{@code minecraft:oak_log[axis=y]}</li>
     * <li>{@code minecraft:chest{Items:[{id:"minecraft:diamond",Count:1b}]}}</li>
     * </ul>
     *
     * @return The fully formatted block argument string.
     */
    String getBlock();

    /**
     * Overrides the default {@code toString()} method to simply return the
     * result of {@code getBlock()}. This ensures that when a {@code Block} object
     * is concatenated into the command string, it produces the correct command argument.
     * * @return The fully formatted block argument string, identical to {@code getBlock()}.
     */
    @Override
    String toString();
}