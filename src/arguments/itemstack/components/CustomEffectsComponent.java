package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the SNBT array for the 'custom_effects' field within the 'minecraft:potion_contents' component.
 * It acts as a container for one or more individual {@code CustomEffectEntry} objects.
 * <p>
 * Final SNBT format: {@code custom_effects:[{...effect1...}, {...effect2...}]}
 */
public class CustomEffectsComponent implements ItemComponentTag {
    // The list holds the individual effect objects. Using final ensures the list reference doesn't change.
    private final List<CustomEffectEntry> effects;

    /**
     * Private constructor initializes an empty list of effects.
     */
    private CustomEffectsComponent() {
        this.effects = new ArrayList<>();
    }

    /**
     * Static factory method to begin building the custom effects list.
     * @return A new CustomEffectsComponent instance.
     */
    public static CustomEffectsComponent create() {
        return new CustomEffectsComponent();
    }

    /**
     * Adds a single custom effect object to the list.
     * @param effect The CustomEffectEntry instance, which represents a single potion effect definition. Cannot be null.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the {@code effect} is null.
     */
    public CustomEffectsComponent addEffect(CustomEffectEntry effect) {
        if (effect == null) {
            throw new IllegalArgumentException("CustomEffectEntry cannot be null.");
        }
        this.effects.add(effect);
        return this;
    }

    /**
     * Builds the final SNBT string representation for the custom effects array field.
     * @return The formatted SNBT string, e.g., {@code custom_effects:[{id:"minecraft:speed",...}, {...}]}.
     */
    @Override
    public String buildComponentString() {
        if (effects.isEmpty()) {
            // Returns an empty array string if no effects were added.
            return "custom_effects:[]";
        }

        // Stream the list, map each entry to its SNBT object string, and join them with commas,
        // wrapping the whole result in array brackets [].
        String effectsArray = effects.stream()
                .map(CustomEffectEntry::buildSnbt)
                .collect(Collectors.joining(",", "[", "]"));

        return "custom_effects:" + effectsArray;
    }
}