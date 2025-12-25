package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.item.components.ComponentId;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 🏷️ **Custom Name Component Implementation**
 * <p>
 * Manages the "minecraft:custom_name" data component.
 * This component overrides the display name of an item using a JSON-formatted
 * text component string.
 * </p>
 */
public class CustomNameComponent implements ItemComponent {

    private final TextComponent name;

    /**
     * Private constructor to enforce use of static factory.
     * @param name The TextComponent representing the name. Must not be null.
     */
    private CustomNameComponent(TextComponent name) {
        this.name = Objects.requireNonNull(name, "Custom name cannot be null.");
    }

    /**
     * Creates a new CustomNameComponent.
     * @param name The TextComponent to display as the item's name.
     * @return A new instance of CustomNameComponent.
     */
    public static CustomNameComponent create(TextComponent name) {
        return new CustomNameComponent(name);
    }

    @Override
    public ComponentId getId() {
        return ComponentId.CUSTOM_NAME;
    }

    /**
     * Converts the component into the required NBT structure.
     * <p>
     * <b>NBT Format:</b> StringTag("minecraft:custom_name", "{\"text\":\"My Item\"}")
     * </p>
     * @return A {@link StringTag} containing the serialized JSON name.
     */
    @Override
    public NBTTag toNbt() {
        // In modern Minecraft, custom_name is a StringTag containing a JSON string.
        // We use the ID's ResourceLocation as the tag name for serialization.
        return new StringTag(getId().getResourceLocation(), name.toString());
    }

    /**
     * Retrieves the underlying TextComponent.
     * @return The name component.
     */
    public TextComponent getName() {
        return name;
    }
}