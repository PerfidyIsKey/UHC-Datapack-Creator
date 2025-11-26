package shared;

/**
 * Defines the four available difficulty levels for the Minecraft /difficulty command.
 * The enum constants are in all caps, and the toString() method returns the lowercase
 * command argument string as required by the Minecraft command syntax.
 */
public enum DifficultyId {
    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    /**
     * Returns the difficulty name in lowercase, which is the valid command argument.
     * E.g., PEACEFUL -> "peaceful"
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}