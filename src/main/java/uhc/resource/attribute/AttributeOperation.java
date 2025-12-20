package uhc.resource.attribute;

/**
 * 🧮 **Attribute Operation Registry**
 * <p>
 * Defines how the modifier amount is applied to the attribute's base value.
 * </p>
 */
public enum AttributeOperation {
    /**
     * Increments the attribute by the specified amount.
     * Formula: X = X + Amount
     */
    ADD_VALUE,

    /**
     * Adds the amount multiplied by the base value to the attribute.
     * Formula: X = X + (Base * Amount)
     */
    ADD_MULTIPLIED_BASE,

    /**
     * Multiplies the total value (after previous operations) by (1 + Amount).
     * Formula: X = X * (1 + Amount)
     */
    ADD_MULTIPLIED_TOTAL;

    public String getNbtName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return getNbtName();
    }
}