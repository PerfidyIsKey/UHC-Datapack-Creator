package shared;

/**
 * Defines the valid values for the 'Type' tag in a firework star explosion.
 * Now includes both the legacy byte value (for old NBT) and the modern string value
 * (for 1.20.5+ Item Components).
 */
public enum FireworkShape {
    // Enum constants now include the component string name
    SMALL_BALL((byte)0, "small_ball"),
    LARGE_BALL((byte)1, "large_ball"),
    STAR((byte)2, "star"),
    CREEPER((byte)3, "creeper"),
    BURST((byte)4, "burst");

    private final byte value;
    private final String componentValue; // New field for the 1.20.5+ string component

    FireworkShape(byte value, String componentValue) {
        this.value = value;
        this.componentValue = componentValue;
    }

    /**
     * Gets the legacy byte value for the Type tag (pre-1.20.5 format).
     */
    public byte getValue() {
        return value;
    }

    /**
     * Gets the string value used in the Item Component 'shape' field (1.20.5+ format).
     */
    public String getComponentValue() {
        return componentValue;
    }
}