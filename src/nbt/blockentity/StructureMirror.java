package nbt.blockentity;

/**
 * Valid values for the 'mirror' tag in a Structure Block NBT.
 */
public enum StructureMirror {
    NONE("NONE"),
    LEFT_RIGHT("LEFT_RIGHT"),
    FRONT_BACK("FRONT_BACK");

    private final String nbtName;

    StructureMirror(String nbtName) {
        this.nbtName = nbtName;
    }

    /** Returns the exact string value required for the NBT tag. */
    public String getNbtName() {
        return nbtName;
    }
}