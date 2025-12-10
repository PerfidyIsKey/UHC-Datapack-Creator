package uhc.data.nbt.entity.data;

import uhc.resource.EntityTag;

/**
 * Data Transfer Object (DTO) for configuring the NBT structure of a Minecraft Armor Stand entity.
 * <p>
 * This record defines the common boolean flags and entity tags that affect
 * an armor stand's behavior and appearance.
 *
 * @param invulnerable If true, prevents the stand from taking damage (NBT: {@code Invulnerable}).
 * @param marker If true, reduces the hitbox and prevents interaction/gravity (NBT: {@code Marker}).
 * @param invisible If true, hides the stand's model (NBT: {@code Invisible}).
 * @param tags An array of custom NBT tags to apply to the entity.
 */
public record ArmorStandData(
        boolean invulnerable,
        boolean marker,
        boolean invisible,
        EntityTag[] tags
) {

    /**
     * Compact Canonical Constructor for validation and ensuring field integrity.
     * Ensures the {@code tags} array is never null, providing a default empty array if null is passed.
     */
    public ArmorStandData {
        // Use Objects.requireNonNullElse for a clean way to handle null tags input
        // or reassign if the constructor receives a null.
        if (tags == null) {
            tags = new EntityTag[0];
        }
    }

    /**
     * Static factory method to create a new ArmorStandData instance.
     *
     * @param invulnerable Should the stand be invulnerable?
     * @param marker Should the stand be a marker (reduced hitbox, no gravity)?
     * @param invisible Should the stand be invisible?
     * @param tags Array of custom NBT tags. Can be null or empty.
     * @return A new, validated ArmorStandData record.
     */
    public static ArmorStandData create(boolean invulnerable, boolean marker, boolean invisible, EntityTag[] tags) {
        return new ArmorStandData(invulnerable, marker, invisible, tags);
    }
}