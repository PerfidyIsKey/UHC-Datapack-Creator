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
 * This class serves as the base for all entity-specific builders (mobs, items, projectiles).
 * </p>
 * <p>
 * <b>Technical Note:</b> Uses recursive generics {@code <T extends EntityNBT<T>>} to ensure
 * that methods called from the base class return the specific subclass type, preserving
 * the fluent chaining API.
 * </p>
 * @param <T> The specific type of the builder (Self-reference)
 */

public abstract class EntityNBT<T extends EntityNBT<T>> {
    private final CompoundTag root;

    /**
     * Protected constructor for subclasses.
     * @param root The root CompoundTag where data will be stored.
     */
    protected EntityNBT(CompoundTag root) {
        this.root = Objects.requireNonNull(root, "Root CompoundTag cannot be null.");
    }

    // --- 🆔 Identity and Generic Data ---

    /** * Sets the namespaced ID of the entity.
     * <p><b>Requirement:</b> This is mandatory for entities inside the {@code Passengers} list.</p>
     * @param entityId Namespaced ID (e.g., "minecraft:zombie").
     * @return {@code (T)} The specific builder instance.
     */
    public T id(String entityId) {
        root.put(new StringTag("id", Objects.requireNonNull(entityId, "Entity ID cannot be null.")));
        return (T) this;
    }

    /** * Sets the 'data' compound.
     * In 1.20.5+, this is frequently used for custom technical data or Marker entity storage.
     */
    public T data(CompoundTag customData) {
        Objects.requireNonNull(customData, "Custom data compound cannot be null.");
        customData.setName("data");
        root.put(customData);
        return (T) this;
    }

    /** * Sets the entity's UUID using the modern Int-Array format.
     * <p>Logic: Splits the 128-bit UUID into four 32-bit signed integers.</p>
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

    // --- 🏃 Movement and Physics ---

    /** * Sets absolute spawn coordinates.
     * <b>Note:</b> Minecraft uses Doubles for high-precision positioning.
     */
    public T pos(double x, double y, double z) {
        ListTag posList = new ListTag("Pos");
        posList.add(new DoubleTag("", x));
        posList.add(new DoubleTag("", y));
        posList.add(new DoubleTag("", z));
        root.put(posList);
        return (T) this;
    }

    /** * Sets velocity vector (meters per tick).
     * <b>Catch:</b> If Motion is set to 0,0,0, the entity will spawn stationary.
     */
    public T motion(double dx, double dy, double dz) {
        ListTag motionList = new ListTag("Motion");
        motionList.add(new DoubleTag("", dx));
        motionList.add(new DoubleTag("", dy));
        motionList.add(new DoubleTag("", dz));
        root.put(motionList);
        return (T) this;
    }

    /** Sets Yaw (horizontal) and Pitch (vertical) rotation in degrees. */
    public T rotation(float yaw, float pitch) {
        ListTag rotList = new ListTag("Rotation");
        rotList.add(new FloatTag("", yaw));
        rotList.add(new FloatTag("", pitch));
        root.put(rotList);
        return (T) this;
    }

    /** Toggles gravity. If true, the entity floats or maintains its Y-velocity. */
    public T noGravity(boolean noGravity) {
        root.put(new ByteTag("NoGravity", (byte) (noGravity ? 1 : 0)));
        return (T) this;
    }

    /** Sets whether the entity is touching the ground. */
    public T onGround(boolean onGround) {
        root.put(new ByteTag("OnGround", (byte) (onGround ? 1 : 0)));
        return (T) this;
    }

    /** * Sets current fall distance.
     * <b>Logic:</b> Clamped to 0 to avoid immediate fall-damage calculation errors on spawn.
     */
    public T fallDistance(double distance) {
        root.put(new DoubleTag("fall_distance", Math.max(0, distance)));
        return (T) this;
    }

    // --- ✨ Status and Visuals ---

    /** Toggles the glowing outline effect (spectral). */
    public T glowing(boolean glowing) {
        root.put(new ByteTag("Glowing", (byte) (glowing ? 1 : 0)));
        return (T) this;
    }

    /** If true, the entity cannot take damage from any source. */
    public T invulnerable(boolean invulnerable) {
        root.put(new ByteTag("Invulnerable", (byte) (invulnerable ? 1 : 0)));
        return (T) this;
    }

    /** If true, the entity produces no sounds (ambient, hurt, or death). */
    public T silent(boolean silent) {
        root.put(new ByteTag("Silent", (byte) (silent ? 1 : 0)));
        return (T) this;
    }

    // --- 🕰️ Environmental Timers ---

    /** Sets air supply ticks (300 is full/15 seconds). */
    public T air(short ticks) {
        root.put(new ShortTag("Air", (short) Math.max(0, ticks)));
        return (T) this;
    }

    /** Sets remaining fire ticks. Use {@code -20} for a non-burning state. */
    public T fire(short ticks) {
        root.put(new ShortTag("Fire", ticks));
        return (T) this;
    }

    // --- 📝 Strings and Logic Tags ---

    /** Sets the name shown above the entity. Stored as a JSON-formatted string. */
    public T customName(TextComponent component) {
        Objects.requireNonNull(component, "TextComponent cannot be null.");
        root.put(new StringTag("CustomName", component.toString()));
        return (T) this;
    }

    /** Toggles the permanent visibility of the custom name. */
    public T customNameVisible(boolean visible) {
        root.put(new ByteTag("CustomNameVisible", (byte) (visible ? 1 : 0)));
        return (T) this;
    }

    /** * Adds scoreboard/technical tags.
     * <b>Catch:</b> Clears existing tags if the provided list is null.
     */
    public T tags(List<String> scoreboardTags) {
        if (scoreboardTags == null) {
            root.remove("Tags");
            return (T) this;
        }
        ListTag tagsList = new ListTag("Tags");
        for (String tag : scoreboardTags) {
            if (tag != null) tagsList.add(new StringTag("", tag));
        }
        root.put(tagsList);
        return (T) this;
    }

    /**
     * Adds one or more entities as passengers riding this entity.
     * <p><b>Catch:</b> Each passenger MUST have its ID set via {@code .id()} to spawn correctly.</p>
     * @param passengers Builders for the riding entities.
     * @return (T) Current builder.
     */
    public T passengers(EntityNBT<?>... passengers) {
        if (passengers == null || passengers.length == 0) {
            root.remove("Passengers");
            return (T) this;
        }

        ListTag passengersList = (ListTag) root.get("Passengers");
        if (passengersList == null) {
            passengersList = new ListTag("Passengers");
        }

        for (EntityNBT<?> passenger : passengers) {
            if (passenger != null) {
                // We build the passenger's NBT and add it to the stack
                passengersList.add(passenger.build());
            }
        }

        root.put(passengersList);
        return (T) this;
    }

    // --- 🚀 Finalization ---

    protected CompoundTag root() {
        return root;
    }

    /** * Builds the final CompoundTag.
     * @return The complete NBT root for the entity.
     */
    public CompoundTag build() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}