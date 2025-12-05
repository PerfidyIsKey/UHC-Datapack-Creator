package commands;

import arguments.Entity;
import shared.GameMode;

public class SetGameMode {
    private final GameMode gamemode;
    private Entity target;

    private SetGameMode(GameMode gameMode) {
        this.gamemode = gameMode;
    }

    public static SetGameMode create(GameMode gameMode) {
        return new SetGameMode(gameMode);
    }

    public SetGameMode target(Entity target) {
        this.target = target;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("gamemode ");
        sb.append(gamemode.toString());

        if (target != null) {
            sb.append(" ").append(target);
        }
        return sb.toString();
    }

    public String setDefault() {
        return "defaultgamemode " + gamemode;
    }
}
