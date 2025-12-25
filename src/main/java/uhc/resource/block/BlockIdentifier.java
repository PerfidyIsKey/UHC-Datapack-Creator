package uhc.resource.block;

/**
 * 🧱 **Specific Block Identifier**
 * <p>
 * This interface marks a resource as a single, discrete block unit in Minecraft.
 * It serves as a type-safe filter to distinguish between individual placeable blocks
 * (like {@code stone} or {@code red_wool}) and block groups/tags (like {@code #logs}).
 * </p>
 * <p>
 * <b>Usage:</b> Use {@code BlockIdentifier} in method signatures that perform
 * world modification (e.g., {@code setBlock}) to prevent illegal usage of tags.
 * </p>
 * * @see BlockResource
 * @see BlockId
 * @see DynamicBlock
 */
public interface BlockIdentifier extends BlockResource {

    /**
     * Inherits the contract for retrieving the namespaced identifier.
     * <p><b>Implementation Note:</b> For a {@code BlockIdentifier}, this must
     * return a specific block ID without the '#' prefix used by tags.</p>
     * * @return The fully qualified resource location (e.g., "minecraft:dirt").
     */
    @Override
    String getResourceLocation();

    /**
     * Validates that the identifier points to a valid, single block.
     * <p><b>Error Catching:</b> Implementations should ensure the path contains
     * only lowercase alphanumeric characters, underscores, dots, or dashes,
     * and strictly lacks a leading '#' symbol.</p>
     * * @throws IllegalStateException if the resource location is malformed or
     * represents a tag instead of a single block.
     */
    @Override
    default void validate() throws IllegalStateException {
        String location = this.getResourceLocation();

        // 1. Check for null or empty strings
        if (location == null || location.isBlank()) {
            throw new IllegalStateException("Block identifier cannot be null or empty.");
        }

        // 2. Ensure it is NOT a tag
        if (location.startsWith("#")) {
            throw new IllegalStateException("Expected a specific block, but found a tag: " + location +
                    ". BlockIdentifiers cannot be prefixed with '#'.");
        }

        // 3. Delegate to base validation for standard syntax checking (regex, etc.)
        BlockResource.super.validate();
    }
}