package commands.random;

public enum RandomAction {
    RESET("reset"),
    ROLL("roll"),
    VALUE("value");

    private final String actionName;

    RandomAction(String actionName) {
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
