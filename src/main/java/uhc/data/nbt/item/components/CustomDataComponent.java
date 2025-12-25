package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.CompoundTag;
import uhc.resource.item.components.ComponentId;

import java.util.Objects;

/**
 * 🏷️ **Custom Data Component Implementation**
 * <p>
 * Manages the "minecraft:custom_data" component.
 * Acts as a container for arbitrary NBT data used by plugins or data packs.
 * </p>
 */
public class CustomDataComponent implements ItemComponent {

    private final CompoundTag data;

    /**
     * Private constructor to enforce use of static factory.
     */
    private CustomDataComponent() {
        this.data = CompoundTag.create(getId().getResourceLocation());
    }

    /**
     * Static factory method to create a new instance.
     * @return A new CustomDataComponent instance.
     */
    public static CustomDataComponent create() {
        return new CustomDataComponent();
    }

    /**
     * Adds an NBT tag to the custom data container.
     * @param tag The tag to add (e.g. ByteTag, StringTag, etc.)
     * @return This component for fluent building.
     */
    public CustomDataComponent put(NBTTag tag) {
        this.data.put(Objects.requireNonNull(tag));
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.CUSTOM_DATA;
    }

    @Override
    public NBTTag toNbt() {
        return data;
    }
}