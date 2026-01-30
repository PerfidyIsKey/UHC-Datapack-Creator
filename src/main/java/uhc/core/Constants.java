package uhc.core;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.resource.color.TextColor;
import uhc.resource.entity.EntityId;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 📐 **Project Constants**
 * <p>
 * This class serves as the central repository for global, immutable conversion factors
 * and static values used across the UHC engine. It ensures mathematical consistency
 * for time-based logic and scoring calculations.
 * </p>
 * <p>
 * <b>Strict Policy:</b> This is a static-only utility class. Instantiation is
 * prohibited, and all mathematical operations include overflow protection.
 * </p>
 */
public final class Constants {

    // --- 👥 Game Entities ---

    /**
     * The global administration entity reference.
     * <p>Targets the nearest {@code minecraft:marker} entity used for technical command execution.</p>
     */
    public static final Entity admin;

    // --- 🎨 Visual Components ---

    /**
     * A standardized grey divider used in chat and sidebar formatting.
     * <p>Display: {@code  | } in Dark Gray and Bold.</p>
     */
    public static final TextComponent bannerText;

    // --- ⏳ Time Conversion Factors ---

    /** * The number of seconds contained within a single minute.
     * <p>Value: {@code 60}</p>
     */
    public static final int MIN_TO_SECOND = 60;

    /** * The number of game ticks that occur in one real-world second in Minecraft.
     * <p>Value: {@code 20}</p>
     */
    public static final int TICK_TO_SECOND = 20;

    // --- 🚀 Static Initialization ---

    static {
        try {
            // 1. Initialize the admin selector targeting specific technical markers
            admin = Entity.ofSelector(
                    TargetSelector.NEAREST_ENTITY,
                    SelectorArgumentsBuilder.create().type(EntityId.MARKER));

            // 2. Initialize formatting components for UI consistency
            bannerText = TextComponent.text(" | ").color(TextColor.DARK_GRAY).bold(true);

            // 3. Strict verification of integrity
            if (admin == null) {
                throw new IllegalStateException("Admin entity selector failed to build.");
            }
            if (bannerText == null) {
                throw new IllegalStateException("Banner text component failed to build.");
            }

        } catch (Exception e) {
            // Re-wrapping in ExceptionInInitializerError to halt the JVM if constants are missing
            throw new ExceptionInInitializerError("CRITICAL: Failed to initialize global Constants registry. " + e.getMessage());
        }
    }

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to enforce the static utility nature of the class.
     * @throws UnsupportedOperationException Always thrown to prevent instantiation.
     */
    private Constants() {
        throw new UnsupportedOperationException("Constants is a static utility class and cannot be instantiated.");
    }

    // --- 🛠️ Mathematical Utilities ---

    /**
     * Converts a duration from minutes to seconds with overflow protection.
     * <p>Formula: {@code minutes * 60}</p>
     *
     * @param minutes The duration in minutes to be converted.
     * @return The equivalent duration in seconds as an integer.
     * @throws ArithmeticException if the resulting value exceeds {@link Integer#MAX_VALUE}.
     * @throws IllegalArgumentException if the provided minute value is negative.
     */
    public static int minutesToSeconds(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Time Error: Cannot convert negative minutes (" + minutes + ").");
        }
        try {
            // Using Math.multiplyExact to prevent silent overflow errors
            return Math.multiplyExact(minutes, MIN_TO_SECOND);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Time Conversion Overflow: " + minutes + " minutes exceeds integer capacity in seconds.");
        }
    }

    /**
     * Converts a duration from minutes to Minecraft game ticks with overflow protection.
     * <p>Formula: {@code minutes * 60 * 20}</p>
     *
     * @param minutes The duration in minutes to be converted.
     * @return The equivalent duration in game ticks (20 ticks = 1 second).
     * @throws ArithmeticException if the final tick count exceeds {@link Integer#MAX_VALUE}.
     * @throws IllegalArgumentException if the provided minute value is negative.
     */
    public static int minutesToTicks(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Time Error: Cannot convert negative minutes (" + minutes + ").");
        }
        try {
            // Step-by-step exact multiplication to ensure precision at every stage
            int seconds = Math.multiplyExact(minutes, MIN_TO_SECOND);
            return Math.multiplyExact(seconds, TICK_TO_SECOND);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Tick Conversion Overflow: " + minutes + " minutes is too large to represent in game ticks.");
        }
    }

    /**
     * Converts a duration from seconds to Minecraft game ticks.
     * <p>Formula: {@code seconds * 20}</p>
     *
     * @param seconds The duration in seconds to be converted.
     * @return The equivalent duration in ticks.
     * @throws ArithmeticException if the result overflows.
     */
    public static int secondsToTicks(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Time Error: Cannot convert negative seconds (" + seconds + ").");
        }
        try {
            return Math.multiplyExact(seconds, TICK_TO_SECOND);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Tick Conversion Overflow: " + seconds + " seconds is too large for an integer tick count.");
        }
    }
}