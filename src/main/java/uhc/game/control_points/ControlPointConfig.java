package uhc.game.control_points;

import uhc.core.Constants;

/**
 * ⚙️ **Control Point Configuration**
 * <p>
 * This static utility class serves as the central configuration hub for the
 * Control Point game mechanic. It defines the operational status, scoring
 * frequency, and the mathematical thresholds required for objective capture.
 * </p>
 * <p>
 * This class is designed to be immutable and non-instantiable, mirroring the
 * architecture of {@code DatapackConfig}.
 * </p>
 */
public final class ControlPointConfig {

    // --- 🟢 Status & Operational Mode ---

    /** * Global toggle for the Control Point system.
     * <p>When set to {@code false}, all control point processing, HUD elements,
     * and capture logic will be bypassed by the engine.</p>
     */
    public static final boolean ENABLED = true;

    /** * The specific logic mode currently active for control points.
     * <ul>
     * <li>{@code 0}: King of the Hill (Standard)</li>
     * <li>{@code 1}: Conquest (Multiple occupancy)</li>
     * </ul>
     */
    public static final int MODE = 0;

    // --- 📊 Quantitative Parameters ---

    /** * The total number of active Control Points to be initialized and tracked
     * during the game session.
     */
    public static final int AMOUNT = 2;

    /** * The standard progress score added per second to a Control Point when
     * occupied by a valid player.
     */
    public static final int ADD_RATE = 2;

    // --- 🎯 Capture Thresholds & Calculations ---

    /** * The required duration, in minutes, for a player to achieve a 0% to 100%
     * capture under standard conditions.
     * <p>This field is private as it serves as a raw input for the
     * {@link #MAX_SCORE} calculation.</p>
     */
    private static final int MINUTES_TO_CAPTURE = 20;

    /** * The final score threshold required to successfully capture a Control Point.
     * <p>Calculated dynamically using the engine's time constants:
     * {@code minutesToSeconds(MINUTES_TO_CAPTURE) * ADD_RATE}.</p>
     */
    public static final int MAX_SCORE = calculateMaxScore();

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to prevent the instantiation of this utility class.
     * <p>All configuration parameters must be accessed statically.</p>
     * * @throws UnsupportedOperationException Always thrown to prevent instantiation.
     */
    private ControlPointConfig() {
        try {
            throw new UnsupportedOperationException("ControlPointConfig is a static utility class.");
        } catch (Exception e) {
            // Re-throwing as the required exception type for utility classes
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    // --- 🛠️ Internal Logic ---

    /**
     * Performs the initial calculation for the MAX_SCORE constant.
     * <p>This method handles potential errors during the initialization of the
     * static final field to ensure the class loads safely.</p>
     * * @return The calculated maximum score or a safe default of 2400.
     */
    private static int calculateMaxScore() {
        try {
            return Constants.minutesToSeconds(MINUTES_TO_CAPTURE) * ADD_RATE;
        } catch (Exception e) {
            // Fallback value to prevent class loading failure (20 mins * 60s * 2 rate)
            return 2400;
        }
    }
}