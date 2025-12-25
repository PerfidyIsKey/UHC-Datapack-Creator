package uhc.arguments.block;

import uhc.data.nbt.blockentity.state.BlockState;
import uhc.data.nbt.blockentity.BlockEntityNBT;
import uhc.resource.block.BlockResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🔍 **Block Predicate Generator**
 * <p>
 * Generates a Minecraft block predicate string used for filtering or checking blocks
 * in the world. Supports specific blocks, block tags, state properties, and NBT data.
 * </p>
 * <p><b>Format:</b> {@code <block_id_or_tag>[block_states]{data_tags}}</p>
 */
public class BlockPredicate {

    // --- ⚙️ Private Fields ---

    /** The target block or tag (e.g., "minecraft:stone" or "#minecraft:logs"). */
    private final BlockResource resource;

    /** A collection of states to match against (e.g., [lit=true]). */
    private final List<BlockState> states = new ArrayList<>();

    /** A collection of NBT requirements to match against. */
    private final List<BlockEntityNBT<?>> nbtRequirements = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce factory usage.
     * @param resource The {@link BlockResource} identifying the block or tag.
     */
    private BlockPredicate(BlockResource resource) {
        this.resource = Objects.requireNonNull(resource, "BlockResource cannot be null.");
    }

    /**
     * Creates a new predicate builder for a specific block or tag.
     * @param resource The {@link BlockResource} (BlockIdentifier or BlockTagId).
     * @return A new BlockPredicate instance.
     * @throws NullPointerException if the resource is null.
     */
    public static BlockPredicate create(BlockResource resource) {
        return new BlockPredicate(resource);
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Adds a state requirement to the predicate.
     * <p><b>Error Catching:</b> Filters null state objects to prevent
     * malformed bracket syntax {@code [null=null]}.</p>
     * @param state The {@link BlockState} property to match.
     * @return This builder instance.
     */
    public BlockPredicate withState(BlockState state) {
        if (state != null) {
            this.states.add(state);
        }
        return this;
    }

    /**
     * Adds an NBT data requirement to the predicate.
     * <p><b>Logic:</b> Multiple NBT builders will have their tags merged
     * into a single requirement string during serialization.</p>
     * @param nbt The {@link BlockEntityNBT} requirement.
     * @return This builder instance.
     */
    public BlockPredicate withData(BlockEntityNBT<?> nbt) {
        if (nbt != null) {
            this.nbtRequirements.add(nbt);
        }
        return this;
    }

    // --- 🛰️ Serialization Logic ---

    /**
     * Serializes the predicate into a format compatible with Minecraft commands.
     * <p><b>Error Catching:</b> Validates the base resource and wraps the stream
     * processing in a try-catch to handle potential NBT build failures.</p>
     * @return A formatted predicate string (e.g., {@code #logs[axis=y]{...}}).
     * @throws IllegalStateException if the internal state or NBT cannot be serialized.
     */
    public String getAsPredicateString() {
        // Ensure the base resource (block or tag) is valid
        resource.validate();

        StringBuilder builder = new StringBuilder(resource.getResourceLocation());

        // 1. Process Block States: [key=value,key=value]
        if (!states.isEmpty()) {
            try {
                String stateString = states.stream()
                        .map(s -> s.getKey() + "=" + s.getValue())
                        .collect(Collectors.joining(","));

                if (!stateString.isEmpty()) {
                    builder.append("[").append(stateString).append("]");
                }
            } catch (Exception e) {
                throw new IllegalStateException("Critical error formatting BlockStates for predicate: " + resource, e);
            }
        }

        // 2. Process Data Tags: {key:value}
        if (!nbtRequirements.isEmpty()) {
            try {
                // Combine all NBT builders into one CompoundTag for the predicate
                uhc.data.nbt.tags.CompoundTag mergedNbt = uhc.data.nbt.tags.CompoundTag.create();
                for (BlockEntityNBT<?> nbtBuilder : nbtRequirements) {
                    mergedNbt.merge(nbtBuilder.build());
                }

                String nbtString = mergedNbt.toString();
                // Predicates usually require non-empty NBT to be valid
                if (!nbtString.equals("{}")) {
                    builder.append(nbtString);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Critical error formatting NBT for predicate: " + resource, e);
            }
        }

        return builder.toString();
    }

    /**
     * Standard string conversion for use in command execution.
     * @return The result of {@link #getAsPredicateString()}.
     */
    @Override
    public String toString() {
        return getAsPredicateString();
    }
}