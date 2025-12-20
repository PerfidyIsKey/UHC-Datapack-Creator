package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.ListTag;
import uhc.data.nbt.tags.StringTag;
import uhc.resource.item.components.ComponentId;
import uhc.text.TextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 📜 **Lore Component Implementation**
 * <p>
 * Manages the "minecraft:lore" data component (1.20.5+).
 * This component adds lines of descriptive text to an item's tooltip.
 * </p>
 * <p>
 * <b>NBT Structure:</b>
 * {@code minecraft:lore: [ '{"text":"Line 1"}', '{"text":"Line 2"}' ]}
 * </p>
 */
public class LoreComponent implements ItemComponent {

    private final List<TextComponent> lines = new ArrayList<>();

    /**
     * Private constructor for lore lines.
     * @param lines Initial lines of text to display.
     */
    private LoreComponent(TextComponent... lines) {
        if (lines != null) {
            for (TextComponent line : lines) {
                if (line != null) this.lines.add(line);
            }
        }
    }

    /**
     * Creates a new LoreComponent with the specified lines.
     * @param lines Array of TextComponents.
     * @return A new instance of LoreComponent.
     */
    public static LoreComponent create(TextComponent... lines) {
        return new LoreComponent(lines);
    }

    /**
     * Adds an additional line of lore to the existing list.
     * @param line The TextComponent to append to the bottom of the lore.
     * @return This component instance for fluent chaining.
     */
    public LoreComponent addLine(TextComponent line) {
        if (line != null) {
            this.lines.add(line);
        }
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.LORE;
    }

    /**
     * Converts the lore lines into the required Minecraft NBT structure.
     * <p>
     * <b>NBT Representation:</b> A ListTag containing JSON-formatted StringTags.
     * </p>
     * @return A named {@link ListTag} ready for item serialization.
     */
    @Override
    public NBTTag toNbt() {
        // The ListTag name must be the namespaced ID (minecraft:lore)
        ListTag list = new ListTag(getId().getResourceLocation());

        for (TextComponent line : lines) {
            // Individual tags within a ListTag have no internal name/key
            list.add(new StringTag("", line.toString()));
        }

        return list;
    }

    /**
     * Returns an immutable view of the lines currently in this component.
     * @return List of TextComponents.
     */
    public List<TextComponent> getLines() {
        return Collections.unmodifiableList(lines);
    }
}