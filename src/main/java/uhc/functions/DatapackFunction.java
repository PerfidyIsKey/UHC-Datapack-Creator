package uhc.functions;

import uhc.command.commands.Comment;
import uhc.components.functions.Function;
import uhc.core.Datapack;
import uhc.core.Namespace;

import java.util.Objects;

/**
 * 🧱 **Datapack Function Contract**
 * <p>
 * This interface defines the essential contract for all functional modules within the
 * UHC datapack. It serves as the entry point for feature registration and provides
 * standardized utility methods to ensure consistent file formatting and safe command injection.
 * </p>
 */
public interface DatapackFunction {

    // --- 🏗️ Core Registration Method ---

    /**
     * Executes the component registration logic for a specific feature module.
     * <p>
     * Implementing classes must use this method to instantiate their {@link Function}
     * components and register them into the provided {@link Namespace}.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance used for cross-namespace
     * references (non-null).
     * @param namespace The primary {@link Namespace} where this function's
     * components should be stored (non-null).
     * @throws NullPointerException if the {@code datapack} or {@code namespace} is null.
     */
    void register(Datapack datapack, Namespace namespace);

    // --- 🛠️ Shared Formatting Utilities (Default Methods) ---

    /**
     * Appends a standardized, decorative header block to an {@code .mcfunction} file.
     * <p>
     * This ensures all generated files have uniform documentation, making the
     * final datapack easier to debug and read.
     * </p>
     *
     * @param function The {@link Function} component currently being modified (non-null).
     * @param title    The descriptive title of the function logic to display in the header (non-null).
     * @throws NullPointerException if {@code function} or {@code title} is null (Strict Error Policy).
     */
    default void appendStandardHeader(Function function, String title) {
        // Enforce no-fallback policy: Immediate error if parameters are missing
        Objects.requireNonNull(function, "Header Generation Error: Target function component cannot be null.");
        Objects.requireNonNull(title, "Header Generation Error: Header title cannot be null.");

        try {
            function.addLine(Comment.create("================================================"));
            function.addLine(Comment.create(" " + title.toUpperCase()));
            function.addLine(Comment.create(" This file is AUTO-GENERATED. Do not edit."));
            function.addLine(Comment.create("================================================"));
            function.addLine(Comment.create(" "));
        } catch (Exception e) {
            // Catching any unexpected errors during line addition
            throw new RuntimeException("CRITICAL: Failed to append standard header to function " + function.getPath() + ". Reason: " + e.getMessage(), e);
        }
    }

    /**
     * Safe-wraps the addition of a line to a function component.
     * <p>
     * This method prevents "silent null" additions. If the provided line object
     * is null, the system will throw a clear error message including the path
     * of the failing function.
     * </p>
     *
     * @param function The target {@link Function} component (non-null).
     * @param line     The command, comment, or raw string to add (non-null).
     * @throws NullPointerException if {@code line} or {@code function} is null.
     */
    default void addSafeLine(Function function, Object line) {
        // Validation before execution
        Objects.requireNonNull(function, "SafeLine Error: Target function component is null.");

        // Custom error message for the specific failing object
        if (line == null) {
            throw new NullPointerException("Function Assembly Error: Attempted to add a null line to function at path: " + function.getPath());
        }

        try {
            // Avoid raw string conversion if possible, or handle specifically
            if (line instanceof String str) {
                function.addLine(Comment.create(str));
            } else {
                function.addLine(Comment.create(line.toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to inject line into " + function.getPath() + ". " + e.getMessage(), e);
        }
    }
}