package uhc.arguments.entity;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.EntityTag;
import uhc.resource.EntityType;
import uhc.game.GameMode;
import uhc.score.ScoreObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
     * @param limit The number of entities to target (e.g., 1). Must be positive.
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

    /**
     * Sets the maximum distance for the selector, equivalent to "distance=..[range]".
     * @param range The maximum distance (e.g., 10 results in "..10").
     */
    public SelectorArgumentsBuilder distance(int range) {
        if (range < 0) {
            throw new IllegalArgumentException("Distance range must be non-negative.");
        }
        // Defaults to a maximum distance range: "..10"
        this.arguments.add("distance=.." + range);
        return this;
    }

    // --- Coordinate & Volume Arguments ---

    /**
     * Sets the required X-coordinate for the selection origin.
     * Often used in conjunction with {@code dy} and {@code dz}.
     * @param x The absolute or relative X coordinate.
     */
    public SelectorArgumentsBuilder x(String x) {
        if (x == null || x.trim().isEmpty()) {
            throw new IllegalArgumentException("X coordinate cannot be null or empty.");
        }
        this.arguments.add("x=" + x.trim());
        return this;
    }

    /**
     * Sets the required Y-coordinate for the selection origin.
     * @param y The absolute or relative Y coordinate.
     */
    public SelectorArgumentsBuilder y(String y) {
        if (y == null || y.trim().isEmpty()) {
            throw new IllegalArgumentException("Y coordinate cannot be null or empty.");
        }
        this.arguments.add("y=" + y.trim());
        return this;
    }

    /**
     * Sets the required Z-coordinate for the selection origin.
     * @param z The absolute or relative Z coordinate.
     */
    public SelectorArgumentsBuilder z(String z) {
        if (z == null || z.trim().isEmpty()) {
            throw new IllegalArgumentException("Z coordinate cannot be null or empty.");
        }
        this.arguments.add("z=" + z.trim());
        return this;
    }

    /**
     * Sets the required dimension of the selection box in the X-axis (delta X).
     * @param dx The width of the selection box in the X-axis.
     */
    public SelectorArgumentsBuilder dx(double dx) {
        this.arguments.add("dx=" + dx);
        return this;
    }

    /**
     * Sets the required dimension of the selection box in the Y-axis (delta Y).
     * @param dy The height of the selection box in the Y-axis.
     */
    public SelectorArgumentsBuilder dy(double dy) {
        this.arguments.add("dy=" + dy);
        return this;
    }

    /**
     * Sets the required dimension of the selection box in the Z-axis (delta Z).
     * @param dz The depth of the selection box in the Z-axis.
     */
    public SelectorArgumentsBuilder dz(double dz) {
        this.arguments.add("dz=" + dz);
        return this;
    }

    // --- Rotation Arguments ---

    /**
     * Filters entities by their horizontal rotation (yaw).
     * @param range A valid Minecraft range string (e.g., "-90..90").
     */
    public SelectorArgumentsBuilder xRotation(String range) {
        if (range == null || range.trim().isEmpty()) {
            throw new IllegalArgumentException("X rotation range cannot be null or empty.");
        }
        this.arguments.add("x_rotation=" + range.trim());
        return this;
    }

    /**
     * Filters entities by their vertical rotation (pitch).
     * @param range A valid Minecraft range string (e.g., "-90..90").
     */
    public SelectorArgumentsBuilder yRotation(String range) {
        if (range == null || range.trim().isEmpty()) {
            throw new IllegalArgumentException("Y rotation range cannot be null or empty.");
        }
        this.arguments.add("y_rotation=" + range.trim());
        return this;
    }


    // --- Level/Experience Argument ---

    /**
     * Filters players by their experience level range.
     * @param range A valid Minecraft range string (e.g., "10..20", "..5").
     */
    public SelectorArgumentsBuilder level(String range) {
        if (range == null || range.trim().isEmpty()) {
            throw new IllegalArgumentException("Level range cannot be null or empty.");
        }
        this.arguments.add("level=" + range.trim());
        return this;
    }


    // --- Type Arguments ---

    /**
     * Includes a specific entity type in the selection.
     * @param type The required EntityType.
     */
    public SelectorArgumentsBuilder type(EntityType type) {
        return type(type, false);
    }

    /**
     * Includes or excludes a specific entity type in the selection.
     * @param type The required EntityType.
     * @param not If true, excludes the type (e.g., {@code type=!cow}).
     */
    public SelectorArgumentsBuilder type(EntityType type, Boolean not) {
        Objects.requireNonNull(type, "EntityType cannot be null.");
        String prefix = not ? "!" : "";
        this.arguments.add("type=" + prefix + type.getResourceLocation());
        return this;
    }


    // --- Tag Arguments ---

    /**
     * Includes entities that have a specific tag.
     * @param tag The tag name (as an EntityTag object).
     */
    public SelectorArgumentsBuilder tag(EntityTag tag) {
        return tag(tag, false);
    }

    /**
     * Includes or excludes entities based on a specific tag.
     * @param tag The tag name (as an EntityTag object).
     * @param not If true, excludes entities with this tag (e.g., {@code tag=!test_tag}).
     */
    public SelectorArgumentsBuilder tag(EntityTag tag, Boolean not) {
        Objects.requireNonNull(tag, "EntityTag cannot be null.");
        String prefix = not ? "!" : "";
        // Assuming tag.toString() provides the raw tag string (e.g., "test_tag")
        this.arguments.add("tag=" + prefix + tag.toString());
        return this;
    }


    // --- Scores Argument ---

    /**
     * Filters players based on their scores in specified objectives.
     * @param scores A map where keys are score objectives and values are range strings or fixed values.
     */
    public SelectorArgumentsBuilder scores(Map<ScoreObjective, Object> scores) {
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("Scores map cannot be null or empty.");
        }

        StringBuilder sb = new StringBuilder("scores={");

        boolean first = true;
        for (var entry : scores.entrySet()) {
            if (!first) sb.append(",");
            // Key is ScoreObjective (which should override toString() to the objective name)
            // Value is the score value/range (int, string, or range)
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }

        sb.append("}");
        arguments.add(sb.toString());

        return this;
    }

    // --- Gamemode Arguments ---

    /**
     * Filters players by a specific game mode.
     * @param gamemode The required GameMode.
     */
    public SelectorArgumentsBuilder gamemode(GameMode gamemode) {
        return gamemode(gamemode, false);
    }

    /**
     * Filters or excludes players by a specific game mode.
     * @param gamemode The required GameMode.
     * @param not If true, excludes players in this game mode (e.g., {@code gamemode=!creative}).
     */
    public SelectorArgumentsBuilder gamemode(GameMode gamemode, boolean not) {
        Objects.requireNonNull(gamemode, "Gamemode cannot be null.");

        String prefix = not ? "!" : "";
        // Assuming GameMode.toString() returns the lowercase mode name (e.g., "survival")
        arguments.add("gamemode=" + prefix + gamemode);
        return this;
    }


    // --- Team Arguments ---

    /**
     * Filters for entities that are **not** currently on a team.
     * Equivalent to {@code team=!}.
     */
    public SelectorArgumentsBuilder team() {
        arguments.add("team=!");
        return this;
    }

    /**
     * Filters for entities based on their team membership.
     * @param not If true, selects entities not on **any** team ({@code team=!}).
     * If false, selects entities on **any** team ({@code team=}).
     */
    public SelectorArgumentsBuilder team(Boolean not) {
        String value = not ? "!" : ""; // team=! vs team=
        arguments.add("team=" + value);
        return this;
    }

    /**
     * Filters for entities on a specific team.
     * @param team The name of the required team.
     */
    public SelectorArgumentsBuilder team(String team) {
        return team(team, false);
    }

    /**
     * Filters for or excludes entities from a specific team.
     * @param team The name of the required team.
     * @param not If true, excludes entities on this team (e.g., {@code team=!red}).
     */
    public SelectorArgumentsBuilder team(String team, boolean not) {
        if (team == null || team.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty.");
        }
        String prefix = not ? "!" : "";
        arguments.add("team=" + prefix + team.trim());
        return this;
    }

    // --- NBT Argument ---

    /**
     * Filters selection by requiring the entity to match the provided NBT structure.
     * @param nbt The root NBT compound tag for the entity.
     * @param not If true, excludes entities that match the NBT structure.
     */
    public SelectorArgumentsBuilder nbt(CompoundTag nbt, boolean not) {
        Objects.requireNonNull(nbt, "NBT tag cannot be null.");
        String prefix = not ? "!" : "";
        // Assuming TagConverter.toJson outputs the required Minecraft string format {key:value, ...}
        this.arguments.add("nbt=" + prefix + TagConverter.toJson(nbt));
        return this;
    }

    /**
     * Filters selection by requiring the entity to match the provided NBT structure.
     * @param nbt The root NBT compound tag for the entity.
     */
    public SelectorArgumentsBuilder nbt(CompoundTag nbt) {
        return nbt(nbt, false);
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