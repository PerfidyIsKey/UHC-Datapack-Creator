package uhc.command.commands;

import uhc.arguments.coordinate.Vec2;
import uhc.arguments.time.VariableGameTime;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🗺️ **WorldBorder Command Builder (WorldBorderCommand)**
 * <p>
 * Provides a flattened, type-safe API for constructing Minecraft {@code /worldborder} commands.
 * This class manages play area boundaries, expansion/shrinkage rates, and player safety mechanics.
 * </p>
 * <p>
 * <b>Strict Policy:</b> This class does not provide default fallbacks for invalid parameters.
 * If input exceeds Minecraft engine limits or is null where required, an explicit exception is thrown.
 * </p>
 */
public class WorldBorderCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The internal formatted sub-command segment.
     * Contains the action keyword and its respective arguments (e.g., "set 100 60").
     */
    private final String instruction;

    /** * Minecraft's absolute maximum coordinate for world border size/distance.
     * The engine typically hard-caps the border diameter at 59,999,968 blocks.
     */
    private static final double MAX_SIZE = 59999968.0;

    // --- 🏗️ Private Constructor ---

    /**
     * Constructs a WorldBorderCommand with a pre-validated instruction string.
     * @param instruction The sub-command and its arguments.
     * @throws NullPointerException if instruction is null.
     */
    private WorldBorderCommand(String instruction) {
        this.instruction = Objects.requireNonNull(instruction, "WorldBorder Error: Internal instruction string cannot be null.");
    }

    // --- 🎯 Static Entry Points: Size Management (ADD / SET) ---

    /**
     * Constructs a command to add a relative distance to the current world border diameter.
     * <p>Syntax: {@code /worldborder add <distance>}</p>
     * @param distance Blocks to add (use negative values to shrink).
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if distance exceeds engine limits.
     */
    public static WorldBorderCommand add(double distance) {
        return add(distance, null);
    }

    /**
     * Constructs a command to add a relative distance to the current diameter over a period of time.
     * <p>Syntax: {@code /worldborder add <distance> <time>}</p>
     * @param distance Blocks to add.
     * @param time     The {@link VariableGameTime} duration for the transition; can be null for instant.
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if distance exceeds engine limits.
     */
    public static WorldBorderCommand add(double distance, VariableGameTime time) {
        validateDistance(distance, false);
        String cmd = "add " + distance + (time != null ? " " + time : "");
        return new WorldBorderCommand(cmd);
    }

    /**
     * Constructs a command to set the world border to an absolute diameter.
     * <p>Syntax: {@code /worldborder set <distance>}</p>
     * @param distance The target diameter in blocks.
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if distance is negative or exceeds limits.
     */
    public static WorldBorderCommand set(double distance) {
        return set(distance, null);
    }

    /**
     * Constructs a command to set the world border to an absolute diameter over a period of time.
     * <p>Syntax: {@code /worldborder set <distance> <time>}</p>
     * @param distance The target diameter.
     * @param time     The {@link VariableGameTime} duration for the transition; can be null for instant.
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if distance is negative or exceeds limits.
     */
    public static WorldBorderCommand set(double distance, VariableGameTime time) {
        validateDistance(distance, true);
        String cmd = "set " + distance + (time != null ? " " + time : "");
        return new WorldBorderCommand(cmd);
    }

    // --- 🎯 Static Entry Points: Damage Mechanics ---

    /**
     * Sets the amount of damage dealt per second for every block a player is outside the border buffer.
     * <p>Syntax: {@code /worldborder damage amount <damagePerBlock>}</p>
     * @param damagePerBlock Damage amount per block (must be non-negative).
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if damage is negative.
     */
    public static WorldBorderCommand damageAmount(float damagePerBlock) {
        if (damagePerBlock < 0.0f) throw new IllegalArgumentException("WorldBorder Error: Damage amount cannot be negative.");
        return new WorldBorderCommand("damage amount " + damagePerBlock);
    }

    /**
     * Sets the distance players can move outside the border before taking damage.
     * <p>Syntax: {@code /worldborder damage buffer <distance>}</p>
     * @param distance Buffer distance in blocks (must be non-negative).
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if distance is negative.
     */
    public static WorldBorderCommand damageBuffer(float distance) {
        if (distance < 0.0f) throw new IllegalArgumentException("WorldBorder Error: Buffer distance cannot be negative.");
        return new WorldBorderCommand("damage buffer " + distance);
    }

    // --- 🎯 Static Entry Points: Warning Indicators ---

    /**
     * Sets the distance from the border at which a player's screen begins to tint red.
     * <p>Syntax: {@code /worldborder warning distance <distance>}</p>
     * @param distance Warning distance in blocks (must be non-negative).
     * @return A new {@link WorldBorderCommand} instance.
     * @throws IllegalArgumentException if distance is negative.
     */
    public static WorldBorderCommand warningDistance(int distance) {
        if (distance < 0) throw new IllegalArgumentException("WorldBorder Error: Warning distance cannot be negative.");
        return new WorldBorderCommand("warning distance " + distance);
    }

    /**
     * Sets the time before a shrinking border reaches a player at which the red tint appears.
     * <p>Syntax: {@code /worldborder warning time <time>}</p>
     * @param time The {@link VariableGameTime} duration for the warning; must not be null.
     * @return A new {@link WorldBorderCommand} instance.
     * @throws NullPointerException if time is null.
     */
    public static WorldBorderCommand warningTime(VariableGameTime time) {
        Objects.requireNonNull(time, "WorldBorder Error: Warning time object cannot be null.");
        return new WorldBorderCommand("warning time " + time);
    }

    // --- 🎯 Static Entry Points: Utility ---

    /**
     * Sets the central point of the world border.
     * <p>Syntax: {@code /worldborder center <x> <z>}</p>
     * @param pos The {@link Vec2} absolute coordinates for the center; must not be null.
     * @return A new {@link WorldBorderCommand} instance.
     * @throws NullPointerException if pos is null.
     */
    public static WorldBorderCommand center(Vec2 pos) {
        Objects.requireNonNull(pos, "WorldBorder Error: Center position coordinates cannot be null.");
        return new WorldBorderCommand("center " + pos);
    }

    /**
     * Constructs a command to query the current diameter of the world border.
     * <p>Syntax: {@code /worldborder get}</p>
     * @return A new {@link WorldBorderCommand} instance for the query.
     */
    public static WorldBorderCommand get() {
        return new WorldBorderCommand("get");
    }

    // --- ⚙️ Internal Technical Logic ---

    /**
     * Validates that the distance provided is within the Minecraft world border engine limits.
     * @param distance The diameter or relative change being requested.
     * @param absolute If true, validates that the distance is not negative (required for 'set').
     * @throws IllegalArgumentException if distance values are logically or technically invalid.
     */
    private static void validateDistance(double distance, boolean absolute) {
        if (absolute && distance < 0) {
            throw new IllegalArgumentException("WorldBorder Error: Diameter cannot be negative when using 'set'.");
        }
        if (Math.abs(distance) > MAX_SIZE) {
            throw new IllegalArgumentException("WorldBorder Error: Distance " + distance + " exceeds Minecraft's max limit: " + MAX_SIZE);
        }
    }

    /**
     * Generates the finalized Minecraft command string.
     * @return The complete command string (e.g., "worldborder set 500 120").
     */
    @Override
    public String generate() {
        return "worldborder " + this.instruction;
    }

    /**
     * Returns the generated command string for logging or execution.
     * <p><b>No-Fallback Policy:</b> If generation fails due to internal state,
     * a clear error message is returned as the string representation.</p>
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        try {
            return generate();
        } catch (Exception e) {
            return "/* WorldBorder Error: " + e.getMessage() + " */";
        }
    }
}