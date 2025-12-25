package uhc.arguments.item.slot;

/**
 * 🔗 **Minecraft Base Item Slots**
 * <p>
 * Defines the base, named categories of inventory slots recognized by the Minecraft {@code /item} and {@code /replaceitem} commands.
 * These constants represent the category prefix (e.g., {@code armor}, {@code enderchest}, {@code weapon}).
 * </p>
 * <p>
 * For slots that require a numerical index (e.g., container slots), use the {@link #withSlotNumber(int)} method.
 * </p>
 *
 */
public enum ItemSlot implements SpecificItemSlot {

    /** Represents the armor chestplate slot: {@code armor.chest} */
    CHEST("armor.chest"),

    /** Represents the Ender Chest inventory as a whole: {@code enderchest}. Used with slot numbers (0-26). */
    ENDERCHEST("enderchest"),

    /** Represents the armor boots slot: {@code armor.feet} */
    FEET("armor.feet"),

    /** Represents the armor helmet slot: {@code armor.head} */
    HEAD("armor.head"),

    /** Represents the player's hotbar slots: {@code hotbar}. Used with slot numbers (0-8). */
    HOTBAR("hotbar"),

    /** Represents the player's main inventory slots (excluding hotbar): {@code inventory}. Used with slot numbers (0-26). */
    INVENTORY("inventory"),

    /** Represents the armor leggings slot: {@code armor.legs} */
    LEGS("armor.legs"),

    /** Represents the player's main hand item slot: {@code weapon.mainhand} */
    MAINHAND("weapon.mainhand"),

    /** Represents the player's off-hand item slot: {@code weapon.offhand} */
    OFFHAND("weapon.offhand");

    private final String symbol;

    /**
     * Private constructor for enum constants.
     * @param symbol The Minecraft command string for the base slot category.
     */
    ItemSlot(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Creates a type-safe, specific slot instance by appending a numerical index to the base slot symbol.
     * This is typically used for container slots like Ender Chest (0-26), hotbar (0-8), or non-armor inventory (0-26).
     * * @param slotNumber The numerical index of the slot (e.g., 5 for the sixth slot).
     * @return A {@code SpecificItemSlot} instance representing the final command string (e.g., "enderchest.5").
     * @throws IllegalArgumentException if the {@code slotNumber} is less than zero.
     */
    public SpecificItemSlot withSlotNumber(int slotNumber) {
        if (slotNumber < 0) {
            // CRITICAL ERROR CATCH: Slot numbers must be non-negative.
            throw new IllegalArgumentException("Slot number cannot be negative. Value provided: " + slotNumber);
        }
        // Note: Specific upper bounds (like 26 for inventory) are often handled externally
        // as they vary by target (e.g., a chest has 27 slots, a block dispenser has 9).
        return new NumberedItemSlot(this.symbol, slotNumber);
    }

    /**
     * @return The base command string for this slot category (e.g., "enderchest").
     */
    @Override
    public String getCommandString() {
        return symbol;
    }

    /**
     * Internal, private class used to represent a complete, numerically indexed slot,
     * such as {@code inventory.10} or {@code enderchest.26}.
     */
    private static class NumberedItemSlot implements SpecificItemSlot {
        private final String baseSymbol;
        private final int slotNumber;

        /**
         * @param baseSymbol The base category (e.g., "inventory").
         * @param slotNumber The numerical index (e.g., 10).
         */
        public NumberedItemSlot(String baseSymbol, int slotNumber) {
            this.baseSymbol = baseSymbol;
            this.slotNumber = slotNumber;
        }

        /**
         * @return The final, combined command string (e.g., "inventory.10").
         */
        @Override
        public String getCommandString() {
            return baseSymbol + "." + slotNumber;
        }
    }
}