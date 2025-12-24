package uhc.command.commands;

import uhc.arguments.coordinate.Vec3;
import uhc.command.MinecraftCommand;
import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.util.TagConverter;
import uhc.resource.entity.EntityId;

import java.util.Objects;

/**
 * 🥚 **Summon Command Builder**
 * <p>
 * Provides a fluent API for the {@code /summon} command.
 * Accepts any {@link EntityNBT} implementation, providing maximum flexibility
 * for spawning diverse UHC entities.
 * </p>
 */
public class SummonCommand implements MinecraftCommand {

    private final EntityId entity;
    private Vec3 pos;
    private EntityNBT<?> nbt; // Uses wildcard to accept any subclass of EntityNBT

    private SummonCommand(EntityId entity) {
        this.entity = Objects.requireNonNull(entity, "EntityId cannot be null.");
    }

    public static SummonCommand create(EntityId entity) {
        return new SummonCommand(entity);
    }

    public SummonCommand pos(Vec3 pos) {
        this.pos = Objects.requireNonNull(pos, "Position argument cannot be null.");
        return this;
    }

    /**
     * Applies entity NBT data.
     * @param nbt Any builder extending EntityNBT (ItemEntityNBT, MobNBT, etc.)
     * @return The current builder instance.
     */
    public SummonCommand nbt(EntityNBT<?> nbt) {
        this.nbt = Objects.requireNonNull(nbt, "EntityNBT cannot be null.");
        return this;
    }

    @Override
    public String generate() {
        StringBuilder command = new StringBuilder("summon ");
        command.append(entity.getResourceLocation());

        // Coordinate logic: NBT follows coordinates.
        if (pos != null) {
            command.append(" ").append(pos);
        } else if (nbt != null) {
            command.append(" ~ ~ ~");
        }

        if (nbt != null) {
            // Converts the built CompoundTag into a JSON string for the command.
            command.append(" ").append(TagConverter.toJson(nbt.build()));
        }

        return command.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}