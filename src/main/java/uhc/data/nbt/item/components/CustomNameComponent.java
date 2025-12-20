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
 * This sets the display name of the item using a JSON text component.
 * </p>
 */
public class CustomNameComponent implements ItemComponent {

    private final TextComponent name;

    /**
     * Creates a new Custom Name component.
     * @param name The TextComponent representing the name. Must not be null.
     */
    public CustomNameComponent(TextComponent name) {
        this.name = Objects.requireNonNull(name, "Custom name cannot be null.");
    }

    @Override
    public ComponentId getId() {
        return ComponentId.CUSTOM_NAME;
    }

    /**
     * Converts the component into the required NBT structure.
     * <p>
     * <b>Format:</b> {@code "minecraft:custom_name": '{"text":"My Item","color":"gold"}'}
     * </p>
     * @return A named {@link StringTag} containing the JSON text.
     */
    @Override
    public NBTTag toNbt() {
        // In 1.20.5+, custom_name is a StringTag containing the JSON text component.
        // The tag name must be the namespaced ID (minecraft:custom_name).
        return new StringTag(getId().getResourceLocation(), name.toString());
    }

    public TextComponent getName() {
        return name;
    }
}