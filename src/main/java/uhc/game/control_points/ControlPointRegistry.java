package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.resource.world.BiomeId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * 🏢 **Control Point Registry**
 * <p>
 * This class serves as the central authority for managing and accessing {@link ControlPointData} instances.
 * It manages a set of predefined (private) static objectives and allows for the dynamic registration
 * of new points.
 * </p>
 * <p>
 * Access is strictly controlled through identifiers and randomized selection logic to ensure
 * game-loop integrity and balanced objective distribution.
 * </p>
 */
public final class ControlPointRegistry {

    // --- 🏛️ Private Static Constants (Predefined Objectives) ---

    /** * Static objective entry 0: Swamp point at absolute coordinates. */
    private static final ControlPointData ENTRY0 = new ControlPointData(
            0, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(207, 64, -191), BiomeId.SWAMP
    );

    /** * Static objective entry 1: Windswept Gravelly Hills point at absolute coordinates. */
    private static final ControlPointData ENTRY1 = new ControlPointData(
            1, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-70, 92, -408), BiomeId.WINDSWEPT_GRAVELLY_HILLS
    );

    /** * Static objective entry 2: Taiga point at absolute coordinates. */
    private static final ControlPointData ENTRY2 = new ControlPointData(
            2, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-190, 72, -134), BiomeId.TAIGA
    );

    /** * Static objective entry 3: River point at absolute coordinates. */
    private static final ControlPointData ENTRY3 = new ControlPointData(
            3, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(344, 63, 280), BiomeId.RIVER
    );

    /** * Static objective entry 4: Old Growth Pine Taiga point at absolute coordinates. */
    private static final ControlPointData ENTRY4 = new ControlPointData(
            4, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-255, 93, -107), BiomeId.OLD_GROWTH_PINE_TAIGA
    );

    /** * Static objective entry 5: Old Growth Spruce Taiga point at absolute coordinates. */
    private static final ControlPointData ENTRY5 = new ControlPointData(
            5, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-185, 72, 296), BiomeId.OLD_GROWTH_SPRUCE_TAIGA
    );

    // --- 📄 Fields ---

    /** * The internal mapping of numerical identifiers to their respective {@link ControlPointData}.
     * Encapsulated to prevent external modification of the registry state.
     */
    private final Map<Integer, ControlPointData> registry;

    /** * A {@link Random} instance used for generating randomized subsets of control points
     * in {@link #getRandom(int)}.
     */
    private final Random random;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new ControlPointRegistry instance.
     * <p>Initializes the internal storage and automatically registers the predefined
     * static objectives into the local registry session.</p>
     */
    public ControlPointRegistry() {
        this.registry = new HashMap<>();
        this.random = new Random();

        // Populate the instance registry with global defaults
        addInternal(ENTRY0);
        addInternal(ENTRY1);
        addInternal(ENTRY2);
        addInternal(ENTRY3);
        addInternal(ENTRY4);
        addInternal(ENTRY5);
    }

    // --- 🚀 Registration Logic ---

    /**
     * Registers a new dynamic Control Point into the system.
     * <p>The {@link uhc.resource.world.DimensionId} is automatically resolved via the provided Biome.</p>
     *
     * @param id    The unique numerical identifier for the point.
     * @param max   The capture score threshold.
     * @param rate  The amount of score added per occupancy interval.
     * @param pos   The {@link BlockPos} location of the point.
     * @param biome The {@link BiomeId} which defines the environment and dimension.
     * @return The successfully registered {@link ControlPointData} instance, or a fallback object if registration fails.
     */
    public ControlPointData register(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        try {
            Objects.requireNonNull(pos, "BlockPos parameter must not be null.");
            Objects.requireNonNull(biome, "BiomeId parameter must not be null.");

            if (this.registry.containsKey(id)) {
                throw new IllegalArgumentException("Control Point ID " + id + " is already occupied in the registry.");
            }

            ControlPointData data = new ControlPointData(id, max, rate, pos, biome);
            this.registry.put(id, data);
            return data;
        } catch (Exception e) {
            // Error Catching: Ensure a non-null fallback to maintain system stability
            return new ControlPointData(id, 100, 1, (pos != null ? pos : BlockPos.absolute(0, 0, 0)), BiomeId.PLAINS);
        }
    }

    /**
     * Internal helper method to facilitate the registration of static constants
     * during object construction.
     * * @param data The {@link ControlPointData} to insert.
     */
    private void addInternal(ControlPointData data) {
        try {
            if (data != null) {
                this.registry.put(data.getId(), data);
            }
        } catch (Exception e) {
            // Error Catching: Prevent constructor failure if map insertion fails
        }
    }

    // --- 🔍 Public Retrieval Methods ---

    /**
     * Queries the registry for a specific control point by its ID.
     *
     * @param id The numerical identifier of the objective.
     * @return An {@link Optional} containing the objective data if found, otherwise empty.
     */
    public Optional<ControlPointData> getById(int id) {
        try {
            return Optional.ofNullable(this.registry.get(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves an immutable view of all registered control points.
     *
     * @return A {@link List} containing all currently active {@link ControlPointData} objects.
     */
    public List<ControlPointData> getAll() {
        try {
            return List.copyOf(this.registry.values());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Selects a randomized subset of registered control points.
     * <p>If the requested count exceeds the available points, all points are returned in a shuffled order.</p>
     *
     * @param count The number of objectives to retrieve.
     * @return A shuffled {@link List} of {@link ControlPointData} instances.
     */
    public List<ControlPointData> getRandom(int count) {
        try {
            List<ControlPointData> allPoints = new ArrayList<>(this.registry.values());

            if (count <= 0) return new ArrayList<>();

            Collections.shuffle(allPoints, this.random);

            if (count >= allPoints.size()) {
                return allPoints;
            }

            return allPoints.subList(0, count);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- ⚙️ Utility Methods ---

    /**
     * Determines the current population size of the registry.
     *
     * @return The total number of objectives (static and dynamic) managed by this instance.
     */
    public int size() {
        try {
            return this.registry.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Wipes all data from the active registry instance.
     * <p>Note: This will also remove the internalized static objectives for this specific instance.</p>
     */
    public void clear() {
        try {
            this.registry.clear();
        } catch (Exception e) {
            // Error Catching: Silently handle potential map clear failures
        }
    }
}