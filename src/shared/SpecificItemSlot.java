package shared;

/**
 * Represents a final, specific item slot in the Minecraft command syntax
 * (e.g., 'inventory.0', 'weapon.mainhand', 'armor.head').
 */
public interface SpecificItemSlot {
    String getCommandString();

}