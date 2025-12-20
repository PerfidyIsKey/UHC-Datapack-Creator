package uhc.resource.item;

/**
 * 🛡️ **Equipment Slot Registry**
 * <p>
 * Defines the valid slots where attribute modifiers and equippable components
 * can be applied.
 * </p>
 */
public enum EquipmentSlot {
    /** The modifier applies regardless of which slot the item is in. */
    ANY,

    /** Main hand only. */
    MAINHAND,

    /** Off hand only. */
    OFFHAND,

    /** Helmet slot. */
    HEAD,

    /** Chestplate slot. */
    CHEST,

    /** Leggings slot. */
    LEGS,

    /** Boots slot. */
    FEET,

    /** Body slot (used for Horse Armor or Wolf Armor). */
    BODY;


    /**
     * @return The raw string value expected by Minecraft NBT.
     */
    public String getNbtName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return getNbtName();
    }
}