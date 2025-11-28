package commands;

import arguments.coordinate.Vec3;
import nbt.tags.CompoundTag;
import nbt.util.TagConverter;
import shared.EntityType;

/**
 * Represents the Minecraft 'summon' command structure.
 * This command creates a new entity at a specified position.
 * <p>
 * Command syntax: {@code /summon <entity> [pos] [nbt]}
 */
public class Summon {

    // The required type of entity to be summoned.
    private final EntityType entity;
    // The optional position to summon the entity. Defaults to executor's position if null.
    private Vec3 pos;
    // The optional NBT data tag for the entity.
    private CompoundTag nbt;

    /**
     * Private constructor to enforce object creation via the static factory method.
     *
     * @param entity The required entity type.
     */
    private Summon(EntityType entity) {
        this.entity = entity;
    }

    /**
     * Static factory method to create a new Summon command instance.
     *
     * @param entity The required {@link EntityType} to summon.
     * @return A new Summon instance ready for optional configuration.
     * @throws IllegalArgumentException if the provided {@code entity} is null.
     */
    public static Summon create(EntityType entity) {
        if (entity == null) {
            throw new IllegalArgumentException("EntityType cannot be null for the summon command.");
        }
        return new Summon(entity);
    }

    /**
     * Sets the optional position where the entity will be summoned.
     *
     * @param pos The {@link Vec3} coordinate argument.
     * @return The current Summon instance for method chaining.
     * @throws IllegalArgumentException if the provided {@code pos} is null.
     */
    public Summon pos(Vec3 pos) {
        if (pos == null) {
            throw new IllegalArgumentException("Position argument cannot be null. Use create(entity) for default position.");
        }
        this.pos = pos;
        return this;
    }

    /**
     * Sets the optional NBT data tag to apply to the summoned entity.
     *
     * @param nbt The {@link CompoundTag} containing entity data.
     * @return The current Summon instance for method chaining.
     * @throws IllegalArgumentException if the provided {@code nbt} is null.
     */
    public Summon nbt(CompoundTag nbt) {
        if (nbt == null) {
            throw new IllegalArgumentException("NBT CompoundTag cannot be null.");
        }
        this.nbt = nbt;
        return this;
    }

    /**
     * Constructs and returns the final string representation of the 'summon' command.
     * Correctly handles NBT, even if the position is omitted, by defaulting position to "~ ~ ~".
     *
     * @return The complete, formatted Minecraft command string.
     */
    public String build() {
        StringBuilder command = new StringBuilder("summon ");

        // 1. Append mandatory EntityType
        command.append(entity.toString());

        // 2. Handle position (pos)
        // If pos is set, use it.
        // If pos is NOT set but NBT IS set, we MUST use "~ ~ ~" as the default position placeholder.
        Vec3 finalPos = null;
        if (this.pos != null) {
            finalPos = this.pos;
        } else if (this.nbt != null) {
            // Default to "~ ~ ~" if NBT is present but position is not explicitly set.
            finalPos = Vec3.relative(0, 0, 0);
        }

        if (finalPos != null) {
            command.append(" ").append(finalPos);

            // 3. Handle NBT
            if (nbt != null) {
                // NBT is converted to the required JSON string format.
                command.append(" ").append(TagConverter.toJson(nbt));
            }
        }
        // If neither pos nor nbt is set, the command is simply "summon <entity>".

        return command.toString();
    }

    /**
     * Returns the built command string, identical to {@code build()}.
     */
    @Override
    public String toString() {
        return build();
    }
}