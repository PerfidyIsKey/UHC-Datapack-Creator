package uhc.data.nbt.entity.dynamic_tiles;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.*;
import java.util.Objects;

/**
 * 🧱 **Falling Block NBT Builder**
 * <p>
 * Specialized builder for Falling Block entities. Manages block states,
 * landing behavior, and entity damage (e.g., falling anvils).
 * </p>
 */
public class FallingBlockNBT extends EntityNBT<FallingBlockNBT> {

    private FallingBlockNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Falling Block NBT builder.
     * @return A new instance of FallingBlockNBT.
     */
    public static FallingBlockNBT create() {
        return new FallingBlockNBT(CompoundTag.create());
    }

    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 🧊 Block Definition ---

    /**
     * Sets the block state this entity represents.
     * @param blockId The resource location (e.g., "minecraft:anvil").
     * @param properties Optional properties compound (e.g., { "facing": "north" }).
     * @return This builder instance.
     */
    public FallingBlockNBT blockState(String blockId, CompoundTag properties) {
        Objects.requireNonNull(blockId, "Block ID cannot be null.");
        CompoundTag state = CompoundTag.create("BlockState");
        state.put(new StringTag("Name", blockId));

        if (properties != null) {
            properties.setName("Properties");
            state.put(properties);
        }

        root().put(state);
        return this;
    }

    /**
     * Sets the Tile Entity (Block Entity) data for the block.
     * @param nbt The NBT data to apply to the block once it lands.
     * @return This builder instance.
     */
    public FallingBlockNBT tileEntityData(CompoundTag nbt) {
        if (nbt == null) {
            root().remove("TileEntityData");
        } else {
            nbt.setName("TileEntityData");
            root().put(nbt);
        }
        return this;
    }

    // --- 🛠️ Landing Logic ---

    public FallingBlockNBT cancelDrop(boolean cancel) {
        root().put(new ByteTag("CancelDrop", (byte) (cancel ? 1 : 0)));
        return this;
    }

    public FallingBlockNBT dropItem(boolean drop) {
        root().put(new ByteTag("DropItem", (byte) (drop ? 1 : 0)));
        return this;
    }

    /**
     * Sets the age of the entity in ticks.
     * <p>
     * <b>Note:</b> If this exceeds 600, or 100 while outside build limits,
     * the entity is deleted.
     * </p>
     */
    public FallingBlockNBT time(int ticks) {
        root().put(new IntTag("Time", ticks));
        return this;
    }

    // --- ⚔️ Combat & Damage ---

    public FallingBlockNBT hurtEntities(boolean hurt) {
        root().put(new ByteTag("HurtEntities", (byte) (hurt ? 1 : 0)));
        return this;
    }

    /**
     * Sets the damage multiplier based on fall distance.
     * @param amount Default: Anvils = 2.0, Dripstone = 6.0.
     */
    public FallingBlockNBT fallHurtAmount(float amount) {
        root().put(new FloatTag("FallHurtAmount", amount));
        return this;
    }

    /**
     * Sets the maximum damage this block can inflict.
     * @param max Default: 40 (usually stored as points, i.e., 40HP).
     */
    public FallingBlockNBT fallHurtMax(int max) {
        root().put(new IntTag("FallHurtMax", max));
        return this;
    }
}