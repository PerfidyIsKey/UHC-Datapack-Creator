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
            fileCommands.add(SummonCommand.create(EntityId.MARKER)
                    .pos(Vec3.absolute(0, Constant.worldBottom, 0))
                    .nbt(MarkerNBT.create()
                            .customName(TextComponent.text("Admin")))
                    .generate());

            // Set time
            fileCommands.add(TimeCommand.create(
                            TimeCommand.TimeAction.SET,
                            VariableGameTime.create(0))
                    .generate());

            // Set gamerules
            fileCommands.add(GameRuleCommand.create(GameRuleId.COMMAND_BLOCK_OUTPUT)
                    .booleanValue(true)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DO_DAYLIGHT_CYCLE)
                    .booleanValue(false)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.KEEP_INVENTORY)
                    .booleanValue(true)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DO_MOB_SPAWNING)
                    .booleanValue(false)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DO_TILE_DROPS)
                    .booleanValue(false)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DROWNING_DAMAGE)
                    .booleanValue(false)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.FALL_DAMAGE)
                    .booleanValue(false)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.FIRE_DAMAGE)
                    .booleanValue(false)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.SEND_COMMAND_FEEDBACK)
                    .booleanValue(true)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DO_IMMEDIATE_RESPAWN)
                    .booleanValue(true)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DISABLE_RAIDS)
                    .booleanValue(true)
                    .generate());
            fileCommands.add(GameRuleCommand.create(GameRuleId.DO_INSOMNIA)
                    .booleanValue(false)
                    .generate());

            // Reset scores of all entities
            fileCommands.add(scoreboard.Reset("@e"));
            fileCommands.add(scoreboard.Set(Constant.adminOld, Objective.MinHealth, 20));
            fileCommands.add(scoreboard.Set(Constant.adminOld, Objective.Victory, 1));

            // Get all player UUIDs
            for (int i = 0; i < 4; i++) {
                fileCommands.add(Execute.As("@a", false) +
                        Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                        DataCommand.createGet(
                                        DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                        DataPath.createWithIndex(DataPathId.UUID, i))
                                .generate());
            }

            // Create jukebox at 0,0
            fileCommands.add(Execute.In(Dimension.overworld) +
                    SetBlockCommand.create(
                            BlockPos.absolute(startCoordinate),
                            Block.create(BlockId.JUKEBOX)
                                    .withState(HasRecordState.of(true))
                                    .withData(JukeboxNBT.create()
                                            .recordItem(SingleItemStack.create(ItemId.MUSIC_DISC_STAL, 1)))));

            // Remove tags
            fileCommands.add(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.RESPAWN_DISABLED)
                    .generate());
            fileCommands.add(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.IRON_MAN_CANDIDATE)
                    .generate());
            fileCommands.add(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.IRON_MAN)
                    .generate());
            fileCommands.add(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .remove(EntityTag.RESPAWN)
                    .generate());
            fileCommands.add(TagCommand.target(Constant.admin)
                    .remove(EntityTag.GAME_STARTED)
                    .generate());

            // Set world border
            fileCommands.add(WorldBorderCommand.create(WorldBorderCommand.WorldBorderAction.SET)
                    .distance(2 * world.getSize())
                    .generate());

            // Display ranks
            fileCommands.add(Schedule.callFunction(FileName.display_rank));

            // Set time dummy scoreboard entries
            fileCommands.add(scoreboard.Set("NightTime", getObjectiveByName(Objective.Time), 600));

            // Reset teams & solos
            for (Team t : teams) {
                fileCommands.add(t.emptyTeam());
            }

            // Reset player attributes
            fileCommands.add(Execute.As("@a") +
                    AttributeCommand.setBase(
                            Entity.ofSelector(TargetSelector.SENDER),
                            AttributeId.SCALE,
                            1));
            fileCommands.add(Execute.As("@a") +
                    AttributeCommand.setBase(
                            Entity.ofSelector(TargetSelector.SENDER),
                            AttributeId.WAYPOINT_TRANSMIT_RANGE,
                            0));

            // Set gamemode of player executing the command to creative
            fileCommands.add(GameModeCommand.create(GameModeId.CREATIVE)
                    .target(Entity.ofSelector(TargetSelector.SENDER))
                    .generate()
            );

            // Clear scheduled commands
            fileCommands.add(Schedule.callFunction(FileName.clear_schedule));

            // Clear all player effects
            fileCommands.add(EffectCommand.clear(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .generate());

            // Give admin start potions
            fileCommands.add(Schedule.callFunction(FileName.start_potions));

            // Start timers
            fileCommands.add(Schedule.callFunction(FileName.timer_developer_20));

            // Care Packages
            if (OperationMode.carePackages) {
                // Set scoreboard dummies
                fileCommands.add(scoreboard.Set("CarePackages", getObjectiveByName(Objective.Time), 1200));

                // Remove tags
                fileCommands.add(TagCommand.target(Constant.admin)
                        .remove(EntityTag.CARE_PACKAGES_DROPPED)
                        .generate());
            }

            // Control Point
            if (OperationMode.controlPoints) {
                // Reset scoreboard objectives
                for (int i = 1; i < controlPoints.size() + 1; i++) {
                    for (Team team : teams) {
                        fileCommands.add(scoreboard.Set(team.getName(), getObjectiveByName(Objective.OnCP.extendName(i)), 0));
                        fileCommands.add(scoreboard.Set(team.getName(), getObjectiveByName(Objective.PrevCP.extendName(i)), 0));
                        fileCommands.add(scoreboard.Reset(team.getPlayerColor(), getObjectiveByName(Objective.ControlPoint.extendName(i))));
                    }
                    fileCommands.add(scoreboard.Set(Constant.adminOld, Objective.DisplayCP.extendName(i), 0));
                    fileCommands.add(scoreboard.Set(Constant.adminOld, Objective.ColorCP.extendName(i), -1));
                    fileCommands.add(scoreboard.Set("@a", Objective.ReceivedPerk, 0));
                }
                fileCommands.add(scoreboard.Reset("Solo", getObjectiveByName(Objective.CPScore)));
                for (Team t : teams) {
                    fileCommands.add(scoreboard.Reset(t.getPlayerColor(), getObjectiveByName(Objective.CPScore)));
                    fileCommands.add(t.joinTeam(t.getPlayerColor()));
                }

                // Set scoreboard dummies
                fileCommands.add(scoreboard.Set("Perk1", getObjectiveByName(Objective.CPScore), 3 * singleton.getMinToCPScore()));
                fileCommands.add(scoreboard.Set("Perk2", getObjectiveByName(Objective.CPScore), 6 * singleton.getMinToCPScore()));
                fileCommands.add(scoreboard.Set("Perk3", getObjectiveByName(Objective.CPScore), 12 * singleton.getMinToCPScore()));
                fileCommands.add(scoreboard.Set("Perk4", getObjectiveByName(Objective.CPScore), 15 * singleton.getMinToCPScore()));
                fileCommands.add(scoreboard.Set("TimeVictory", getObjectiveByName(Objective.CPScore), 20 * singleton.getMinToCPScore()));
                fileCommands.add(scoreboard.Set("ControlPoints", getObjectiveByName(Objective.Time), 1800));

                // Remove tags
                fileCommands.add(TagCommand.target(Constant.admin)
                        .remove(EntityTag.indexed(EntityTag.CONTROL_POINT_ENABLED, 1))
                        .generate());
                fileCommands.add(TagCommand.target(Constant.admin)
                        .remove(EntityTag.indexed(EntityTag.CONTROL_POINT_ENABLED, 2))
                        .generate());
                fileCommands.add(TagCommand.target(Constant.admin)
                        .remove(EntityTag.CONTROL_POINT_CAPTURED)
                        .generate());

                // Spawn new Control Points
                fileCommands.add(Schedule.callFunction(FileName.spawn_control_points));

                // Reset bossbars
                BossBar bossBarCp1 = getBossbarByName("cp1");
                BossBar bossBarCp2 = getBossbarByName("cp2");
                fileCommands.add(bossBarCp1.setColor(BossBarColor.white));
                fileCommands.add(bossBarCp1.setVisible(false));
                fileCommands.add(bossBarCp1.setPlayers("@a"));
                fileCommands.add(bossBarCp1.setTitle(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
                fileCommands.add(bossBarCp1.setValue(0));
                fileCommands.add(bossBarCp2.setColor(BossBarColor.white));
                fileCommands.add(bossBarCp2.setVisible(false));
                fileCommands.add(bossBarCp2.setPlayers("@a"));
                fileCommands.add(bossBarCp2.setTitle(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
                fileCommands.add(bossBarCp2.setValue(0));

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
                fileCommands.add(scoreboard.Set("TraitorFaction", getObjectiveByName(Objective.Time), 2400));

                // Remove tags
                fileCommands.add(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .remove(EntityTag.TRAITOR)
                        .generate());
                fileCommands.add(TagCommand.target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .remove(EntityTag.DONT_MAKE_TRAITOR)
                        .generate());
                fileCommands.add(TagCommand.target(Constant.admin)
                        .remove(EntityTag.TRAITORS_ASSIGNED)
                        .generate());
            }

            // In-game team creation
            if (OperationMode.teamCreationInGame) {
                // Reset scoreboard objectives
                fileCommands.add(scoreboard.Set("@a", Objective.IsKiller, 0));
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