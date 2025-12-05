package commands.worldborder;

/**
 * Defines the available actions for the Minecraft /worldborder command.
 */
public enum WorldBorderAction {
    ADD("add"),
    CENTER("center"),
    DAMAGE("damage"),
    GET("get"),
    SET("set"),
    WARNING("warning");

    private final String actionName;

    WorldBorderAction(String actionName) {
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