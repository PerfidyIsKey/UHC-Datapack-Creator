package uhc.arguments.block;

import uhc.resource.block.BlockIdentifier;

/**
 * A concrete, immutable implementation of the {@link BlockPredicate} interface
 * that represents the simplest form of a block predicate: an identifier only.
 * <p>
 * This is suitable for matching all instances of a single Block ID (e.g., 'minecraft:dirt')
 * or all blocks within a specific Block Tag (e.g., '#minecraft:planks').
 */
public class SimpleBlockPredicate implements BlockPredicate {

    // The underlying BlockIdentifier (ID or Tag) which provides the resource string.
    private final BlockIdentifier id;

    /**
     * Private constructor to enforce object creation via the static factory method.
     *
     * @param id The immutable identifier (ID or Tag) of the block predicate.
     */
    private SimpleBlockPredicate(BlockIdentifier id) {
        this.id = id;
    }

    /**
     * Static factory method to create a new SimpleBlockPredicate instance.
     *
     * @param id The required {@link BlockIdentifier} for this predicate argument.
     * @return A new SimpleBlockPredicate instance.
     * @throws IllegalArgumentException if the provided {@code id} is null.
     */
    public static SimpleBlockPredicate create(BlockIdentifier id) {
        if (id == null) {
            throw new IllegalArgumentException("BlockIdentifier (ID or Tag) cannot be null when creating a SimpleBlockPredicate.");
        }
        return new SimpleBlockPredicate(id);
    }

    /**
     * Returns the identifier string as the complete block predicate string.
     *
     * @return The fully qualified block identifier string (e.g., 'minecraft:dirt' or '#minecraft:planks').
     * * NOTE: Assuming getResourceLocation() is the correct method on BlockIdentifier
     * to return the final string (ID or #Tag). If using the suggested BlockIdentifier
     * structure, this method should ideally call {@code id.getIdentifier()}.
     */
    @Override
    public String getPredicate() {
        // Using getResourceLocation() as in the original code, but ensure
        // BlockIdentifier/BlockId interface returns the correct string here.
        return id.getResourceLocation();
    }

    /**
     * Returns the identifier string, suitable for direct insertion into a command.
     *
     * @return The fully qualified block identifier string.
     */
    @Override
    public String toString() {
        return getPredicate();
    }
}