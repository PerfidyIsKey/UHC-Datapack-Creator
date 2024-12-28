package Enums;

public enum InventorySlot {
    chest("armor.chest"),
    enderchest("enderchest"),
    feet("armor.feet"),
    head("armor.head"),
    inventory("inventory"),
    legs("armor.legs"),
    mainhand("weapon.mainhand"),
    offhand("weapon.offhand");

    private final String symbol;

    InventorySlot(String symbol) {
        this.symbol = symbol;
    }

    public static String setIndex(InventorySlot type, int slot) {
        return type + "." + slot;
    }

    public String toString() {
        return symbol;
    }
}
