package uhc.command.commands;

import uhc.arguments.coordinate.Vec2;
import uhc.command.MinecraftCommand;
import java.util.Objects;

/**
 * 🗺️ **WorldBorder Command Builder**
 * <p>
 * Provides a fluent API for the {@code /worldborder} command.
 * This command manages the play area boundary, shrink speed, and player
 * safety/damage mechanics.
 * </p>
 */
public class WorldBorderCommand implements MinecraftCommand {
    private final WorldBorderAction action;

    // ADD/SET: Diameter or size change, transition time in seconds
    private Double sizeChangeOrDiameter;
    private Integer time;

    // CENTER: Target coordinates
    private Vec2 centerPos;

    // DAMAGE: amount (per block), buffer (safe zone outside border)
    private Float damagePerBlock;
    private Float bufferDistance;

    // WARNING: distance (blocks), time (seconds)
    private Integer warningDistance;
    private Integer warningTime;

    private WorldBorderCommand(WorldBorderAction action) {
        this.action = Objects.requireNonNull(action, "WorldBorder action cannot be null.");
    }

    /**
     * Initializes a new WorldBorder builder for a specific sub-command.
     */
    public static WorldBorderCommand create(WorldBorderAction action) {
        return new WorldBorderCommand(action);
    }

    // --- ADD / SET methods ---

    /**
     * Sets the target diameter or change in blocks.
     * @param distance Range: -59,999,968 to 59,999,968.
     */
    public WorldBorderCommand distance(double distance) {
        if (action != WorldBorderAction.ADD && action != WorldBorderAction.SET) {
            throw new IllegalStateException("Distance is only valid for ADD or SET actions.");
        }
        final double MAX_SIZE = 59999968.0;
        if (Math.abs(distance) > MAX_SIZE) {
            throw new IllegalArgumentException("Distance exceeds Minecraft's size limit of " + MAX_SIZE);
        }
        if (action == WorldBorderAction.SET && distance < 0) {
            throw new IllegalArgumentException("World border diameter must be non-negative for SET.");
        }
        this.sizeChangeOrDiameter = distance;
        return this;
    }

    /**
     * Sets the duration (in seconds) for the border to transition to its new size.
     */
    public WorldBorderCommand time(int time) {
        if (action != WorldBorderAction.ADD && action != WorldBorderAction.SET) {
            throw new IllegalStateException("Time is only valid for ADD or SET actions.");
        }
        if (time < 0) {
            throw new IllegalArgumentException("Transition time cannot be negative.");
        }
        this.time = time;
        return this;
    }

    // --- CENTER method ---

    /**
     * Sets the central point of the world border.
     */
    public WorldBorderCommand center(Vec2 pos) {
        if (action != WorldBorderAction.CENTER) {
            throw new IllegalStateException("Center coordinates are only valid for the CENTER action.");
        }
        this.centerPos = Objects.requireNonNull(pos, "Center position cannot be null.");
        return this;
    }

    // --- DAMAGE sub-command methods ---

    /**
     * Sets damage taken per second for every block a player is outside the buffer.
     */
    public WorldBorderCommand damageAmount(float damagePerBlock) {
        if (action != WorldBorderAction.DAMAGE) {
            throw new IllegalStateException("'damage amount' is only valid for the DAMAGE action.");
        }
        if (damagePerBlock < 0.0f) throw new IllegalArgumentException("Damage cannot be negative.");
        if (this.bufferDistance != null) {
            throw new IllegalStateException("Cannot set both damage amount and buffer in one command.");
        }
        this.damagePerBlock = damagePerBlock;
        return this;
    }

    /**
     * Sets the distance players can move outside the border before taking damage.
     */
    public WorldBorderCommand damageBuffer(float distance) {
        if (action != WorldBorderAction.DAMAGE) {
            throw new IllegalStateException("'damage buffer' is only valid for the DAMAGE action.");
        }
        if (distance < 0.0f) throw new IllegalArgumentException("Buffer distance cannot be negative.");
        if (this.damagePerBlock != null) {
            throw new IllegalStateException("Cannot set both damage amount and buffer in one command.");
        }
        this.bufferDistance = distance;
        return this;
    }

    // --- WARNING sub-command methods ---

    /**
     * Sets the distance at which the player's screen begins to tint red.
     */
    public WorldBorderCommand warningDistance(int distance) {
        if (action != WorldBorderAction.WARNING) {
            throw new IllegalStateException("'warning distance' is only valid for the WARNING action.");
        }
        if (distance < 0) throw new IllegalArgumentException("Warning distance cannot be negative.");
        if (this.warningTime != null) {
            throw new IllegalStateException("Cannot set both warning distance and time in one command.");
        }
        this.warningDistance = distance;
        return this;
    }

    /**
     * Sets how many seconds before a shrinking border reaches a player the tint appears.
     */
    public WorldBorderCommand warningTime(int time) {
        if (action != WorldBorderAction.WARNING) {
            throw new IllegalStateException("'warning time' is only valid for the WARNING action.");
        }
        if (time < 0) throw new IllegalArgumentException("Warning time cannot be negative.");
        if (this.warningDistance != null) {
            throw new IllegalStateException("Cannot set both warning distance and time in one command.");
        }
        this.warningTime = time;
        return this;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("worldborder ");
        sb.append(action);

        switch (action) {
            case ADD:
            case SET:
                if (sizeChangeOrDiameter == null) throw new IllegalStateException("Distance/Diameter is required.");
                sb.append(" ").append(sizeChangeOrDiameter);
                if (time != null) sb.append(" ").append(time);
                break;

            case CENTER:
                if (centerPos == null) throw new IllegalStateException("Center position is required.");
                sb.append(" ").append(centerPos);
                break;

            case DAMAGE:
                if (damagePerBlock != null) sb.append(" amount ").append(damagePerBlock);
                else if (bufferDistance != null) sb.append(" buffer ").append(bufferDistance);
                else throw new IllegalStateException("DAMAGE requires either 'amount' or 'buffer'.");
                break;

            case WARNING:
                if (warningDistance != null) sb.append(" distance ").append(warningDistance);
                else if (warningTime != null) sb.append(" time ").append(warningTime);
                else throw new IllegalStateException("WARNING requires either 'distance' or 'time'.");
                break;

            case GET:
                break;
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    public enum WorldBorderAction {
        ADD, CENTER, DAMAGE, GET, SET, WARNING;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}