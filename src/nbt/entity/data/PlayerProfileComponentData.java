package nbt.entity.data;

/**
 * Data Transfer Object for the content of the "minecraft:profile" item component.
 * This is used for player heads to specify the texture via the player name.
 */
public record PlayerProfileComponentData(
        String playerName
) {}