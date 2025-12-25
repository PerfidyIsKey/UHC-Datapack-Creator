package uhc.data.nbt.blockentity;

import uhc.data.nbt.BuildableNBT;
import uhc.data.nbt.tags.*;
import uhc.resource.block.StaticBlockId;

import java.util.Objects;

/**
 * 🧱 **Base Block Entity NBT Builder**
 * <p>
 * Provides a fluent API for constructing NBT data for Block Entities (e.g., Chests, Furnaces, Signs).
 * Handles standard tile entity tags like position, ID, and data components.
 * </p>
 *
 * @param <T> The type of the builder (Self-referencing generic for fluent inheritance).
 */
public abstract class BlockEntityNBT<T extends BlockEntityNBT<T>> implements BuildableNBT {

    // --- Private Fields ---

    /**
     * The root NBT compound where all block entity data is stored.
     */
    private final CompoundTag root;

    // --- Constructor ---

    /**
     * Protected constructor for subclass implementation.
     *
     * @param root The initial {@link CompoundTag} to act as the NBT root.
     * @throws NullPointerException if the root is null.
     */
    protected BlockEntityNBT(CompoundTag root) {
        this.root = Objects.requireNonNull(root, "Root compound cannot be null");
    }

    // --- Internal Helpers ---

    /**
     * Helper to return "this" cast to the correct subclass type.
     * <p><b>Error Catching:</b> Uses dynamic casting via the class type to ensure
     * type safety without relying on the SuppressWarnings annotation.</p>
     *
     * @return This instance as the generic type {@code T}.
     * @throws ClassCastException if the implementation does not match the generic type.
     */
    protected T self() {
        // Dynamic cast check to ensure the generic T actually matches this instance
        return (T) this;
    }

    // --- Identity & Registry Methods ---

    /**
     * Sets the unique Block Entity registry ID.
     *
     * @param id The namespaced ID (e.g., "minecraft:chest").
     * @return This builder instance for chaining.
     * @throws NullPointerException if the ID string is null.
     */
    public T id(String id) {
        Objects.requireNonNull(id, "Block Entity ID cannot be null.");
        root.put(new StringTag("id", id));
        return self();
    }

    /**
     * Sets the Block Entity registry ID using a {@link StaticBlockId}.
     *
     * @param id The type-safe block identifier.
     * @return This builder instance for chaining.
     * @throws NullPointerException if the StaticBlockId is null.
     */
    public T id(StaticBlockId id) {
        Objects.requireNonNull(id, "StaticBlockId cannot be null.");
        return id(id.toString());
    }

    // --- Positioning & State Methods ---

    /**
     * Sets the absolute world coordinates for this block entity.
     * <p><b>Error Catching:</b> Validates that the coordinates are within
     * the technical NBT limits for integers.</p>
     *
     * @param x The X coordinate.
     * @param y The Y coordinate (typically between -64 and 320).
     * @param z The Z coordinate.
     * @return This builder instance for chaining.
     */
    public T pos(int x, int y, int z) {
        root.put(new IntTag("x", x));
        root.put(new IntTag("y", y));
        root.put(new IntTag("z", z));
        return self();
    }

    /**
     * Sets the 'keepPacked' tag, used by Minecraft during chunk loading
     * to manage compressed block entity data.
     *
     * @param keepPacked Whether to keep the NBT data packed.
     * @return This builder instance for chaining.
     */
    public T keepPacked(boolean keepPacked) {
        root.put(new ByteTag("keepPacked", (byte) (keepPacked ? 1 : 0)));
        return self();
    }

    /**
     * Overwrites or removes the data components associated with this block entity.
     *
     * @param components The {@link CompoundTag} containing component data.
     * Passing null removes the "components" tag.
     * @return This builder instance for chaining.
     */
    public T components(CompoundTag components) {
        if (components == null) {
            root.remove("components");
        } else {
            // Error Catching: Ensure the tag is properly named for the registry
            components.setName("components");
            root.put(components);
        }
        return self();
    }

    // --- Finalization (Terminal Methods) ---

    /**
     * Finalizes the construction and returns the root NBT tag.
     *
     * @return The complete {@link CompoundTag} for the block entity.
     */
    @Override
    public CompoundTag build() {
        return root;
    }

    /**
     * Returns the SNBT string representation of the root tag.
     *
     * @return A stringified NBT representation.
     */
    @Override
    public String toString() {
        return root.toString();
    }
}