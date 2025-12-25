package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.resource.item.components.ComponentId;

/**
 * Interface for modern Minecraft Data Components.
 */
public interface ItemComponent {
    /**
     * @return The Registry ID for the component.
     */
    ComponentId getId();

    /**
     * Converts the component data into a serializable NBT structure.
     * The returned NBTTag's name should match the ComponentId's resource location.
     */
    NBTTag toNbt();
}