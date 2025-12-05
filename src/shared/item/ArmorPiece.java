package shared.item;

/**
 * Represents the type of armor piece (e.g., helmet, chestplate, boots).
 */
public enum ArmorPiece {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS;

    /**
     * Converts the enum name to the lowercase resource name part (e.g., "HELMET" -> "helmet").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}