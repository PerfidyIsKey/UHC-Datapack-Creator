package uhc.arguments.block;

import uhc.data.nbt.blockentity.state.BlockState;
import uhc.data.nbt.blockentity.BlockEntityNBT;
import uhc.resource.block.BlockIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🧱 **Block Representation**
 * <p>
 * This class serves as the final, string-serializable container for a Minecraft block.
 * It combines a specific {@link BlockIdentifier} with optional {@link BlockState}s
 * and {@link BlockEntityNBT} data to create a format compatible with commands
 * like {@code /setblock} or {@code /fill}.
 * </p>
 * <p><b>Format:</b> {@code namespace:path[states]{nbt}}</p>
 */
public class Block {

    // --- ⚙️ Private Fields ---

    /** The unique identifier for the block (e.g., "minecraft:chest"). Must not be a tag. */
    private final BlockIdentifier id;

    /** A list of block properties, such as 'facing=north' or 'lit=true'. */
    private final List<BlockState> states = new ArrayList<>();

    /** Optional NBT data for block entities (e.g., inventory contents, sign text). */
    private BlockEntityNBT<?> data;

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce the use of the static factory method.
     * @param id The validated block identifier.
     */
    private Block(BlockIdentifier id) {
        this.id = Objects.requireNonNull(id, "Block ID cannot be null.");
    }

    /**
     * Entry point for creating a new Block representation.
     * @param id A specific {@link BlockIdentifier} (e.g., BlockId.CHEST).
     * @return A new Block builder instance.
     * @throws NullPointerException if the id is null.
     */
    public static Block create(BlockIdentifier id) {
        return new Block(id);
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Adds a specific state/property to the block.
     * <p><b>Error Catching:</b> Silently ignores null states to prevent
     * serialization crashes during stream processing.</p>
     * @param state The {@link BlockState} to apply.
     * @return This Block instance for chaining.
     */
    public Block withState(BlockState state) {
        if (state != null) {
            this.states.add(state);
        }
        return this;
    }

    /**
     * Attaches NBT data (Block Entity data) to this block.
     * @param data The {@link BlockEntityNBT} object containing the data structure.
     * @return This Block instance for chaining.
     */
    public Block withData(BlockEntityNBT<?> data) {
        this.data = data;
        return this;
    }

    // --- 🛰️ Serialization Logic ---

    /**
     * Serializes the complete block state into a Minecraft-ready command string.
     * <p><b>Process:</b>
     * 1. Appends the resource location.<br>
     * 2. Formats block states into bracketed {@code [key=value]} syntax.<br>
     * 3. Appends Stringified NBT (SNBT) if data is present and non-empty.
     * </p>
     * @return A formatted Minecraft block string (e.g., {@code minecraft:chest[facing=north]{Items:[...]}}).
     * @throws IllegalStateException if the underlying ID validation fails.
     */
    public String getAsCommandString() {
        // Ensure the ID is still valid before building
        id.validate();

        StringBuilder builder = new StringBuilder(id.getResourceLocation());

        // 1. Process Block States: [key=value,key=value]
        if (!states.isEmpty()) {
            try {
                String stateString = states.stream()
                        .filter(Objects::nonNull) // Defensive check
                        .map(s -> s.getKey() + "=" + s.getValue())
                        .collect(Collectors.joining(","));

                if (!stateString.isEmpty()) {
                    builder.append("[").append(stateString).append("]");
                }
            } catch (Exception e) {
                throw new IllegalStateException("Failed to serialize block states for: " + id, e);
            }
        }

        // 2. Process NBT Data: {key:value}
        if (data != null) {
            try {
                // build() returns the CompoundTag; we use its SNBT (Stringified NBT) form
                String nbtString = data.build().toString();
                // Avoid adding empty brackets which can cause command syntax errors in some versions
                if (nbtString != null && !nbtString.equals("{}") && !nbtString.isEmpty()) {
                    builder.append(nbtString);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Failed to serialize NBT data for: " + id, e);
            }
        }

        return builder.toString();
    }

    /**
     * Alias for {@link #getAsCommandString()} to support standard Java string behavior.
     * @return The serialized block string.
     */
    @Override
    public String toString() {
        return getAsCommandString();
    }
}