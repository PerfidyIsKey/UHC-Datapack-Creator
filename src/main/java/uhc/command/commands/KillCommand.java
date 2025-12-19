package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 💀 **Kill Command Builder**
 * <p>
 * Provides a fluent API for the {@code /kill} command.
 * This command instantly removes entities from the world.
 * </p>
 * <p>
 * <b>Syntax Variants:</b>
 * <ul>
 * <li>{@code /kill <targets>} - Kills specific entities (players, mobs, items).</li>
 * <li>{@code /kill} - (Java Edition) Kills the command sender.</li>
 * </ul>
 * </p>
 */
public class KillCommand implements MinecraftCommand {
    private Entity targets;

    private KillCommand() {}

    /**
     * Initializes a new KillCommand builder.
     * @return A new builder instance.
     */
    public static KillCommand create() { return new KillCommand(); }

    /**
     * Specifies the entities to be killed.
     * <p>If not called, the command will default to killing the sender ({@code /kill}).</p>
     * @param targets The target selector (e.g., @e[type=zombie], @a, or a specific player).
     * @return The current builder instance.
     */
    public KillCommand targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return The formatted command (e.g., "kill @e[type=item]" or "kill").
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("kill");

        // Minecraft allows /kill without arguments to target the executor.
        // If targets is provided, we append it to target specific entities.
        if (targets != null) {
            sb.append(" ").append(targets);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}