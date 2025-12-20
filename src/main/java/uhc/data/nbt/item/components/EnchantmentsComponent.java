package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.ByteTag;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.resource.EnchantmentId;
import uhc.resource.item.components.ComponentId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ✨ **Enchantments Component Implementation**
 * <p>
 * Manages the "minecraft:enchantments" data component.
 * Maps enchantment resource locations to their respective levels and
 * controls tooltip visibility.
 * </p>
 */
public class EnchantmentsComponent implements ItemComponent {

    private final Map<String, Integer> enchantments = new HashMap<>();
    private boolean showInTooltip = true;

    public EnchantmentsComponent() {}

    /**
     * Adds an enchantment to the component.
     * @param id The namespaced ID of the enchantment (e.g., "minecraft:sharpness").
     * @param level The enchantment level.
     * @return This component instance for chaining.
     */
    public EnchantmentsComponent add(EnchantmentId id, int level) {
        Objects.requireNonNull(id, "Enchantment ID cannot be null.");
        if (level > 0) {
            enchantments.put(id.getResourceLocation(), level);
        } else {
            enchantments.remove(id.getResourceLocation());
        }
        return this;
    }

    /**
     * Toggles whether the enchantments are visible in the item's tooltip.
     * @param show True to show (default), false to hide.
     * @return This component instance for chaining.
     */
    public EnchantmentsComponent showInTooltip(boolean show) {
        this.showInTooltip = show;
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.ENCHANTMENTS;
    }

    /**
     * Converts the map of enchantments into the required NBT structure.
     * <p>
     * <b>Format:</b>
     * {@code "minecraft:enchantments": {levels: {"minecraft:sharp": 5}, show_in_tooltip: 1b}}
     * </p>
     */
    @Override
    public NBTTag toNbt() {
        CompoundTag root = CompoundTag.create(getId().getResourceLocation());

        // The 'levels' compound holds the actual enchantment data
        CompoundTag levels = CompoundTag.create("levels");
        for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            levels.put(new IntTag(entry.getKey(), entry.getValue()));
        }

        root.put(levels);
        root.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));

        return root;
    }
}