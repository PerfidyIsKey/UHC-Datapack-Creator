package controlpoints;

import shared.EntityTag;

/**
 * Represents a dynamic Control Point name used as an entity tag (e.g., "cp1", "cp2").
 */
public record ControlPointTag(String name) implements EntityTag {

    // Constructor validation can go here if needed

    @Override
    public String getTagName() {
        return name;
    }

    @Override
    public String toString() {
        return getTagName();
    }
}