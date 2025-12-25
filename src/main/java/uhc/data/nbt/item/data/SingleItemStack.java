package uhc.data.nbt.item.data;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.item.ItemId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 📦 **Single Item Stack**
 * <p>
 * Represents an item stack structure typically used for entities or blocks that hold
 * a single item without needing a slot index (e.g., Jukeboxes, Item Entities, or
 * Player 'SelectedItem').
 * </p>
 * <p>
 * Following Minecraft 1.20.5+ standards, this utilizes the 'components' map rather
 * than the legacy 'tag' compound.
 * </p>
 */
public class SingleItemStack {
    /** * The unique namespaced identifier for the item (e.g., "minecraft:diamond").
     * This field is final to ensure the stack type remains consistent.
     */
    private final ItemId id;

    /** * The quantity of items in this specific stack.
     * While vanilla usually limits this to 64, NBT can technically support up to 99
     * for specialized technical stacks.
     */
    private int count;

    /** * A collection of {@link ItemComponent} objects representing the 1.20.5+
     * Data Components (e.g., Damage, Enchantments, Custom Data).
     */
    private final List<ItemComponent> components = new ArrayList<>();

    /**
     * Private constructor to enforce controlled instantiation via factory methods.
     * * @param id    The {@link ItemId} for the stack. Cannot be null or {@link ItemId#AIR}.
     * @param count The stack size. Automatically clamped between 1 and 99.
     * @throws IllegalArgumentException if the ID is {@link ItemId#AIR}.
     * @throws NullPointerException     if the ID is null.
     */
    private SingleItemStack(ItemId id, int count) {
        this.id = Objects.requireNonNull(id, "Item ID cannot be null.");

        // Error Catching: Minecraft item stacks in NBT cannot be 'air'.
        // Absence of an item is represented by the absence of the tag itself.
        if (id.equals(ItemId.AIR)) {
            throw new IllegalArgumentException("SingleItemStack cannot be air. Represent empty slots by omitting the tag.");
        }

        this.count = Math.max(1, Math.min(99, count));
    }

    /**
     * Factory method to create a stack with a default count of 1.
     * * @param id The item identifier.
     * @return A new {@code SingleItemStack} instance.
     */
    public static SingleItemStack create(ItemId id) {
        return new SingleItemStack(id, 1);
    }

    /**
     * Factory method to create a stack with a specific quantity.
     * * @param id    The item identifier.
     * @param count The desired stack size (clamped 1-99).
     * @return A new {@code SingleItemStack} instance.
     */
    public static SingleItemStack create(ItemId id, int count) {
        return new SingleItemStack(id, count);
    }

    /**
     * Adds a data component to the item stack.
     * * @param component The property to apply. If null, a {@link NullPointerException} is thrown.
     * @return This instance for method chaining.
     */
    public SingleItemStack addComponent(ItemComponent component) {
        Objects.requireNonNull(component, "Item component to add cannot be null.");
        this.components.add(component);
        return this;
    }

    /**
     * Serializes the item stack into a {@link CompoundTag}.
     * <p>
     * Format: {@code {id: "minecraft:stone", count: 64, components: {"minecraft:damage": 0}}}
     * </p>
     * * @return A {@link CompoundTag} representing the serialized item.
     */
    public CompoundTag toNbt() {
        // Create an anonymous root compound for the item data
        CompoundTag tag = CompoundTag.create("");

        // Add basic item identification
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", count));

        // Process Data Components if any exist
        if (!components.isEmpty()) {
            CompoundTag componentsMap = CompoundTag.create("components");

            // Flag to track if we successfully converted at least one component
            boolean addedAny = false;

            for (ItemComponent component : components) {
                // Get the component's NBT representation
                var componentData = component.toNbt();

                // Catch potential null NBT returns from components
                if (componentData != null) {
                    // Assuming put(NBTTag) or put(CompoundTag) handles the entry
                    componentsMap.put(componentData);
                    addedAny = true;
                }
            }

            // Only attach the "components" compound if it actually contains data
            // This replaces the .isEmpty() check to avoid compiler errors
            if (addedAny) {
                tag.put(componentsMap);
            }
        }
        return tag;
    }

    /** * Gets the item identifier.
     * @return The {@link ItemId}.
     */
    public ItemId getId() {
        return id;
    }

    /** * Gets the number of items in the stack.
     * @return The stack count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Updates the stack count with safety bounds.
     * * @param count The new quantity (clamped 1-99).
     */
    public void setCount(int count) {
        this.count = Math.max(1, Math.min(99, count));
    }
}