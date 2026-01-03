package uhc.functions;

import uhc.arguments.coordinate.Vec3;
import uhc.arguments.entity.Entity;
import uhc.arguments.entity.SelectorArgumentsBuilder;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.time.VariableGameTime;
import uhc.command.commands.*;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
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
 * deduction mechanics (Traitor announcements).
 * </p>
 * <p>
 * <b>Logic Flow:</b>
 * <ol>
 * <li>Plays global atmospheric sound effects.</li>
 * <li>Forces the deceased player into Spectator mode.</li>
 * <li>Tags and schedules respawns if the grace period is active.</li>
 * <li>Identifies and reveals Traitors upon death.</li>
 * <li>Detects and warns players about Team-Killing/Betrayal.</li>
 * <li>Cleans up scoreboard triggers to prevent logic loops.</li>
 * </ol>
 * </p>
 */
public class PlayerDeathFunction implements DatapackFunction {

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Entry point for registering the player death handling sequence.
     * <p>
     * <b>Strict Error Policy:</b> This method utilizes fail-fast validation. If any
     * required {@link FunctionPath} or {@link ScoreboardObjectiveId} is missing
     * from the project registry, the build will halt immediately with a clear error.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for component storage (non-null).
     * @throws NullPointerException if {@code datapack}, {@code namespace}, or required
     * internal constants are missing.
     * @throws RuntimeException     if the assembly of the logic segments fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Parameter Validation ---
        Objects.requireNonNull(datapack, "PlayerDeath Error: Datapack cannot be null.");
        Objects.requireNonNull(namespace, "PlayerDeath Error: Namespace cannot be null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.HANDLE_PLAYER_DEATH,
                "Registry Error: FunctionPath.HANDLE_PLAYER_DEATH constant is missing.");

        final Function deathFunction = new Function(path);

        try {
            // Header generation via inherited interface method
            this.appendStandardHeader(deathFunction, "UHC Lifecycle: Player Death Handling");

            // --- 🔊 Phase 1: Audio/Visual Effects ---
            deathFunction.addLine(Comment.create("SECTION 1: Global Death Notification (Thunder)"));
            deathFunction.addLine(PlaySoundCommand.create(SoundId.THUNDER)
                    .source(SoundSource.MASTER)
                    .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .pos(Vec3.relative(0, 50, 0))
                    .volume(100));

            // --- 🔄 Phase 2: Game State Transitions ---
            deathFunction.addLine(Comment.create("SECTION 2: Spectator Transition"));
            deathFunction.addLine(GameModeCommand.create(GameModeId.SPECTATOR)
                    .target(Entity.ofSelector(
                            TargetSelector.ALL_PLAYERS,
                            SelectorArgumentsBuilder.create()
                                    .scores(Map.of(ScoreboardObjectiveId.DEATHS, 1))
                                    .gamemode(GameModeId.SPECTATOR, true))));

            // --- ♻️ Phase 3: Conditional Respawn Management ---
            deathFunction.addLine(Comment.create("SECTION 3: Early Game Respawn Check"));
            this.assembleRespawnLogic(deathFunction);

            // --- 🕵️ Phase 4: Faction & Traitor Events ---
            deathFunction.addLine(Comment.create("SECTION 4: Traitor Elimination Events"));
            this.assembleTraitorLogic(deathFunction);

            // --- ⚔️ Phase 5: Team & Betrayal Tracking ---
            deathFunction.addLine(Comment.create("SECTION 5: Team Kill / Betrayal Detection"));
            this.assembleBetrayalLogic(deathFunction);

            // --- 🧹 Phase 6: Scoreboard Cleanup ---
            deathFunction.addLine(Comment.create("SECTION 6: Scoreboard Reset"));
            this.assembleCleanupLogic(deathFunction);

            // Final Registration
            namespace.addComponent(deathFunction);

        } catch (Exception e) {
            // Contextual wrap for build-time errors
            throw new RuntimeException("CRITICAL: Failed to assemble PlayerDeath logic at ["
                    + path + "]. Details: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Segments ---

    /**
     * Handles the tagging and scheduling of respawn tasks.
     * <p>
     * If the {@code RESPAWN_DISABLED} tag is absent from the nearest entity
     * (the global controller), the deceased player is tagged for a 5-tick
     * delayed respawn.
     * </p>
     *
     * @param function The function being modified.
     * @throws NullPointerException if FunctionPath.RESPAWN_PLAYER is null.
     */
    private void assembleRespawnLogic(Function function) {
        SelectorArgumentsBuilder deathScore = SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.DEATHS, 1));

        function.addLine(ExecuteCommand.create()
                .unlessEntity(Entity.ofSelector(TargetSelector.NEAREST_ENTITY, SelectorArgumentsBuilder.create().tag(EntityTag.RESPAWN_DISABLED)))
                .run(TagCommand.target(Entity.ofSelector(TargetSelector.NEAREST_PLAYER, deathScore)).add(EntityTag.RESPAWN)));

        function.addLine(FunctionCommand.create(Objects.requireNonNull(FunctionPath.DROP_PLAYER_HEADS, "Missing Path: DROP_PLAYER_HEADS")));

        function.addLine(ExecuteCommand.create()
                .unlessEntity(Entity.ofSelector(TargetSelector.NEAREST_ENTITY, SelectorArgumentsBuilder.create().tag(EntityTag.RESPAWN_DISABLED)))
                .run(ScheduleCommand.function(Objects.requireNonNull(FunctionPath.RESPAWN_PLAYER, "Missing Path: RESPAWN_PLAYER"), VariableGameTime.tick(5))));
    }

    /**
     * Logic for identifying and announcing the death of a "Traitor" player.
     * * @param function The function being modified.
     */
    private void assembleTraitorLogic(Function function) {
        function.addLine(ExecuteCommand.create()
                .ifEntity(Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                        SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.DEATHS, 1)).tag(EntityTag.TRAITOR)))
                .run(TellrawCommand.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS),
                        TextComponent.load(bannerText)
                                .append(TextComponent.text("A TRAITOR HAS BEEN ELIMINATED").color(TextColor.RED).bold(true))
                                .append(TextComponent.load(bannerText)))));
    }

    /**
     * Detects if the player who gained a kill is on the same team as the deceased.
     * <p>
     * If friendly fire is detected, a warning is sent to the killer and a flag
     * (IS_KILLER) is set to ensure the logic doesn't re-trigger.
     * </p>
     *
     * @param function The function being modified.
     */
    private void assembleBetrayalLogic(Function function) {
        Entity killer = Entity.ofSelector(TargetSelector.NEAREST_PLAYER, SelectorArgumentsBuilder.create().team(true).scores(Map.of(ScoreboardObjectiveId.TEMP_KILLS, 1)));
        Entity victim = Entity.ofSelector(TargetSelector.NEAREST_PLAYER, SelectorArgumentsBuilder.create().team(true).scores(Map.of(ScoreboardObjectiveId.DEATHS, 1)));

        function.addLine(ExecuteCommand.create()
                .ifEntity(killer).ifEntity(victim)
                .unlessScore(killer, ScoreboardObjectiveId.IS_KILLER, Range.exact(1))
                .run(TellrawCommand.create(killer, TextComponent.text("Looks like you do not want a teammate.").color(TextColor.RED).bold(true))));

        function.addLine(ExecuteCommand.create()
                .ifEntity(killer).ifEntity(victim)
                .run(ScoreboardCommand.players().set(ScoreboardObjectiveId.IS_KILLER, 1).target(killer)));
    }

    /**
     * Resets temporary scoreboard objectives.
     * <p>
     * Critical for preventing the death logic from running every tick while
     * a player is in the death screen.
     * </p>
     *
     * @param function The function being modified.
     */
    private void assembleCleanupLogic(Function function) {
        function.addLine(ScoreboardCommand.players().reset(ScoreboardObjectiveId.TEMP_KILLS)
                .target(Entity.ofSelector(TargetSelector.NEAREST_PLAYER, SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.TEMP_KILLS, 1)))));
        function.addLine(ScoreboardCommand.players().reset(ScoreboardObjectiveId.DEATHS)
                .target(Entity.ofSelector(TargetSelector.NEAREST_PLAYER, SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.DEATHS, 1)))));
    }
}