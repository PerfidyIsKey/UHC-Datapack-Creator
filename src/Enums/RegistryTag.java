package Enums;

public enum RegistryTag {
    // --- Built-in Minecraft tags ---
    LEAVES("leaves"),
    PLANKS("planks"),
    LOGS("logs"),

    // --- Custom UHC tags ---
    BLOCK_BEACON_LIGHT("uhc", "block_beacon_light");

    private final String namespace;
    private final String name;

    RegistryTag(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    RegistryTag(String name) {
        this.namespace = "minecraft";
        this.name = name;
    }

    public String getFullId() {
        return "#" + namespace + ":" + name;
    }

    public boolean isMinecraftTag() {
        return "minecraft".equals(namespace);
    }

    public boolean isCustomTag() {
        return !"minecraft".equals(namespace);
    }

    @Override
    public String toString() {
        return getFullId();
    }
}
