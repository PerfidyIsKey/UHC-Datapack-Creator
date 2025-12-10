package uhc.arguments.itemstack.components;

import uhc.arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'use_cooldown' component.
 * Format: use_cooldown={seconds:30}
 */
public class UseCooldownComponent implements ItemComponentTag {
    private final int seconds;

    private UseCooldownComponent(int seconds) {
        this.seconds = seconds;
    }

    public static UseCooldownComponent create(int seconds) {
        return new UseCooldownComponent(seconds);
    }

    @Override
    public String buildComponentString() {
        // Output format: use_cooldown={seconds:30}
        return "use_cooldown={seconds:" + seconds + "}";
    }
}