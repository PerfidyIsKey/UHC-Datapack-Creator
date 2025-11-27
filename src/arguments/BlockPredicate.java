package arguments;

/**
 * Defines the contract for any object representing a Minecraft block predicate.
 * <p>
 * A block predicate is used in commands like {@code /fill} or {@code /clone}
 * and specifies criteria for matching one or more blocks. Unlike a simple block
 * argument, its identifier can be a single block ID or a block tag, and it
 * can optionally include block states and NBT data tags.
 * <p>
 * **Format:** {@code <block_id_or_tag>[block_states]{data_tags}}
 */
public interface BlockPredicate {

    /**
     * Returns the raw string representation of the complete block predicate,
     * including the identifier (ID or Tag), optional block states, and
     * optional NBT data tags.
     * <p>
     * Example outputs:
     * <ul>
     * <li>{@code minecraft:stone}</li>
     * <li>{@code #minecraft:planks}</li>
     * <li>{@code #minecraft:wooden_stairs[facing=north]}</li>
     * <li>{@code minecraft:chest{Items:[{id:"minecraft:diamond"}]}}</li>
     * </ul>
     *
     * @return The fully formatted block predicate string.
     */
    String getPredicate();

    /**
     * Overrides the default {@code toString()} method to simply return the
     * result of {@code getPredicate()}. This ensures that a {@code BlockPredicate} object
     * can be seamlessly inserted into the final command string.
     * * @return The fully formatted block predicate string, identical to {@code getPredicate()}.
     */
    @Override
    String toString();
}