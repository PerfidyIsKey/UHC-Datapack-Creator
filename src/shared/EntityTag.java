package shared;

/**
 * Enum for type-safe representation of Minecraft entity tags (used in the 'Tags' ListTag).
 * Using an enum prevents string typos when assigning tags to entities.
 */
public enum EntityTag {
    CARE_PACKAGE("CarePackage"),
    IS_FLYING("IsFlying");

    private final String tagName;

    EntityTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Gets the raw string value required for the NBT Tag List.
     */
    public String getTagName() {
        return tagName;
    }
}