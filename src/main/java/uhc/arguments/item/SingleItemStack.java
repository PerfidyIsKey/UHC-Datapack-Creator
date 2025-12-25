package uhc.arguments.item;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.item.ItemId;
import uhc.resource.item.ItemResource;

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

    // --- ⚙️ State & Fields ---

    /** * The unique namespaced identifier for the item (e.g., "minecraft:diamond").
     * This field is final to ensure the stack type remains consistent throughout
     * its lifecycle.
     */
    private final ItemResource id;

    /** * The quantity of items in this specific stack.
     * While vanilla usually limits this to 64, NBT can technically support up to 99
     * for specialized technical stacks before visual overflow occurs.
     */
    private int count;

    /** * A collection of {@link ItemComponent} objects representing the 1.20.5+
     * Data Components (e.g., Damage, Enchantments, Custom Data).
     */
    private final List<ItemComponent> components = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce controlled instantiation via factory methods.
     * <p><b>Error Catching:</b> Validates that the item is not {@link ItemId#AIR}
     * and clamps the count to valid Minecraft NBT bounds (1-99).</p>
     * * @param id    The {@link ItemResource} for the stack. Cannot be null.
     * @param count The stack size. Automatically clamped between 1 and 99.
     * @throws IllegalArgumentException if the ID is {@link ItemId#AIR}.
     * @throws NullPointerException     if the ID is null.
     */
    private SingleItemStack(ItemResource id, int count) {
        this.id = Objects.requireNonNull(id, "Item ID cannot be null for an ItemStack.");

        // Minecraft item stacks in NBT cannot be 'air'.
        // Absence of an item is represented by the absence of the tag itself.
        if (id.equals(ItemId.AIR)) {
            throw new IllegalArgumentException("SingleItemStack cannot be air. Represent empty slots by omitting the tag.");
        }

        this.count = Math.max(1, Math.min(99, count));
    }

    /**
     * Factory method to create a stack with a default count of 1.
     * * @param id The {@link ItemResource} identifier.
     * @return A new {@code SingleItemStack} instance.
     * @throws NullPointerException if the ID is null.
     */
    public static SingleItemStack create(ItemResource id) {
        return new SingleItemStack(id, 1);
    }

    /**
     * Factory method to create a stack with a specific quantity.
     * * @param id    The {@link ItemResource} identifier.
     * @param count The desired stack size (clamped 1-99).
     * @return A new {@code SingleItemStack} instance.
     * @throws NullPointerException if the ID is null.
     */
    public static SingleItemStack create(ItemResource id, int count) {
        return new SingleItemStack(id, count);
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Adds a data component to the item stack.
     * <p><b>Error Catching:</b> Prevents the addition of null components which
     * would cause failures during the serialization phase.</p>
     * * @param component The {@link ItemComponent} property to apply.
     * @return This instance for method chaining.
     * @throws NullPointerException if the component is null.
     */
    public SingleItemStack addComponent(ItemComponent component) {
        this.components.add(Objects.requireNonNull(component, "Item component to add cannot be null."));
        return this;
    }

    // --- 🛰️ Accessors & Mutators ---

    /** * Gets the item identifier.
     * @return The {@link ItemResource} object.
     */
    public ItemResource getId() {
        return id;
    }

    /** * Gets the number of items in the stack.
     * @return The current stack count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Updates the stack count with safety bounds.
     * <p><b>Error Catching:</b> Automatically clamps the value between 1 and 99
     * to ensure NBT compatibility.</p>
     * * @param count The new quantity.
     */
    public void setCount(int count) {
        this.count = Math.max(1, Math.min(99, count));
    }

    // --- 📝 Serialization ---

    /**
     * Serializes the item stack into a {@link CompoundTag}.
     * <p><b>Format:</b> {@code {id: "minecraft:stone", count: 64, components: {"minecraft:damage": 0}}}</p>
     * <p><b>Error Catching:</b> Validates that components produce non-null NBT data
     * before adding them to the components map. If a component fails to serialize,
     * it is caught and skipped to preserve the rest of the stack.</p>
     * * @return A {@link CompoundTag} representing the serialized item.
     * @throws IllegalStateException if the item ID fails its internal validation checks.
     */
    public CompoundTag toNbt() {
        // Ensure the ID (ResourceLocation) is syntactically valid before serializing
        id.validate();

        // Create an anonymous root compound for the item data
        CompoundTag tag = CompoundTag.create("");

        // Add basic item identification
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", count));

        // Process Data Components if any exist
        if (!components.isEmpty()) {
            CompoundTag componentsMap = CompoundTag.create("components");
            boolean addedAny = false;

            for (ItemComponent component : components) {
                try {
                    var componentData = component.toNbt();
                    if (componentData != null) {
                        componentsMap.put(componentData);
                        addedAny = true;
                    }
                } catch (Exception e) {
                    // Critical Catch: Prevents one bad component implementation from
                    // failing the whole stack serialization.
                }
            }

            // Only attach the "components" compound if it actually contains successful data
            if (addedAny) {
                tag.put(componentsMap);
            }
        }
        return tag;
    }
}