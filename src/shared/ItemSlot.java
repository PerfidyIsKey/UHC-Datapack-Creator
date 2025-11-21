package shared;

public enum ItemSlot {
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

    ItemSlot(String symbol) {
        this.symbol = symbol;
    }

    public String setSlotNumber(int slotNumber) {
        return symbol + "." + slotNumber;
    }

    public String toString() {
        return symbol;
    }
}
