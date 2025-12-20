package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.IntTag;
import uhc.resource.item.components.ComponentId;

/**
 * 🛠️ **Damage Component Implementation**
 * <p>
 * Manages the "minecraft:damage" component.
 * Represents the amount of durability already consumed from the item.
 * </p>
 */
public class DamageComponent implements ItemComponent {

    private final int damage;

    /**
     * Creates a new Damage component.
     * @param damage The amount of durability used (must be non-negative).
     */
    public DamageComponent(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        this.damage = damage;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.DAMAGE;
    }

    /**
     * Converts the damage value into an IntTag.
     * <p>
     * <b>Format:</b> {@code "minecraft:damage": 5}
     * </p>
     * @return A named {@link IntTag} representing the damage.
     */
    @Override
    public NBTTag toNbt() {
        // The tag name must be the namespaced ID (minecraft:damage).
        return new IntTag(getId().getResourceLocation(), damage);
    }

    public int getDamage() {
        return damage;
    }
}