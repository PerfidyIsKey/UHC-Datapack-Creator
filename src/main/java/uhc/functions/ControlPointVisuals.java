package uhc.functions;

import uhc.arguments.block.Block;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.command.commands.*;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.game.control_points.ControlPointRegistry;
import uhc.game.team.TeamData;
import uhc.game.team.TeamRegistry;
import uhc.resource.block.BlockId;
import uhc.resource.block.ColorableBlock;
import uhc.resource.block.DynamicBlock;
import uhc.resource.tag.EntityTag;
import uhc.score.Range;
import uhc.score.ScoreboardObjectiveId;

import java.util.Objects;

import static uhc.core.Constants.admin;

/**
 * 🎨 **Control Point Visuals Function**
 * <p>
 * This class orchestrates the generation of visual feedback for the Capture Point system.
 * It manages bossbar synchronization, team-colored waypoints, and block-based state
 * changes (Beacon/Glass).
 * </p>
 * <p>
 * <b>Logic Flow:</b>
 * <ol>
 * <li>Synchronizes capture progress with the bossbar UI.</li>
 * <li>Iterates through all teams to apply conditional color schemes.</li>
 * <li>Maintains the physical beacon infrastructure and beam.</li>
 * </ol>
 * </p>
 */
public class ControlPointVisuals implements DatapackFunction {

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Entry point for registering the Control Point visual update sequence.
     * <p>
     * <b>Strict Error Policy:</b> Utilizing fail-fast validation. If the registry
     * contains no active points or teams, the build will halt to prevent
     * generating broken datapack logic.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for component storage (non-null).
     * @throws NullPointerException  if parameters are missing.
     * @throws IllegalStateException if the ControlPointRegistry is empty.
     * @throws RuntimeException      if logic assembly fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Parameter & Registry Validation ---
        Objects.requireNonNull(datapack, "Visuals Error: Datapack cannot be null.");
        Objects.requireNonNull(namespace, "Visuals Error: Namespace cannot be null.");

        if (ControlPointRegistry.getActivePoints().isEmpty()) {
            throw new IllegalStateException("Registry Error: No active Control Points found in registry.");
        }

        final FunctionPath path = Objects.requireNonNull(FunctionPath.CONTROL_POINT_VISUALS_1,
                "Registry Error: FunctionPath.CONTROL_POINT_VISUALS_1 is missing.");

        final Function visualsFunction = new Function(path);

        try {
            // Header generation via inherited interface method
            this.appendStandardHeader(visualsFunction, "Control Point: Visual & Hardware Maintenance");

            // --- 📊 Phase 1: Bossbar Progress ---
            visualsFunction.addLine(Comment.create("SECTION 1: UI Progress Synchronization"));
            this.assembleBossbarLogic(visualsFunction);
            visualsFunction.addLine(Comment.create(" "));

            // --- 🎨 Phase 2: Team Dynamic Visuals ---
            visualsFunction.addLine(Comment.create("SECTION 2: Team-Specific Color Branding"));
            this.assembleTeamLogic(visualsFunction);
            visualsFunction.addLine(Comment.create(" "));

            // --- 🗼 Phase 3: Beacon Hardware ---
            visualsFunction.addLine(Comment.create("SECTION 3: Physical Infrastructure & Beam"));
            this.assembleBeaconLogic(visualsFunction);

            // Final Registration
            namespace.addComponent(visualsFunction);

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to assemble Control Point visuals at ["
                    + path + "]. Details: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Segments ---

    /**
     * Maps the capture progress scoreboard value to the Bossbar UI.
     * * @param function The function being modified.
     */
    private void assembleBossbarLogic(Function function) {
        var firstPoint = ControlPointRegistry.getActivePoints().get(0);

        function.addLine(ExecuteCommand.create()
                .storeBossbar(
                        ExecuteCommand.StoreType.RESULT,
                        firstPoint.getBossbar(),
                        ExecuteCommand.BossbarValueType.VALUE
                )
                .run(ScoreboardCommand.players().get(admin, ScoreboardObjectiveId.DISPLAY_CP)));
    }

    /**
     * Generates conditional logic for every registered team to update colors.
     * * @param function The function being modified.
     */
    private void assembleTeamLogic(Function function) {
        var firstPoint = ControlPointRegistry.getActivePoints().get(0);
        final EntityTag pointTag = firstPoint.name();

        for (TeamData team : TeamRegistry.ALL) {
            Objects.requireNonNull(team, "Registry Error: Encountered null TeamData.");

            function.addLine(Comment.create("Applying visuals for Team: " + team.title()));

            // A. Bossbar Color Update
            function.addLine(ExecuteCommand.create()
                    .ifScore(admin, ScoreboardObjectiveId.COLOR_CP, Range.exact(team.getId()))
                    .run(BossbarCommand.set(firstPoint.getBossbar().getId())
                            .color(team.getBossbarColor())));

            // B. Waypoint/Entity Branding
            Entity waypoint = Entity.ofSelector(TargetSelector.NEAREST_ENTITY,
                    SelectorArgumentsBuilder.create().tag(pointTag));

            function.addLine(ExecuteCommand.create()
                    .ifScore(admin, ScoreboardObjectiveId.COLOR_CP, Range.exact(team.getId()))
                    .run(WaypointCommand.create(waypoint).color(team.getTextColor())));

            // C. Capture Block Update (Glass)
            Block coloredGlass = Block.create(DynamicBlock.color(team.getDyeColor(), ColorableBlock.STAINED_GLASS));

            function.addLine(ExecuteCommand.create()
                    .ifScore(admin, ScoreboardObjectiveId.COLOR_CP, Range.exact(team.getId()))
                    .in(firstPoint.getDimension())
                    .run(SetBlockCommand.create(firstPoint.getPos(), coloredGlass)));
        }
    }

    /**
     * Manages the physical beacon blocks and calls external protection functions.
     * * @param function The function being modified.
     */
    private void assembleBeaconLogic(Function function) {
        var firstPoint = ControlPointRegistry.getActivePoints().get(0);
        var centerPos = firstPoint.getPos();

        // Ensure Emerald Base (3x1x3)
        function.addLine(ExecuteCommand.create()
                .in(firstPoint.getDimension())
                .run(FillCommand.create(
                        centerPos.getPosRelative(-1, -1, -1),
                        centerPos.getPosRelative(1, -1, 1),
                        Block.create(BlockId.EMERALD_BLOCK))));

        // Set Beacon block
        function.addLine(ExecuteCommand.create()
                .in(firstPoint.getDimension())
                .run(SetBlockCommand.create(centerPos, Block.create(BlockId.BEACON))));

        // External Beam/NBT Protection
        function.addLine(FunctionCommand.create(Objects.requireNonNull(FunctionPath.PROTECT_BEACON_1,
                "Missing Path: PROTECT_BEACON_1")));
    }
}