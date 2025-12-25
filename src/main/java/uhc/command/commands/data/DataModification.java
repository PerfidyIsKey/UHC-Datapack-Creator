package uhc.command.commands.data;

import uhc.command.commands.DataCommand;

/**
 * 🛠️ **Data Modification Interface**
 * <p>
 * This interface defines the contract for all NBT modification operations within
 * the {@code /data modify} and {@code /data merge} command branches.
 * </p>
 * <p>
 * Implementing classes are responsible for generating the "source" or "action"
 * portion of the command, such as:
 * <ul>
 * <li>{@code set value <value>}</li>
 * <li>{@code insert 0 from entity @s Inventory[0]}</li>
 * <li>{@code append from storage uhc:main contents}</li>
 * </ul>
 * </p>
 */
public interface DataModification {

    // --- 🛠️ Core Contract ---

    /**
     * Constructs the finalized command fragment for this specific modification.
     * <p>
     * <b>Implementation Note:</b> This method should return the specific
     * modification syntax required by Minecraft, including the operation
     * keyword (set, merge, append, etc.) and the data source.
     * </p>
     * <p>
     * <b>Error Catching:</b> Implementations should validate that all internal
     * objects (like {@code DataPath} or {@code DataValue}) are non-null and
     * produce valid strings before returning the result.
     * </p>
     * * @return The formatted command fragment (e.g., "set value 10b").
     * @throws IllegalStateException if the internal state of the modification
     * builder is incomplete or invalid.
     */
    String build();

    /**
     * Provides the modification string for command assembly.
     * <p>
     * By overriding {@code toString}, modification objects can be seamlessly
     * appended to {@link StringBuilder} instances during command generation.
     * </p>
     * * @return The result of the {@link #build()} method.
     */
    @Override
    String toString();

    // --- ⚙️ Validation Logic ---

    /**
     * Performs a deep validation of the modification parameters.
     * <p>
     * This method ensures that the modification is syntactically correct
     * before it is processed by the {@link DataCommand} builder.
     * </p>
     * * @throws IllegalStateException if any required component of the
     * modification is missing or malformed.
     */
    default void validate() throws IllegalStateException {
        String result = build();
        if (result == null || result.trim().isEmpty()) {
            throw new IllegalStateException("DataModification produced a null or empty command fragment.");
        }
    }
}