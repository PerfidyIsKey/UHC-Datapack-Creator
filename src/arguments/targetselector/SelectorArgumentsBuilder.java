package arguments.targetselector;

import shared.EntityType;
import java.util.ArrayList;
import java.util.List;

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

    // --- Type Arguments ---

    /**
     * Includes a specific entity type in the selection.
     * @param type The required EntityType.
     */
    public SelectorArgumentsBuilder type(EntityType type) {
        this.arguments.add("type=" + type.getResourceLocation());
        return this;
    }

    /**
     * Excludes a specific entity type from the selection.
     * @param type The EntityType to exclude.
     */
    public SelectorArgumentsBuilder excludeType(EntityType type) {
        this.arguments.add("type=!" + type.getResourceLocation());
        return this;
    }

    // --- Tag Arguments ---

    /**
     * Includes entities that have a specific tag.
     * @param tag The tag name (string).
     */
    public SelectorArgumentsBuilder tag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty.");
        }
        this.arguments.add("tag=" + tag.trim());
        return this;
    }

    /**
     * Excludes entities that have a specific tag.
     * @param tag The tag name (string).
     */
    public SelectorArgumentsBuilder excludeTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty.");
        }
        this.arguments.add("tag=!" + tag.trim());
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