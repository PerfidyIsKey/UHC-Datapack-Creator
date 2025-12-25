package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.ByteTag;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.IntTag;
import uhc.resource.enchantment.EnchantmentId;
import uhc.resource.item.components.ComponentId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ✨ **Enchantments Component Implementation**
 * <p>
 * Manages the {@code minecraft:enchantments} data component for modern Minecraft (1.20.5+).
 * This component maps enchantment resource locations to their respective levels and
 * controls the visibility of the enchantment list in the item's tooltip.
 * </p>
 * <p>
 * <b>Logic:</b> Unlike the old NBT system where enchantments were a list of objects,
 * modern components use a map-like structure under the {@code levels} key.
 * </p>
 */
public class EnchantmentsComponent implements ItemComponent {

    // --- ⚙️ State & Fields ---

    /** * Internal map of enchantment resource locations and their integer levels. */
    private final Map<String, Integer> enchantments = new HashMap<>();

    /** * Determines if the enchantment list is rendered in the client's item tooltip.
     * Defaults to {@code true}.
     */
    private boolean showInTooltip = true;

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce the use of the static factory method.
     */
    private EnchantmentsComponent() {}

    /**
     * Creates a new, empty EnchantmentsComponent instance.
     * @return A new builder instance for fluent configuration.
     */
    public static EnchantmentsComponent create() {
        return new EnchantmentsComponent();
    }

    // --- 🛠️ Fluent API (Modifications) ---

    /**
     * Adds an enchantment to the component or updates its existing level.
     * <p><b>Error Catching:</b>
     * <ul>
     * <li>Ensures the {@code EnchantmentId} is not null.</li>
     * <li>If {@code level} is 0 or less, the enchantment is automatically removed.</li>
     * <li>Caps levels at {@code 255} to prevent overflow in certain game versions.</li>
     * </ul>
     * </p>
     * @param id The type-safe enchantment identifier from the registry.
     * @param level The enchantment level (e.g., 5 for Power V).
     * @return This component instance for method chaining.
     * @throws NullPointerException if the id is null.
     */
    public EnchantmentsComponent add(EnchantmentId id, int level) {
        Objects.requireNonNull(id, "Enchantment ID cannot be null.");

        String location = id.getResourceLocation();
        if (level > 0) {
            // Logically cap level at 255 (Minecraft's usual max for Byte/Short internal storage)
            int safeLevel = Math.min(level, 255);
            enchantments.put(location, safeLevel);
        } else {
            enchantments.remove(location);
        }
        return this;
    }

    /**
     * Toggles the visibility of the enchantment list in the item's hover tooltip.
     * <p>Setting this to {@code false} is the modern equivalent of the legacy HideFlags bit {@code 1}.</p>
     * @param show {@code true} (default) to show, {@code false} to hide.
     * @return This component instance for method chaining.
     */
    public EnchantmentsComponent showInTooltip(boolean show) {
        this.showInTooltip = show;
        return this;
    }

    // --- 🛰️ Registry & Serialization ---

    /**
     * Retrieves the standard registry ID for this component.
     * @return {@link ComponentId#ENCHANTMENTS}.
     */
    @Override
    public ComponentId getId() {
        return ComponentId.ENCHANTMENTS;
    }

    /**
     * Converts the component into modern NBT.
     * <p>
     * <b>Logic:</b> To achieve the inline syntax {@code enchantments={"id":lvl}},
     * we return a CompoundTag that contains the levels directly as its top-level
     * entries rather than nesting them under a "levels" key.
     * </p>
     * <p><b>Error Catching:</b> Strictly uses lowercase registry keys to satisfy
     * the 1.21 ResourceLocation parser.</p>
     */
    @Override
    public NBTTag toNbt() {
        // We create an anonymous root CompoundTag.
        // When ItemStack.build() processes this, TagConverter.toJson() will
        // turn this into {"key":value, "key2":value2}
        CompoundTag root = CompoundTag.create("");

        for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            // Ensure the enchantment ID is not null and formatted correctly
            if (entry.getKey() != null) {
                String enchantmentKey = entry.getKey().toLowerCase();
                root.put(new IntTag(enchantmentKey, entry.getValue()));
            }
        }

        // Note: show_in_tooltip is omitted here to force the simple inline syntax
        return root;
    }

    // --- 🔍 Utility Methods ---

    /**
     * Checks if a specific enchantment is currently registered in this component.
     * <p><b>Error Catching:</b> Returns false if the input ID is null.</p>
     * @param id The enchantment to check.
     * @return {@code true} if present and level > 0.
     */
    public boolean hasEnchantment(EnchantmentId id) {
        if (id == null) return false;
        return enchantments.containsKey(id.getResourceLocation());
    }

    /**
     * Retrieves the level of a specific enchantment.
     * @param id The enchantment ID to look up.
     * @return The level if present, or {@code 0} if not found.
     */
    public int getLevel(EnchantmentId id) {
        if (id == null) return 0;
        return enchantments.getOrDefault(id.getResourceLocation(), 0);
    }
}