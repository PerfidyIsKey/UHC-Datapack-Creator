package uhc.arguments.data;

/**
 * Represents a type-safe NBT literal value in a data command.
 * Examples: "4b", "5.5d", "{tag:1b}"
 */
public class DataValue {
    private final String nbtValue;

    /**
     * Private constructor. Use the static 'create' factory methods.
     */
    private DataValue(String nbtValue) {
        if (nbtValue == null || nbtValue.trim().isEmpty()) {
            throw new IllegalArgumentException("NBT value cannot be null or empty.");
        }
        this.nbtValue = nbtValue;
    }

    /**
     * Creates an NBT value from a raw string (e.g., NBT compound or list).
     */
    public static DataValue createRaw(String nbtValue) {
        return new DataValue(nbtValue);
    }

    /**
     * Creates a type-safe Byte value (e.g., "4b").
     */
    public static DataValue createByte(byte value) {
        return new DataValue(value + "b");
    }

    /**
     * Creates a type-safe Double value (e.g., "5.5d").
     */
    public static DataValue createDouble(double value) {
        return new DataValue(value + "d");
    }

    // Add similar methods for other types (Int, String, Long, Float, etc.)

    @Override
    public String toString() {
        return nbtValue;
    }
}