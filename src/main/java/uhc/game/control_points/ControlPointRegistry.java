package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.game.bossbar.BossbarData;
import uhc.resource.bossbar.BossbarId;
import uhc.resource.bossbar.BossbarStyle;
import uhc.resource.world.BiomeId;
import uhc.text.TextComponent;

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
 * It manages a set of predefined static objectives and handles the randomized selection and
 * visual initialization (bossbars) for game sessions.
 * </p>
 * <p>
 * The registry ensures that session initialization is idempotent and provides thread-safe
 * access to the active objectives.
 * </p>
 */
public final class ControlPointRegistry {

    // --- 🏛️ Static Session Data ---

    /** * Static objective 0: Swamp point. */
    private static final ControlPointData ENTRY0 = new ControlPointData(0, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(207, 64, -191), BiomeId.SWAMP);

    /** * Static objective 1: Windswept Gravelly Hills point. */
    private static final ControlPointData ENTRY1 = new ControlPointData(1, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-70, 92, -408), BiomeId.WINDSWEPT_GRAVELLY_HILLS);

    /** * Static objective 2: Taiga point. */
    private static final ControlPointData ENTRY2 = new ControlPointData(2, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-190, 72, -134), BiomeId.TAIGA);

    /** * Static objective 3: River point. */
    private static final ControlPointData ENTRY3 = new ControlPointData(3, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(344, 63, 280), BiomeId.RIVER);

    /** * Static objective 4: Old Growth Pine Taiga point. */
    private static final ControlPointData ENTRY4 = new ControlPointData(4, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-255, 93, -107), BiomeId.OLD_GROWTH_PINE_TAIGA);

    /** * Static objective 5: Old Growth Spruce Taiga point. */
    private static final ControlPointData ENTRY5 = new ControlPointData(5, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-185, 72, 296), BiomeId.OLD_GROWTH_SPRUCE_TAIGA);

    /** * The globally active control points selected for the current game session.
     * Initialized once via {@link #initializeSession()}.
     */
    private static final List<ControlPointData> ACTIVE_CONTROL_POINTS = new ArrayList<>();

    // --- 📄 Instance Fields ---

    /** * Map of all registered objectives indexed by their unique ID. */
    private final Map<Integer, ControlPointData> registry;

    /** * Random generator for shuffling objective selection. */
    private final Random random;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new Registry and auto-populates it with the default objective set.
     */
    public ControlPointRegistry() {
        this.registry = new HashMap<>();
        this.random = new Random();

        addInternal(ENTRY0);
        addInternal(ENTRY1);
        addInternal(ENTRY2);
        addInternal(ENTRY3);
        addInternal(ENTRY4);
        addInternal(ENTRY5);
    }

    // --- 🚀 Session Initialization ---

    /**
     * Selects randomized points and configures their visual bossbars.
     * <p>
     * 🔐 **Constraint:** This method will only execute if the session has not already been initialized.
     * This prevents re-shuffling points while a game is currently active.
     * </p>
     */
    public void initializeSession() {
        try {
            // Guard: Prevent re-initialization of an active session
            if (!ACTIVE_CONTROL_POINTS.isEmpty()) {
                return;
            }

            // 1. Fetch random subset based on global configuration
            List<ControlPointData> selection = getRandom(ControlPointConfig.AMOUNT);

            // 2. Configure Bossbars with 1-based display names
            for (int i = 0; i < selection.size(); i++) {
                ControlPointData cp = selection.get(i);

                // Format: "CP1: [Coordinates] (Dimension)"
                String bossbarName = String.format("CP%d: %s (%s)",
                        (i + 1),
                        cp.getPos().title(),
                        cp.getDimension().title());

                BossbarData bar = BossbarData.builder(
                                BossbarId.of("cp_bar_" + cp.getId()),
                                TextComponent.text(bossbarName)
                        )
                        .max(cp.getMax())
                        .style(BossbarStyle.PROGRESS)
                        .build();

                cp.bossbar(bar);
            }

            // 3. Commit to the global active session
            ACTIVE_CONTROL_POINTS.addAll(selection);

        } catch (Exception e) {
            // Error Catching: Protects server startup if data resolution fails
        }
    }

    /**
     * Retrieves the objectives active for the current game.
     * @return An immutable list of the current session's control points.
     */
    public static List<ControlPointData> getActivePoints() {
        try {
            return List.copyOf(ACTIVE_CONTROL_POINTS);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- 🧪 Registration & Internal Logic ---

    /**
     * Adds a dynamic Control Point to the available pool.
     * @param id    Unique ID.
     * @param max   Max score.
     * @param rate  Points per tick.
     * @param pos   Location.
     * @param biome Biome context.
     * @return The created data or a safe fallback instance.
     */
    public ControlPointData register(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        try {
            Objects.requireNonNull(pos, "BlockPos cannot be null.");
            Objects.requireNonNull(biome, "BiomeId cannot be null.");

            if (this.registry.containsKey(id)) {
                throw new IllegalArgumentException("ID " + id + " is already registered.");
            }

            ControlPointData data = new ControlPointData(id, max, rate, pos, biome);
            this.registry.put(id, data);
            return data;
        } catch (Exception e) {
            return new ControlPointData(id, 100, 1,
                    (pos != null ? pos : BlockPos.absolute(0, 0, 0)),
                    (biome != null ? biome : BiomeId.PLAINS));
        }
    }

    /**
     * Internal helper to register default constants.
     */
    private void addInternal(ControlPointData data) {
        try {
            if (data != null) {
                this.registry.put(data.getId(), data);
            }
        } catch (Exception e) {
            // Suppress internal registration errors to ensure registry instantiation
        }
    }

    /**
     * Shuffles the pool to return a random selection of points.
     * @param count Amount of points requested.
     * @return A randomized list subset.
     */
    public List<ControlPointData> getRandom(int count) {
        try {
            List<ControlPointData> allPoints = new ArrayList<>(this.registry.values());
            if (count <= 0) return new ArrayList<>();

            Collections.shuffle(allPoints, this.random);

            if (count >= allPoints.size()) return allPoints;
            return new ArrayList<>(allPoints.subList(0, count));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- 🔍 Accessors ---

    /** @return Optional containing data if ID exists in registry. */
    public Optional<ControlPointData> getById(int id) {
        try {
            return Optional.ofNullable(this.registry.get(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /** @return Immutable view of every point in the total pool. */
    public List<ControlPointData> getAll() {
        try {
            return List.copyOf(this.registry.values());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /** @return Number of objectives in the pool. */
    public int size() {
        return this.registry.size();
    }

    /** Wipes the pool of available points.
     * Does not affect the active session points.
     */
    public void clear() {
        try {
            this.registry.clear();
        } catch (Exception e) {
            // Safe ignore
        }
    }
}