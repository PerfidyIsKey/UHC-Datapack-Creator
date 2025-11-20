package arguments.targetselector;

import shared.EntityTag;
import shared.EntityType;
import shared.GameMode;
import shared.ScoreObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Fluent builder for creating type-safe target selector arguments (e.g., [limit=1, type=!cow]).
 * This class handles the formatting of arguments into the required comma-separated string
 * enclosed in square brackets.
 */
public class SelectorArgumentsBuilder {
    private final List<String> arguments = new ArrayList<>();

    private SelectorArgumentsBuilder() {}

    public static SelectorArgumentsBuilder create() {
        return new SelectorArgumentsBuilder();
    }

    // --- Core Arguments ---

    /**
     * Sets the maximum number of entities to target.
     * @param limit The number of entities to target (e.g., 1).
     */
    public SelectorArgumentsBuilder limit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be a positive integer.");
        }
        this.arguments.add("limit=" + limit);
        return this;
    }

    /**
     * Sets the distance range for the selector.
     * @param range A valid Minecraft range string (e.g., "5..10", "..5", "10..").
     */
    public SelectorArgumentsBuilder distance(String range) {
        if (range == null || range.trim().isEmpty()) {
            throw new IllegalArgumentException("Distance range cannot be null or empty.");
        }
        this.arguments.add("distance=" + range.trim());
        return this;
    }

    public SelectorArgumentsBuilder distance(int range) {
        this.arguments.add("distance=" + range);
        return this;
    }

    // --- Type Arguments ---

    /**
     * Includes a specific entity type in the selection.
     * @param type The required EntityType.
     */
    public SelectorArgumentsBuilder type(EntityType type) {
        this.arguments.add("type=" + type.getResourceLocation());
        return this;
    }

    public SelectorArgumentsBuilder type(EntityType type, Boolean not) {

        String prefix = not ? "!" : "";

        this.arguments.add("type=" + prefix + type.getResourceLocation());
        return this;
    }


    // --- Tag Arguments ---

    /**
     * Includes entities that have a specific tag.
     * @param tag The tag name (string).
     */
    public SelectorArgumentsBuilder tag(EntityTag tag) {
        this.arguments.add("tag=" + tag);
        return this;
    }

    public SelectorArgumentsBuilder tag(EntityTag tag, Boolean not) {
        String prefix = not ? "!" : "";

        this.arguments.add("tag=" + prefix + tag);
        return this;
    }


    public SelectorArgumentsBuilder scores(Map<ScoreObjective, Object> scores) {
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("Scores cannot be null or empty.");
        }

        StringBuilder sb = new StringBuilder("scores={");

        boolean first = true;
        for (var entry : scores.entrySet()) {
            if (!first) sb.append(",");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }

        sb.append("}");
        arguments.add(sb.toString());

        return this;
    }

    public SelectorArgumentsBuilder gamemode(GameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException("Gamemode cannot be null or empty.");

        arguments.add("gamemode=" + gamemode);
        return this;
    }

    public SelectorArgumentsBuilder gamemode(GameMode gamemode, boolean not) {
        if (gamemode == null) throw new IllegalArgumentException("Gamemode cannot be null or empty.");

        String prefix = not ? "!" : "";
        arguments.add("gamemode=" + prefix + gamemode);
        return this;
    }


    /**
     * Finalizes the builder and returns the formatted selector arguments string,
     * including the enclosing square brackets, e.g., "[limit=1,tag=!test_tag]".
     * Returns an empty string if no arguments were added.
     */
    public String build() {
        if (arguments.isEmpty()) {
            return "";
        }
        // Format: [arg1=val1,arg2=val2]
        String content = String.join(",", arguments);
        return "[" + content + "]";
    }
}