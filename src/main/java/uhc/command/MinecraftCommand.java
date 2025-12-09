package uhc.command;

/**
 * 🧱 **Minecraft Command Interface**
 * * Defines the essential contract for all command objects used within the datapack project.
 * Any class representing a specific Minecraft command (e.g., {@code SayCommand},
 * {@code ExecuteCommand}) must implement this interface to be recognized and correctly
 * processed by the {@code Generator} and {@code DatapackComponent} systems.
 */
public interface MinecraftCommand {

    /**
     * Generates the final, executable Minecraft command string.
     * * This method is the core responsibility of every command class, translating
     * type-safe object arguments and command structure into a single, valid command line.
     * * @return The complete command string (e.g., "say Hello World"), ready for a {@code .mcfunction} file.
     */
    String generate();

    /**
     * Provides a convenient string representation of the command, typically used for
     * debugging or quick output conversion.
     * * This method should delegate directly to {@code generate()}.
     * * @return The complete command string.
     */
    @Override
    String toString();
}