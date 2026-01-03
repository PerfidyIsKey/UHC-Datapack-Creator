package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.game.bossbar.BossbarData;
import uhc.resource.tag.EntityTag;
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
 * While core identity and spatial fields are final, it supports a {@link BossbarData}
 * component that is expected to be assigned during session initialization.
 * </p>
 */
public final class ControlPointData {

    // --- 🔑 Identity & Scoring Properties ---

    /** * The unique numerical identifier for this specific objective instance.
     */
    private final int id;

    /** * The maximum progress score required to successfully achieve a 100% capture.
     */
    private final int max;

    /** * The numerical increment added to the progress score per occupancy interval.
     */
    private final int rate;

    // --- 📍 Spatial & Environmental Context ---

    /** * The physical center coordinates of the control point in the Minecraft world.
     */
    private final BlockPos pos;

    /** * The biome identifier at the point's location, used for environmental context.
     */
    private final BiomeId biome;

    /** * The dimension (Overworld, Nether, or End) where this objective is located.
     * <p>Resolved automatically from the {@link BiomeId} during construction.</p>
     */
    private final DimensionId dimension;

    // --- 📊 Visuals ---

    /** * The boss bar configuration used to display capture progress to players.
     * <p>This field is assigned after construction via {@link #bossbar(BossbarData)}.</p>
     */
    private BossbarData bossbar;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new validated instance of {@code ControlPointData}.
     * * @param id    The unique objective ID.
     * @param max   The progress threshold for capture.
     * @param rate  The progress speed.
     * @param pos   The non-null {@link BlockPos} location.
     * @param biome The non-null {@link BiomeId} environment.
     * @throws IllegalArgumentException if validation fails or dimension resolution is impossible.
     */
    public ControlPointData(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        try {
            this.id = id;
            this.max = max;
            this.rate = rate;

            // Strict Validation: No fallbacks allowed
            this.pos = Objects.requireNonNull(pos, "ControlPointData creation failed: BlockPos must not be null.");
            this.biome = Objects.requireNonNull(biome, "ControlPointData creation failed: BiomeId must not be null.");

            // Resolve dimension context
            this.dimension = biome.getDimension();

            if (this.dimension == null) {
                throw new IllegalStateException("ControlPointData creation failed: DimensionId could not be resolved from biome: " + biome.getDimension());
            }
        } catch (NullPointerException | IllegalStateException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during ControlPointData construction: " + e.getMessage(), e);
        }
    }

    // --- 📝 Identity Logic ---

    /**
     * Generates the internal technical name used for command identifiers.
     * * @return The string "cp" followed by the numerical ID.
     * @throws IllegalStateException if the ID is negative (unexpected state).
     */
    public EntityTag name() {
        if (this.id < 0) {
            throw new IllegalStateException("Cannot generate name: Control Point ID is negative (" + this.id + ")");
        }
        return EntityTag.indexed(EntityTag.CP, id);
    }

    // --- 🔍 Accessors & Mutators ---

    /** * Associates a {@link BossbarData} configuration with this control point.
     * * @param bossbar The configuration for the visual progress bar.
     * @throws NullPointerException if the provided bossbar is null.
     */
    public void bossbar(BossbarData bossbar) {
        this.bossbar = Objects.requireNonNull(bossbar, "Assignment failed: BossbarData for " + name() + " cannot be null.");
    }

    /** * Retrieves the assigned boss bar configuration.
     * * @return The assigned {@link BossbarData}.
     * @throws IllegalStateException if accessed before the bossbar has been initialized.
     */
    public BossbarData getBossbar() {
        if (this.bossbar == null) {
            throw new IllegalStateException("Access failed: Bossbar has not been initialized for Control Point: " + name());
        }
        return this.bossbar;
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
     * Provides a detailed string representation of the data model for debugging.
     * * @return A formatted string of the object's current state.
     * @throws RuntimeException if serialization fails.
     */
    @Override
    public String toString() {
        try {
            return String.format("ControlPointData[id=%d, name=%s, dimension=%s, assignedBossbar=%b]",
                    id, name(), dimension, (bossbar != null));
        } catch (Exception e) {
            throw new RuntimeException("ControlPointData serialization failed: " + e.getMessage());
        }
    }
}