package uhc.data.nbt.blockentity;

/**
 * Valid values for the 'rotation' tag in a Structure Block NBT.
 */
public enum StructureRotation {
    NONE("NONE"),
    CLOCKWISE_90("CLOCKWISE_90"),
    CLOCKWISE_180("CLOCKWISE_180"),
    COUNTERCLOCKWISE_90("COUNTERCLOCKWISE_90");

    private final String nbtName;

    StructureRotation(String nbtName) {
        this.nbtName = nbtName;
    }

    /** Returns the exact string value required for the NBT tag. */
    public String getNbtName() {
        return nbtName;
    }
}