package uhc.arguments.entity;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.EntityTag;
import uhc.resource.entity.EntityId;
import uhc.game.GameModeId;
import uhc.score.ScoreObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 🎯 **Target Selector Argument Builder**
 * <p>
 * Fluent builder for creating type-safe Minecraft target selector arguments
 * (e.g., {@code [limit=1, type=!cow, distance=..10]}).
 * </p>
 */
public class SelectorArgumentsBuilder {

    /** Internal list of formatted "key=value" strings. */
    private final List<String> arguments = new ArrayList<>();

    /** Private constructor to enforce factory method usage. */
    private SelectorArgumentsBuilder() {}

    /**
     * Initializes a new SelectorArgumentsBuilder.
     * @return A fresh builder instance.
     */
    public static SelectorArgumentsBuilder create() {
        return new SelectorArgumentsBuilder();
    }

    // --- Core Arguments ---

    /**
     * Sets the maximum number of entities to target.
     * @param limit The number of entities. Must be 1 or greater.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder limit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be a positive integer.");
        }
        this.arguments.add("limit=" + limit);
        return this;
    }

    /**
     * Sets the distance range (e.g., "5..10").
     * @param range Raw range string.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder distance(String range) {
        if (range == null || range.isBlank()) {
            throw new IllegalArgumentException("Distance range cannot be null or empty.");
        }
        this.arguments.add("distance=" + range.trim());
        return this;
    }

    /**
     * Sets the maximum distance, formatting it as "..range".
     * @param range Maximum distance. Must be non-negative.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder distance(int range) {
        if (range < 0) {
            throw new IllegalArgumentException("Distance range must be non-negative.");
        }
        this.arguments.add("distance=.." + range);
        return this;
    }

    // --- Coordinate & Volume Arguments ---

    /** Sets the selection origin X coordinate. */
    public SelectorArgumentsBuilder x(String x) {
        if (x == null || x.isBlank()) throw new IllegalArgumentException("X cannot be blank.");
        this.arguments.add("x=" + x.trim());
        return this;
    }

    /** Sets the selection origin Y coordinate. */
    public SelectorArgumentsBuilder y(String y) {
        if (y == null || y.isBlank()) throw new IllegalArgumentException("Y cannot be blank.");
        this.arguments.add("y=" + y.trim());
        return this;
    }

    /** Sets the selection origin Z coordinate. */
    public SelectorArgumentsBuilder z(String z) {
        if (z == null || z.isBlank()) throw new IllegalArgumentException("Z cannot be blank.");
        this.arguments.add("z=" + z.trim());
        return this;
    }

    /** Sets the width of the selection volume (delta X). */
    public SelectorArgumentsBuilder dx(double dx) {
        this.arguments.add("dx=" + dx);
        return this;
    }

    /** Sets the height of the selection volume (delta Y). */
    public SelectorArgumentsBuilder dy(double dy) {
        this.arguments.add("dy=" + dy);
        return this;
    }

    /** Sets the depth of the selection volume (delta Z). */
    public SelectorArgumentsBuilder dz(double dz) {
        this.arguments.add("dz=" + dz);
        return this;
    }

    // --- Rotation Arguments ---

    /** Filters entities by horizontal rotation (yaw) range. */
    public SelectorArgumentsBuilder xRotation(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("X rotation range cannot be blank.");
        this.arguments.add("x_rotation=" + range.trim());
        return this;
    }

    /** Filters entities by vertical rotation (pitch) range. */
    public SelectorArgumentsBuilder yRotation(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("Y rotation range cannot be blank.");
        this.arguments.add("y_rotation=" + range.trim());
        return this;
    }

    // --- Level/Experience Argument ---

    /** Filters players by their experience level range. */
    public SelectorArgumentsBuilder level(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("Level range cannot be blank.");
        this.arguments.add("level=" + range.trim());
        return this;
    }

    // --- Type Arguments ---

    /** Filters by entity type. */
    public SelectorArgumentsBuilder type(EntityId type) {
        return type(type, false);
    }

    /** Filters or excludes by entity type. */
    public SelectorArgumentsBuilder type(EntityId type, Boolean not) {
        Objects.requireNonNull(type, "EntityType cannot be null.");
        String prefix = (not != null && not) ? "!" : "";
        this.arguments.add("type=" + prefix + type.getResourceLocation());
        return this;
    }

    // --- Tag Arguments ---

    /** Filters by entity scoreboard tag. */
    public SelectorArgumentsBuilder tag(EntityTag tag) {
        return tag(tag, false);
    }

    /** Filters or excludes by entity scoreboard tag. */
    public SelectorArgumentsBuilder tag(EntityTag tag, Boolean not) {
        Objects.requireNonNull(tag, "EntityTag cannot be null.");
        String prefix = (not != null && not) ? "!" : "";
        this.arguments.add("tag=" + prefix + tag.toString());
        return this;
    }

    // --- Scores Argument ---

    /**
     * Filters entities by scoreboard values.
     * @param scores Map of Objective to Value/Range.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder scores(Map<ScoreObjective, Object> scores) {
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("Scores map cannot be null or empty.");
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

    // --- Gamemode Arguments ---

    /** Filters players by game mode. */
    public SelectorArgumentsBuilder gamemode(GameModeId gamemode) {
        return gamemode(gamemode, false);
    }

    /** Filters or excludes players by game mode. */
    public SelectorArgumentsBuilder gamemode(GameModeId gamemode, boolean not) {
        Objects.requireNonNull(gamemode, "Gamemode cannot be null.");
        String prefix = not ? "!" : "";
        arguments.add("gamemode=" + prefix + gamemode);
        return this;
    }

    // --- Team Arguments ---

    /** Selects entities NOT on any team. */
    public SelectorArgumentsBuilder team() {
        arguments.add("team=!");
        return this;
    }

    /** Selects entities based on team presence. */
    public SelectorArgumentsBuilder team(Boolean not) {
        String value = (not != null && not) ? "!" : "";
        arguments.add("team=" + value);
        return this;
    }

    /** Filters for entities on a specific team. */
    public SelectorArgumentsBuilder team(String team) {
        return team(team, false);
    }

    /** Filters or excludes entities from a specific team. */
    public SelectorArgumentsBuilder team(String team, boolean not) {
        if (team == null || team.isBlank()) throw new IllegalArgumentException("Team name cannot be blank.");
        String prefix = not ? "!" : "";
        arguments.add("team=" + prefix + team.trim());
        return this;
    }

    // --- NBT Argument ---

    /**
     * Filters selection by NBT structure match.
     * <b>Note:</b> Performance intensive. Use with other filters.
     * @param nbt Entity NBT builder.
     * @param not If true, excludes matching entities.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder nbt(EntityNBT<?> nbt, boolean not) {
        Objects.requireNonNull(nbt, "EntityNBT builder cannot be null.");
        CompoundTag tag = nbt.build();
        String prefix = not ? "!" : "";
        this.arguments.add("nbt=" + prefix + TagConverter.toJson(tag));
        return this;
    }

    /** Filters selection by NBT structure match. */
    public SelectorArgumentsBuilder nbt(EntityNBT<?> nbt) {
        return nbt(nbt, false);
    }

    /**
     * Finalizes the builder into a square-bracketed string.
     * @return Formatted arguments, e.g., "[limit=1]" or empty string if no args.
     */
    public String build() {
        if (arguments.isEmpty()) return "";
        return "[" + String.join(",", arguments) + "]";
    }
}