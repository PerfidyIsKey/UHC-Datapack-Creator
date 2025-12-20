package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.ByteTag;
import uhc.data.nbt.tags.CompoundTag;
import uhc.resource.item.components.ComponentId;

/**
 * 🛡️ **Unbreakable Component Implementation**
 * <p>
 * Manages the "minecraft:unbreakable" data component.
 * When applied, the item will not lose durability when used.
 * </p>
 */
public class UnbreakableComponent implements ItemComponent {
    private final boolean showInTooltip;

    /**
     * Creates an Unbreakable component.
     * @param showInTooltip If true, "Unbreakable" will appear in the item's tooltip.
     */
    public UnbreakableComponent(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    /**
     * Creates an Unbreakable component that shows in the tooltip by default.
     */
    public UnbreakableComponent() {
        this(true);
    }

    @Override
    public ComponentId getId() {
        return ComponentId.UNBREAKABLE;
    }

    /**
     * Converts the component into the required NBT structure.
     * <p>
     * <b>Format:</b> {@code "minecraft:unbreakable": {show_in_tooltip: 1b}}
     * </p>
     * @return A named {@link CompoundTag} representing the component.
     */
    @Override
    public NBTTag toNbt() {
        // The root of the component must be named with the full resource location.
        CompoundTag tag = CompoundTag.create(getId().getResourceLocation());

        // Minecraft 1.20.5+ expects a byte tag for the tooltip toggle.
        tag.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));

        return tag;
    }

    public boolean isShowInTooltip() {
        return showInTooltip;
    }
}