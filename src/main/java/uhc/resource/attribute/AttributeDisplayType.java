package uhc.resource.attribute;

/**
 * 👁️ **Attribute Display Type Registry**
 * <p>
 * Defines how an attribute modifier is rendered in the item's tooltip.
 * </p>
 */
public enum AttributeDisplayType {
    /**
     * Shows the standard green/red stat line (e.g., "+5 Attack Damage").
     */
    DEFAULT,

    /**
     * The modifier is active, but it does not appear in the tooltip at all.
     */
    HIDDEN,

    /**
     * Replaces the standard stat line with a custom TextComponent.
     */
    OVERRIDE;

    public String getNbtName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return getNbtName();
    }
}