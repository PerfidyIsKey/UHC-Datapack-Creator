package uhc.data.nbt.entity.other;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.CompoundTag;

/**
 * 📍 **Marker Entity NBT Builder**
 * <p>
 * Specialized builder for 'minecraft:marker' entities.
 * Used for storing technical data at specific locations without performance cost.
 * </p>
 */
public class MarkerNBT extends EntityNBT<MarkerNBT> {

    private MarkerNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Marker NBT builder.
     */
    public static MarkerNBT create() {
        return new MarkerNBT(CompoundTag.create());
    }

    /**
     * Markers are unique because they have a dedicated 'data' tag
     * specifically meant for developers to store custom NBT.
     * * @param data The custom data compound.
     * @return This builder instance.
     */
    public MarkerNBT data(CompoundTag data) {
        data.setName("data");
        root().put(data);
        return this;
    }

    // Overriding base methods to ensure the fluent API doesn't break
    @Override
    public MarkerNBT glowing(boolean glowing) {
        super.glowing(glowing);
        return this;
    }
}