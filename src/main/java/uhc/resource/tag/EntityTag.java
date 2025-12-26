package uhc.resource.tag;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🏷️ **Entity Tag Registry**
 * <p>
 * This registry defines the contract for Minecraft entity tags used to categorize
 * entities for selectors or NBT logic. It provides a type-safe system for
 * both predefined static tags and dynamic versioned tags.
 * </p>
 * <p><b>Formatting:</b> All tags are automatically converted to <i>PascalCase</i>
 * (e.g., {@code GAME_STARTED} becomes {@code "GameStarted"}).</p>
 */
public interface EntityTag {

    // --- 🎮 Game State Tags ---

    /** Signals that the global UHC match is currently in progress. */
    EntityTag GAME_STARTED = Internal.GAME_STARTED;

    /** Indicates that the respawn mechanism is active for the tagged entity. */
    EntityTag RESPAWN = Internal.RESPAWN;

    /** Permanently disables respawn logic for the tagged entity. */
    EntityTag RESPAWN_DISABLED = Internal.RESPAWN_DISABLED;

    /** Enables administrative debug markers and logging for the entity. */
    EntityTag DEBUG = Internal.DEBUG;

    // --- 🕵️ Traitor Mode Tags ---

    /** Marks a player as a Traitor in the current game session. */
    EntityTag TRAITOR = Internal.TRAITOR;

    /** Global flag indicating the traitor assignment phase has concluded. */
    EntityTag TRAITORS_ASSIGNED = Internal.TRAITORS_ASSIGNED;

    /** Blacklists an entity from being selected as a Traitor. */
    EntityTag DONT_MAKE_TRAITOR = Internal.DONT_MAKE_TRAITOR;

    // --- 📦 Care Package & Event Tags ---

    /** Identifies an entity (e.g., a FallingBlock) as a Care Package. */
    EntityTag CARE_PACKAGE = Internal.CARE_PACKAGE;

    /** Tracks the total number of care packages that have spawned. */
    EntityTag CARE_PACKAGES_DROPPED = Internal.CARE_PACKAGES_DROPPED;

    /** Marks an entity as a valid target for reward prediction votes. */
    EntityTag PREDICTION_CANDIDATE = Internal.PREDICTION_CANDIDATE;

    /** Signals that the prediction window for the current event has closed. */
    EntityTag PREDICTIONS_COMPLETED = Internal.PREDICTIONS_COMPLETED;

    // --- ⚔️ Combat & Achievement Tags ---

    /** Identifies the current Iron Man (last player to remain undamaged). */
    EntityTag IRON_MAN = Internal.IRON_MAN;

    /** Identifies players who are still eligible to become the Iron Man. */
    EntityTag IRON_MAN_CANDIDATE = Internal.IRON_MAN_CANDIDATE;

    /** Triggers internal logic to verify or update wolf collar colors. */
    EntityTag COLLAR_CHECK = Internal.COLLAR_CHECK;

    /** State flag indicating if an entity is currently flying (via elytra or ability). */
    EntityTag IS_FLYING = Internal.IS_FLYING;

    // --- 🚩 Control Point (CP) Tags ---

    /** Applied to a control point entity once capture progress reaches 100%. */
    EntityTag CONTROL_POINT_CAPTURED = Internal.CONTROL_POINT_CAPTURED;

    /** Global flag used to activate the logic gates for control point mechanics. */
    EntityTag CONTROL_POINT_ENABLED = Internal.CONTROL_POINT_ENABLED;

    /** Shorthand base tag for control point related entities. */
    EntityTag CP = Internal.CP;

    // --- 🤝 Social & UI Tags ---

    /** Used for logic queries checking win/victory status for an entity. */
    EntityTag AM_I_WINNING = Internal.AM_I_WINNING;

    /** Visual/Logic flag for players actively looking for a teammate. */
    EntityTag LOOKING_FOR_TEAM_MATE = Internal.LOOKING_FOR_TEAM_MATE;

    // --- 🛰️ Core Contract ---

    /**
     * Retrieves the tag identifier formatted as PascalCase.
     * <p><b>Example:</b> {@code GAME_STARTED.getTagName()} -> {@code "GameStarted"}.</p>
     * @return The non-null string value used in Minecraft commands and NBT.
     */
    String getTagName();

    /**
     * Validates that the tag name is compatible with Minecraft command syntax.
     * <p><b>Error Catching:</b> Rejects names with spaces, commas, or brackets which
     * would break target selectors (e.g., {@code @e[tag=PascalCaseTag]}).</p>
     * @throws IllegalStateException if the tag is null, empty, or contains illegal characters.
     */
    default void validate() throws IllegalStateException {
        String tag = getTagName();
        if (tag == null || tag.isBlank()) {
            throw new IllegalStateException("Entity tag name cannot be null or blank.");
        }

        // Regex check for characters that break target selectors: [space] , = ] [
        if (tag.matches(".*[ ,=\\]\\[].*")) {
            throw new IllegalStateException("Entity tag '" + tag + "' contains illegal selector characters.");
        }
    }

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates a numbered variant of an existing tag for multiple objective tracking.
     * <p><b>Example:</b> {@code EntityTag.indexed(EntityTag.CP, 1)} -> {@code "Cp1"}.</p>
     * @param base The base {@link EntityTag} to extend.
     * @param index The numerical version to append.
     * @return A new type-safe indexed EntityTag instance.
     * @throws NullPointerException if base is null.
     * @throws IllegalArgumentException if index is negative.
     */
    static EntityTag indexed(EntityTag base, int index) {
        Objects.requireNonNull(base, "Base tag cannot be null for indexing.");
        if (index < 0) {
            throw new IllegalArgumentException("Tag index cannot be negative: " + index);
        }
        return new IndexedTag(base.getTagName(), index);
    }

    /**
     * Formats a raw SNAKE_CASE string into PascalCase.
     * @param input The raw enum name or string.
     * @return The formatted string (e.g., "IRON_MAN" -> "IronMan").
     */
    private static String formatPascal(String input) {
        if (input == null || input.isEmpty()) return "";
        try {
            return Arrays.stream(input.split("_"))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
                    .collect(Collectors.joining());
        } catch (Exception e) {
            // Logical fallback to original string if split/stream fails
            return input;
        }
    }

    // --- 📦 Internal Implementations ---

    /**
     * Internal singleton registry for static entity tags.
     * <p>This enum is private to ensure the interface constants are the only entry points.</p>
     */
    enum Internal implements EntityTag {
        GAME_STARTED, RESPAWN, RESPAWN_DISABLED, DEBUG,
        TRAITOR, TRAITORS_ASSIGNED, DONT_MAKE_TRAITOR,
        CARE_PACKAGE, CARE_PACKAGES_DROPPED, PREDICTION_CANDIDATE, PREDICTIONS_COMPLETED,
        IRON_MAN, IRON_MAN_CANDIDATE, COLLAR_CHECK, IS_FLYING,
        CONTROL_POINT_CAPTURED, CONTROL_POINT_ENABLED, CP,
        AM_I_WINNING, LOOKING_FOR_TEAM_MATE;

        private final String pascalName;

        Internal() {
            this.pascalName = EntityTag.formatPascal(this.name());
            // Validates the PascalCase name immediately upon class loading
            this.validate();
        }

        @Override public String getTagName() { return pascalName; }
        @Override public String toString() { return getTagName(); }
    }

    /**
     * A versioned tag implementation (e.g., BaseName1, BaseName2).
     * @param baseName The base PascalCase string.
     * @param index The numerical suffix.
     */
    record IndexedTag(String baseName, int index) implements EntityTag {
        /**
         * Ensures the combined tag name is valid for Minecraft upon construction.
         */
        public IndexedTag {
            this.validate();
        }

        @Override public String getTagName() { return baseName + index; }
        @Override public String toString() { return getTagName(); }
    }
}