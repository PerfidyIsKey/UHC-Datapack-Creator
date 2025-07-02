package Enums;

public enum InventorySlot {
    CHEST("armor.chest"),
    ENDERCHEST("enderchest"),
    FEET("armor.feet"),
    HEAD("armor.head"),
    HOTBAR("hotbar"),
    INVENTORY("inventory"),
    LEGS("armor.legs"),
    MAINHAND("weapon.mainhand"),
    OFFHAND("weapon.offhand");

    private final String symbol;

    InventorySlot(String symbol) {
        this.symbol = symbol;
    }

    public String setSlotNumber(int slotNumber) {
        return symbol + "." + slotNumber;
    }

    public String toString() {
        return symbol;
    }
}
