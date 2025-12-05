package commands.item;

/**
 * Represents the target or source of an item command, which can be a block or an entity.
 * (block <pos> | entity <targets>)
 */
public interface ItemTarget {
    /**
     * Returns the command string prefix (e.g., "block 10 60 20" or "entity @s").
     */
    String getCommandString();
}