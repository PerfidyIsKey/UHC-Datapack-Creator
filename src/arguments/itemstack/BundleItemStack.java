package arguments.itemstack;

import shared.item.DyeColor;
import shared.item.ItemId;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class BundleItemStack implements ItemStack {
    private final DyeColor color;
    private final List<ItemComponentTag> components;
    private static final String BASE_NAME = "bundle"; // Base item name is 'bundle'

    private BundleItemStack(DyeColor color, ItemComponentTag... components) {
        this.color = color;
        this.components = Arrays.asList(components);
    }

    public static BundleItemStack create(DyeColor color, ItemComponentTag... components) {
        return new BundleItemStack(color, components);
    }

    @Override
    public ItemId getItemId() {
        // Return a placeholder or throw error if ItemId enum is strictly required elsewhere
        // For command building, we just need the resource location string (next method).
        throw new UnsupportedOperationException("BundleItemStack does not use a single static ItemId enum value.");
    }

    // Key method: Generates the full resource location string, e.g., "minecraft:yellow_bundle"
    public String getResourceLocation() {
        return "minecraft:" + color.toString() + "_" + BASE_NAME;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder(getResourceLocation());

        if (!components.isEmpty()) {
            StringJoiner componentsJoiner = new StringJoiner(",", "[", "]");

            for (ItemComponentTag component : components) {
                String componentString = component.buildComponentString();
                if (!componentString.isEmpty()) {
                    componentsJoiner.add(componentString);
                }
            }
            sb.append(componentsJoiner);
        }

        return sb.toString();
    }
}