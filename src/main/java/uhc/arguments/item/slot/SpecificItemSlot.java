package uhc.arguments.item.slot;

/**
 * 📦 **Specific Item Slot Interface**
 * <p>
 * Represents a final, explicitly named inventory slot used in Minecraft commands,
 * particularly the {@code /item replace} or {@code /data modify} commands.
 * </p>
 * <p>
 * This interface is implemented by enums or classes that can generate a slot string
 * that adheres to the format: {@code <category>.<index>}
 * (e.g., {@code container.0}, {@code armor.head}, {@code weapon.mainhand}).
 * </p>
 */
public interface SpecificItemSlot {

    /**
     * Generates the Minecraft command string representing the target slot.
     * <p>
     * Implementation Note: This should return the raw identifier required
     * by the Minecraft protocol.
     * </p>
     * @return The final string used in the command (e.g., "inventory.0").
     */
    String getCommandString();

    /**
     * Mandatory override to ensure that when this interface is used in
     * string concatenation or logging, it yields the valid command string.
     * @return Result of {@link #getCommandString()}.
     */
    @Override
    String toString();
}