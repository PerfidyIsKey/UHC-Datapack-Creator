package shared;

/**
 * Defines the valid modes for a Minecraft Structure Block,
 * used in both NBT tags and BlockState properties.
 */
public enum StructureBlockMode {
    SAVE("SAVE"),
    LOAD("LOAD"),
    CORNER("CORNER"),
    DATA("DATA");

    private final String value;

    StructureBlockMode(String value) {
        this.value = value;
    }

    /**
     * Returns the capitalized string value used in the Structure Block NBT tag (e.g., "SAVE").
     */
    public String getNbtValue() {
        return value;
    }

    /**
     * Returns the lowercase string value used in BlockState properties (e.g., "save").
     */
    public String getBlockStateValue() {
        return value.toLowerCase();
    }
}