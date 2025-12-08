package commands.data;

public enum DataAction {
    /** Corresponds to the 'data get' subcommand. */
    GET,
    /** Corresponds to the 'data merge' subcommand. */
    MERGE,
    /** Corresponds to the 'data modify' subcommand. */
    MODIFY,
    /** Corresponds to the 'data remove' subcommand. */
    REMOVE;

    /**
     * Converts the enum name (e.g., GET) to the required lowercase
     * Minecraft command subcommand part (e.g., "get").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}