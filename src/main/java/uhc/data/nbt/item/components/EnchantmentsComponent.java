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
 * Manages the "minecraft:enchantments" data component (1.20.5+).
 * Maps enchantment resource locations to their respective levels and
 * controls tooltip visibility.
 * </p>
 * <p>
 * <b>Note:</b> This is for active items (Swords, Armor). For Enchanted Books,
 * use the stored_enchantments component.
 * </p>
 */
public class EnchantmentsComponent implements ItemComponent {

    private final Map<String, Integer> enchantments = new HashMap<>();
    private boolean showInTooltip = true;

    private EnchantmentsComponent() {}

    /**
     * Creates a new EnchantmentsComponent instance.
     * @return A new builder instance.
     */
    public static EnchantmentsComponent create() {
        return new EnchantmentsComponent();
    }

    /**
     * Adds an enchantment or updates its level.
     * <p>If the level is 0 or less, the enchantment is removed from the item.</p>
     * @param id The type-safe enchantment ID from the registry.
     * @param level The enchantment level (e.g., 5 for Sharpness V).
     * @return This component instance for fluent chaining.
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
     * Toggles whether the enchantments list is visible in the item's tooltip.
     * @param show True (default) to show, false to hide (equivalent to old HideFlags).
     * @return This component instance for fluent chaining.
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
     * Converts the component into modern NBT.
     * <p>
     * <b>NBT Structure:</b>
     * <pre>
     * {
     * "minecraft:enchantments": {
     * "levels": {
     * "minecraft:sharpness": 5,
     * "minecraft:unbreaking": 3
     * },
     * "show_in_tooltip": 1b
     * }
     * }
     * </pre>
     * </p>
     */
    @Override
    public NBTTag toNbt() {
        CompoundTag root = CompoundTag.create(getId().getResourceLocation());

        CompoundTag levels = CompoundTag.create("levels");
        for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            levels.put(new IntTag(entry.getKey(), entry.getValue()));
        }

        root.put(levels);
        root.put(new ByteTag("show_in_tooltip", (byte) (showInTooltip ? 1 : 0)));

        return root;
    }

    /**
     * Checks if a specific enchantment is present.
     * @param id The enchantment to check.
     * @return true if present.
     */
    public boolean hasEnchantment(EnchantmentId id) {
        return enchantments.containsKey(id.getResourceLocation());
    }
}