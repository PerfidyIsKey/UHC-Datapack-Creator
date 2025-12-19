package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.coordinate.Vec2;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🏹 **SpreadPlayers Command Builder**
 * <p>
 * Provides a fluent API for the {@code /spreadplayers} command. This is used to
 * teleport entities to random locations within a specified boundary.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /spreadplayers <center> <spreadDistance> <maxRange> [under <maxHeight>] <respectTeams> <targets>}
 * </p>
 */
public class SpreadPlayersCommand implements MinecraftCommand {
    private final Vec2 center;
    private final float spreadDistance;
    private final float maxRange;
    private final Boolean respectTeams;
    private final Entity targets;
    private int maxHeight;

    private SpreadPlayersCommand(Vec2 center, float spreadDistance, float maxRange, Boolean respectTeams, Entity targets) {
        this.center = Objects.requireNonNull(center, "Center coordinates cannot be null.");
        this.spreadDistance = spreadDistance;
        this.maxRange = maxRange;
        this.respectTeams = Objects.requireNonNull(respectTeams, "respectTeams boolean must be specified.");
        this.targets = Objects.requireNonNull(targets, "Target entities cannot be null.");
    }

    private SpreadPlayersCommand(Vec2 center, float spreadDistance, float maxRange, int maxHeight, Boolean respectTeams, Entity targets) {
        this(center, spreadDistance, maxRange, respectTeams, targets);
        this.maxHeight = maxHeight;
    }

    /**
     * Creates a spreadplayers command.
     * @param center The X and Z center of the spread.
     * @param spreadDistance The minimum distance between players/teams.
     * @param maxRange The maximum distance from the center.
     * @param respectTeams If true, teammates will be kept together.
     * @param targets The entities to spread.
     */
    public static SpreadPlayersCommand create(Vec2 center, float spreadDistance, float maxRange, Boolean respectTeams, Entity targets) {
        validateDistances(spreadDistance, maxRange);
        return new SpreadPlayersCommand(center, spreadDistance, maxRange, respectTeams, targets);
    }

    public static SpreadPlayersCommand create(Vec2 center, int spreadDistance, int maxRange, Boolean respectTeams, Entity targets) {
        return create(center, (float) spreadDistance, (float) maxRange, respectTeams, targets);
    }

    public static SpreadPlayersCommand create(Vec2 center, double spreadDistance, double maxRange, Boolean respectTeams, Entity targets) {
        return create(center, (float) spreadDistance, (float) maxRange, respectTeams, targets);
    }

    /**
     * Creates a spreadplayers command with a maximum height restriction.
     * @param maxHeight The maximum Y-level for the spread (e.g., to keep players in caves or under a roof).
     */
    public static SpreadPlayersCommand create(Vec2 center, float spreadDistance, float maxRange, int maxHeight, Boolean respectTeams, Entity targets) {
        validateDistances(spreadDistance, maxRange);
        return new SpreadPlayersCommand(center, spreadDistance, maxRange, maxHeight, respectTeams, targets);
    }

    private static void validateDistances(float spread, float max) {
        if (spread < 0) throw new IllegalArgumentException("Spread distance cannot be negative.");
        if (max < spread + 1.0f) {
            throw new IllegalArgumentException("Max range must be at least 1.0 greater than spread distance.");
        }
    }

    /**
     * Generates the final Minecraft command string.
     * <p>Note: The 'under' argument is inserted between maxRange and respectTeams if maxHeight is non-zero.</p>
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("spreadplayers ");

        // Mandatory parameters: center X Z, spreadDistance, maxRange
        sb.append(center).append(" ").append(spreadDistance).append(" ").append(maxRange).append(" ");

        // Optional maxHeight parameter (Java Edition 1.16+)
        if (maxHeight != 0) {
            sb.append("under ").append(maxHeight).append(" ");
        }

        // Final mandatory parameters: respectTeams, targets
        sb.append(respectTeams).append(" ").append(targets);

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}