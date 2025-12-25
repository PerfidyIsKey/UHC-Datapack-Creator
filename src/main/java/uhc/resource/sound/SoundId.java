package uhc.resource.sound;

import uhc.arguments.sound.SoundEvent;
import uhc.core.DatapackConfig;

/**
 * 🔊 **Sound Registry ID Mapper**
 * <p>
 * Maps common Minecraft Sound Events to their resource locations.
 * Used by {@link SoundEvent} and {@link uhc.data.nbt.item.components.InstrumentComponent}.
 * </p>
 */
public enum SoundId {
    // --- Environment / Mood ---
    BASALT("ambient.basalt_deltas.mood"),
    CRIMSON("ambient.crimson_forest.mood"),
    WARPED("ambient.warped_forest.mood"),

    // --- Entities & Events ---
    WITHER("entity.wither.spawn"),
    THUNDER("entity.lightning_bolt.thunder"),

    // --- Goat Horns (For InstrumentComponent) ---
    HORN_PONDER("item.goat_horn.sound.0"),
    HORN_SING("item.goat_horn.sound.1"),
    HORN_SEEK("item.goat_horn.sound.2"),
    HORN_FEEL("item.goat_horn.sound.3"),
    HORN_ADMIRE("item.goat_horn.sound.4"),
    HORN_CALL("item.goat_horn.sound.5"),
    HORN_YEARN("item.goat_horn.sound.6"),
    HORN_DREAM("item.goat_horn.sound.7");

    private final String path;
    private final String namespace;

    /**
     * Constructor for specific namespace and path.
     */
    SoundId(String path, String namespace) {
        this.path = path;
        this.namespace = namespace;
    }

    /**
     * Constructor that automatically handles namespaced paths.
     * If "namespace:path" is provided, it splits them.
     * Otherwise, defaults to the Minecraft namespace.
     */
    SoundId(String path) {
        if (path.contains(":")) {
            String[] split = path.split(":", 2);
            this.namespace = split[0];
            this.path = split[1];
        } else {
            this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
            this.path = path;
        }
    }

    /**
     * Returns the full NBT-compliant ResourceLocation string.
     * @return String like "minecraft:entity.wither.spawn"
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}