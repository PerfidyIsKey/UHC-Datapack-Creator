package uhc.resource.tag;

import java.util.Objects;

/**
 * 🏷️ **Static Entity Tag Registry**
 * <p>
 * This enum provides type-safe representations of predefined Minecraft entity tags.
 * These tags are primarily used for game state management, role assignment (e.g., Traitors),
 * and event tracking (e.g., Care Packages).
 * </p>
 * <p>
 * Implements {@link EntityTag}, ensuring these constants can be used anywhere a
 * tag-based selector or NBT modification is required.
 * </p>
 */
public enum StaticEntityTag implements EntityTag {

    // --- 🎮 Game State Tags ---

    /** * Indicates that the global UHC game has officially started. */
    GAME_STARTED("GameStarted"),

    /** * Used to enable/disable the respawn logic for players. */
    RESPAWN("Respawn"),

    /** * A safety flag to permanently disable respawning for specific contexts. */
    RESPAWN_DISABLED("RespawnDisabled"),

    /** * Enables debug-level logging and visual feedback for the tagged entity. */
    DEBUG("Debug"),

    // --- 🕵️ Traitor Mode Tags ---

    /** * Marks an entity as a Traitor in specialized game modes. */
    TRAITOR("Traitor"),

    /** * Global flag to indicate that the traitor selection process is finished. */
    TRAITORS_ASSIGNED("TraitorsAssigned"),

    /** * Prevents a specific player from being selected as a Traitor. */
    DONT_MAKE_TRAITOR("DontMakeTraitor"),

    // --- 📦 Care Package & Event Tags ---

    /** * Marks an entity (usually a falling block or chest) as a Care Package. */
    CARE_PACKAGE("CarePackage"),

    /** * Tracking tag used to count or manage dropped care packages. */
    CARE_PACKAGES_DROPPED("CarePackagesDropped"),

    /** * Marks an entity as currently being a candidate for a reward prediction. */
    PREDICTION_CANDIDATE("PredicationCandidate"),

    /** * Indicates that the prediction cycle for an event has concluded. */
    PREDICTIONS_COMPLETED("PredictionsCompleted"),

    // --- ⚔️ Combat & Achievement Tags ---

    /** * Marks the player currently holding the "Iron Man" title (last to take damage). */
    IRON_MAN("IronMan"),

    /** * Players who have not yet taken damage and are eligible for Iron Man. */
    IRON_MAN_CANDIDATE("IronManCandidate"),

    /** * Technical tag used to trigger or verify collar-based mechanics for tamed mobs. */
    COLLAR_CHECK("CollarCheck"),

    /** * Active check to determine if an entity is currently in a flying state. */
    IS_FLYING("IsFlying"),

    // --- 🚩 Control Point Tags ---

    /** * Indicates a control point has been successfully seized. */
    CONTROL_POINT_CAPTURED("ControlPointCaptured"),

    /** * Logic gate to enable the first control point objective. */
    CONTROL_POINT_1_ENABLED("ControlPoint1Enabled"),

    /** * Logic gate to enable the second control point objective. */
    CONTROL_POINT_2_ENABLED("ControlPoint2Enabled"),

    // --- 🤝 Social & UI Tags ---

    /** * Used to see if a player's logic is currently querying their win status. */
    AM_I_WINNING("AmIWinning"),

    /** * Visual or logic flag for players actively seeking a team mate. */
    LOOKING_FOR_TEAM_MATE("LookingForTeamMate");

    // --- ⚙️ Internal State ---

    /** * The exact string value of the tag as it appears in Minecraft NBT/Selectors. */
    private final String tagName;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for the static registry.
     * <p><b>Error Catching:</b> Trims the input and executes {@link #validate()}
     * immediately to catch illegal characters during class loading.</p>
     * @param tagName The raw tag string.
     */
    StaticEntityTag(String tagName) {
        this.tagName = Objects.requireNonNull(tagName, "Tag name cannot be null").trim();

        // Ensure the predefined tags are valid for command selectors.
        this.validate();
    }

    // --- 🛰️ EntityTag Implementation ---

    /**
     * Retrieves the raw string value required for the NBT {@code Tags} list or selectors.
     * @return The case-sensitive tag name.
     */
    @Override
    public String getTagName() {
        return tagName;
    }

    /**
     * Performs a syntax check on the tag name.
     * <p><b>Error Catching:</b> Ensures no spaces or selector-breaking characters
     * (like commas or brackets) exist in the defined constants.</p>
     * @throws IllegalStateException if a registry constant violates naming rules.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (tagName.isEmpty()) {
            throw new IllegalStateException("Static tag name for " + name() + " cannot be empty.");
        }

        if (tagName.contains(" ") || tagName.contains(",") || tagName.contains("]") || tagName.contains("=")) {
            throw new IllegalStateException("Static tag '" + tagName + "' contains illegal selector characters.");
        }
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a StaticEntityTag based on its raw string value.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. Returns {@code null}
     * if the tag is not part of the predefined static registry.</p>
     * @param rawName The raw tag string to look up.
     * @return The matching {@link StaticEntityTag}, or {@code null} if not found.
     */
    public static StaticEntityTag fromString(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return null;
        }

        String target = rawName.trim();
        for (StaticEntityTag tag : values()) {
            if (tag.tagName.equalsIgnoreCase(target)) {
                return tag;
            }
        }
        return null;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the raw tag name for direct inclusion in command strings.
     * @return The result of {@link #getTagName()}.
     */
    @Override
    public String toString() {
        return getTagName();
    }
}