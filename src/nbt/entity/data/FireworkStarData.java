package nbt.entity.data;

import shared.FireworkShape;
import java.util.List;

/**
 * Data Transfer Object (DTO) holding the configuration for a single Firework Star Explosion NBT.
 * This separates the configuration from the NBT construction logic.
 */
public class FireworkStarData {
    private final FireworkShape shape;
    private final List<Integer> colors; // TextColor IDs (int)
    private final boolean trail;
    private final boolean flicker;

    public FireworkStarData(FireworkShape shape, List<Integer> colors, boolean trail, boolean flicker) {
        if (colors == null || colors.isEmpty()) {
            throw new IllegalArgumentException("Firework star must have at least one color.");
        }
        this.shape = shape;
        this.colors = colors;
        this.trail = trail;
        this.flicker = flicker;
    }

    public FireworkShape getShape() { return shape; }
    public List<Integer> getColors() { return colors; }
    public boolean isTrail() { return trail; }
    public boolean isFlicker() { return flicker; }
}