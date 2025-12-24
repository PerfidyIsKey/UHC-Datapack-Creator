package uhc.data.nbt.entity.items;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.item.ItemNBT;
import uhc.data.nbt.tags.*;
import java.util.Objects;
import java.util.UUID;

/**
 * 📦 **Item NBT Builder**
 * <p>
 * Specialized builder for Item entities (dropped items on the ground).
 * Manages despawn timers, health, pickup restrictions, and attribution.
 * </p>
 */
public class ItemEntityNBT extends EntityNBT<ItemEntityNBT> {

    private ItemEntityNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Item NBT builder.
     * @return A new instance of ItemNBT.
     */
    public static ItemEntityNBT create() {
        return new ItemEntityNBT(CompoundTag.create());
    }

    @Override
    public CompoundTag root() {
        return build();
    }

    // --- ⏳ Life & Survival ---

    /**
     * Sets the age of the item in ticks.
     * <p>
     * <b>Special Value:</b> Use {@code -32768} to prevent the item from
     * ever despawning (the age will not increase).
     * </p>
     * @param ticks Ticks since the item was dropped (default despawn is 6000).
     * @return This builder instance.
     */
    public ItemEntityNBT age(short ticks) {
        root().put(new ShortTag("Age", ticks));
        return this;
    }

    /**
     * Sets the health of the item entity.
     * @param health The health value (default is 5). Item is destroyed at 0.
     * @return This builder instance.
     */
    public ItemEntityNBT health(short health) {
        root().put(new ShortTag("Health", health));
        return this;
    }

    /**
     * Sets the pickup delay in ticks.
     * <p>
     * <b>Special Value:</b> Use {@code 32767} to make the item impossible
     * to pick up (the delay will not decrease).
     * </p>
     * @param ticks Ticks until the item can be collected.
     * @return This builder instance.
     */
    public ItemEntityNBT pickupDelay(short ticks) {
        root().put(new ShortTag("PickupDelay", ticks));
        return this;
    }

    // --- 📦 Item Stack ---

    /**
     * Sets the actual item data for this entity.
     * <p>
     * This method takes the high-level {@link ItemNBT} builder, finalizes its
     * internal structure, and injects it into the "Item" tag of the entity.
     * </p>
     * @param itemData The Item builder (e.g., Diamond Sword with Enchantments).
     * @return This builder instance for fluent chaining.
     * @throws NullPointerException if itemData is null.
     */
    public ItemEntityNBT item(ItemNBT itemData) {
        Objects.requireNonNull(itemData, "ItemNBT builder cannot be null.");

        // Finalize the item stack NBT (id, count, and components)
        CompoundTag finalizedItem = itemData.build();

        // Ensure the tag is named "Item" so the entity recognizes it
        finalizedItem.setName("Item");

        // Inject into the entity root
        root().put(finalizedItem);
        return this;
    }

    // --- 👥 Attribution & Ownership ---

    /**
     * Restricts pickup to a specific player.
     * @param uuid The UUID of the only player allowed to pick this up.
     * @return This builder instance.
     */
    public ItemEntityNBT owner(UUID uuid) {
        return setUuidArray("Owner", uuid);
    }

    /**
     * Sets the entity who threw or dropped this item.
     * @param uuid The UUID of the thrower.
     * @return This builder instance.
     */
    public ItemEntityNBT thrower(UUID uuid) {
        return setUuidArray("Thrower", uuid);
    }

    /**
     * Internal helper to handle UUID to IntArray conversion.
     */
    private ItemEntityNBT setUuidArray(String key, UUID uuid) {
        if (uuid == null) {
            root().remove(key);
            return this;
        }
        long mostSig = uuid.getMostSignificantBits();
        long leastSig = uuid.getLeastSignificantBits();
        int[] uuidArray = {
                (int) (mostSig >> 32), (int) mostSig,
                (int) (leastSig >> 32), (int) leastSig
        };
        root().put(new IntArrayTag(key, uuidArray));
        return this;
    }
}