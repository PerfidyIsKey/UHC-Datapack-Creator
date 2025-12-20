package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.EffectId;
import uhc.resource.item.components.ComponentId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🧪 **Potion Contents Component Implementation**
 * <p>
 * Manages the "minecraft:potion_contents" data component (1.20.5+).
 * Handles vanilla potion types, custom RGB liquid colors, and specific
 * status effect overrides.
 * </p>
 */
public class PotionContentsComponent implements ItemComponent {

    private EffectId potion;
    private Integer customColor;
    private String customName;
    private final List<CustomEffect> customEffects = new ArrayList<>();

    private PotionContentsComponent(EffectId potionId) {
        this.potion = potionId;
    }

    /**
     * Creates an empty potion component.
     */
    public static PotionContentsComponent create() {
        return new PotionContentsComponent(null);
    }

    /**
     * Creates a potion component based on a vanilla potion type.
     * @param potionId The type-safe ID (e.g., PotionId.HEALING).
     */
    public static PotionContentsComponent create(EffectId potionId) {
        return new PotionContentsComponent(potionId);
    }

    public PotionContentsComponent potion(EffectId potionId) {
        this.potion = potionId;
        return this;
    }

    /**
     * Sets a custom RGB color for the potion liquid and particles.
     * @param color Integer RGB value (e.g., 0xFF0000 for Red).
     */
    public PotionContentsComponent customColor(int color) {
        this.customColor = color;
        return this;
    }

    /**
     * Sets a custom RGB color using a HEX string.
     * <p>
     * Supported formats: {@code "#RRGGBB"} or {@code "RRGGBB"}.
     * </p>
     * <b>Example:</b> {@code .customColor("#FF5555")} for UHC Red.
     *
     * @param hex The hex color string.
     * @return This component instance for chaining.
     * @throws IllegalArgumentException if the hex string is invalid.
     */
    public PotionContentsComponent customColor(String hex) {
        Objects.requireNonNull(hex, "Hex color string cannot be null.");

        // Remove hash if present
        String cleanHex = hex.startsWith("#") ? hex.substring(1) : hex;

        // Error Catch: Validate length (must be 6 characters for RRGGBB)
        if (cleanHex.length() != 6) {
            throw new IllegalArgumentException("Invalid HEX color length. Expected 6 characters, got: " + cleanHex.length());
        }

        try {
            // Convert Hex to decimal integer for NBT
            this.customColor = Integer.parseInt(cleanHex, 16);
        } catch (NumberFormatException e) {
            // Error Catch: Handle non-hexadecimal characters (e.g., "GGGGGG")
            throw new IllegalArgumentException("Invalid HEX color characters in: " + hex);
        }

        return this;
    }

    /**
     * Sets a custom name suffix used in translation keys.
     * Note: This is not the display name; use CustomNameComponent for that.
     */
    public PotionContentsComponent customName(String customName) {
        this.customName = customName;
        return this;
    }

    /**
     * Adds a unique status effect to the potion.
     */
    public PotionContentsComponent addEffect(CustomEffect effect) {
        this.customEffects.add(Objects.requireNonNull(effect));
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.POTION_CONTENTS;
    }

    @Override
    public NBTTag toNbt() {
        String res = getId().getResourceLocation();

        // Mode A: Simple (Only a base potion type, no custom data)
        if (potion != null && customColor == null && customName == null && customEffects.isEmpty()) {
            return new StringTag(res, potion.getResourceLocation());
        }

        // Mode B: Detailed (Compound tag for custom brews)
        CompoundTag root = CompoundTag.create(res);
        if (potion != null) root.put(new StringTag("potion", potion.getResourceLocation()));
        if (customColor != null) root.put(new IntTag("custom_color", customColor));
        if (customName != null) root.put(new StringTag("custom_name", customName));

        if (!customEffects.isEmpty()) {
            ListTag effectList = new ListTag("custom_effects");
            for (CustomEffect effect : customEffects) {
                effectList.add(effect.toNbt());
            }
            root.put(effectList);
        }
        return root;
    }

    /**
     * Represents a specific status effect with duration and potency.
     * <p>
     * Use {@link #create(EffectId, int, int)} to instantiate.
     * </p>
     */
    public static class CustomEffect {
        private final EffectId id;
        private byte amplifier;
        private int duration;
        private boolean ambient = false;
        private boolean showParticles = true;
        private boolean showIcon = true;

        /**
         * Private constructor to enforce factory method usage.
         */
        private CustomEffect(EffectId id, int duration, int amplifier) {
            this.id = Objects.requireNonNull(id, "Effect ID cannot be null.");
            // Minecraft logic: amplifier 0 is level 1, 1 is level 2, etc.
            this.amplifier = (byte) Math.max(0, amplifier);
            this.duration = duration;
        }

        /**
         * Factory method for creating a custom status effect.
         * * @param id The type-safe EffectId (e.g., EffectId.REGENERATION).
         * @param duration Total ticks the effect lasts (20 ticks = 1 second). Use -1 for infinite.
         * @param amplifier Potency level MINUS ONE (0 = Level I, 1 = Level II).
         * @return A new CustomEffect instance.
         */
        public static CustomEffect create(EffectId id, int duration, int amplifier) {
            return new CustomEffect(id, duration, amplifier);
        }

        /**
         * Sets whether this effect is 'ambient' (like a Beacon).
         * Ambient effects have much subtler, translucent particles.
         */
        public CustomEffect ambient(boolean ambient) {
            this.ambient = ambient;
            return this;
        }

        /**
         * Toggles the visibility of particles emitted by the player.
         */
        public CustomEffect particles(boolean show) {
            this.showParticles = show;
            return this;
        }

        /**
         * Toggles the visibility of the effect icon in the top-right of the HUD.
         */
        public CustomEffect icon(boolean show) {
            this.showIcon = show;
            return this;
        }

        /**
         * Serializes the effect into an unnamed CompoundTag for the custom_effects list.
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