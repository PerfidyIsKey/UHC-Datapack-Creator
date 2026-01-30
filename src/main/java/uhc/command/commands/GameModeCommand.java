package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.gameplay.GameModeId;

import java.util.Objects;

/**
 * 🕹️ **GameMode Command Builder (GameModeCommand)**
 * <p>
 * This class provides a robust, immutable interface for generating both the
 * {@code /gamemode} and {@code /defaultgamemode} commands.
 * </p>
 * <p>
 * <b>Strict Validation:</b> This class enforces mandatory non-null values for
 * game modes and targets (where applicable). It follows a "fail-fast" policy,
 * throwing explicit exceptions rather than providing default command fallbacks.
 * </p>
 */
public final class GameModeCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The specific game mode to be applied.
     * <p>References technical IDs such as {@code survival}, {@code creative}, etc.</p>
     */
    private final GameModeId gamemode;

    /** * The target entity or player selector to receive the mode change.
     * <p>If {@code null}, the command targets the executor.</p>
     */
    private final Entity target;

    /** * Determines the command root to be used.
     * <p>If {@code true}, generates {@code /defaultgamemode}; otherwise {@code /gamemode}.</p>
     */
    private final boolean isDefault;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor used by static overloads to ensure immutable state.
     *
     * @param gamemode  The target mode (non-null).
     * @param target    The target entity (nullable).
     * @param isDefault Flag for using the default game mode root.
     * @throws NullPointerException If the gamemode is null.
     */
    private GameModeCommand(GameModeId gamemode, Entity target, boolean isDefault) {
        this.gamemode = Objects.requireNonNull(gamemode, "Initialization Error: GameModeId cannot be null.");
        this.target = target;
        this.isDefault = isDefault;
    }

    // --- 🚀 Static Overloads ---

    /**
     * Creates a command to change the executor's own game mode.
     * <p>Syntax: {@code /gamemode <mode>}</p>
     *
     * @param mode The desired {@link GameModeId}.
     * @return A validated GameModeCommand instance.
     * @throws NullPointerException If the mode is null.
     */
    public static GameModeCommand of(GameModeId mode) {
        return new GameModeCommand(mode, null, false);
    }

    /**
     * Creates a command to change the game mode of a specific target.
     * <p>Syntax: {@code /gamemode <mode> <target>}</p>
     *
     * @param mode   The desired {@link GameModeId}.
     * @param target The {@link Entity} selector to affect (non-null).
     * @return A validated GameModeCommand instance.
     * @throws NullPointerException If either parameter is null.
     */
    public static GameModeCommand of(GameModeId mode, Entity target) {
        Objects.requireNonNull(target, "Validation Error: Targeted gamemode change requires a non-null Entity.");
        return new GameModeCommand(mode, target, false);
    }

    /**
     * Creates a command to change the world's default game mode.
     * <p>Syntax: {@code /defaultgamemode <mode>}</p>
     *
     * @param mode The desired {@link GameModeId}.
     * @return A validated GameModeCommand instance.
     * @throws NullPointerException If the mode is null.
     */
    public static GameModeCommand ofDefault(GameModeId mode) {
        return new GameModeCommand(mode, null, true);
    }

    // --- ⚙️ Generation Logic ---

    /**
     * Generates the finalized Minecraft command string.
     * <p>
     * This method validates the state of internal objects during execution. If
     * a {@code defaultgamemode} is requested while a target is present, an error
     * is thrown as this is an invalid Minecraft syntax.
     * </p>
     *
     * @return A formatted command string.
     * @throws IllegalStateException If the command configuration is syntactically impossible.
     * @throws RuntimeException If any component fails to serialize to text.
     */
    @Override
    public String generate() {
        try {
            if (isDefault) {
                if (target != null) {
                    throw new IllegalStateException("Syntax Error: /defaultgamemode does not support target selectors.");
                }
                return "defaultgamemode " + gamemode;
            }

            StringBuilder sb = new StringBuilder("gamemode ");
            sb.append(gamemode);

            if (target != null) {
                sb.append(" ").append(target);
            }

            return sb.toString();
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            // Catching underlying serialization errors from GameModeId or Entity
            throw new RuntimeException("CRITICAL: Failed to generate GameMode command string due to component failure.", e);
        }
    }

    /**
     * Returns the generated command string.
     * @return The Minecraft command text.
     */
    @Override
    public String toString() {
        return generate();
    }

    // --- 🔍 Accessors ---

    /** * Retrieves the game mode identifier.
     * @return The non-null {@link GameModeId}.
     */
    public GameModeId getGamemode() {
        return Objects.requireNonNull(gamemode, "Access Error: Gamemode field is unexpectedly null.");
    }

    /** * Retrieves the target entity selector.
     * @return The {@link Entity} target, or {@code null} if targeting the executor.
     */
    public Entity getTarget() {
        return target;
    }

    /** * Indicates if this command sets the world default.
     * @return {@code true} if generating {@code /defaultgamemode}.
     */
    public boolean isDefault() {
        return isDefault;
    }
}