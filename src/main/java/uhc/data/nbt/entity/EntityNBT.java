package uhc.data.nbt.entity;

import uhc.data.nbt.BuildableNBT;
import uhc.data.nbt.tags.*;
import uhc.text.TextComponent;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

/**
 * 🧬 **Generic Entity NBT Builder**
 * <p>
 * A fluent builder for generic Minecraft entity data (1.20.5+).
 * This class serves as the base for all entity-specific builders (mobs, items, projectiles).
 * </p>
 *
 * @param <T> The specific type of the builder (Self-reference for fluent inheritance).
 */
public abstract class EntityNBT<T extends EntityNBT<T>> implements BuildableNBT {

    // --- Private Fields ---

    /** * The root NBT compound where all entity attributes are stored.
     */
    private final CompoundTag root;

    // --- Constructor ---

    /**
     * Protected constructor for subclasses.
     * @param root The root {@link CompoundTag} where data will be stored.
     * @throws NullPointerException if root is null.
     */
    protected EntityNBT(CompoundTag root) {
        this.root = Objects.requireNonNull(root, "Root CompoundTag cannot be null.");
    }

    // --- Internal Helpers ---

    /**
     * Casts 'this' to the generic type T without using SuppressWarnings.
     * @return This instance cast to the subclass type.
     */
    protected T self() {
        return (T) this;
    }

    // --- 🆔 Identity and Generic Data ---

    /** * Sets the namespaced ID of the entity.
     * <p><b>Requirement:</b> Mandatory for entities inside the {@code Passengers} list.</p>
     * @param entityId Namespaced ID (e.g., "minecraft:zombie").
     * @return The specific builder instance.
     * @throws NullPointerException if entityId is null.
     */
    public T id(String entityId) {
        Objects.requireNonNull(entityId, "Entity ID cannot be null.");
        root.put(new StringTag("id", entityId));
        return self();
    }

    /** * Sets the 'data' compound for custom technical data.
     * @param customData The compound to store.
     * @return The specific builder instance.
     * @throws NullPointerException if customData is null.
     */
    public T data(CompoundTag customData) {
        Objects.requireNonNull(customData, "Custom data compound cannot be null.");
        customData.setName("data");
        root.put(customData);
        return self();
    }

    /** * Sets the entity's UUID using the Int-Array format.
     * @param id The {@link UUID} to assign.
     * @return The specific builder instance.
     * @throws NullPointerException if id is null.
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
        return self();
    }

    // --- 🏃 Movement and Physics ---

    /** * Sets absolute spawn coordinates.
     * @param x X-coordinate.
     * @param y Y-coordinate.
     * @param z Z-coordinate.
     * @return The specific builder instance.
     */
    public T pos(double x, double y, double z) {
        ListTag posList = new ListTag("Pos");
        posList.add(new DoubleTag("", x));
        posList.add(new DoubleTag("", y));
        posList.add(new DoubleTag("", z));
        root.put(posList);
        return self();
    }

    /** * Sets velocity vector (meters per tick).
     * @param dx Velocity X.
     * @param dy Velocity Y.
     * @param dz Velocity Z.
     * @return The specific builder instance.
     */
    public T motion(double dx, double dy, double dz) {
        ListTag motionList = new ListTag("Motion");
        motionList.add(new DoubleTag("", dx));
        motionList.add(new DoubleTag("", dy));
        motionList.add(new DoubleTag("", dz));
        root.put(motionList);
        return self();
    }

    /** * Sets horizontal (Yaw) and vertical (Pitch) rotation in degrees.
     * @param yaw Horizontal rotation.
     * @param pitch Vertical rotation.
     * @return The specific builder instance.
     */
    public T rotation(float yaw, float pitch) {
        ListTag rotList = new ListTag("Rotation");
        rotList.add(new FloatTag("", yaw));
        rotList.add(new FloatTag("", pitch));
        root.put(rotList);
        return self();
    }

    /** * Toggles gravity for the entity.
     * @param noGravity If true, gravity is disabled.
     * @return The specific builder instance.
     */
    public T noGravity(boolean noGravity) {
        root.put(new ByteTag("NoGravity", (byte) (noGravity ? 1 : 0)));
        return self();
    }

    /** * Sets whether the entity is touching the ground.
     * @param onGround True if touching ground.
     * @return The specific builder instance.
     */
    public T onGround(boolean onGround) {
        root.put(new ByteTag("OnGround", (byte) (onGround ? 1 : 0)));
        return self();
    }

    /** * Sets current fall distance. Clamped to a minimum of 0.
     * @param distance Fall distance in blocks.
     * @return The specific builder instance.
     */
    public T fallDistance(double distance) {
        root.put(new DoubleTag("fall_distance", Math.max(0, distance)));
        return self();
    }

    // --- ✨ Status and Visuals ---

    /** * Toggles the glowing outline effect.
     * @param glowing True if entity should glow.
     * @return The specific builder instance.
     */
    public T glowing(boolean glowing) {
        root.put(new ByteTag("Glowing", (byte) (glowing ? 1 : 0)));
        return self();
    }

    /** * Sets whether the entity can take damage.
     * @param invulnerable True for invulnerability.
     * @return The specific builder instance.
     */
    public T invulnerable(boolean invulnerable) {
        root.put(new ByteTag("Invulnerable", (byte) (invulnerable ? 1 : 0)));
        return self();
    }

    /** * Toggles entity sounds.
     * @param silent True if entity should be silent.
     * @return The specific builder instance.
     */
    public T silent(boolean silent) {
        root.put(new ByteTag("Silent", (byte) (silent ? 1 : 0)));
        return self();
    }

    // --- 🕰️ Environmental Timers ---

    /** * Sets air supply (300 is full). Clamped at 0.
     * @param ticks Remaining air in ticks.
     * @return The specific builder instance.
     */
    public T air(short ticks) {
        root.put(new ShortTag("Air", (short) Math.max(0, ticks)));
        return self();
    }

    /** * Sets remaining fire ticks (-20 for none).
     * @param ticks Fire ticks.
     * @return The specific builder instance.
     */
    public T fire(short ticks) {
        root.put(new ShortTag("Fire", ticks));
        return self();
    }

    // --- 📝 Strings and Custom Names ---

    /** * Sets the custom name shown above the entity.
     * @param component The {@link TextComponent} name.
     * @return The specific builder instance.
     * @throws NullPointerException if component is null.
     */
    public T customName(TextComponent component) {
        Objects.requireNonNull(component, "TextComponent cannot be null.");
        root.put(new StringTag("CustomName", component.toString()));
        return self();
    }

    /** * Toggles permanent visibility of the custom name.
     * @param visible True to always show name.
     * @return The specific builder instance.
     */
    public T customNameVisible(boolean visible) {
        root.put(new ByteTag("CustomNameVisible", (byte) (visible ? 1 : 0)));
        return self();
    }

    /** * Sets scoreboard tags. Passing null removes existing tags.
     * @param scoreboardTags List of tag strings.
     * @return The specific builder instance.
     */
    public T tags(List<String> scoreboardTags) {
        if (scoreboardTags == null) {
            root.remove("Tags");
            return self();
        }
        ListTag tagsList = new ListTag("Tags");
        for (String tag : scoreboardTags) {
            if (tag != null) tagsList.add(new StringTag("", tag));
        }
        root.put(tagsList);
        return self();
    }

    /**
     * Adds passengers riding this entity.
     * @param passengers Builders for the riding entities.
     * @return The specific builder instance.
     */
    public T passengers(EntityNBT<?>... passengers) {
        if (passengers == null || passengers.length == 0) {
            root.remove("Passengers");
            return self();
        }

        ListTag passengersList = (ListTag) root.get("Passengers");
        if (passengersList == null) {
            passengersList = new ListTag("Passengers");
        }

        for (EntityNBT<?> passenger : passengers) {
            if (passenger != null) {
                passengersList.add(passenger.build());
            }
        }

        root.put(passengersList);
        return self();
    }

    // --- 🚀 Terminal Methods ---

    /** * Accessor for the internal root compound.
     * @return The root {@link CompoundTag}.
     */
    protected CompoundTag root() {
        return root;
    }

    /** * Finalizes construction and returns the NBT root.
     * @return The complete {@link CompoundTag}.
     */
    @Override
    public CompoundTag build() {
        return root;
    }

    /**
     * @return The SNBT string of this entity.
     */
    @Override
    public String toString() {
        return root.toString();
    }
}