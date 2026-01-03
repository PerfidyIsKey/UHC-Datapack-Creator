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
 * <b>Strict Policy:</b> This class follows a "fail-fast" mechanism. If configuration is
 * missing or a session is initialized incorrectly, it throws an exception immediately
 * to prevent the generation of a broken datapack.
 * </p>
 */
public final class ControlPointRegistry {

    // --- 🏛️ Data Fields ---

    /** * The internal pool of all registered control points, indexed by their unique integer ID.
     * This represents the complete set of valid locations defined in the application source.
     */
    private final Map<Integer, ControlPointData> registry;

    /** * The specific subset of control points selected for the current active game session.
     * Stored statically to provide a global access point for datapack generators.
     */
    private static final List<ControlPointData> ACTIVE_CONTROL_POINTS = new ArrayList<>();

    /** * The random number generator used for shuffling the master pool to ensure
     * objective variety across different game sessions.
     */
    private final Random random;

    // --- 🏗️ Constructor ---

    /**
     * Constructs the registry and populates the master pool with default objectives.
     * <p>
     * <b>Note:</b> This constructor initializes the master pool but does not start a session.
     * The {@code initializeSession()} method must be called explicitly from the entry point.
     * </p>
     * @throws IllegalStateException if registration of default points fails due to logic errors.
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

        } catch (Exception e) {
            throw new IllegalStateException("Registry Critical Failure: Could not populate the master control point pool. " + e.getMessage(), e);
        }
    }

    // --- 🚀 Session Management ---

    /**
     * Selects a randomized subset of points from the pool and initializes their UI components.
     * <p>
     * This transitions the registry from a dormant state to an "Active Session".
     * It generates Bossbar metadata for each selected point based on its world coordinates.
     * </p>
     * @throws IllegalStateException if a session is already active or if the randomized selection is empty.
     * @throws RuntimeException if Bossbar assignment fails due to underlying builder errors.
     */
    public void initializeSession() {
        // Guard: Ensure we do not overwrite an active session
        if (!ACTIVE_CONTROL_POINTS.isEmpty()) {
            throw new IllegalStateException("Session Error: Cannot re-initialize session. Active points already exist. " +
                    "Clear the current session before attempting a re-initialization.");
        }

        // Retrieve selection based on global configuration
        List<ControlPointData> selection = getRandom(ControlPointConfig.AMOUNT);

        if (selection.isEmpty()) {
            throw new IllegalStateException("Session Error: Randomized selection returned zero points. " +
                    "Ensure ControlPointConfig.AMOUNT is > 0 and the registry pool is not empty.");
        }

        try {
            for (int i = 0; i < selection.size(); i++) {
                ControlPointData cp = selection.get(i);

                // UI Labeling: "CP[Number]: [X, Y, Z] ([Biome])"
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
            throw new RuntimeException("Session Error: Critical failure during Bossbar metadata generation: " + e.getMessage(), e);
        }
    }

    /**
     * Resets the active session, clearing all selected points and their associated Bossbar data.
     * <p>
     * Essential for unit testing or multi-stage generation where a fresh state is required.
     * </p>
     */
    public static void clearSession() {
        ACTIVE_CONTROL_POINTS.clear();
    }

    /**
     * Retrieves the unmodifiable list of objectives active for the current session.
     * @return An unmodifiable view of the active {@link ControlPointData} list.
     * @throws IllegalStateException if the session has not been initialized.
     */
    public static List<ControlPointData> getActivePoints() {
        if (ACTIVE_CONTROL_POINTS.isEmpty()) {
            throw new IllegalStateException("Access Error: Request for active points denied. initializeSession() must be executed first.");
        }
        return Collections.unmodifiableList(ACTIVE_CONTROL_POINTS);
    }

    // --- 🧪 Pool Registration & Logic ---

    /**
     * Registers a new Control Point objective into the master registry pool.
     * @param id    The unique numerical ID for this point.
     * @param max   The point cap required to fully capture the objective.
     * @param rate  The progression increment added per game tick.
     * @param pos   The physical location in the world (must be non-null).
     * @param biome The biome identifier for context (must be non-null).
     * @return The newly instantiated {@link ControlPointData} object.
     * @throws NullPointerException if {@code pos} or {@code biome} is null.
     * @throws IllegalArgumentException if the provided ID is already registered in the pool.
     */
    public ControlPointData register(int id, int max, int rate, BlockPos pos, BiomeId biome) {
        Objects.requireNonNull(pos, "Registration Error [ID " + id + "]: BlockPos is required and cannot be null.");
        Objects.requireNonNull(biome, "Registration Error [ID " + id + "]: BiomeId is required and cannot be null.");

        if (this.registry.containsKey(id)) {
            throw new IllegalArgumentException("Registration Error: The Control Point ID " + id + " is already present in the registry.");
        }

        ControlPointData data = new ControlPointData(id, max, rate, pos, biome);
        this.registry.put(id, data);
        return data;
    }

    /**
     * Shuffles the registry and returns a specific count of objectives.
     * @param count The number of points to select for the session.
     * @return A randomized {@link List} containing the selected objectives.
     * @throws IllegalArgumentException if the requested count exceeds the available registry size.
     */
    public List<ControlPointData> getRandom(int count) {
        if (count > this.registry.size()) {
            throw new IllegalArgumentException(String.format("Selection Error: Registry size is %d, but %d points were requested. " +
                    "Check your ControlPointConfig settings.", this.registry.size(), count));
        }

        List<ControlPointData> allPoints = new ArrayList<>(this.registry.values());
        Collections.shuffle(allPoints, this.random);

        return new ArrayList<>(allPoints.subList(0, count));
    }

    // --- 🔍 Accessors ---

    /**
     * Retrieves a specific point from the master pool by its unique ID.
     * @param id The ID to search for.
     * @return The found {@link ControlPointData}.
     * @throws NoSuchElementException if the ID is not registered in the pool.
     */
    public ControlPointData getById(int id) {
        ControlPointData data = this.registry.get(id);
        if (data == null) {
            throw new NoSuchElementException("Access Error: No control point is registered with the ID " + id);
        }
        return data;
    }

    /** * Returns all objectives currently registered in the master pool.
     * @return An unmodifiable view of all registered points.
     */
    public List<ControlPointData> getAll() {
        return List.copyOf(this.registry.values());
    }

    /** * Returns the total capacity of the master pool.
     * @return The number of objectives registered.
     */
    public int size() {
        return this.registry.size();
    }
}