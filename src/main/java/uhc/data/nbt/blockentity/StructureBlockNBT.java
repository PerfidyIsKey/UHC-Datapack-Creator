package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.*;
import uhc.resource.block.StaticBlockId;
import uhc.resource.block.structure_block.StructureBlockMode;
import uhc.resource.block.structure_block.StructureMirror;
import uhc.resource.block.structure_block.StructureRotation;

import java.util.Objects;

/**
 * 🏗️ **Structure Block NBT Builder**
 * <p>
 * Specialized builder for Structure Block entities. This class constructs the
 * NBT data required to define a structure block's behavior, including its
 * dimensions, transformation logic (mirror/rotation), and integrity.
 * </p>
 */
public class StructureBlockNBT extends BlockEntityNBT<StructureBlockNBT> {

    /**
     * Private constructor used by the static factory method.
     * @param root The underlying CompoundTag that stores the NBT data.
     */
    private StructureBlockNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Structure Block NBT builder with the default structure_block ID.
     * @return A new instance of StructureBlockNBT.
     */
    public static StructureBlockNBT create() {
        return new StructureBlockNBT(CompoundTag.create())
                .id(StaticBlockId.STRUCTURE_BLOCK);
    }

    // --- 📝 Metadata & Configuration ---

    /**
     * Sets the internal name of the structure.
     * <p>Normally formatted as a resource location, e.g., "minecraft:village/plains/house_1".</p>
     * @param structureName The name of the structure.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT name(String structureName) {
        build().put(new StringTag("name", Objects.requireNonNullElse(structureName, "")));
        return this;
    }

    /**
     * Sets the creator of the structure.
     * @param author The name of the author. Defaults to "?" if null or blank.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT author(String author) {
        String finalAuthor = (author == null || author.isBlank()) ? "?" : author;
        build().put(new StringTag("author", finalAuthor));
        return this;
    }

    /**
     * Sets the functional mode of the structure block.
     * @param mode The {@link StructureBlockMode} (SAVE, LOAD, CORNER, or DATA).
     * @return This builder for fluent chaining.
     * @throws NullPointerException if the mode is null.
     */
    public StructureBlockNBT mode(StructureBlockMode mode) {
        Objects.requireNonNull(mode, "Structure mode cannot be null.");
        build().put(new StringTag("mode", mode.getNbtValue()));
        return this;
    }

    /**
     * Sets custom metadata for 'DATA' mode structure blocks.
     * <p>This is often used for custom functions triggered during world generation.</p>
     * @param metadata The metadata string.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT metadata(String metadata) {
        build().put(new StringTag("metadata", Objects.requireNonNullElse(metadata, "")));
        return this;
    }

    // --- 📏 Dimensions & Offset ---

    /**
     * Sets the relative offset of the structure area from the structure block's position.
     * @param posX Relative X offset (Max range: -48 to 48).
     * @param posY Relative Y offset (Max range: -48 to 48).
     * @param posZ Relative Z offset (Max range: -48 to 48).
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT offset(int posX, int posY, int posZ) {
        build().put(new IntTag("posX", posX));
        build().put(new IntTag("posY", posY));
        build().put(new IntTag("posZ", posZ));
        return this;
    }

    /**
     * Sets the size of the structure area.
     * <p><b>Validation:</b> Dimensions are capped between 0 and 48 as per Minecraft limitations.</p>
     * @param sizeX Width (0-48).
     * @param sizeY Height (0-48).
     * @param sizeZ Depth (0-48).
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT size(int sizeX, int sizeY, int sizeZ) {
        // Error Catching: Enforce Minecraft's 48-block limit
        if (sizeX > 48 || sizeY > 48 || sizeZ > 48) {
            throw new IllegalArgumentException("Structure block size cannot exceed 48 in any dimension.");
        }
        if (sizeX < 0 || sizeY < 0 || sizeZ < 0) {
            throw new IllegalArgumentException("Structure block size cannot be negative.");
        }

        build().put(new IntTag("sizeX", sizeX));
        build().put(new IntTag("sizeY", sizeY));
        build().put(new IntTag("sizeZ", sizeZ));
        return this;
    }

    // --- 🔄 Transform & Logic ---

    /**
     * Sets the mirroring mode for the structure.
     * @param mirror The {@link StructureMirror} (NONE, LEFT_RIGHT, or FRONT_BACK).
     * @return This builder for fluent chaining.
     * @throws NullPointerException if mirror is null.
     */
    public StructureBlockNBT mirror(StructureMirror mirror) {
        Objects.requireNonNull(mirror, "Mirror mode cannot be null.");
        build().put(new StringTag("mirror", mirror.getNbtName()));
        return this;
    }

    /**
     * Sets the rotation for the structure.
     * @param rotation The {@link StructureRotation} (0, 90, 180, 270 degrees).
     * @return This builder for fluent chaining.
     * @throws NullPointerException if rotation is null.
     */
    public StructureBlockNBT rotation(StructureRotation rotation) {
        Objects.requireNonNull(rotation, "Rotation cannot be null.");
        build().put(new StringTag("rotation", rotation.getNbtName()));
        return this;
    }

    /**
     * Determines the percentage of the structure to be placed.
     * @param integrity A float between 0.0 and 1.0. 1.0 is full placement.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT integrity(float integrity) {
        // Error Catching: Clamp value between 0 and 1
        float clampedIntegrity = Math.max(0.0f, Math.min(1.0f, integrity));
        build().put(new FloatTag("integrity", clampedIntegrity));
        return this;
    }

    /**
     * Sets the seed for the integrity randomization logic.
     * @param seed The random seed.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT seed(long seed) {
        build().put(new LongTag("seed", seed));
        return this;
    }

    // --- ⚙️ Flags ---

    /**
     * Sets whether entities (players, mobs, items) within the area should be ignored.
     * @param ignore If true, only blocks are saved/loaded.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT ignoreEntities(boolean ignore) {
        build().put(new ByteTag("ignoreEntities", (byte) (ignore ? 1 : 0)));
        return this;
    }

    /**
     * Sets the current redstone power state of the block.
     * @param powered True if receiving a signal.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT powered(boolean powered) {
        build().put(new ByteTag("powered", (byte) (powered ? 1 : 0)));
        return this;
    }

    /**
     * Toggles the visibility of the white bounding box outline in-game.
     * @param show True to show the outline.
     * @return This builder for fluent chaining.
     */
    public StructureBlockNBT showBoundingBox(boolean show) {
        build().put(new ByteTag("showboundingbox", (byte) (show ? 1 : 0)));
        return this;
    }
}