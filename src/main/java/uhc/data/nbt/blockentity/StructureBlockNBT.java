package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.*;
import uhc.resource.StaticBlockId;
import uhc.resource.block.structure_block.StructureBlockMode;
import uhc.resource.block.structure_block.StructureMirror;
import uhc.resource.block.structure_block.StructureRotation;

import java.util.Objects;

/**
 * 🏗️ **Structure Block NBT Builder**
 * <p>
 * Specialized builder for Structure Block entities.
 * Used to define the behavior of blocks that can capture and place 3D structures.
 * </p>
 */
public class StructureBlockNBT extends BlockEntityNBT<StructureBlockNBT> {

    private StructureBlockNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Structure Block NBT builder.
     * @param x World X coordinate of the block.
     * @param y World Y coordinate of the block.
     * @param z World Z coordinate of the block.
     * @return A new instance of StructureBlockNBT.
     */
    public static StructureBlockNBT create(int x, int y, int z) {
        return new StructureBlockNBT(CompoundTag.create())
                .id(StaticBlockId.STRUCTURE_BLOCK)
                .pos(x, y, z);
    }

    // --- 📝 Metadata & Configuration ---

    /**
     * Sets the internal name of the structure (e.g., "minecraft:village/plains/house_1").
     */
    public StructureBlockNBT name(String structureName) {
        build().put(new StringTag("name", structureName));
        return this;
    }

    /**
     * Sets the creator of the structure.
     * @param author The name of the author (defaults to "?" if null).
     */
    public StructureBlockNBT author(String author) {
        build().put(new StringTag("author", Objects.requireNonNullElse(author, "?")));
        return this;
    }

    /**
     * Sets the functional mode of the structure block.
     * @param mode The {@link StructureBlockMode} (SAVE, LOAD, CORNER, or DATA).
     */
    public StructureBlockNBT mode(StructureBlockMode mode) {
        Objects.requireNonNull(mode, "Structure mode cannot be null.");
        build().put(new StringTag("mode", mode.getNbtValue()));
        return this;
    }

    /**
     * Sets custom metadata for 'DATA' mode structure blocks.
     */
    public StructureBlockNBT metadata(String metadata) {
        build().put(new StringTag("metadata", metadata));
        return this;
    }

    // --- 📏 Dimensions & Offset ---

    /**
     * Sets the relative offset of the structure area from the structure block.
     */
    public StructureBlockNBT offset(int posX, int posY, int posZ) {
        build().put(new IntTag("posX", posX));
        build().put(new IntTag("posY", posY));
        build().put(new IntTag("posZ", posZ));
        return this;
    }

    /**
     * Sets the size of the structure area.
     * @param sizeX Width (Max 48)
     * @param sizeY Height (Max 48)
     * @param sizeZ Depth (Max 48)
     */
    public StructureBlockNBT size(int sizeX, int sizeY, int sizeZ) {
        build().put(new IntTag("sizeX", sizeX));
        build().put(new IntTag("sizeY", sizeY));
        build().put(new IntTag("sizeZ", sizeZ));
        return this;
    }

    // --- 🔄 Transform & Logic ---

    /**
     * Sets the mirroring mode for the structure.
     * @param mirror NONE, LEFT_RIGHT, or FRONT_BACK.
     */
    public StructureBlockNBT mirror(StructureMirror mirror) {
        Objects.requireNonNull(mirror, "Mirror mode cannot be null.");
        build().put(new StringTag("mirror", mirror.getNbtName()));
        return this;
    }

    /**
     * Sets the rotation for the structure.
     * @param rotation NONE, CLOCKWISE_90, CLOCKWISE_180, or COUNTERCLOCKWISE_90.
     */
    public StructureBlockNBT rotation(StructureRotation rotation) {
        Objects.requireNonNull(rotation, "Rotation cannot be null.");
        build().put(new StringTag("rotation", rotation.getNbtName()));
        return this;
    }

    /**
     * Determines how much of the structure is placed (0.0 to 1.0).
     * @param integrity 1.0 for a full structure, lower for a "decayed" look.
     */
    public StructureBlockNBT integrity(float integrity) {
        build().put(new FloatTag("integrity", integrity));
        return this;
    }

    /**
     * Sets the seed for the integrity randomization.
     */
    public StructureBlockNBT seed(long seed) {
        build().put(new LongTag("seed", seed));
        return this;
    }

    // --- ⚙️ Flags ---

    /**
     * When true, entities within the saved/loaded area are ignored.
     */
    public StructureBlockNBT ignoreEntities(boolean ignore) {
        build().put(new ByteTag("ignoreEntities", (byte) (ignore ? 1 : 0)));
        return this;
    }

    /**
     * Defines if the structure block is currently receiving redstone power.
     */
    public StructureBlockNBT powered(boolean powered) {
        build().put(new ByteTag("powered", (byte) (powered ? 1 : 0)));
        return this;
    }

    /**
     * Toggles the visibility of the white bounding box outline.
     */
    public StructureBlockNBT showBoundingBox(boolean show) {
        build().put(new ByteTag("showboundingbox", (byte) (show ? 1 : 0)));
        return this;
    }
}