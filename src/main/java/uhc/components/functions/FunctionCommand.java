package uhc.components.functions;

/**
 * Represents a single command line within a Minecraft function.
 */
public record FunctionCommand(String command) {

    /**
     * Outputs the command ready for the file.
     */
    @Override
    public String toString() {
        return command;
    }
}