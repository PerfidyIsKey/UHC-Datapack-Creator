package shared.item;

/**
 * Defines item IDs (resource locations) used in NBT data,
 * automatically applying the 'minecraft' namespace by default.
 */
public enum ItemId {
    AIR("air"),
    CHEST("chest"),
    GOAT_HORN("goat_horn"),
    IRON_AXE("iron_axe"),
    MUSIC_DISC_STAL("music_disc_stal"),
    PLAYER_HEAD("player_head"),
    SHIELD("shield"),
    TRIDENT("trident");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    ItemId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    ItemId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant item ID.
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Generates the ItemId resource location for a specific armor combination.
     * This avoids having to list all armor combinations explicitly in the enum.
     * @param material The armor material (e.g., DIAMOND).
     * @param piece The armor piece (e.g., HELMET).
     * @return The full resource location string (e.g., "minecraft:diamond_helmet").
     */
    public static String getArmorResourceLocation(ArmorMaterial material, ArmorPiece piece) {
        // Uses the existing DEFAULT_NAMESPACE constant for the prefix.
        // Format: minecraft:<material>_<piece>
        return DEFAULT_NAMESPACE + ":" + material.toString() + "_" + piece.toString();
    }

    /**
     * Generates the ItemId resource location for a specific tool combination.
     * This avoids having to list all tool combinations explicitly in the enum.
     * @param material The tool material (e.g., DIAMOND).
     * @param piece The tool piece (e.g., AXE).
     * @return The full resource location string (e.g., "minecraft:diamond_axe").
     */
    public static String getToolResourceLocation(ToolMaterial material, ToolPiece piece) {
        // Uses the existing DEFAULT_NAMESPACE constant for the prefix.
        // Format: minecraft:<material>_<piece>
        return DEFAULT_NAMESPACE + ":" + material.toString() + "_" + piece.toString();
    }
}