package uhc.core;

import uhc.arguments.block.BlockPos;
import uhc.arguments.block.ColumnPos;
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
 */
public final class Constants {

    // --- 👥 Game Entities ---

    /**
     * The global administration entity reference.
     * <p>Targets the nearest {@code minecraft:marker} entity used for technical command execution.</p>
     */
    public static final Entity admin;

    // Coordinates
    public static final ColumnPos spawnColumn;
    public static final BlockPos spawnPos;

    public static final int worldBottom = -64;

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
            // Initialize the admin selector
            admin = Entity.ofSelector(
                    TargetSelector.NEAREST_ENTITY,
                    SelectorArgumentsBuilder.create().type(EntityId.MARKER));

            spawnColumn = ColumnPos.absolute(0, 0);
            spawnPos = BlockPos.absolute(0, 64, 0);

            // Initialize formatting components
            bannerText = TextComponent.text(" | ").color(TextColor.DARK_GRAY).bold(true);

            // Verify integrity of constants
            Objects.requireNonNull(admin, "Constants failure: Admin entity selector could not be initialized.");
            Objects.requireNonNull(bannerText, "Constants failure: bannerText component could not be initialized.");

        } catch (Exception e) {
            throw new ExceptionInInitializerError("Critical failure during static initialization of Constants: " + e.getMessage());
        }
    }

    // --- 🏗️ Constructor ---

    /**
     * Private constructor to prevent the instantiation of this utility class.
     * @throws UnsupportedOperationException Always thrown with a clear error message.
     */
    private Constants() {
        throw new UnsupportedOperationException("Constants is a static utility class and cannot be instantiated.");
    }

    // --- 🛠️ Mathematical Utilities ---

    /**
     * Converts a duration from minutes to seconds.
     * <p>Formula: {@code minutes * 60}</p>
     * * @param minutes The duration in minutes to be converted.
     * @return The equivalent duration in seconds.
     * @throws ArithmeticException if the result exceeds the capacity of an integer.
     */
    public static int minutesToSeconds(int minutes) {
        try {
            // Math.multiplyExact provides built-in overflow detection
            return Math.multiplyExact(minutes, MIN_TO_SECOND);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Conversion failed: " + minutes + " minutes is too large to represent in seconds.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during minutesToSeconds conversion: " + e.getMessage());
        }
    }

    /**
     * Converts a duration from minutes to Minecraft game ticks.
     * <p>Formula: {@code minutes * 60 * 20}</p>
     * * @param minutes The duration in minutes to be converted.
     * @return The equivalent duration in ticks.
     * @throws ArithmeticException if the calculation results in an integer overflow.
     */
    public static int minutesToTicks(int minutes) {
        try {
            // Chained exact multiplication to ensure precision and safety
            int seconds = Math.multiplyExact(minutes, MIN_TO_SECOND);
            return Math.multiplyExact(seconds, TICK_TO_SECOND);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Conversion failed: " + minutes + " minutes is too large to represent in game ticks.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during minutesToTicks conversion: " + e.getMessage());
        }
    }
}