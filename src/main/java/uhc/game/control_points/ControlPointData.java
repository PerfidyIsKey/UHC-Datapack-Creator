package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.resource.world.BiomeId;
import uhc.resource.world.DimensionId;

import java.util.Objects;

/**
 * 🚩 **Control Point Data**
 * <p>
 * An immutable data model representing a capture-based objective within the game.
 * This class encapsulates scoring parameters, physical world coordinates, and
 * environmental context required for game-loop processing.
 * </p>
 */
public final class ControlPointData {

    // --- 🔑 Scoring & Identity ---

    /** * The unique numerical identifier for this objective.
     */
    private final int id;

    /** * The maximum progress score required to successfully capture this point.
     */
    private final int max;

    /** * The amount of progress score contributed per occupancy interval.
     */
    private final int rate;

    // --- 📍 Spatial & Environmental Context ---

    /** * The physical location of the control point center in the world.
     */
    private final BlockPos pos;

    /** * The specific dimension (Overworld, Nether, etc.) where this point resides.
     */
    private final DimensionId dimension;

    /** * The biome associated with the point's location.
     */
    private final BiomeId biome;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new validated instance of ControlPointData.
     *
     * @param id        The unique ID of the point.
     * @param max       The total score required for capture.
     * @param rate      The score increment granted to occupants.
     * @param pos       The physical {@link BlockPos}. Cannot be null.
     * @param dimension The {@link DimensionId}. Cannot be null.
     * @param biome     The {@link BiomeId}. Cannot be null.
     * @throws NullPointerException if pos, dimension, or biome are null.
     */
    public ControlPointData(int id, int max, int rate, BlockPos pos,
                            DimensionId dimension, BiomeId biome) {
        this.id = id;
        this.max = max;
        this.rate = rate;

        // Error Catching: Validating mandatory spatial objects.
        this.pos = Objects.requireNonNull(pos, "Position coordinate cannot be null.");
        this.dimension = Objects.requireNonNull(dimension, "Dimension identifier cannot be null.");
        this.biome = Objects.requireNonNull(biome, "Biome identifier cannot be null.");
    }

    // --- 📝 Naming Logic ---

    /**
     * Provides the standardized internal identifier for the control point.
     * * @return The string {@code "cp"} appended with the numerical ID (e.g., "cp1").
     */
    public String name() {
        try {
            return "cp" + id;
        } catch (Exception e) {
            // Fallback to prevent logic breaks during ID string formation.
            return "cp_err_" + id;
        }
    }

    // --- 🔍 Accessors ---

    /** * @return The unique identifier integer.
     */
    public int getId() {
        return id;
    }

    /** * @return The capture score ceiling.
     */
    public int getMax() {
        return max;
    }

    /** * @return The progress accumulation rate.
     */
    public int getRate() {
        return rate;
    }

    /** * @return The {@link BlockPos} location.
     */
    public BlockPos getPos() {
        return pos;
    }

    /** * @return The {@link DimensionId} location.
     */
    public DimensionId getDimension() {
        return dimension;
    }

    /** * @return The {@link BiomeId} environment.
     */
    public BiomeId getBiome() {
        return biome;
    }

    // --- ⚙️ Utility Methods ---

    /**
     * Generates a descriptive string representation of the data.
     * * @return A formatted summary containing identity and spatial data.
     */
    @Override
    public String toString() {
        try {
            // Using objects directly to utilize their respective toString implementations.
            return String.format("ControlPointData[name=%s, max=%d, pos=%s, biome=%s]",
                    name(), max, pos, biome);
        } catch (Exception e) {
            return "ControlPointData{Error:" + e.getMessage() + "}";
        }
    }
}