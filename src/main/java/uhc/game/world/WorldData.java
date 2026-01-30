package uhc.game.world;

import uhc.arguments.block.BlockPos;
import uhc.arguments.block.ColumnPos;
import java.util.Objects;

/**
 * 🌍 **World Data Configuration (WorldData)**
 * <p>
 * This class serves as the central registry for world-specific constants and
 * coordinate anchors used throughout the UHC game logic. It manages the
 * physical limits of the world and dynamic scaling based on player count.
 * </p>
 * <p>
 * <b>Strict Policy:</b> This class does not provide default fallbacks. If data
 * is accessed before initialization or with invalid parameters, an explicit
 * exception is thrown.
 * </p>
 */
public class WorldData {

    // --- 📏 Coordinate Constants ---

    /** * The lowest possible Y-coordinate in the world (the Void floor).
     * Standard Minecraft 1.18+ value is -64.
     */
    public static final int worldBottom = -64;

    /** * The absolute X and Z center of the world, represented as a column.
     * Used as the anchor for world border operations.
     */
    public static final ColumnPos spawnColumn;

    /** * The absolute position at the center of the world at the very bottom height.
     * Coordinates: (0, {@link #worldBottom}, 0).
     */
    public static final BlockPos spawnBottom;

    /** * The default spawn block position for entities at sea level.
     * Coordinates: (0, 64, 0).
     */
    public static final BlockPos spawnBlock;

    // --- 📈 Dynamic Game State ---

    /** * The calculated radius of the world border (distance from center to edge).
     * Initialized via {@link #calculateWorldSize(int)}.
     */
    private static Integer worldRadius;

    // --- 🏗️ Static Initializer ---

    static {
        try {
            // Validate basic world height constraints
            if (worldBottom > 0) {
                throw new IllegalStateException("World Configuration Error: worldBottom cannot be positive.");
            }

            // Primary Anchor Initialization
            spawnColumn = ColumnPos.absolute(0, 0);
            spawnBottom = BlockPos.absolute(0, worldBottom, 0);
            spawnBlock = BlockPos.absolute(0, 64, 0);

            // Null-check validation for coordinate objects
            Objects.requireNonNull(spawnColumn, "Static Init Error: spawnColumn failed to initialize.");
            Objects.requireNonNull(spawnBottom, "Static Init Error: spawnBottom failed to initialize.");
            Objects.requireNonNull(spawnBlock, "Static Init Error: spawnBlock failed to initialize.");

        } catch (Exception e) {
            // No-fallback: If constants cannot be built, the application must stop.
            throw new RuntimeException("CRITICAL: WorldData static registry initialization failed.", e);
        }
    }

    // --- ⚙️ Size Calculation Logic ---

    /**
     * Calculates and locks the world size based on the participant count.
     * <p><b>Scaling Logic:</b></p>
     * <ul>
     * <li>1-6 players: 500 block radius</li>
     * <li>7-20 players: 750 block radius</li>
     * <li>21+ players: 1000 block radius</li>
     * </ul>
     *
     * @param players The total number of players in the session.
     * @throws IllegalArgumentException if player count is less than 1.
     * @throws IllegalStateException if the world size has already been defined.
     */
    public static void calculateWorldSize(int players) {
        if (worldRadius != null) {
            throw new IllegalStateException("WorldData Error: calculateWorldSize was called, but size is already locked at " + worldRadius);
        }
        if (players < 1) {
            throw new IllegalArgumentException("WorldData Error: Cannot calculate world size for " + players + " players. Minimum 1 required.");
        }

        if (players <= 6) {
            worldRadius = 500;
        } else if (players <= 20) {
            worldRadius = 750;
        } else {
            worldRadius = 1000;
        }
    }

    /**
     * Retrieves the calculated world border radius (center to edge).
     * * @return The world border radius in blocks.
     * @throws IllegalStateException if accessed before {@link #calculateWorldSize(int)} is called.
     */
    public static int getWorldRadius() {
        if (worldRadius == null) {
            throw new IllegalStateException("WorldData Error: Attempted to access worldRadius before it was initialized.");
        }
        return worldRadius;
    }

    /**
     * Retrieves the total calculated world diameter (edge to edge).
     * <p>This value is used for the {@code /worldborder set} command.</p>
     * * @return The world border diameter (2 * radius).
     * @throws IllegalStateException if accessed before initialization.
     */
    public static int getWorldDiameter() {
        // We call getWorldRadius() to trigger the null-check logic automatically
        return 2 * getWorldRadius();
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Validates if a Y-coordinate is within the legal bounds of the world height.
     * * @param y The Y-coordinate to check.
     * @return {@code true} if valid.
     * @throws IllegalArgumentException if the coordinate is below {@link #worldBottom}.
     */
    public static boolean validateY(int y) {
        if (y < worldBottom) {
            throw new IllegalArgumentException("Coordinate Error: Y-level " + y + " is below the defined world bottom (" + worldBottom + ").");
        }
        return true;
    }
}