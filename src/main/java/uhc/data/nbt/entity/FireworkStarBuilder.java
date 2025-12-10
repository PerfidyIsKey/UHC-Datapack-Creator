package uhc.data.nbt.entity;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.ByteTag;
import uhc.data.nbt.tags.IntArrayTag;
import uhc.data.nbt.entity.data.FireworkStarData;

/**
 * Builds the NBT CompoundTag for a single firework star explosion from a FireworkStarData object.
 */
public class FireworkStarBuilder {

    // Static factory method to perform the conversion
    public static CompoundTag build(FireworkStarData data) {
        CompoundTag star = CompoundTag.create();

        // Type is type-safe via enum
        star.put(new ByteTag("Type", data.getShape().getValue()));

        // Convert List<Integer> to int[] for IntArrayTag
        int[] colorArray = data.getColors().stream().mapToInt(i -> i).toArray();
        star.put(new IntArrayTag("Colors", colorArray));

        // Optional boolean properties (ByteTags)
        if (data.isTrail()) {
            star.put(new ByteTag("Trail", (byte)1));
        }
        if (data.isFlicker()) {
            star.put(new ByteTag("Flicker", (byte)1));
        }

        return star;
    }
}