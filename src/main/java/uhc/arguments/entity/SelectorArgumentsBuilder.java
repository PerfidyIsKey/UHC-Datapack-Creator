package uhc.arguments.entity;

import uhc.data.nbt.BuildableNBT;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.tag.EntityTag;
import uhc.resource.entity.EntityId;
import uhc.resource.gameplay.GameModeId;
import uhc.score.ScoreboardObjectiveId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 🎯 **Target Selector Argument Builder**
 * <p>
 * A fluent, type-safe builder designed to construct the argument block of a Minecraft
 * target selector (e.g., the part inside {@code @a[...]}).
 * </p>
 * <p>
 * <b>Strict Validation Policy:</b> This builder does not provide default values or
 * fallbacks. If an invalid range, null resource, or empty string is provided, the
 * builder will immediately throw an exception to prevent malformed command execution.
 * </p>
 */
public class SelectorArgumentsBuilder {

    // --- 📂 Internal State ---

    /** * A collection of formatted "key=value" strings ready for joining.
     * Arguments are stored in the order they are added.
     */
    private final List<String> arguments = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /** * Private constructor to enforce usage of the {@link #create()} factory method.
     */
    private SelectorArgumentsBuilder() {}

    /**
     * Creates a new, empty instance of the SelectorArgumentsBuilder.
     * @return A fresh builder instance.
     */
    public static SelectorArgumentsBuilder create() {
        return new SelectorArgumentsBuilder();
    }

    // --- 👤 Identity & Name Arguments ---

    /**
     * Filters entities by their exact display name.
     * @param name The literal name to target.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if the name is null or blank.
     */
    public SelectorArgumentsBuilder name(String name) {
        return name(name, false);
    }

    /**
     * Filters or excludes entities based on their exact display name.
     * @param name The name string.
     * @param not  If true, prepends '!' to exclude entities with this name.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if the name is null or blank.
     */
    public SelectorArgumentsBuilder name(String name, boolean not) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Selector Error: Entity name filter cannot be null or blank.");
        }
        try {
            String prefix = not ? "!" : "";
            this.arguments.add("name=" + prefix + name.trim());
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Selector Error: Critical failure appending name argument.", e);
        }
    }

    // --- 🔢 Numerical & Core Arguments ---

    /**
     * Sets the 'limit' argument to restrict the number of entities selected.
     * @param limit Maximum entity count. Must be 1 or higher.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if limit is less than 1.
     */
    public SelectorArgumentsBuilder limit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Selector Error: 'limit' must be at least 1 (received: " + limit + ").");
        }
        this.arguments.add("limit=" + limit);
        return this;
    }

    /**
     * Filters entities by distance using a raw range string (e.g., "5..10").
     * @param range The range expression.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if range is null or blank.
     */
    public SelectorArgumentsBuilder distance(String range) {
        if (range == null || range.isBlank()) {
            throw new IllegalArgumentException("Selector Error: Distance range string cannot be null or empty.");
        }
        this.arguments.add("distance=" + range.trim());
        return this;
    }

    /**
     * Sets a maximum distance limit (formats as "..range").
     * @param range Maximum distance (radius). Must be non-negative.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if range is negative.
     */
    public SelectorArgumentsBuilder distance(int range) {
        if (range < 0) {
            throw new IllegalArgumentException("Selector Error: Distance radius cannot be negative.");
        }
        this.arguments.add("distance=.." + range);
        return this;
    }

    /**
     * Filters players by their experience level range.
     * @param range Level range expression (e.g., "10..20").
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if range is blank.
     */
    public SelectorArgumentsBuilder level(String range) {
        if (range == null || range.isBlank()) {
            throw new IllegalArgumentException("Selector Error: Experience level range cannot be blank.");
        }
        this.arguments.add("level=" + range.trim());
        return this;
    }

    // --- 📍 Spatial & Volume Arguments ---

    /** * Sets the X-coordinate origin for the selection.
     * @param x Coordinate string (absolute or relative).
     * @return This builder instance for chaining.
     */
    public SelectorArgumentsBuilder x(String x) {
        if (x == null || x.isBlank()) throw new IllegalArgumentException("Selector Error: X origin cannot be blank.");
        this.arguments.add("x=" + x.trim());
        return this;
    }

    /** * Sets the Y-coordinate origin for the selection.
     * @param y Coordinate string (absolute or relative).
     * @return This builder instance for chaining.
     */
    public SelectorArgumentsBuilder y(String y) {
        if (y == null || y.isBlank()) throw new IllegalArgumentException("Selector Error: Y origin cannot be blank.");
        this.arguments.add("y=" + y.trim());
        return this;
    }

    /** * Sets the Z-coordinate origin for the selection.
     * @param z Coordinate string (absolute or relative).
     * @return This builder instance for chaining.
     */
    public SelectorArgumentsBuilder z(String z) {
        if (z == null || z.isBlank()) throw new IllegalArgumentException("Selector Error: Z origin cannot be blank.");
        this.arguments.add("z=" + z.trim());
        return this;
    }

    /** Sets the selection box width (delta X). */
    public SelectorArgumentsBuilder dx(double dx) {
        this.arguments.add("dx=" + dx);
        return this;
    }

    /** Sets the selection box height (delta Y). */
    public SelectorArgumentsBuilder dy(double dy) {
        this.arguments.add("dy=" + dy);
        return this;
    }

    /** Sets the selection box depth (delta Z). */
    public SelectorArgumentsBuilder dz(double dz) {
        this.arguments.add("dz=" + dz);
        return this;
    }

    /** Filters entities by yaw (horizontal rotation) range. */
    public SelectorArgumentsBuilder xRotation(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("Selector Error: Yaw range cannot be blank.");
        this.arguments.add("x_rotation=" + range.trim());
        return this;
    }

    /** Filters entities by pitch (vertical rotation) range. */
    public SelectorArgumentsBuilder yRotation(String range) {
        if (range == null || range.isBlank()) throw new IllegalArgumentException("Selector Error: Pitch range cannot be blank.");
        this.arguments.add("y_rotation=" + range.trim());
        return this;
    }

    // --- 🏷️ Registry & Identity Arguments ---

    /** Filters by entity type (e.g., minecraft:zombie). */
    public SelectorArgumentsBuilder type(EntityId type) {
        return type(type, false);
    }

    /** * Filters or excludes entities by type.
     * @param type The {@link EntityId} (non-null).
     * @param not  True to exclude the type.
     * @return This builder instance for chaining.
     */
    public SelectorArgumentsBuilder type(EntityId type, Boolean not) {
        Objects.requireNonNull(type, "Selector Error: EntityId cannot be null.");
        String prefix = (not != null && not) ? "!" : "";
        this.arguments.add("type=" + prefix + type.getResourceLocation());
        return this;
    }

    /** Filters entities containing the specified scoreboard tag. */
    public SelectorArgumentsBuilder tag(EntityTag tag) {
        return tag(tag, false);
    }

    /** * Filters or excludes entities based on scoreboard tags.
     * @param tag The {@link EntityTag} (non-null).
     * @param not True to exclude the tag.
     * @return This builder instance for chaining.
     */
    public SelectorArgumentsBuilder tag(EntityTag tag, Boolean not) {
        Objects.requireNonNull(tag, "Selector Error: EntityTag cannot be null.");
        String prefix = (not != null && not) ? "!" : "";
        this.arguments.add("tag=" + prefix + tag.toString());
        return this;
    }

    /** Filters players by gamemode. */
    public SelectorArgumentsBuilder gamemode(GameModeId gamemode) {
        return gamemode(gamemode, false);
    }

    /** * Filters or excludes players based on gamemode.
     * @param gamemode The {@link GameModeId} (non-null).
     * @param not      True to exclude the gamemode.
     * @return This builder instance for chaining.
     */
    public SelectorArgumentsBuilder gamemode(GameModeId gamemode, boolean not) {
        Objects.requireNonNull(gamemode, "Selector Error: GamemodeId cannot be null.");
        String prefix = not ? "!" : "";
        this.arguments.add("gamemode=" + prefix + gamemode.name().toLowerCase());
        return this;
    }

    // --- 👥 Team & Score Arguments ---

    /** * Filters entities based on team membership.
     * @param not If true, selects players NOT on a team. If false, selects players ON a team.
     */
    public SelectorArgumentsBuilder team(Boolean not) {
        String value = (not != null && not) ? "!" : "";
        arguments.add("team=" + value);
        return this;
    }

    /** Filters for a specific team. */
    public SelectorArgumentsBuilder team(String team) {
        return team(team, false);
    }

    /** * Filters or excludes entities from a specific team.
     * @param team The team name.
     * @param not  True to exclude the team.
     */
    public SelectorArgumentsBuilder team(String team, boolean not) {
        if (team == null || team.isBlank()) throw new IllegalArgumentException("Selector Error: Team name cannot be blank.");
        String prefix = not ? "!" : "";
        arguments.add("team=" + prefix + team.trim());
        return this;
    }

    /** * Constructs the 'scores' block from a map of objectives and target values.
     * @param scores Map of Objective IDs to values/ranges.
     */
    public SelectorArgumentsBuilder scores(Map<ScoreboardObjectiveId, Object> scores) {
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("Selector Error: Scores map cannot be null or empty.");
        }
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Selector Error: Failed to build score block.", e);
        }
    }

    // --- 📦 Technical Arguments ---

    /** * Filters entities by matching NBT data.
     * @param nbt The {@link BuildableNBT} data.
     */
    public SelectorArgumentsBuilder nbt(BuildableNBT nbt) {
        return nbt(nbt, false);
    }

    /** * Filters or excludes entities by matching NBT data.
     * @param nbt The {@link BuildableNBT} data.
     * @param not True to exclude matches.
     */
    public SelectorArgumentsBuilder nbt(BuildableNBT nbt, boolean not) {
        Objects.requireNonNull(nbt, "Selector Error: BuildableNBT cannot be null.");
        try {
            CompoundTag tag = nbt.build();
            String prefix = not ? "!" : "";
            this.arguments.add("nbt=" + prefix + TagConverter.toJson(tag));
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Selector Error: Critical failure during NBT serialization.", e);
        }
    }

    // --- 🚀 Terminal Method ---

    /**
     * Finalizes the building process and wraps arguments in square brackets.
     * @return A formatted argument string (e.g., "[type=zombie,limit=1]") or empty if no arguments exist.
     */
    public String build() {
        if (arguments.isEmpty()) return "";
        return "[" + String.join(",", arguments) + "]";
    }
}