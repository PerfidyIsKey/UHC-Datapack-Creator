package uhc.arguments.entity;

import uhc.data.nbt.BuildableNBT;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.tag.EntityTag;
import uhc.resource.entity.EntityId;
import uhc.resource.gameplay.GameModeId;
import uhc.score.ScoreObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 🎯 **Target Selector Argument Builder**
 * <p>
 * Fluent builder for creating type-safe Minecraft target selector arguments.
 * It produces the square-bracketed string appended to selectors (e.g., {@code @e[...]}).
 * </p>
 */
public class SelectorArgumentsBuilder {

    // --- Private Fields ---

    /** * Internal storage for formatted "key=value" strings.
     * These are joined by commas during the {@link #build()} phase.
     */
    private final List<String> arguments = new ArrayList<>();

    // --- Constructors & Factories ---

    /** * Private constructor to enforce the use of the static factory method.
     */
    private SelectorArgumentsBuilder() {}

    /**
     * Initializes a new Target Selector Argument builder.
     * @return A fresh builder instance.
     */
    public static SelectorArgumentsBuilder create() {
        return new SelectorArgumentsBuilder();
    }

    // --- 🔢 Numerical & Core Arguments ---

    /**
     * Sets the maximum number of entities to target (limit).
     * @param limit The number of entities. Must be 1 or greater.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if limit is less than 1.
     */
    public SelectorArgumentsBuilder limit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Selector limit must be at least 1.");
        }
        this.arguments.add("limit=" + limit);
        return this;
    }

    /**
     * Sets the distance range using a raw string (e.g., "5..10", "10..", "..5").
     * @param range The raw range string.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if range is null or blank.
     */
    public SelectorArgumentsBuilder distance(String range) {
        if (range == null || range.isBlank()) {
            throw new IllegalArgumentException("Distance range string cannot be null or empty.");
        }
        this.arguments.add("distance=" + range.trim());
        return this;
    }

    /**
     * Sets the maximum distance, automatically formatting it as an upper bound (e.g., "..10").
     * @param range Maximum distance. Must be non-negative.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if range is negative.
     */
    public SelectorArgumentsBuilder distance(int range) {
        if (range < 0) {
            throw new IllegalArgumentException("Distance range cannot be negative.");
        }
        this.arguments.add("distance=.." + range);
        return this;
    }

    /**
     * Filters players by their experience level range (e.g., "10..").
     * @param range The level range string.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if range is blank.
     */
    public SelectorArgumentsBuilder level(String range) {
        if (range == null || range.isBlank()) {
            throw new IllegalArgumentException("Level range cannot be blank.");
        }
        this.arguments.add("level=" + range.trim());
        return this;
    }

    // --- 📍 Spatial & Volume Arguments ---

    /** * Sets the selection origin X coordinate.
     * @param x The X coordinate string (supports relative ~ or absolute values).
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder x(String x) {
        if (x == null || x.isBlank()) throw new IllegalArgumentException("X origin cannot be blank.");
        this.arguments.add("x=" + x.trim());
        return this;
    }

    /** * Sets the selection origin Y coordinate.
     * @param y The Y coordinate string.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder y(String y) {
        if (y == null || y.isBlank()) throw new IllegalArgumentException("Y origin cannot be blank.");
        this.arguments.add("y=" + y.trim());
        return this;
    }

    /** * Sets the selection origin Z coordinate.
     * @param z The Z coordinate string.
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder z(String z) {
        if (z == null || z.isBlank()) throw new IllegalArgumentException("Z origin cannot be blank.");
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

    /** * Filters entities by horizontal rotation (yaw) range.
     * @param range Yaw range (e.g., "-180..180").
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder xRotation(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("X rotation range cannot be blank.");
        this.arguments.add("x_rotation=" + range.trim());
        return this;
    }

    /** * Filters entities by vertical rotation (pitch) range.
     * @param range Pitch range (e.g., "-90..90").
     * @return This builder for chaining.
     */
    public SelectorArgumentsBuilder yRotation(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("Y rotation range cannot be blank.");
        this.arguments.add("y_rotation=" + range.trim());
        return this;
    }

    // --- 🏷️ Registry & Identity Arguments ---

    /** Filters selection by entity type. */
    public SelectorArgumentsBuilder type(EntityId type) {
        return type(type, false);
    }

    /** * Filters or excludes selection by entity type.
     * @param type The {@link EntityId} to target.
     * @param not If true, uses the '!' exclusion prefix.
     * @return This builder instance for chaining.
     * @throws NullPointerException if type is null.
     */
    public SelectorArgumentsBuilder type(EntityId type, Boolean not) {
        Objects.requireNonNull(type, "EntityType cannot be null.");
        String prefix = (not != null && not) ? "!" : "";
        this.arguments.add("type=" + prefix + type.getResourceLocation());
        return this;
    }

    /** Filters selection by scoreboard tag. */
    public SelectorArgumentsBuilder tag(EntityTag tag) {
        return tag(tag, false);
    }

    /** * Filters or excludes selection by scoreboard tag.
     * @param tag The {@link EntityTag} to target.
     * @param not If true, uses the '!' exclusion prefix.
     * @return This builder instance for chaining.
     * @throws NullPointerException if tag is null.
     */
    public SelectorArgumentsBuilder tag(EntityTag tag, Boolean not) {
        Objects.requireNonNull(tag, "EntityTag cannot be null.");
        String prefix = (not != null && not) ? "!" : "";
        this.arguments.add("tag=" + prefix + tag.toString());
        return this;
    }

    /** Filters players by game mode. */
    public SelectorArgumentsBuilder gamemode(GameModeId gamemode) {
        return gamemode(gamemode, false);
    }

    /** * Filters or excludes players by game mode.
     * @param gamemode The {@link GameModeId} enum value.
     * @param not If true, uses the '!' exclusion prefix.
     * @return This builder instance for chaining.
     * @throws NullPointerException if gamemode is null.
     */
    public SelectorArgumentsBuilder gamemode(GameModeId gamemode, boolean not) {
        Objects.requireNonNull(gamemode, "Gamemode cannot be null.");
        String prefix = not ? "!" : "";
        this.arguments.add("gamemode=" + prefix + gamemode.name().toLowerCase());
        return this;
    }

    // --- 👥 Team & Score Arguments ---

    /** Filters entities based on team presence or absolute absence. */
    public SelectorArgumentsBuilder team(Boolean not) {
        String value = (not != null && not) ? "!" : "";
        arguments.add("team=" + value);
        return this;
    }

    /** Filters for entities on a specific named team. */
    public SelectorArgumentsBuilder team(String team) {
        return team(team, false);
    }

    /** * Filters or excludes entities from a specific team.
     * @param team The team name.
     * @param not If true, uses the '!' exclusion prefix.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if team name is blank.
     */
    public SelectorArgumentsBuilder team(String team, boolean not) {
        if (team == null || team.isBlank()) throw new IllegalArgumentException("Team name cannot be blank.");
        String prefix = not ? "!" : "";
        arguments.add("team=" + prefix + team.trim());
        return this;
    }

    /**
     * Filters entities by complex scoreboard values.
     * @param scores Map of Scoreboard Objective to the target value or range.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if scores map is null or empty.
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

    // --- 📦 Technical Arguments ---

    /**
     * Filters selection by NBT structure match.
     * <p><b>Performance Warning:</b> NBT checks are CPU-intensive. Place this last in your builder chain.</p>
     * @param nbt The {@link BuildableNBT} representing the required data structure.
     * @param not If true, excludes entities that match this NBT.
     * @return This builder instance for chaining.
     * @throws NullPointerException if nbt is null.
     */
    public SelectorArgumentsBuilder nbt(BuildableNBT nbt, boolean not) {
        Objects.requireNonNull(nbt, "BuildableNBT cannot be null.");
        CompoundTag tag = nbt.build();
        String prefix = not ? "!" : "";
        this.arguments.add("nbt=" + prefix + TagConverter.toJson(tag));
        return this;
    }

    /** Filters selection by NBT structure match. */
    public SelectorArgumentsBuilder nbt(BuildableNBT nbt) {
        return nbt(nbt, false);
    }

    // --- 🚀 Terminal Method ---

    /**
     * Finalizes the builder into a square-bracketed string.
     * @return Formatted arguments (e.g., "[limit=1,type=cow]"), or an empty string if no arguments were added.
     */
    public String build() {
        if (arguments.isEmpty()) return "";
        return "[" + String.join(",", arguments) + "]";
    }
}