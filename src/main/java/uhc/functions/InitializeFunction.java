package uhc.functions;

import uhc.arguments.block.Block;
import uhc.arguments.block.BlockPos;
import uhc.arguments.coordinate.Vec3;
import uhc.command.commands.*;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.data.nbt.blockentity.SignNBT;
import uhc.data.nbt.blockentity.state.FacingBlockState;
import uhc.data.nbt.blockentity.state.WaterloggedState;
import uhc.data.nbt.entity.projectiles.FireworkRocketNBT;
import uhc.data.nbt.item.components.FireworksComponent;
import uhc.game.control_points.ControlPointConfig;
import uhc.game.control_points.ControlPointData;
import uhc.game.control_points.ControlPointRegistry;
import uhc.resource.block.BlockId;
import uhc.resource.block.DynamicBlock;
import uhc.resource.block.WoodBlock;
import uhc.resource.block.WoodType;
import uhc.resource.world.DimensionId;
import uhc.resource.entity.EntityId;
import uhc.resource.gameplay.DifficultyId;
import uhc.resource.gameplay.GameModeId;
import uhc.resource.gameplay.GameRuleId;
import uhc.score.DisplaySlot;
import uhc.score.ScoreboardObjectiveId;
import uhc.team.TeamData;
import uhc.team.TeamRegistry;
import uhc.text.ClickEvent;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 🛠️ **Initialize Function Component**
 * <p>
 * This class handles the generation of the core initialization logic for the UHC match.
 * It configures game rules, scoreboard objectives, teams, and world structures.
 * </p>
 */
public class InitializeFunction implements DatapackFunction {

    /**
     * Registers the initialization function within the provided datapack namespace.
     * <p>
     * The method executes the following logical phases:
     * <ol>
     * <li>Validation: Ensures the Datapack and Namespace are valid.</li>
     * <li>Environment: Configures gamerules, difficulty, and spawn points.</li>
     * <li>Infrastructure: Sets up scoreboards and team data.</li>
     * <li>Construction: Generates the physical barrier lobby and memorial sign.</li>
     * <li>Objectives: Configures active Control Point bossbars.</li>
     * </ol>
     * </p>
     *
     * @param datapack            The {@link Datapack} receiving the function.
     * @param customNamespaceName The name of the target namespace.
     * @throws RuntimeException if any phase of the registration fails.
     */
    @Override
    public void register(Datapack datapack, String customNamespaceName) {
        try {
            // --- 1. Parameter Validation ---
            Objects.requireNonNull(datapack, "Datapack registry failed: Input datapack is null.");
            Objects.requireNonNull(customNamespaceName, "Datapack registry failed: Namespace name is null or empty.");

            Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);
            if (customNamespace == null) {
                throw new IllegalStateException("Failed to resolve namespace: " + customNamespaceName);
            }

            FunctionPath functionPath = FunctionPath.INITIALIZATION;
            Function currentFunction = new Function(functionPath);

            // --- ⚙️ Phase 1: Game Rules & Global Environment ---
            currentFunction.addLine(Comment.create("=== World Settings & Gamerules ==="));

            for (DimensionId dimension : DimensionId.values()) {
                Objects.requireNonNull(dimension, "Environment setup failed: Found null DimensionId in registry.");
                currentFunction.addLine(ExecuteCommand.create()
                        .in(dimension)
                        .run(GameRuleCommand.create(GameRuleId.NATURAL_REGENERATION)
                                .booleanValue(false)));
            }

            currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_IMMEDIATE_RESPAWN).booleanValue(true));
            currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_PATROL_SPAWNING).booleanValue(false));
            currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_MOB_SPAWNING).booleanValue(false));
            currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_WEATHER_CYCLE).booleanValue(false));
            currentFunction.addLine(GameRuleCommand.create(GameRuleId.SPAWN_RADIUS).intValue(0));

            currentFunction.addLine(DifficultyCommand.create().difficulty(DifficultyId.HARD));
            currentFunction.addLine(GameModeCommand.create(GameModeId.ADVENTURE).setDefault());
            currentFunction.addLine(SetWorldSpawnCommand.create().pos(BlockPos.absolute(0, 221, 0)));

            // --- 📊 Phase 2: Scoreboard & Teams ---
            currentFunction.addLine(Comment.create("=== Scoreboard Initialization ==="));
            currentFunction.addAll(ScoreboardCommand.batch().registerAll());
            currentFunction.addLine(ScoreboardCommand.objectives().setDisplay(DisplaySlot.BELOW_NAME, ScoreboardObjectiveId.HEARTS));
            currentFunction.addLine(ScoreboardCommand.objectives().setDisplay(DisplaySlot.LIST, ScoreboardObjectiveId.HEARTS));

            currentFunction.addLine(Comment.create("=== Team Setup ==="));
            if (TeamRegistry.ALL.isEmpty()) {
                throw new IllegalStateException("Team setup failed: TeamRegistry is empty.");
            }
            for (TeamData team : TeamRegistry.ALL) {
                Objects.requireNonNull(team, "Team setup failed: Found null TeamData in TeamRegistry.");
                currentFunction.addAll(TeamCommand.initialize(team));
            }

            // --- 🏗️ Phase 3: Lobby & Staging ---
            currentFunction.addLine(Comment.create("=== Staging Area Construction ==="));

            // Generate the physical lobby structure
            currentFunction.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                    .run(FillCommand.create(BlockPos.absolute(-6, 220, -6), BlockPos.absolute(6, 226, 6), Block.create(BlockId.BARRIER))));

            currentFunction.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                    .run(FillCommand.create(BlockPos.absolute(-5, 221, -5), BlockPos.absolute(5, 226, 5), Block.create(BlockId.AIR))));

            // Construct the memorial sign with click events
            currentFunction.addLine(Comment.create("Memorial Sign Logic"));
            currentFunction.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                    .run(SetBlockCommand.create(
                            BlockPos.absolute(0, 222, -5),
                            Block.create(DynamicBlock.wood(WoodType.CHERRY, WoodBlock.WALL_SIGN))
                                    .withState(FacingBlockState.SOUTH)
                                    .withState(WaterloggedState.of(false))
                                    .withData(SignNBT.create()
                                            .waxed(false)
                                            .backText(SignNBT.SignSideNBT.create()
                                                    .messages(TextComponent.text("You have"),
                                                            TextComponent.text("angered"),
                                                            TextComponent.text("the Gods!")))
                                            .frontText(SignNBT.SignSideNBT.create()
                                                    .messages(TextComponent.text("In Rememberance")
                                                                    .click(ClickEvent.runCommand(SummonCommand.create(EntityId.FIREWORK_ROCKET)
                                                                            .pos(Vec3.relative(0, 0, 0))
                                                                            .nbt(FireworkRocketNBT.create()
                                                                                    .glowing(true)
                                                                                    .fireworksItem(FireworksComponent.create()
                                                                                            .addExplosion(FireworksComponent.Explosion.create()
                                                                                                    .shape(FireworksComponent.FireworkShape.STAR)))))),
                                                            TextComponent.text("of our"),
                                                            TextComponent.text("Command Center"),
                                                            TextComponent.text("2014-2025")))))));

            // --- 🚩 Phase 4: Control Point Objectives ---
            if (ControlPointConfig.ENABLED) {
                currentFunction.addLine(Comment.create("=== Control Point Objectives ==="));
                if (ControlPointRegistry.getActivePoints().isEmpty()) {
                    throw new IllegalStateException("Control Point setup failed: ENABLED is true but no active points are registered.");
                }
                for (ControlPointData cp : ControlPointRegistry.getActivePoints()) {
                    Objects.requireNonNull(cp, "Control Point setup failed: Found null ControlPointData in Registry.");
                    currentFunction.addLine(Comment.create("Initializing " + cp.name()));
                    currentFunction.addAll(BossbarCommand.initialize(cp.getBossbar()));
                }
            }

            // Finalize registration
            customNamespace.addComponent(currentFunction);

        } catch (NullPointerException | IllegalStateException e) {
            // Throwing a clear error instead of providing a fallback
            throw new RuntimeException("InitializeFunction registration failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred during initialization registration: " + e.getMessage(), e);
        }
    }
}