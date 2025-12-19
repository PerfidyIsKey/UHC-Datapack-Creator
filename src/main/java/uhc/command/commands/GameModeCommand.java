package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.game.GameMode;

import java.util.Objects;

/**
 * 🕹️ **GameMode Command Builder**
 * <p>
 * Provides a fluent API for both the {@code /gamemode} and {@code /defaultgamemode} commands.
 * </p>
 */
public class GameModeCommand implements MinecraftCommand {
    private final GameMode gamemode;
    private Entity target;
    private boolean useDefaultRoot = false;

    private GameModeCommand(GameMode gameMode) {
        this.gamemode = Objects.requireNonNull(gameMode, "GameMode cannot be null.");
    }

    /**
     * Initializes a new GameMode builder.
     */
    public static GameModeCommand create(GameMode gameMode) {
        return new GameModeCommand(gameMode);
    }

    /**
     * Sets the target player(s) for the /gamemode command.
     * <p>Note: This is ignored if {@link #setDefault()} is called.</p>
     */
    public GameModeCommand target(Entity target) {
        this.target = target;
        return this;
    }

    /**
     * Switches the command root to {@code /defaultgamemode}.
     * <p>When active, the command will ignore any targets set.</p>
     * @return The current builder instance.
     */
    public GameModeCommand setDefault() {
        this.useDefaultRoot = true;
        return this;
    }

    /**
     * Generates the command string based on the configured mode.
     * @return e.g., "gamemode survival @a" or "defaultgamemode spectator".
     */
    @Override
    public String generate() {
        if (useDefaultRoot) {
            // defaultgamemode <mode> (does not support targets)
            return "defaultgamemode " + gamemode;
        }

        StringBuilder sb = new StringBuilder("gamemode ");
        sb.append(gamemode);

        if (target != null) {
            sb.append(" ").append(target);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}