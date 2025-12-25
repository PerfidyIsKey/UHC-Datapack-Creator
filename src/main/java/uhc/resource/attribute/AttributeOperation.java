package uhc.resource.attribute;

import java.util.Objects;

/**
 * 🧮 **Attribute Operation Registry**
 * <p>
 * Defines the mathematical logic used to apply a modifier's amount to an
 * attribute's base value. Operations are applied in a specific order:
 * {@code ADD_VALUE} first, followed by {@code ADD_MULTIPLIED_BASE},
 * and finally {@code ADD_MULTIPLIED_TOTAL}.
 * </p>
 */
public enum AttributeOperation {

    // --- 🔢 Operation Constants ---

    /**
     * **Operation 0: Addition**
     * <p>Increments the attribute by the specified amount.</p>
     * <p><b>Formula:</b> {@code Final = Base + Amount}</p>
     */
    ADD_VALUE,

    /**
     * **Operation 1: Multiplication (Base)**
     * <p>Adds the amount multiplied by the base value to the current total.
     * Multiple modifiers of this type are additive with each other.</p>
     * <p><b>Formula:</b> {@code Final = Base + (Base * Amount)}</p>
     */
    ADD_MULTIPLIED_BASE,

    /**
     * **Operation 2: Multiplication (Total)**
     * <p>Multiplies the current total value (after previous operations are applied)
     * by {@code (1 + Amount)}.</p>
     * <p><b>Formula:</b> {@code Final = Total * (1 + Amount)}</p>
     */
    ADD_MULTIPLIED_TOTAL;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase NBT-compatible name for the operation.
     * <p><b>Example:</b> {@code AttributeOperation.ADD_VALUE.getNbtName()} returns {@code "add_value"}.</p>
     * @return The lowercase identifier string.
     */
    public String getNbtName() {
        return this.name().toLowerCase();
    }

    /**
     * Retrieves the internal numeric ID used by Minecraft for this operation.
     * @return The integer ID (0 for ADD_VALUE, 1 for BASE, 2 for TOTAL).
     */
    public int getId() {
        return this.ordinal();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves an AttributeOperation from its numeric ID.
     * <p><b>Error Catching:</b> Validates that the ID falls within the 0-2 range.
     * If the ID is invalid, it returns {@link #ADD_VALUE} as a safe fallback.</p>
     * @param id The integer ID of the operation.
     * @return The matching {@link AttributeOperation}, or {@link #ADD_VALUE} if out of bounds.
     */
    public static AttributeOperation fromId(int id) {
        AttributeOperation[] values = values();
        if (id < 0 || id >= values.length) {
            return ADD_VALUE;
        }
        return values[id];
    }

    /**
     * Safely retrieves an AttributeOperation from a raw string.
     * <p><b>Error Catching:</b> Handles null, blank, or mistyped strings by
     * returning {@link #ADD_VALUE}. Performs a case-insensitive lookup.</p>
     * @param input The raw operation name (e.g., "add_multiplied_total").
     * @return The matching {@link AttributeOperation}, or {@link #ADD_VALUE} if invalid.
     */
    public static AttributeOperation fromString(String input) {
        if (input == null || input.isBlank()) {
            return ADD_VALUE;
        }

        String target = input.toUpperCase().trim();
        try {
            return AttributeOperation.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped operations from external configs
            return ADD_VALUE;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase NBT name for use in command generation or NBT building.
     * @return The result of {@link #getNbtName()}.
     */
    @Override
    public String toString() {
        return getNbtName();
    }
}