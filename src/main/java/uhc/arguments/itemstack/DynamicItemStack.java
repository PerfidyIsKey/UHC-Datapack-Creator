package uhc.arguments.itemstack;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.ItemId;

import java.util.Objects;

/**
 * Concrete implementation of ItemStack used for dynamic item IDs, such as
 * those generated for armor and tools, where the ID is known as a String
 * but does not correspond to a specific ItemId enum constant.
 * This class ensures type safety by still requiring an ItemStack interface,
 * while allowing for string-based resource locations.
 */
public class DynamicItemStack implements ItemStack {
    private final String resourceLocation;

    /** * The raw string representation of all data components, formatted as
     * "key1=value1,key2=value2" (e.g., "minecraft:damage=10, \"minecraft:enchantments\"=[{...}]").
     * This field holds the content that goes inside the item stack's brackets [].
     */
    private final String componentString;

    /**
     * Creates a dynamic ItemStack without components.
     * @param resourceLocation The full Minecraft resource location (e.g., "minecraft:iron_chestplate").
     */
    private DynamicItemStack(String resourceLocation) {
        this(resourceLocation, null);
    }

    /**
     * Creates a dynamic ItemStack with optional data components formatted as a component string.
     * @param resourceLocation The full Minecraft resource location.
     * @param componentString The raw, formatted component string (e.g., {@code "minecraft:potion_contents"={...}}).
     */
    private DynamicItemStack(String resourceLocation, String componentString) {
        if (resourceLocation == null || resourceLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource location cannot be null or empty.");
        }
        this.resourceLocation = resourceLocation;
        this.componentString = componentString;
    }

    public static DynamicItemStack create(String resourceLocation) {
        return new DynamicItemStack(resourceLocation);
    }

    /**
     * Creates a dynamic ItemStack from a single component's CompoundTag.
     * The CompoundTag must be named with the component's ID (e.g., "minecraft:potion_contents").
     * * @param resourceLocation The full Minecraft resource location.
     * @param componentTag The CompoundTag containing the single component's data.
     * Its NAME is used as the component KEY in the final string.
     * @return A new DynamicItemStack instance.
     */
    public static DynamicItemStack create(String resourceLocation, CompoundTag componentTag) {
        // The component tag MUST be named to identify the component ID.
        String componentKey = Objects.requireNonNull(componentTag.getName(),
                "Component tag must have a name (the component ID).");

        if (componentKey.isEmpty()) {
            throw new IllegalArgumentException("Component tag name cannot be empty.");
        }

        // --- 1. Get the SNBT string of the component's VALUE (the CompoundTag's contents) ---

        // To get only the content (e.g., {potion:"..."}) and ignore the tag's name
        // during TagConverter.toJson, we temporarily clear the name.
        String originalName = componentTag.getName();
        componentTag.setName("");

        // This yields the SNBT value, e.g., {potion:"minecraft:strong_strength"}
        String componentValueSNBT = TagConverter.toJson(componentTag);

        // Restore the name (good practice if the object is used elsewhere)
        componentTag.setName(originalName);

        // --- 2. Format the KEY and combine into "key=value" ---

        // FIX: Component keys in the item stack format (item[key=value]) should use the raw resource location
        // without quoting, even if they contain a namespace.
        String formattedKey = componentKey;

        // Combine into the required item stack format: "key=value"
        String componentString = formattedKey + "=" + componentValueSNBT;

        return new DynamicItemStack(resourceLocation, componentString);
    }

    @Override
    public ItemId getItemId() {
        // This item stack does not map to a fixed ItemId constant, so we return null.
        return null;
    }

    /**
     * Returns the full item stack string in the format: item_id[component_key=component_value].
     */
    @Override
    public String build() {
        if (componentString == null || componentString.trim().isEmpty()) {
            return resourceLocation;
        }
        // Minecraft 1.20.5+ uses data components in [] brackets.
        return resourceLocation + "[" + componentString + "]";
    }
}