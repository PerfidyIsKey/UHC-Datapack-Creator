package uhc.functions;

import Enums.Dimension;
import Enums.Objective;
import Enums.ScoreboardLocation;
import HelperClasses.Execute;
import HelperClasses.ScoreboardObjectiveOld;
import HelperClasses.Team;
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
import uhc.resource.block.BlockId;
import uhc.resource.block.DynamicBlock;
import uhc.resource.block.WoodBlock;
import uhc.resource.block.WoodType;
import uhc.resource.dimension.DimensionId;
import uhc.resource.entity.EntityId;
import uhc.resource.gameplay.DifficultyId;
import uhc.resource.gameplay.GameModeId;
import uhc.resource.gameplay.GameRuleId;
import uhc.text.ClickEvent;
import uhc.text.TextComponent;

public class InitializeFunction implements DatapackFunction {


    @Override
    public void register(Datapack datapack, String customNamespaceName) {
        // --- 1. Parameter Validation ---
        if (datapack == null) {
            throw new IllegalArgumentException("The Datapack object cannot be null during function registration.");
        }
        if (customNamespaceName == null || customNamespaceName.isBlank()) {
            throw new IllegalArgumentException("The custom namespace name cannot be null or blank.");
        }

        // --- 2. Get the required namespace ---
        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);

        // Safety Check: Verify successful creation/retrieval of required namespace
        if (customNamespace == null) {
            // Updated error message to reflect the class name
            throw new IllegalStateException("Failed to obtain the necessary custom namespace ('" + customNamespaceName + "'). Cannot register ClearEnderChestFunction components.");
        }


        // --- 3. Create the Function component ---
        // Function path example: data/{customNamespaceName}/function/util/clear_enderchest.mcfunction
        FunctionPath functionPath = FunctionPath.INITIALIZATION;
        Function currentFunction = new Function(functionPath);

        /* Set gamerules */
        // Set natural health regeneration to false in all dimensions
        for (DimensionId dimension : DimensionId.values()) {
            currentFunction.addLine(ExecuteCommand.create()
                    .in(dimension)
                    .run(GameRuleCommand.create(GameRuleId.NATURAL_REGENERATION)
                            .booleanValue(false)));
        }

        currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_IMMEDIATE_RESPAWN)
                .booleanValue(true));
        currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_PATROL_SPAWNING)
                .booleanValue(false));
        currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_MOB_SPAWNING)
                .booleanValue(false));
        currentFunction.addLine(GameRuleCommand.create(GameRuleId.DO_WEATHER_CYCLE)
                .booleanValue(false));
        currentFunction.addLine(GameRuleCommand.create(GameRuleId.SPAWN_RADIUS)
                .intValue(0));

        // Set difficulty
        currentFunction.addLine(DifficultyCommand.create()
                .difficulty(DifficultyId.HARD));

        // Set default gamemode
        currentFunction.addLine(GameModeCommand.create(GameModeId.ADVENTURE)
                .setDefault());

        // Set world spawn
        currentFunction.addLine(SetWorldSpawnCommand.create()
                .pos(BlockPos.absolute(0, 221, 0)));

        // Create scoreboard objectives
        currentFunction.addAll(ScoreboardCommand.batch().registerAll());
        currentFunction.addLine();
        currentFunction.addLine();
        currentFunction.addLine();
        currentFunction.addLine();

        // Register the function component with the custom namespace
        customNamespace.addComponent(currentFunction);
    }


    // Create scoreboard objectives
        for (
    ScoreboardObjectiveOld objective : scoreboardObjectives) {
        fileCommands.add(objective.add());
    }
        fileCommands.add(new ScoreboardObjectiveOld().setDisplay(ScoreboardLocation.below_name, Objective.Hearts));
        fileCommands.add(new ScoreboardObjectiveOld().setDisplay(ScoreboardLocation.list, Objective.Hearts));

    // Create teams
        for (
    Team t : teams) {
        fileCommands.add(t.add());
        fileCommands.add(t.setTeamColor());
    }

    // Create staging area
        fileCommands.add(Execute.In(Dimension.overworld) +
            FillCommand.create(
            BlockPos.absolute(-6, 220, -6),
            BlockPos.absolute(6, 226, 6),
            Block.create(BlockId.BARRIER))
            .generate());
        fileCommands.add(Execute.In(Dimension.overworld) +
            FillCommand.create(
            BlockPos.absolute(-5, 221, -5),
            BlockPos.absolute(5, 226, 5),
            Block.create(BlockId.AIR))
            .generate());

        fileCommands.add(Execute.In(Dimension.overworld) +
            SetBlockCommand.create(
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
            TextComponent.text("2014-2025"))))));

    // Control Point
        if (OperationMode.controlPoints) {
        // Create bossbars
        fileCommands.add(getBossbarByName("cp1").remove());
        fileCommands.add(getBossbarByName("cp2").remove());
        fileCommands.add(getBossbarByName("cp1").add(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(getBossbarByName("cp1").setMax(controlPoints.get(0).getMaxVal()));
        fileCommands.add(getBossbarByName("cp2").add(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(getBossbarByName("cp2").setMax(controlPoints.get(1).getMaxVal()));
    }
}
