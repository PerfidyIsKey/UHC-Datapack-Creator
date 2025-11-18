package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.ItemData;
import shared.EntityType;
import java.util.Map;
import nbt.NBTTag;

/**
 * Factory/Builder for the Item Entity NBT structure.
 * Item entity NBT is simple: {Item:{id:<string>, count:<int>, components:{...}}}
 */
public class ItemNbtBuilder implements EntityNbtBuilder {

    private final ItemData data;
    private final BaseEntityNbt baseNbt;

    public ItemNbtBuilder(ItemData data) {
        this.data = data;
        // Use ITEM entity type for the base NBT (though no common properties are expected)
        this.baseNbt = new BaseEntityNbt(EntityType.ITEM, null);
    }

    @Override
    public CompoundTag buildNbt() {
        // Start with the base NBT (which is currently just an empty compound tag)
        CompoundTag rootNbt = baseNbt.buildNbt();

        // The root NBT must contain the 'Item' compound tag
        CompoundTag itemTag = new CompoundTag("Item");

        // 1. Item ID and Count
        itemTag.put(new StringTag("id", data.itemId()));
        itemTag.put(new IntTag("count", data.count()));

        // 2. Components Map: {"components": {...}}
        if (!data.components().isEmpty()) {
            CompoundTag componentsMap = new CompoundTag("components");

            // Iterate over the components map (key is "minecraft:profile", value is the component data NBT)
            for (Map.Entry<String, CompoundTag> entry : data.components().entrySet()) {
                CompoundTag namedComponentTag = createNamedCompoundTag(entry.getKey(), entry.getValue());
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