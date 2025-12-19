package uhc.command.commands;

import uhc.arguments.coordinate.Vec3;
import uhc.command.MinecraftCommand;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.entity.EntityId;

import java.util.Objects;

/**
 * 🥚 **Summon Command Builder**
 * <p>
 * Provides a fluent API for the {@code /summon} command. This command is used to
 * spawn an entity (mobs, items, projectiles, etc.) into the world.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /summon <entity> [<pos>] [<nbt>]}
 * </p>
 */
public class SummonCommand implements MinecraftCommand {

    // The required type of entity to be summoned.
    private final EntityId entity;
    // The optional position. Defaults to executor's position if null.
    private Vec3 pos;
    // The optional NBT data tag for the entity.
    private CompoundTag nbt;

    private SummonCommand(EntityId entity) {
        this.entity = Objects.requireNonNull(entity, "EntityId cannot be null.");
    }

    /**
     * Initializes a new Summon command builder.
     * @param entity The namespaced ID of the entity to spawn (e.g., minecraft:zombie).
     * @return A new SummonCommand instance.
     */
    public static SummonCommand create(EntityId entity) {
        return new SummonCommand(entity);
    }

    /**
     * Sets the location where the entity will spawn.
     * @param pos The Vec3 coordinates.
     * @return The current builder instance.
     */
    public SummonCommand pos(Vec3 pos) {
        this.pos = Objects.requireNonNull(pos, "Position argument cannot be null.");
        return this;
    }

    /**
     * Applies custom NBT data to the summoned entity.
     * <p>Note: If used without a specific position, the builder will
     * automatically provide {@code ~ ~ ~} as a placeholder.</p>
     * @param nbt The CompoundTag containing properties (e.g., {IsBaby:1b, Health:10f}).
     * @return The current builder instance.
     */
    public SummonCommand nbt(CompoundTag nbt) {
        this.nbt = Objects.requireNonNull(nbt, "NBT CompoundTag cannot be null.");
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * <p>Handles the positional requirement: NBT cannot be appended unless
     * a position argument is present.</p>
     * @return The formatted command (e.g., "summon zombie ~ ~ ~ {IsBaby:1b}").
     */
    @Override
    public String generate() {
        StringBuilder command = new StringBuilder("summon ");

        // 1. Mandatory Entity ID
        command.append(entity);

        // 2. Handle Positional Logic
        // Minecraft syntax requires coordinates if NBT is to follow.
        if (pos != null) {
            command.append(" ").append(pos);

            // 3. Append NBT if coordinates were already added
            if (nbt != null) {
                command.append(" ").append(TagConverter.toJson(nbt));
            }
        } else if (nbt != null) {
            // If NBT exists but no pos was set, we must provide a relative placeholder
            command.append(" ~ ~ ~ ").append(TagConverter.toJson(nbt));
        }
        // If both pos and nbt are null, command is simply "summon <entity>"

        return command.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}