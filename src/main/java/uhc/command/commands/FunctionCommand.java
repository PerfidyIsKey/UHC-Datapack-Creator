package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.components.FunctionName;
import uhc.resource.data.DataPath;
import uhc.data.nbt.tags.CompoundTag;
import uhc.resource.data.StoragePath;

/**
 * 🛠️ **Function Command Builder**
 * <p>
 * Constructs the {@code /function} command for Minecraft, supporting three primary syntaxes:
 * <ul>
 * <li>Basic: {@code /function <name>}</li>
 * <li>Static Args: {@code /function <name> <arguments>}</li>
 * <li>Macro with Source: {@code /function <name> with <source_type> <source> [<path>]}</li>
 * </ul>
 * </p>
 */
public class FunctionCommand implements MinecraftCommand {
    private final FunctionName name;
    private CompoundTag arguments;
    private FunctionSource source;
    private DataPath dataPath;

    /**
     * Private constructor used by the factory method.
     * @param name The target function or tag identifier.
     */
    private FunctionCommand(FunctionName name) {
        this.name = name;
    }

    /** * Factory method to initialize a function command builder.
     * @param name The identifier of the function (e.g., FunctionPath.LOAD).
     * @return A new builder instance.
     */
    public static FunctionCommand create(FunctionName name) {
        return new FunctionCommand(name);
    }

    /** * Appends static NBT arguments to the function call.
     * <p><b>Note:</b> This is mutually exclusive with {@link #with(FunctionSource)}.</p>
     * @param arguments The CompoundTag containing argument data.
     * @return The builder instance for chaining.
     */
    public FunctionCommand arguments(CompoundTag arguments) {
        this.arguments = arguments;
        this.source = null; // Enforce exclusivity
        return this;
    }

    /** * Configures the command to run as a macro, pulling data from a specific source.
     * <p><b>Note:</b> This is mutually exclusive with {@link #arguments(CompoundTag)}.</p>
     * @param source The data source (Block, Entity, or Storage).
     * @return The builder instance for chaining.
     */
    public FunctionCommand with(FunctionSource source) {
        this.source = source;
        this.arguments = null; // Enforce exclusivity
        return this;
    }

    /** * Specifies an optional NBT path to narrow down data from the source.
     * <p>Only utilized if {@link #with(FunctionSource)} is set.</p>
     * @param dataPath The NBT path (e.g., "Items[0]").
     * @return The builder instance for chaining.
     */
    public FunctionCommand path(DataPath dataPath) {
        this.dataPath = dataPath;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return A string starting with "function ...".
     * @throws IllegalStateException If the function name is missing or if data path is provided without a source.
     */
    @Override
    public String generate() {
        if (name == null) {
            throw new IllegalStateException("Function name/identifier cannot be null.");
        }

        StringBuilder sb = new StringBuilder("function ");
        sb.append(name.getIdentifier());

        if (arguments != null) {
            // Variant: function <name> <nbt_compound_tag>
            sb.append(" ").append(arguments);
        } else if (source != null) {
            // Variant: function <name> with (block|entity|storage) <source_id> [<nbt_path>]
            sb.append(" with ")
                    .append(source.getType())
                    .append(" ")
                    .append(source.getValue());

            if (dataPath != null && !dataPath.toString().isEmpty()) {
                sb.append(" ").append(dataPath);
            }
        } else if (dataPath != null) {
            // Logic Error Catching: A path without a source is invalid syntax in Minecraft
            throw new IllegalStateException("Cannot specify a DataPath without providing a FunctionSource (using .with())");
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return generate();
    }

    /** * Defines the structure for data sources compatible with the 'with' keyword.
     */
    public interface FunctionSource {
        /** @return The Minecraft source type keyword: "block", "entity", or "storage". */
        String getType();
        /** @return The string representation of the specific source (coords, selector, or ID). */
        String getValue();
    }

    /** * Represents a block source using coordinates.
     * <p>Result: {@code block <x> <y> <z>}</p>
     */
    public record BlockSource(BlockPos pos) implements FunctionSource {
        @Override public String getType() { return "block"; }
        @Override public String getValue() { return pos.toString(); }
    }

    /** * Represents an entity source using a selector or UUID.
     * <p>Result: {@code entity <selector>}</p>
     */
    public record EntitySource(Entity entity) implements FunctionSource {
        @Override public String getType() { return "entity"; }
        @Override public String getValue() { return entity.toString(); }
    }

    /** * Represents a global storage source.
     * <p>Result: {@code storage namespace:path}</p>
     */
    public record StorageSource(StoragePath storagePath) implements FunctionSource {
        @Override
        public String getType() {
            return "storage";
        }

        @Override
        public String getValue() {
            return storagePath.getIdentifier();
        }
    }
}