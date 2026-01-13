package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.coordinate.Vec3;
import uhc.command.MinecraftCommand;
import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.entity.EntityId;

import java.util.Objects;

/**
 * 🥚 **Summon Command Builder**
 * <p>
 * Provides a semantic API for the {@code /summon} command.
 * This command spawns a new entity into the world with optional positional
 * data (using {@link Vec3} or {@link BlockPos}) and complex NBT components.
 * </p>
 */
public class SummonCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The type of entity to summon (e.g., minecraft:zombie). */
    private final EntityId entity;

    /** * The optional location where the entity will spawn. */
    private final Vec3 pos;

    /** * The optional NBT data (Mob data, Item data, etc.) for the spawned entity. */
    private final EntityNBT<?> nbt;

    // --- 🏗️ Constructors & Static Entry Points ---

    /**
     * Private constructor used by the static factory methods.
     * @param entity The non-null entity type.
     * @param pos    The optional position (may be null).
     * @param nbt    The optional NBT builder (may be null).
     */
    private SummonCommand(EntityId entity, Vec3 pos, EntityNBT<?> nbt) {
        this.entity = Objects.requireNonNull(entity, "Summon Error: EntityId cannot be null.");
        this.pos = pos;
        this.nbt = nbt;
    }

    /**
     * Entry point for a basic summon at the executor's location.
     * @param entity The entity type to spawn.
     * @return A SummonCommand instance.
     */
    public static SummonCommand entity(EntityId entity) {
        return new SummonCommand(entity, null, null);
    }

    // --- 🚀 Vec3 Overloads ---

    /**
     * Entry point for summoning an entity at a specific vector position.
     * @param entity The entity type to spawn.
     * @param pos    The high-precision coordinate location.
     * @return A SummonCommand instance.
     */
    public static SummonCommand entity(EntityId entity, Vec3 pos) {
        Objects.requireNonNull(pos, "Summon Error: Vec3 position cannot be null.");
        return new SummonCommand(entity, pos, null);
    }

    /**
     * Entry point for summoning an entity at a specific vector position with custom NBT data.
     * @param entity The entity type to spawn.
     * @param pos    The high-precision coordinate location.
     * @param nbt    The entity NBT builder.
     * @return A SummonCommand instance.
     */
    public static SummonCommand entity(EntityId entity, Vec3 pos, EntityNBT<?> nbt) {
        Objects.requireNonNull(pos, "Summon Error: Vec3 position cannot be null when defining NBT.");
        Objects.requireNonNull(nbt, "Summon Error: NBT builder cannot be null.");
        return new SummonCommand(entity, pos, nbt);
    }

    // --- 🚀 BlockPos Overloads ---

    /**
     * Entry point for summoning an entity at a specific block position.
     * <p>Note: Internally converts the {@link BlockPos} to a {@link Vec3}.</p>
     * @param entity The entity type to spawn.
     * @param pos    The discrete block coordinate location.
     * @return A SummonCommand instance.
     */
    public static SummonCommand entity(EntityId entity, BlockPos pos) {
        Objects.requireNonNull(pos, "Summon Error: BlockPos cannot be null.");
        return new SummonCommand(entity, pos.toVec3(), null);
    }

    /**
     * Entry point for summoning an entity at a specific block position with custom NBT data.
     * @param entity The entity type to spawn.
     * @param pos    The discrete block coordinate location.
     * @param nbt    The entity NBT builder.
     * @return A SummonCommand instance.
     */
    public static SummonCommand entity(EntityId entity, BlockPos pos, EntityNBT<?> nbt) {
        Objects.requireNonNull(pos, "Summon Error: BlockPos cannot be null when defining NBT.");
        Objects.requireNonNull(nbt, "Summon Error: NBT builder cannot be null.");
        return new SummonCommand(entity, pos.toVec3(), nbt);
    }

    /**
     * Entry point for summoning an entity at the executor's location with custom NBT data.
     * <p>Note: This will automatically handle the coordinate placeholders (~ ~ ~) internally.</p>
     * @param entity The entity type to spawn.
     * @param nbt    The entity NBT builder.
     * @return A SummonCommand instance.
     */
    public static SummonCommand entity(EntityId entity, EntityNBT<?> nbt) {
        Objects.requireNonNull(nbt, "Summon Error: NBT builder cannot be null.");
        return new SummonCommand(entity, null, nbt);
    }

    // --- ⚙️ Command Generation ---

    /**
     * Validates and generates the final Minecraft command string.
     * <p>
     * <b>Syntax Logic:</b>
     * If NBT is present but no position was provided, {@code ~ ~ ~} is injected
     * because NBT is the third optional argument.
     * </p>
     * @return The formatted /summon command.
     * @throws RuntimeException if the NBT conversion or string assembly fails.
     */
    @Override
    public String generate() {
        final StringBuilder sb = new StringBuilder("summon ");

        try {
            sb.append(this.entity.getResourceLocation());

            // Handle coordinate positioning
            if (this.pos != null) {
                sb.append(" ").append(this.pos);
            } else if (this.nbt != null) {
                // Minecraft requires coordinates if NBT is to follow.
                sb.append(" ~ ~ ~");
            }

            // Handle NBT component
            if (this.nbt != null) {
                final String jsonNbt = TagConverter.toJson(this.nbt.build());
                if (jsonNbt == null || jsonNbt.isBlank()) {
                    throw new IllegalStateException("NBT conversion resulted in an empty string.");
                }
                sb.append(" ").append(jsonNbt);
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to generate /summon command for ["
                    + this.entity.getResourceLocation() + "]. Details: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the generated command string.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}