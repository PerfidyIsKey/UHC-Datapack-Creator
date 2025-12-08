package uhc.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom formatter that adds ANSI color codes to log messages
 * based on their level for improved readability and easier error distinction.
 * <p>
 * This formatter ensures that the full stack trace for SEVERE logs is appended
 * directly to the formatted output.
 */
public class CustomConsoleFormatter extends Formatter {

    // --- ANSI Color Codes for Terminal Output ---
    public static final String ANSI_RESET = "\u001B[0m"; // Stops color/formatting
    public static final String ANSI_GREEN = "\u001B[32m"; // General success/INFO
    public static final String ANSI_RED = "\u001B[31m";   // Critical errors/SEVERE
    public static final String ANSI_YELLOW = "\u001B[33m"; // Warnings/WARNING

    /**
     * Formats the given LogRecord into a string with color and includes the stack trace if present.
     * @param record The LogRecord to be formatted.
     * @return The formatted, color-coded log string.
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        // 1. Determine the color based on the log level name
        String color = switch (record.getLevel().getName()) {
            case "INFO" -> ANSI_GREEN;
            case "WARNING" -> ANSI_YELLOW;
            case "SEVERE" -> ANSI_RED;
            // Default level (e.g., CONFIG, FINE) receives no special color
            default -> ANSI_RESET;
        };

        // 2. Format the message with color and structure
        // Include a standard structure: [LEVEL] Message
        builder.append(color);
        builder.append("[");
        builder.append(record.getLevel().getName());
        builder.append("] ");
        builder.append(record.getMessage());
        builder.append(ANSI_RESET); // Reset color after the message
        builder.append(System.lineSeparator()); // Always end with a newline

        // 3. Include the stack trace for SEVERE errors (CRITICAL FIX APPLIED)
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            // Use StringWriter and PrintWriter to capture the stack trace output
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            // Print the exception and its trace to the StringWriter
            thrown.printStackTrace(pw);

            // Append the captured stack trace to the builder, keeping the SEVERE color
            builder.append(ANSI_RED);
            builder.append("--- EXCEPTION STACK TRACE ---\n");
            builder.append(sw.toString());
            builder.append(ANSI_RESET); // Reset color after the trace
        }

        return builder.toString();
    }
}