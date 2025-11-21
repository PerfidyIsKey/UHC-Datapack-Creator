package shared;

public enum ItemSlot implements SpecificItemSlot {
    CHEST("armor.chest"),
    ENDERCHEST("enderchest"),
    FEET("armor.feet"),
    HEAD("armor.head"),
    HOTBAR("hotbar"),
    INVENTORY("inventory"), // This is the base slot
    LEGS("armor.legs"),
    MAINHAND("weapon.mainhand"),
    OFFHAND("weapon.offhand");

    private final String symbol;

    ItemSlot(String symbol) {
        this.symbol = symbol;
    }

    // A type-safe way to get a numbered slot (e.g., 'inventory.0')
    public SpecificItemSlot withSlotNumber(int slotNumber) {
        // You could add validation here if needed, but for 'inventory',
        // numbers 0-26 are typically valid, or 0-8 for 'hotbar'.
        return new NumberedItemSlot(this.symbol, slotNumber);
    }

    @Override
    public String getCommandString() {
        return symbol;
    }

    // Inner class to represent a final, numbered slot
    private static class NumberedItemSlot implements SpecificItemSlot {
        private final String baseSymbol;
        private final int slotNumber;

        public NumberedItemSlot(String baseSymbol, int slotNumber) {
            this.baseSymbol = baseSymbol;
            this.slotNumber = slotNumber;
        }

        @Override
        public String getCommandString() {
            return baseSymbol + "." + slotNumber;
        }
    }
}