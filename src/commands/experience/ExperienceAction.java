package commands.experience;

public enum ExperienceAction {
    ADD("add"),
    SET("set"),
    QUERY("query");

    private final String actionName;

    ExperienceAction(String actionName) {
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
