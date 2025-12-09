package uhc.command.commands;

import uhc.command.MinecraftCommand;

/**
 * 💬 Represents the Minecraft {@code /say} command: {@code /say <message>}.
 * This class ensures the message argument is present before command generation.
 */
public class SayCommand implements MinecraftCommand {

    // The message content to be displayed in the game chat.
    private final String message;

    /**
     * Private constructor to enforce the use of the {@code create} static factory method.
     * @param message The non-null, non-empty message content.
     */
    private SayCommand(String message) {
        this.message = message;
    }

    /**
     * Public static factory method to create a new {@code SayCommand} instance.
     * * 🐛 **Error Catching (Validation):** Ensures the message is not null, empty, or just whitespace,
     * which would lead to an invalid or useless command in Minecraft.
     * * @param message The message to be spoken in-game.
     * @return A new {@code SayCommand} instance.
     * @throws IllegalArgumentException if the provided message is null or empty.
     */
    public static SayCommand create(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("The message for the SayCommand cannot be null or empty.");
        }
        return new SayCommand(message);
    }

    /**
     * Generates the final, executable Minecraft command string.
     * * @return The complete command string, e.g., "say [UHC] Datapack initializing!"
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("say ");

        sb.append(message);

        return sb.toString();
    }

    /**
     * Returns the command string representation, typically used for debugging or simple utility.
     * It delegates the generation to the {@code generate()} method.
     * * @return The complete command string.
     */
    @Override
    public String toString() {
        return generate();
    }
}