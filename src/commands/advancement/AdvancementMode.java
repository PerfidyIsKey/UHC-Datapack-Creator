package commands.advancement;

/**
 * Defines the mode/scope of the /advancement command that requires an advancement ID.
 * The 'everything' mode is handled separately in the Advancement builder.
 */
public enum AdvancementMode {
    EVERYTHING("everything"),
    ONLY("only"),
    FROM("from"),
    THROUGH("through"),
    UNTIL("until");

    private final String modeName;

    AdvancementMode(String modeName) {
        this.modeName = modeName;
    }

    @Override
    public String toString() {
        return modeName;
    }
}