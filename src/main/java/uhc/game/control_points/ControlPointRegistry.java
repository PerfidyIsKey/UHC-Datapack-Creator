package uhc.game.control_points;

import uhc.arguments.block.BlockPos;
import uhc.game.bossbar.BossbarData;
import uhc.resource.bossbar.BossbarId;
import uhc.resource.bossbar.BossbarStyle;
import uhc.resource.world.BiomeId;
import uhc.text.TextComponent;

import java.util.*;

/**
 * 🏢 **Control Point Registry**
 * <p>
 * This class serves as the central authority for all capture objectives. It maintains
 * a master pool of all possible points and handles the randomization and UI initialization
 * (Bossbars) for the current game session.
 * </p>
 * <p>
 * <b>Strict Policy:</b> No fallback coordinates or dummy objects are provided. If
 * configuration is missing or invalid, the class will fail-fast with a clear error
 * to ensure game integrity.
 * </p>
 */
public final class ControlPointRegistry {

    // --- 🏛️ Data Fields ---

    /** * The internal pool of all registered control points, indexed by their unique integer ID. */
    private final Map<Integer, ControlPointData> registry;

    /** * The specific subset of control points selected for the current active game session. */
    private static final List<ControlPointData> ACTIVE_CONTROL_POINTS = new ArrayList<>();

    /** * The random number generator used for shuffling the objective selection. */
    private final Random random;

    // --- 🏗️ Constructor ---

    /**
     * Constructs the registry and populates the master pool with default objectives.
     * <p>
     * After registration, it automatically attempts to initialize the session based on
     * global configuration.
     * </p>
     * @throws IllegalStateException if registration of default points or session initialization fails.
     */
    public ControlPointRegistry() {
        this.registry = new HashMap<>();
        this.random = new Random();

        try {
            // --- Default Pool Population ---
            register(0, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(207, 64, -191), BiomeId.SWAMP);
            register(1, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-70, 92, -408), BiomeId.WINDSWEPT_GRAVELLY_HILLS);
            register(2, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-190, 72, -134), BiomeId.TAIGA);
            register(3, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(344, 63, 280), BiomeId.RIVER);
            register(4, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-255, 93, -107), BiomeId.OLD_GROWTH_PINE_TAIGA);
            register(5, ControlPointConfig.MAX_SCORE, ControlPointConfig.ADD_RATE, BlockPos.absolute(-185, 72, 296), BiomeId.OLD_GROWTH_SPRUCE_TAIGA);

            // Auto-trigger session setup
            initializeSession();
        } catch (Exception e) {
            throw new IllegalStateException("Registry Critical Failure: Could not finalize default setup. " + e.getMessage(), e);
        }
    }

    // --- 🚀 Session Management ---

    /**
     * Selects a randomized subset of points from the pool and initializes their Bossbars.
     * <p>
     * <b>Constraints:</b>
     * <ul>
     * <li>Cannot be called if a session is already active.</li>
     * <li>Requires a non-zero amount defined in {@link ControlPointConfig}.</li>
     * </ul>
     * </p>
     * @throws IllegalStateException if selection fails or the session is already initialized.
     */
    public void initializeSession() {
        // Guard: Prevent re-shuffling while a game is active
        if (!ACTIVE_CONTROL_POINTS.isEmpty()) {
            throw new IllegalStateException("Session Error: Cannot re-initialize session. Active points already exist.");
        }

        List<ControlPointData> selection = getRandom(ControlPointConfig.AMOUNT);

        if (selection.isEmpty()) {
            throw new IllegalStateException("Session Error: Randomized selection returned zero points. Check pool size or config.");
        }

        try {
            for (int i = 0; i < selection.size(); i++) {
                ControlPointData cp = selection.get(i);

                // UI Display Name: "CP1: [X, Y, Z] (Biome)"
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

            ACTIVE_CONTROL_POINTS.addAll(selection);
        } catch (Exception e) {
            throw new RuntimeException("Session Error: Failed to assign Bossbars to selected points: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the unmodifiable list of objectives active for the current session.
     * @return Immutable {@link List} of {@link ControlPointData}.
     * @throws IllegalStateException if the session has not been initialized yet.
     */
    public static List<ControlPointData> getActivePoints() {
        if (ACTIVE_CONTROL_POINTS.isEmpty()) {
            throw new IllegalStateException("Access Error: No active control points found. initializeSession() must be called first.");
        }
        return List.copyOf(ACTIVE_CONTROL_POINTS);
    }

    // --- 🧪 Pool Registration & Logic ---

    /**
     * Registers a new Control Point into the master pool.
     * @param id    The unique numerical ID.
     * @param max   The point cap for capture.
     * @param rate  How many points are added per tick.
     * @param pos   The non-null {@link BlockPos} location.
     * @param biome The non-null {@link BiomeId} context.
     * @return The newly created {@link ControlPointData}.
     * @throws NullPointerException if pos or biome is null.
     * @throws IllegalArgumentException if the ID is already registered.
     */
    public ControlPointData register(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        Objects.requireNonNull(pos, "Registration Error [ID " + id + "]: BlockPos cannot be null.");
        Objects.requireNonNull(biome, "Registration Error [ID " + id + "]: BiomeId cannot be null.");

        if (this.registry.containsKey(id)) {
            throw new IllegalArgumentException("Registration Error: Control Point ID " + id + " is already in use.");
        }

        ControlPointData data = new ControlPointData(id, max, rate, pos, biome);
        this.registry.put(id, data);
        return data;
    }

    /**
     * Returns a random subset of points from the pool.
     * @param count The number of points to select.
     * @return A randomized {@link List} of selected objectives.
     * @throws IllegalArgumentException if count is higher than available registry size.
     */
    public List<ControlPointData> getRandom(int count) {
        if (count > this.registry.size()) {
            throw new IllegalArgumentException(String.format("Selection Error: Registry only contains %d points, but %d were requested.", this.registry.size(), count));
        }

        List<ControlPointData> allPoints = new ArrayList<>(this.registry.values());
        Collections.shuffle(allPoints, this.random);

        return new ArrayList<>(allPoints.subList(0, count));
    }

    // --- 🔍 Accessors ---

    /**
     * Retrieves a point by ID from the registry pool.
     * @param id The ID to search for.
     * @return The found {@link ControlPointData}.
     * @throws NoSuchElementException if the ID does not exist.
     */
    public ControlPointData getById(int id) {
        ControlPointData data = this.registry.get(id);
        if (data == null) {
            throw new NoSuchElementException("Access Error: No control point registered with ID " + id);
        }
        return data;
    }

    /** @return An unmodifiable view of every registered objective. */
    public List<ControlPointData> getAll() {
        return List.copyOf(this.registry.values());
    }

    /** @return The total number of objectives available in the pool. */
    public int size() {
        return this.registry.size();
    }
}