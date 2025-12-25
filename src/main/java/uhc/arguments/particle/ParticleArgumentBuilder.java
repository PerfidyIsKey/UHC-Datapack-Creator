package uhc.arguments.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ✨ **Particle Argument Builder**
 * <p>
 * A specialized builder designed to construct the configuration NBT for particles
 * that require additional data, such as {@code minecraft:dust} or {@code minecraft:block}.
 * </p>
 * <p>
 * <b>Output Format:</b> {@code {key1:value1,key2:value2}}
 * </p>
 */
public class ParticleArgumentBuilder {

    // --- ⚙️ State & Fields ---

    /** * Internal collection of formatted configuration strings.
     * Each entry represents a key-value pair (e.g., "scale:1.0").
     */
    private final List<String> arguments = new ArrayList<>();

    // --- 🏗️ Constructor & Factories ---

    /**
     * Private constructor to enforce the use of the fluent {@link #create()} method.
     */
    private ParticleArgumentBuilder() {}

    /**
     * Initializes a new fluent builder for particle configuration.
     * @return A fresh {@link ParticleArgumentBuilder} instance.
     */
    public static ParticleArgumentBuilder create() {
        return new ParticleArgumentBuilder();
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Defines the RGB color for the particle.
     * <p><b>Error Catching:</b> Validates that color components are within the
     * standard 0.0 to 1.0 range.</p>
     * * @param red   Red component (0.0 - 1.0).
     * @param green Green component (0.0 - 1.0).
     * @param blue  Blue component (0.0 - 1.0).
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if any color component is out of bounds.
     */
    public ParticleArgumentBuilder color(float red, float green, float blue) {
        validateColor(red, green, blue);
        this.arguments.add("color:[" + red + "," + green + "," + blue + "]");
        return this;
    }

    /**
     * Defines the RGB color using a float array.
     * <p><b>Error Catching:</b> Validates array length (must be 3) and
     * ensures components are within the 0.0 to 1.0 range.</p>
     * * @param rgb A float array containing [R, G, B].
     * @return This builder instance for chaining.
     * @throws NullPointerException     if the array is null.
     * @throws IllegalArgumentException if the array length is not 3 or values are out of bounds.
     */
    public ParticleArgumentBuilder color(float[] rgb) {
        Objects.requireNonNull(rgb, "Color array cannot be null.");
        if (rgb.length != 3) {
            throw new IllegalArgumentException("RGB color array must have exactly 3 elements.");
        }

        validateColor(rgb[0], rgb[1], rgb[2]);
        this.arguments.add("color:[" + rgb[0] + "," + rgb[1] + "," + rgb[2] + "]");
        return this;
    }

    /**
     * Sets the visual scale of the particle.
     * <p><b>Error Catching:</b> Ensures scale is a positive value.</p>
     * * @param scale The size multiplier.
     * @return This builder instance for chaining.
     * @throws IllegalArgumentException if scale is negative.
     */
    public ParticleArgumentBuilder scale(float scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Particle scale cannot be negative.");
        }
        this.arguments.add("scale:" + scale);
        return this;
    }

    // --- 🛰️ Generation Logic ---

    /**
     * Compiles the added arguments into a valid Minecraft NBT string.
     * <p><b>Error Catching:</b> Returns an empty string if no arguments were provided
     * to prevent generating empty curly braces {@code {}} which might be invalid
     * for certain particle types.</p>
     * * @return A formatted string like {@code {color:[1.0,0.0,0.0],scale:1.0}},
     * or an empty string if no arguments exist.
     */
    public String build() {
        if (arguments.isEmpty()) {
            return "";
        }

        // Format: {arg1:val1,arg2:val2}
        // Note: Minecraft NBT usually uses ':' for key-value pairs
        String content = String.join(",", arguments);
        return "{" + content + "}";
    }

    // --- 🔒 Internal Helpers ---

    /**
     * Helper to validate that floats are within the 0.0 to 1.0 range.
     */
    private void validateColor(float r, float g, float b) {
        if (isInvalid(r) || isInvalid(g) || isInvalid(b)) {
            throw new IllegalArgumentException("Color components must be between 0.0 and 1.0.");
        }
    }

    private boolean isInvalid(float val) {
        return val < 0.0f || val > 1.0f;
    }
}