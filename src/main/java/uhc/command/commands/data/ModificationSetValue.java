package uhc.command.commands.data;

import uhc.arguments.data.DataValue;
import java.util.Objects;

/**
 * 📍 **Set Value Operation**
 * <p>
 * This class implements the {@code set value <value>} logic for the Minecraft
 * {@code /data} command system. It is used to replace the existing NBT data
 * at a specific path with a new, typed value.
 * </p>
 * <p>
 * As a member of the {@link DataModification} hierarchy, it provides the
 * terminal segment of the {@code modify} command branch.
 * </p>
 */
public class ModificationSetValue implements DataModification {

    // --- ⚙️ State & Fields ---

    /** * The rich {@link DataValue} object representing the data to be set.
     * This holds the typed information (e.g., 1b, 1.0f, or a String)
     * to ensure Minecraft parses the NBT correctly.
     */
    private final DataValue value;

    // --- 🏗️ Constructor & Factory ---

    /**
     * Private constructor to enforce controlled instantiation.
     * <p><b>Error Catching:</b> Utilizes {@link Objects#requireNonNull} to prevent
     * the creation of a modification that would result in an "undefined" or
     * "null" value in the final command string.</p>
     * * @param value The typed {@link DataValue} to be assigned.
     * @throws NullPointerException if the provided value is null.
     */
    private ModificationSetValue(DataValue value) {
        this.value = Objects.requireNonNull(value, "DataValue cannot be null for ModificationSetValue.");
    }

    /**
     * Static factory method to create a 'set value' modification.
     * * @param value The {@link DataValue} to apply (e.g., NBTValues.ofInt(5)).
     * @return A new instance of {@link ModificationSetValue}.
     * @throws NullPointerException if value is null.
     */
    public static ModificationSetValue create(DataValue value) {
        return new ModificationSetValue(value);
    }

    // --- 🛰️ DataModification Implementation ---

    /**
     * Builds the specific command fragment for this modification.
     * <p><b>Syntax:</b> {@code set value <value>}</p>
     * <p><b>Error Catching:</b> Validates that the internal {@link DataValue}
     * produces a non-empty string before finalization.</p>
     * * @return The formatted command fragment (e.g., "set value 42b").
     * @throws IllegalStateException if the value's string representation is null or empty.
     */
    @Override
    public String build() {
        String valueString = value.toString();

        // Ensure the value hasn't been corrupted or misconfigured
        if (valueString == null || valueString.trim().isEmpty()) {
            throw new IllegalStateException("The DataValue object failed to produce a valid string for NBT assignment.");
        }

        return "set value " + valueString;
    }

    /**
     * Returns the built modification fragment.
     * <p>Delegates directly to {@link #build()} for consistency with
     * StringBuilder-based command generation.</p>
     * * @return The result of the build process.
     */
    @Override
    public String toString() {
        return build();
    }

    // --- 🔍 Accessors ---

    /**
     * Retrieves the internal data value object.
     * @return The immutable {@link DataValue}.
     */
    public DataValue getValue() {
        return value;
    }
}