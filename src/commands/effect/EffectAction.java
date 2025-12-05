package commands.effect;

public enum EffectAction {
    CLEAR("clear"),
    GIVE("give");

    private final String actionName;

    EffectAction(String actionName) {
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
