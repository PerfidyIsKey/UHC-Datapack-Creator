package uhc.data.nbt.entity.dynamic_tiles;

import uhc.data.nbt.blockentity.state.BlockState;
import uhc.data.nbt.blockentity.BlockEntityNBT;
import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.*;
import uhc.resource.block.BlockIdentifier;

import java.util.Objects;

/**
 * 🧱 **Falling Block NBT Builder**
 * <p>
 * This specialized builder manages the NBT structure for {@code falling_block} entities.
 * It controls the block's visual identity, the state properties (like rotation),
 * and the complex data stored within it (like the contents of a falling chest).
 * </p>
 * <p><b>NBT Structure:</b> Manages tags such as {@code BlockState}, {@code TileEntityData},
 * and various landing/damage parameters.</p>
 */
public class FallingBlockNBT extends EntityNBT<FallingBlockNBT> {

    // --- 🏗️ Constructors & Factories ---

    /**
     * Internal constructor for the FallingBlock builder.
     * @param root The root {@link CompoundTag} to store entity data.
     */
    private FallingBlockNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Falling Block NBT builder with an empty root tag.
     * @return A fresh instance of {@link FallingBlockNBT}.
     */
    public static FallingBlockNBT create() {
        return new FallingBlockNBT(CompoundTag.create());
    }

    // --- 🛰️ Core Contract Implementation ---

    /**
     * Retrieves the root compound tag representing the entity's data.
     * @return The underlying {@link CompoundTag}.
     */
    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 🧊 Block Definition ---

    /**
     * Sets the physical block identity and its associated state properties.
     * <p><b>Error Catching:</b> Strictly validates that the {@link BlockIdentifier} is non-null.
     * It also filters null entries from the {@code states} varargs to prevent malformed
     * NBT strings that would cause the client to crash or render the block as purple/black.</p>
     * * @param blockId The specific {@link BlockIdentifier} (e.g., BlockId.SAND).
     * @param states  Optional {@link BlockState} objects (e.g., FacingBlockState.NORTH).
     * @return This builder instance for fluent chaining.
     * @throws NullPointerException if blockId is null.
     */
    public FallingBlockNBT blockState(BlockIdentifier blockId, BlockState... states) {
        Objects.requireNonNull(blockId, "Block ID cannot be null for FallingBlock.");

        CompoundTag blockStateTag = CompoundTag.create("BlockState");

        // Pass the BlockIdentifier object's resource location
        blockStateTag.put(new StringTag("Name", blockId.getResourceLocation()));

        // Only create the Properties compound if states are actually provided
        if (states != null && states.length > 0) {
            CompoundTag properties = CompoundTag.create("Properties");
            boolean added = false;

            for (BlockState state : states) {
                if (state != null) {
                    // Extract data from the BlockState object
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
     * Sets the Tile Entity (Block Entity) data, applied when the block lands.
     * <p><b>Error Catching:</b> If the provided builder is null, any existing
     * TileEntityData is safely removed from the root to prevent data corruption.</p>
     * * @param blockEntity The {@link BlockEntityNBT} builder (e.g., ChestNBT, SpawnerNBT).
     * @return This builder instance.
     */
    public FallingBlockNBT tileEntityData(BlockEntityNBT<?> blockEntity) {
        if (blockEntity == null) {
            root().remove("TileEntityData");
        } else {
            try {
                // Build the data from the rich BlockEntityNBT object
                CompoundTag nbt = blockEntity.build();
                // Ensure the tag is named correctly for the Falling Block schema
                nbt.setName("TileEntityData");
                root().put(nbt);
            } catch (Exception e) {
                // Catch potential build errors from internal NBT logic
                throw new IllegalStateException("Failed to assemble TileEntityData for FallingBlock.", e);
            }
        }
        return this;
    }

    // --- 🛠️ Landing & Lifecycle Logic ---

    /**
     * Determines if the block should vanish instead of dropping as an item
     * if it cannot be placed (e.g., falling onto a torch).
     * @param cancel True to prevent the item drop.
     * @return This builder instance.
     */
    public FallingBlockNBT cancelDrop(boolean cancel) {
        root().put(new ByteTag("CancelDrop", (byte) (cancel ? 1 : 0)));
        return this;
    }

    /**
     * Determines if the block drops as an item when it breaks.
     * @param drop True to allow item dropping.
     * @return This builder instance.
     */
    public FallingBlockNBT dropItem(boolean drop) {
        root().put(new ByteTag("DropItem", (byte) (drop ? 1 : 0)));
        return this;
    }

    /**
     * Sets the age of the falling block entity in ticks.
     * <p><b>Warning:</b> Falling blocks are deleted if age exceeds 600 ticks,
     * or 100 ticks if the block is at a height where it cannot land.</p>
     * @param ticks The elapsed time in ticks.
     * @return This builder instance.
     */
    public FallingBlockNBT time(int ticks) {
        root().put(new IntTag("Time", ticks));
        return this;
    }

    // --- ⚔️ Combat & Environmental Damage ---

    /**
     * Sets whether this block should deal damage to entities it falls through.
     * @param hurt True to enable entity damage (standard for Anvils).
     * @return This builder instance.
     */
    public FallingBlockNBT hurtEntities(boolean hurt) {
        root().put(new ByteTag("HurtEntities", (byte) (hurt ? 1 : 0)));
        return this;
    }

    /**
     * Sets the amount of damage dealt per block fallen.
     * <p>Examples: Anvils (2.0), Pointed Dripstone (6.0).</p>
     * @param amount The damage multiplier.
     * @return This builder instance.
     */
    public FallingBlockNBT fallHurtAmount(float amount) {
        root().put(new FloatTag("FallHurtAmount", amount));
        return this;
    }

    /**
     * Sets the maximum cap for damage inflicted by this block.
     * @param max The maximum damage (usually 40 for 20 hearts).
     * @return This builder instance.
     */
    public FallingBlockNBT fallHurtMax(int max) {
        root().put(new IntTag("FallHurtMax", max));
        return this;
    }
}