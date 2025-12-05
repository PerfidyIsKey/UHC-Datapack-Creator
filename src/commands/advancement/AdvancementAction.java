package commands.advancement;

/**
 * Defines the available actions for the Minecraft /advancement command.
 */
public enum AdvancementAction {
    GRANT,
    REVOKE;


    /**
     * Converts the enum name to the lowercase resource name part (e.g., "GRANT" -> "grant").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
