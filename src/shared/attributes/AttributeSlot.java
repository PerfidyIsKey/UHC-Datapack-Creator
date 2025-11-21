package shared.attributes;

/**
 * Defines valid equipment slots where an attribute modifier applies.
 */
public enum AttributeSlot {
    MAINHAND("mainhand"),
    OFFHAND("offhand"),
    FEET("feet"),
    LEGS("legs"),
    CHEST("chest"),
    HEAD("head"),
    ARMOR("armor"),
    BODY("body"); // 1.20.5+

    private final String commandString;

    AttributeSlot(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}