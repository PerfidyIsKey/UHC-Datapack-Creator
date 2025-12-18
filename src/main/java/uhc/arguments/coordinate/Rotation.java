package uhc.arguments.coordinate;

import java.util.Locale;

/**
 * 🔄 **Rotation Argument**
 * <p>
 * Represents a rotation consisting of Yaw (horizontal) and Pitch (vertical).
 * Supports both absolute values and relative (tilde) notation.
 * </p>
 */
public class Rotation {
    private final String yaw;
    private final String pitch;

    private Rotation(String yaw, String pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Creates an absolute rotation.
     * @param yaw Horizontal rotation (-180.0 to 179.9).
     * @param pitch Vertical rotation (-90.0 to 90.0).
     * @return A new Rotation instance.
     * @throws IllegalArgumentException if pitch is outside the valid range.
     */
    public static Rotation at(double yaw, double pitch) {
        if (pitch < -90.0 || pitch > 90.0) {
            throw new IllegalArgumentException("Pitch must be between -90.0 and 90.0 degrees.");
        }
        return new Rotation(format(yaw), format(pitch));
    }

    /**
     * Creates a rotation relative to the current execution rotation (~ ~).
     */
    public static Rotation relative() {
        return new Rotation("~", "~");
    }

    /**
     * Creates a rotation with relative offsets (e.g., ~-5 ~5).
     * @param yawOffset Horizontal offset in degrees.
     * @param pitchOffset Vertical offset in degrees.
     * @return A new Rotation instance.
     */
    public static Rotation relative(double yawOffset, double pitchOffset) {
        return new Rotation("~" + format(yawOffset), "~" + format(pitchOffset));
    }

    /**
     * Utility to format doubles to remove trailing zeros where possible.
     */
    private static String format(double value) {
        if (value == 0.0) return "0";
        // Uses US locale to ensure period decimal separator
        return String.format(Locale.US, "%s", value).replaceAll("\\.0$", "");
    }

    @Override
    public String toString() {
        return yaw + " " + pitch;
    }
}