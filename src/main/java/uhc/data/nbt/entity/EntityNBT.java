package uhc.data.nbt.entity;

import uhc.data.nbt.tags.*;
import uhc.text.TextComponent;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

/**
 * 🧬 **Generic Entity NBT Builder**
 * <p>
 * A fluent builder for generic Minecraft entity data.
 * Uses recursive generics to allow fluent method chaining across the inheritance hierarchy.
 * </p>
 * @param <T> The specific type of the builder (Self-reference)
 */
@SuppressWarnings("unchecked")
public abstract class EntityNBT<T extends EntityNBT<T>> {
    private final CompoundTag root;

    protected EntityNBT(CompoundTag root) {
        this.root = Objects.requireNonNull(root, "Root CompoundTag cannot be null.");
    }

    // --- Identity and Generic Data ---

    /** Sets the string ID of the entity (e.g., "minecraft:creeper"). */
    public T id(String entityId) {
        root.put(new StringTag("id", Objects.requireNonNull(entityId)));
        return (T) this;
    }

    /** Sets the 'data' compound for custom component data (common in 1.20.5+). */
    public T data(CompoundTag customData) {
        Objects.requireNonNull(customData, "Custom data compound cannot be null.");
        customData.setName("data");
        root.put(customData);
        return (T) this;
    }

    /** * Sets the entity's UUID using the four-int-array format.
     * Logic converts a standard UUID into the NBT-required int[4] array.
     */
    public T uuid(UUID id) {
        Objects.requireNonNull(id, "UUID cannot be null.");
        long most = id.getMostSignificantBits();
        long least = id.getLeastSignificantBits();
        int[] uuidArray = new int[]{
                (int) (most >> 32), (int) most,
                (int) (least >> 32), (int) least
        };
        root.put(new IntArrayTag("UUID", uuidArray));
        return (T) this;
    }

    // --- Movement and Physics ---

    /** Sets the absolute X, Y, and Z coordinates. */
    public T pos(double x, double y, double z) {
        ListTag posList = new ListTag("Pos");
        // Internal elements of a ListTag should have empty names in your implementation
        posList.add(new DoubleTag("", x));
        posList.add(new DoubleTag("", y));
        posList.add(new DoubleTag("", z));
        root.put(posList);
        return (T) this;
    }

    /** Sets velocity in meters per tick. Range should generally be [-10.0, 10.0]. */
    public T motion(double dx, double dy, double dz) {
        ListTag motionList = new ListTag("Motion");
        motionList.add(new DoubleTag("", dx));
        motionList.add(new DoubleTag("", dy));
        motionList.add(new DoubleTag("", dz));
        root.put(motionList);
        return (T) this;
    }

    /** Sets Yaw (rotation around Y) and Pitch (up/down) in degrees. */
    public T rotation(float yaw, float pitch) {
        ListTag rotList = new ListTag("Rotation");
        rotList.add(new FloatTag("", yaw));
        rotList.add(new FloatTag("", pitch));
        root.put(rotList);
        return (T) this;
    }

    /** If true, the entity ignores gravity and does not fall. */
    public T noGravity(boolean noGravity) {
        root.put(new ByteTag("NoGravity", (byte) (noGravity ? 1 : 0)));
        return (T) this;
    }

    /** Indicates if the entity is currently colliding with the floor. */
    public T onGround(boolean onGround) {
        root.put(new ByteTag("OnGround", (byte) (onGround ? 1 : 0)));
        return (T) this;
    }

    /** Sets the distance fallen; clamped to 0 to prevent NBT logic errors. */
    public T fallDistance(double distance) {
        root.put(new DoubleTag("fall_distance", Math.max(0, distance)));
        return (T) this;
    }

    // --- Status and Visuals ---

    public T glowing(boolean glowing) {
        root.put(new ByteTag("Glowing", (byte) (glowing ? 1 : 0)));
        return (T) this;
    }

    public T invulnerable(boolean invulnerable) {
        root.put(new ByteTag("Invulnerable", (byte) (invulnerable ? 1 : 0)));
        return (T) this;
    }

    public T silent(boolean silent) {
        root.put(new ByteTag("Silent", (byte) (silent ? 1 : 0)));
        return (T) this;
    }

    // --- Environmental Timers ---

    /** Remaining air ticks (300 is full). Clamped to non-negative. */
    public T air(short ticks) {
        root.put(new ShortTag("Air", (short) Math.max(0, ticks)));
        return (T) this;
    }

    /** Ticks until fire goes out. Use -20 for "not burning". */
    public T fire(short ticks) {
        root.put(new ShortTag("Fire", ticks));
        return (T) this;
    }

    // --- Strings and Tags ---

    /** Sets display name using {@link TextComponent} (stored as JSON). */
    public T customName(TextComponent component) {
        Objects.requireNonNull(component, "TextComponent cannot be null.");
        root.put(new StringTag("CustomName", component.toString()));
        return (T) this;
    }

    public T customNameVisible(boolean visible) {
        root.put(new ByteTag("CustomNameVisible", (byte) (visible ? 1 : 0)));
        return (T) this;
    }

    /** Adds scoreboard tags for technical logic. */
    public T tags(List<String> scoreboardTags) {
        ListTag tagsList = new ListTag("Tags");
        for (String tag : scoreboardTags) {
            tagsList.add(new StringTag("", tag));
        }
        root.put(tagsList);
        return (T) this;
    }

    // --- Finalization ---

    /** Provides access to the underlying root tag for subclasses. */
    protected CompoundTag root() {
        return root;
    }

    public CompoundTag build() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}