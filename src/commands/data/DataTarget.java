package commands.data;

/**
 * Interface defining the contract for the target of a Minecraft '/data' command.
 * Implementing classes specify whether the target is a block (e.g., 'block 10 60 5')
 * or an entity (e.g., 'entity @s').
 */
public interface DataTarget {

    /**
     * Retrieves the raw string representation of the target selector or coordinates.
     * @return The target string (e.g., "@s" or "10 60 5").
     */
    String getTarget();

    /**
     * Builds the complete target part of the command, including the target type prefix.
     * Implementing classes should typically return a string like "entity @s" or "block 10 60 5".
     * @return The full target string for the command.
     */
    @Override
    String toString();
}