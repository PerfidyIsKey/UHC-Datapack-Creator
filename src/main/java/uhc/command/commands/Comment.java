package uhc.command.commands;

import uhc.command.MinecraftCommand;

/**
 * 📝 Represents a custom comment line in a Minecraft function file (prefixed with #).
 * Comments are treated as commands to allow for interleaving with actual commands.
 */
public class Comment implements MinecraftCommand {

    private final String text;

    /**
     * Private constructor to enforce the use of the static factory method.
     */
    private Comment(String text) {
        this.text = text;
    }

    /**
     * Public static factory method to create a new Comment instance.
     * @param text The text of the comment (the '#' will be added automatically).
     * @return A new Comment instance.
     */
    public static Comment create(String text) {
        if (text == null) {
            // Treat null input as an empty comment line
            text = "";
        }
        return new Comment(text);
    }

    /**
     * Generates the command string, ensuring it is always prefixed with the comment symbol.
     * @return The complete comment line, e.g., "# This is a section header"
     */
    @Override
    public String generate() {
        // Automatically prepend the Minecraft comment symbol.
        return "# " + this.text;
    }

    @Override
    public String toString() {
        return generate();
    }
}