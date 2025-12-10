package uhc.arguments.itemstack;

/**
 * Marker interface for all component tags used within the [data_components] part of the give command.
 * E.g., instrument="...", use_cooldown={...}.
 */
public interface ItemComponentTag {
    /**
     * Builds the string representation of the component,
     * e.g., 'instrument="minecraft:..."' or 'use_cooldown={seconds:30}'.
     */
    String buildComponentString();
}