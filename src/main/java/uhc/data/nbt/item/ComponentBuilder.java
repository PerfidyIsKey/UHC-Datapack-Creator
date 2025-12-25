package uhc.data.nbt.item;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.item.components.ItemComponent;
import uhc.data.nbt.tags.CompoundTag;
import java.util.Objects;

/**
 * 🛠️ **Component Builder**
 * <p>
 * A modular collector for modern Minecraft Data Components.
 * This builder accepts any implementation of {@link ItemComponent} and
 * assembles them into the final 'components' NBT map.
 * </p>
 */
public class ComponentBuilder {

    private final CompoundTag root = CompoundTag.create();

    /**
     * Initializes a new ComponentBuilder instance.
     * @return A new builder.
     */
    public static ComponentBuilder create() {
        return new ComponentBuilder();
    }

    /**
     * Adds a specific data component to the item.
     * <p>
     * This method leverages polymorphism: it takes any {@link ItemComponent},
     * retrieves its NBT representation, and ensures the tag name is correctly
     * set to the namespaced ID before placing it in the map.
     * </p>
     * @param component The component implementation (e.g., LoreComponent, CustomNameComponent).
     * @return This builder instance for fluent chaining.
     */
    public ComponentBuilder add(ItemComponent component) {
        Objects.requireNonNull(component, "Cannot add a null component to the builder.");

        // Convert the component logic into raw NBT
        NBTTag nbt = component.toNbt();

        // Safety check: ensure the tag name matches the component's registry ID
        // This is critical for the CompoundTag.put() to use the correct key.
        nbt.setName(component.getId().getResourceLocation());

        root.put(nbt);
        return this;
    }

    /**
     * Finalizes the building process.
     * @return A {@link CompoundTag} representing the 'components' map.
     */
    public CompoundTag build() {
        return root;
    }
}