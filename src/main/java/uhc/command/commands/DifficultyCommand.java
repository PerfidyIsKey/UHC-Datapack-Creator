package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.resource.gameplay.DifficultyId;

import java.util.Objects;

/**
 * 📊 **Difficulty Command Builder**
 * <p>
 * A fluent builder for the {@code /difficulty} command.
 * This command can be used in two ways:
 * <ul>
 * <li><b>Set:</b> {@code /difficulty <difficulty>} - Changes the world difficulty.</li>
 * <li><b>Query:</b> {@code /difficulty} - Displays the current world difficulty.</li>
 * </ul>
 * </p>
 */
public class DifficultyCommand implements MinecraftCommand {
    private DifficultyId difficulty;

    /**
     * Private constructor to enforce use of the static factory method.
     */
    private DifficultyCommand() {}

    /**
     * Starts the construction of the /difficulty command.
     * @return A new builder instance.
     */
    public static DifficultyCommand create() {
        return new DifficultyCommand();
    }

    /**
     * Sets the difficulty level for the command.
     * <p>If this is not called, the command will act as a query.</p>
     * @param difficulty The desired {@link DifficultyId} (e.g., PEACEFUL, EASY, NORMAL, HARD).
     * @return The current builder instance.
     * @throws NullPointerException if the provided difficulty is null.
     */
    public DifficultyCommand difficulty(DifficultyId difficulty) {
        // Using Objects.requireNonNull for standard idiomatic null-checking
        this.difficulty = Objects.requireNonNull(difficulty, "DifficultyId cannot be null. To query difficulty, simply call create().generate().");
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return The complete command string (e.g., "difficulty hard" or "difficulty").
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("difficulty");

        // Minecraft Java Edition: "/difficulty" without arguments returns the current difficulty.
        if (difficulty != null) {
            sb.append(" ").append(difficulty);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}