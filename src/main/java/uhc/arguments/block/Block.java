package uhc.arguments.block;

import uhc.data.nbt.blockentity.state.BlockState;
import uhc.data.nbt.blockentity.BlockEntityNBT;
import uhc.resource.block.BlockId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🧱 **Block Representation**
 * <p>
 * Combines a BlockId, BlockStates, and BlockEntityNBT into a single
 * string-serializable format compatible with Minecraft commands.
 * </p>
 */
public class Block {
    private final BlockId id;
    private final List<BlockState> states = new ArrayList<>();
    private BlockEntityNBT<?> data;

    private Block(BlockId id) {
        this.id = Objects.requireNonNull(id, "Block ID cannot be null.");
    }

    public static Block create(BlockId id) {
        return new Block(id);
    }

    public Block withState(BlockState state) {
        if (state != null) {
            this.states.add(state);
        }
        return this;
    }

    public Block withData(BlockEntityNBT<?> data) {
        this.data = data;
        return this;
    }

    /**
     * Serializes the block into the format: id[states]{nbt}
     * @return A formatted Minecraft block string.
     */
    public String getAsCommandString() {
        StringBuilder builder = new StringBuilder(id.getResourceLocation());

        // 1. Process Block States: [key=value,key=value]
        if (!states.isEmpty()) {
            String stateString = states.stream()
                    .map(s -> s.getKey() + "=" + s.getValue())
                    .collect(Collectors.joining(","));
            builder.append("[").append(stateString).append("]");
        }

        // 2. Process NBT Data: {key:value}
        if (data != null) {
            // build() returns the CompoundTag; we use its SNBT (Stringified NBT) form
            String nbtString = data.build().toString();
            if (!nbtString.equals("{}")) { // Avoid adding empty brackets
                builder.append(nbtString);
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return getAsCommandString();
    }
}