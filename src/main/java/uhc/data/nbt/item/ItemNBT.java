package uhc.data.nbt.item;

import uhc.data.nbt.BuildableNBT;
import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.item.ItemId;
import uhc.resource.item.ItemResource;

import java.util.Objects;

/**
 * 📦 **Item NBT Builder**
 * <p>
 * Represents a Minecraft Item Stack (version 1.20.5+).
 * This class manages the core identity of an item (ID and Count) and utilizes
 * a {@link ComponentBuilder} to handle the modern Data Component system.
 * </p>
 */
public class ItemNBT implements BuildableNBT {

    // --- Private Fields ---

    /** * The root NBT compound containing the "id", "count", and "components" keys.
     */
    private final CompoundTag root;

    /** * Internal builder used to aggregate individual {@link ItemComponent} objects
     * before they are serialized into the root "components" tag.
     */
    private final ComponentBuilder componentBuilder;

    // --- Constructors & Static Factories ---

    /**
     * Private constructor to initialize the builder state.
     * @param root The initial root tag containing ID and Count.
     */
    private ItemNBT(CompoundTag root) {
        this.root = root;
        this.componentBuilder = ComponentBuilder.create();
    }

    /**
     * Initializes a new Item Stack NBT builder with a default count of 1.
     * <p><b>Error Catching:</b> Prevents null IDs and prohibits the use of 'minecraft:air'
     * as an item stack ID, which is invalid for most NBT-based item operations.</p>
     * * @param id The type-safe {@link ItemId} of the item.
     * @return A new ItemNBT builder instance.
     * @throws NullPointerException if the id is null.
     * @throws IllegalArgumentException if the id represents an air block.
     */
    public static ItemNBT create(ItemResource id) {
        Objects.requireNonNull(id, "Item ID cannot be null.");

        if (id.equals(ItemId.AIR)) {
            throw new IllegalArgumentException("Item stacks cannot be initialized with 'minecraft:air'.");
        }

        CompoundTag tag = CompoundTag.create();
        tag.put(new StringTag("id", id.getResourceLocation()));
        tag.put(new IntTag("count", 1));

        return new ItemNBT(tag);
    }

    // --- Fluent API (Setters) ---

    /**
     * Sets the stack size (count) for this item.
     * <p><b>Error Catching:</b> Clamps the count between 0 and 99 to prevent
     * invalid stack sizes that could crash clients or break inventory logic.</p>
     * * @param count The number of items in the stack.
     * @return This builder instance for chaining.
     */
    public ItemNBT count(int count) {
        // Validation: Minecraft typically caps stacks at 64, but 99 is often
        // the technical NBT limit before overflow or glitching.
        int validatedCount = Math.max(0, Math.min(99, count));
        root.put(new IntTag("count", validatedCount));
        return this;
    }

    /**
     * Adds a specific data component (e.g., PotionContents, CustomName) to the item.
     * <p><b>Error Catching:</b> Validates that the component is not null before
     * passing it to the internal builder.</p>
     * * @param component The {@link ItemComponent} implementation to add.
     * @return This builder instance for chaining.
     * @throws NullPointerException if the component is null.
     */
    public ItemNBT add(ItemComponent component) {
        Objects.requireNonNull(component, "Cannot add a null ItemComponent to ItemNBT.");
        this.componentBuilder.add(component);
        return this;
    }

    /**
     * Overwrites or removes the "components" tag using a pre-constructed CompoundTag.
     * Useful for copying components from one item stack to another.
     * * @param components The NBT compound representing the components map.
     * If null, existing components are cleared.
     * @return This builder instance for chaining.
     */
    public ItemNBT components(CompoundTag components) {
        if (components == null) {
            root.remove("components");
        } else {
            // Ensure the internal name is correctly set for the NBT structure
            components.setName("components");
            root.put(components);
        }
        return this;
    }

    // --- Finalization & Serialization ---

    /**
     * Finalizes the Item Stack by compiling all added components into the root tag.
     * <p><b>Logic:</b> If no components were added via {@code add()},
     * the "components" tag is omitted to keep the NBT lean.</p>
     * * @return The complete {@link CompoundTag} representing the Item Stack.
     */
    @Override
    public CompoundTag build() {
        CompoundTag componentsNbt = componentBuilder.build();

        // Error Catching: Only attach the 'components' tag if it contains data
        // to avoid empty {} tags in the final NBT string.
        if (componentsNbt != null && !componentsNbt.getValue().isEmpty()) {
            componentsNbt.setName("components");
            root.put(componentsNbt);
        }

        return root;
    }

    /**
     * Returns the SNBT (Stringified NBT) representation of this item.
     * Useful for debugging and command output.
     */
    @Override
    public String toString() {
        return root.toString();
    }
}