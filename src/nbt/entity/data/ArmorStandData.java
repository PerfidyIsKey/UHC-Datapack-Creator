package nbt.entity.data;

/**
 * Data Transfer Object for the Armor Stand entity NBT structure.
 * Uses a Java Record for conciseness and immutability.
 * * Invulnerable: Prevents the stand from taking damage.
 * Marker: Reduces hitbox and prevents interaction/gravity, essential for invisible markers.
 * Invisible: Hides the stand's model.
 */
public record ArmorStandData(
        boolean invulnerable,
        boolean marker,
        boolean invisible,
        String[] tags
) {
    // Custom constructor to ensure tags is never null
    public ArmorStandData {
        if (tags == null) {
            tags = new String[0];
        }
    }
}