package uhc.resource.item;

/**
 * 🛡️ **Equipment Slot Registry**
 * <p>
 * Defines the valid slots used by "minecraft:attribute_modifiers" and
 * the "minecraft:equippable" component.
 * </p>
 */
public enum EquipmentSlot {

    /** * The modifier applies regardless of which slot the item is in.
     * Primarily used in Attribute Modifiers.
     */
    ANY,

    // --- 📦 Group Slots (Commonly used in 'equippable' component) ---

    /** Represents both MAINHAND and OFFHAND. */
    HAND,

    /** Represents HEAD, CHEST, LEGS, and FEET. */
    ARMOR,

    // --- ✋ Hand Slots ---

    /** The item must be in the player's primary hand. */
    MAINHAND,

    /** The item must be in the player's secondary hand. */
    OFFHAND,

    // --- 👕 Armor Slots ---

    /** Helmet / Headwear slot. */
    HEAD,

    /** Chestplate / Elytra slot. */
    CHEST,

    /** Leggings slot. */
    LEGS,

    /** Boots slot. */
    FEET,

    // --- 🐎 Mount & Animal Slots (1.20.5+) ---

    /** Used for Wolf Armor or Horse Armor. */
    BODY,

    /** Used specifically for the saddle slot on ridable entities. */
    SADDLE;

    /**
     * @return The raw string value expected by Minecraft NBT (snake_case).
     */
    public String getNbtName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return getNbtName();
    }

    /**
     * Validation check to see if the slot is a specific physical slot.
     * Group slots (HAND, ARMOR) may cause errors if used in certain NBT attributes.
     * * @return true if the slot represents a single specific equipment location.
     */
    public boolean isSpecific() {
        return this != ANY && this != HAND && this != ARMOR;
    }
}