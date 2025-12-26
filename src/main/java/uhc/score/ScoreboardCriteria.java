package uhc.score;

import uhc.core.DatapackConfig;
import uhc.resource.block.BlockIdentifier;
import uhc.resource.item.ItemResource;
import java.util.Objects;

/**
 * 📊 **Scoreboard Criteria Identifier**
 * <p>
 * Defines the contract for scoreboard objective criteria in Minecraft. This interface
 * provides a type-safe way to define how scores are updated, ranging from manual
 * "dummy" values to automatic game statistics like mining, item usage, or health tracking.
 * </p>
 */
public interface ScoreboardCriteria {

    // --- 📋 Fixed Vanilla Constants ---

    /**
     * **Manual Score Management**
     * <p>The most common criteria for UHC; scores are only changed via commands or code.
     * Use this for custom game stats like "Coins" or "UHC Kills".</p>
     */
    ScoreboardCriteria DUMMY = new CriteriaImpl.Simple("dummy");

    /**
     * **Automatic Health Tracking**
     * <p>Tracks player health points (0-20). Changes instantly as the player heals or takes damage.</p>
     */
    ScoreboardCriteria HEALTH = new CriteriaImpl.Simple("health");

    /**
     * **Automatic Death Tracker**
     * <p>Increments by 1 every time the player dies.</p>
     */
    ScoreboardCriteria DEATH_COUNT = new CriteriaImpl.Simple("deathCount");

    /**
     * **Automatic Player Kill Tracker**
     * <p>Increments by 1 when the player kills another player.</p>
     */
    ScoreboardCriteria PLAYER_KILL_COUNT = new CriteriaImpl.Simple("playerKillCount");

    // --- 🛰️ Logic & Contract ---

    /**
     * Retrieves the fully qualified Minecraft criteria identifier.
     * <p><b>Examples:</b> {@code "dummy"}, {@code "minecraft.mined:minecraft.stone"}.</p>
     * @return The raw string representation used in Minecraft commands and NBT.
     */
    String getCriteriaName();

    /**
     * Validates the integrity of the criteria identifier.
     * <p><b>Error Catching:</b> Ensures the identifier is not null or whitespace,
     * which would cause command execution failure in-game.</p>
     * @throws IllegalStateException if the generated criteria name is malformed.
     */
    default void validate() throws IllegalStateException {
        String name = getCriteriaName();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Generated Scoreboard criteria name cannot be null or empty.");
        }
    }

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates a type-safe criteria for mining a specific block.
     * <p><b>Error Catching:</b> Validates that the {@code block} is a specific block
     * and not a tag (e.g., #logs), as tags are invalid in scoreboard criteria.</p>
     * @param block The specific {@link BlockIdentifier} to track.
     * @return A criteria formatted as {@code <namespace>.mined:<block_location>}.
     * @throws NullPointerException if the block is null.
     */
    static ScoreboardCriteria mined(BlockIdentifier block) {
        Objects.requireNonNull(block, "Cannot create 'mined' criteria from a null BlockIdentifier.");
        block.validate(); // Catches logic errors like using tags (#)
        return new CriteriaImpl.Namespaced("mined", block.getResourceLocation());
    }

    /**
     * Creates a type-safe criteria for using a specific item.
     * <p><b>Error Catching:</b> Performs resource-level validation on the {@code item}
     * to ensure it follows Minecraft's naming conventions.</p>
     * @param item The specific {@link ItemResource} to track.
     * @return A criteria formatted as {@code <namespace>.used:<item_location>}.
     * @throws NullPointerException if the item is null.
     */
    static ScoreboardCriteria used(ItemResource item) {
        Objects.requireNonNull(item, "Cannot create 'used' criteria from a null ItemResource.");
        item.validate();
        return new CriteriaImpl.Namespaced("used", item.getResourceLocation());
    }

    /**
     * Creates a criteria for a built-in or custom namespaced statistic.
     * <p><b>Type Safety:</b> Uses the {@link CustomStatistics} enum/registry to ensure
     * the path exists and is correctly namespaced.</p>
     * @param stat The {@link CustomStatistics} entry.
     * @return A criteria formatted as {@code <namespace>.custom:<stat_location>}.
     * @throws NullPointerException if the stat is null.
     */
    static ScoreboardCriteria custom(CustomStatistics stat) {
        Objects.requireNonNull(stat, "Cannot create 'custom' criteria from a null CustomStatistics entry.");
        return new CriteriaImpl.Namespaced("custom", stat.getResourceLocation());
    }
}

/**
 * 📦 **Internal Scoreboard Implementations**
 * <p>
 * This class and its records are package-private to enforce the use of
 * {@link ScoreboardCriteria}'s factory methods. This prevents external
 * code from bypassing validation checks.
 * </p>
 */
final class CriteriaImpl {

    /**
     * Private constructor to prevent instantiation of this utility container.
     */
    private CriteriaImpl() {
        throw new UnsupportedOperationException("This is a container class and cannot be instantiated.");
    }

    /**
     * Represents standard, fixed Minecraft criteria (e.g., dummy).
     * @param name The raw criteria name.
     */
    record Simple(String name) implements ScoreboardCriteria {
        @Override
        public String getCriteriaName() {
            return name;
        }

        @Override
        public String toString() {
            return getCriteriaName();
        }
    }

    /**
     * Represents dynamic, namespaced statistics (mined, used, custom).
     * @param type The category of the stat (e.g., "mined").
     * @param resourceLocation The resource being tracked (e.g., "minecraft:stone").
     */
    record Namespaced(String type, String resourceLocation) implements ScoreboardCriteria {
        /**
         * Computes the full criteria string using the global DatapackConfig.
         * @return A string like "minecraft.mined:minecraft.stone".
         */
        @Override
        public String getCriteriaName() {
            // Error Catching: Ensure the global namespace is available
            String namespace = DatapackConfig.MINECRAFT_NAMESPACE;
            if (namespace == null) throw new IllegalStateException("DatapackConfig.MINECRAFT_NAMESPACE is not initialized.");

            return namespace + "." + type + ":" + resourceLocation;
        }

        @Override
        public String toString() {
            return getCriteriaName();
        }
    }
}