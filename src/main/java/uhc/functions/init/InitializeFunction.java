package uhc.functions.init;

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
import uhc.functions.DatapackFunction;
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
import uhc.game.team.TeamData;
import uhc.game.team.TeamRegistry;
import uhc.text.ClickEvent;
import uhc.text.TextComponent;

import java.util.Collection;
import java.util.Objects;

/**
 * 🛠️ **Initialize Function Component**
 * <p>
 * This module is the architect of the UHC match environment. It handles the
 * sequential generation of commands for gamerules, scoreboards, team setup,
 * and physical lobby construction.
 * </p>
 */
public class InitializeFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * Header title for the auto-generated .mcfunction file. */
    private static final String HEADER_TITLE = "UHC Match Initialization: Global Setup";

    /** * Standardized spawn location for the lobby structure. */
    private static final BlockPos SPAWN_POS = BlockPos.absolute(0, 221, 0);

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Entry point for assembling the initialization sequence.
     * <p>
     * <b>Validation:</b> Verifies the registry state of Teams and Control Points.
     * <b>Phase Ordering:</b> Environment -> Infrastructure -> Construction -> Objectives.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for storage (non-null).
     * @throws NullPointerException if infrastructure is missing.
     * @throws RuntimeException     if assembly logic fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {
        Objects.requireNonNull(datapack, "Initialize Error: Datapack cannot be null.");
        Objects.requireNonNull(namespace, "Initialize Error: Namespace cannot be null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.INITIALIZATION,
                "Registry Error: FunctionPath.INITIALIZATION is not defined.");

        final Function currentFunction = new Function(path);

        try {
            // A. Mandatory File Header
            this.appendStandardHeader(currentFunction, HEADER_TITLE);

            // B. Multi-Phase Assembly
            this.assembleEnvironmentLogic(currentFunction);
            this.assembleInfrastructureLogic(currentFunction);
            this.assembleConstructionLogic(currentFunction);

            if (ControlPointConfig.ENABLED) {
                this.assembleControlPointLogic(currentFunction);
            }

            // C. Registration
            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to assemble " + path + ". Reason: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Phase 1: Environment Logic ---

    /**
     * Configures the global Minecraft environment for UHC gameplay.
     * @param function The target function component (non-null).
     */
    private void assembleEnvironmentLogic(Function function) {
        this.addSafeLine(function, "--- Phase 1: Environment Setup ---");

        // Hardcore health regen mechanics
        for (DimensionId dimension : DimensionId.values()) {
            function.addLine(ExecuteCommand.create().in(dimension)
                    .run(GameRuleCommand.set(GameRuleId.NATURAL_REGENERATION, false)));
        }
        function.addLine(GameRuleCommand.set(GameRuleId.DO_IMMEDIATE_RESPAWN, true));
        function.addLine(GameRuleCommand.set(GameRuleId.DO_PATROL_SPAWNING, false));
        function.addLine(GameRuleCommand.set(GameRuleId.DO_MOB_SPAWNING, false));
        function.addLine(GameRuleCommand.set(GameRuleId.DO_WEATHER_CYCLE, false));

        function.addLine(DifficultyCommand.create().difficulty(DifficultyId.HARD));
        function.addLine(GameModeCommand.create(GameModeId.ADVENTURE).setDefault());
        function.addLine(SetWorldSpawnCommand.create().pos(SPAWN_POS));
        this.addSafeLine(function, "");
    }

    // --- 🛠️ Phase 2: Infrastructure Logic ---

    /**
     * Sets up the scoreboard system and initializes all defined teams.
     * @param function The target function component (non-null).
     * @throws IllegalStateException if TeamRegistry is empty.
     */
    private void assembleInfrastructureLogic(Function function) {
        this.addSafeLine(function, "--- Phase 2: Scoreboard & Team Registry ---");
        function.addAll(ScoreboardCommand.batch().registerAll());
        function.addLine(ScoreboardCommand.objectives().setDisplay(DisplaySlot.BELOW_NAME, ScoreboardObjectiveId.HEARTS));

        final Collection<TeamData> allTeams = TeamRegistry.ALL;
        if (allTeams.isEmpty()) {
            throw new IllegalStateException("Infrastructure Error: No teams found in TeamRegistry. UHC requires teams.");
        }

        for (TeamData team : allTeams) {
            Objects.requireNonNull(team, "Team Registry Error: Null team entry detected.");
            function.addAll(TeamCommand.initialize(team));
        }
        this.addSafeLine(function, "");
    }

    // --- 🛠️ Phase 3: Construction Logic ---

    /**
     * Builds the physical lobby and the memorial sign at the world center.
     * @param function The target function component (non-null).
     */
    private void assembleConstructionLogic(Function function) {
        this.addSafeLine(function, "--- Phase 3: Structural Construction ---");

        final Block barrier = Block.create(BlockId.BARRIER);
        final Block air = Block.create(BlockId.AIR);

        // Lobby shell construction
        function.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                .run(FillCommand.create(BlockPos.absolute(-6, 220, -6), BlockPos.absolute(6, 226, 6), barrier)));
        function.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                .run(FillCommand.create(BlockPos.absolute(-5, 221, -5), BlockPos.absolute(5, 226, 5), air)));

        // Decorative sign logic
        this.placeMemorialSign(function);
        this.addSafeLine(function, "");
    }

    /**
     * Internal utility to place the complex interactive Cherry Sign.
     */
    private void placeMemorialSign(Function function) {
        final Block sign = Block.create(DynamicBlock.wood(WoodType.CHERRY, WoodBlock.WALL_SIGN))
                .withState(FacingBlockState.SOUTH)
                .withState(WaterloggedState.of(false))
                .withData(SignNBT.create()
                        .backText(SignNBT.SignSideNBT.create().messages(
                                TextComponent.text("You have"),
                                TextComponent.text("angered"),
                                TextComponent.text("the Gods!")))
                        .frontText(SignNBT.SignSideNBT.create().messages(
                                TextComponent.text("In Remembrance").click(ClickEvent.runCommand(
                                        SummonCommand.entity(EntityId.FIREWORK_ROCKET, Vec3.relative(0, 0, 0)))),
                                TextComponent.text("of our"),
                                TextComponent.text("Command Center"),
                                TextComponent.text("2014-2025"))));

        function.addLine(ExecuteCommand.create().in(DimensionId.OVERWORLD)
                .run(SetBlockCommand.create(BlockPos.absolute(0, 222, -5), sign)));
    }

    // --- 🛠️ Phase 4: Control Point Logic ---

    /**
     * Initializes capture objectives and bossbars if the feature is enabled.
     * @param function The target function component (non-null).
     */
    private void assembleControlPointLogic(Function function) {
        this.addSafeLine(function, "--- Phase 4: Control Point Objectives ---");

        final var points = ControlPointRegistry.getActivePoints();
        if (points.isEmpty()) {
            throw new IllegalStateException("Objective Error: Control Point logic is ENABLED but the registry is empty.");
        }

        for (ControlPointData cp : points) {
            Objects.requireNonNull(cp, "Objective Registry Error: Null ControlPointData detected.");
            function.addAll(BossbarCommand.initialize(cp.getBossbar()));
        }
    }
}