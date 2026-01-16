package uhc.functions.startup;

import Enums.*;
import HelperClasses.BossBar;
import HelperClasses.Execute;
import HelperClasses.Team;
import controlpoints.ControlPoint;
import uhc.arguments.block.Block;
import uhc.arguments.block.BlockPos;
import uhc.arguments.coordinate.Vec3;
import uhc.arguments.data.DataPath;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.SingleItemStack;
import uhc.arguments.time.VariableGameTime;
import uhc.command.commands.*;
import uhc.command.commands.data.DataTargetEntity;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Constants;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.data.nbt.blockentity.JukeboxNBT;
import uhc.data.nbt.blockentity.state.HasRecordState;
import uhc.data.nbt.entity.other.MarkerNBT;
import uhc.functions.DatapackFunction;
import uhc.resource.attribute.AttributeId;
import uhc.resource.block.BlockId;
import uhc.resource.data.DataPathId;
import uhc.resource.entity.EntityId;
import uhc.resource.gameplay.GameModeId;
import uhc.resource.gameplay.GameRuleId;
import uhc.resource.item.ItemId;
import uhc.resource.tag.EntityTag;
import uhc.score.ScoreboardObjectiveId;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 📄 **Empty Function Template**
 * <p>
 * A boilerplate implementation of the {@link DatapackFunction} contract.
 * This class serves as a blueprint for creating new functional modules
 * within the Minecraft Datapack.
 * </p>
 * <p>
 * <b>Usage:</b>
 * 1. Clone this class.
 * 2. Define a unique {@link FunctionPath}.
 * 3. Implement custom logic using {@code this.addSafeLine()}.
 * </p>
 */
public class DeveloperModeFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The descriptive title utilized for generating the standardized header
     * at the beginning of the {@code .mcfunction} file.
     */
    private static final String FUNCTION_TITLE = "Template Function Title";

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Executes the registration of the function logic into the provided namespace.
     * <p>
     * <b>Process:</b>
     * <ol>
     * <li>Validates infrastructure components (Datapack, Namespace).</li>
     * <li>Initializes the {@link Function} component via the registry path.</li>
     * <li>Injects documentation headers and command lines.</li>
     * <li>Persists the finalized component into the Namespace.</li>
     * </ol>
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance used for cross-reference (non-null).
     * @param namespace The target {@link Namespace} where the resulting file will reside (non-null).
     * @throws NullPointerException if {@code datapack}, {@code namespace}, or the internal {@code path} is null.
     * @throws RuntimeException     if assembly logic fails during the line-injection phase or registration.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 📂 1. Primary Parameter Validation ---
        Objects.requireNonNull(datapack, "Function Registration Error: The Datapack container must not be null.");
        Objects.requireNonNull(namespace, "Function Registration Error: The target Namespace must not be null.");

        // --- 📂 2. Function Path & Component Initialization ---
        // Change FunctionPath.EMPTY to the specific enum entry relevant to your feature.
        final FunctionPath path = Objects.requireNonNull(FunctionPath.DEVELOPER_MODE,
                "Registry Error: The provided FunctionPath is not defined or is null in the system registry.");

        final Function currentFunction = new Function(path);

        // --- ⚔️ 3. Command & Comment Assembly Logic ---
        try {
            // A. Inject Standardized Header Block
            // This inherited method ensures uniform documentation across all files.
            this.appendStandardHeader(currentFunction, FUNCTION_TITLE);

            // B. Inject Informational Documentation
            // Using addSafeLine ensures strings are automatically converted to # Comments.
            this.addSafeLine(currentFunction, "Core Logic: [Describe the primary purpose here]");

            // C. Inject Executable Logic
            // Placeholder for MinecraftCommand objects or further documentation strings.
            // Example: this.addSafeLine(currentFunction, ScoreboardCommand.players().target(...)...);
            // Recreate forceload
            currentFunction.addLine(ForceLoadCommand.remove(Constants.spawnColumn));
            currentFunction.addLine(ForceLoadCommand.add(Constants.spawnColumn));

            // Create marker entity
            currentFunction.addLine(KillCommand.targets(Constants.admin));
            currentFunction.addLine(SummonCommand.entity(EntityId.MARKER,
                    Constants.spawnPos,
                    MarkerNBT.create()
                            .customName(TextComponent.text("Admin"))));

            // Set time
            currentFunction.addLine(TimeCommand.set(VariableGameTime.create(0)));

            // Set gamerules
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.COMMAND_BLOCK_OUTPUT, true));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DO_DAYLIGHT_CYCLE, false));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.KEEP_INVENTORY, true));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DO_MOB_SPAWNING, false));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DO_TILE_DROPS, false));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DROWNING_DAMAGE, false));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.FALL_DAMAGE, false));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.FIRE_DAMAGE, false));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.SEND_COMMAND_FEEDBACK, true));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DO_IMMEDIATE_RESPAWN, true));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DISABLE_RAIDS, true));
            currentFunction.addLine(GameRuleCommand.set(GameRuleId.DO_INSOMNIA, false));

            // Reset scores of all entities
            currentFunction.addLine(ScoreboardCommand.reset(Entity.ofSelector(TargetSelector.ALL_ENTITIES)));
            currentFunction.addLine(ScoreboardCommand.setScore(Constants.admin, ScoreboardObjectiveId.MIN_HEALTH, 20));
            currentFunction.addLine(ScoreboardCommand.setScore(Constants.admin, ScoreboardObjectiveId.VICTORY, 1));

            // Get all player UUIDs
            for (int i = 0; i < 4; i++) {
                currentFunction.addLine(ExecuteCommand.create()
                        .as(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .storeResultScore(Entity.ofSelector(TargetSelector.SENDER), ScoreboardObjectiveId.indexed(ScoreboardObjectiveId.COLLAR_CHECK, i))
                        .run(DataCommand.get(Entity.ofSelector(TargetSelector.SENDER), DataPath.createWithIndex(DataPathId.UUID, i))));
            }

            // Create jukebox at 0,0
            currentFunction.addLine(Execute.In(Dimension.overworld) +
                    SetBlockCommand.create(
                            BlockPos.absolute(startCoordinate),
                            Block.create(BlockId.JUKEBOX)
                                    .withState(HasRecordState.of(true))
                                    .withData(JukeboxNBT.create()
                                            .recordItem(SingleItemStack.create(ItemId.MUSIC_DISC_STAL, 1)))));

            // Remove tags
            currentFunction.addLine(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.RESPAWN_DISABLED));
            currentFunction.addLine(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.IRON_MAN_CANDIDATE));
            currentFunction.addLine(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.IRON_MAN));
            currentFunction.addLine(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.RESPAWN));
            currentFunction.addLine(TagCommand.target(Constant.admin)
                    .remove(EntityTag.GAME_STARTED));

            // Set world border
            currentFunction.addLine(WorldBorderCommand.create(WorldBorderCommand.WorldBorderAction.SET)
                    .distance(2 * world.getSize()));

            // Display ranks
            currentFunction.addLine(Schedule.callFunction(FileName.display_rank));

            // Set time dummy scoreboard entries
            currentFunction.addLine(scoreboard.Set("NightTime", getObjectiveByName(Objective.Time), 600));

            // Reset teams & solos
            for (Team t : teams) {
                currentFunction.addLine(t.emptyTeam());
            }

            // Reset player attributes
            currentFunction.addLine(Execute.As("@a") +
                    AttributeCommand.setBase(
                            Entity.ofSelector(TargetSelector.SENDER),
                            AttributeId.SCALE,
                            1));
            currentFunction.addLine(Execute.As("@a") +
                    AttributeCommand.setBase(
                            Entity.ofSelector(TargetSelector.SENDER),
                            AttributeId.WAYPOINT_TRANSMIT_RANGE,
                            0));

            // Set gamemode of player executing the command to creative
            currentFunction.addLine(GameModeCommand.create(GameModeId.CREATIVE)
                    .target(Entity.ofSelector(TargetSelector.SENDER))
            );

            // Clear scheduled commands
            currentFunction.addLine(Schedule.callFunction(FileName.clear_schedule));

            // Clear all player effects
            currentFunction.addLine(EffectCommand.clear(Entity.ofSelector(TargetSelector.ALL_PLAYERS)));

            // Give admin start potions
            currentFunction.addLine(Schedule.callFunction(FileName.start_potions));

            // Start timers
            currentFunction.addLine(Schedule.callFunction(FileName.timer_developer_20));

            // Care Packages
            if (OperationMode.carePackages) {
                // Set scoreboard dummies
                currentFunction.addLine(scoreboard.Set("CarePackages", getObjectiveByName(Objective.Time), 1200));

                // Remove tags
                currentFunction.addLine(TagCommand.target(Constant.admin)
                        .remove(EntityTag.CARE_PACKAGES_DROPPED)
                        .generate());
            }

            // Control Point
            if (OperationMode.controlPoints) {
                // Reset scoreboard objectives
                for (int i = 1; i < controlPoints.size() + 1; i++) {
                    for (Team team : teams) {
                        currentFunction.addLine(scoreboard.Set(team.getName(), getObjectiveByName(Objective.OnCP.extendName(i)), 0));
                        currentFunction.addLine(scoreboard.Set(team.getName(), getObjectiveByName(Objective.PrevCP.extendName(i)), 0));
                        currentFunction.addLine(scoreboard.Reset(team.getPlayerColor(), getObjectiveByName(Objective.ControlPoint.extendName(i))));
                    }
                    currentFunction.addLine(scoreboard.Set(Constant.adminOld, Objective.DisplayCP.extendName(i), 0));
                    currentFunction.addLine(scoreboard.Set(Constant.adminOld, Objective.ColorCP.extendName(i), -1));
                    currentFunction.addLine(scoreboard.Set("@a", Objective.ReceivedPerk, 0));
                }
                currentFunction.addLine(scoreboard.Reset("Solo", getObjectiveByName(Objective.CPScore)));
                for (Team t : teams) {
                    currentFunction.addLine(scoreboard.Reset(t.getPlayerColor(), getObjectiveByName(Objective.CPScore)));
                    currentFunction.addLine(t.joinTeam(t.getPlayerColor()));
                }

                // Set scoreboard dummies
                currentFunction.addLine(scoreboard.Set("Perk1", getObjectiveByName(Objective.CPScore), 3 * singleton.getMinToCPScore()));
                currentFunction.addLine(scoreboard.Set("Perk2", getObjectiveByName(Objective.CPScore), 6 * singleton.getMinToCPScore()));
                currentFunction.addLine(scoreboard.Set("Perk3", getObjectiveByName(Objective.CPScore), 12 * singleton.getMinToCPScore()));
                currentFunction.addLine(scoreboard.Set("Perk4", getObjectiveByName(Objective.CPScore), 15 * singleton.getMinToCPScore()));
                currentFunction.addLine(scoreboard.Set("TimeVictory", getObjectiveByName(Objective.CPScore), 20 * singleton.getMinToCPScore()));
                currentFunction.addLine(scoreboard.Set("ControlPoints", getObjectiveByName(Objective.Time), 1800));

                // Remove tags
                currentFunction.addLine(TagCommand.target(Constant.admin)
                        .remove(EntityTag.indexed(EntityTag.CONTROL_POINT_ENABLED, 1))
                        .generate());
                currentFunction.addLine(TagCommand.target(Constant.admin)
                        .remove(EntityTag.indexed(EntityTag.CONTROL_POINT_ENABLED, 2))
                        .generate());
                currentFunction.addLine(TagCommand.target(Constant.admin)
                        .remove(EntityTag.CONTROL_POINT_CAPTURED)
                        .generate());

                // Spawn new Control Points
                currentFunction.addLine(Schedule.callFunction(FileName.spawn_control_points));

                // Reset bossbars
                BossBar bossBarCp1 = getBossbarByName("cp1");
                BossBar bossBarCp2 = getBossbarByName("cp2");
                currentFunction.addLine(bossBarCp1.setColor(BossBarColor.white));
                currentFunction.addLine(bossBarCp1.setVisible(false));
                currentFunction.addLine(bossBarCp1.setPlayers("@a"));
                currentFunction.addLine(bossBarCp1.setTitle(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
                currentFunction.addLine(bossBarCp1.setValue(0));
                currentFunction.addLine(bossBarCp2.setColor(BossBarColor.white));
                currentFunction.addLine(bossBarCp2.setVisible(false));
                currentFunction.addLine(bossBarCp2.setPlayers("@a"));
                currentFunction.addLine(bossBarCp2.setTitle(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
                currentFunction.addLine(bossBarCp2.setValue(0));

                // Kill waypoints
                for (ControlPoint controlPoint : controlPoints) {
                    currentFunction.addLine(KillCommand.targets(Entity.ofSelector(
                            TargetSelector.NEAREST_ENTITY,
                            SelectorArgumentsBuilder.create()
                                    .tag(controlPoint.getName()))));
                }
            }

            // Traitor Faction
            if (OperationMode.traitorFaction) {
                // Set scoreboard dummies
                currentFunction.addLine(scoreboard.Set("TraitorFaction", getObjectiveByName(Objective.Time), 2400));

                // Remove tags
                currentFunction.addLine(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .remove(EntityTag.TRAITOR)
                        .generate());
                currentFunction.addLine(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .remove(EntityTag.DONT_MAKE_TRAITOR)
                        .generate());
                currentFunction.addLine(TagCommand.target(Constant.admin)
                        .remove(EntityTag.TRAITORS_ASSIGNED)
                        .generate());
            }

            // In-game team creation
            if (OperationMode.teamCreationInGame) {
                // Reset scoreboard objectives
                currentFunction.addLine(scoreboard.Set("@a", Objective.IsKiller, 0));
            }

            // D. Structural Formatting
            // Appends a blank line for visual scannability in the output file.
            this.addSafeLine(currentFunction, "");

        } catch (Exception e) {
            // "No-Fallback" Policy: We throw a RuntimeException immediately rather than generating partial files.
            throw new RuntimeException("CRITICAL: Failed to assemble function logic for [" + path + "]. " + e.getMessage(), e);
        }

        // --- 🚀 4. Final Namespace Registration ---
        try {
            // Finalize the registration within the Namespace container.
            namespace.addComponent(currentFunction);
        } catch (Exception e) {
            // Catching potential internal registration conflicts (e.g., duplicate paths).
            throw new RuntimeException("CRITICAL: Final registration of [" + path + "] failed for namespace: " + namespace.getName(), e);
        }
    }
}