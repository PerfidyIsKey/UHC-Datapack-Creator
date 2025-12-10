package uhc.game;

/**
 * Represents the different categories of Minecraft sound sources.
 * Constants are capitalized for readability, and the stored value is lowercase
 * for use in commands and resource files.
 */
public enum SoundSource {
    MASTER("master"),
    MUSIC("music"),
    RECORD("record"),
    WEATHER("weather"),
    BLOCK("block"),
    HOSTILE("hostile"),
    NEUTRAL("neutral"),
    PLAYER("player"),
    AMBIENT("ambient"),
    VOICE("voice");

    // The lowercase string value required by Minecraft
    private final String resourceName;

    SoundSource(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Retrieves the lowercase string identifier for use in NBT or commands.
     * @return The lowercase sound source identifier (e.g., "master").
     */
    public String getResourceName() {
        return resourceName;
    }
}