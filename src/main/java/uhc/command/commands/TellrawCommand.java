package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 📢 **Tellraw Command Builder**
 * <p>
 * This class facilitates the creation of the Minecraft {@code /tellraw} command.
 * It ensures that rich text messages are properly formatted and targeted at the correct entities.
 * </p>
 */
public class TellrawCommand implements MinecraftCommand {
    private final Entity targets;
    private final TextComponent message;

    /**
     * Private constructor to enforce the use of static factory methods.
     * Validates that neither targets nor message are null upon instantiation.
     * * @param targets The target selector or player.
     * @param message The rich text component to display.
     * @throws NullPointerException if targets or message is null.
     */
    private TellrawCommand(Entity targets, TextComponent message) {
        this.targets = Objects.requireNonNull(targets, "Targets (Entity) cannot be null.");
        this.message = Objects.requireNonNull(message, "Message (TextComponent) cannot be null.");
    }

    /**
     * Creates a TellrawCommand using a pre-constructed {@link TextComponent}.
     * * @param targets The entities who should receive the message.
     * @param message The rich text component.
     * @return A validated TellrawCommand instance.
     */
    public static TellrawCommand create(Entity targets, TextComponent message) {
        return new TellrawCommand(targets, message);
    }

    /**
     * Creates a TellrawCommand by wrapping a raw String into a simple {@link TextComponent}.
     * * @param targets The entities who should receive the message.
     * @param message The plain text message.
     * @return A validated TellrawCommand instance.
     */
    public static TellrawCommand create(Entity targets, String message) {
        return new TellrawCommand(targets, TextComponent.text(message));
    }

    /**
     * Generates the final Minecraft command string.
     * <p>
     * Format: {@code tellraw <targets> <message>}
     * </p>
     * * @return The formatted command string ready for execution.
     * @throws IllegalStateException if the internal state is somehow corrupted.
     */
    @Override
    public String generate() {
        // Double-check logic for safety, though constructor validation makes this unlikely to fail.
        if (targets == null || message == null) {
            throw new IllegalStateException("TellrawCommand state is invalid: targets or message is null.");
        }

        return "tellraw " + targets + " " + message.build();
    }

    /**
     * Returns the same result as {@link #generate()} for easy debugging and logging.
     */
    @Override
    public String toString() {
        return generate();
    }
}