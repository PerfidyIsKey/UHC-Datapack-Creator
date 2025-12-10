package uhc.resource.block;

/**
 * Defines the contract for the identifier part of a block predicate.
 * This can be a standard block resource location (Block ID) or a block tag
 * prefixed with the '#' symbol (Block Tag).
 */
public interface BlockIdentifier {
    /**
     * Returns the exact string needed for the command argument.
     * Examples: 'minecraft:stone' or '#minecraft:planks'.
     * @return The formatted identifier string.
     */
    String getResourceLocation();

    @Override
    String toString();
}