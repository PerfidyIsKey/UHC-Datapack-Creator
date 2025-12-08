package shared.item;

/**
 * Represents the material type for armor pieces (e.g., diamond, iron, netherite).
 */
public enum ArmorMaterial {
    LEATHER,
    CHAINMAIL,
    GOLDEN,
    IRON,
    DIAMOND,
    NETHERITE;

    /**
     * Converts the enum name to the lowercase resource name part (e.g., "DIAMOND" -> "diamond").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}