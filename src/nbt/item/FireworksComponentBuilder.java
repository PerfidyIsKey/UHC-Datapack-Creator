package nbt.item;

import nbt.tags.CompoundTag;
import nbt.tags.StringTag;
import nbt.tags.ByteTag;
import nbt.tags.ListTag;
import nbt.tags.IntArrayTag;
import nbt.entity.data.FireworkStarData;
import java.util.List;

/**
 * Builds the nested NBT structure for the "minecraft:fireworks" item component.
 * This class translates FireworkStarData DTOs into the 1.20.5+ item component structure:
 * {explosions:[{shape:<string>, ...}], flight_duration: <byte>}
 */
public class FireworksComponentBuilder {

    /**
     * Builds the content of the "minecraft:fireworks" component.
     */
    public static CompoundTag build(byte flightDuration, List<FireworkStarData> explosions) {
        CompoundTag fireworksData = CompoundTag.create("minecraft:fireworks");

        // 1. Build the list of explosions (Stars)
        ListTag explosionsList = new ListTag("explosions");

        for (FireworkStarData starData : explosions) {
            CompoundTag explosionTag = CompoundTag.create();

            // shape:<string> (using the updated enum's string value, e.g., "star")
            explosionTag.put(new StringTag("shape", starData.getShape().getComponentValue()));

            // colors:[<int>...]
            int[] colorArray = starData.getColors().stream().mapToInt(i -> i).toArray();
            explosionTag.put(new IntArrayTag("colors", colorArray));

            // trail:<byte> (boolean)
            if (starData.isTrail()) {
                explosionTag.put(new ByteTag("trail", (byte)1));
            }
            // flicker:<byte> (boolean)
            if (starData.isFlicker()) {
                explosionTag.put(new ByteTag("flicker", (byte)1));
            }

            explosionsList.add(explosionTag);
        }

        fireworksData.put(explosionsList);

        // 2. Add flight duration
        fireworksData.put(new ByteTag("flight_duration", flightDuration));

        return fireworksData;
    }
}