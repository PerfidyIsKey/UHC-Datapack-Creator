package uhc.arguments.block;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.block.BlockIdentifier;

/**
 * A concrete implementation of the {@link BlockPredicate} interface that dynamically
 * constructs the predicate string from its constituent parts:
 * Identifier (ID or Tag), optional Block States, and optional NBT (Data Tags).
 * <p>
 * The output format strictly follows the Minecraft predicate syntax:
 * {@code <id_or_tag>[block_states]{data_tags}}.
 */
public class DynamicBlockPredicate implements BlockPredicate {

    // The final, immutable string representation of the block predicate argument.
    private final String predicate;

    /**
     * Private constructor to enforce object creation through the static factory methods.
     *
     * @param predicate The fully constructed block predicate string.
     */
    private DynamicBlockPredicate(String predicate) {
        this.predicate = predicate;
    }

    /**
     * Creates a DynamicBlockPredicate with only a Block Identifier (ID or Tag).
     *
     * @param id The required identifier (ID or Tag, e.g., 'minecraft:stone' or '#minecraft:planks').
     * @return A new DynamicBlockPredicate instance.
     * @throws IllegalArgumentException if the provided {@code id} is null.
     */
    public static DynamicBlockPredicate create(BlockIdentifier id) {
        if (id == null) {
            throw new IllegalArgumentException("BlockIdentifier (ID or Tag) cannot be null when creating a DynamicBlockPredicate.");
        }
        return new DynamicBlockPredicate(id.getResourceLocation()); // Using getIdentifier()
    }

    /**
     * Creates a DynamicBlockPredicate with an Identifier and Block States.
     *
     * @param id The required identifier.
     * @param state The required block states (properties).
     * @return A new DynamicBlockPredicate instance.
     * @throws IllegalArgumentException if either {@code id} or {@code state} is null.
     */
    public static DynamicBlockPredicate create(BlockIdentifier id, BlockState state) {
        if (id == null || state == null) {
            throw new IllegalArgumentException("BlockIdentifier and BlockState cannot be null for this creation method.");
        }
        return new DynamicBlockPredicate(id.getResourceLocation() + state.build());
    }

    /**
     * Creates a DynamicBlockPredicate with an Identifier and NBT Data Tags.
     * The NBT is converted to a JSON string representation for the command.
     *
     * @param id The required identifier.
     * @param nbt The required NBT data to match against (e.g., chest contents).
     * @return A new DynamicBlockPredicate instance.
     * @throws IllegalArgumentException if either {@code id} or {@code nbt} is null.
     */
    public static DynamicBlockPredicate create(BlockIdentifier id, CompoundTag nbt) {
        if (id == null || nbt == null) {
            throw new IllegalArgumentException("BlockIdentifier and CompoundTag (NBT) cannot be null for this creation method.");
        }
        return new DynamicBlockPredicate(id.getResourceLocation() + TagConverter.toJson(nbt));
    }

    /**
     * Creates a DynamicBlockPredicate with the Identifier, Block States, AND NBT Data Tags.
     *
     * @param id The required identifier.
     * @param state The required block states.
     * @param nbt The required NBT data.
     * @return A new DynamicBlockPredicate instance.
     * @throws IllegalArgumentException if {@code id}, {@code state}, or {@code nbt} is null.
     */
    public static DynamicBlockPredicate create(BlockIdentifier id, BlockState state, CompoundTag nbt) {
        if (id == null || state == null || nbt == null) {
            throw new IllegalArgumentException("All arguments (Identifier, BlockState, CompoundTag) must be non-null for this creation method.");
        }
        return new DynamicBlockPredicate(id.getResourceLocation() + state.build() + TagConverter.toJson(nbt));
    }

    /**
     * @return The complete block predicate string, including ID/Tag, states, and NBT.
     */
    @Override
    public String getPredicate() {
        return predicate;
    }

    /**
     * @return The complete block predicate string, suitable for direct insertion into a command.
     */
    @Override
    public String toString() {
        return predicate;
    }
}