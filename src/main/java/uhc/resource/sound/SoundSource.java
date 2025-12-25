package uhc.resource.sound;

/**
 * 🔊 **Sound Source Registry**
 * <p>
 * Defines the categories of Minecraft sound sources used for volume control,
 * audio mixing, and the {@code /playsound} command.
 * </p>
 * <p>
 * This enum maps Java constants to the lowercase identifiers required by
 * Minecraft's internal sound engine.
 * </p>
 */
public enum SoundSource {

    // --- 🎼 Global & Environment Sources ---

    /** The primary volume slider that affects all other categories. */
    MASTER,

    /** Background music tracks. */
    MUSIC,

    /** Audio from Music Discs and Jukeboxes. */
    RECORD,

    /** Environmental sounds like rain, thunder, and wind. */
    WEATHER,

    /** Sounds produced by block interactions (e.g., breaking, placing, or pistons). */
    BLOCK,

    /** Background atmospheric sounds specific to biomes or locations. */
    AMBIENT,

    // --- 🏹 Entity & Player Sources ---

    /** Sounds from aggressive mobs (e.g., Zombies, Creepers). */
    HOSTILE,

    /** Sounds from passive or neutral mobs (e.g., Cows, Villagers, Wolves). */
    NEUTRAL,

    /** Sounds produced specifically by players (e.g., footsteps, eating, swimming). */
    PLAYER,

    /** Narrative or voice-over sounds. */
    VOICE;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase string identifier for use in commands or resource files.
     * <p><b>Example:</b> {@code SoundSource.HOSTILE.getResourceName()} returns {@code "hostile"}.</p>
     * * @return The lowercase identifier derived directly from the constant name.
     */
    public String getResourceName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a SoundSource from a raw string.
     * <p><b>Error Catching:</b> Handles null, blank, or unrecognized strings by
     * returning {@link #MASTER} as a fail-safe default. This ensures the audio
     * system continues to function even if a config file is malformed.</p>
     * * @param input The raw source name (e.g., "Ambient" or "weather").
     * @return The matching {@link SoundSource}, or {@link #MASTER} if invalid.
     */
    public static SoundSource fromString(String input) {
        if (input == null || input.isBlank()) {
            return MASTER;
        }

        String target = input.toUpperCase().trim();
        try {
            return SoundSource.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped sources and fallback to Master
            return MASTER;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase resource name for direct command insertion.
     * <p><b>Implementation:</b> Delegates to {@link #getResourceName()}.</p>
     * * @return The lowercase sound source (e.g., "neutral").
     */
    @Override
    public String toString() {
        return getResourceName();
    }
}