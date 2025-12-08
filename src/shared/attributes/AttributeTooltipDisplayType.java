package shared.attributes;

/**
 * Defines the valid types for displaying an attribute modifier's value on an item's tooltip.
 */
public enum AttributeTooltipDisplayType {
    /** The modifier is shown with its value. */
    VALUE("value"),
    /** The modifier is hidden from the tooltip. */
    HIDDEN("hidden");

    private final String commandString;

    AttributeTooltipDisplayType(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}