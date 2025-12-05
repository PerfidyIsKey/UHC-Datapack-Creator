package arguments.block;

import arguments.Block;
import nbt.tags.CompoundTag;
import nbt.util.TagConverter;
import shared.BlockId;

/**
 * A concrete implementation of the {@link Block} interface that dynamically
 * constructs the block argument string from its constituent parts:
 * Block ID, optional Block States, and optional NBT (Data Tags).
 * <p>
 * The output format strictly follows the Minecraft command syntax:
 * {@code <block_id>[block_states]{data_tags}}.
 */
public class DynamicBlock implements Block {

    // The final, immutable string representation of the block argument.
    private final String block;

    /**
     * Private constructor to enforce object creation through the static factory methods.
     *
     * @param block The fully constructed block string.
     */
    private DynamicBlock(String block) {
        this.block = block;
    }

    /**
     * Creates a DynamicBlock with only a Block ID (the simplest form).
     *
     * @param id The required unique identifier for the block (e.g., 'minecraft:stone').
     * @return A new DynamicBlock instance.
     * @throws IllegalArgumentException if the provided {@code id} is null.
     */
    public static DynamicBlock create(BlockId id) {
        if (id == null) {
            throw new IllegalArgumentException("BlockId cannot be null when creating a DynamicBlock.");
        }
        return new DynamicBlock(id.getResourceLocation());
    }

    /**
     * Creates a DynamicBlock with a Block ID and Block States (e.g., 'minecraft:oak_log[axis=y]').
     *
     * @param id The required unique identifier for the block.
     * @param state The optional block states (properties) for the block.
     * @return A new DynamicBlock instance.
     * @throws IllegalArgumentException if either {@code id} or {@code state} is null.
     */
    public static DynamicBlock create(BlockId id, BlockState state) {
        if (id == null || state == null) {
            throw new IllegalArgumentException("BlockId and BlockState cannot be null for this creation method.");
        }
        // Assumes BlockId.toString() or similar method is implicitly available via concatenation
        return new DynamicBlock(id.getResourceLocation() + state.build());
    }

    /**
     * Creates a DynamicBlock with a Block ID and NBT Data Tags (e.g., 'minecraft:chest{Items:[...]').
     * The NBT is converted to a JSON string representation for the command.
     *
     * @param id The required unique identifier for the block.
     * @param nbt The required NBT data to attach to the block (e.g., chest contents).
     * @return A new DynamicBlock instance.
     * @throws IllegalArgumentException if either {@code id} or {@code nbt} is null.
     */
    public static DynamicBlock create(BlockId id, CompoundTag nbt) {
        if (id == null || nbt == null) {
            throw new IllegalArgumentException("BlockId and CompoundTag (NBT) cannot be null for this creation method.");
        }
        return new DynamicBlock(id.getResourceLocation() + TagConverter.toJson(nbt));
    }

    /**
     * Creates a DynamicBlock with a Block ID, Block States, AND NBT Data Tags (the most complex form).
     *
     * @param id The required unique identifier for the block.
     * @param state The required block states (properties).
     * @param nbt The required NBT data to attach to the block.
     * @return A new DynamicBlock instance.
     * @throws IllegalArgumentException if {@code id}, {@code state}, or {@code nbt} is null.
     */
    public static DynamicBlock create(BlockId id, BlockState state, CompoundTag nbt) {
        if (id == null || state == null || nbt == null) {
            throw new IllegalArgumentException("All arguments (BlockId, BlockState, CompoundTag) must be non-null for this creation method.");
        }
        return new DynamicBlock(id.getResourceLocation() + state.build() + TagConverter.toJson(nbt));
    }

    /**
     * @return The complete block argument string, including ID, states, and NBT.
     */
    @Override
    public String getBlock() {
        return block;
    }

    /**
     * @return The complete block argument string, suitable for direct insertion into a command.
     */
    @Override
    public String toString() {
        return block;
    }
}