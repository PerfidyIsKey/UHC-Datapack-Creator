package controlpoints;

import shared.EntityTag;

/**
 * Represents a dynamic Control Point identifier used as an entity tag in the game world
 * (e.g., "cp1", "cp2"). This record ensures the tag name is non-null and non-empty.
 */
public record ControlPointTag(String name) implements EntityTag {

    /**
     * Canonical constructor to ensure the tag name is valid upon creation.
     * @param name The unique string identifier for the control point.
     * @throws IllegalArgumentException if the {@code name} is null or empty after trimming.
     */
    public ControlPointTag {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ControlPointTag name cannot be null or empty.");
        }
    }

    /**
     * Static factory method for explicit creation, returning a validated instance.
     * @param name The unique identifier for the control point.
     * @return A new, validated ControlPointTag instance.
     */
    public static ControlPointTag create(String name) {
        // The record's canonical constructor handles validation automatically here.
        return new ControlPointTag(name);
    }

    /**
     * Returns the raw string identifier of the control point.
     * @return The control point's tag name.
     */
    @Override
    public String getTagName() {
        return name;
    }

    /**
     * Returns the raw tag name, suitable for use in logging or debugging.
     * @return The control point's tag name.
     */
    @Override
    public String toString() {
        return getTagName();
    }
}