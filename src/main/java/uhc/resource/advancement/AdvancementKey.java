package uhc.resource.advancement;

import java.util.Objects;

/**
 * 🔑 **Advancement Key Registry**
 * <p>
 * Defines the specific leaf-node identifiers (keys) for Minecraft advancements.
 * This enum maps Java constants to the path segments used in namespaced keys
 * (e.g., the 'root' in {@code minecraft:story/root}).
 * </p>
 * <p>
 * Because multiple advancement tabs (Story, Nether, Adventure) use the identifier
 * {@code "root"}, this enum uses unique constant names while mapping to the
 * correct Minecraft string.
 * </p>
 */
public enum AdvancementKey {

    // --- 📖 Story Keys ---

    /** The starting point of the Story tab. Identifier: {@code root}. */
    ROOT("root"),

    /** Awarded for punching a tree. Identifier: {@code mine_wood}. */
    MINE_WOOD,

    /** Awarded for obtaining diamond armor. Identifier: {@code shiny_gear}. */
    SHINY_GEAR,

    // --- 👺 Nether Keys ---

    /** The starting point of the Nether tab. Identifier: {@code root}. */
    NETHER_ROOT("root"),

    /** Awarded for entering a Nether portal. Identifier: {@code enter_nether}. */
    ENTER_NETHER,

    /** Awarded for killing a Blaze. Identifier: {@code obtain_blaze_rod}. */
    OBTAIN_BLAZE_ROD,

    // --- 🌌 End Keys ---

    /** Awarded for defeating the Ender Dragon. Identifier: {@code kill_dragon}. */
    KILL_DRAGON,

    // --- 🏹 Adventure Keys ---

    /** The starting point of the Adventure tab. Identifier: {@code root}. */
    ADVENTURE_ROOT("root"),

    /** Awarded for defeating any hostile mob. Identifier: {@code kill_a_mob}. */
    KILL_A_MOB;

    // --- ⚙️ State & Fields ---

    /** * The specific string segment used in the resource location.
     * If null, the lowercase name of the enum constant is used.
     */
    private final String overrideKey;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Automatic Key Constructor**
     * <p>Uses {@code name().toLowerCase()} as the Minecraft identifier.</p>
     */
    AdvancementKey() {
        this.overrideKey = null;
    }

    /**
     * 🟡 **Manual Override Constructor**
     * <p>Used for keys that share a Minecraft name but need unique Enum constants
     * (e.g., multiple roots).</p>
     * @param overrideKey The specific lowercase string used by Minecraft.
     */
    AdvancementKey(String overrideKey) {
        this.overrideKey = Objects.requireNonNull(overrideKey, "Override key cannot be null.");
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the string identifier for this advancement key.
     * <p><b>Logic:</b> Returns the override if provided; otherwise, returns
     * the lowercase constant name.</p>
     * @return The Minecraft-compliant path segment.
     */
    public String getKey() {
        return (overrideKey != null) ? overrideKey : this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves an AdvancementKey from a raw string.
     * <p><b>Error Catching:</b> Handles null or blank inputs by returning
     * the generic {@link #ROOT}. If a string is provided that does not match
     * an override or a constant name, it returns {@link #ROOT} as a fallback.</p>
     * @param input The raw key name (e.g., "kill_dragon").
     * @return The matching {@link AdvancementKey}, or {@link #ROOT} if invalid.
     */
    public static AdvancementKey fromString(String input) {
        if (input == null || input.isBlank()) {
            return ROOT;
        }

        String target = input.toLowerCase().trim();
        for (AdvancementKey key : values()) {
            if (key.getKey().equals(target)) {
                return key;
            }
        }

        // Fail-safe to prevent logic errors in resource resolution
        return ROOT;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the key segment for use in command generation or path building.
     * @return The result of {@link #getKey()}.
     */
    @Override
    public String toString() {
        return getKey();
    }
}