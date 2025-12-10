package commands;

import uhc.arguments.coordinate.Vec2;
import commands.worldborder.WorldBorderAction;

/**
 * Fluent builder for the Minecraft /worldborder command, with comprehensive error handling
 * and assignment safety for all sub-commands.
 */
public class WorldBorder {
    private final WorldBorderAction action;

    // Fields for ADD/SET actions (distance is double, time is int)
    private Double sizeChangeOrDiameter;
    private Integer time;

    // Field for CENTER action (pos is Vec2)
    private Vec2 centerPos;

    // Fields for DAMAGE sub-actions (amount is float, buffer is float)
    private Float damagePerBlock;
    private Float bufferDistance;

    // Fields for WARNING sub-actions (distance is int, time is int)
    private Integer warningDistance;
    private Integer warningTime;

    private WorldBorder(WorldBorderAction action) {
        if (action == null) {
            throw new IllegalArgumentException("WorldBorder action cannot be null.");
        }
        this.action = action;
    }

    public static WorldBorder create(WorldBorderAction action) {
        return new WorldBorder(action);
    }

    // --- ADD / SET methods (uses double distance and optional int time) ---

    /**
     * Sets the distance (diameter or change) for ADD or SET actions.
     * Constraints: -59,999,968 to 59,999,968 (double). Must be set for ADD/SET.
     */
    public WorldBorder distance(double distance) {
        if (action != WorldBorderAction.ADD && action != WorldBorderAction.SET) {
            throw new IllegalStateException("Distance parameter is only valid for ADD or SET actions.");
        }
        final double MAX_SIZE = 59999968.0;
        if (distance < -MAX_SIZE || distance > MAX_SIZE) {
            throw new IllegalArgumentException("Distance/Diameter (" + distance + ") must be between -" + MAX_SIZE + " and " + MAX_SIZE + ".");
        }
        if (action == WorldBorderAction.SET && distance < 0) {
            // Diameter must be non-negative when setting the absolute size.
            throw new IllegalArgumentException("New World Border diameter must be non-negative for the SET action.");
        }
        this.sizeChangeOrDiameter = distance;
        return this;
    }

    /**
     * Sets the time (in seconds) over which the border grows or shrinks for ADD or SET actions.
     * Constraints: 0 to 2,147,483,647 (int). Optional.
     */
    public WorldBorder time(int time) {
        if (action != WorldBorderAction.ADD && action != WorldBorderAction.SET) {
            throw new IllegalStateException("Time parameter is only valid for ADD or SET actions.");
        }
        if (time < 0) {
            throw new IllegalArgumentException("Time in seconds cannot be negative.");
        }
        this.time = time;
        return this;
    }

    // --- CENTER method (uses Vec2 pos) ---

    /**
     * Sets the center coordinates for the CENTER action.
     * @param pos The Vec2 coordinates for the center.
     */
    public WorldBorder center(Vec2 pos) {
        if (action != WorldBorderAction.CENTER) {
            throw new IllegalStateException("Center coordinates are only valid for the CENTER action.");
        }
        if (pos == null) {
            throw new IllegalArgumentException("Center position cannot be null.");
        }
        this.centerPos = pos;
        return this;
    }

    // --- DAMAGE sub-command methods (uses float) ---

    /**
     * Sets the damage per block for the 'damage amount' sub-command.
     * Constraints: float >= 0.0.
     * @throws IllegalStateException if damageBuffer has already been set.
     */
    public WorldBorder damageAmount(float damagePerBlock) {
        if (action != WorldBorderAction.DAMAGE) {
            throw new IllegalStateException("'damage amount' is only valid when the main action is DAMAGE.");
        }
        if (damagePerBlock < 0.0f) {
            throw new IllegalArgumentException("Damage per block must be greater than or equal to 0.0.");
        }
        if (this.bufferDistance != null) {
            throw new IllegalStateException("Only one DAMAGE sub-command (amount or buffer) can be set.");
        }
        this.damagePerBlock = damagePerBlock;
        return this;
    }

    /**
     * Sets the buffer distance for the 'damage buffer' sub-command.
     * Constraints: float >= 0.0.
     * @throws IllegalStateException if damageAmount has already been set.
     */
    public WorldBorder damageBuffer(float distance) {
        if (action != WorldBorderAction.DAMAGE) {
            throw new IllegalStateException("'damage buffer' is only valid when the main action is DAMAGE.");
        }
        if (distance < 0.0f) {
            throw new IllegalArgumentException("Damage buffer distance must be greater than or equal to 0.0.");
        }
        if (this.damagePerBlock != null) {
            throw new IllegalStateException("Only one DAMAGE sub-command (amount or buffer) can be set.");
        }
        this.bufferDistance = distance;
        return this;
    }

    // --- WARNING sub-command methods (uses int) ---

    /**
     * Sets the warning distance (blocks) for the 'warning distance' sub-command.
     * Constraints: int >= 0.
     * @throws IllegalStateException if warningTime has already been set.
     */
    public WorldBorder warningDistance(int distance) {
        if (action != WorldBorderAction.WARNING) {
            throw new IllegalStateException("'warning distance' is only valid when the main action is WARNING.");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("Warning distance must be non-negative.");
        }
        if (this.warningTime != null) {
            throw new IllegalStateException("Only one WARNING sub-command (distance or time) can be set.");
        }
        this.warningDistance = distance;
        return this;
    }

    /**
     * Sets the warning time (seconds) for the 'warning time' sub-command.
     * Constraints: int >= 0.
     * @throws IllegalStateException if warningDistance has already been set.
     */
    public WorldBorder warningTime(int time) {
        if (action != WorldBorderAction.WARNING) {
            throw new IllegalStateException("'warning time' is only valid when the main action is WARNING.");
        }
        if (time < 0) {
            throw new IllegalArgumentException("Warning time must be non-negative.");
        }
        if (this.warningDistance != null) {
            throw new IllegalStateException("Only one WARNING sub-command (distance or time) can be set.");
        }
        this.warningTime = time;
        return this;
    }

    /**
     * Builds the final /worldborder command string, enforcing parameter requirements.
     * @throws IllegalStateException if required parameters are missing for the chosen action.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("worldborder ");

        sb.append(action.toString());

        switch (action) {
            case ADD:
            case SET:
                if (sizeChangeOrDiameter == null) {
                    throw new IllegalStateException("The '" + action.toString() + "' action requires a distance/diameter.");
                }
                sb.append(" ").append(sizeChangeOrDiameter);
                // Optional time is appended if set
                if (time != null) {
                    sb.append(" ").append(time);
                }
                break;

            case CENTER:
                if (centerPos == null) {
                    throw new IllegalStateException("The 'center' action requires a position (Vec2). Call center(pos) before build().");
                }
                sb.append(" ").append(centerPos.toString());
                break;

            case DAMAGE:
                boolean damageAmountSet = damagePerBlock != null;
                boolean damageBufferSet = bufferDistance != null;

                if (damageAmountSet && damageBufferSet) {
                    throw new IllegalStateException("Only one DAMAGE sub-command (amount or buffer) can be set.");
                } else if (damageAmountSet) {
                    sb.append(" amount ").append(damagePerBlock);
                } else if (damageBufferSet) {
                    sb.append(" buffer ").append(bufferDistance);
                } else {
                    throw new IllegalStateException("The 'damage' action requires a sub-command (amount or buffer).");
                }
                break;

            case WARNING:
                boolean warningDistanceSet = warningDistance != null;
                boolean warningTimeSet = warningTime != null;

                if (warningDistanceSet && warningTimeSet) {
                    throw new IllegalStateException("Only one WARNING sub-command (distance or time) can be set.");
                } else if (warningDistanceSet) {
                    sb.append(" distance ").append(warningDistance);
                } else if (warningTimeSet) {
                    sb.append(" time ").append(warningTime);
                } else {
                    throw new IllegalStateException("The 'warning' action requires a sub-command (distance or time).");
                }
                break;

            case GET:
                // No arguments needed for GET.
                break;

            default:
                // This case should be unreachable if all WorldBorderAction enums are covered.
                break;
        }

        return sb.toString();
    }
}