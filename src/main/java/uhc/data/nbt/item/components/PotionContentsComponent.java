package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.effect.EffectId;
import uhc.resource.item.components.ComponentId;
import uhc.resource.potion.PotionId;
import uhc.text.TextComponent;
import uhc.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🧪 **Potion Contents Component Implementation**
 * <p>
 * Manages the {@code "minecraft:potion_contents"} data component (1.20.5+).
 * This component stores the liquid type, custom RGB color, and any additional
 * status effects that are not part of the base potion.
 * </p>
 */
public class PotionContentsComponent implements ItemComponent {

    // --- Private Fields ---

    /** * The primary potion registry ID.
     * Determines the base effect, duration, and default color (e.g., "minecraft:strong_swiftness").
     */
    private PotionId potion;

    /** * Optional RGB integer override for the potion liquid and particles.
     * If null, Minecraft uses the default color associated with the {@link #potion} field.
     */
    private Integer customColor;

    /** * The translation suffix for the potion.
     * Note: This modifies the translation key rather than the display name.
     */
    private TextComponent customName;

    /** * A list of extra status effects applied when the potion is consumed.
     * These effects are added on top of the base {@link #potion} effect.
     */
    private final List<CustomEffect> customEffects = new ArrayList<>();

    // --- Constructors & Static Factories ---

    /**
     * Internal constructor for the component.
     * @param potion The base {@link PotionId}, or null for a custom brew with no base.
     */
    private PotionContentsComponent(PotionId potion) {
        this.potion = potion;
    }

    /**
     * Creates an empty potion contents component.
     * @return A new instance with no initial effects or colors.
     */
    public static PotionContentsComponent create() {
        return new PotionContentsComponent(null);
    }

    /**
     * Creates a potion contents component with a specific base potion.
     * @param potion The type-safe {@link PotionId}.
     * @return A new instance initialized with the provided potion.
     */
    public static PotionContentsComponent create(PotionId potion) {
        return new PotionContentsComponent(potion);
    }

    // --- Fluent Setters (API) ---

    /**
     * Sets or updates the base potion type.
     * @param potion The {@link PotionId} (e.g., PotionId.strong(EffectId.REGENERATION)).
     * @return This component instance for method chaining.
     */
    public PotionContentsComponent potion(PotionId potion) {
        this.potion = potion;
        return this;
    }

    /**
     * Sets a custom RGB color using a raw decimal integer.
     * @param color The 24-bit RGB integer (e.g., 16711680 for pure red).
     * @return This component instance for method chaining.
     */
    public PotionContentsComponent customColor(int color) {
        this.customColor = color;
        return this;
    }

    /**
     * Sets a custom RGB color using a HEX string.
     * <p><b>Error Catching:</b> Delegates validation to {@link ColorUtil#hexToInt(String)}.</p>
     * @param hex The hex color string (e.g., "#FF5555").
     * @return This component instance for method chaining.
     * @throws IllegalArgumentException if the hex format is invalid.
     */
    public PotionContentsComponent customColor(String hex) {
        this.customColor = ColorUtil.hexToInt(hex);
        return this;
    }

    /**
     * Sets the custom name suffix for the potion translation key.
     * @param customName The {@link TextComponent} containing the suffix.
     * @return This component instance for method chaining.
     * @throws NullPointerException if customName is null.
     */
    public PotionContentsComponent customName(TextComponent customName) {
        this.customName = Objects.requireNonNull(customName, "Custom name component cannot be null.");
        return this;
    }

    /**
     * Adds an additional status effect to this potion.
     * @param effect The {@link CustomEffect} configuration.
     * @return This component instance for method chaining.
     * @throws NullPointerException if the effect is null.
     */
    public PotionContentsComponent addEffect(CustomEffect effect) {
        this.customEffects.add(Objects.requireNonNull(effect, "Custom effect cannot be null."));
        return this;
    }

    // --- NBT Serialization Logic ---

    /** @return The standardized component ID for potion contents. */
    @Override
    public ComponentId getId() {
        return ComponentId.POTION_CONTENTS;
    }

    /**
     * Serializes the component into Minecraft's NBT format.
     * <p><b>Error Catching:</b> If only a base potion is present, returns a simple {@link StringTag}.
     * Otherwise, constructs a {@link CompoundTag} to hold multiple data points.</p>
     * @return The serialized {@link NBTTag}.
     */
    @Override
    public NBTTag toNbt() {
        String resKey = getId().getResourceLocation();

        // Optimization: Mode A - Simple String (Standard vanilla-style potions)
        if (potion != null && customColor == null && customName == null && customEffects.isEmpty()) {
            return new StringTag(resKey, potion.getResourceLocation());
        }

        // Mode B - Detailed Compound (Custom brews or modified vanilla potions)
        CompoundTag root = CompoundTag.create(resKey);

        if (potion != null) {
            root.put(new StringTag("potion", potion.getResourceLocation()));
        }

        if (customColor != null) {
            root.put(new IntTag("custom_color", customColor));
        }

        if (customName != null) {
            // Extracts raw text from the component as NBT expects a String here
            root.put(new StringTag("custom_name", customName.build()));
        }

        if (!customEffects.isEmpty()) {
            ListTag effectList = new ListTag("custom_effects");
            for (CustomEffect effect : customEffects) {
                effectList.add(effect.toNbt());
            }
            root.put(effectList);
        }

        return root;
    }

    // --- Inner Classes ---

    /**
     * Represents an individual status effect with specific duration and potency.
     */
    public static class CustomEffect {
        private final EffectId id;
        private final byte amplifier;
        private final int duration;
        private boolean ambient = false;
        private boolean showParticles = true;
        private boolean showIcon = true;

        /**
         * Private constructor for CustomEffect.
         * <p><b>Error Catching:</b> Clamps the amplifier to valid byte bounds (-128 to 127).</p>
         */
        private CustomEffect(EffectId id, int duration, int amplifier) {
            this.id = Objects.requireNonNull(id, "Status effect ID cannot be null.");
            this.amplifier = (byte) Math.max(-128, Math.min(127, amplifier));
            this.duration = duration;
        }

        /**
         * Factory method for custom effects.
         * @param id The base {@link EffectId}.
         * @param duration Duration in ticks (20 ticks = 1 second).
         * @param amplifier Potency level (0 = Level I, 1 = Level II).
         * @return A new CustomEffect instance.
         */
        public static CustomEffect create(EffectId id, int duration, int amplifier) {
            return new CustomEffect(id, duration, amplifier);
        }

        /** @param ambient If true, particles are translucent (beacon style). @return this. */
        public CustomEffect ambient(boolean ambient) { this.ambient = ambient; return this; }

        /** @param show If false, no particles are emitted. @return this. */
        public CustomEffect particles(boolean show) { this.showParticles = show; return this; }

        /** @param show If false, the effect icon is hidden from the HUD. @return this. */
        public CustomEffect icon(boolean show) { this.showIcon = show; return this; }

        /**
         * Serializes the effect into an unnamed {@link CompoundTag}.
         * @return The effect data tag.
         */
        protected CompoundTag toNbt() {
            CompoundTag tag = CompoundTag.create("");
            tag.put(new StringTag("id", id.getResourceLocation()));
            tag.put(new ByteTag("amplifier", amplifier));
            tag.put(new IntTag("duration", duration));
            tag.put(new ByteTag("ambient", (byte) (ambient ? 1 : 0)));
            tag.put(new ByteTag("show_particles", (byte) (showParticles ? 1 : 0)));
            tag.put(new ByteTag("show_icon", (byte) (showIcon ? 1 : 0)));
            return tag;
        }
    }
}