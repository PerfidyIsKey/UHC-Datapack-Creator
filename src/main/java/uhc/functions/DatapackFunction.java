package uhc.functions;

import uhc.command.MinecraftCommand;
import uhc.command.commands.Comment;
import uhc.components.functions.Function;
import uhc.core.Datapack;
import uhc.core.Namespace;

import java.util.Objects;

/**
 * 🧱 **Datapack Function Contract**
 * <p>Standardized contract for all functional modules within the UHC datapack.</p>
 */
public interface DatapackFunction {

    void register(Datapack datapack, Namespace namespace);

    /**
     * Appends a standardized, decorative header block to an {@code .mcfunction} file.
     */
    default void appendStandardHeader(Function function, String title) {
        Objects.requireNonNull(function, "Header Generation Error: Target function component cannot be null.");
        Objects.requireNonNull(title, "Header Generation Error: Header title cannot be null.");

        function.addLine(Comment.create("================================================"));
        function.addLine(Comment.create(" " + title.toUpperCase()));
        function.addLine(Comment.create(" This file is AUTO-GENERATED. Do not edit."));
        function.addLine(Comment.create("================================================"));
        function.addLine(Comment.create(" "));
    }

    /**
     * Safe-wraps the addition of a line to a function component.
     * <p>
     * <b>Logic:</b> If the object is a {@link MinecraftCommand}, it is added as executable logic.
     * Otherwise, it is automatically converted into a {@link Comment}.
     * </p>
     *
     * @param function The target {@link Function} component (non-null).
     * @param line     The command or comment text to add (non-null).
     */
    default void addSafeLine(Function function, Object line) {
        Objects.requireNonNull(function, "SafeLine Error: Target function component is null.");

        if (line == null) {
            throw new NullPointerException("Function Assembly Error: Attempted to add a null line to function: " + function.getPath());
        }

        try {
            // Check if the line is intended to be an executable command
            if (line instanceof MinecraftCommand command) {
                function.addLine(command);
            } else {
                // If it's just a String or other Object, wrap it as a comment
                function.addLine(Comment.create(line.toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to inject line into " + function.getPath() + ". " + e.getMessage(), e);
        }
    }
}