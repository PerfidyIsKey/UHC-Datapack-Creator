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
 * Manages the "minecraft:lore" data component, which displays lines of
 * descriptive text in an item's tooltip.
 * </p>
 */
public class LoreComponent implements ItemComponent {

    private final List<TextComponent> lines = new ArrayList<>();

    /**
     * Creates a new Lore component with the specified lines.
     * @param lines The lines of text to display.
     */
    public LoreComponent(TextComponent... lines) {
        if (lines != null) {
            Collections.addAll(this.lines, lines);
        }
    }

    /**
     * Adds an additional line of lore to the existing list.
     * @param line The text component to add.
     * @return This component instance for chaining.
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
     * Converts the lore lines into a {@link ListTag} of {@link StringTag}s.
     * Each StringTag contains the JSON representation of the TextComponent.
     * * @return An {@link NBTTag} (specifically a ListTag) ready for the components map.
     */
    @Override
    public NBTTag toNbt() {
        // Create the ListTag named with the namespaced ID (minecraft:lore)
        ListTag list = new ListTag(getId().getResourceLocation());

        for (TextComponent line : lines) {
            // Minecraft lore expects a list of JSON-formatted strings.
            // StringTag name is empty inside a ListTag.
            list.add(new StringTag("", line.toString()));
        }

        return list;
    }

    /**
     * @return An unmodifiable view of the lore lines.
     */
    public List<TextComponent> getLines() {
        return Collections.unmodifiableList(lines);
    }
}