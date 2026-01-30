package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.components.FunctionName;
import uhc.resource.data.DataPath;
import uhc.data.nbt.tags.CompoundTag;
import uhc.resource.data.StoragePath;

import java.util.Objects;

/**
 * 🛠️ **Function Command Builder (FunctionCommand)**
 * <p>
 * This class orchestrates the generation of the {@code /function} command,
 * supporting basic execution, NBT-argument execution, and data-driven macros.
 * </p>
 * <p>
 * <b>Command Variations:</b>
 * <ul>
 * <li>{@code function <name>}</li>
 * <li>{@code function <name> <arguments>}</li>
 * <li>{@code function <name> with <source_type> <source> [<path>]}</li>
 * </ul>
 * </p>
 */
public final class FunctionCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The unique identifier or tag of the function to be executed.
     * <p>Example: {@code minecraft:load} or {@code uhc:game/start}</p>
     */
    private final FunctionName name;

    /** * Static NBT arguments passed to the function as a macro input.
     * <p>Note: In Minecraft, this is mutually exclusive with the {@code with} keyword.</p>
     */
    private final CompoundTag arguments;

    /** * The dynamic data source (Block, Entity, or Storage) used for macro-based execution.
     * <p>Utilizes the {@link FunctionSource} interface for polymorphic type handling.</p>
     */
    private final FunctionSource source;

    /** * An optional NBT path used to extract a specific subset of data from the {@link #source}.
     * <p>If this is provided, a {@code source} must also be present.</p>
     */
    private final DataPath dataPath;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor used by static overloads to enforce a validated, immutable state.
     * * @param name      The function name (non-null).
     * @param arguments Optional static NBT arguments.
     * @param source    Optional macro data source.
     * @param dataPath  Optional NBT data path.
     * @throws NullPointerException if the {@code name} is null.
     */
    private FunctionCommand(FunctionName name, CompoundTag arguments, FunctionSource source, DataPath dataPath) {
        this.name = Objects.requireNonNull(name, "FunctionCommand Error: name identifier cannot be null.");
        this.arguments = arguments;
        this.source = source;
        this.dataPath = dataPath;
    }

    // --- 🚀 Static Overloads (Single-Call Initialization) ---

    /**
     * Constructs a basic function call.
     * <p>Syntax: {@code /function <name>}</p>
     * * @param name The target function name.
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name) {
        return new FunctionCommand(name, null, null, null);
    }

    /**
     * Constructs a function call with static NBT arguments.
     * <p>Syntax: {@code /function <name> <arguments>}</p>
     * * @param name      The target function name.
     * @param arguments The NBT compound containing arguments (non-null).
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name, CompoundTag arguments) {
        Objects.requireNonNull(arguments, "FunctionCommand Error: arguments cannot be null in this context.");
        return new FunctionCommand(name, arguments, null, null);
    }

    /**
     * Constructs a macro function call using a block as the data source.
     * <p>Syntax: {@code /function <name> with block <sourcePos>}</p>
     * * @param name      The target function name.
     * @param sourcePos The world position of the block source (non-null).
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name, BlockPos sourcePos) {
        Objects.requireNonNull(sourcePos, "FunctionCommand Error: block source position cannot be null.");
        return new FunctionCommand(name, null, new BlockSource(sourcePos), null);
    }

    /**
     * Constructs a macro function call using a block and a specific NBT path.
     * <p>Syntax: {@code /function <name> with block <sourcePos> <path>}</p>
     * * @param name      The target function name.
     * @param sourcePos The world position of the block source (non-null).
     * @param path      The NBT path within the block data (non-null).
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name, BlockPos sourcePos, DataPath path) {
        Objects.requireNonNull(sourcePos, "FunctionCommand Error: block source position cannot be null.");
        Objects.requireNonNull(path, "FunctionCommand Error: data path cannot be null.");
        return new FunctionCommand(name, null, new BlockSource(sourcePos), path);
    }

    /**
     * Constructs a macro function call using an entity as the data source.
     * <p>Syntax: {@code /function <name> with entity <source>}</p>
     * * @param name   The target function name.
     * @param source The entity selector or reference (non-null).
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name, Entity source) {
        Objects.requireNonNull(source, "FunctionCommand Error: entity source cannot be null.");
        return new FunctionCommand(name, null, new EntitySource(source), null);
    }

    /**
     * Constructs a macro function call using an entity and a specific NBT path.
     * <p>Syntax: {@code /function <name> with entity <source> <path>}</p>
     * * @param name   The target function name.
     * @param source The entity selector or reference (non-null).
     * @param path   The NBT path within the entity data (non-null).
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name, Entity source, DataPath path) {
        Objects.requireNonNull(source, "FunctionCommand Error: entity source cannot be null.");
        Objects.requireNonNull(path, "FunctionCommand Error: data path cannot be null.");
        return new FunctionCommand(name, null, new EntitySource(source), path);
    }

    /**
     * Constructs a macro function call using global storage as the data source.
     * <p>Syntax: {@code /function <name> with storage <source> [<path>]}</p>
     * * @param name   The target function name.
     * @param source The storage identifier (non-null).
     * @param path   The optional NBT path within the storage (nullable).
     * @return A validated FunctionCommand instance.
     */
    public static FunctionCommand of(FunctionName name, StoragePath source, DataPath path) {
        Objects.requireNonNull(source, "FunctionCommand Error: storage source cannot be null.");
        return new FunctionCommand(name, null, new StorageSource(source), path);
    }

    // --- ⚙️ Generation Logic ---

    /**
     * Compiles the command components into a valid Minecraft command string.
     * <p>
     * This method performs a final logic check to ensure that the user has not attempted
     * to combine static arguments with a macro source, which is illegal in Minecraft.
     * </p>
     * * @return A formatted string starting with "function".
     * @throws IllegalStateException if the internal configuration is syntactically invalid.
     */
    @Override
    public String generate() {
        try {
            StringBuilder sb = new StringBuilder("function ");
            sb.append(name.getIdentifier());

            // Check for illegal combination: arguments and source are mutually exclusive
            if (arguments != null && source != null) {
                throw new IllegalStateException("Command Syntax Error: Cannot provide both static arguments and a macro 'with' source.");
            }

            if (arguments != null) {
                // Syntax: function <name> <arguments>
                sb.append(" ").append(arguments);
            } else if (source != null) {
                // Syntax: function <name> with <type> <val> [<path>]
                sb.append(" with ")
                        .append(source.getType())
                        .append(" ")
                        .append(source.getValue());

                if (dataPath != null) {
                    sb.append(" ").append(dataPath);
                }
            } else if (dataPath != null) {
                // Logic Error: Path exists but no source to apply it to
                throw new IllegalStateException("Command Syntax Error: DataPath provided without a corresponding FunctionSource.");
            }

            return sb.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to generate function command. " + e.getMessage(), e);
        }
    }

    /**
     * Standard string representation of the generated command.
     * @return The Minecraft command string.
     */
    @Override
    public String toString() {
        return generate();
    }

    // --- 📂 Internal Source Definitions ---

    /**
     * 🧩 **Function Source Interface**
     * <p>
     * Defines the contract for data sources compatible with the {@code with} keyword
     * in the {@code /function} command.
     * </p>
     */
    public interface FunctionSource {
        /** @return The Minecraft source type keyword: "block", "entity", or "storage". */
        String getType();
        /** @return The string representation of the source identifier (coords, selector, or storage path). */
        String getValue();
    }

    /** * 🧱 **Block Source Implementation**
     * <p>Represents a block data source at a specific world position.</p>
     */
    public record BlockSource(BlockPos pos) implements FunctionSource {
        @Override public String getType() { return "block"; }
        @Override public String getValue() { return pos.toString(); }
    }

    /** * 👥 **Entity Source Implementation**
     * <p>Represents an entity data source via selector or UUID.</p>
     */
    public record EntitySource(Entity entity) implements FunctionSource {
        @Override public String getType() { return "entity"; }
        @Override public String getValue() { return entity.toString(); }
    }

    /** * 📦 **Storage Source Implementation**
     * <p>Represents a global storage data source.</p>
     */
    public record StorageSource(StoragePath storagePath) implements FunctionSource {
        @Override public String getType() { return "storage"; }
        @Override public String getValue() { return storagePath.getIdentifier(); }
    }
}