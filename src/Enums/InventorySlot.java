package Enums;

public enum InventorySlot {
    chest("armor.chest"),
    enderchest("enderchest"),
    feet("armor.feet"),
    head("armor.head"),
    hotbar("hotbar"),
    inventory("inventory"),
    legs("armor.legs"),
    mainhand("weapon.mainhand"),
    offhand("weapon.offhand");

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
