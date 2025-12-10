package uhc.attribute;

/**
 * Defines the math operation for an attribute modifier.
 */
public enum AttributeOperation {
    // Adds the value directly to the attribute's base value (default=0)
    ADD_VALUE("add_value"),
    // Adds (value * base_value) to the attribute's value (default=1)
    ADD_MULTIPLIED_BASE("add_multiplied_base"),
    // Adds (value * (base_value + all_modifiers_from_0_and_1)) to the attribute's value (default=2)
    ADD_MULTIPLIED_TOTAL("add_multiplied_total");

    private final String commandString;

    AttributeOperation(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}