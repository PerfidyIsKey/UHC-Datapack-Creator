package uhc.game.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 📇 **Player Registry**
 * <p>
 * This class serves as the central data authority for the UHC session. It manages
 * the lifecycle and accessibility of all {@link PlayerData} instances.
 * </p>
 * <p>
 * <b>Strict Data Policy:</b> This registry does not allow for partial data or
 * default "dummy" players. If the internal hardcoded roster contains an error
 * (e.g., a null name or ID), the constructor will throw a critical exception
 * to prevent the game from starting with corrupted state.
 * </p>
 */
public final class PlayerRegistry {

    // --- 📂 Internal Data Storage ---

    /** * The master collection of all registered players.
     * This list is immutable once the constructor completes to ensure thread-safety
     * and data integrity during the match.
     */
    private final List<PlayerData> players;

    // --- 🏗️ Constructor & Initialization ---

    /**
     * Constructs the registry and populates it with the hardcoded player roster.
     * <p>
     * <b>Validation:</b> Every entry is validated against the {@link PlayerData}
     * constraints. The entire list is wrapped in {@link Collections#unmodifiableList}
     * after population.
     * </p>
     * @throws RuntimeException if any entry in the roster fails validation or
     * if the population process is interrupted.
     */
    public PlayerRegistry() {
        List<PlayerData> roster = new ArrayList<>();

        try {
            // Identity: ID, Name, Rank, TraitorFactor, Participating
            roster.add(new PlayerData(1, "Snodog627", 81, 52.0f, true));
            roster.add(new PlayerData(2, "Mr9Madness", 82, 60.0f, true));
            roster.add(new PlayerData(17, "Kalazniq", 64, 58.0f, true));
            roster.add(new PlayerData(18, "Vladik71", 45, 0.0f, false));
            roster.add(new PlayerData(24, "ThurianBodan", 2, 0.0f, false));
            roster.add(new PlayerData(25, "PerfidyIsKey", 89, 58.0f, false));
            roster.add(new PlayerData(27, "jonmo0105", 21, 0.0f, false));
            roster.add(new PlayerData(28, "TheDinoGame", 76, 50.0f, false));
            roster.add(new PlayerData(30, "Th3Flash05", 13, 46.0f, false));
            roster.add(new PlayerData(31, "viccietors", 24, 49.0f, false));
            roster.add(new PlayerData(33, "_HexGamer", 59, 50.0f, false));
            roster.add(new PlayerData(34, "Bobdafish", 23, 46.0f, false));
            roster.add(new PlayerData(38, "SpookySpiker", 23, 53.0f, false));
            roster.add(new PlayerData(40, "Eason950116", 0, 0.0f, false));
            roster.add(new PlayerData(41, "CorruptUncle", 2, 44.0f, false));
            roster.add(new PlayerData(42, "Pimmie36", 4, 0.0f, false));
            roster.add(new PlayerData(44, "PbQuinn", 44, 61.0f, true));
            roster.add(new PlayerData(45, "Vermeil_Chan", 35, 0.0f, false));
            roster.add(new PlayerData(46, "Jobbo2002", 2, 0.0f, false));
            roster.add(new PlayerData(47, "Uncle_Lolly", 1, 0.0f, false));
            roster.add(new PlayerData(48, "AurqSnqtcher", 61, 57.0f, true));
            roster.add(new PlayerData(49, "cat_person", 10, 0.0f, false));
            roster.add(new PlayerData(50, "GoldBard2474348", 5, 0.0f, false));
            roster.add(new PlayerData(51, "CrimsonCid", 3, 0.0f, false));
            roster.add(new PlayerData(52, "NekoBotUwU", 47, 54.0f, false));
            roster.add(new PlayerData(53, "TheTolstar", 11, 0.0f, false));
            roster.add(new PlayerData(54, "Vogelfan", 156, 60.0f, false));
            roster.add(new PlayerData(55, "mrminebase", 4, 0.0f, false));
            roster.add(new PlayerData(56, "JOPONOS", 76, 55.0f, false));
            roster.add(new PlayerData(57, "neokneipies", 0, 0.0f, false));
            roster.add(new PlayerData(58, "marckstef", 9, 0.0f, false));
            roster.add(new PlayerData(59, "Dan_Fingerman", 4, 0.0f, false));
            roster.add(new PlayerData(60, "Bertje13", 35, 0.0f, false));
            roster.add(new PlayerData(61, "blacksnake29", 38, 0.0f, false));
            roster.add(new PlayerData(62, "PwodY_", 0, 0.0f, false));
            roster.add(new PlayerData(63, "EarthKun1", 22, 0.0f, true));
            roster.add(new PlayerData(64, "BlueBlacky", 13, 0.0f, false));
            roster.add(new PlayerData(65, "x_Mylan_x", 3, 0.0f, false));
            roster.add(new PlayerData(66, "Leavd", 31, 0.0f, false));
            roster.add(new PlayerData(67, "amiazukiN", 1, 0.0f, false));
            roster.add(new PlayerData(68, "Mosu_2000", 2, 0.0f, false));
            roster.add(new PlayerData(69, "DAABlocksholmes", 8, 0.0f, false));
            roster.add(new PlayerData(70, "ANDRIQS", 116, 54.0f, false));
            roster.add(new PlayerData(71, "Joker447xd9", 29, 0.0f, false));
            roster.add(new PlayerData(72, "Cthuleric", 39, 0.0f, false));
            roster.add(new PlayerData(73, "smilerification", 17, 0.0f, false));
            roster.add(new PlayerData(74, "Sachi_Senpai", 50, 56.0f, false));
            roster.add(new PlayerData(75, "Arrownymes", 89, 55.0f, false));
            roster.add(new PlayerData(76, "ThunderZinogre", 16, 0.0f, false));
            roster.add(new PlayerData(77, "JacobWouter", 71, 0.0f, false));
            roster.add(new PlayerData(78, "iron_crate", 53, 59.0f, true));
            roster.add(new PlayerData(79, "sanswithagun", 93, 57.0f, true));
            roster.add(new PlayerData(80, "YeetFrog", 18, 0.0f, true));
            roster.add(new PlayerData(81, "Athype", 16, 0.0f, false));
            roster.add(new PlayerData(82, "Lazyness98", 8, 0.0f, false));
            roster.add(new PlayerData(83, "AVexedFrog", 9, 0.0f, false));
            roster.add(new PlayerData(84, "sugarsweetgen", 10, 0.0f, false));
            roster.add(new PlayerData(85, "Em0tic0n1212", 104, 0.0f, true));
            roster.add(new PlayerData(86, "Mellaan_", 16, 0.0f, false));
            roster.add(new PlayerData(87, "PotatoLot", 19, 0.0f, false));
            roster.add(new PlayerData(88, "SunshineWasTaken", 136, 59.0f, true));
            roster.add(new PlayerData(89, "TheMuffinsen", 6, 0.0f, false));
            roster.add(new PlayerData(90, "nei720", 33, 0.0f, false));
            roster.add(new PlayerData(91, "KarenFinance1287", 14, 0.0f, false));
            roster.add(new PlayerData(92, "MrUnknown17", 22, 0.0f, false));
            roster.add(new PlayerData(93, "Cept_s", 25, 0.0f, false));
            roster.add(new PlayerData(94, "Robert_Shadow", 13, 0.0f, false));
            roster.add(new PlayerData(95, "Bredark", 72, 61.0f, true));
            roster.add(new PlayerData(96, "Fushi_96", 2, 0.0f, true));
            roster.add(new PlayerData(97, "Xylios__", 20, 0.0f, true));

            this.players = Collections.unmodifiableList(roster);
        } catch (Exception e) {
            // Error Catching: Wrap the underlying cause (likely a null in PlayerData)
            throw new RuntimeException("CRITICAL REGISTRY FAILURE: The player data set contains malformed entries. " + e.getMessage(), e);
        }
    }

    // --- 🔍 Retrieval & Query Logic ---

    /**
     * Retrieves the complete, unmodifiable list of every player known to the system.
     * @return An immutable {@link List} of {@link PlayerData}.
     * @throws NullPointerException if the internal storage has been compromised.
     */
    public List<PlayerData> getAllPlayers() {
        return Objects.requireNonNull(players, "Registry Access Failure: Master player list is null.");
    }

    /**
     * Filters the registry to return only players currently marked for active participation.
     * @return A {@link List} containing only active {@link PlayerData} entries.
     * @throws IllegalStateException if the stream filtering process encounters a data error.
     */
    public List<PlayerData> getParticipatingPlayers() {
        try {
            return players.stream()
                    .filter(PlayerData::isParticipating)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException("Registry Query Failure: Encountered an error while filtering active participants.", e);
        }
    }

    /**
     * Searches for a single player by their unique numerical ID.
     * <p>
     * <b>Strict Logic:</b> If the ID is not found, an exception is thrown.
     * No fallback/null objects are returned.
     * </p>
     * @param id The {@link Integer} ID to search for (non-null).
     * @return The matching {@link PlayerData} instance.
     * @throws NullPointerException if the provided ID is null.
     * @throws IllegalArgumentException if no player in the registry matches the provided ID.
     */
    public PlayerData getPlayerById(Integer id) {
        Objects.requireNonNull(id, "Search Failure: Cannot search for a player using a null ID.");

        return players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Registry Search Error: No player exists with ID [" + id + "]."));
    }
}