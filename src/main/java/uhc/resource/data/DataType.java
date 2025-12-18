package uhc.resource.data;

/**
 * 💾 **NBT Data Type**
 * <p>
 * Defines the numerical type used when storing or scaling data in Minecraft's NBT format.
 * Primarily used in {@code /execute store} for blocks, entities, and storage.
 * </p>
 */
public enum DataType {
    /** A 1-byte signed integer (-128 to 127). */
    BYTE,

    /** A 2-byte signed integer (-32,768 to 32,767). */
    SHORT,

    /** A 4-byte signed integer. */
    INT,

    /** An 8-byte signed integer. */
    LONG,

    /** A 4-byte single-precision floating point number. */
    FLOAT,

    /** An 8-byte double-precision floating point number. */
    DOUBLE;

    /**
     * Safely retrieves a DataType from a string name.
     * @param value The type name (e.g., "double", "INT").
     * @return The matching {@link DataType}, or {@link #DOUBLE} as a safe fallback.
     */
    public static DataType fromString(String value) {
        if (value == null) return DOUBLE;
        try {
            return valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Defaulting to DOUBLE ensures maximum precision if the input is unrecognized
            return DOUBLE;
        }
    }

    /**
     * Returns the lowercase name for use in Minecraft command syntax.
     * @return The data type name (e.g., "float").
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}