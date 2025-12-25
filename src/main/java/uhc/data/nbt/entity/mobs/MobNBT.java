package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 🧟 **Mob NBT Builder**
 * <p>
 * Extends {@link EntityNBT} to include data specific to living mobs (Entities with AI, health, and equipment).
 * Uses recursive generics to maintain the fluent API across sub-classes.
 * </p>
 * @param <T> The specific type of the builder for fluent chaining.
 */
public abstract class MobNBT<T extends MobNBT<T>> extends EntityNBT<T> {

    /**
     * Protected constructor for MobNBT.
     * @param root The root CompoundTag for this mob.
     */
    protected MobNBT(CompoundTag root) {
        super(root);
    }

    // --- Stats and Health ---

    /** * Sets the current health of the mob.
     * @param health Current health value (Minecraft uses floats for heart precision).
     * @return (T) The specific builder instance.
     */
    public T health(float health) {
        root().put(new FloatTag("Health", health));
        return (T) this;
    }

    /** * Sets extra health provided by the Absorption effect (Golden Apples, etc).
     * @param amount The number of absorption hearts.
     * @return (T) The specific builder instance.
     */
    public T absorptionAmount(float amount) {
        root().put(new FloatTag("AbsorptionAmount", amount));
        return (T) this;
    }

    /** * Disables the mob's AI, making it completely stationary and non-interactive.
     * @param noAI True to disable AI.
     * @return (T) The specific builder instance.
     */
    public T noAI(boolean noAI) {
        root().put(new ByteTag("NoAI", (byte) (noAI ? 1 : 0)));
        return (T) this;
    }

    /** * If true, the mob will not despawn even if the player moves far away.
     * @param persistence True to prevent natural despawning.
     * @return (T) The specific builder instance.
     */
    public T persistenceRequired(boolean persistence) {
        root().put(new ByteTag("PersistenceRequired", (byte) (persistence ? 1 : 0)));
        return (T) this;
    }

    // --- Equipment and Drops ---

    /** * Sets the mob's equipment (Armor and Held Items).
     * @param equipment CompoundTag containing 'ArmorItems' and 'HandItems'.
     * @return (T) The specific builder instance.
     */
    public T equipment(CompoundTag equipment) {
        Objects.requireNonNull(equipment, "Equipment tag cannot be null.");
        equipment.setName("ArmorItems");
        root().put(equipment);
        return (T) this;
    }

    /** * Allows the mob to pick up items and wear armor from the ground.
     * @param canPickUp True to allow looting.
     * @return (T) The specific builder instance.
     */
    public T canPickUpLoot(boolean canPickUp) {
        root().put(new ByteTag("CanPickUpLoot", (byte) (canPickUp ? 1 : 0)));
        return (T) this;
    }

    /** * Sets the mob's dominant hand to be the left hand.
     * @param leftHanded True for left-handed.
     * @return (T) The specific builder instance.
     */
    public T leftHanded(boolean leftHanded) {
        root().put(new ByteTag("LeftHanded", (byte) (leftHanded ? 1 : 0)));
        return (T) this;
    }

    /**
     * Sets the probability that equipped items will drop when the mob dies.
     * <p><b>Catch:</b> This updates both {@code HandDropChances} and {@code ArmorDropChances}.</p>
     * @param chances Up to 5 floats (0.0 to 1.0).
     * Order: [Main Hand, Off Hand, Boots, Leggings, Chestplate, Helmet].
     * @return (T) The specific builder instance.
     */
    public T dropChances(float... chances) {
        if (chances == null) return (T) this;

        ListTag handChances = new ListTag("HandDropChances");
        ListTag armorChances = new ListTag("ArmorDropChances");

        for (int i = 0; i < chances.length; i++) {
            // Error catching: Clamp between 0.0 and 1.0
            float clamped = Math.max(0.0f, Math.min(1.0f, chances[i]));
            FloatTag tag = new FloatTag("", clamped);

            if (i < 2) handChances.add(tag);
            else if (i < 6) armorChances.add(tag);
        }

        root().put(handChances);
        root().put(armorChances);
        return (T) this;
    }

    // --- Potion Effects & Attributes ---

    /** * Sets the list of active potion effects for the mob.
     * @param effects A list of CompoundTags containing effect ID, Duration, and Amplifier.
     * @return (T) The specific builder instance.
     */
    public T activeEffects(List<CompoundTag> effects) {
        Objects.requireNonNull(effects, "Effects list cannot be null.");
        ListTag list = new ListTag("active_effects");
        for (CompoundTag effect : effects) {
            list.add(effect);
        }
        root().put(list);
        return (T) this;
    }

    // --- Leashing ---

    /**
     * Attaches the mob to a specific coordinate (usually a fence post).
     * @return (T) The specific builder instance.
     */
    public T leash(int x, int y, int z) {
        root().put(new IntArrayTag("leash", new int[]{x, y, z}));
        return (T) this;
    }

    /**
     * Attaches the mob to another entity via its UUID.
     * @return (T) The specific builder instance.
     */
    public T leash(UUID entityUuid) {
        Objects.requireNonNull(entityUuid, "Leash UUID cannot be null.");
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
     * Sets a raw leash NBT compound for this mob.
     * @return (T) The specific builder instance.
     */
    public T leash(CompoundTag leash) {
        if (leash != null) {
            leash.setName("leash");
            root().put(leash);
        }
        return (T) this;
    }

    // --- Pathfinding & Home ---

    /** Sets the "home" position coordinates for mob pathfinding. */
    public T homePos(int x, int y, int z) {
        root().put(new IntArrayTag("home_pos", new int[]{x, y, z}));
        return (T) this;
    }

    /** Sets the maximum distance the mob can wander from its home position. */
    public T homeRadius(int radius) {
        root().put(new IntTag("home_radius", radius));
        return (T) this;
    }
}