package shared;

/**
 * Common entity boolean NBT tags that are represented by a ByteTag (1b or 0b).
 * These are used by BaseEntityNbt and consumed by all entity builders.
 */
public enum BooleanNbtProperty {
    GLOWING("Glowing"),
    SILENT("Silent"),
    INVISIBLE("Invisible"),
    NO_GRAVITY("NoGravity"),
    INVULNERABLE("Invulnerable");

    private final String nbtName;

    BooleanNbtProperty(String nbtName) {
        this.nbtName = nbtName;
    }

    public String getNbtName() {
        return nbtName;
    }
}