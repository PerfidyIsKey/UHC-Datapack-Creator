package shared.item;

/**
 * Represents the type of tool piece (e.g., axe, pickaxe, sword).
 */
public enum ToolPiece {
    AXE,
    PICKAXE,
    SHOVEL,
    HOE,
    SWORD;

    /**
     * Converts the enum name to the lowercase resource name part (e.g., "PICKAXE" -> "pickaxe").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}