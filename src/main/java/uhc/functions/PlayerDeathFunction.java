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

import static uhc.core.Constants.admin;
import static uhc.core.Constants.bannerText;

/**
 * 💀 **Player Death Logic Handler**
 * <p>
 * This class registers the {@code handle_player_death.mcfunction} which manages
 * game-state transitions after a player dies.
 * </p>
 * <p>
 * <b>Logic Flow:</b>
 * <ol>
 * <li>Plays ambient death sounds (Thunder).</li>
 * <li>Moves dead players to Spectator mode.</li>
 * <li>Handles conditional early-game respawns.</li>
 * <li>Tracks traitor eliminations and team betrayal logic.</li>
 * <li>Cleans up temporary scoreboard values.</li>
 * </ol>
 * </p>
 */
public class PlayerDeathFunction implements DatapackFunction {

    /**
     * Registers the death handling logic into the provided datapack.
     * * @param datapack The {@link Datapack} instance where this function will be housed.
     * @param customNamespaceName The string name of the namespace to use.
     * @throws IllegalArgumentException if the datapack or namespace name is invalid.
     * @throws IllegalStateException if the namespace cannot be retrieved or components fail to build.
     */
    @Override
    public void register(Datapack datapack, String customNamespaceName) {

        // --- 🛡️ 1. Validation & Namespace Resolution ---

        Objects.requireNonNull(datapack, "Registration failed: Datapack reference cannot be null.");
        Objects.requireNonNull(customNamespaceName, "Registration failed: Namespace name cannot be null.");

        if (customNamespaceName.isBlank()) {
            throw new IllegalArgumentException("Registration failed: Namespace name cannot be empty.");
        }

        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);
        if (customNamespace == null) {
            throw new IllegalStateException("Critical Error: Could not resolve namespace '" + customNamespaceName + "'.");
        }

        // --- 🏗️ 2. Function Initialization ---

        FunctionPath functionPath = FunctionPath.HANDLE_PLAYER_DEATH;
        Objects.requireNonNull(functionPath, "Critical Error: FunctionPath.HANDLE_PLAYER_DEATH is undefined.");

        Function currentFunction = new Function(functionPath);

        try {
            // --- 🔊 3. Audio/Visual Effects ---

            currentFunction.addLine(Comment.create("--- Audio/Visual Effects ---"));
            currentFunction.addLine(PlaySoundCommand.create(SoundId.THUNDER)
                    .source(SoundSource.MASTER)
                    .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                    .pos(Vec3.relative(0, 50, 0))
                    .volume(100));

            // --- 🔄 4. State Transitions ---

            currentFunction.addLine(Comment.create("--- State Transitions ---"));
            currentFunction.addLine(GameModeCommand.create(GameModeId.SPECTATOR)
                    .target(Entity.ofSelector(
                            TargetSelector.ALL_PLAYERS,
                            SelectorArgumentsBuilder.create()
                                    .scores(Map.of(ScoreboardObjectiveId.DEATHS, 1))
                                    .gamemode(GameModeId.SPECTATOR, true))));

            currentFunction.addLine(ScoreboardCommand.players().set(ScoreboardObjectiveId.MIN_HEALTH, 20)
                    .target(admin));

            // --- ♻️ 5. Early Game Respawn Logic ---

            currentFunction.addLine(Comment.create("--- Early Game Respawn Logic ---"));

            // Tag player for respawn if RESPAWN_DISABLED marker is absent
            currentFunction.addLine(ExecuteCommand.create()
                    .unlessEntity(Entity.ofSelector(
                            TargetSelector.NEAREST_ENTITY,
                            SelectorArgumentsBuilder.create().tag(EntityTag.RESPAWN_DISABLED)))
                    .run(TagCommand.target(Entity.ofSelector(
                                    TargetSelector.NEAREST_PLAYER,
                                    SelectorArgumentsBuilder.create()
                                            .scores(Map.of(ScoreboardObjectiveId.DEATHS, 1))))
                            .add(EntityTag.RESPAWN)));

            currentFunction.addLine(FunctionCommand.create(FunctionPath.DROP_PLAYER_HEADS));

            // Schedule the respawn function
            currentFunction.addLine(ExecuteCommand.create()
                    .unlessEntity(Entity.ofSelector(
                            TargetSelector.NEAREST_ENTITY,
                            SelectorArgumentsBuilder.create().tag(EntityTag.RESPAWN_DISABLED)))
                    .run(ScheduleCommand.function(FunctionPath.RESPAWN_PLAYER,
                            VariableGameTime.tick(5))));

            // --- 🕵️ 6. Faction & Traitor Events ---

            currentFunction.addLine(Comment.create("--- Faction & Traitor Events ---"));
            currentFunction.addLine(ExecuteCommand.create()
                    .ifEntity(Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                            SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.DEATHS, 1))
                                    .tag(EntityTag.TRAITOR)))
                    .run(TellrawCommand.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS),
                            TextComponent.load(bannerText)
                                    .append(TextComponent.text("A TRAITOR HAS BEEN ELIMINATED").color(TextColor.RED).bold(true))
                                    .append(TextComponent.load(bannerText))
                                    .append(TextComponent.text("WELL DONE").color(TextColor.GOLD).bold(true))
                                    .append(TextComponent.load(bannerText)))));

            // --- ⚔️ 7. Team & Killer Tracking ---

            currentFunction.addLine(Comment.create("--- Team & Killer Tracking ---"));

            Entity killer = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                    SelectorArgumentsBuilder.create().team(true).scores(Map.of(ScoreboardObjectiveId.TEMP_KILLS, 1)));
            Entity dead = Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                    SelectorArgumentsBuilder.create().team(true).scores(Map.of(ScoreboardObjectiveId.DEATHS, 1)));

            // Betrayal check: Notify killer if they killed a teammate
            currentFunction.addLine(ExecuteCommand.create()
                    .ifEntity(killer).ifEntity(dead)
                    .unlessScore(killer, ScoreboardObjectiveId.IS_KILLER, Range.exact(1))
                    .run(TellrawCommand.create(killer,
                            TextComponent.text("Looks like you do not want a teammate.").color(TextColor.RED).bold(true))));

            // Mark killer so betrayal message only triggers once
            currentFunction.addLine(ExecuteCommand.create()
                    .ifEntity(killer).ifEntity(dead)
                    .run(ScoreboardCommand.players().set(ScoreboardObjectiveId.IS_KILLER, 1).target(killer)));

            // --- 🧹 8. Cleanup Scoreboards ---

            currentFunction.addLine(Comment.create("--- Cleanup Scoreboards ---"));

            currentFunction.addLine(ScoreboardCommand.players()
                    .reset(ScoreboardObjectiveId.TEMP_KILLS)
                    .target(Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                            SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.TEMP_KILLS, 1)))));

            currentFunction.addLine(ScoreboardCommand.players()
                    .reset(ScoreboardObjectiveId.DEATHS)
                    .target(Entity.ofSelector(TargetSelector.NEAREST_PLAYER,
                            SelectorArgumentsBuilder.create().scores(Map.of(ScoreboardObjectiveId.DEATHS, 1)))));

            // --- 💾 9. Final Registration ---

            customNamespace.addComponent(currentFunction);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to build PlayerDeathFunction logic: " + e.getMessage(), e);
        }
    }
}