package uhc.arguments.itemstack;

import uhc.data.nbt.util.TagConverter;
import uhc.resource.item.ItemId;
import uhc.data.nbt.item.components.ItemComponent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Concrete implementation for the Goat Horn item stack.
 */
public class GoatHornItemStack implements ItemStack {
    private final List<ItemComponent> components;
    private static final ItemId ITEM_ID = ItemId.GOAT_HORN;

    /**
     * Creates a Goat Horn ItemStack with a variable number of components.
     */
    private GoatHornItemStack(ItemComponent... components) {
        this.components = Arrays.asList(components);
    }

    public static GoatHornItemStack create(ItemComponent... components) {
        return new GoatHornItemStack(components);
    }

    @Override
    public ItemId getItemId() {
        return ITEM_ID;
    }

    /**
     * Builds the full item stack string, including the item ID and components.
     * Example: minecraft:goat_horn[instrument="...",use_cooldown={...}]
     */
    @Override
    public String build() {
        StringBuilder sb = new StringBuilder(ITEM_ID.getResourceLocation());

        if (!components.isEmpty()) {
            StringJoiner componentsJoiner = new StringJoiner(",", "[", "]");

            for (ItemComponent component : components) {
                String componentString = TagConverter.toJson(component.toNbt());
                // Ensure we don't add empty strings if a component builder returns one
                if (!componentString.isEmpty()) {
                    componentsJoiner.add(componentString);
                }
            }

            // Only append components block if there's content inside
            if (componentsJoiner.length() > 2) { // 2 characters are from [ and ]
                sb.append(componentsJoiner.toString());
            }
        }

        return sb.toString();
    }
}