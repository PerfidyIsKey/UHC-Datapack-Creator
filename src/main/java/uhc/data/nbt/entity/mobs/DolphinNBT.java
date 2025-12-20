package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.traits.BreedableNBT;
import uhc.data.nbt.tags.*;

/**
 * 🐬 **Dolphin NBT Builder**
 * <p>
 * Specialized builder for Dolphin entities.
 * Inherits from MobNBT and implements BreedableNBT for growth logic.
 * </p>
 */
public class DolphinNBT extends MobNBT<DolphinNBT> implements BreedableNBT<DolphinNBT> {

    private DolphinNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Dolphin NBT builder.
     * @return A new DolphinNBT instance.
     */
    public static DolphinNBT create() {
        return new DolphinNBT(CompoundTag.create());
    }

    /**
     * Bridges the trait interfaces to the root NBT tag.
     */
    @Override
    public CompoundTag root() {
        return build();
    }

    // --- Dolphin Specific Data ---

    /**
     * Sets the dolphin's moistness level.
     * <p>
     * 2400 is the standard maximum (full moisture).
     * The dolphin begins taking damage once this reaches 0.
     * </p>
     * @param moistness Ticks of moisture remaining.
     */
    public DolphinNBT moistness(int moistness) {
        // Validation: Minecraft typically caps this at 2400
        root().put(new IntTag("Moistness", moistness));
        return this;
    }

    /**
     * Sets whether the dolphin has recently received a fish from a player.
     * <p>
     * When true, the dolphin will lead the player to shipwrecks or ruins.
     * </p>
     * @param gotFish True if the dolphin was fed.
     */
    public DolphinNBT gotFish(boolean gotFish) {
        root().put(new ByteTag("GotFish", (byte) (gotFish ? 1 : 0)));
        return this;
    }

    /**
     * Helper to set the dolphin to the standard maximum moisture level.
     */
    public DolphinNBT fullyMoist() {
        return moistness(2400);
    }
}