package nbt.item;

import nbt.tags.CompoundTag;
import nbt.tags.StringTag;
import nbt.entity.data.PlayerProfileComponentData;

/**
 * Builds the nested NBT structure for the "minecraft:profile" item component:
 * {name:<string>}
 */
public class PlayerProfileComponentBuilder {

    /**
     * Builds the content of the "minecraft:profile" component.
     */
    public static CompoundTag build(PlayerProfileComponentData data) {
        CompoundTag profileData = CompoundTag.create();

        // The component requires a "name" tag
        profileData.put(new StringTag("name", data.playerName()));

        return profileData;
    }
}