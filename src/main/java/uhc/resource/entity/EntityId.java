package uhc.resource.entity;

/**
 * Defines the valid resource locations for Minecraft entities,
 * automatically applying the 'minecraft:' namespace by default.
 */
public enum EntityId {
    AREA_EFFECT_CLOUD("area_effect_cloud"),
    ARMOR_STAND("armor_stand"),
    DOLPHIN("dolphin"),
    FALLING_BLOCK("falling_block"),
    FIREWORK_ROCKET("firework_rocket"),
    HORSE("horse"),
    ITEM("item"),
    MARKER("marker"),
    WOLF("wolf");

    private final String resourceLocation;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Constructor. Prefixes with "minecraft:" unless a namespace is already present.
     */

    EntityId(String path, String namespace) {
        this.resourceLocation = namespace + ":" + path;
    }

    EntityId(String path) {
        this.resourceLocation = DEFAULT_NAMESPACE + ":" + path;
    }

    /**
     * Returns the full NBT-compliant resource location, e.g., "minecraft:zombie".
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}