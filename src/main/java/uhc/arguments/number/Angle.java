package uhc.arguments.number;

/**
 * Represents a yaw angle argument for commands like /setworldspawn.
 * Must be a single-precision floating-point number or include tilde (~) notation.
 */
public class Angle {
    private final String yaw;

    private Angle(String yaw) {
        // Regex: Allows optional tilde (~), optional sign (-), and floating point numbers
        // (e.g., 90.0, -180, ~45.5).
        if (!yaw.matches("^~?-?\\d+(\\.\\d+)?$")) {
            throw new IllegalArgumentException("Invalid angle format. Must be a single float or a float with '~'.");
        }
        this.yaw = yaw;
    }

    /**
     * Creates an absolute yaw angle (e.g., 0.0, -180.0).
     */
    public static Angle absolute(double yaw) {
        return new Angle(String.valueOf(yaw));
    }

    /**
     * Creates a relative yaw angle (e.g., ~45.5).
     * @param yaw The offset from the current execution yaw rotation.
     */
    public static Angle relative(double yaw) {
        return new Angle("~" + yaw);
    }

    @Override
    public String toString() {
        // Returns the single yaw value (e.g., "-90.5" or "~15.0")
        return yaw;
    }
}