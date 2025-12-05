package commands.tag;

/**
 * Defines the available actions for the Minecraft /tag command.
 */
public enum TagAction {
    ADD("add"),
    REMOVE("remove"),
    LIST("list");

    private final String actionName;

    TagAction(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Returns the lowercase action name used in the command string.
     */
    @Override
    public String toString() {
        return actionName;
    }
}