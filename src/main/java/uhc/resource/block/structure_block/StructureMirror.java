package uhc.resource.block.structure_block;

/**
 * Valid values for the 'mirror' tag in a Structure Block NBT.
 */
public enum StructureMirror {
    NONE,
    LEFT_RIGHT,
    FRONT_BACK;

    /** Returns the exact string value required for the NBT tag. */
    public String getNbtName() {
        return name();
    }
}