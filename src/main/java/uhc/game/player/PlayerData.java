package uhc.game.player;

import java.util.Objects;

/**
 * 👤 **Player Data Model**
 * <p>
 * This class serves as a robust data container for player-specific information
 * within the UHC system. It maintains identity, competitive statistics, and
 * active session states.
 * </p>
 * <p>
 * <b>Strict Validation:</b> This class enforces a no-null policy for all Object-based
 * fields (Integer, String, Boolean). Any attempt to initialize or modify these
 * fields with null values will result in an immediate exception.
 * </p>
 */
public class PlayerData {

    // --- 🆔 Identity Fields ---

    /** * The unique numerical identifier for the player.
     * <p>Primary key used for database or registry lookups.</p>
     */
    private Integer id;

    /** * The Minecraft username of the player.
     * <p>Stored as a String to match the Mojang profile name.</p>
     */
    private String name;

    // --- 📈 Competitive Fields ---

    /** * The numerical rank or Elo rating of the player.
     * <p>Determines matchmaking or seeding within the UHC tournament.</p>
     */
    private Integer rank;

    /** * The timestamp or probability weighting of the player's last traitor status.
     * <p>Type: {@code float}. Used by the faction engine to prevent players
     * from being assigned the traitor role too frequently in consecutive games.</p>
     */
    private float lastTraitor;

    // --- 🎮 State Fields ---

    /** * A flag indicating if the player is currently an active participant in the match.
     * <p>If false, the player is considered a spectator or inactive.</p>
     */
    private Boolean participating;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a fully initialized PlayerData instance.
     * <p>
     * Validation is performed on all non-primitive parameters to ensure the
     * object state is valid upon creation.
     * </p>
     * * @param id            The unique integer ID (non-null).
     * @param name          The player's username (non-null).
     * @param rank          The current competitive rank (non-null).
     * @param lastTraitor   The traitor probability factor.
     * @param participating The active participation status (non-null).
     * @throws NullPointerException if any Object-based argument is null.
     */
    public PlayerData(Integer id, String name, Integer rank, float lastTraitor, Boolean participating) {
        try {
            this.id = Objects.requireNonNull(id, "Initialization failed: Player ID cannot be null.");
            this.name = Objects.requireNonNull(name, "Initialization failed: Player name cannot be null.");
            this.rank = Objects.requireNonNull(rank, "Initialization failed: Player rank cannot be null.");
            this.participating = Objects.requireNonNull(participating, "Initialization failed: Participation flag cannot be null.");
            this.lastTraitor = lastTraitor;
        } catch (NullPointerException e) {
            throw new NullPointerException("PlayerData Constructor Error >> " + e.getMessage());
        }
    }

    // --- 🔑 Identity Accessors ---

    /** * Retrieves the player's unique ID.
     * @return The {@link Integer} ID.
     * @throws NullPointerException if the field has been corrupted to null.
     */
    public Integer getId() {
        return Objects.requireNonNull(id, "Data Access Error: ID field is null.");
    }

    /** * Updates the player's unique ID.
     * @param id The new non-null {@link Integer} ID.
     */
    public void setId(Integer id) {
        this.id = Objects.requireNonNull(id, "Data Modification Error: Cannot set a null ID.");
    }

    /** * Retrieves the player's username.
     * @return The {@link String} name.
     * @throws NullPointerException if the field is null.
     */
    public String getName() {
        return Objects.requireNonNull(name, "Data Access Error: Name field is null.");
    }

    /** * Updates the player's username.
     * @param name The new non-null {@link String} name.
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Data Modification Error: Cannot set a null Name.");
    }

    // --- 📊 Competitive Accessors ---

    /** * Retrieves the current rank.
     * @return The {@link Integer} rank value.
     */
    public Integer getRank() {
        return Objects.requireNonNull(rank, "Data Access Error: Rank field is null.");
    }

    /** * Updates the competitive rank.
     * @param rank The new non-null {@link Integer} rank.
     */
    public void setRank(Integer rank) {
        this.rank = Objects.requireNonNull(rank, "Data Modification Error: Cannot set a null Rank.");
    }

    /** * Retrieves the last traitor factor.
     * @return The raw {@code float} value.
     */
    public float getLastTraitor() {
        return lastTraitor;
    }

    /** * Updates the traitor factor.
     * @param lastTraitor The new float value.
     */
    public void setLastTraitor(float lastTraitor) {
        this.lastTraitor = lastTraitor;
    }

    // --- 🏃 State Accessors ---

    /** * Checks the participation status of the player.
     * @return The {@link Boolean} status.
     */
    public Boolean isParticipating() {
        return Objects.requireNonNull(participating, "Data Access Error: Participating field is null.");
    }

    /** * Updates the participation status.
     * @param participating The new non-null {@link Boolean} status.
     */
    public void setParticipating(Boolean participating) {
        this.participating = Objects.requireNonNull(participating, "Data Modification Error: Cannot set a null Participation status.");
    }

    // --- ⚙️ Standard Overrides ---

    /**
     * Generates a string representation of the player's data for logging.
     * @return A formatted string containing all current field values.
     */
    @Override
    public String toString() {
        try {
            return String.format("PlayerData[id=%d, name='%s', rank=%d, traitorFactor=%.2f, active=%b]",
                    id, name, rank, lastTraitor, participating);
        } catch (Exception e) {
            throw new RuntimeException("Critical Error: Failed to serialize PlayerData to String.", e);
        }
    }
}