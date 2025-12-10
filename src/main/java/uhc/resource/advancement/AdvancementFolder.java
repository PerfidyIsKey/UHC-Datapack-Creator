package uhc.resource.advancement;

/**
 * Defines the high-level folder structure used by Minecraft for built-in advancements.
 * E.g., 'story', 'nether', 'adventure'.
 */
public enum AdvancementFolder {
    STORY("story"),
    NETHER("nether"),
    END("end"),
    ADVENTURE("adventure");

    private final String folderName;

    AdvancementFolder(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Returns the string representation of the folder path segment.
     */
    @Override
    public String toString() {
        return folderName;
    }
}