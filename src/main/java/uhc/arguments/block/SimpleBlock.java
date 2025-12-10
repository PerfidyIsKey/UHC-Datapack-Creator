package uhc.arguments.block;

import uhc.resource.BlockId;

/**
 * A concrete, immutable implementation of the {@link Block} interface
 * that represents the simplest form of a block argument: a block ID only.
 * <p>
 * This is suitable for blocks that have no block states or NBT data tags
 * specified in the command (e.g., 'minecraft:stone').
 */
public class SimpleBlock implements Block {

    // The underlying BlockId which provides the resource location string.
    private final BlockId id;

    /**
     * Private constructor to enforce object creation via the static factory method.
     *
     * @param id The immutable identifier of the block.
     */
    private SimpleBlock(BlockId id) {
        this.id = id;
    }

    /**
     * Static factory method to create a new SimpleBlock instance.
     *
     * @param id The required {@link BlockId} for this block argument.
     * @return A new SimpleBlock instance.
     * @throws IllegalArgumentException if the provided {@code id} is null.
     */
    public static SimpleBlock create(BlockId id) {
        if (id == null) {
            throw new IllegalArgumentException("BlockId cannot be null when creating a SimpleBlock.");
        }
        return new SimpleBlock(id);
    }

    /**
     * Returns the block ID as the complete block argument string.
     *
     * @return The fully qualified block ID string (e.g., 'minecraft:dirt').
     */
    @Override
    public String getBlock() {
        return id.getResourceLocation();
    }

    /**
     * Returns the block ID as the complete block argument string, suitable for
     * direct insertion into a command.
     *
     * @return The fully qualified block ID string.
     */
    @Override
    public String toString() {
        return id.getResourceLocation();
    }
}