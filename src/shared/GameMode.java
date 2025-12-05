package shared;

/**
 * Represents the four main game modes in Minecraft commands.
 * Stores a user-friendly display name (e.g., "Survival") but returns
 * the required lowercase command argument (e.g., "survival").
 */
public enum GameMode {

    // Enum constants are typically uppercase, but here we define them
    // to match the desired display name for better readability.
    SURVIVAL("survival"),
    CREATIVE("creative"),
    ADVENTURE("adventure"),
    SPECTATOR("spectator");

    // Private field to hold the lowercase command string
    private final String commandString;

    /**
     * Constructor for the enum constants.
     * @param displayName The capitalized, user-friendly name for display.
     */
    GameMode(String displayName) {
        // We convert the display name to lowercase for the actual command value
        this.commandString = displayName;
    }

    /**
     * Returns the required command argument string (e.g., "survival").
     * This is the value used when building a command string.
     */
    @Override
    public String toString() {
        return commandString;
    }
}