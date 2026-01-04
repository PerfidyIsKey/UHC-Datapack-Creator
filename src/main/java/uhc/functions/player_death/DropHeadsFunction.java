package uhc.functions.player_death;

import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.command.commands.ExecuteCommand;
import uhc.command.commands.SummonCommand;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.data.nbt.entity.items.ItemEntityNBT;
import uhc.data.nbt.item.ItemNBT;
import uhc.data.nbt.item.components.ProfileComponent;
import uhc.functions.DatapackFunction;
import uhc.game.player.PlayerData;
import uhc.game.player.PlayerRegistry;
import uhc.resource.block.BlockId;
import uhc.resource.entity.EntityId;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static uhc.score.ScoreboardObjectiveId.DEATHS;

/**
 * 💀 **Drop Player Heads Function Module**
 * <p>
 * Generates the {@code drop_player_heads.mcfunction} file. This module iterates
 * through all participating players and creates conditional commands to summon
 * their specific player head upon death.
 * </p>
 * <p>
 * <b>Logic Flow:</b>
 * Uses {@code /execute at} for every player in the {@link PlayerRegistry}.
 * If their {@code deaths} score is 1, a {@code player_head} item entity is
 * summoned at their location with the corresponding skin profile.
 * </p>
 */
public class DropHeadsFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The header title used for the generated .mcfunction file documentation. */
    private static final String FUNCTION_TITLE = "UHC Death Handler: Drop Player Heads";

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Registers the head-drop logic into the provided namespace.
     * <p>
     * <b>Strict Validation:</b>
     * This method verifies the infrastructure, the registry status, and
     * the validity of every individual {@link PlayerData} entry.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for storage (non-null).
     * @throws NullPointerException  if mandatory parameters are missing.
     * @throws RuntimeException      if command assembly or NBT nesting fails.
     * @throws IllegalStateException if the {@link PlayerRegistry} is empty or null.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Infrastructure Validation ---
        Objects.requireNonNull(datapack, "DropHeads Registration Error: Datapack is null.");
        Objects.requireNonNull(namespace, "DropHeads Registration Error: Namespace is null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.DROP_PLAYER_HEADS,
                "Registry Error: FunctionPath.DROP_PLAYER_HEADS is not defined.");

        final Function dropFunction = new Function(path);

        try {
            // A. Inject Standardized Header
            this.appendStandardHeader(dropFunction, FUNCTION_TITLE);

            // B. Registry Validation
            final Collection<PlayerData> participants = PlayerRegistry.getParticipating();
            if (participants == null || participants.isEmpty()) {
                throw new IllegalStateException("CRITICAL: PlayerRegistry is null or empty. " +
                        "Head-drop logic cannot be generated without participants.");
            }

            // C. Command Assembly Loop
            for (PlayerData player : participants) {
                // Fail immediately if registry contains corrupted data
                Objects.requireNonNull(player, "Registry Error: Found null PlayerData in participant list.");

                this.assembleHeadDropForPlayer(dropFunction, player);
            }

            // D. Visual Termination
            this.addSafeLine(dropFunction, "");
            this.addSafeLine(dropFunction, "--- Head-drop logic generation complete ---");

            // E. Persistence
            namespace.addComponent(dropFunction);

        } catch (Exception e) {
            // No-fallback: Differentiates between registry errors and syntax errors
            throw new RuntimeException("CRITICAL: Failed to assemble DropHeads function at [" + path + "]. " +
                    "Error: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Builders ---

    /**
     * Constructs the specific /execute and /summon logic for a single player.
     * <p>
     * Creates: {@code execute at @p[name=X,scores={deaths=1}] run summon item ~ ~ ~ {Item:{...}}}
     * </p>
     *
     * @param function The {@link Function} component (non-null).
     * @param player   The {@link PlayerData} for the target (non-null).
     * @throws NullPointerException if player name or mandatory data is missing.
     */
    private void assembleHeadDropForPlayer(Function function, PlayerData player) {
        final String playerName = Objects.requireNonNull(player.getName(),
                "Logic Error: Cannot generate drop logic for player with null name.");

        this.addSafeLine(function, "Handler for player: " + playerName);

        // Define the target selector with score filtering
        final Entity targetedPlayer = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .name(playerName)
                        .scores(Map.of(DEATHS, 1)));

        // Assemble the complex NBT and command chain
        try {
            function.addLine(ExecuteCommand.create()
                    .at(targetedPlayer)
                    .run(SummonCommand.create(EntityId.ITEM)
                            .pos(Vec3.relative(0, 0, 0))
                            .nbt(this.buildHeadItemNBT(playerName))));

            this.addSafeLine(function, "");
        } catch (Exception e) {
            throw new RuntimeException("Command Builder Error: Failed to assemble summon logic for [" + playerName + "].", e);
        }
    }

    /**
     * Builds the {@link ItemEntityNBT} containing the specific player head block.
     * * @param playerName The name of the player whose skin will be applied.
     * @return A fully populated NBT object.
     */
    private ItemEntityNBT buildHeadItemNBT(String playerName) {
        return ItemEntityNBT.create()
                .item(ItemNBT.create(BlockId.PLAYER_HEAD)
                        .count(1)
                        .add(ProfileComponent.create(playerName)));
    }
}