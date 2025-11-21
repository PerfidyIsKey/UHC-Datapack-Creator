package arguments.itemstack;

import shared.item.ItemId;

/**
 * Concrete implementation of ItemStack used for dynamic item IDs, such as
 * those generated for armor and tools, where the ID is known as a String
 * but does not correspond to a specific ItemId enum constant.
 * * This class ensures type safety by still requiring an ItemStack interface,
 * while allowing for string-based resource locations.
 */
public class DynamicItemStack implements ItemStack {
    private final String resourceLocation;
    private final String components; // Raw string representation of data components (e.g., "enchantments:[{...}]")

    /**
     * Creates a dynamic ItemStack without components.
     * @param resourceLocation The full Minecraft resource location (e.g., "minecraft:iron_chestplate").
     */
    private DynamicItemStack(String resourceLocation) {
        this(resourceLocation, null);
    }

    /**
     * Creates a dynamic ItemStack with optional data components.
     * @param resourceLocation The full Minecraft resource location.
     * @param components The raw string representation of the data components (e.g., "Damage:10, enchantments:[{...}]").
     */
    private DynamicItemStack(String resourceLocation, String components) {
        if (resourceLocation == null || resourceLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource location cannot be null or empty.");
        }
        this.resourceLocation = resourceLocation;
        this.components = components;
    }

    public static DynamicItemStack create(String resourceLocation) {
        return new DynamicItemStack(resourceLocation);
    }

    public static DynamicItemStack create(String resourceLocation, String components) {
        return new DynamicItemStack(resourceLocation, components);
    }

    @Override
    public ItemId getItemId() {
        // This item stack does not map to a fixed ItemId constant, so we return null.
        return null;
    }

    /**
     * Returns the full item stack string in the format: item_id[data_components].
     */
    @Override
    public String build() {
        if (components == null || components.trim().isEmpty()) {
            return resourceLocation;
        }
        // Minecraft 1.20.5+ uses data components in [] brackets.
        return resourceLocation + "[" + components + "]";
    }
}