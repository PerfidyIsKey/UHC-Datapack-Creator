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
 * This class orchestrates the comprehensive setup of a UHC match environment.
 * It is responsible for generating the {@code initialization.mcfunction} file which
 * handles gamerules, difficulty, scoreboard registration, team creation, and the
 * physical construction of the lobby/staging area.
 * </p>
 */
public class InitializeFunction implements DatapackFunction {

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Executes the multi-phase registration of the UHC initialization logic.
     * <p>
     * <b>Strict Error Policy:</b> This method validates the state of {@link TeamRegistry}
     * and {@link ControlPointRegistry} before assembly. If critical data is missing,
     * it throws a {@link RuntimeException} to prevent a broken game state.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for the initialization logic (non-null).
     * @throws NullPointerException if parameters are null.
     * @throws RuntimeException     if any phase (Environment, Infrastructure, Construction) fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {
        // 1. Parameter Validation
        Objects.requireNonNull(datapack, "Initialize Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "Initialize Error: Target Namespace cannot be null.");

        // Ensure the function path is registered in the enum constants
        final FunctionPath functionPath = Objects.requireNonNull(FunctionPath.INITIALIZATION,
                "Registry Error: FunctionPath.INITIALIZATION is not defined in the system.");

        final Function currentFunction = new Function(functionPath);

        try {
            // Header generation via inherited interface method
            this.appendStandardHeader(currentFunction, "UHC Match Initialization: Global Setup");

            // --- ⚙️ Phase 1: Environment & Game Rules ---
            this.assembleEnvironmentLogic(currentFunction);

            // --- 📊 Phase 2: Infrastructure (Scoreboards & Teams) ---
            this.assembleInfrastructureLogic(currentFunction);

            // --- 🏗️ Phase 3: World Construction (Lobby & Memorial) ---
            this.assembleConstructionLogic(currentFunction);

            // --- 🚩 Phase 4: Objectives (Control Points) ---
            if (ControlPointConfig.ENABLED) {
                this.assembleControlPointLogic(currentFunction);
            }

            // Register finalized component into the namespace
            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            // Contextual wrap for build-time errors
            throw new RuntimeException("CRITICAL: Initialization assembly failed at path ["
                    + functionPath + "]. Details: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Assembly Phases ---

    /**
     * Configures dimensions, gamerules, difficulty, and spawn parameters.
     * * @param function The function component being assembled.
     * @throws RuntimeException if command generation fails.
     */
    private void assembleEnvironmentLogic(Function function) {
        function.addLine(Comment.create("--- Phase 1: Environment Setup ---"));

        // Disable health regen across all dimensions for UHC mechanics
        for (DimensionId dimension : DimensionId.values()) {
            function.addLine(ExecuteCommand.create().in(dimension)
                    .run(GameRuleCommand.create(GameRuleId.NATURAL_REGENERATION).booleanValue(false)));
        }

        function.addLine(DifficultyCommand.create().difficulty(DifficultyId.HARD));
        function.addLine(GameModeCommand.create(GameModeId.ADVENTURE).setDefault());
        function.addLine(SetWorldSpawnCommand.create().pos(BlockPos.absolute(0, 221, 0)));
        function.addLine(Comment.create(" "));
    }

    /**
     * Registers all scoreboard objectives and team data.
     * * @param function The function component being assembled.
     * @throws IllegalStateException if TeamRegistry is empty.
     */
    private void assembleInfrastructureLogic(Function function) {
        function.addLine(Comment.create("--- Phase 2: Scoreboard & Team Registry ---"));
        function.addAll(ScoreboardCommand.batch().registerAll());
        function.addLine(ScoreboardCommand.objectives().setDisplay(DisplaySlot.BELOW_NAME, ScoreboardObjectiveId.HEARTS));

        // Strict Check: Cannot start a UHC without teams
        if (TeamRegistry.ALL.isEmpty()) {
            throw new IllegalStateException("Infrastructure Error: TeamRegistry is empty. Define teams before initializing.");
        }

        for (TeamData team : TeamRegistry.ALL) {
            function.addAll(TeamCommand.initialize(Objects.requireNonNull(team, "Found null team in TeamRegistry")));
        }
        function.addLine(Comment.create(" "));
    }

    /**
     * Generates physical barrier structures and the interactive memorial sign.
     * * @param function The function component being assembled.
     */
    private void assembleConstructionLogic(Function function) {
        function.addLine(Comment.create("--- Phase 3: Structural Construction ---"));

        // Build Barrier Box (Shell and Air Clearing)
        function.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                .run(FillCommand.create(BlockPos.absolute(-6, 220, -6), BlockPos.absolute(6, 226, 6), Block.create(BlockId.BARRIER))));
        function.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                .run(FillCommand.create(BlockPos.absolute(-5, 221, -5), BlockPos.absolute(5, 226, 5), Block.create(BlockId.AIR))));

        // Memorial Sign with interactive ClickEvents
        function.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                .run(SetBlockCommand.create(BlockPos.absolute(0, 222, -5),
                        Block.create(DynamicBlock.wood(WoodType.CHERRY, WoodBlock.WALL_SIGN))
                                .withState(FacingBlockState.SOUTH)
                                .withState(WaterloggedState.of(false))
                                .withData(SignNBT.create()
                                        .backText(SignNBT.SignSideNBT.create().messages(
                                                TextComponent.text("You have"),
                                                TextComponent.text("angered"),
                                                TextComponent.text("the Gods!")))
                                        .frontText(SignNBT.SignSideNBT.create().messages(
                                                TextComponent.text("In Remembrance").click(ClickEvent.runCommand(
                                                        SummonCommand.create(EntityId.FIREWORK_ROCKET).pos(Vec3.relative(0, 0, 0)))),
                                                TextComponent.text("of our"),
                                                TextComponent.text("Command Center"),
                                                TextComponent.text("2014-2025")))))));
        function.addLine(Comment.create(" "));
    }

    /**
     * Initializes bossbars for active control points if enabled in config.
     * * @param function The function component being assembled.
     * @throws IllegalStateException if config is enabled but registry is empty.
     */
    private void assembleControlPointLogic(Function function) {
        function.addLine(Comment.create("--- Phase 4: Control Point Objectives ---"));

        var points = ControlPointRegistry.getActivePoints();
        if (points.isEmpty()) {
            throw new IllegalStateException("Control Point Error: Config is ENABLED but no points are registered in ControlPointRegistry.");
        }

        for (ControlPointData cp : points) {
            function.addAll(BossbarCommand.initialize(Objects.requireNonNull(cp, "Found null ControlPointData").getBossbar()));
        }
    }
}