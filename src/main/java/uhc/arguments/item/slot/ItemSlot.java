package uhc.arguments.item.slot;

import java.util.Objects;

/**
 * 🔗 **Minecraft Base Item Slots**
 * <p>
 * Defines the base, named categories of inventory slots recognized by the Minecraft {@code /item}
 * and {@code /replaceitem} commands. These constants represent the category prefix
 * (e.g., {@code armor}, {@code enderchest}, {@code weapon}).
 * </p>
 * <p>
 * <b>Usage:</b> Use the constants directly for fixed slots (e.g., {@code HEAD}).
 * For indexed slots, use {@link #withSlotNumber(int)}.
 * </p>
 */
public enum ItemSlot implements SpecificItemSlot {

    // --- 🛡️ Armor Slots ---

    /** Represents the armor helmet slot: {@code armor.head} */
    HEAD("armor.head"),

    /** Represents the armor chestplate slot: {@code armor.chest} */
    CHEST("armor.chest"),

    /** Represents the armor leggings slot: {@code armor.legs} */
    LEGS("armor.legs"),

    /** Represents the armor boots slot: {@code armor.feet} */
    FEET("armor.feet"),

    // --- ⚔️ Weapon Slots ---

    /** Represents the player's main hand item slot: {@code weapon.mainhand} */
    MAINHAND("weapon.mainhand"),

    /** Represents the player's off-hand item slot: {@code weapon.offhand} */
    OFFHAND("weapon.offhand"),

    // --- 🎒 Indexed Inventory Categories ---

    /** Represents the Ender Chest inventory as a whole: {@code enderchest}. Generally used with 0-26. */
    ENDERCHEST("enderchest"),

    /** Represents the player's hotbar slots: {@code hotbar}. Generally used with 0-8. */
    HOTBAR("hotbar"),

    /** Represents the player's main inventory slots: {@code inventory}. Generally used with 0-26. */
    INVENTORY("inventory");

    // --- 📄 Fields ---

    /** * The internal Minecraft command-line identifier for the slot. */
    private final String symbol;

    // --- 🏗️ Constructor ---

    /**
     * Private constructor for enum constants to bind the Minecraft symbol.
     * @param symbol The raw Minecraft string for the base slot category.
     */
    ItemSlot(String symbol) {
        this.symbol = symbol;
    }

    // --- 🏭 Factory Methods ---

    /**
     * Creates a type-safe, specific slot instance by appending a numerical index to this category.
     * <p>Example: {@code INVENTORY.withSlotNumber(5)} results in {@code inventory.5}.</p>
     * * @param slotNumber The numerical index of the slot (must be non-negative).
     * @return A {@link SpecificItemSlot} instance representing the final indexed command string.
     * @throws IllegalArgumentException if the {@code slotNumber} is negative.
     */
    public SpecificItemSlot withSlotNumber(int slotNumber) {
        if (slotNumber < 0) {
            throw new IllegalArgumentException("Item Slot Error: Slot index cannot be negative. Provided: " + slotNumber);
        }
        return new NumberedItemSlot(this, slotNumber);
    }

    // --- ⚙️ SpecificItemSlot Implementation ---

    /**
     * Returns the raw Minecraft symbol for this slot category.
     * @return The command string (e.g., "armor.head").
     */
    @Override
    public String getCommandString() {
        return this.symbol;
    }

    /**
     * Overrides the default string representation to provide the Minecraft command identifier.
     * @return Result of {@link #getCommandString()}.
     */
    @Override
    public String toString() {
        return getCommandString();
    }

    // --- 🔢 Internal Classes ---

    /**
     * 🔢 **Indexed Item Slot Implementation**
     * <p>
     * Private immutable implementation for slots that require dot-notation indices.
     * </p>
     */
    private static final class NumberedItemSlot implements SpecificItemSlot {

        /** * The base ItemSlot category (e.g., INVENTORY). */
        private final ItemSlot base;

        /** * The numerical index for this specific slot. */
        private final int slotNumber;

        /**
         * @param base       The category parent (non-null).
         * @param slotNumber The validated non-negative index.
         */
        private NumberedItemSlot(ItemSlot base, int slotNumber) {
            this.base = Objects.requireNonNull(base, "Numbered slot construction failed: Base ItemSlot is null.");
            this.slotNumber = slotNumber;
        }

        /**
         * Combines the base symbol and the index for the final Minecraft syntax.
         * @return The formatted string (e.g., "hotbar.0").
         */
        @Override
        public String getCommandString() {
            return this.base.getCommandString() + "." + this.slotNumber;
        }

        /**
         * Ensures string conversion yields the valid command string.
         * @return Result of {@link #getCommandString()}.
         */
        @Override
        public String toString() {
            return getCommandString();
        }
    }
}