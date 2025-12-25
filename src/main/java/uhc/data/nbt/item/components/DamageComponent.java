package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.IntTag;
import uhc.resource.item.components.ComponentId;

/**
 * 🛠️ **Damage Component Implementation**
 * <p>
 * Manages the "minecraft:damage" data component (1.20.5+).
 * This component tracks the amount of durability already consumed from a tool,
 * weapon, or piece of armor.
 * </p>
 * * <p><b>Note:</b> Minecraft uses this value to determine the durability bar length.
 * If this value exceeds the item's maximum durability, the item will break
 * on its next use.</p>
 */
public class DamageComponent implements ItemComponent {

    private final int damage;

    /**
     * Private constructor for the damage component.
     * * @param damage The amount of durability used.
     * Must be a non-negative integer.
     * @throws IllegalArgumentException if damage is less than 0.
     */
    private DamageComponent(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative. Value: " + damage);
        }
        this.damage = damage;
    }

    /**
     * Creates a new Damage component for fluent item building.
     * * @param damage Amount of durability used (0 = brand new).
     * @return A new instance of DamageComponent.
     */
    public static DamageComponent create(int damage) {
        return new DamageComponent(damage);
    }

    @Override
    public ComponentId getId() {
        return ComponentId.DAMAGE;
    }

    /**
     * Converts the damage value into the modern NBT structure.
     * * <p><b>NBT Representation:</b> {@code "minecraft:damage": <int>}</p>
     * * @return An {@link IntTag} with the component's ResourceLocation as the key.
     */
    @Override
    public NBTTag toNbt() {
        // The tag name is derived from ComponentId.DAMAGE (minecraft:damage).
        return new IntTag(getId().getResourceLocation(), damage);
    }

    /**
     * Returns the current damage value stored in this component.
     * @return The integer damage.
     */
    public int getDamage() {
        return damage;
    }
}