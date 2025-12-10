package uhc.arguments.item;

/**
 * 🎯 **Item Command Target Interface**
 * <p>
 * Defines a required component for the Minecraft {@code /item} command, representing
 * either the **destination** or **source** of an item operation.
 * </p>
 * <p>
 * Implementing classes must generate the command segment that specifies the
 * inventory owner, which takes one of two formats:
 * <ul>
 * <li>Block target: {@code block <x> <y> <z>}</li>
 * <li>Entity target: {@code entity <target_selector>}</li>
 * </ul>
 * </p>
 */
public interface ItemTarget {

    /**
     * Generates the command segment that specifies the inventory owner and location.
     * * @return The complete command string prefix for the target (e.g., "block 10 60 20" or "entity @s").
     */
    String getCommandString();
}