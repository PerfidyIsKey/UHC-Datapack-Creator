package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.resource.world.BiomeId;
import uhc.resource.world.DimensionId;

import java.util.Objects;

/**
 * 🚩 **Control Point Data**
 * <p>
 * An immutable data model representing a capture-based objective within the game world.
 * This class encapsulates scoring parameters, physical coordinates, and environmental
 * context required for game-loop processing.
 * </p>
 * <p>
 * To ensure environmental consistency, this model automatically derives the
 * {@link DimensionId} from the provided {@link BiomeId} during instantiation.
 * </p>
 */
public final class ControlPointData {

    // --- 🔑 Identity & Scoring Properties ---

    /** * The unique numerical identifier for this specific objective.
     */
    private final int id;

    /** * The maximum progress score required to successfully capture this point.
     */
    private final int max;

    /** * The amount of progress score contributed per occupancy interval.
     */
    private final int rate;

    // --- 📍 Spatial & Environmental Context ---

    /** * The physical center location of the control point in the Minecraft world.
     */
    private final BlockPos pos;

    /** * The biome associated with the point's location, used for environmental effects.
     */
    private final BiomeId biome;

    /** * The dimension (Overworld, Nether, etc.) where this point resides.
     * <p>This is resolved automatically via {@link BiomeId#getDimension()}.</p>
     */
    private final DimensionId dimension;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new validated instance of {@code ControlPointData}.
     * <p>Validation ensures that spatial and environmental data are non-null and that
     * the dimension is correctly mapped from the biome.</p>
     *
     * @param id    The unique ID of the point.
     * @param max   The total score required for capture.
     * @param rate  The score increment granted to occupants per interval.
     * @param pos   The physical {@link BlockPos}. Cannot be null.
     * @param biome The {@link BiomeId} which dictates the dimension. Cannot be null.
     * @throws IllegalArgumentException if validation fails or parameters are null.
     */
    public ControlPointData(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        try {
            this.id = id;
            this.max = max;
            this.rate = rate;

            // Strict validation of mandatory spatial objects.
            this.pos = Objects.requireNonNull(pos, "Spatial position (BlockPos) cannot be null.");
            this.biome = Objects.requireNonNull(biome, "Environmental context (BiomeId) cannot be null.");

            // Automated resolution of dimension through biome association.
            this.dimension = biome.getDimension();

            if (this.dimension == null) {
                throw new IllegalStateException("Dimension could not be resolved from biome: " + biome.name());
            }
        } catch (Exception e) {
            // Error Catching: Wrap and rethrow to provide clear feedback during registry population.
            throw new IllegalArgumentException("Initialization failed for ControlPointData: " + e.getMessage());
        }
    }

    // --- 📝 Identity Logic ---

    /**
     * Provides the standardized internal identifier string for the control point.
     * * @return The string {@code "cp"} appended with the numerical ID (e.g., "cp5").
     */
    public String name() {
        try {
            return "cp" + id;
        } catch (Exception e) {
            // Fallback to prevent string concatenation failures in logs.
            return "cp_unknown";
        }
    }

    // --- 🔍 Accessors ---

    /** * @return The unique numerical identifier.
     */
    public int getId() {
        return id;
    }

    /** * @return The capture score ceiling required for a successful objective completion.
     */
    public int getMax() {
        return max;
    }

    /** * @return The progress accumulation rate per occupancy tick/interval.
     */
    public int getRate() {
        return rate;
    }

    /** * @return The physical {@link BlockPos} of the objective.
     */
    public BlockPos getPos() {
        return pos;
    }

    /** * @return The {@link BiomeId} environment assigned to this point.
     */
    public BiomeId getBiome() {
        return biome;
    }

    /** * @return The {@link DimensionId} resolved during construction.
     */
    public DimensionId getDimension() {
        return dimension;
    }

    // --- ⚙️ Utility Methods ---

    /**
     * Generates a descriptive summary of the control point data.
     * * @return A formatted string containing identity, dimension, and position.
     */
    @Override
    public String toString() {
        try {
            // Using internal getters to ensure safe access during string formation.
            return String.format("ControlPointData[id=%d, name=%s, dimension=%s, pos=%s]",
                    getId(), name(), getDimension(), getPos());
        } catch (Exception e) {
            // Error Catching: Prevent logging from crashing during debug sessions.
            return "ControlPointData{Error: " + e.getMessage() + "}";
        }
    }
}