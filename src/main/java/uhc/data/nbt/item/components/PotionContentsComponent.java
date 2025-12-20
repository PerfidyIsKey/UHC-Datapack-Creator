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
 * Manages the "minecraft:potion_contents" component.
 * </p>
 */
public class PotionContentsComponent implements ItemComponent {

    private EffectId potion;
    private Integer customColor;
    private String customName;
    private final List<CustomEffect> customEffects = new ArrayList<>();

    public PotionContentsComponent() {}

    public PotionContentsComponent(EffectId potionId) {
        this.potion = potionId;
    }

    public PotionContentsComponent potion(EffectId potionId) {
        this.potion = potionId;
        return this;
    }

    public PotionContentsComponent customColor(int color) {
        this.customColor = color;
        return this;
    }

    /**
     * @param customName Plain string suffix for the translation key.
     */
    public PotionContentsComponent customName(String customName) {
        this.customName = customName;
        return this;
    }

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

        // 1. Simple Mode
        if (potion != null && customColor == null && customName == null && customEffects.isEmpty()) {
            return new StringTag(res, potion.getResourceLocation());
        }

        // 2. Detailed Mode
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

    public static class CustomEffect {
        private final EffectId id;
        private byte amplifier;
        private int duration;
        private boolean ambient = false;
        private boolean showParticles = true;
        private boolean showIcon = true;

        public CustomEffect(EffectId id, int duration, int amplifier) {
            this.id = Objects.requireNonNull(id);
            // Minecraft logic: amplifier 0 is level 1.
            this.amplifier = (byte) Math.max(0, amplifier);
            // duration -1 is infinite, 0/lesser is 1 tick.
            this.duration = duration;
        }

        public CustomEffect ambient(boolean ambient) { this.ambient = ambient; return this; }
        public CustomEffect particles(boolean show) { this.showParticles = show; return this; }
        public CustomEffect icon(boolean show) { this.showIcon = show; return this; }

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