package uhc.data.nbt.entity.data;

import uhc.game.FireworkShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for creating FireworkStarData DTOs via method chaining.
 * Example: FireworkStarDataBuilder.create().setShape(STAR).addColor(0xFF0000).build();
 */
public class FireworkStarDataBuilder {
    private FireworkShape shape = FireworkShape.SMALL_BALL; // Default shape
    private final List<Integer> colors = new ArrayList<>();
    private boolean trail = false;
    private boolean flicker = false;

    private FireworkStarDataBuilder() {
        // Initialize with a default color (white) if none is explicitly added
        colors.add(0xFFFFFF);
    }

    public static FireworkStarDataBuilder create() {
        return new FireworkStarDataBuilder();
    }

    public FireworkStarDataBuilder setShape(FireworkShape shape) {
        this.shape = shape;
        return this;
    }

    /**
     * Adds a color code (decimal or hex) to the star's explosion.
     * Clears the default white if the first custom color is added.
     */
    public FireworkStarDataBuilder addColor(int color) {
        // Clear default white if custom colors are added
        if (colors.size() == 1 && colors.get(0).equals(0xFFFFFF)) {
            colors.clear();
        }
        this.colors.add(color);
        return this;
    }

    public FireworkStarDataBuilder withTrail() {
        this.trail = true;
        return this;
    }

    public FireworkStarDataBuilder withFlicker() {
        this.flicker = true;
        return this;
    }

    public FireworkStarData buildData() {
        if (colors.isEmpty()) {
            throw new IllegalStateException("Firework star must have at least one color.");
        }
        return new FireworkStarData(shape, colors, trail, flicker);
    }
}