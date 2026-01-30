package uhc.functions.player_death;

import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.time.VariableGameTime;
import uhc.command.Comment;
import uhc.command.commands.*;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.functions.DatapackFunction;
import uhc.resource.color.TextColor;
import uhc.resource.gameplay.GameModeId;
import uhc.resource.sound.SoundId;
import uhc.resource.sound.SoundSource;
import uhc.resource.tag.EntityTag;
import uhc.score.Range;
import uhc.score.ScoreboardObjectiveId;
import uhc.text.TextComponent;

import java.util.Map;
import java.util.Objects;

import static uhc.core.Constants.bannerText;

/**
 * 💀 **Player Death Logic Handler**
 * <p>
 * This class is responsible for generating the {@code handle_player_death.mcfunction}.
 * It orchestrates the transition of a player from an active participant to a
 * spectator, handles conditional early-game respawns, and manages social
 * deduction mechanics.
 * </p>
 */
public class PlayerDeathFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The header title used for the generated .mcfunction file documentation. */
    private static final String FUNCTION_TITLE = "UHC Lifecycle: Player Death Handling";

    /** * A score map representing exactly one death, used frequently in selectors. */
    private static final Map<ScoreboardObjectiveId, Object> DEATH_SCORE = Map.of(ScoreboardObjectiveId.DEATHS, 1);

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Entry point for registering the player death handling sequence.
     * <p>
     * <b>Strict Error Policy:</b> This method utilizes fail-fast validation. If any
     * required path or objective is missing, the build will halt immediately.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for storage (non-null).
     * @throws NullPointerException if infrastructure is missing.
     * @throws RuntimeException     if the assembly of the logic segments fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {
        Objects.requireNonNull(datapack, "PlayerDeath Error: Datapack cannot be null.");
        Objects.requireNonNull(namespace, "PlayerDeath Error: Namespace cannot be null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.HANDLE_PLAYER_DEATH,
                "Registry Error: FunctionPath.HANDLE_PLAYER_DEATH is missing from project constants.");

        final Function deathFunction = new Function(path);

        try {
            this.appendStandardHeader(deathFunction, FUNCTION_TITLE);

            // --- 🔊 Phase 1: Global Notification ---
            deathFunction.addLine(Comment.create("SECTION 1: Global Death Notification (Thunder)"));
            deathFunction.addLine(PlaySoundCommand.create(SoundId.THUNDER)
                    .source(SoundSource.MASTER)
                    .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .pos(Vec3.relative(0, 50, 0))
                    .volume(100));

            // --- 🔄 Phase 2: State Transitions ---
            this.assembleTransitionLogic(deathFunction);

            // --- ♻️ Phase 3: Respawn Checks ---
            this.assembleRespawnLogic(deathFunction);

            // --- 🕵️ Phase 4: Traitor Events ---
            this.assembleTraitorLogic(deathFunction);

            // --- ⚔️ Phase 5: Betrayal Detection ---
            this.assembleBetrayalLogic(deathFunction);

            // --- 🧹 Phase 6: Scoreboard Cleanup ---
            this.assembleCleanupLogic(deathFunction);

            namespace.addComponent(deathFunction);

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to assemble PlayerDeath logic at [" + path + "]. " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Segments ---

    /**
     * Forces deceased players who are not already spectators into Spectator mode.
     * @param function The target function (non-null).
     */
    private void assembleTransitionLogic(Function function) {
        final Entity deceasedNonSpectator = Entity.ofSelector(TargetSelector.ALL_PLAYERS,
                SelectorArgumentsBuilder.create()
                        .scores(DEATH_SCORE)
                        .gamemode(GameModeId.SPECTATOR, true));

        function.addLine(Comment.create("SECTION 2: Spectator Transition"));
        function.addLine(GameModeCommand.create(GameModeId.SPECTATOR).target(deceasedNonSpectator));
    }

    /**
     * Manages early-game respawns and head-drop logic.
     * @param function The target function (non-null).
     * @throws NullPointerException if required paths are missing.
     */
    private void assembleRespawnLogic(Function function) {
        function.addLine(Comment.create("SECTION 3: Early Game Respawn Check"));

        final SelectorArgumentsBuilder deceasedCriteria = SelectorArgumentsBuilder.create().scores(DEATH_SCORE);
        final Entity controller = Entity.ofSelector(TargetSelector.NEAREST_ENTITY,
                SelectorArgumentsBuilder.create().tag(EntityTag.RESPAWN_DISABLED));

        final FunctionPath headPath = Objects.requireNonNull(FunctionPath.DROP_PLAYER_HEADS, "Missing path: DROP_PLAYER_HEADS");
        final FunctionPath respawnPath = Objects.requireNonNull(FunctionPath.RESPAWN_PLAYER, "Missing path: RESPAWN_PLAYER");

        // 1. Tag for respawn if grace period is active
        function.addLine(ExecuteCommand.create()
                .unlessEntity(controller)
                .run(TagCommand.add(Entity.ofSelector(TargetSelector.NEAREST_PLAYER, deceasedCriteria), EntityTag.RESPAWN)));

        // 2. Always drop head
        function.addLine(FunctionCommand.of(headPath));

        // 3. Schedule respawn function if allowed
        function.addLine(ExecuteCommand.create()
                .unlessEntity(controller)
                .run(ScheduleCommand.function(respawnPath, VariableGameTime.tick(5))));
    }

    /**
     * Reveals and announces the death of Traitor faction members.
     * @param function The target function (non-null).
     */
    private void assembleTraitorLogic(Function function) {
        function.addLine(Comment.create("SECTION 4: Traitor Elimination Events"));

        final Entity deadTraitor = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create().scores(DEATH_SCORE).tag(EntityTag.TRAITOR));

        final TextComponent announcement = TextComponent.load(bannerText)
                .append(TextComponent.text("A TRAITOR HAS BEEN ELIMINATED").color(TextColor.RED).bold(true))
                .append(TextComponent.load(bannerText));

        function.addLine(ExecuteCommand.create()
                .ifEntity(deadTraitor)
                .run(TellrawCommand.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS), announcement)));
    }

    /**
     * Detects betrayal/team-killing and warns the offender.
     * @param function The target function (non-null).
     */
    private void assembleBetrayalLogic(Function function) {
        function.addLine(Comment.create("SECTION 5: Team Kill Detection"));

        final Entity killer = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create().team(true).scores(Map.of(ScoreboardObjectiveId.TEMP_KILLS, 1)));

        final Entity victim = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create().team(true).scores(DEATH_SCORE));

        // Note: Using 'team(true)' ensures we only detect betrayal between players with the same team
        function.addLine(ExecuteCommand.create()
                .ifEntity(killer).ifEntity(victim)
                .unlessScore(killer, ScoreboardObjectiveId.IS_KILLER, Range.exact(1))
                .run(TellrawCommand.create(killer, TextComponent.text("Betrayal! You killed a teammate.").color(TextColor.RED).bold(true))));

        function.addLine(ExecuteCommand.create()
                .ifEntity(killer).ifEntity(victim)
                .run(ScoreboardCommand.setScore(killer, ScoreboardObjectiveId.IS_KILLER, 1)));
    }

    /**
     * Cleans up transient scores to prevent infinite logic loops.
     * @param function The target function (non-null).
     */
    private void assembleCleanupLogic(Function function) {
        function.addLine(Comment.create("SECTION 6: Scoreboard Reset"));

        final Entity killTrigger = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.TEMP_KILLS, 1)));

        final Entity deathTrigger = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create().scores(DEATH_SCORE));

        function.addLine(ScoreboardCommand.reset(killTrigger, ScoreboardObjectiveId.TEMP_KILLS));
        function.addLine(ScoreboardCommand.reset(deathTrigger, ScoreboardObjectiveId.DEATHS));
    }
}