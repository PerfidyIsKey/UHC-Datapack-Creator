package uhc.functions;

import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.command.commands.Comment;
import uhc.command.commands.ExecuteCommand;
import uhc.command.commands.SummonCommand;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.data.nbt.entity.items.ItemEntityNBT;
import uhc.data.nbt.item.ItemNBT;
import uhc.data.nbt.item.components.ProfileComponent;
import uhc.game.player.PlayerData;
import uhc.game.player.PlayerRegistry;
import uhc.resource.block.BlockId;
import uhc.resource.entity.EntityId;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static uhc.score.ScoreboardObjectiveId.DEATHS;

/**
 * 💀 **Drop Heads Function Generator**
 * <p>
 * This class generates the {@code drop_player_heads.mcfunction} file. It iterates
 * through the current registry of participating players and creates conditional
 * commands to summon a player's head as an item upon their death.
 * </p>
 * <p>
 * <b>Logic Overview:</b>
 * For every registered participant, an {@code /execute at} command is generated
 * that checks for the player's death score. If triggered, it summons an
 * {@code item} entity containing a {@code player_head} with the correct NBT profile.
 * </p>
 */
public class DropHeadsFunction implements DatapackFunction {

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Registers the player head-drop logic into the datapack.
     * <p>
     * <b>Strict Validation:</b>
     * This method verifies the existence of participating players and the validity
     * of the {@link FunctionPath}. If {@link PlayerRegistry} returns a null
     * collection, the process halts to prevent generating an empty utility file.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for the function (non-null).
     * @throws NullPointerException  if {@code datapack}, {@code namespace}, or required
     * constants are null.
     * @throws RuntimeException      if logic assembly fails during the iteration of
     * player data.
     * @throws IllegalStateException if no participating players are found in the registry.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Parameter Validation ---
        Objects.requireNonNull(datapack, "DropHeads Registration Error: Datapack is null.");
        Objects.requireNonNull(namespace, "DropHeads Registration Error: Namespace is null.");

        // Resolve function path; fail-fast if constant is missing
        final FunctionPath path = Objects.requireNonNull(FunctionPath.DROP_PLAYER_HEADS,
                "Registry Error: FunctionPath.DROP_PLAYER_HEADS is not defined.");

        final Function dropFunction = new Function(path);

        try {
            // Inherited header formatting from DatapackFunction interface
            this.appendStandardHeader(dropFunction, "UHC Player Death Handler: Drop Head Logic");

            // --- 2. Registry Acquisition ---
            final Collection<PlayerData> participants = PlayerRegistry.getParticipating();

            if (participants == null) {
                throw new IllegalStateException("CRITICAL: PlayerRegistry.getParticipating() returned null. " +
                        "Cannot generate head-drop logic without player data.");
            }

            // --- 3. Command Generation Loop ---
            for (PlayerData player : participants) {
                // Ensure player data is valid before processing
                Objects.requireNonNull(player, "Registry Error: Found null PlayerData in participant list.");

                this.assembleHeadDropForPlayer(dropFunction, player);
            }

            // Finalize registration
            namespace.addComponent(dropFunction);

        } catch (Exception e) {
            // Catching nested NBT assembly or Command construction errors
            throw new RuntimeException("CRITICAL: DropHeads generation failed for path [" + path + "]. " +
                    "Reason: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Builders ---

    /**
     * Appends the specific drop logic for a single player to the function.
     * <p>
     * This method constructs a command that executes at the location of a player
     * with exactly 1 death score, summoning their specific head profile.
     * </p>
     *
     * @param function The {@link Function} component being modified (non-null).
     * @param player   The {@link PlayerData} of the target participant (non-null).
     * @throws NullPointerException if any parameter is missing.
     */
    private void assembleHeadDropForPlayer(Function function, PlayerData player) {
        String playerName = Objects.requireNonNull(player.getName(), "Logic Error: Player name is null.");

        function.addLine(Comment.create("Handler for player: " + playerName));

        // Construct: execute at @p[name=X,scores={deaths=1}] run summon item ~ ~ ~ {Item:{...}}
        function.addLine(ExecuteCommand.create()
                .at(Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                        SelectorArgumentsBuilder.create()
                                .name(playerName)
                                .scores(Map.of(DEATHS, 1))))
                .run(SummonCommand.create(EntityId.ITEM)
                        .pos(Vec3.relative(0, 0, 0))
                        .nbt(ItemEntityNBT.create()
                                .item(ItemNBT.create(BlockId.PLAYER_HEAD)
                                        .count(1)
                                        .add(ProfileComponent.create(playerName))))));

        function.addLine(Comment.create(" "));
    }
}