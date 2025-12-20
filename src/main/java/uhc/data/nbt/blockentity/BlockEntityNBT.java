package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.*;
import java.util.Objects;

/**
 * @param <T> The type of the builder (Self-referencing generic)
 */
public abstract class BlockEntityNBT<T extends BlockEntityNBT<T>> {

    private final CompoundTag root;

    protected BlockEntityNBT(CompoundTag root) {
        this.root = Objects.requireNonNull(root, "Root compound cannot be null");
    }

    /**
     * Helper to return "this" cast to the correct subclass type.
     */
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public CompoundTag build() {
        return root;
    }

    public T id(String id) {
        root.put(new StringTag("id", id));
        return self();
    }

    // Support your StaticBlockId if applicable
    public T id(uhc.resource.StaticBlockId id) {
        return id(id.toString());
    }

    public T pos(int x, int y, int z) {
        root.put(new IntTag("x", x));
        root.put(new IntTag("y", y));
        root.put(new IntTag("z", z));
        return self();
    }

    public T keepPacked(boolean keepPacked) {
        root.put(new ByteTag("keepPacked", (byte) (keepPacked ? 1 : 0)));
        return self();
    }

    public T components(CompoundTag components) {
        if (components == null) {
            root.remove("components");
        } else {
            components.setName("components");
            root.put(components);
        }
        return self();
    }
}