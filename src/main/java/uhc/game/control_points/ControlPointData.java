package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.game.bossbar.BossbarData;
import uhc.resource.world.BiomeId;
import uhc.resource.world.DimensionId;

import java.util.Objects;
import java.util.Optional;

/**
 * 🚩 **Control Point Data**
 * <p>
 * An immutable data model representing a capture-based objective within the game world.
 * This class encapsulates scoring parameters, physical coordinates, and environmental
 * context required for game-loop processing.
 * </p>
 * <p>
 * While the core identity and spatial fields are final, it supports an optional
 * {@link BossbarData} component that can be attached after instantiation.
 * </p>
 */
public final class ControlPointData {

    // --- 🔑 Identity & Scoring Properties ---

    /** * The unique numerical identifier for this specific objective instance. */
    private final int id;

    /** * The maximum progress score required to successfully achieve a 100% capture. */
    private final int max;

    /** * The numerical increment added to the progress score per occupancy interval. */
    private final int rate;

    // --- 📍 Spatial & Environmental Context ---

    /** * The physical center coordinates of the control point in the Minecraft world. */
    private final BlockPos pos;

    /** * The biome identifier at the point's location, used for specialized logic or effects. */
    private final BiomeId biome;

    /** * The dimension (Overworld, Nether, or End) where this objective is located.
     * <p>This is resolved automatically from the provided {@link BiomeId}.</p>
     */
    private final DimensionId dimension;

    // --- 📊 Optional Visuals ---

    /** * The optional boss bar configuration used to display capture progress to players.
     * <p>This field is non-final to allow for late-binding initialization.</p>
     */
    private BossbarData bossbar;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new validated instance of {@code ControlPointData}.
     * <p>Derives the dimension from the biome and ensures all mandatory spatial data is present.</p>
     *
     * @param id    The unique objective ID.
     * @param max   The progress threshold for capture.
     * @param rate  The progress speed.
     * @param pos   The non-null {@link BlockPos} location.
     * @param biome The non-null {@link BiomeId} environment.
     * @throws IllegalArgumentException if null arguments are passed or dimension resolution fails.
     */
    public ControlPointData(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        try {
            this.id = id;
            this.max = max;
            this.rate = rate;

            this.pos = Objects.requireNonNull(pos, "Spatial position (BlockPos) cannot be null.");
            this.biome = Objects.requireNonNull(biome, "Environmental context (BiomeId) cannot be null.");

            // Derive dimension context from the biome
            this.dimension = biome.getDimension();

            if (this.dimension == null) {
                throw new IllegalStateException("DimensionId could not be resolved from BiomeId: " + biome);
            }
        } catch (Exception e) {
            // Error Catching: Wrap potential NPEs or StateExceptions for cleaner engine logs
            throw new IllegalArgumentException("ControlPointData initialization failed: " + e.getMessage());
        }
    }

    // --- 📝 Identity Logic ---

    /**
     * Generates the internal technical name used for command identifiers and tags.
     * @return The string "cp" followed by the numerical ID (e.g., "cp1").
     */
    public String name() {
        try {
            return "cp" + id;
        } catch (Exception e) {
            return "cp_unknown";
        }
    }

    // --- 🔍 Accessors & Mutators ---

    /** * Associates a {@link BossbarData} configuration with this control point.
     * @param bossbar The configuration for the visual progress bar.
     */
    public void bossbar(BossbarData bossbar) {
        try {
            // Ensuring we don't accidentally set a null reference that would break getters
            this.bossbar = Objects.requireNonNull(bossbar, "BossbarData cannot be null.");
        } catch (Exception e) {
            // Error Catching: Maintains stability if a null bar is passed
        }
    }

    /** * Retrieves the optional boss bar configuration.
     * @return An {@link Optional} containing the boss bar data if set, otherwise empty.
     */
    public Optional<BossbarData> getBossbar() {
        try {
            return Optional.ofNullable(bossbar);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /** @return The unique instance ID. */
    public int getId() { return id; }

    /** @return The maximum capture score limit. */
    public int getMax() { return max; }

    /** @return The capture progress accumulation rate. */
    public int getRate() { return rate; }

    /** @return The physical {@link BlockPos} coordinates. */
    public BlockPos getPos() { return pos; }

    /** @return The {@link BiomeId} environment. */
    public BiomeId getBiome() { return biome; }

    /** @return The resolved {@link DimensionId}. */
    public DimensionId getDimension() { return dimension; }

    // --- ⚙️ Utility Overrides ---

    /**
     * Provides a formatted string representation of the data model.
     * @return A string containing ID, Name, Dimension, and Bossbar status.
     */
    @Override
    public String toString() {
        try {
            return String.format("ControlPointData[id=%d, name=%s, dimension=%s, hasBossbar=%b]",
                    id, name(), dimension, (bossbar != null));
        } catch (Exception e) {
            // Error Catching: Ensures logging never triggers a crash
            return "ControlPointData{Error during serialization}";
        }
    }
}