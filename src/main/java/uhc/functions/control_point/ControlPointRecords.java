package uhc.functions.control_point;

import uhc.command.commands.ExecuteCommand;
import uhc.command.commands.ScoreboardCommand;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.functions.DatapackFunction;
import uhc.game.team.TeamData;
import uhc.game.team.TeamRegistry;
import uhc.score.ComparatorType;
import uhc.score.OperationType;
import uhc.score.Range;
import uhc.score.ScoreboardObjectiveId;

import java.util.Objects;

import static uhc.core.Constants.admin;

/**
 * 🏰 **Control Point Records Processor**
 * <p>
 * This class handles the registration of the logic responsible for tracking
 * which team currently holds the highest score on a control point.
 * </p>
 * <p>
 * <b>Implementation:</b> Uses inherited utilities from {@link DatapackFunction}
 * to ensure standardized file headers and safe command injection.
 * </p>
 */
public class ControlPointRecords implements DatapackFunction {

    // --- 📄 Fields ---

    /** * The title used for the standardized header generation.
     * This value is passed to the default interface method for formatting.
     */
    private static final String FUNCTION_TITLE = "Control Point High Score Records";

    // --- 🏗️ Methods ---

    /**
     * Registers the record-tracking logic using the standardized {@link DatapackFunction} contract.
     * <p>
     * This method iterates through all registered teams and generates the necessary
     * {@code /execute} chains to maintain a global high score on the {@code admin} entity.
     * </p>
     * * @param datapack  The global {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for component storage (non-null).
     * @throws NullPointerException if any required context or team data is missing.
     * @throws IllegalStateException if the TeamRegistry is uninitialized.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 📂 1. Context & Path Validation ---

        Objects.requireNonNull(datapack, "ControlPoint Registration Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "ControlPoint Registration Error: Target Namespace cannot be null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.CONTROL_POINT_UPDATE_RECORDS,
                "Registry Error: FunctionPath.CONTROL_POINT_UPDATE_RECORDS is missing from the system definitions.");

        final Function currentFunction = new Function(path);

        // --- 📝 2. Header Generation ---

        // Utilizing the interface default method to inject the standardized header block
        this.appendStandardHeader(currentFunction, FUNCTION_TITLE);

        // --- ⚔️ 3. Logic Assembly ---

        if (TeamRegistry.ALL == null) {
            throw new IllegalStateException("ControlPoint Generation Error: TeamRegistry.ALL is null. Cannot proceed without team definitions.");
        }

        for (TeamData team : TeamRegistry.ALL) {
            // Strict non-null enforcement for team data
            Objects.requireNonNull(team, "ControlPoint Logic Error: A null team entry was encountered in the registry.");
            Objects.requireNonNull(team.entity(), "ControlPoint Logic Error: Team '" + team.getId() + "' has no valid entity associated with it.");

            // Documentation for the specific team block using the safe line utility
            this.addSafeLine(currentFunction, "Processing Record for Team: " + team.getId());

            try {
                /*
                 * Logic Branch A: Update Leading Team Identifier
                 * Updates the 'COLOR_CP' score on the admin entity to match the current team's unique ID.
                 */
                this.addSafeLine(currentFunction, ExecuteCommand.create()
                        .ifScore(team.entity(), ScoreboardObjectiveId.ON_CP, Range.min(1))
                        .ifScore(team.entity(), ScoreboardObjectiveId.CP_SCORE, ComparatorType.GREATER, admin, ScoreboardObjectiveId.DISPLAY_CP)
                        .run(ScoreboardCommand.setScore(admin, ScoreboardObjectiveId.COLOR_CP, team.getId())));

                /*
                 * Logic Branch B: Synchronize Numeric High Score
                 * Performs a scoreboard operation to copy the team's score to the global DISPLAY_CP objective.
                 */
                this.addSafeLine(currentFunction, ExecuteCommand.create()
                        .ifScore(team.entity(), ScoreboardObjectiveId.ON_CP, Range.min(1))
                        .ifScore(team.entity(), ScoreboardObjectiveId.CP_SCORE, ComparatorType.GREATER, admin, ScoreboardObjectiveId.DISPLAY_CP)
                        .run(ScoreboardCommand.operation(admin, ScoreboardObjectiveId.DISPLAY_CP, OperationType.ASSIGNMENT, team.entity(), ScoreboardObjectiveId.CP_SCORE)));

                // Adding visual whitespace between team blocks for easier debugging in the .mcfunction file
                this.addSafeLine(currentFunction, "");

            } catch (Exception e) {
                // Capturing and rethrowing assembly errors with high-context messages
                throw new RuntimeException("CRITICAL: Failed to assemble records logic for team [" + team.getId() + "]. " + e.getMessage(), e);
            }
        }

        // --- 🚀 4. Finalization ---

        try {
            namespace.addComponent(currentFunction);
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to register compiled function [" + path + "] into namespace: " + namespace.getName(), e);
        }
    }
}