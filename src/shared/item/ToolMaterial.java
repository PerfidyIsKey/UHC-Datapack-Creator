package shared.item;

/**
 * Represents the material type for tools (e.g., wooden, stone, iron, diamond).
 */
public enum ToolMaterial {
    WOODEN,
    STONE,
    GOLDEN,
    IRON,
    DIAMOND,
    NETHERITE;

    /**
     * Converts the enum name to the lowercase resource name part (e.g., "DIAMOND" -> "diamond").
     */
    @Override
    public String toString() {
        // Wooden tools use "wooden" prefix, stone uses "stone", etc.
        return this.name().toLowerCase();
    }
}