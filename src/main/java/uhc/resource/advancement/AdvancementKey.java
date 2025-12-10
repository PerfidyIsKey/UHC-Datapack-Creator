package uhc.resource.advancement;

/**
 * Defines the specific ID (key) part of a Minecraft advancement resource location.
 * E.g., 'root', 'shiny_gear', 'mine_wood'.
 */
public enum AdvancementKey {
    // Story Keys
    ROOT("root"),
    MINE_WOOD("mine_wood"),
    SHINY_GEAR("shiny_gear"),

    // End Keys
    KILL_DRAGON("kill_dragon"),

    // Nether Keys
    NETHER_ROOT("root"), // Note: The root of the nether tree is also named 'root'
    ENTER_NETHER("enter_nether"),
    OBTAIN_BLAZE_ROD("obtain_blaze_rod"), // User's example was GHAST_TEAR, renaming to actual key

    // Adventure Keys
    ADVENTURE_ROOT("root"), // Note: The root of the adventure tree is also named 'root'
    KILL_MOB("kill_a_mob");

    private final String key;

    AdvancementKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}