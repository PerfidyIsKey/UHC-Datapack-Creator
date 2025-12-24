package uhc.data.nbt.entity.projectiles;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.*;
import uhc.resource.ItemId;

import java.util.Objects;

/**
 * 🎆 **Firework Rocket NBT Builder**
 * <p>
 * Specialized builder for Firework Rocket entities.
 * Manages flight duration, explosion timing, and item-based components.
 * </p>
 */
public class FireworkRocketNBT extends ProjectileNBT {

    private FireworkRocketNBT(CompoundTag root) {
        super(root);
    }

    public static FireworkRocketNBT create() {
        return new FireworkRocketNBT(CompoundTag.create());
    }

    // --- 🚀 Flight Logic ---

    public FireworkRocketNBT life(int ticks) {
        root().put(new IntTag("Life", Math.max(0, ticks)));
        return this;
    }

    public FireworkRocketNBT lifeTime(int ticks) {
        root().put(new IntTag("LifeTime", Math.max(0, ticks)));
        return this;
    }

    public FireworkRocketNBT shotAtAngle(boolean shotAtAngle) {
        root().put(new ByteTag("ShotAtAngle", (byte) (shotAtAngle ? 1 : 0)));
        return this;
    }

    // --- 📦 Item Data ---

    /**
     * Sets the FireworksItem data using one or more components.
     * <p>
     * This automatically builds an internal Item Stack (minecraft:firework_rocket)
     * and attaches the provided components to its 'components' map.
     * </p>
     * @param components One or more {@link ItemComponent} (e.g., FireworksComponent).
     * @return This builder instance.
     */
    public FireworkRocketNBT fireworksItem(ItemComponent... components) {
        Objects.requireNonNull(components, "Components cannot be null.");

        // 1. Create the root 'FireworksItem' compound
        CompoundTag itemStack = CompoundTag.create("FireworksItem");

        // 2. Mandatory Item Stack fields
        itemStack.put(new StringTag("id", ItemId.FIREWORK_ROCKET.getResourceLocation()));
        itemStack.put(new IntTag("count", 1));

        // 3. Create the 'components' compound
        CompoundTag componentsMap = CompoundTag.create("components");
        for (ItemComponent component : components) {
            if (component != null) {
                // Each component's toNbt() provides the key-value pair
                componentsMap.put(component.toNbt());
            }
        }

        // 4. Attach components to the item stack, then stack to entity root
        if (!componentsMap.getValue().isEmpty()) {
            itemStack.put(componentsMap);
        }

        root().put(itemStack);
        return this;
    }

    // --- 🚀 Fix: Overriding base methods for chaining ---

    @Override
    public FireworkRocketNBT glowing(boolean glowing) {
        super.glowing(glowing);
        return this;
    }

    @Override
    public FireworkRocketNBT invulnerable(boolean invulnerable) {
        super.invulnerable(invulnerable);
        return this;
    }

    @Override
    public FireworkRocketNBT silent(boolean silent) {
        super.silent(silent);
        return this;
    }
}