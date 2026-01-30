package uhc.game.player;

import java.util.Objects;

/**
 * 👤 **Player Data Model (PlayerData)**
 * <p>
 * This class serves as a robust data container for player-specific information
 * within the UHC system. It maintains identity, competitive statistics, and
 * active session states.
 * </p>
 * <p>
 * <b>Strict Validation Policy:</b>
 * <ul>
 * <li>No null values allowed for Object-based fields.</li>
 * <li>Numerical bounds checking (ID and Rank must be non-negative).</li>
 * <li>Immediate exception throwing on invalid input (No Fallbacks).</li>
 * </ul>
 * </p>
 */
public class PlayerData {

    // --- 🆔 Identity Fields ---

    /** * The unique numerical identifier for the player.
     * Primary key used for database or registry lookups.
     */
    private Integer id;

    /** * The Minecraft username of the player.
     * Stored as a String to match the Mojang profile name exactly.
     */
    private String name;

    // --- 📈 Competitive Fields ---

    /** * The numerical rank or Elo rating of the player.
     * Determines matchmaking or seeding within the UHC tournament.
     */
    private Integer rank;

    /** * The timestamp or probability weighting of the player's last traitor status.
     * Used by the faction engine to prevent repeat role assignments.
     */
    private float lastTraitor;

    // --- 🎮 State Fields ---

    /** * A flag indicating if the player is currently an active participant in the match.
     * If false, the player is considered a spectator or inactive for the current build.
     */
    private Boolean participating;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a fully initialized and validated PlayerData instance.
     * * @param id            The unique integer ID (must be non-null and >= 0).
     * @param name          The player's username (must be non-null and non-empty).
     * @param rank          The current competitive rank (must be non-null and >= 0).
     * @param lastTraitor   The traitor probability factor or timestamp.
     * @param participating The active participation status (must be non-null).
     * * @throws NullPointerException if any Object-based argument is null.
     * @throws IllegalArgumentException if numerical values are out of valid bounds.
     */
    public PlayerData(Integer id, String name, Integer rank, float lastTraitor, Boolean participating) {
        // We use an internal method to ensure consistent validation between constructor and setters
        this.setId(id);
        this.setName(name);
        this.setRank(rank);
        this.setParticipating(participating);
        this.lastTraitor = lastTraitor;
    }

    // --- 🔑 Identity Accessors ---

    /** * Retrieves the player's unique ID.
     * @return The non-null {@link Integer} ID.
     * @throws IllegalStateException if the ID has somehow become null.
     */
    public Integer getId() {
        return Objects.requireNonNull(id, "Data Integrity Error: ID field is unexpectedly null.");
    }

    /** * Updates the player's unique ID with validation.
     * @param id The new non-null {@link Integer} ID (must be >= 0).
     * @throws NullPointerException if id is null.
     * @throws IllegalArgumentException if id is negative.
     */
    public void setId(Integer id) {
        Objects.requireNonNull(id, "Data Modification Error: Cannot set a null ID.");
        if (id < 0) {
            throw new IllegalArgumentException("Data Modification Error: ID cannot be negative (Provided: " + id + ").");
        }
        this.id = id;
    }

    /** * Retrieves the player's Minecraft username.
     * @return The non-null {@link String} name.
     * @throws IllegalStateException if the name field is null.
     */
    public String getName() {
        return Objects.requireNonNull(name, "Data Integrity Error: Name field is unexpectedly null.");
    }

    /** * Updates the player's username with validation.
     * @param name The new non-null {@link String} name.
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if name is empty or blank.
     */
    public void setName(String name) {
        Objects.requireNonNull(name, "Data Modification Error: Cannot set a null Name.");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Data Modification Error: Player name cannot be empty or whitespace.");
        }
        this.name = name;
    }

    // --- 📊 Competitive Accessors ---

    /** * Retrieves the player's current competitive rank.
     * @return The non-null {@link Integer} rank.
     */
    public Integer getRank() {
        return Objects.requireNonNull(rank, "Data Integrity Error: Rank field is unexpectedly null.");
    }

    /** * Updates the competitive rank with validation.
     * @param rank The new non-null {@link Integer} rank (must be >= 0).
     * @throws NullPointerException if rank is null.
     * @throws IllegalArgumentException if rank is negative.
     */
    public void setRank(Integer rank) {
        Objects.requireNonNull(rank, "Data Modification Error: Cannot set a null Rank.");
        if (rank < 0) {
            throw new IllegalArgumentException("Data Modification Error: Rank cannot be negative (Provided: " + rank + ").");
        }
        this.rank = rank;
    }

    /** * Retrieves the last traitor weighting factor.
     * @return The raw {@code float} value.
     */
    public float getLastTraitor() {
        return lastTraitor;
    }

    /** * Updates the traitor probability/history factor.
     * @param lastTraitor The new float value.
     */
    public void setLastTraitor(float lastTraitor) {
        this.lastTraitor = lastTraitor;
    }

    // --- 🏃 State Accessors ---

    /** * Checks if the player is marked as an active participant.
     * @return The non-null {@link Boolean} participation status.
     */
    public Boolean isParticipating() {
        return Objects.requireNonNull(participating, "Data Integrity Error: Participating field is unexpectedly null.");
    }

    /** * Updates the participation status for the current session.
     * @param participating The new non-null {@link Boolean} status.
     * @throws NullPointerException if participating is null.
     */
    public void setParticipating(Boolean participating) {
        this.participating = Objects.requireNonNull(participating, "Data Modification Error: Participation status must be true or false (null not allowed).");
    }

    // --- ⚙️ Standard Overrides ---

    /**
     * Generates a string representation of the player's data for logging and debugging.
     * <p>Uses strict serialization: if fields are missing, it throws an error instead of returning "null".</p>
     * @return A formatted string containing ID, name, rank, traitor factor, and activity status.
     * @throws RuntimeException if the object is in an invalid state during serialization.
     */
    @Override
    public String toString() {
        try {
            return String.format("PlayerData[id=%d, name='%s', rank=%d, traitorFactor=%.2f, active=%b]",
                    getId(), getName(), getRank(), getLastTraitor(), isParticipating());
        } catch (Exception e) {
            throw new RuntimeException("Critical Serialization Error: Failed to generate PlayerData string. " + e.getMessage());
        }
    }
}