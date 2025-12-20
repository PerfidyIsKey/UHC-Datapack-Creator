package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.FloatTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.item.components.ComponentId;

/**
 * ⏳ **Use Cooldown Component Implementation**
 * <p>
 * Manages the "minecraft:use_cooldown" component.
 * Triggers a visual and mechanical cooldown on items when used.
 * </p>
 */
public class UseCooldownComponent implements ItemComponent {

    private float seconds;
    private String cooldownGroup;

    /**
     * Private constructor for fluent building.
     */
    private UseCooldownComponent() {}

    /**
     * Creates a new UseCooldownComponent instance.
     * @return A new builder instance.
     */
    public static UseCooldownComponent create() {
        return new UseCooldownComponent();
    }

    /**
     * Sets the duration of the cooldown.
     * @param seconds Non-negative float duration in seconds.
     */
    public UseCooldownComponent seconds(float seconds) {
        this.seconds = Math.max(0.0f, seconds);
        return this;
    }

    /**
     * Sets an optional cooldown group.
     * <p>
     * Items in the same group share cooldowns regardless of their item type.
     * If omitted, the cooldown only applies to items of the same type (e.g., all Ender Pearls).
     * </p>
     * @param cooldownGroup A unique resource location (e.g., "uhc:combat_items").
     */
    public UseCooldownComponent cooldownGroup(String cooldownGroup) {
        this.cooldownGroup = cooldownGroup;
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.USE_COOLDOWN;
    }

    @Override
    public NBTTag toNbt() {
        CompoundTag root = CompoundTag.create(getId().getResourceLocation());

        root.put(new FloatTag("seconds", seconds));

        if (cooldownGroup != null) {
            root.put(new StringTag("cooldown_group", cooldownGroup));
        }

        return root;
    }
}