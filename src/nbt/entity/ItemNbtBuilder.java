package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.ItemData;
import shared.EntityType;
import java.util.Map;
import nbt.NBTTag;
import shared.ItemComponentType;

/**
 * Factory/Builder for the Item Entity NBT structure.
 * Item entity NBT is simple: {Item:{id:<string>, count:<int>, components:{...}}}
 */
public class ItemNbtBuilder implements EntityNbtBuilder {

    private final ItemData data;
    private final BaseEntityNbt baseNbt;

    private ItemNbtBuilder(ItemData data) {
        this.data = data;
        // Use ITEM entity type for the base NBT (though no common properties are expected)
        this.baseNbt = BaseEntityNbt.create(EntityType.ITEM, null);
    }

    public static ItemNbtBuilder create(ItemData data) {
        return new ItemNbtBuilder(data);
    }

    @Override
    public CompoundTag buildNbt() {
        CompoundTag rootNbt = baseNbt.buildNbt();
        CompoundTag itemTag = new CompoundTag("Item");

        // 1. Item ID and Count
        itemTag.put(new StringTag("id", data.id().getResourceLocation()));
        itemTag.put(new IntTag("count", data.count()));

        // 2. Components Map: {"components": {...}}
        if (!data.components().isEmpty()) {
            CompoundTag componentsMap = new CompoundTag("components");

            for (Map.Entry<ItemComponentType, CompoundTag> entry : data.components().entrySet()) {

                // Retrieve the actual string resource location from the enum key
                String componentKey = entry.getKey().getResourceLocation();

                // Build the named tag (e.g., "minecraft:profile": {...})
                CompoundTag namedComponentTag = createNamedCompoundTag(componentKey, entry.getValue());
                componentsMap.put(namedComponentTag);
            }
            itemTag.put(componentsMap);
        }

        // 3. Finalize the root entity tag by adding the Item tag
        rootNbt.put(itemTag);

        return rootNbt;
    }

    /**
     * Helper to create a new CompoundTag with the correct name (key)
     * by copying the contents from an existing unnamed CompoundTag (the value).
     * This is necessary because CompoundTag only supports put(Tag tag).
     */
    private CompoundTag createNamedCompoundTag(String name, CompoundTag source) {
        // Create a new CompoundTag with the desired name (e.g., "minecraft:profile")
        CompoundTag namedTag = new CompoundTag(name);

        // Copy all inner tags (e.g., the 'name' tag for the player profile) from the source
        // Assuming CompoundTag.getValue() returns the internal Map<String, Tag> used by TagConverter
        for (Map.Entry<String, NBTTag> innerEntry : source.getValue().entrySet()) {
            // The inner tags are already correctly named, so we put them directly.
            namedTag.put(innerEntry.getValue());
        }
        return namedTag;
    }
}