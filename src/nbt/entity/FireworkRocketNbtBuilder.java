package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.FireworkRocketData;
import nbt.item.FireworksComponentBuilder;
import shared.EntityType;
import shared.BooleanNbtProperty;
import java.util.Map;

/**
 * Factory/Builder for the Firework Rocket Entity NBT structure, consuming a FireworkRocketData DTO.
 * This version uses the single-argument put(Tag) method for nesting CompoundTags, requiring
 * that the nested tags (like the components map and firework component data) carry their own names.
 */
public class FireworkRocketNbtBuilder implements EntityNbtBuilder {

    private final FireworkRocketData data;
    // Assuming BaseEntityNbt exists
    private final BaseEntityNbt baseNbt;

    private FireworkRocketNbtBuilder(FireworkRocketData data) {
        this.data = data;
        this.baseNbt = BaseEntityNbt.create(EntityType.FIREWORK_ROCKET, null);
    }

    public static FireworkRocketNbtBuilder create(FireworkRocketData data) {
        return new FireworkRocketNbtBuilder(data);
    }

    @Override
    public CompoundTag buildNbt() {

        // 1. Start with Base Entity NBT (id, Glowing:1b, etc.)
        for (Map.Entry<BooleanNbtProperty, Boolean> entry : data.getBooleanProperties().entrySet()) {
            if (entry.getValue()) {
                baseNbt.setBooleanProperty(entry.getKey(), true);
            }
        }
        CompoundTag rootNbt = baseNbt.buildNbt();

        // 2. Build the Item Components Map: {"components": {"minecraft:fireworks": {...}}}

        // This CompoundTag is named "components"
        CompoundTag componentsMap = CompoundTag.create("components");

        // This tag is assumed to be named "minecraft:fireworks" inside the builder
        CompoundTag fireworksComponentData = FireworksComponentBuilder.build(
                data.getFlightDuration(),
                data.getExplosions()
        );

        // The nested tag (fireworksComponentData) must carry its own name to be inserted here.
        componentsMap.put(fireworksComponentData);


        // 3. Build the "FireworksItem" tag
        // This CompoundTag is named "FireworksItem"
        CompoundTag fireworksItem = CompoundTag.create("FireworksItem");

        fireworksItem.put(new StringTag("id", EntityType.FIREWORK_ROCKET.getResourceLocation()));
        fireworksItem.put(new IntTag("count", 1));
        fireworksItem.put(componentsMap); // Attach the components map


        // 4. Finalize the root entity tag
        rootNbt.put(new IntTag("LifeTime", data.getFlightDuration() * 20));
        rootNbt.put(new IntTag("Life", 0));
        rootNbt.put(fireworksItem);

        return rootNbt;
    }
}