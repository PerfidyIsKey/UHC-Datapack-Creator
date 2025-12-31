package uhc.core;

/**
 * 📐 **Project Constants**
 * <p>
 * This class serves as the central repository for global, immutable conversion factors
 * and static values used across the UHC engine. It ensures mathematical consistency
 * for time-based logic and scoring calculations.
 * </p>
 * <p>
 * This is a utility class and cannot be instantiated.
 * </p>
 */
public final class Constants {

    // --- ⏳ Time Conversion Factors ---

    /** * The number of seconds contained within a single minute.
     * <p>Value: {@code 60}</p>
     */
    public static final int MIN_TO_SECOND = 60;

    /** * The number of game ticks that occur in one real-world second in Minecraft.
     * <p>Value: {@code 20}</p>
     */
    public static final int TICK_TO_SECOND = 20;

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to prevent the instantiation of this utility class.
     * * @throws UnsupportedOperationException Always thrown to prevent reflection-based instantiation.
     */
    private Constants() {
        try {
            throw new UnsupportedOperationException("Constants is a static utility class and cannot be instantiated.");
        } catch (Exception e) {
            // Re-throwing to ensure the error is properly propagated
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    // --- 🛠️ Mathematical Methods ---

    /**
     * Safely converts a duration from minutes to seconds.
     * <p>Formula: {@code minutes * MIN_TO_SECOND}</p>
     * * @param minutes The duration in minutes to be converted.
     * @return The equivalent duration in seconds, or {@code 0} if an error occurs.
     */
    public static int minutesToSeconds(int minutes) {
        try {
            return minutes * MIN_TO_SECOND;
        } catch (Exception e) {
            // Error Catching: Prevents arithmetic overflow or calculation errors from crashing the thread
            return 0;
        }
    }

    /**
     * Safely converts a duration from minutes to Minecraft game ticks.
     * <p>Formula: {@code minutes * MIN_TO_SECOND * TICK_TO_SECOND}</p>
     * * @param minutes The duration in minutes to be converted.
     * @return The equivalent duration in ticks, or {@code 0} if an error occurs.
     */
    public static int minutesToTicks(int minutes) {
        try {
            return minutes * MIN_TO_SECOND * TICK_TO_SECOND;
        } catch (Exception e) {
            // Error Catching: Safety fallback for extreme values
            return 0;
        }
    }
}