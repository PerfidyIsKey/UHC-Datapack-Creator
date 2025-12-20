package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.ByteTag;
import uhc.data.nbt.tags.CompoundTag;
import uhc.resource.item.components.ComponentId;

/**
 * 🛡️ **Unbreakable Component Implementation**
 * <p>
 * Manages the "minecraft:unbreakable" data component (1.20.5+).
 * When applied to an item, it prevents durability loss from use, attacks, or breaking blocks.
 * </p>
 */
public class UnbreakableComponent implements ItemComponent {

    private final boolean showInTooltip;

    /**
     * Private constructor for the unbreakable component.
     * @param showInTooltip Whether the "Unbreakable" line appears in the item's tooltip.
     */
    private UnbreakableComponent(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    /**
     * Creates an Unbreakable component that displays in the tooltip.
     * @return A new instance of UnbreakableComponent.
     */
    public static UnbreakableComponent create() {
        return new UnbreakableComponent(true);
    }

    /**
     * Creates an Unbreakable component with custom tooltip visibility.
     * @param showInTooltip If true, displays "Unbreakable"; if false, the effect is hidden.
     * @return A new instance of UnbreakableComponent.
     */
    public static UnbreakableComponent create(Boolean showInTooltip) {
        // Error Catch: Handle potential null from wrapper Boolean
        return new UnbreakableComponent(showInTooltip != null ? showInTooltip : true);
    }

    @Override
    public ComponentId getId() {
        return ComponentId.UNBREAKABLE;
    }

    /**
     * Converts the component into the modern Compound NBT structure.
     * <p>
     * <b>NBT Representation:</b>
     * <pre>
     * "minecraft:unbreakable": {
     * "show_in_tooltip": 1b
     * }
     * </pre>
     * </p>
     * @return A {@link CompoundTag} named with the component's resource location.
     */
    @Override
    public NBTTag toNbt() {
        // Root is named "minecraft:unbreakable"
        CompoundTag tag = CompoundTag.create(getId().getResourceLocation());

        // Value is a byte (1 for true, 0 for false)
        tag.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));

        return tag;
    }

    /**
     * @return The current visibility state of the unbreakable tooltip.
     */
    public boolean isShowInTooltip() {
        return showInTooltip;
    }
}