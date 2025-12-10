package uhc.data.nbt.entity;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.StringTag;
import uhc.data.nbt.tags.ByteTag;
import uhc.resource.EntityType;
import uhc.data.resource.BooleanNbtProperty;

/**
 * Provides the foundational NBT structure for all entities.
 * Modified to optionally omit the 'id' tag for cleaner /summon commands.
 */
public class BaseEntityNbt {

    private final EntityType id;
    private String customName;
    private final CompoundTag nbt = CompoundTag.create("");

    private BaseEntityNbt(EntityType id, String customName) {
        this.id = id;
        this.customName = customName;
        // The explicit addition of the "id" tag has been removed here to match the
        // minimal output requirements requested for the /summon command.
    }

    public static BaseEntityNbt create(EntityType id, String customName) {
        return new BaseEntityNbt(id, customName);
    }

    public void setBooleanProperty(BooleanNbtProperty property, boolean value) {
        byte byteValue = value ? (byte)1 : (byte)0;
        this.nbt.put(new ByteTag(property.getNbtName(), byteValue));
    }

    public CompoundTag buildNbt() {
        if (customName != null && !customName.isEmpty()) {
            this.nbt.put(new StringTag("CustomName", customName));
        }
        return this.nbt;
    }
}