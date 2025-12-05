package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;
import nbt.tags.CompoundTag;
import nbt.util.TagConverter;

/**
 * Represents the 'custom_data' component, holding custom NBT.
 * Format: custom_data={locateTeammate:1b}
 */
public class CustomDataComponent implements ItemComponentTag {
    private final CompoundTag customNbt;

    private CustomDataComponent(CompoundTag customNbt) {
        this.customNbt = customNbt;
    }

    public static CustomDataComponent create(CompoundTag customNbt) {
        return new CustomDataComponent(customNbt);
    }

    @Override
    public String buildComponentString() {
        // We use the same NBT command string format builder here.
        // Assuming CompoundTag has the toMinecraftCommandString() or similar method.
        return "custom_data=" + TagConverter.toJson(customNbt);
    }
}