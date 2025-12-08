package nbt.entity;

import nbt.tags.*;
import nbt.entity.data.ArmorStandData;
import shared.EntityTag;

/**
 * Factory/Builder for the Armor Stand Entity NBT structure.
 * <p>
 * This builder translates the strong-typed {@link ArmorStandData} configuration
 * object into a compliant Minecraft NBT {@link CompoundTag} ready for use in commands.
 * The output NBT root tag has an empty string name.
 */
public class ArmorStandNbtBuilder implements EntityNbtBuilder {

    private final ArmorStandData data;

    /**
     * Private constructor to enforce object creation via the static factory method.
     *
     * @param data The non-null, immutable data configuration for the Armor Stand.
     */
    private ArmorStandNbtBuilder(ArmorStandData data) {
        this.data = data;
    }

    /**
     * Static factory method to create a new builder instance.
     *
     * @param data The required {@link ArmorStandData} configuration.
     * @return A new ArmorStandNbtBuilder instance.
     * @throws IllegalArgumentException if the provided {@code data} is null.
     */
    public static ArmorStandNbtBuilder create(ArmorStandData data) {
        if (data == null) {
            throw new IllegalArgumentException("ArmorStandData cannot be null when creating the NBT builder.");
        }
        return new ArmorStandNbtBuilder(data);
    }

    /**
     * Builds the complete NBT {@link CompoundTag} structure for the Armor Stand.
     * <p>
     * Output structure includes: {@code {Invulnerable:1b, Marker:1b, Invisible:1b, Tags:["tag1", "tag2"]}}
     *
     * @return The root CompoundTag containing the Armor Stand configuration.
     */
    @Override
    public CompoundTag buildNbt() {
        CompoundTag rootNbt = CompoundTag.create("");

        // Helper to convert boolean (true) NBT values to the required 1b (ByteTag)
        final Byte TRUE_BYTE = (byte)1;

        // 1. Invulnerable:1b
        if (data.invulnerable()) {
            rootNbt.put(new ByteTag("Invulnerable", TRUE_BYTE));
        }

        // 2. Marker:1b
        if (data.marker()) {
            rootNbt.put(new ByteTag("Marker", TRUE_BYTE));
        }

        // 3. Invisible:1b
        if (data.invisible()) {
            rootNbt.put(new ByteTag("Invisible", TRUE_BYTE));
        }

        // 4. Tags:[]
        // We rely on ArmorStandData ensuring data.tags() is never null (empty array is fine)
        if (data.tags().length > 0) {
            // NBT structure requires a ListTag that holds StringTags
            ListTag tagsList = new ListTag("Tags");

            for (EntityTag tag : data.tags()) {
                // IMPORTANT: String tags inside a ListTag should have an empty name (key)
                tagsList.add(new StringTag("", tag.getTagName()));
            }

            rootNbt.put(tagsList);
        }

        return rootNbt;
    }
}