package uhc.resource.block.structure_block;

/**
 * Defines the valid modes for a Minecraft Structure Block,
 * used in both NBT tags and BlockState properties.
 */
public enum StructureBlockMode {
    SAVE,
    LOAD,
    CORNER,
    DATA;

    /**
     * Returns the capitalized string value used in the Structure Block NBT tag (e.g., "SAVE").
     */
    public String getNbtValue() {
        return name();
    }

    /**
     * Returns the lowercase string value used in BlockState properties (e.g., "save").
     */
    public String getBlockStateValue() {
        return name().toLowerCase();
    }
}