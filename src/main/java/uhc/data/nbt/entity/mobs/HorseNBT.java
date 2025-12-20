package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.MobNBT;
import uhc.data.nbt.entity.traits.BreedableNBT;
import uhc.data.nbt.tags.*;
import uhc.resource.entity.mobs.horse.HorseVariant;

import java.util.Objects;
import java.util.UUID;

/**
 * 🐎 **Horse NBT Builder**
 * <p>
 * A specialized builder for equine entities.
 * Strictly implements the Horse-specific data paths.
 * </p>
 */
public class HorseNBT extends MobNBT<HorseNBT> implements BreedableNBT<HorseNBT> {

    private HorseNBT(CompoundTag root) {
        super(root);
    }

    public static HorseNBT create() {
        return new HorseNBT(CompoundTag.create());
    }

    @Override
    public CompoundTag root() {
        return build();
    }

    // --- Breeding and Growth ---

    public HorseNBT bred(boolean bred) {
        root().put(new ByteTag("Bred", (byte) (bred ? 1 : 0)));
        return this;
    }

    public HorseNBT eatingHaystack(boolean eating) {
        root().put(new ByteTag("EatingHaystack", (byte) (eating ? 1 : 0)));
        return this;
    }

    // --- Taming and Ownership ---

    public HorseNBT owner(UUID uuid) {
        Objects.requireNonNull(uuid, "Owner UUID cannot be null.");
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        root().put(new IntArrayTag("Owner", new int[]{
                (int) (most >> 32), (int) most,
                (int) (least >> 32), (int) least
        }));
        return this;
    }

    public HorseNBT tame(boolean tame) {
        root().put(new ByteTag("Tame", (byte) (tame ? 1 : 0)));
        return this;
    }

    public HorseNBT temper(int temper) {
        int clamped = Math.max(0, Math.min(100, temper));
        root().put(new IntTag("Temper", clamped));
        return this;
    }

    // --- Visual Appearance ---

    public HorseNBT variant(HorseVariant color, HorseVariant marking) {
        Objects.requireNonNull(color, "Color cannot be null.");
        Objects.requireNonNull(marking, "Marking cannot be null.");

        // color | (marking << 8)
        int combinedId = HorseVariant.calculate(color, marking);
        root().put(new IntTag("Variant", combinedId));
        return this;
    }
}