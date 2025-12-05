package arguments.itemstack.components;

import shared.EffectId;

/**
 * Represents a single custom effect object within the {@code custom_effects} array,
 * part of the {@code minecraft:potion_contents} component.
 * <p>
 * Final SNBT format: {@code {id:"minecraft:speed", amplifier:0, duration:200, show_particles:0b, show_icon:0b, ambient:0b}}
 */
public class CustomEffectEntry {
    private final EffectId id;
    private int amplifier = 0; // Default: 0 (Level I)
    private int duration = 200; // Default: 200 ticks (10 seconds)
    private boolean showParticles = false; // Default: false (0b)
    private boolean showIcon = false; // Default: false (0b)
    private boolean ambient = false; // Default: false (0b)

    /**
     * Private constructor. Ensures the required EffectId is set upon creation.
     * @param id The base effect ID for this entry (e.g., EffectId.SPEED).
     * @throws IllegalArgumentException if {@code id} is null.
     */
    private CustomEffectEntry(EffectId id) {
        if (id == null) {
            throw new IllegalArgumentException("EffectId cannot be null.");
        }
        this.id = id;
    }

    /**
     * Static factory method to begin defining a custom effect.
     * @param id The base effect ID.
     * @return A new CustomEffectEntry instance.
     */
    public static CustomEffectEntry create(EffectId id) {
        return new CustomEffectEntry(id);
    }

    // --- Fluent Setters for Customization ---

    /**
     * Sets the power level of the effect (0 = Level I, 1 = Level II, etc.).
     * @param amplifier The amplifier value (must be non-negative).
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the amplifier is negative.
     */
    public CustomEffectEntry amplifier(int amplifier) {
        if (amplifier < 0) {
            throw new IllegalArgumentException("Amplifier cannot be negative.");
        }
        this.amplifier = amplifier;
        return this;
    }

    /**
     * Sets the duration of the effect in game ticks (20 ticks = 1 second).
     * @param duration The duration in ticks. Must be non-negative.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the duration is negative.
     */
    public CustomEffectEntry duration(int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative.");
        }
        this.duration = duration;
        return this;
    }

    /**
     * Controls whether particle effects are displayed around the affected entity.
     * @param showParticles {@code true} to show particles (1b), {@code false} to hide them (0b). Default is {@code false}.
     * @return This builder for chaining.
     */
    public CustomEffectEntry showParticles(boolean showParticles) {
        this.showParticles = showParticles;
        return this;
    }

    /**
     * Controls whether the effect icon is displayed in the corner of the player's screen.
     * @param showIcon {@code true} to show the icon (1b), {@code false} to hide it (0b). Default is {@code false}.
     * @return This builder for chaining.
     */
    public CustomEffectEntry showIcon(boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }

    /**
     * Controls whether the effect is rendered subtly in the background, like a beacon effect.
     * Ambient effects have a different particle color.
     * @param ambient {@code true} for ambient effect (1b), {@code false} otherwise (0b). Default is {@code false}.
     * @return This builder for chaining.
     */
    public CustomEffectEntry ambient(boolean ambient) {
        this.ambient = ambient;
        return this;
    }

    /**
     * Builds the final SNBT object string for this single effect entry.
     * @return The SNBT string, e.g., {@code {id:"minecraft:speed",amplifier:0,duration:200,show_particles:0b,show_icon:0b,ambient:0b}}.
     */
    public String buildSnbt() {
        // Minecraft SNBT uses 0b/1b for boolean bytes
        int particles = showParticles ? 1 : 0;
        int icon = showIcon ? 1 : 0;
        int ambientByte = ambient ? 1 : 0;

        return String.format(
                "{id:\"%s\",amplifier:%d,duration:%d,show_particles:%db,show_icon:%db,ambient:%db}",
                id.getResourceLocation(), // Assumes EffectId correctly provides the resource location (e.g., "minecraft:speed")
                amplifier,
                duration,
                particles,
                icon,
                ambientByte
        );
    }
}