package uhc.command;

import java.util.Objects;

/**
 * 📝 **Minecraft Function Comment**
 * <p>
 * Represents a documentation line within an {@code .mcfunction} file.
 * This class implements {@link MinecraftCommand} to allow comments to be
 * interleaved seamlessly with actual executable logic during the function
 * assembly process.
 * </p>
 * <p>
 * Comments are prefixed with the {@code #} symbol, ensuring they are ignored
 * by the Minecraft command parser while remaining visible to developers
 * inspecting the generated datapack files.
 * </p>
 */
public class Comment implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The raw text content of the comment.
     * This field is final to maintain the immutability of the command instance.
     */
    private final String text;

    // --- 🏗️ Constructor & Factory ---

    /**
     * Private constructor to enforce the use of the static factory method.
     * * @param text The validated text for the comment.
     */
    private Comment(String text) {
        this.text = text;
    }

    /**
     * Creates a new comment line for a function file.
     * <p>
     * Validation: This method enforces a "no-fallback" policy. If the provided
     * text is null, it will not default to an empty string; instead, it will
     * throw an exception to highlight a logic error in the caller.
     * </p>
     * * @param text The description text to include in the comment.
     * @return A new {@link Comment} instance.
     * @throws NullPointerException if the provided {@code text} is null.
     */
    public static Comment create(String text) {
        Objects.requireNonNull(text, "Comment construction failed: The provided text string cannot be null.");
        return new Comment(text);
    }

    // --- ⚙️ Command Generation ---

    /**
     * Generates the final command string as it will appear in the .mcfunction file.
     * <p>
     * The output follows the standard Minecraft format: {@code # <text>}.
     * </p>
     * * @return A formatted comment string prefixed with the hash symbol.
     * @throws RuntimeException if an unexpected error occurs during string concatenation.
     */
    @Override
    public String generate() {
        try {
            return "# " + this.text;
        } catch (Exception e) {
            // Catching potential system-level errors during string joining
            throw new RuntimeException("Critical failure: Could not generate comment string for: " + this.text, e);
        }
    }

    /**
     * Provides a string representation of this object, primarily for debugging.
     * This calls the {@link #generate()} method to ensure consistency.
     * * @return The generated comment line.
     */
    @Override
    public String toString() {
        return generate();
    }
}