package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.item.components.ComponentId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🎇 **Fireworks Component Implementation**
 * <p>
 * Manages the "minecraft:fireworks" data component.
 * Controls flight duration and the list of explosion effects.
 * </p>
 */
public class FireworksComponent implements ItemComponent {

    private byte flightDuration = 1;
    private final List<Explosion> explosions = new ArrayList<>();

    private FireworksComponent() {}

    public static FireworksComponent create() {
        return new FireworksComponent();
    }

    /**
     * Sets the flight duration of the rocket.
     * @param duration Number of gunpowders (typically 1-3). Range: -128 to 127.
     */
    public FireworksComponent flightDuration(int duration) {
        this.flightDuration = (byte) duration;
        return this;
    }

    /**
     * Adds an explosion effect to this firework.
     * @param explosion A built {@link Explosion} instance.
     */
    public FireworksComponent addExplosion(Explosion explosion) {
        if (this.explosions.size() < 256) {
            this.explosions.add(Objects.requireNonNull(explosion));
        }
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.FIREWORKS;
    }

    @Override
    public NBTTag toNbt() {
        CompoundTag root = CompoundTag.create(getId().getResourceLocation());

        root.put(new ByteTag("flight_duration", flightDuration));

        if (!explosions.isEmpty()) {
            ListTag explosionList = new ListTag("explosions");
            for (Explosion exp : explosions) {
                explosionList.add(exp.toNbt());
            }
            root.put(explosionList);
        }

        return root;
    }

    /**
     * 💥 **Firework Explosion Builder**
     */
    public static class Explosion {
        private FireworkShape shape = FireworkShape.SMALL_BALL;
        private final List<Integer> colors = new ArrayList<>();
        private final List<Integer> fadeColors = new ArrayList<>();
        private boolean hasTrail = false;
        private boolean hasTwinkle = false;

        private Explosion() {}

        public static Explosion create() {
            return new Explosion();
        }

        public Explosion shape(FireworkShape shape) {
            this.shape = shape;
            return this;
        }

        public Explosion color(int rgb) {
            this.colors.add(rgb);
            return this;
        }

        public Explosion fadeColor(int rgb) {
            this.fadeColors.add(rgb);
            return this;
        }

        public Explosion trail(boolean trail) {
            this.hasTrail = trail;
            return this;
        }

        public Explosion twinkle(boolean twinkle) {
            this.hasTwinkle = twinkle;
            return this;
        }

        protected CompoundTag toNbt() {
            CompoundTag tag = CompoundTag.create(""); // Unnamed list element
            tag.put(new StringTag("shape", shape.getNbtName()));
            tag.put(new ByteTag("has_trail", (byte) (hasTrail ? 1 : 0)));
            tag.put(new ByteTag("has_twinkle", (byte) (hasTwinkle ? 1 : 0)));

            if (!colors.isEmpty()) {
                tag.put(new IntArrayTag("colors", colors.stream().mapToInt(i -> i).toArray()));
            }
            if (!fadeColors.isEmpty()) {
                tag.put(new IntArrayTag("fade_colors", fadeColors.stream().mapToInt(i -> i).toArray()));
            }

            return tag;
        }
    }

    public enum FireworkShape {
        SMALL_BALL, LARGE_BALL, STAR, CREEPER, BURST;
        public String getNbtName() { return name().toLowerCase(); }
    }
}