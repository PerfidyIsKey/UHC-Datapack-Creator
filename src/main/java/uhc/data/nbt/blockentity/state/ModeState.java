package uhc.data.nbt.blockentity.state;

/**
 * 🧱 **Mode Block State Property**
 * <p>
 * Represents the 'mode' property used by Structure Blocks and Redstone Comparators.
 * The value is derived directly from the enum constant name in lowercase.
 * </p>
 */
public enum ModeState implements BlockState {

    // --- Comparator Modes ---
    /** The comparator is in comparison mode (default). */
    COMPARE,
    /** The comparator is in subtraction mode (torch is lit). */
    SUBTRACT,

    // --- Structure Block Modes ---
    /** Used to define the bounds of a structure. */
    CORNER,
    /** Used for data access and custom functions. */
    DATA,
    /** Used to load a saved structure into the world. */
    LOAD,
    /** Used to save a structure to a file. */
    SAVE;

    /**
     * Retrieves the property key defined by Minecraft for these blocks.
     * @return The constant property key "mode".
     */
    @Override
    public String getKey() {
        return "mode";
    }

    /**
     * Retrieves the property value as a lowercase string.
     * <p>
     * For example, {@code SUBTRACT} returns "subtract".
     * </p>
     * @return The lowercase representation of the enum name.
     */
    @Override
    public String getValue() {
        return this.name().toLowerCase();
    }

    /**
     * Provides a string representation of the state value.
     * @return The same value as {@link #getValue()}.
     */
    @Override
    public String toString() {
        return getValue();
    }
}