package uhc.arguments.slot;

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
     * @return The final string used in the command (e.g., "inventory.0", "weapon.mainhand").
     */
    String getCommandString();
}