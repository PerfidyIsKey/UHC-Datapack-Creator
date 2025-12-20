package uhc.resource.entity.mobs.armor_stand;

/**
 * 🛠️ **Armor Stand Slot Interaction Enum**
 * <p>
 * Helps calculate the 'DisabledSlots' bitfield for Armor Stands.
 * Formula: (Action Value) << (Slot ID)
 * </p>
 */
public enum DisabledSlots {
    MAIN_HAND(0),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4),
    OFF_HAND(5);

    private final int slotId;

    DisabledSlots(int slotId) {
        this.slotId = slotId;
    }

    /**
     * Calculates the bitfield value to disable specific actions for this slot.
     * * @param remove  Disable taking the item out.
     * @param replace Disable swapping the item.
     * @param place   Disable putting an item in.
     * @return The calculated bitfield part for this slot.
     */
    public int getBitfield(boolean remove, boolean replace, boolean place) {
        int bitfield = 0;
        if (remove)  bitfield |= (1 << slotId);
        if (replace) bitfield |= (16 << slotId);
        if (place)   bitfield |= (1024 << slotId);
        return bitfield;
    }

    /**
     * Helper to disable ALL interactions for ALL slots.
     * Result is the magic number: 4144959
     */
    public static int disableAll() {
        int total = 0;
        for (DisabledSlots slot : values()) {
            total |= slot.getBitfield(true, true, true);
        }
        return total;
    }
}