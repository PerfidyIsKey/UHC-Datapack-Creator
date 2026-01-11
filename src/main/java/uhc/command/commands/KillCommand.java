package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 💀 **Kill Command Builder**
 * <p>
 * Provides a semantic API for the {@code /kill} command.
 * This command instantly removes entities from the world or kills the command sender.
 * </p>
 */
public class KillCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The optional target(s) for the kill command. If null, the command targets the sender. */
    private final Entity targets;

    // --- 🏗️ Constructors & Static Entry Points ---

    /**
     * Private constructor to enforce entry through static overloads.
     * @param targets The target entity/selector, or null for self-kill.
     */
    private KillCommand(Entity targets) {
        this.targets = targets;
    }

    /**
     * Entry point for {@code /kill} targeting the command sender.
     * @return A KillCommand instance for self-elimination.
     */
    public static KillCommand self() {
        return new KillCommand(null);
    }

    /**
     * Entry point for {@code /kill <targets>} targeting specific entities.
     * <p><b>Strict Validation:</b> Will not accept a null entity to prevent accidental self-kills.</p>
     * @param targets The required target selector or entity.
     * @return A KillCommand instance for the specified targets.
     * @throws NullPointerException if {@code targets} is null.
     */
    public static KillCommand targets(Entity targets) {
        Objects.requireNonNull(targets, "Kill Error: Specified targets cannot be null. Use self() for a sender-only kill.");
        return new KillCommand(targets);
    }

    // --- ⚙️ Command Generation ---

    /**
     * Generates the final Minecraft command string.
     * <p>
     * <b>Syntax:</b>
     * <ul>
     * <li>{@code kill} - If no targets were specified (sender only).</li>
     * <li>{@code kill <targets>} - If specific entities were provided.</li>
     * </ul>
     * </p>
     * @return The formatted command string.
     * @throws RuntimeException if an error occurs during string assembly.
     */
    @Override
    public String generate() {
        try {
            if (this.targets == null) {
                return "kill";
            }
            return "kill " + this.targets;
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to generate kill command for targets: " + this.targets, e);
        }
    }

    /**
     * Returns the generated command string.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}