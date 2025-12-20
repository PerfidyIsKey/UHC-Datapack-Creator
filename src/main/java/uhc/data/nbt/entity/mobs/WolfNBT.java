package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.traits.AngerableNBT;
import uhc.data.nbt.entity.traits.BreedableNBT;
import uhc.data.nbt.entity.traits.TamableNBT;
import uhc.data.nbt.tags.*;
import uhc.resource.entity.mobs.wolf.WolfSoundVariant;
import uhc.resource.entity.mobs.wolf.WolfVariant;

import java.util.Objects;

/**
 * 🐺 **Wolf NBT Builder**
 * <p>
 * A comprehensive builder for Wolf entities.
 * Inherits from MobNBT with recursive generics to allow seamless method chaining.
 * </p>
 */
public class WolfNBT extends MobNBT<WolfNBT> implements
        BreedableNBT<WolfNBT>,
        TamableNBT<WolfNBT>,
        AngerableNBT<WolfNBT> {

    private WolfNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Wolf NBT builder.
     */
    public static WolfNBT create() {
        return new WolfNBT(CompoundTag.create());
    }

    /**
     * Bridges the trait interfaces to the root NBT tag.
     */
    @Override
    public CompoundTag root() {
        return build();
    }

    // --- Wolf Specific Data ---

    /**
     * Sets the color of the wolf's collar (0-15).
     * @param colorID The dye color ID. 2 is Magenta.
     */
    public WolfNBT collarColor(int colorID) {
        // Validation: Ensure valid Dye ID range
        byte clamped = (byte) Math.max(0, Math.min(15, colorID));
        root().put(new ByteTag("CollarColor", clamped));
        return this;
    }

    /**
     * Sets the visual breed of the wolf (1.20.5+ feature).
     */
    public WolfNBT variant(WolfVariant variant) {
        Objects.requireNonNull(variant, "Wolf variant cannot be null.");
        root().put(new StringTag("variant", variant.getNamespaceId())); // Assuming enum provides ID
        return this;
    }

    /**
     * Sets specific sound profiles for the entity.
     */
    public WolfNBT soundVariant(WolfSoundVariant soundVariant) {
        Objects.requireNonNull(soundVariant, "Sound variant cannot be null.");
        root().put(new StringTag("sound_variant", soundVariant.getNamespaceId()));
        return this;
    }
}