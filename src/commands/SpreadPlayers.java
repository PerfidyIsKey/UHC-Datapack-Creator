package commands;

import arguments.Entity;
import arguments.coordinate.Vec2;

public class SpreadPlayers {
    private final Vec2 center;
    private final float spreadDistance;
    private final float maxRange;
    private final Boolean respectTeams;
    private final Entity targets;
    private int maxHeight;

    private SpreadPlayers(Vec2 center, float spreadDistance, float maxRange, Boolean respectTeams, Entity targets) {
        this.center = center;
        this.spreadDistance = spreadDistance;
        this.maxRange = maxRange;
        this.respectTeams = respectTeams;
        this.targets = targets;
    }

    private SpreadPlayers(Vec2 center, float spreadDistance, float maxRange, int maxHeight, Boolean respectTeams, Entity targets) {
        this.center = center;
        this.spreadDistance = spreadDistance;
        this.maxRange = maxRange;
        this.maxHeight = maxHeight;
        this.respectTeams = respectTeams;
        this.targets = targets;
    }

    public static SpreadPlayers create(Vec2 center, float spreadDistance, float maxRange, Boolean respectTeams, Entity targets) {
        return new SpreadPlayers(center, spreadDistance, maxRange, respectTeams, targets);
    }

    public static SpreadPlayers create(Vec2 center, int spreadDistance, int maxRange, Boolean respectTeams, Entity targets) {
        return new SpreadPlayers(center, spreadDistance, maxRange, respectTeams, targets);
    }

    public static SpreadPlayers create(Vec2 center, double spreadDistance, double maxRange, Boolean respectTeams, Entity targets) {
        return new SpreadPlayers(center, (float) spreadDistance, (float) maxRange, respectTeams, targets);
    }

    public static SpreadPlayers create(Vec2 center, float spreadDistance, float maxRange, int maxHeight, Boolean respectTeams, Entity targets) {
        return new SpreadPlayers(center, spreadDistance, maxRange, maxHeight, respectTeams, targets);
    }

    public String build() {
        StringBuilder sb = new StringBuilder("spreadplayers ");

        sb.append(center).append(" ").append(spreadDistance).append(" ").append(maxRange).append(" ");
        if (maxHeight != 0) {
            sb.append("under ").append(maxHeight).append(" ");
        }
        sb.append(respectTeams).append(" ").append(targets);

        return sb.toString();
    }
}
