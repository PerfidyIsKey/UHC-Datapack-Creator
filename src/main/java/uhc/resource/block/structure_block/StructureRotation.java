package uhc.resource.block.structure_block;

/**
 * Valid values for the 'rotation' tag in a Structure Block NBT.
 */
public enum StructureRotation {
    NONE,
    CLOCKWISE_90,
    CLOCKWISE_180,
    COUNTERCLOCKWISE_90;

    /** Returns the exact string value required for the NBT tag. */
    public String getNbtName() {
        return name();
    }
}