package shared;

/**
 * Defines valid enchantment IDs used in the enchantments item component.
 */
public enum EnchantmentId {
    VANISHING_CURSE("vanishing_curse"),
    FIRE_ASPECT("fire_aspect"),
    SHARPNESS("sharpness"),
    IMPALING("impaling"),
    LOYALTY("loyalty"),
    EFFICIENCY("efficiency");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    EnchantmentId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }
}