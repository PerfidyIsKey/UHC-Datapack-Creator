package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;
import shared.EffectId;
import java.util.StringJoiner;

/**
 * Represents the "minecraft:potion_contents" component tag.
 * This component defines the base potion type, custom liquid color, and a list of custom effects.
 * <p>
 * Final SNBT format: {@code minecraft:potion_contents={potion:"...", custom_color:..., custom_effects:[...]}}
 */
public class PotionContentsComponent implements ItemComponentTag {
    private EffectId potionId;
    // Use Integer object to distinguish between un-set (null) and color 0 (black).
    private Integer customColor;
    private CustomEffectsComponent effectsComponent;

    private PotionContentsComponent() {}

    /**
     * Static factory method to begin building the PotionContents component.
     * @return A new PotionContentsComponent instance.
     */
    public static PotionContentsComponent create() {
        return new PotionContentsComponent();
    }

    /**
     * Sets the base potion effect ID if this component represents a standard (non-custom) potion.
     * Using this field will override any effects applied via {@code customEffects}.
     * @param potionId The base EffectId (e.g., EffectId.FIRE_RESISTANCE). Cannot be null.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the {@code potionId} is null.
     */
    public PotionContentsComponent potion(EffectId potionId) {
        if (potionId == null) {
            throw new IllegalArgumentException("Base Potion ID cannot be null.");
        }
        this.potionId = potionId;
        return this;
    }

    /**
     * Sets the custom color for the potion liquid using an integer RGB value.
     * This color is used for the potion's liquid texture.
     * @param color The 24-bit integer color (0xRRGGBB, max value is 16777215).
     * @return This builder for chaining.
     */
    public PotionContentsComponent customColor(int color) {
        this.customColor = color;
        return this;
    }

    /**
     * Sets the custom color for the potion liquid using a 6-digit hex string.
     * Converts the hex string (e.g., "808080") to a decimal integer (e.g., 8421504).
     *
     * @param hex The 6-character hex string (e.g., "RRGGBB"). Cannot be null.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the hex string is null, not 6 characters, or contains invalid hex characters.
     */
    public PotionContentsComponent customColor(String hex) {
        if (hex == null || hex.length() != 6) {
            throw new IllegalArgumentException("Hex color must be a non-null, 6-character string.");
        }
        try {
            int color = Integer.parseInt(hex, 16);
            this.customColor = color;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color format: " + hex + ". Must only contain hexadecimal digits (0-9, A-F).", e);
        }
        return this;
    }

    /**
     * Sets the container for one or more custom potion effects to be applied when consumed/used.
     * This field is required for custom potions.
     *
     * @param effects The CustomEffectsComponent containing all effect entries. Cannot be null.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the {@code effects} component is null.
     */
    public PotionContentsComponent customEffects(CustomEffectsComponent effects) {
        if (effects == null) {
            throw new IllegalArgumentException("CustomEffectsComponent cannot be null.");
        }
        this.effectsComponent = effects;
        return this;
    }

    /**
     * Builds the SNBT representation of the component value.
     * @return The SNBT object string, e.g., {@code minecraft:potion_contents={custom_color:8421504,custom_effects:[...]}}.
     */
    @Override
    public String buildComponentString() {
        StringJoiner innerSnbt = new StringJoiner(",");

        // 1. Add base potion ID if present
        if (potionId != null) {
            // NOTE: Assumes EffectId.getResourceLocation() correctly includes the 'minecraft:' namespace.
            innerSnbt.add("potion:\"" + potionId.getResourceLocation() + "\"");
        }

        // 2. Add custom color if present
        if (customColor != null) {
            // custom_color uses the decimal representation of the 24-bit RGB integer
            innerSnbt.add("custom_color:" + customColor);
        }

        // 3. Add custom effects array if present
        if (effectsComponent != null) {
            // The CustomEffectsComponent must return the key-value string: custom_effects:[...]
            innerSnbt.add(effectsComponent.buildComponentString());
        }

        // Handle the case where no fields were set (results in an empty potion)
        if (innerSnbt.length() == 0) {
            // It's technically valid, but likely unintentional. We'll allow it.
        }

        // Final format: component_name={inner_snbt}
        return "minecraft:potion_contents={" + innerSnbt.toString() + "}";
    }
}