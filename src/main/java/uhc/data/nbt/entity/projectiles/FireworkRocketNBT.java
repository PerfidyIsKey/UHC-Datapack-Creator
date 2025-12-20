package uhc.data.nbt.entity.projectiles;

import uhc.data.nbt.tags.*;
import java.util.Objects;

/**
 * 🎆 **Firework Rocket NBT Builder**
 * <p>
 * Specialized builder for Firework Rocket entities.
 * Manages flight duration, explosion timing, and item data.
 * </p>
 */
public class FireworkRocketNBT extends ProjectileNBT {

    private FireworkRocketNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Firework Rocket NBT builder.
     * @return A new instance of FireworkRocketNBT.
     */
    public static FireworkRocketNBT create() {
        return new FireworkRocketNBT(CompoundTag.create());
    }

    // --- 🚀 Flight Logic ---

    /**
     * Sets the number of ticks this rocket has currently been flying.
     * @param ticks Ticks elapsed.
     * @return This builder instance.
     */
    public FireworkRocketNBT life(int ticks) {
        root().put(new IntTag("Life", Math.max(0, ticks)));
        return this;
    }

    /**
     * Sets the total duration (in ticks) before the rocket explodes.
     * <p>
     * <b>Formula:</b> ((FlightLevel + 1) * 10 + rand(0..5) + rand(0..6))
     * </p>
     * @param ticks Total ticks until detonation.
     * @return This builder instance.
     */
    public FireworkRocketNBT lifeTime(int ticks) {
        root().put(new IntTag("LifeTime", Math.max(0, ticks)));
        return this;
    }

    /**
     * Defines if the firework was launched at an angle (e.g., from a Crossbow or Dispenser).
     * @param shotAtAngle true if shot at an angle.
     * @return This builder instance.
     */
    public FireworkRocketNBT shotAtAngle(boolean shotAtAngle) {
        root().put(new ByteTag("ShotAtAngle", (byte) (shotAtAngle ? 1 : 0)));
        return this;
    }

    // --- 📦 Item Data ---

    /**
     * Sets the Fireworks Item compound representing the rocket item itself.
     * <p>
     * This compound contains the 'Fireworks' tag which defines explosions and flight duration.
     * </p>
     * @param itemData The CompoundTag representing the Item Stack.
     * @throws NullPointerException if itemData is null.
     * @return This builder instance.
     */
    public FireworkRocketNBT fireworksItem(CompoundTag itemData) {
        Objects.requireNonNull(itemData, "FireworksItem compound cannot be null.");
        itemData.setName("FireworksItem");
        root().put(itemData);
        return this;
    }
}