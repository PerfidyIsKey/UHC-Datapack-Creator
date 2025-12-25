package uhc.data.nbt.entity.dynamic_tiles;

import uhc.data.nbt.blockentity.state.BlockState;
import uhc.data.nbt.blockentity.BlockEntityNBT;
import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.*;

import java.util.Objects;

/**
 * 🧱 **Falling Block NBT Builder**
 * <p>
 * Specialized builder for Falling Block entities. Manages block states,
 * landing behavior, and entity data (e.g., contents of a falling chest).
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
     * Sets the block identity. State properties are optional.
     * <p>
     * <b>Usage:</b><br>
     * {@code blockState(BlockId.SAND)} // Simple block<br>
     * {@code blockState(BlockId.OAK_STAIRS, FacingBlockState.NORTH)} // With state
     * </p>
     * @param blockId The block type.
     * @param states  Optional property descriptors (e.g., facing, lit, powered).
     * @return This builder instance.
     */
    public FallingBlockNBT blockState(BlockId blockId, BlockState... states) {
        Objects.requireNonNull(blockId, "Block ID cannot be null.");

        CompoundTag blockStateTag = CompoundTag.create("BlockState");
        blockStateTag.put(new StringTag("Name", blockId.getResourceLocation()));

        // Only create the Properties compound if states are actually provided
        if (states != null && states.length > 0) {
            CompoundTag properties = CompoundTag.create("Properties");
            boolean added = false;

            for (BlockState state : states) {
                if (state != null) {
                    properties.put(new StringTag(state.getKey(), state.getValue()));
                    added = true;
                }
            }

            // Avoid adding an empty "Properties: {}" tag if all varargs were null
            if (added) {
                blockStateTag.put(properties);
            }
        }

        root().put(blockStateTag);
        return this;
    }

    /**
     * Sets the Tile Entity (Block Entity) data for the block.
     * <p>
     * <b>Catch:</b> This data is applied to the block when it lands.
     * Useful for falling chests, spawners, or banners.
     * </p>
     * @param blockEntity The builder containing the block's internal NBT data.
     * @return This builder instance.
     */
    public FallingBlockNBT tileEntityData(BlockEntityNBT<?> blockEntity) {
        if (blockEntity == null) {
            root().remove("TileEntityData");
        } else {
            // Retrieve the built CompoundTag from the BlockEntityNBT builder
            CompoundTag nbt = blockEntity.build();
            // Ensure the tag is named correctly for the Falling Block NBT structure
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