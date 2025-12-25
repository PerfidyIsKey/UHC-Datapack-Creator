package controlpoints;

import uhc.resource.tag.EntityTag;

/**
 * Represents a dynamic **Control Point identifier** used to uniquely tag entities
 * in the game world (e.g., "cp1", "cp2").
 * <p>
 * This record serves as an immutable, value-based identifier that implements the
 * {@code EntityTag} contract.
 */
public record ControlPointTag(String name) implements EntityTag {

    /**
     * Canonical constructor for the {@code ControlPointTag}.
     * <p>
     * Ensures that the provided tag name is valid (non-null and non-empty)
     * before the instance is created.
     *
     * @param name The unique string identifier for the control point.
     * @throws IllegalArgumentException if the {@code name} is {@code null} or consists only of whitespace.
     */
    public ControlPointTag {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ControlPointTag name cannot be null or empty.");
        }
    }

    /**
     * Static factory method for explicit and validated creation of a {@code ControlPointTag}.
     *
     * @param name The unique string identifier for the control point.
     * @return A new, validated {@code ControlPointTag} instance.
     */
    public static ControlPointTag create(String name) {
        // The record's canonical constructor automatically handles validation here.
        return new ControlPointTag(name);
    }

    /**
     * Retrieves the raw string identifier of the control point.
     * <p>
     * This method fulfills the contract defined by the {@code EntityTag} interface.
     *
     * @return The immutable, non-empty tag name.
     */
    @Override
    public String getTagName() {
        return name;
    }

    /**
     * Provides the string representation of this control point tag.
     * <p>
     * This overrides {@code Object.toString()} and returns the raw tag name,
     * which is suitable for direct use in logging or debugging output.
     *
     * @return The control point's tag name (e.g., "cp1").
     */
    @Override
    public String toString() {
        return getTagName();
    }

    /**
     * Returns the tag name converted entirely to uppercase characters.
     *
     * @return The uppercase version of the tag name.
     */
    public String toUpperCase() {
        return name.toUpperCase();
    }
}