package arguments.itemstack;

import arguments.itemstack.components.EnchantmentsComponent;
import arguments.itemstack.components.InstrumentComponent;
import arguments.itemstack.components.UseCooldownComponent;
import shared.ItemId;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Concrete implementation for the Goat Horn item stack.
 */
public class GoatHornItemStack implements ItemStack {
    private final List<ItemComponentTag> components;
    private static final ItemId ITEM_ID = ItemId.GOAT_HORN;

    /**
     * Creates a Goat Horn ItemStack with a variable number of components.
     */
    private GoatHornItemStack(ItemComponentTag... components) {
        this.components = Arrays.asList(components);
    }

    public static GoatHornItemStack create(ItemComponentTag... components) {
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

            for (ItemComponentTag component : components) {
                String componentString = component.buildComponentString();
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