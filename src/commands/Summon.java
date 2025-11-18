package commands;

import arguments.Vec3;
import nbt.tags.CompoundTag;
import nbt.util.TagConverter;
import shared.EntityType;

public class Summon {
    private final EntityType entity; // Specifies the entity to be summoned.
    private Vec3 pos; // Specifies the position to summon the entity. If not specified, defaults to the position of the command's execution.
    private CompoundTag nbt; // Specifies the data tag for the entity, this can include any NBT in Entity format.

    private Summon(EntityType entity) {
        this.entity = entity;
    }

    public static Summon create(EntityType entity) {
        return new Summon(entity);
    }

    public Summon setPos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public Summon setNbt(CompoundTag nbt) {
        this.nbt = nbt;
        return this;
    }

    public String build() {
        StringBuilder command = new StringBuilder("summon ");

        command.append(entity.toString());

        if (pos != null) {
            command.append(" ").append(pos.toString());

            if (nbt != null) {
                command.append(" ").append(TagConverter.toJson(nbt));
            }
        }

        return command.toString();
    }

    @Override
    public String toString() {
        return build();
    }

}
