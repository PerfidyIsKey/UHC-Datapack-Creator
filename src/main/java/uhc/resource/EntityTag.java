package uhc.resource;

/**
 * Interface for type-safe representation of Minecraft entity tags used in selectors
 * (e.g., in the 'tag=' selector argument).
 */
public interface EntityTag {
    /**
     * Gets the raw string value of the tag required for commands/NBT.
     */
    String getTagName();

    /**
     * Returns the tag name, used automatically by StringBuilder and String concatenation.
     */
    @Override
    String toString();
}