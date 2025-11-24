package shared;

/**
 * Defines the types of experience metrics available, each with a corresponding
 * lowercase name used for command identification.
 */
public enum ExperienceType {
    LEVELS("levels"),
    POINTS("points");

    private final String typeName;

    /**
     * Constructs an ExperienceType enum member with its corresponding command string.
     * Note: Enum constants are capitalized (e.g., LEVELS), but the string value is lowercase (e.g., "levels").
     * @param typeName The lowercase name used for the experience type.
     */
    ExperienceType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Returns the lowercase name of the experience type used in commands or data storage.
     */
    @Override
    public String toString() {
        return typeName;
    }
}