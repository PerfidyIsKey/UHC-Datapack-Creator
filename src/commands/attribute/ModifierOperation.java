package commands.attribute;

/**
 * Defines the operation type for the 'modifier add' command.
 * The options are add_value, add_multiplied_base, or add_multiplied_total.
 */
public enum ModifierOperation {
    ADD_VALUE("add_value"),
    ADD_MULTIPLIED_BASE("add_multiplied_base"),
    ADD_MULTIPLIED_TOTAL("add_multiplied_total");

    private final String command;

    ModifierOperation(String command) {
        this.command = command;
    }

    /**
     * Returns the exact string required by the Minecraft command syntax.
     */
    @Override
    public String toString() {
        return command;
    }
}