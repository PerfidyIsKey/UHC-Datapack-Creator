package uhc.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * 🎨 **Custom Console Log Formatter**
 * * Extends the standard Java {@code Formatter} to customize the log output
 * format and apply ANSI color codes based on the log level. This ensures clear
 * visual distinction between critical errors, warnings, success messages, and
 * detailed status updates.
 */
public class CustomConsoleFormatter extends Formatter {

    // --- ANSI Color Codes ---
    /** Resets the color/style to default. */
    public static final String ANSI_RESET = "\u001B[0m";
    /** Bright green color, typically used for success messages (INFO). */
    public static final String ANSI_GREEN = "\u001B[32m";
    /** Bright red color, typically used for severe errors (SEVERE). */
    public static final String ANSI_RED = "\u001B[31m";
    /** Bright yellow color, typically used for warnings and detailed status/unchanged updates (WARNING/CONFIG). */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * Formats the given LogRecord into a customizable, colored string.
     * * @param record The log record to be formatted.
     * @return The formatted, ANSI-colored string ready for console output.
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        // 1. Determine the color based on the log level's integer value.
        // Checking the integer value ensures the color mapping is language-independent
        // (i.e., it works even when 'INFO' is localized as 'INFORMACIÓN').
        String color = ANSI_RESET;
        Level level = record.getLevel();

        // The color assignment logic must check levels from highest priority downwards,
        // otherwise INFO will always be matched before CONFIG is checked.

        if (level.intValue() >= Level.SEVERE.intValue()) {
            // SEVERE errors are critical.
            color = ANSI_RED;
        } else if (level.intValue() >= Level.WARNING.intValue()) {
            // WARNINGS (e.g., DELETE status) are important status updates.
            color = ANSI_YELLOW;
        } else if (level.intValue() >= Level.INFO.intValue()) {
            // INFO messages (e.g., Start/Complete generation) indicate high-level success.
            color = ANSI_GREEN;
        } else if (level.intValue() >= Level.CONFIG.intValue()) {
            // CONFIG messages (e.g., CREATE/UPDATE/UNCHANGED file status) are detailed updates.
            color = ANSI_YELLOW;
        }

        // 2. Build the main log message format: [HH:MM:SS] [LEVEL] MESSAGE
        builder.append(color);
        builder.append("[");

        // Format the time as HH:MM:SS (sub-string ensures a clean format)
        builder.append(Instant.ofEpochMilli(record.getMillis()).toString().substring(11, 19));
        builder.append("] [");

        // Use the raw English level name for display consistency regardless of localization
        // NOTE: The record.getLevel().getName() returns the localized name if the environment is set up that way.
        // We'll trust the color logic above and use the name provided by the record.
        builder.append(record.getLevel().getName());
        builder.append("] ");

        // Append the actual message content
        builder.append(record.getMessage());

        builder.append(ANSI_RESET);
        builder.append(System.lineSeparator());

        // 3. Include the stack trace for errors (Throwable != null)
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            // Use StringWriter and PrintWriter to capture the full stack trace text.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            thrown.printStackTrace(pw);

            // Print the stack trace in red for maximum visibility.
            builder.append(ANSI_RED);
            builder.append(sw.toString());
            builder.append(ANSI_RESET);
        }

        return builder.toString();
    }
}