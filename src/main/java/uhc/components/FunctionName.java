package uhc.components;

/**
 * 📍 **Minecraft Resource Identifier Contract**
 * <p>
 * This interface provides a common type for any object that can be
 * invoked via the {@code /function} command.
 * </p>
 */
public interface FunctionName {

    /**
     * Returns the full, command-ready identifier for the resource.
     * <p>
     * <b>Implementation Requirements:</b>
     * <ul>
     * <li>For <b>Functions</b>: Must return the namespaced path (e.g., {@code "uhc:init/load"}).</li>
     * <li>For <b>Tags</b>: Must return the namespaced path prefixed with a hash (e.g., {@code "#minecraft:tick"}).</li>
     * </ul>
     * </p>
     * * @return A {@link String} formatted for direct insertion into a Minecraft command.
     */
    String getIdentifier();
}