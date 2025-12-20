package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.*;
import uhc.resource.BlockId;

import java.util.Objects;

/**
 * 🧱 **Block Entity NBT Builder**
 * <p>
 * Base builder for Block Entities (formerly Tile Entities).
 * These are used for blocks that require additional data beyond simple metadata,
 * such as containers, signs, or spawners.
 * </p>
 */
public class BlockEntityNBT {

    private final CompoundTag root;

    /**
     * Internal constructor for the BlockEntityNBT builder.
     * @param root The root compound tag.
     */
    protected BlockEntityNBT(CompoundTag root) {
        this.root = Objects.requireNonNull(root, "Root compound cannot be null");
    }

    /**
     * Initializes a new Block Entity NBT builder with the required spatial data.
     * @param id The namespaced ID of the block entity (e.g., "minecraft:chest").
     * @param x  The X coordinate in the world.
     * @param y  The Y coordinate in the world.
     * @param z  The Z coordinate in the world.
     * @return A new instance of BlockEntityNBT.
     */
    public static BlockEntityNBT create(BlockId id, int x, int y, int z) {
        return new BlockEntityNBT(CompoundTag.create())
                .id(id)
                .pos(x, y, z);
    }

    /**
     * Returns the underlying CompoundTag structure.
     */
    public CompoundTag build() {
        return root;
    }

    // --- 📍 Core Metadata ---

    /**
     * Sets the Block Entity ID.
     * @param id Namespaced ID (e.g., "minecraft:furnace").
     */
    public BlockEntityNBT id(BlockId id) {
        Objects.requireNonNull(id, "Block Entity ID cannot be null.");
        root.put(new StringTag("id", id.getResourceLocation()));
        return this;
    }

    /**
     * Sets the world coordinates for this block entity.
     */
    public BlockEntityNBT pos(int x, int y, int z) {
        root.put(new IntTag("x", x));
        root.put(new IntTag("y", y));
        root.put(new IntTag("z", z));
        return this;
    }

    /**
     * Defines if the block entity should be considered "invalid" or packed.
     * <p>
     * If true, the block is not immediately placed when a chunk is loaded.
     * </p>
     */
    public BlockEntityNBT keepPacked(boolean keepPacked) {
        root.put(new ByteTag("keepPacked", (byte) (keepPacked ? 1 : 0)));
        return this;
    }

    // --- 🧩 Modern Components ---

    /**
     * Sets the optional data components for the block entity.
     * <p>
     * Used in modern Minecraft versions to store structured data like
     * custom names, container contents, or specialized block state logic.
     * </p>
     * @param components The CompoundTag representing the components map.
     */
    public BlockEntityNBT components(CompoundTag components) {
        if (components == null) {
            root.remove("components");
        } else {
            components.setName("components");
            root.put(components);
        }
        return this;
    }
}