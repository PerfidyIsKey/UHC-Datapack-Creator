package shared;

/**
 * Defines the types of display for particles available, each with a corresponding
 * lowercase name used for data identification.
 */
public enum DisplayType {
    FORCE("force"),
    NORMAL("normal");

    private final String typeName;

    /**
     * Constructs a DisplayType enum member with its corresponding string value.
     * Note: Enum constants are capitalized (e.g., NORMAL), but the string value is lowercase (e.g., "normal").
     * @param typeName The lowercase name used for the display type.
     */
    DisplayType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Returns the lowercase name of the display type used in data storage or commands.
     */
    @Override
    public String toString() {
        return typeName;
    }
}