package uhc.resource.tag;

import java.util.Objects;

/**
 * 🏷️ **Entity Tag Interface**
 * <p>
 * Defines the contract for type-safe representations of Minecraft entity tags.
 * These tags are used as arbitrary string labels on entities to categorize them
 * for selectors (e.g., {@code @e[tag=custom_tag]}) or conditional logic in functions.
 * </p>
 */
public interface EntityTag {

    // --- 🛰️ Core Contract Methods ---

    /**
     * Retrieves the raw string value of the tag required for Minecraft commands and NBT.
     * <p><b>Example:</b> Should return {@code "uhc_participant"} for a UHC player tag.</p>
     * @return The non-null, case-sensitive string representation of the tag.
     */
    String getTagName();

    /**
     * Performs a validity check on the tag name to ensure it is compatible with
     * Minecraft's command and NBT standards.
     * <p><b>Error Catching:</b> Should throw an exception if the tag name contains
     * illegal characters such as spaces, quotes, or commas which would break
     * command selectors.</p>
     * @throws IllegalStateException if the tag format is invalid for use in selectors.
     */
    default void validate() throws IllegalStateException {
        String tag = getTagName();
        if (tag == null || tag.isBlank()) {
            throw new IllegalStateException("Entity tag name cannot be null or blank.");
        }

        // Tags in selectors cannot contain spaces or special selector characters
        if (tag.contains(" ") || tag.contains(",") || tag.contains("=") || tag.contains("]")) {
            throw new IllegalStateException("Entity tag '" + tag + "' contains illegal characters (spaces, commas, etc).");
        }
    }

    // --- 📝 Standard Overrides ---

    /**
     * Returns the tag name, allowing this object to be used seamlessly in
     * {@link StringBuilder} and string concatenation for command building.
     * <p><b>Implementation:</b> Should typically return the result of {@link #getTagName()}.</p>
     * @return The tag string.
     */
    @Override
    String toString();

    // --- 🛠️ Static Utility (Default Implementation Helper) ---

    /**
     * A utility method to create a simple, immutable implementation of an EntityTag.
     * <p><b>Error Catching:</b> Validates the provided string immediately to
     * catch formatting errors before they reach the command engine.</p>
     * @param name The raw tag name.
     * @return A validated EntityTag instance.
     * @throws NullPointerException if the name is null.
     */
    static EntityTag of(String name) {
        Objects.requireNonNull(name, "Tag name cannot be null.");

        return new EntityTag() {
            private final String tagName = name.trim();

            {
                // Validate immediately upon instantiation
                validate();
            }

            @Override
            public String getTagName() {
                return tagName;
            }

            @Override
            public String toString() {
                return tagName;
            }
        };
    }
}