package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 🧟 **Mob NBT Builder**
 * <p>
 * Extends {@link EntityNBT} to include data specific to living mobs.
 * Uses recursive generics to maintain the fluent API across sub-classes.
 * </p>
 * @param <T> The specific type of the builder for fluent chaining.
 */
@SuppressWarnings("unchecked")
public abstract class MobNBT<T extends MobNBT<T>> extends EntityNBT<T> {

    protected MobNBT(CompoundTag root) {
        super(root);
    }

    // --- Stats and Health ---

    /** Sets the current health of the mob. Minecraft uses floats for heart precision. */
    public T health(float health) {
        root().put(new FloatTag("Health", health));
        return (T) this;
    }

    /** Sets extra health provided by the Absorption effect (Golden Apples, etc). */
    public T absorptionAmount(float amount) {
        root().put(new FloatTag("AbsorptionAmount", amount));
        return (T) this;
    }

    /** Disables the mob's AI, making it completely stationary. */
    public T noAI(boolean noAI) {
        root().put(new ByteTag("NoAI", (byte) (noAI ? 1 : 0)));
        return (T) this;
    }

    /** If true, the mob will not despawn even if the player is far away. */
    public T persistenceRequired(boolean persistence) {
        root().put(new ByteTag("PersistenceRequired", (byte) (persistence ? 1 : 0)));
        return (T) this;
    }

    // --- Equipment and Drops ---

    /** * Sets the mob's equipment (Armor and Held Items).
     * @param equipment CompoundTag containing 'ArmorItems' and 'HandItems'.
     */
    public T equipment(CompoundTag equipment) {
        Objects.requireNonNull(equipment, "Equipment tag cannot be null.");
        equipment.setName("ArmorItems"); // standard Minecraft key, adjust if using custom logic
        root().put(equipment);
        return (T) this;
    }

    /** Allows the mob to pick up items and wear armor from the ground. */
    public T canPickUpLoot(boolean canPickUp) {
        root().put(new ByteTag("CanPickUpLoot", (byte) (canPickUp ? 1 : 0)));
        return (T) this;
    }

    /** Sets the main hand to be the left hand. */
    public T leftHanded(boolean leftHanded) {
        root().put(new ByteTag("LeftHanded", (byte) (leftHanded ? 1 : 0)));
        return (T) this;
    }

    // --- Potion Effects & Attributes ---

    /** Sets the list of active potion effects. */
    public T activeEffects(List<CompoundTag> effects) {
        ListTag list = new ListTag("active_effects");
        for (CompoundTag effect : effects) {
            list.add(effect);
        }
        root().put(list);
        return (T) this;
    }

    // --- Leashing ---

    /**
     * Attaches the mob to a specific coordinate (fence post).
     */
    public T leash(int x, int y, int z) {
        root().put(new IntArrayTag("leash", new int[]{x, y, z}));
        return (T) this;
    }

    /**
     * Attaches the mob to another entity via UUID.
     */
    public T leash(UUID entityUuid) {
        Objects.requireNonNull(entityUuid);
        CompoundTag leashCompound = CompoundTag.create("leash");

        long most = entityUuid.getMostSignificantBits();
        long least = entityUuid.getLeastSignificantBits();
        int[] uuidArray = new int[]{
                (int) (most >> 32), (int) most,
                (int) (least >> 32), (int) least
        };

        leashCompound.put(new IntArrayTag("UUID", uuidArray));
        root().put(leashCompound);
        return (T) this;
    }

    /**
     * Sets the leash NBT for this mob.
     * Since this is now concrete, children like HorseNBT don't need to implement it.
     */
    @SuppressWarnings("unchecked")
    public T leash(CompoundTag leash) {
        if (leash != null) {
            leash.setName("leash");
            root().put(leash);
        }
        return (T) this;
    }

    // --- Pathfinding ---

    public T homePos(int x, int y, int z) {
        root().put(new IntArrayTag("home_pos", new int[]{x, y, z}));
        return (T) this;
    }

    public T homeRadius(int radius) {
        root().put(new IntTag("home_radius", radius));
        return (T) this;
    }
}