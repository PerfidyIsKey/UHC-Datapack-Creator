package uhc.arguments.block;

import uhc.data.nbt.blockentity.state.BlockState;
import uhc.data.nbt.blockentity.BlockEntityNBT;
import uhc.data.nbt.util.TagConverter;
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

    /** * The unique identifier for the block (e.g., {@code "minecraft:chest"}).
     * This represents the base block type and must not be a tag (starting with #).
     */
    private final BlockIdentifier id;

    /** * A list of block properties, such as {@code 'facing=north'} or {@code 'lit=true'}.
     * These define the specific variant or orientation of the block.
     */
    private final List<BlockState> states = new ArrayList<>();

    /** * Optional NBT data for block entities (e.g., inventory contents, structure block settings).
     * This is applied as the data component of the block placement.
     */
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
     * @param id A specific {@link BlockIdentifier} (e.g., {@code BlockId.CHEST}).
     * @return A new Block builder instance.
     * @throws NullPointerException if the id is null.
     */
    public static Block create(BlockIdentifier id) {
        return new Block(id);
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Adds a specific state/property to the block.
     * <p><b>Error Catching:</b> Proactively filters null states to prevent
     * null pointer exceptions during the serialization stream.</p>
     * @param state The {@link BlockState} property to apply.
     * @return This Block instance for fluent chaining.
     */
    public Block withState(BlockState state) {
        if (state != null) {
            this.states.add(state);
        }
        return this;
    }

    /**
     * Attaches Block Entity (NBT) data to this block.
     * @param data The {@link BlockEntityNBT} structure containing the data.
     * @return This Block instance for fluent chaining.
     */
    public Block withData(BlockEntityNBT<?> data) {
        this.data = data;
        return this;
    }

    // --- 🛰️ Serialization Logic ---

    /**
     * Serializes the complete block state into a Minecraft-ready command string.
     * <p><b>Logic:</b>
     * 1. Validates the {@link BlockIdentifier}.<br>
     * 2. Processes {@link BlockState}s into the {@code [key=value]} bracket format.<br>
     * 3. Utilizes {@link TagConverter} to transform the NBT object into SNBT (Stringified NBT).
     * </p>
     * <p><b>Error Catching:</b> Handles potential serialization failures from the
     * NBT builder or state streams, throwing informative exceptions if data is malformed.</p>
     * @return A formatted Minecraft block string (e.g., {@code minecraft:stone[lit=true]{...}}).
     * @throws IllegalStateException if ID validation fails or NBT serialization crashes.
     */
    public String getAsCommandString() {
        // 1. Ensure the ID is valid (no nulls or illegal characters)
        try {
            id.validate();
        } catch (Exception e) {
            throw new IllegalStateException("Block ID validation failed for identifier: " + id, e);
        }

        StringBuilder builder = new StringBuilder(id.getResourceLocation());

        // 2. Process Block States: [key=value,key=value]
        if (!states.isEmpty()) {
            String stateString = states.stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.getKey() + "=" + s.getValue())
                    .collect(Collectors.joining(","));

            if (!stateString.isEmpty()) {
                builder.append("[").append(stateString).append("]");
            }
        }

        // 3. Process NBT Data: {key:value}
        if (data != null) {
            try {
                // CATCHING ERROR: data.build().toString() would return a memory reference.
                // We MUST use TagConverter.toJson to get actual SNBT for the command.
                String nbtString = TagConverter.toJson(data.build());

                // Guard against adding empty braces which can cause syntax errors in certain commands
                if (nbtString != null && !nbtString.equals("{}") && !nbtString.isEmpty()) {
                    builder.append(nbtString);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Critical failure serializing NBT data for block: " + id.getResourceLocation(), e);
            }
        }

        return builder.toString();
    }

    /**
     * Alias for {@link #getAsCommandString()} to support standard Java string concatenation.
     * @return The serialized block string.
     */
    @Override
    public String toString() {
        return getAsCommandString();
    }
}