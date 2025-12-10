package uhc.resource;

import uhc.resource.block.BlockIdentifier;

/**
 * Defines the contract for any object representing a Minecraft Block ID.
 * <p>
 * This ID must be a valid Minecraft resource location, which includes
 * the namespace (default is {@code minecraft}) and the block name (e.g., {@code stone}).
 * The full required format is typically {@code namespace:block_name}.
 * <p>
 * This interface extends {@link BlockIdentifier}, ensuring that a Block ID can be
 * used wherever a generic block or block tag identifier is required.
 */
public interface BlockId extends BlockIdentifier {

    /**
     * Retrieves the complete, fully qualified resource location string for the block.
     * <p>
     * Example outputs:
     * <ul>
     * <li>{@code minecraft:grass_block}</li>
     * <li>{@code custom_mod:my_ore}</li>
     * </ul>
     *
     * @return The fully qualified resource location string.
     */
    String getResourceLocation();

    /**
     * Overrides the default {@code toString()} method to return the resource location.
     * This is crucial for seamless concatenation when building the final command string.
     *
     * @return The fully qualified resource location string, identical to {@code getResourceLocation()}.
     */
    @Override
    String toString();
}