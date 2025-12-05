import arguments.*;
import EntityClasses.*;
import Enums.*;
import FileGeneration.*;
import HelperClasses.*;
import ItemClasses.*;
import ItemModifiers.*;
import Predicates.*;
import TeamGeneration.*;
import arguments.Entity;
import arguments.block.BlockState;
import arguments.block.DynamicBlock;
import arguments.block.SimpleBlock;
import arguments.block.SimpleBlockPredicate;
import arguments.coordinate.Vec3;
import arguments.itempredicate.SimpleItemPredicate;
import arguments.itemstack.*;
import arguments.itemstack.components.*;
import arguments.itemstack.components.attributes.AttributeDisplayTag;
import arguments.itemstack.components.attributes.AttributeModifierEntry;
import arguments.particle.ParticleArgument;
import arguments.particle.ParticleArgumentBuilder;
import arguments.targetselector.SelectorArgumentsBuilder;
import arguments.targetselector.TargetSelector;
import arguments.time.VariableGameTime;
import commands.*;
import commands.Random;
import commands.advancement.AdvancementAction;
import commands.data.DataPath;
import commands.data.DataTargetEntity;
import commands.data.DataValue;
import commands.data.ModificationSetValue;
import commands.effect.EffectAction;
import commands.experience.ExperienceAction;
import commands.forceload.ForceLoadAction;
import commands.item.ItemAction;
import commands.item.ItemTargetEntity;
import commands.random.RandomAction;
import commands.recipe.RecipeAction;
import commands.tag.TagAction;
import commands.time.TimeAction;
import commands.worldborder.WorldBorderAction;
import controlpoints.ControlPoint;
import controlpoints.ControlPointTag;
import nbt.blockentity.*;
import nbt.blockentity.StructureBlockEntity.StructureDataKey;
import nbt.entity.*;
import nbt.entity.data.*;
import nbt.item.PlayerProfileComponentBuilder;
import nbt.tags.ByteTag;
import nbt.tags.CompoundTag;
import nbt.tags.IntTag;
import nbt.tags.StringTag;
import shared.attributes.AttributeId;
import shared.attributes.AttributeOperation;
import shared.attributes.AttributeSlot;
import shared.attributes.AttributeTooltipDisplayType;
import shared.block.ColorableBlockId;
import shared.block.WoodBlockId;
import shared.item.*;
import shared.nbt.*;
import utils.TextComponent;
import shared.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Main {


    //TODO: Automate process using args.
    public static void main(String[] args) throws IOException {
        new Main().run(args);
    }

    //packages
    FileTools fileTools = new FileTools();

    //DatapackData<

    private String uhcNumber;
    private static final String version = "4.0";
    private String dataPackLocation;
    private String pluginLocation;
    private String worldLocation;
    private String dataPackName;
    private static final String namespace = "uhc";

    private String fileLocation;
    private CommunityMode communityMode = CommunityMode.DIORITE;
    private int teamMode;
    //DatapackData>

    //GameData<
    private static final int chestSize = 27;
    private int[] startCoordinate;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> cpList = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    private ArrayList<Perk> perks = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Season> seasons = new ArrayList<>();
    private ArrayList<String> quotes = new ArrayList<>();
    private ArrayList<BossBar> bossBars = new ArrayList<>();
    public static World world = new World(0, Constant.worldHeight, Constant.worldBottom, Constant.worldShape);
    private static final int cpTickPerSecond = 1;
    private static final int cp2ActivationInMin = 6;
    private int cp2ActivationScore;
    private int maxCPScore;
    private static final int cpCaptureInMin = 20;
    private static final int maxCPScoreBossbar = 20 * Constant.secPerMinute * cpTickPerSecond * 2;
    private static final int cpMessageThreshold = 5 * Constant.tickFrequencyLong;
    private static final int minJoinDistance = 10;
    private static final int minDamage = 9;
    private static final String[] cartesian = {"X", "Y", "Z"};
    private static int carePackageAmount;
    private static int carePackageSpread;
    private int minTraitorRank;
    private int traitorWaitTime;
    private static final int traitorMode = 1;
    private String communityName;
    public static final Scoreboard scoreboard = new Scoreboard();

    private final Text bannerText = new Text(TextColor.DARK_GRAY, true, false, " | ");

    private TeamGenerator teamGenerator;
    private ServerProperties properties = new ServerProperties();
    private ArrayList<PaperPlugin> plugins = new ArrayList<>();

    private Singleton singleton;

    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run(String[] args) throws IOException {
        singleton = Singleton.getInstance();
        communityModeChange();
        createDatapack();
        System.out.println("Datapack created");
        if (args.length == 0) {
            boolean menuRunning = true;
            Scanner scanner = new Scanner(System.in);
            String input;
            while (menuRunning) {
                System.out.println("-----------------\n");
                System.out.println("Gamemode: " + communityMode);
                System.out.println("Options:\n");
                System.out.println("Change Gamemode (c[n])");
                System.out.println("Re-run (r)");
                System.out.println("Generate teams (t)");
                System.out.println("-----------------");
                System.out.print("> ");
                input = scanner.nextLine();
                if (input.equals("exit")) {
                    menuRunning = false;
                    System.out.println("Exiting System.");
                } else if (input.startsWith("c")) {
                    String command = input.replace("c", "");
                    int num = parseInt(command);
                    changeCommunitymode(num);
                    createDatapack();
                    System.out.println("Datapack created");
                } else if (input.equals("r")) {
                    communityModeChange();
                    createDatapack();
                    System.out.println("Datapack created");
                } else if (input.equals("t")) {
                    teamGenerator = new TeamGenerator(Double.parseDouble(uhcNumber), fileLocation, teams, communityMode);
                    teamGenerator.run();
                } else {
                    System.out.println("Input not recognized.");
                }
            }
        } else {
            teamGenerator = new TeamGenerator(Double.parseDouble(uhcNumber), fileLocation, teams, communityMode);
            teamGenerator.run(args);
        }
    }

    private void changeCommunitymode(int num) throws IOException {
        if (num == 0) {
            communityMode = CommunityMode.DIORITE;
        }
        if (num == 1) {
            communityMode = CommunityMode.URE;
        }
        if (num == 2) {
            communityMode = CommunityMode.KINJIN;
        }

        communityModeChange();
    }


    private void createDatapack() {
        try {
            fileTools.createDatapack(files, fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void communityModeChange() throws IOException {
        files = new ArrayList<>();
        loadUHCData();
        makeServerProperties();
        initSaveDir();
        fileTools = new FileTools(version, dataPackLocation, dataPackName, worldLocation, pluginLocation, namespace);

        initGameData();
        makeFunctionFiles();
        files.addAll(fileTools.makeRecipeFiles());
        if (OperationMode.carePackages) {
            makeLootTableFiles();
        }
        definePlugins();
    }

    private void loadUHCData() {
        // Get data from uhc_data.txt
        uhcNumber = fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "uhcNumber");
        String[] splitStartCoordinates = fileTools.splitLineOnComma(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "startCoordinate"));
        startCoordinate = new int[]{Integer.parseInt(splitStartCoordinates[0]), Integer.parseInt(splitStartCoordinates[1]), Integer.parseInt(splitStartCoordinates[2])};
        communityName = fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "communityName");

        if (OperationMode.traitorFaction) {
            minTraitorRank = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "minTraitorRank"));
            traitorWaitTime = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "traitorWaitTime"));
        }
    }

    private void makeServerProperties() throws IOException {
        String filePath = "Server\\server.properties";

        // Override fields
        properties.set("difficulty", DifficultyId.HARD);
        properties.set("enable-command-block", true);
        properties.set("gamemode", GameMode.ADVENTURE);
        properties.set("level-seed", -2901703172L);
        properties.set("max-players", 50);
        properties.set("motd", communityName + " UHC S" + uhcNumber);
        properties.set("simulation-distance", 5);
        properties.set("spawn-protection", 0);
        properties.set("view-distance", 7);
        if (OperationMode.bots) {
            properties.set("online-mode", false);
        }

        // Save back to the same file
        properties.saveToFile(filePath);

        System.out.println("Server properties updated successfully.");
    }

    private void initSaveDir() {
        // Set directories
        pluginLocation = "Server\\plugins\\";
        worldLocation = "Server\\" + properties.get("level-name") + "\\";
        dataPackLocation = worldLocation + "datapacks\\";
        dataPackName = "uhc-datapack-" + uhcNumber + "v" + version;
        fileLocation = dataPackLocation + dataPackName + "\\data\\";

        // Create new folders if non-existent
        Path path = Paths.get(dataPackLocation);
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGameData() {
        teams = new ArrayList<>();
        scoreboardObjectives = new ArrayList<>();
        players = new ArrayList<>();
        seasons = new ArrayList<>();
        quotes = new ArrayList<>();
        if (OperationMode.controlPoints) {
            bossBars = new ArrayList<>();
            cpList = new ArrayList<>();
            controlPoints = new ArrayList<>();
        }

        // Colors
        TextColor[] colors = {TextColor.YELLOW, TextColor.BLUE, TextColor.RED, TextColor.DARK_PURPLE, TextColor.DARK_GREEN, TextColor.LIGHT_PURPLE, TextColor.BLACK, TextColor.GOLD, TextColor.GRAY, TextColor.AQUA, TextColor.DARK_RED, TextColor.DARK_BLUE, TextColor.DARK_AQUA};
        BossBarColor[] bossbarColors = {BossBarColor.yellow, BossBarColor.blue, BossBarColor.red, BossBarColor.purple, BossBarColor.green, BossBarColor.pink, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white};
        DyeColor[] glassColors = {DyeColor.YELLOW, DyeColor.LIGHT_BLUE, DyeColor.RED, DyeColor.PURPLE, DyeColor.GREEN, DyeColor.PINK, DyeColor.BLACK, DyeColor.ORANGE, DyeColor.GRAY, DyeColor.CYAN, DyeColor.RED, DyeColor.BLUE, DyeColor.BLUE};
        int[] collarColors = {4, 3, 14, 10, 13, 6, 15, 1, 7, 9, 2, 11, 9};
        String[] jsonColors = {"YELLOW", "BLUE", "RED", "PURPLE", "GREEN", "PINK", "BLACK", "ORANGE", "GRAY", "AQUA", "DARK RED", "DARK BLUE", "DARK AQUA"};
        String[] playerColors = {"Yellow", "Blue", "Red", "Purple", "Green", "Pink", "Black", "Orange", "Gray", "Aqua", "DarkRed", "DarkBlue", "DarkAqua"};
        float[][] dustColors = {
                {1.0f, 1.0f, 0.3f}, // Yellow/White
                {0.3f, 0.3f, 1.0f}, // Blue
                {1.0f, 0.3f, 0.3f}, // Red
                {0.7f, 0.0f, 0.7f}, // Purple
                {0.3f, 1.0f, 0.3f}, // Green
                {1.0f, 0.3f, 1.0f}, // Magenta
                {0.0f, 0.0f, 0.0f}, // Black
                {1.0f, 0.7f, 0.0f}, // Orange
                {0.7f, 0.7f, 0.7f}, // Gray
                {0.3f, 1.0f, 1.0f}, // Cyan
                {0.7f, 0.0f, 0.0f}, // Dark Red
                {0.0f, 0.0f, 0.7f}, // Dark Blue
                {0.0f, 0.7f, 0.7f}  // Teal
        };

        // Teams
        teamMode = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "teamMode"));
        for (int i = 0; i < colors.length; i++) {
            Team team = new Team(i, colors[i], bossbarColors[i], glassColors[i], collarColors[i], jsonColors[i], playerColors[i], dustColors[i]);
            teams.add(team);
        }

        // Players
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            boolean isPlaying = Boolean.parseBoolean(playerSplit[4]);
            if (isPlaying) {
                players.add(new Player(Integer.parseInt(playerSplit[0]), playerSplit[1], Integer.parseInt(playerSplit[2]), Double.parseDouble(playerSplit[3]), isPlaying));
            }
        }

        if (OperationMode.carePackages) {
            // World size based on amount of players
            if (players.size() <= 6) {
                carePackageSpread = 450;
                carePackageAmount = 200;
            } else if (players.size() <= 20) {
                carePackageSpread = 500;
                carePackageAmount = 200;
            } else {
                carePackageSpread = 750;
                carePackageAmount = 450;
            }
        }

        world.setWorldSizeByPlayers(players.size());

        // Seasons
        ArrayList<String> seasonsString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\seasonData.txt");
        for (String season : seasonsString) {
            String[] seasonSplit = fileTools.splitLineOnComma(season);
            seasons.add(new Season(Double.parseDouble(seasonSplit[0]), Integer.parseInt(seasonSplit[1]), new Date(Integer.parseInt(seasonSplit[2]), Integer.parseInt(seasonSplit[3]), Integer.parseInt(seasonSplit[4]))));
        }

        // Quotes
        quotes = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\quotes.txt");

        if (OperationMode.controlPoints) {
            // ControlPoints
            ArrayList<String> controlPointString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\controlPoints.txt");
            for (String controlPoint : controlPointString) {
                String[] controlPointSplit = fileTools.splitLineOnComma(controlPoint);
                cpList.add(new ControlPoint(new ControlPointTag("cp"), maxCPScoreBossbar, 0, new Coordinate(Integer.parseInt(controlPointSplit[0]), Integer.parseInt(controlPointSplit[1]), Integer.parseInt(controlPointSplit[2])), Biome.valueOf(controlPointSplit[3])));
            }

            int[] addRates = {2, 3};
            Collections.shuffle(cpList);
            for (int i = 0; i < addRates.length; i++) {
                controlPoints.add(cpList.get(i));
                controlPoints.get(i).setAddRate(addRates[i]);
                controlPoints.get(i).setName(new ControlPointTag("cp" + (i + 1)));
            }

            // Control Point parameters
            singleton.setMinToCPScore(Constant.secPerMinute * cpTickPerSecond * controlPoints.get(0).getAddRate());
            cp2ActivationScore = cp2ActivationInMin * singleton.getMinToCPScore();
            maxCPScore = cpCaptureInMin * singleton.getMinToCPScore();

            // Bossbars
            bossBars.add(new BossBar("cp1"));
            bossBars.add(new BossBar("cp2"));

            // Perks
            perks.add(new Perk(1, new StatusEffect(EffectId.SPEED, 999999, 0, false), SoundId.BASALT, 3 * singleton.getMinToCPScore()));
            perks.add(new Perk(2, Attribute.create(Entity.ofSelector(TargetSelector.SENDER), AttributeId.SCALE).value(0.8), SoundId.CRIMSON, 6 * singleton.getMinToCPScore()));
            perks.add(new Perk(3, new StatusEffect(EffectId.HASTE, 999999, 2, false), SoundId.WARPED, 12 * singleton.getMinToCPScore()));
            perks.add(new Perk(4, new StatusEffect(EffectId.ABSORPTION, 999999, 1, false), SoundId.WITHER, 15 * singleton.getMinToCPScore()));
        }

        // Scoreboard objectives
        scoreboardObjectives.add(new ScoreboardObjective(Objective.TimeDum, ObjectiveType.dummy, "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Time, ObjectiveType.dummy, "\"Elapsed Time\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Time.extendName(2), ObjectiveType.dummy, "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.SideDum, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Hearts, ObjectiveType.health));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Apples, "minecraft.used:minecraft.golden_apple", "\"Golden Apple\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Stone, "minecraft.mined:minecraft.stone"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Diorite, "minecraft.mined:minecraft.diorite"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Andesite, "minecraft.mined:minecraft.andesite"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Granite, "minecraft.mined:minecraft.granite"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Deepslate, "minecraft.mined:minecraft.deepslate"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Mining, ObjectiveType.dummy, "\"I like mining-leaderboard\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Deaths, ObjectiveType.deathCount));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Kills, ObjectiveType.playerKillCount, true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Rank, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.MinHealth, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Victory, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.WolfAge, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.RandomQuotes, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.DamageTaken, "minecraft.custom:minecraft.damage_taken"));
        for (int i = 0; i < 2; i++) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.CollarCheck.extendName(i), ObjectiveType.dummy));
        }

        if (OperationMode.controlPoints) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.CPScore, ObjectiveType.dummy, "\"Control Point score\"", true));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.CPHighscore, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.ReceivedPerk, ObjectiveType.dummy));
            for (int i = 0; i < 2; i++) {
                scoreboardObjectives.add(new ScoreboardObjective(Objective.ControlPoint.extendName(i + 1), ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.OnCP.extendName(i + 1), ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.PrevCP.extendName(i + 1), ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.DisplayCP.extendName(i + 1), ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.ColorCP.extendName(i + 1), ObjectiveType.dummy));
            }
        }

        if (OperationMode.teamCreationInGame) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.TempKills, ObjectiveType.playerKillCount));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.IsKiller, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.FoundTeam, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.Distance, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.TimesCalled, "minecraft.used:minecraft.goat_horn"));
            for (String s : cartesian) {
                scoreboardObjectives.add(new ScoreboardObjective(Objective.Pos + s, ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.Square + s, ObjectiveType.dummy));
            }
        }
    }

    private void makeLootTableFiles() {
        // Build loot table
        LootTableType type = LootTableType.chest;
        LootTableRolls rolls = new LootTableRolls(DistributionType.uniform, 3, 5);
        int bonusRolls = 2;

        // Loot table entry
        ArrayList<LootTableEntry> entries = new ArrayList<>();
        ArrayList<Components> components = new ArrayList<>();
        ArrayList<ItemModifier> functions = new ArrayList<>();

        // Entry #1
        entries.add(new LootTableEntry(17, ItemId.EGG));

        // Entry #2
        entries.add(new LootTableEntry(17, ItemId.LADDER, new SetCount(10)));

        // Entry #3
        entries.add(new LootTableEntry(15, ItemId.STICK, new SetCount(8)));

        // Entry #4
        entries.add(new LootTableEntry(15, ItemId.DIORITE, new SetCount(16)));

        // Entry #5
        entries.add(new LootTableEntry(15, ItemId.AMETHYST_BLOCK, new SetCount(16)));

        // Entry #6
        entries.add(new LootTableEntry(15, ItemId.IRON_INGOT, new SetCount(8)));

        // Entry #7
        entries.add(new LootTableEntry(14, ItemId.ARROW, new SetCount(10)));

        // Entry #8
        entries.add(new LootTableEntry(11, ItemId.BONE, new SetCount(3, new RandomChance(0.4))));

        // Entry #9
        entries.add(new LootTableEntry(10, ItemId.COPPER_BLOCK, new SetCount(16)));

        // Entry #10
        entries.add(new LootTableEntry(10, ItemId.BREAD, new SetCount(5)));

        // Entry #11
        entries.add(new LootTableEntry(10, ItemId.COBWEB, new SetCount(2, new RandomChance(0.4))));

        // Entry #12
        Enchantments enchantment = new Enchantments(EnchantmentType.LURE, 3);
        entries.add(new LootTableEntry(8, ItemId.FISHING_ROD, new SetComponents(enchantment)));

        // Entry #13
        entries.add(new LootTableEntry(8, ItemId.OBSIDIAN, new SetCount(4)));

        // Entry #14
        entries.add(new LootTableEntry(7, ItemId.GLASS, new SetCount(3)));

        // Entry #15
        entries.add(new LootTableEntry(7, ItemId.MELON_SLICE, new SetCount(3, new RandomChance(0.4))));

        // Entry #16
        entries.add(new LootTableEntry(5, ItemId.TNT, new SetCount(4)));

        // Entry #17
        entries.add(new LootTableEntry(5, ItemId.EXPERIENCE_BOTTLE, new SetCount(3, new RandomChance(0.2))));

        // Entry #18
        entries.add(new LootTableEntry(5, ItemId.BOOK));

        // Entry #19
        entries.add(new LootTableEntry(5, ItemId.REDSTONE, new SetCount(16)));

        // Entry #20
        entries.add(new LootTableEntry(5, ItemId.GUNPOWDER, new SetCount(16)));

        // Entry #21
        entries.add(new LootTableEntry(5, ItemId.GOLD_INGOT, new SetCount(4, new RandomChance(0.3))));

        // Entry #22
        entries.add(new LootTableEntry(5, ItemId.LAPIS_LAZULI, new SetCount(10)));

        // Entry #23
        entries.add(new LootTableEntry(4, ItemId.LAVA_BUCKET));

        // Entry #24
        entries.add(new LootTableEntry(4, ItemId.APPLE, new SetCount(2, new RandomChance(0.3))));

        // Entry #25
        entries.add(new LootTableEntry(2, ItemId.DIAMOND, new SetCount(2, new RandomChance(0.3))));

        // Entry #26
        entries.add(new LootTableEntry(3, ItemId.SADDLE));

        // Entry #27
        entries.add(new LootTableEntry(3, ItemId.SPECTRAL_ARROW, new SetCount(10)));

        // Entry #28
        ArrayList<Attributes> attributes = new ArrayList<>();
        attributes.add(new JumpStrength(1));
        attributes.add(new MovementSpeed(0.2));
        String text = "Driftwood";
        EntityData horse = new Horse(60, true, 5, text, attributes);

        SetName name = new SetName(new Text(false, false, "Driftwood's return"));

        functions.add(new SetComponents(horse));
        functions.add(name);

        entries.add(new LootTableEntry(10, ItemId.HORSE_SPAWN_EGG, functions));
        functions = new ArrayList<>();

        // Entry #29
        entries.add(new LootTableEntry(3, ItemId.GLOWSTONE_DUST, new SetCount(6)));

        // Entry #30
        entries.add(new LootTableEntry(3, ItemId.ENDER_PEARL, new SetCount(2, new RandomChance(0.5))));

        // Entry #31
        entries.add(new LootTableEntry(2, ItemId.NETHER_WART, new SetCount(5)));

        // Entry #32
        entries.add(new LootTableEntry(2, ItemId.BLAZE_ROD, new SetCount(2, new RandomChance(0.1))));

        // Entry #33
        entries.add(new LootTableEntry(2, ItemId.GOLDEN_APPLE));

        // Entry #34
        entries.add(new LootTableEntry(2, ItemId.ANVIL));

        // Entry #35
        entries.add(new LootTableEntry(4, ItemId.SPYGLASS));

        // Entry #36
        entries.add(new LootTableEntry(2, ItemId.WOLF_SPAWN_EGG, new SetCount(2, new RandomChance(0.01))));

        // Entry #37
        entries.add(new LootTableEntry(1, ItemId.DIAMOND_HORSE_ARMOR));

        // Entry #38
        entries.add(new LootTableEntry(1, ItemId.NETHERITE_HOE));

        // Entry #39
        enchantment = new Enchantments(EnchantmentType.LOYALTY, 3);
        entries.add(new LootTableEntry(1, ItemId.TRIDENT, new SetComponents(enchantment)));

        // Entry #40
        entries.add(new LootTableEntry(1, ItemId.NETHERITE_UPGRADE_SMITHING_TEMPLATE));

        // Entry #42
        RandomChance condition = new RandomChance(0.001);
        entries.add(new LootTableEntry(1, ItemId.NETHERITE_SCRAP, new SetCount(4, condition)));

        // Entry #43
        PotionContents contents = new PotionContents(EffectId.LUCK, 0, 600, "59C106", true, false, true);
        name = new SetName(new Text(false, false, "Potion of Care Package luck"));

        functions.add(new SetComponents(contents));
        functions.add(name);

        entries.add(new LootTableEntry(2, ItemId.SPLASH_POTION, functions));
        functions = new ArrayList<>();

        // Entry #44
        contents = new PotionContents(EffectId.POISON, 0, 5, "4E9331", false, true, true);
        name = new SetName(new Text(false, false, "Potion of Poison"));

        functions.add(new SetComponents(contents));
        functions.add(name);

        entries.add(new LootTableEntry(2, ItemId.SPLASH_POTION, functions));
        functions = new ArrayList<>();

        // Entry #45
        contents = new PotionContents(EffectId.BLINDNESS, 0, 10, "1F1F23", false, true, true);
        MaxStackSize stack = new MaxStackSize(64);
        name = new SetName(new Text(false, false, "Potion of Blindness"));
        SetCount count = new SetCount(5, new RandomChance(0.3));

        components.add(contents);
        components.add(stack);

        functions.add(new SetComponents(components));
        functions.add(name);
        functions.add(count);

        entries.add(new LootTableEntry(2, ItemId.SPLASH_POTION, functions));
        functions = new ArrayList<>();
        components = new ArrayList<>();

        // Entry #46
        attributes = new ArrayList<>();
        attributes.add(new JumpStrength(0.7));
        attributes.add(new MovementSpeed(0.34));
        text = "Scuderia";
        horse = new Horse(5, true, 4, text, attributes);

        name = new SetName(new Text(false, false, "Grazie Ragazzi"));

        functions.add(new SetComponents(horse));
        functions.add(name);

        entries.add(new LootTableEntry(2, ItemId.HORSE_SPAWN_EGG, functions));
        functions = new ArrayList<>();

        // Entry #47
        // Set title and author
        String title = "Terraria [Ep1]";
        String author = "Mr9Madness";

        // Compile pages
        ArrayList<ArrayList<TextItem>> pages = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(false, false, "Alright, guys, welcome to a new Let's Play!\\n\\n"));
        texts.add(new Text(false, false, "We're starting a new Terraria playthrough with a fresh character—of course, blue hair and blue eyes, because why not? Naming him "));
        texts.add(new Text(false, true, "Mr9Madness."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Creating a new large world... let’s call it "));
        texts.add(new Text(false, true, "YouTube"));
        texts.add(new Text(false, false, ". The game is generating everything—placing dirt, caves, dungeons, and, yes, a big jungle."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "And we’re in!\\n" +
                "\\n" +
                "Terraria is all about surviving, like Minecraft, but 2D. First things first: gathering resources. I see a bunny... well, first blood goes to the bunny, as always."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Snodog627, I'm not saying your name!\\n" +
                "\\n" +
                "Yeah, someone I know doesn’t want me to play this game because he hates it. Too bad, I love it."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Exploring underground...\\n" +
                "\\n" +
                "Found silver, platinum—wait, normally platinum replaces silver? Whatever, I’ll take it. Died over here earlier, but let’s just jump down again, probably a smart move, right?\\n"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Oh, a hook!\\n" +
                "\\n" +
                "That’s rare! And a cloud in a bottle? First episode luck! Now I can double-jump. Also got a Suspicious Looking Eye—first boss summon ready.\\n"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Building my first house.\\n" +
                "\\n" +
                "It's going underground, don't ask why. Also, fireflies are in the game now—no idea what they do, but cool. Need more wood for a hammer, so time to chop trees.\\n"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Exploring further…\\n" +
                "\\n" +
                "Found a boomerang! Oh, and... "));
        texts.add(new Text(false, true, "corruption"));
        texts.add(new Text(false, false, ". Yeah, nope. "));
        texts.add(new Text(false, true, "Get banned!"));
        texts.add(new Text(false, false, " Not dealing with that right now.\\n" +
                "\\n" +
                "That’s it for the first episode! See you next time!"));

        pages.add(texts);
        texts = new ArrayList<>();

        components.add(new WrittenBookContent(title, author, pages));
        entries.add(new LootTableEntry(1, ItemId.WRITTEN_BOOK, new SetComponents(components)));
        components = new ArrayList<>();

        // Entry #48
        title = "The Diorite Experts UHC S01";
        author = "Snodog627";

        pages = new ArrayList<>();

        texts.add(new Text(false, false, "Alright, welcome everyone to The Diorite Experts UltraHardCore (UHC) Season 1!\\n" +
                "\\n" +
                "We’ve got random teams of three competing in this season."));

        pages.add(texts);
        texts = new ArrayList<>();


        texts.add(new Text(false, false, "Here are the teams:\\n" +
                "Team Red: Snodog627 and PR0BA.\\n" +
                "Team Lime: Tiba101 and WarriorJeroen (a.k.a. SGT_Prostidude).\\n" +
                "Team Blue: BananaKid99 (a.k.a. Mr9Madness) and S3R91."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Now, let’s go over the rules as usual:\\n" +
                "Regeneration potions are banned.\\n" +
                "No strip mining allowed.\\n" +
                "If you’re near (0,0), you have to stay there—escape is only allowed through the Nether."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Everyone clear on that? Good.\\n" +
                "\\n" +
                "Alright, let's talk predictions for this UHC! Who do you think is going to win?\\n" +
                "\\n" +
                "Oh, we’re making predictions already? Okay!"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Wait, does everyone have their starting positions? Is everyone set?\\n" +
                "\\n" +
                "Yeah, I think we're good to go!\\n" +
                "\\n" +
                "Alright then—let’s get started! Good luck, everyone!"));

        pages.add(texts);
        texts = new ArrayList<>();

        components.add(new WrittenBookContent(title, author, pages));
        entries.add(new LootTableEntry(1, ItemId.WRITTEN_BOOK, new SetComponents(components)));
        components = new ArrayList<>();

        // Entry #49
        entries.add(new LootTableEntry(2, ItemId.WIND_CHARGE, new SetCount(5)));

        // Make loot table
        LootTable lTable = new LootTable(type, rolls, bonusRolls, entries);

        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(lTable.generateLootTable());

        FileData fileData = new FileData("supply_drop", fileCommands, "loot_table");
        files.add(fileData);

        // Percentages
        fileCommands = new ArrayList<>();
        fileCommands.add(lTable.GenerateRates());
        fileData = new FileData("supply_drop_rates", fileCommands, "loot_table");
        files.add(fileData);
    }

    private void definePlugins() throws IOException {
        plugins.add(new PaperPlugin("ViaVersion-5.5.1.jar", OperationMode.otherVersions, "ViaBackwards-5.5.1.jar"));
        plugins.add(new PaperPlugin("spark-1.10.119-bukkit.jar", OperationMode.debug));
        plugins.add(new PaperPlugin("Chunky-Bukkit-1.4.28.jar", OperationMode.debug));
        plugins.add(new PaperPlugin("openaudiomc-6.10.7.jar", OperationMode.proximity, "OpenAudioMc\\"));

        fileTools.copyPlugins(plugins);
    }

    // Get by name functions
    private BossBar getBossbarByName(String name) {
        return bossBars.stream().filter(bossBar -> name.equals(bossBar.getName())).findAny().orElse(null);
    }

    public ScoreboardObjective getObjectiveByName(String name) {
        return scoreboardObjectives.stream().filter(objective -> name.equals(objective.getName())).findAny().orElse(null);
    }

    public ScoreboardObjective getObjectiveByName(Objective name) {
        return scoreboardObjectives.stream().filter(objective -> name.toString().equals(objective.getName())).findAny().orElse(null);
    }


    // Create function files
    private void makeFunctionFiles() {
        // Developer mode
        files.add(Initialize());
        files.add(DeveloperMode());
        files.add(GetStartPotions());
        files.add(DeveloperPotionControl());
        files.add(ClearEnderChest());
        files.add(DisplayRank());
        files.add(ClearSchedule());
        files.add(DebugGive());
        files.add(DebugRemove());

        // Game start up
        for (int i = 1; i < 9; i++) {
            files.add(RandomTeams(i));
        }
        files.add(Predictions());
        files.add(PredictionsLoop());
        files.add(IntoCalls());
        files.add(SpreadPlayers());
        files.add(SurvivalMode());
        files.add(StartGame());
        files.add(GameStart.GameStarter());

        // Timers
        Update Updating = new Update();
        files.add(Updating.TimerMain1());
        files.add(Updating.TimerMain5());
        files.add(Updating.TimerMain20());
        files.add(Updating.TimerDeveloper20());

        // Messages
        files.add(ScheduleSingleMessages());
        files.add(MessagePVP());
        if (OperationMode.eternalDay) {
            files.add(MessageEternalDay());
        }

        // Updates
        files.add(HorseFrostWalker());
        files.add(UpdateSidebar());
        files.add(RemoveBannedItems());
        files.add(DisplayQuotes());
        files.add(UpdateMineCount());
        files.add(LocateTeammate());
        files.add(WolfUpdates());
        files.add(AnnounceIronMan());
        files.add(CheckIronMan());

        // Player death
        files.add(DropPlayerHeads());
        files.add(RespawnPlayer());
        files.add(UpdateMinHealth());
        files.add(DisableRespawn());
        files.add(PlayerDeathHandler());

        // Victory
        for (int i = 1; i < 3; i++) {
            files.add(Minute(i));
        }
        files.add(TeamsAliveCheck());
        files.add(Victory());
        for (int i = 0; i < teams.size(); i++) {
            files.add(VictoryMessage(teams.get(i), i));
        }
        files.add(InitiateDeathMatch());
        files.add(DeathMatch());

        // Care Packages
        if (OperationMode.carePackages) {
            files.add(DropCarepackages());
        }

        // Control Points
        if (OperationMode.controlPoints) {
            files.add(Updating.TimerControlPoint20());
            files.add(SpawnControlPoints());
            files.add(InitializeControlPoint());
            files.add(SecondControlPoint());
            files.add(ControlPointCaptured());
            files.add(ControlPointTeamScore());
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                files.add(ControlPoint(i));
                files.add(ControlPointScore(i));
                files.add(ControlPointMessages(i));
                files.add(ControlPointVisuals(i));
                files.add(ControlPointUpdateRecords(i));
            }
            files.add(ControlPointPerksCheck());
            for (int i = 0; i < perks.size(); i++) {
                files.add(ControlPointPerks(i));
            }
            files.add(TeamsHighscoreCheck());
        }

        // Traitor Faction
        if (OperationMode.traitorFaction) {
            files.add(Updating.TimerTraitor5());
            files.add(Updating.TimerTraitor20());
            files.add(TraitorHandout());
            files.add(TraitorActionBar());
            files.add(VictoryTraitor());
            files.add(TraitorCheck());
        }

        // In game teams
        if (OperationMode.teamCreationInGame) {
            files.add(JoinTeam());
            files.add(UpdatePlayerDistance());
            files.add(VictoryMessageSolo());
        }

        // Misc
        files.add(EquipGear());
        files.add(GodMode());
        files.add(BattleRoyale());
    }

    private FileData Initialize() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set gamerules
        for (Dimension dimension : Dimension.values()) {
            fileCommands.add(Execute.In(dimension) +
                    GameRule.create(GameRuleId.NATURAL_REGENERATION)
                                    .booleanValue(false)
                                            .build());
        }
        fileCommands.add(GameRule.create(GameRuleId.DO_IMMEDIATE_RESPAWN)
                        .booleanValue(true)
                        .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_PATROL_SPAWNING)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_MOB_SPAWNING)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_WEATHER_CYCLE)
                .booleanValue(false)
                .build());

        // Set difficulty
        fileCommands.add(Difficulty.create()
                .difficulty(DifficultyId.HARD)
                .build());

        // Set default gamemode
        fileCommands.add(SetGameMode.create(GameMode.ADVENTURE)
                .setDefault()
        );

        // Set world spawn
        fileCommands.add(SetWorldSpawn.create()
                .pos(BlockPos.absolute(0, 221, 0))
                .build());

        // Create scoreboard objectives
        for (ScoreboardObjective objective : scoreboardObjectives) {
            fileCommands.add(objective.add());
        }
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.below_name, Objective.Hearts));
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.list, Objective.Hearts));

        // Create teams
        for (Team t : teams) {
            fileCommands.add(t.add());
            fileCommands.add(t.setTeamColor());
        }

        // Create staging area
        fileCommands.add(Execute.In(Dimension.overworld) +
                Fill.create(
                                BlockPos.absolute(-6, 220, -6),
                                BlockPos.absolute(6, 226, 6),
                                SimpleBlock.create(StaticBlockId.BARRIER))
                        .build());
        fileCommands.add(Execute.In(Dimension.overworld) +
                Fill.create(
                                BlockPos.absolute(-5, 221, -5),
                                BlockPos.absolute(6, 226, 5),
                                SimpleBlock.create(StaticBlockId.AIR))
                        .build());

        fileCommands.add(Execute.In(Dimension.overworld) +
                SetBlock.create(
                                BlockPos.absolute(0, 222, -5),
                                DynamicBlock.create(
                                        WoodBlockId.WALL_SIGN.withWoodType(WoodType.CHERRY),
                                        BlockState.create()
                                                .facing(Direction.SOUTH)
                                                .waterlogged(false),
                                        SignEntity.create()
                                                .setWaxed(false)
                                                .getBackSide()
                                                .addMessage(TextComponent.simple("You have"))
                                                .addMessage(TextComponent.simple("angered"))
                                                .addMessage(TextComponent.simple("the Gods!"))
                                                .addMessage(TextComponent.simple(""))
                                                .done()
                                                .getFrontSide()
                                                .addMessage(TextComponent.withClickCommand(
                                                        "In rememberance",
                                                        "run_command",
                                                        Summon.create(EntityType.FIREWORK_ROCKET)
                                                                .pos(Vec3.relative(0, 0, 0))
                                                                .nbt(
                                                                        FireworkRocketNbtBuilder.create(
                                                                                FireworkRocketDataBuilder.create()
                                                                                        .setProperty(BooleanNbtProperty.GLOWING, true)
                                                                                        .addStar(
                                                                                                FireworkStarDataBuilder.create()
                                                                                                        .setShape(FireworkShape.STAR)
                                                                                                        .buildData())
                                                                                        .buildData())
                                                                                .buildNbt())
                                                                .build()))
                                                .addMessage(TextComponent.simple("of our"))
                                                .addMessage(TextComponent.simple("Command Center"))
                                                .addMessage(TextComponent.simple("2014-2025"))
                                                .done()))
                        .build());

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


        return new FileData(FileName.initialize, fileCommands);
    }

    private FileData PlayerDeathHandler() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Play thunder sound
        fileCommands.add(PlaySound.create(SoundId.THUNDER)
                .source(SoundSource.MASTER)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .pos(Vec3.relative(0, 50, 0))
                .volume(100)
                .build()
        );

        // Set all dead players to spectator mode
        fileCommands.add(SetGameMode.create(GameMode.SPECTATOR)
                .target(Entity.ofSelector(
                                TargetSelector.ALL_PLAYERS,
                                SelectorArgumentsBuilder.create()
                                        .scores(Map.of(ScoreObjective.DEATHS, 1))
                                        .gamemode(GameMode.SPECTATOR, true)
                        )
                ).build()
        );

        // Reset player with lowest health
        fileCommands.add(scoreboard.Set(Constant.adminOld, getObjectiveByName(Objective.MinHealth), 20));

        // Add respawn tag to players who die in the first 20 minutes
        fileCommands.add(Execute.Unless("@e[tag=" + TagTemp.RespawnDisabled + "]") +
                Tag.action(Entity.ofSelector(
                                        TargetSelector.NEAREST_PLAYER,
                                        SelectorArgumentsBuilder.create()
                                                .scores(Map.of(ScoreObjective.DEATHS, 1))),
                                TagAction.ADD)
                        .name(StaticEntityTag.RESPAWN)
                        .build());

        // Drop player head
        fileCommands.add(Schedule.callFunction(FileName.drop_player_heads));

        // Do automatic respawn in the first 20 minutes
        fileCommands.add(Execute.Unless("@e[tag=" + TagTemp.RespawnDisabled + "]") +
                Schedule.callFunction(FileName.respawn_player, 5, Duration.TICKS));

        // Traitor Faction
        if (OperationMode.traitorFaction) {
            // Announce traitor deaths
            texts.add(bannerText);
            texts.add(new Text(TextColor.RED, true, false, "A TRAITOR HAS BEEN ELIMINATED"));
            texts.add(bannerText);
            texts.add(new Text(TextColor.GOLD, true, false, "WELL DONE"));
            texts.add(bannerText);
            fileCommands.add(Execute.If("@p[scores={Deaths=1},tag=" + TagTemp.Traitor + "]") +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();
        }

        // In-game teams
        if (OperationMode.teamCreationInGame) {
            // Do not allow killers to form a team
            String killer = "@p[team=,scores={TempKills=1}]";
            String dead = "@p[team=,scores={Deaths=1}]";

            texts.add(new Text(TextColor.RED, true, false, "Looks like you do not want a teammate."));
            fileCommands.add(Execute.If(killer, false) +
                    Execute.IfNext(dead) +
                    Execute.UnlessNext(killer, Objective.IsKiller, 1, true) +
                    new TellRaw(killer, texts).sendRaw());
            texts.clear();

            fileCommands.add(Execute.If(killer, false) +
                    Execute.IfNext(dead, true) +
                    scoreboard.Set(killer, Objective.IsKiller, 1));

            // Reset temporary kill count
            fileCommands.add(scoreboard.Reset("@p[scores={TempKills=1}]", Objective.TempKills));
        }

        // Reset death count
        fileCommands.add(scoreboard.Reset("@p[scores={Deaths=1}]", Objective.Deaths));

        return new FileData(FileName.handle_player_death, fileCommands);
    }

    private FileData DropPlayerHeads() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Summon a player head upon dying
        for (Player p : players) {
            fileCommands.add(Execute.At("@p[name=" + p.getPlayerName() + ",scores={Deaths=1}]") +
                    Summon.create(EntityType.ITEM)
                            .pos(Vec3.relative(0, 0, 0))
                            .nbt(
                                    ItemNbtBuilder.create(
                                                    ItemData.create(
                                                            ItemId.PLAYER_HEAD,
                                                            1,
                                                            Map.of(
                                                                    ItemComponentType.PROFILE,
                                                                    PlayerProfileComponentBuilder.build(
                                                                            PlayerProfileComponentData.create(p.getPlayerName())))))
                                            .buildNbt())
                            .build());
        }

        return new FileData(FileName.drop_player_heads, fileCommands);
    }

    private FileData ControlPointVisuals(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ControlPoint currentCP = controlPoints.get(i - 1);

        // Update bossbar value
        fileCommands.add(Execute.Store(ExecuteStore.result, getBossbarByName("cp" + i), BossBarStore.value) +
                scoreboard.Get(Constant.adminOld, Objective.DisplayCP.extendName(i)));

        // Players in a team
        for (Team team : teams) {
            // Update bossbar color
            fileCommands.add(Execute.If(Constant.adminOld, Objective.ColorCP.extendName(i), team.getID()) +
                    getBossbarByName("cp" + i).setColor(team.getBossbarColor()));

            // Update waypoint color
            fileCommands.add(Execute.If(Constant.adminOld, Objective.ColorCP.extendName(i), team.getID()) +
                    Waypoint.create(Entity.ofSelector(
                                    TargetSelector.NEAREST_ENTITY,
                                    SelectorArgumentsBuilder.create()
                                            .tag(controlPoints.get(i - 1).getName())))
                            .color(team.getColor())
                            .build());

            // Update glass color
            fileCommands.add(Execute.If(Constant.adminOld, Objective.ColorCP.extendName(i), team.getID(), false) +
                    Execute.InNext(currentCP.getCoordinate().getDimension(), true) +
                    SetBlock.create(
                                    BlockPos.absolute(currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY() + 1, currentCP.getCoordinate().getZ()),
                                    DynamicBlock.create(ColorableBlockId.STAINED_GLASS.withColor(team.getDyeColor())))
                            .mode(SetMode.REPLACE)
                            .build());
        }

        // Keep beacon active
        fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension()) +
                Fill.create(
                                BlockPos.absolute(currentCP.getCoordinate().getX() - 1, currentCP.getCoordinate().getY() - 1, currentCP.getCoordinate().getZ() - 1),
                                BlockPos.absolute(currentCP.getCoordinate().getX() + 1, currentCP.getCoordinate().getY() - 1, currentCP.getCoordinate().getZ() + 1),
                                SimpleBlock.create(StaticBlockId.EMERALD_BLOCK))
                        .build());

        fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension()) +
                SetBlock.create(
                                BlockPos.absolute(currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY(), currentCP.getCoordinate().getZ()),
                                SimpleBlock.create(StaticBlockId.BEACON))
                        .build());

        return new FileData(FileName.control_point_visuals_ + "" + i, fileCommands);
    }

    private FileData ControlPointUpdateRecords(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team team : teams) {
            fileCommands.add(Execute.If(team.getName(), Objective.OnCP.extendName(i), "1..", false) +
                    Execute.IfNext(team.getPlayerColor(), Objective.CPScore, ComparatorType.GREATER, Constant.adminOld, Objective.DisplayCP.extendName(i), true) +
                    scoreboard.Set(Constant.adminOld, Objective.ColorCP.extendName(i), team.getID()));
            fileCommands.add(Execute.If(team.getName(), Objective.OnCP.extendName(i), "1..", false) +
                    Execute.IfNext(team.getPlayerColor(), Objective.CPScore, ComparatorType.GREATER, Constant.adminOld, Objective.DisplayCP.extendName(i), true) +
                    scoreboard.Operation(Constant.adminOld, Objective.DisplayCP.extendName(i), ComparatorType.EQUAL, team.getPlayerColor(), Objective.CPScore));
        }

        return new FileData(FileName.control_point_update_records_ + "" + i, fileCommands);
    }

    private FileData ClearEnderChest() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < chestSize; i++) {
            fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS)))
                    .slot(ItemSlot.ENDERCHEST.withSlotNumber(i))
                    .replaceWith(SimpleItemStack.create(ItemId.AIR), 1)
                    .build());
        }

        return new FileData(FileName.clear_enderchest, fileCommands);
    }

    private FileData EquipGear() {
        ArrayList<String> fileCommands = new ArrayList<>();

        ItemTargetEntity targets = ItemTargetEntity.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS));

        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.CHEST)
                .replaceWith(DynamicItemStack.create(ItemId.getArmorResourceLocation(ArmorMaterial.IRON, ArmorPiece.CHESTPLATE)))
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.FEET)
                .replaceWith(DynamicItemStack.create(ItemId.getArmorResourceLocation(ArmorMaterial.IRON, ArmorPiece.BOOTS)))
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.HEAD)
                .replaceWith(DynamicItemStack.create(ItemId.getArmorResourceLocation(ArmorMaterial.IRON, ArmorPiece.HELMET)))
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.LEGS)
                .replaceWith(DynamicItemStack.create(ItemId.getArmorResourceLocation(ArmorMaterial.IRON, ArmorPiece.LEGGINGS)))
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.OFFHAND)
                .replaceWith(SimpleItemStack.create(ItemId.SHIELD))
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(DynamicItemStack.create(ItemId.getToolResourceLocation(ToolMaterial.IRON, ToolPiece.AXE)))
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        targets)
                .slot(ItemSlot.INVENTORY.withSlotNumber(0))
                .replaceWith(DynamicItemStack.create(ItemId.getToolResourceLocation(ToolMaterial.IRON, ToolPiece.SWORD)))
                .build());

        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.REGENERATION)
                .seconds(1)
                .amplifier(255)
                .hideParticles(true)
                .build());

        return new FileData(FileName.equip_gear, fileCommands);
    }

    private FileData GodMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.SENDER))
                .effect(EffectId.RESISTANCE)
                .seconds(99999)
                .amplifier(4)
                .hideParticles(true)
                .build());
        fileCommands.add(Item.create(ItemAction.REPLACE_WITH,
                        ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                .slot(ItemSlot.MAINHAND)
                .replaceWith(
                        ComponentItemStack.create(ItemId.TRIDENT)
                                .addComponent(CustomNameComponent.create(
                                        TextComponent.array(List.of(
                                                TextComponent.complex("aA", TextColor.WHITE, false, false, true),
                                                TextComponent.complex("The", "#8C3CC1", true, false, false),
                                                TextComponent.complex(" Impaler ", "#E280FF", true, false, false),
                                                TextComponent.complex("Aa", TextColor.WHITE, null, null, true)))))
                                .addComponent(LoreComponent.create("This holy weapon impales anything it touches"))
                                .addComponent(DamageComponent.create(0))
                                .addComponent(EnchantmentsComponent.create(Map.of(
                                        EnchantmentId.FIRE_ASPECT, 255,
                                        EnchantmentId.SHARPNESS, 255,
                                        EnchantmentId.IMPALING, 255,
                                        EnchantmentId.LOYALTY, 255,
                                        EnchantmentId.EFFICIENCY, 255)))
                                .addComponent(AttributeModifiersComponent.create(List.of(
                                        AttributeModifierEntry.create(
                                                AttributeId.ARMOR,
                                                AttributeId.ARMOR,
                                                1000.0,
                                                AttributeOperation.ADD_VALUE,
                                                AttributeSlot.ARMOR,
                                                AttributeDisplayTag.create(AttributeTooltipDisplayType.HIDDEN)),
                                        AttributeModifierEntry.create(
                                                AttributeId.ATTACK_DAMAGE,
                                                AttributeId.ATTACK_DAMAGE,
                                                1000.0,
                                                AttributeOperation.ADD_VALUE,
                                                AttributeSlot.MAINHAND,
                                                AttributeDisplayTag.create(AttributeTooltipDisplayType.HIDDEN)))))
                                .addComponent(UnbreakableComponent.create()))
                .build());

        return new FileData(FileName.god_mode, fileCommands);
    }

    private FileData GetStartPotions() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Clear inventory
        fileCommands.add(Clear.create()
                        .targets(Entity.ofSelector(TargetSelector.SENDER))
                                .build());

        // Give potions
        if (!OperationMode.teamCreationInGame) {
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(0))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("808080")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.SPEED)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Set operational mode to Developer Mode."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Developer Mode"))))
                            .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(1))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("FF9933")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.WEAKNESS)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Assign players to teams."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Assign Teams"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(2))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("6633CC")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.SLOW_FALLING)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Who will win this season?."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Predictions"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(3))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("3399FF")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.INVISIBILITY)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Allow players to gather in their Discord channel."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Into Calls"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(4))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("00CC66")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.POISON)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Spread players across the map."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Spread players"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(5))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("CC3333")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.STRENGTH)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Set operational mode to Ready to Play."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Survival Mode"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(6))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("00FF7F")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.SLOWNESS)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Start the game. Good luck!"))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Start Game"))))
                    .build());
        } else {
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(0))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("808080")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.SPEED)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Set operational mode to Developer Mode."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Developer Mode"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(1))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("6633CC")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.SLOW_FALLING)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Who will win this season?."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Predictions"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(2))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("3399FF")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.INVISIBILITY)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Allow players to gather in their Discord channel."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Into Calls"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(3))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("00CC66")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.POISON)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Spread players across the map."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Spread players"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(4))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("CC3333")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.STRENGTH)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Set operational mode to Ready to Play."))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Survival Mode"))))
                    .build());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                    .slot(ItemSlot.HOTBAR.withSlotNumber(5))
                    .replaceWith(ComponentItemStack.create(ItemId.SPLASH_POTION)
                            .addComponent(PotionContentsComponent.create()
                                    .customColor("00FF7F")
                                    .customEffects(CustomEffectsComponent.create()
                                            .addEffect(CustomEffectEntry.create(EffectId.SLOWNESS)
                                                    .amplifier(0)
                                                    .duration(200)
                                                    .showParticles(false)
                                                    .showIcon(false)
                                                    .ambient(false))))
                            .addComponent(LoreComponent.create("Start the game. Good luck!"))
                            .addComponent(CustomNameComponent.create(TextComponent.simple("Start Game"))))
                    .build());
        }

        return new FileData(FileName.start_potions, fileCommands);
    }

    private FileData DeveloperMode() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Create marker entity
        fileCommands.add(Kill.create()
                        .targets(Constant.admin)
                                .build());
        fileCommands.add(
                Summon.create(EntityType.MARKER)
                        .pos(Vec3.absolute(0, Constant.worldBottom, 0))
                        .nbt(BaseEntityNbt.create(EntityType.MARKER, "Admin").buildNbt())
                        .build()
        );

        // Set time
        fileCommands.add(Time.create(
                        TimeAction.SET,
                        VariableGameTime.create(0))
                .build());

        // Set gamerules
        fileCommands.add(GameRule.create(GameRuleId.COMMAND_BLOCK_OUTPUT)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_DAYLIGHT_CYCLE)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.KEEP_INVENTORY)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_MOB_SPAWNING)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_TILE_DROPS)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DROWNING_DAMAGE)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.FALL_DAMAGE)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.FIRE_DAMAGE)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.SEND_COMMAND_FEEDBACK)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_IMMEDIATE_RESPAWN)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DISABLE_RAIDS)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_INSOMNIA)
                .booleanValue(false)
                .build());

        // Reset scores of all entities
        fileCommands.add(scoreboard.Reset("@e"));
        fileCommands.add(scoreboard.Set(Constant.adminOld, Objective.MinHealth, 20));
        fileCommands.add(scoreboard.Set(Constant.adminOld, Objective.Victory, 1));

        // Create jukebox at 0,0
        fileCommands.add(Execute.In(Dimension.overworld) +
                SetBlock.create(
                                BlockPos.absolute(startCoordinate),
                                DynamicBlock.create(
                                        StaticBlockId.JUKEBOX,
                                        BlockState.create()
                                                .hasRecord(true),
                                        JukeboxEntity.create()
                                                .setRecord(ItemId.MUSIC_DISC_STAL, (byte) 1)))
                        .build());

        // Remove tags
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                .name(StaticEntityTag.RESPAWN_DISABLED)
                .build());
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                .name(StaticEntityTag.IRON_MAN_CANDIDATE)
                .build());
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                .name(StaticEntityTag.IRON_MAN)
                .build());
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                .name(StaticEntityTag.RESPAWN)
                .build());
        fileCommands.add(Tag.action(Constant.admin, TagAction.REMOVE)
                .name(StaticEntityTag.GAME_STARTED)
                .build());

        // Set world border
        fileCommands.add(WorldBorder.create(WorldBorderAction.SET)
                .distance(2 * world.getSize())
                .build());

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
                Attribute.create(
                        Entity.ofSelector(TargetSelector.SENDER),
                        AttributeId.SCALE)
                                .setBase(1));
        fileCommands.add(Execute.As("@a") +
                Attribute.create(
                                Entity.ofSelector(TargetSelector.SENDER),
                                AttributeId.WAYPOINT_TRANSMIT_RANGE)
                        .setBase(0));

        // Set gamemode of player executing the command to creative
        fileCommands.add(SetGameMode.create(GameMode.CREATIVE)
                .target(Entity.ofSelector(TargetSelector.SENDER))
                .build()
        );

        // Clear scheduled commands
        fileCommands.add(Schedule.callFunction(FileName.clear_schedule));

        // Clear all player effects
        fileCommands.add(Effect.create(EffectAction.CLEAR)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .build());

        // Give admin start potions
        fileCommands.add(Schedule.callFunction(FileName.start_potions));

        // Start timers
        fileCommands.add(Schedule.callFunction(FileName.timer_developer_20));

        // Care Packages
        if (OperationMode.carePackages) {
            // Set scoreboard dummies
            fileCommands.add(scoreboard.Set("CarePackages", getObjectiveByName(Objective.Time), 1200));

            // Remove tags
            fileCommands.add(Tag.action(Constant.admin, TagAction.REMOVE)
                    .name(StaticEntityTag.CARE_PACKAGES_DROPPED)
                    .build());
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
            fileCommands.add(Tag.action(Constant.admin, TagAction.REMOVE)
                    .name(StaticEntityTag.CONTROL_POINT_1_ENABLED)
                    .build());
            fileCommands.add(Tag.action(Constant.admin, TagAction.REMOVE)
                    .name(StaticEntityTag.CONTROL_POINT_2_ENABLED)
                    .build());
            fileCommands.add(Tag.action(Constant.admin, TagAction.REMOVE)
                    .name(StaticEntityTag.CONTROL_POINT_CAPTURED)
                    .build());

            // Spawn new Control Points
            fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                    ForceLoad.create(ForceLoadAction.ADD)
                            .from(ColumnPos.absolute(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()))
                            .build());
            fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                    ForceLoad.create(ForceLoadAction.ADD)
                            .from(ColumnPos.absolute(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()))
                            .build());
            fileCommands.add(Schedule.callFunction(FileName.spawn_control_points));
            fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                    ForceLoad.create(ForceLoadAction.REMOVE)
                            .from(ColumnPos.absolute(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()))
                            .build());
            fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                    ForceLoad.create(ForceLoadAction.REMOVE)
                            .from(ColumnPos.absolute(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()))
                            .build());

            // Reset bossbars
            BossBar bossBarCp1 = getBossbarByName("cp1");
            BossBar bossBarCp2 = getBossbarByName("cp2");
            fileCommands.add(bossBarCp1.setColor(BossBarColor.white));
            fileCommands.add(bossBarCp1.setVisible(false));
            fileCommands.add(bossBarCp1.setPlayers("@a"));
            fileCommands.add(bossBarCp1.setTitle(controlPoints.get(0).getName().toUpperCase() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
            fileCommands.add(bossBarCp1.setValue(0));
            fileCommands.add(bossBarCp2.setColor(BossBarColor.white));
            fileCommands.add(bossBarCp2.setVisible(false));
            fileCommands.add(bossBarCp2.setPlayers("@a"));
            fileCommands.add(bossBarCp2.setTitle(controlPoints.get(1).getName().toUpperCase() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
            fileCommands.add(bossBarCp2.setValue(0));

            // Kill waypoints
            for (ControlPoint controlPoint : controlPoints) {
                fileCommands.add(Kill.create()
                                .targets(Entity.ofSelector(
                                        TargetSelector.NEAREST_ENTITY,
                                        SelectorArgumentsBuilder.create()
                                                .tag(controlPoint.getName())))
                                        .build());
            }
            fileCommands.add(ForceLoad.create(ForceLoadAction.REMOVE)
                    .build());
        }

        // Traitor Faction
        if (OperationMode.traitorFaction) {
            // Set scoreboard dummies
            fileCommands.add(scoreboard.Set("TraitorFaction", getObjectiveByName(Objective.Time), 2400));

            // Remove tags
            fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                    .name(StaticEntityTag.TRAITOR)
                    .build());
            fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                    .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                    .build());
            fileCommands.add(Tag.action(Constant.admin, TagAction.REMOVE)
                    .name(StaticEntityTag.TRAITORS_ASSIGNED)
                    .build());
        }

        // In-game team creation
        if (OperationMode.teamCreationInGame) {
            // Reset scoreboard objectives
            fileCommands.add(scoreboard.Set("@a", Objective.IsKiller, 0));
        }

        return new FileData(FileName.developer_mode, fileCommands);
    }

    private FileData RandomTeams(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team t : teams) {
            fileCommands.add(t.joinTeam("@r[limit=" + i + ",team=]"));
        }

        return new FileData("" + FileName.random_teams + i, fileCommands);
    }

    private FileData Predictions() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Remove resistance and give regeneration
        fileCommands.add(Effect.create(EffectAction.CLEAR)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .build());
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.REGENERATION)
                .seconds(1)
                .amplifier(255)
                .build());

        // Make players fall
        fileCommands.add(Tag.action(Entity.ofSelector(
                TargetSelector.ALL_PLAYERS,
                SelectorArgumentsBuilder.create()
                        .gamemode(GameMode.ADVENTURE, true)),
                TagAction.ADD)
                        .name(StaticEntityTag.IS_FLYING)
                        .build());

        fileCommands.add(SetGameMode.create(GameMode.ADVENTURE)
                .target(Entity.ofSelector(
                                TargetSelector.ALL_PLAYERS,
                                SelectorArgumentsBuilder.create()
                                        .tag(StaticEntityTag.IS_FLYING)
                        )
                )
                .build()
        );
        fileCommands.add(SetGameMode.create(GameMode.CREATIVE)
                .target(Entity.ofSelector(
                                TargetSelector.ALL_PLAYERS,
                                SelectorArgumentsBuilder.create()
                                        .tag(StaticEntityTag.IS_FLYING)
                        )
                )
                .build()
        );
        fileCommands.add(Tag.action(Entity.ofSelector(
                                TargetSelector.ALL_PLAYERS,
                                SelectorArgumentsBuilder.create()
                                        .tag(StaticEntityTag.IS_FLYING)),
                        TagAction.REMOVE)
                .name(StaticEntityTag.IS_FLYING)
                .build());

        // Set death count for comparison
        fileCommands.add(scoreboard.Set("@a", Objective.Deaths, 0));

        // Teleport everyone underneath the world
        fileCommands.add(Execute.In(Dimension.overworld) +
                Teleport.create()
                                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                                        .location(Vec3.absolute(0, -100, 0))
                                                .build());

        // Announcement message
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, "PREDICTIONS STARTED! GOOD LUCK"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Call predictions loop
        fileCommands.add(Schedule.callFunction(FileName.predictions_loop));

        return new FileData(FileName.predictions, fileCommands);
    }

    private FileData PredictionsLoop() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Check if anyone has won
        if (!OperationMode.teamCreationInGame) {
            for (Team t : teams) {
                // Get tag that predictions have been completed
                fileCommands.add(Execute.If("@p[team=" + t.getName() + ",scores={Deaths=0}]", false) +
                        Execute.UnlessNext("@p[team=!" + t.getName() + ",scores={Deaths=0}]", true) +
                        Tag.action(Constant.admin, TagAction.ADD)
                                .name(StaticEntityTag.PREDICTIONS_COMPLETED)
                                .build());

                // Chat message
                texts.add(bannerText);
                texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
                texts.add(bannerText);
                texts.add(new Text(t.getColor(), true, false, t.getJSONColor()));
                texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, " WILL WIN THE SEASON!"));
                texts.add(bannerText);

                fileCommands.add(Execute.If("@p[team=" + t.getName() + ",scores={Deaths=0}]", false) +
                        Execute.UnlessNext("@p[team=!" + t.getName() + ",scores={Deaths=0}]", true) +
                        new TellRaw("@a", texts).sendRaw());
                texts.clear();
            }
        } else {
            // Choose player as candidate for having won
            fileCommands.add(Tag.action(Entity.ofSelector(
                    TargetSelector.RANDOM_PLAYER,
                    SelectorArgumentsBuilder.create()
                            .team()
                            .scores(Map.of(ScoreObjective.DEATHS, 0))),
                    TagAction.ADD)
                            .name(StaticEntityTag.PREDICTION_CANDIDATE)
                            .build());

            // Get tag that predictions have been completed
            fileCommands.add(Execute.Unless("@p[tag=!" + TagTemp.PredictionCandidate + ",scores={Deaths=0}]") +
                    Tag.action(Constant.admin, TagAction.ADD)
                                    .name(StaticEntityTag.PREDICTIONS_COMPLETED)
                                            .build());

            // Chat message
            texts.add(bannerText);
            texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
            texts.add(bannerText);
            texts.add(new Select("@p[tag=" + TagTemp.PredictionCandidate + "]"));
            texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, " WILL WIN THE SEASON!"));
            texts.add(bannerText);

            fileCommands.add(Execute.Unless("@p[tag=!" + TagTemp.PredictionCandidate + ",scores={Deaths=0}]") +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();

            // Clear candidate tag
            fileCommands.add(Tag.action(Entity.ofSelector(
                                    TargetSelector.NEAREST_PLAYER,
                                    SelectorArgumentsBuilder.create()
                                            .tag(StaticEntityTag.PREDICTION_CANDIDATE)),
                            TagAction.REMOVE)
                    .name(StaticEntityTag.PREDICTION_CANDIDATE)
                    .build());
        }

        // Self-schedule function
        fileCommands.add(Execute.Unless("@e[tag=" + TagTemp.PredictionsCompleted + "]") +
                Schedule.callFunction(FileName.predictions_loop, 1, Duration.TICKS));

        return new FileData(FileName.predictions_loop, fileCommands);
    }

    private FileData IntoCalls() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Teleport to starting coordinates
        fileCommands.add(Execute.In(Dimension.overworld) +
                Teleport.create()
                                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                                        .location(Vec3.absolute(startCoordinate))
                                                .build());

        // Reset scores
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Deaths), 0));
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Kills), 0));

        // Make players invulnerable
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.RESISTANCE)
                .seconds(99999)
                .amplifier(4)
                .hideParticles(true)
                .build());

        return new FileData(FileName.into_calls, fileCommands);
    }

    private FileData SpreadPlayers() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Spread teams together in assigned mode, separate in unassigned mode
        Boolean respectTeams = !OperationMode.teamCreationInGame;

        fileCommands.add(Execute.In(Dimension.overworld) +
                SpreadPlayers.create(
                                Constant.spawnCenter,
                                0.3f * world.getSize(),
                                0.9f * world.getSize(),
                                respectTeams,
                                Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .build());

        return new FileData(FileName.spread_players, fileCommands);
    }

    private FileData SurvivalMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(GameRule.create(GameRuleId.COMMAND_BLOCK_OUTPUT)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_DAYLIGHT_CYCLE)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.KEEP_INVENTORY)
                .booleanValue(false)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_MOB_SPAWNING)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_TILE_DROPS)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DROWNING_DAMAGE)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.FALL_DAMAGE)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.FIRE_DAMAGE)
                .booleanValue(true)
                .build());
        fileCommands.add(GameRule.create(GameRuleId.DO_IMMEDIATE_RESPAWN)
                .booleanValue(true)
                .build());
        fileCommands.add(Schedule.callFunction(FileName.clear_enderchest));

        // Recipes
        // fileCommands.add(giveRecipe("@a", Block.GOLDEN_APPLE.setNamespace(Namespace.uhc)));
        fileCommands.add(Recipe.create(
                        RecipeAction.TAKE,
                        Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .recipe(RecipeId.DRAGON_HEAD)
                .build());

        // Remove resistance
        fileCommands.add(Effect.create(EffectAction.CLEAR)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.RESISTANCE)
                .build());

        // Set scoreboard values
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Hearts), 20));
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.DamageTaken), 0));

        return new FileData(FileName.survival_mode, fileCommands);
    }

    private FileData StartGame() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Set world time
        fileCommands.add(Time.create(
                        TimeAction.SET,
                        VariableGameTime.create(0))
                .build());

        // Give potion effect
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.REGENERATION)
                .seconds(1)
                .amplifier(255)
                .build());
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.SATURATION)
                .seconds(1)
                .amplifier(255)
                .build());
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .effect(EffectId.RESISTANCE)
                .seconds(20 * 60)
                .amplifier(2)
                .hideParticles(true)
                .build());

        // Clear player inventories
        fileCommands.add(Clear.create()
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .build());

        // Set all players to survival mode
        fileCommands.add(SetGameMode.create(GameMode.SURVIVAL)
                .target(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .build()
        );

        // Revoke all advancements
        fileCommands.add(Advancement.create(
                AdvancementAction.REVOKE,
                        Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                        .everything()
                        .build());

        // Experience
        fileCommands.add(Experience.create(
                        ExperienceAction.SET,
                        Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .amount(0)
                .type(ExperienceType.LEVELS)
                .build());
        fileCommands.add(Experience.create(
                        ExperienceAction.SET,
                        Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .amount(0)
                .type(ExperienceType.POINTS)
                .build());

        // Give players teammate tools
        if (!OperationMode.teamCreationInGame) {
            // Teammate tracker
            for (Team team : teams) {
                fileCommands.add(Give.create(Entity.ofSelector(
                                        TargetSelector.ALL_PLAYERS,
                                        SelectorArgumentsBuilder.create()
                                                .team(team.getName())),
                                BundleItemStack.create(
                                        team.getDyeColor(),
                                        EnchantmentsComponent.create(Map.of(EnchantmentId.VANISHING_CURSE, 1)),
                                        CustomDataComponent.create(CompoundTag.create()
                                                .put(new ByteTag("locateTeammate", (byte) 1)))))
                        .build());
            }
        } else {
            // Team caller
            fileCommands.add(Give.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS),
                            GoatHornItemStack.create(
                                    InstrumentComponent.create(GoatHornInstrumentId.PONDER_GOAT_HORN),
                                    UseCooldownComponent.create(30),
                                    EnchantmentsComponent.create(Map.of(EnchantmentId.VANISHING_CURSE, 1))))
                    .build());
        }

        // Change title display time
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .displayTimes(
                        VariableGameTime.second(1),
                        VariableGameTime.second(5),
                        VariableGameTime.second(2))
                .build());

        // Display world size
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("World size: ±" + world.getSize() + " blocks", TextColor.LIGHT_PURPLE))
                .build());

        // Display game start
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex("Game Starting Now!", TextColor.GOLD, true, true, false))
                .build());

        // Change title display time
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .reset()
                .build());

        // Destroy all ground items
        fileCommands.add(Kill.create()
                        .targets(Entity.ofSelector(
                                TargetSelector.ALL_ENTITIES,
                                SelectorArgumentsBuilder.create()
                                        .type(EntityType.ITEM)))
                        .build());

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.game_starter));

        return new FileData(FileName.start_game, fileCommands);
    }

    private FileData BattleRoyale() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Execute.In(Dimension.overworld, false) +
                Execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                SetGameMode.create(GameMode.SURVIVAL)
                        .target(
                                Entity.ofSelector(
                                        TargetSelector.ALL_PLAYERS,
                                        SelectorArgumentsBuilder.create()
                                                .distance("..20")
                                                .gamemode(GameMode.CREATIVE, true)
                                )
                        )
                        .build()
        );
        fileCommands.add(Execute.In(Dimension.overworld, false) +
                Execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                SpreadPlayers.create(
                                Constant.spawnCenter,
                                0.3f * world.getSize(),
                                0.9f * world.getSize(),
                                true,
                                Entity.ofSelector(
                                        TargetSelector.ALL_PLAYERS,
                                        SelectorArgumentsBuilder.create()
                                                .distance("..20")
                                                .gamemode(GameMode.SURVIVAL)))
                        .build());

        return new FileData(FileName.battle_royale, fileCommands);
    }

    private FileData InitializeControlPoint() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Display Control Point 1 enabled
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("is now enabled!", TextColor.LIGHT_PURPLE, true, true, false))
                .build());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex("Control Point 1", TextColor.GOLD, true, true, false))
                .build());

        // Make bossbars visible
        fileCommands.add(getBossbarByName("cp1").setVisible(true));
        fileCommands.add(getBossbarByName("cp2").setVisible(true));

        // Remove CP1 reinforced deepslate block
        fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                ForceLoad.create(ForceLoadAction.ADD)
                        .from(ColumnPos.absolute(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()))
                        .build());
        fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                SetBlock.create(
                                BlockPos.absolute(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getY() + 3, controlPoints.get(0).getCoordinate().getZ()),
                                SimpleBlock.create(StaticBlockId.AIR))
                        .build());
        fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                ForceLoad.create(ForceLoadAction.REMOVE)
                        .from(ColumnPos.absolute(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()))
                        .build());

        // Summon armor stands for locator bar tracking
        for (ControlPoint controlPoint : controlPoints) {
            // Forceload chunk
            fileCommands.add(ForceLoad.create(ForceLoadAction.ADD)
                    .from(ColumnPos.absolute(controlPoint.getCoordinate().getX(), controlPoint.getCoordinate().getZ()))
                    .build());

            // Summon armor stand to be tracked
            fileCommands.add(Summon.create(EntityType.ARMOR_STAND)
                    .pos(Vec3.absolute(controlPoint.getCoordinate().getX(), controlPoint.getCoordinate().getY(), controlPoint.getCoordinate().getZ()))
                    .nbt(ArmorStandNbtBuilder.create(
                                    ArmorStandData.create(
                                            true,
                                            true,
                                            true,
                                            new EntityTag[]{controlPoint.getName()}))
                            .buildNbt())
                    .build());

            // Set transmit range of waypoint
            fileCommands.add(Attribute.create(
                            Entity.ofSelector(
                                    TargetSelector.NEAREST_ENTITY,
                                    SelectorArgumentsBuilder.create()
                                            .tag(controlPoint.getName())),
                            AttributeId.WAYPOINT_TRANSMIT_RANGE)
                    .setBase(Main.world.getFullSize()));

            // Set color of waypoint to white
            fileCommands.add(Waypoint.create(Entity.ofSelector(
                            TargetSelector.NEAREST_ENTITY,
                            SelectorArgumentsBuilder.create()
                                    .tag(controlPoint.getName())))
                            .color(TextColor.WHITE)
                    .build());
        }

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_20));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                        .name(StaticEntityTag.CONTROL_POINT_1_ENABLED)
                                .build());

        return new FileData(FileName.initialize_control_point, fileCommands);
    }

    private FileData SecondControlPoint() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Announce that Control Point 2 is enabled
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, "CONTROL POINT 2 IS NOW AVAILABLE!"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Remove reinforced deepslate from CP2
        fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                ForceLoad.create(ForceLoadAction.ADD)
                        .from(ColumnPos.absolute(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()))
                        .build());
        fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                SetBlock.create(
                                BlockPos.absolute(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getY() + 3, controlPoints.get(1).getCoordinate().getZ()),
                                SimpleBlock.create(StaticBlockId.AIR))
                        .build());
        fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                ForceLoad.create(ForceLoadAction.REMOVE)
                        .from(ColumnPos.absolute(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()))
                        .build());

        // Change bossbar text
        fileCommands.add(getBossbarByName("cp2").setTitle("CP2: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ") - FASTER!!"));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                        .name(StaticEntityTag.CONTROL_POINT_2_ENABLED)
                                .build());

        return new FileData(FileName.second_control_point, fileCommands);
    }

    private FileData Minute(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, i + " MINUTE(S) REMAINING"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex(i + " minute(s) remaining", TextColor.GOLD, true, true, false))
                .build());
        return new FileData("" + FileName.minute_ + i, fileCommands);
    }

    private FileData Victory() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set objective to victory achieved
        fileCommands.add(scoreboard.Set(Constant.adminOld, getObjectiveByName(Objective.Victory), 2));

        // Call deathmatch functions
        fileCommands.add(Execute.If("@a[limit=2,gamemode=!spectator]") +
                Schedule.callFunction(FileName.initiate_deathmatch));

        // Announce iron man
        fileCommands.add(Execute.As("@a[scores={DamageTaken=.." + minDamage + "}]") +
                Schedule.callFunction(FileName.announce_iron_man));

        return new FileData(FileName.victory, fileCommands);
    }

    private FileData InitiateDeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Call deathmatch functions
        fileCommands.add(Schedule.callFunction(FileName.minute_ + "2", 60));
        fileCommands.add(Schedule.callFunction(FileName.minute_ + "1", 60 * 2));
        fileCommands.add(Schedule.callFunction(FileName.death_match, 60 * 3));

        return new FileData(FileName.initiate_deathmatch, fileCommands);
    }

    private FileData VictoryMessage(Team team, int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Chat message
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(team.getColor(), true, false, team.getJSONColor()));
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, " TEAM VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Title
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("has been achieved!", TextColor.LIGHT_PURPLE, true, true, false))
                .build());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex(team.getJSONColor() + " team victory", TextColor.GOLD, true, true, false))
                .build());

        // Proceed to victory mode
        fileCommands.add(Schedule.callFunction(FileName.victory));

        return new FileData("" + FileName.victory_message_ + i, fileCommands);
    }

    private FileData VictoryMessageSolo() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Chat message
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Select(TextColor.WHITE, false, true, "@s"));
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, " HAS ACHIEVED VICTORY!"));
        texts.add(bannerText);

        // Title
        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("Absolute chad.", TextColor.LIGHT_PURPLE, true, true, false))
                .build());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.array(List.of(
                        TextComponent.selector(Entity.ofSelector(TargetSelector.SENDER), TextColor.WHITE, false, true, false),
                        TextComponent.complex(" victorious", TextColor.GOLD, true, false, false))))
                .build());

        // Proceed to victory mode
        fileCommands.add(Schedule.callFunction(FileName.victory));

        return new FileData(FileName.victory_message_solo, fileCommands);
    }

    private FileData VictoryTraitor() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Chat message
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, " TRAITOR VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Title
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("ggez", TextColor.LIGHT_PURPLE, true, true, false))
                .build());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex("Traitors Win", TextColor.GOLD, true, true, false))
                .build());

        // Proceed to victory mode
        fileCommands.add(Schedule.callFunction(FileName.victory));

        return new FileData(FileName.victory_message_traitor, fileCommands);
    }

    private FileData DeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set start worldborder size
        fileCommands.add(WorldBorder.create(WorldBorderAction.SET)
                .distance(400)
                .build());

        // Set destination worldborder size
        fileCommands.add(WorldBorder.create(WorldBorderAction.SET)
                .distance(20)
                .time(180)
                .build());

        // Teleport all living players
        fileCommands.add(Execute.In(Dimension.overworld) +
                Teleport.create()
                        .targets(Entity.ofSelector(
                                TargetSelector.ALL_PLAYERS,
                                SelectorArgumentsBuilder.create()
                                        .gamemode(GameMode.SPECTATOR, true)))
                        .location(Vec3.absolute(3, 153, 3))
                        .build());

        // Spread players in a team together
        fileCommands.add(Execute.In(Dimension.overworld) +
                SpreadPlayers.create(
                                Constant.spawnCenter,
                                75,
                                150,
                                true,
                                Entity.ofSelector(
                                        TargetSelector.ALL_PLAYERS,
                                        SelectorArgumentsBuilder.create()
                                                .gamemode(GameMode.SPECTATOR, true)
                                                .team()))
                        .build());

        if (OperationMode.teamCreationInGame) {
            // Spread players without a team alone
            fileCommands.add(Execute.In(Dimension.overworld) +
                    SpreadPlayers.create(
                                    Constant.spawnCenter,
                                    75,
                                    150,
                                    false,
                                    Entity.ofSelector(
                                            TargetSelector.ALL_PLAYERS,
                                            SelectorArgumentsBuilder.create()
                                                    .gamemode(GameMode.SPECTATOR, true)
                                                    .team()))
                            .build());
        }

        return new FileData(FileName.death_match, fileCommands);
    }

    private FileData ControlPoint(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Current Control Point
        ControlPoint currentCP = controlPoints.get(i - 1);

        // TODO: Change for players without a team
        if (OperationMode.teamCreationInGame) {
            // Update CP glass color solo
            /*
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext("@r[limit=1,gamemode=!spectator,team=]", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.GREATER, Constant.adminOld, getObjectiveByName(Objective.Highscore.extendName(i)), true) +
                    CommandBuilder.setBlock(currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY() + 1, currentCP.getCoordinate().getZ(), Block.STAINED_GLASS.extendColor("white"), SetBlockType.replace));*/
        }

        // Check which teams are on the Control Point
        for (Team team : teams) {
            fileCommands.add(scoreboard.Set(team.getName(), Objective.OnCP.extendName(i), 0));
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@a[gamemode=!spectator,team=" + team.getName() + ",x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                    scoreboard.Add(team.getName(), Objective.OnCP.extendName(i), 1));
        }

        // Check how many teams are on the Control Point
        fileCommands.add(scoreboard.Set("TotalTeamsOnCP" + i, Objective.OnCP.extendName(i), 0));
        for (Team team : teams) {
            fileCommands.add(Execute.If(team.getName(), Objective.OnCP.extendName(i), "1..") +
                    scoreboard.Add("TotalTeamsOnCP" + i, Objective.OnCP.extendName(i), 1));
        }
        fileCommands.add(scoreboard.Set("ContestedCP" + i, Objective.OnCP.extendName(i), 0));
        fileCommands.add(Execute.If("TotalTeamsOnCP" + i, Objective.OnCP.extendName(i), "2..") +
                scoreboard.Set("ContestedCP" + i, Objective.OnCP.extendName(i), 1));

        // Give teams score
        fileCommands.add(Schedule.callFunction(FileName.control_point_score_ + "" + i));

        // Send CP messages
        fileCommands.add(Schedule.callFunction(FileName.control_point_messages_ + "" + i));

        // Update CP visuals
        fileCommands.add(Schedule.callFunction(FileName.control_point_update_records_ + "" + i));
        fileCommands.add(Schedule.callFunction(FileName.control_point_visuals_ + "" + i));

        // Update CP state
        for (Team team : teams) {
            // Update state
            fileCommands.add(scoreboard.Operation(team.getName(), Objective.PrevCP.extendName(i), ComparatorType.EQUAL, team.getName(), Objective.OnCP.extendName(i)));
        }

        // TODO: Behavior for solo players

        return new FileData("" + FileName.control_point_ + i, fileCommands);
    }

    private FileData ControlPointScore(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ControlPoint currentCP = controlPoints.get(i - 1);

        // TODO: Fix for single players
        if (OperationMode.teamCreationInGame) {
            // Give single players on the Control Point score
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@a", true) +
                    scoreboard.Add("@s", getObjectiveByName(Objective.ControlPoint.extendName(i)), currentCP.getAddRate()));
        }

        // Give teams CP score
        for (Team team : teams) {
            fileCommands.add(Execute.If(team.getName(), Objective.OnCP.extendName(i), "1..", false) +
                    Execute.IfNext("ContestedCP" + i, Objective.OnCP.extendName(i), 0, true) +
                    scoreboard.Add(team.getPlayerColor(), Objective.ControlPoint.extendName(i), currentCP.getAddRate()));
        }


        return new FileData("" + FileName.control_point_score_ + i, fileCommands);
    }

    private FileData ControlPointTeamScore() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Update team score
        for (Team team : teams) {
            // Update team display scores
            fileCommands.add(scoreboard.Operation(team.getPlayerColor(), Objective.CPScore, ComparatorType.EQUAL, team.getPlayerColor(), Objective.ControlPoint.extendName(1)));
            fileCommands.add(scoreboard.Operation(team.getPlayerColor(), Objective.CPScore, ComparatorType.ADD, team.getPlayerColor(), Objective.ControlPoint.extendName(2)));

            // Update global highscore
            fileCommands.add(scoreboard.Operation(Constant.adminOld, Objective.CPHighscore, ComparatorType.GREATER, team.getPlayerColor(), Objective.CPScore));
        }

        return new FileData(FileName.control_point_team_score, fileCommands);
    }

    private FileData ControlPointMessages(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Current Control Point
        ControlPoint currentCP = controlPoints.get(i - 1);

        for (Team team : teams) {
            // Announce attacking
            texts.clear();
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, " IS ATTACKING CONTROL POINT " + i + "!"));
            fileCommands.add(Execute.If(team.getName(), Objective.OnCP.extendName(i), "1..", false) +
                    Execute.IfNext(team.getName(), Objective.PrevCP.extendName(i), 0, true) +
                    new TellRaw("@a", texts).sendRaw());

            // Announce abandoning
            texts.clear();
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, " HAS ABANDONED CONTROL POINT " + i + "!"));
            fileCommands.add(Execute.If(team.getName(), Objective.OnCP.extendName(i), 0, false) +
                    Execute.IfNext(team.getName(), Objective.PrevCP.extendName(i), "1..", true) +
                    new TellRaw("@a", texts).sendRaw());
        }

        if (OperationMode.teamCreationInGame) {
            // Players without a team
            // Count players on CP
            fileCommands.add(scoreboard.Set("Solo", Objective.OnCP.extendName(i), 0));
            fileCommands.add(Execute.In(Dimension.overworld, false) +
                    Execute.AsNext("@a[gamemode=!spectator,team=,x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                    scoreboard.Add("Solo", Objective.OnCP.extendName(i), 1));

            // Announce attacking
            texts.clear();
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, "A "));
            texts.add(new Text(TextColor.WHITE, false, false, "SOLO"));
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, " IS ATTACKING CONTROL POINT " + i + "!"));
            fileCommands.add(Execute.If("Solo", Objective.OnCP.extendName(i), "1..", false) +
                    Execute.IfNext("Solo", Objective.PrevCP.extendName(i), 0, true) +
                    new TellRaw("@a", texts).sendRaw());

            // Announce abandoning
            texts.clear();
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, "A "));
            texts.add(new Text(TextColor.WHITE, false, false, "SOLO"));
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, " HAS ABANDONED CONTROL POINT " + i + "!"));
            fileCommands.add(Execute.If("Solo", Objective.OnCP.extendName(i), 0, false) +
                    Execute.IfNext("Solo", Objective.PrevCP.extendName(i), "1..", true) +
                    new TellRaw("@a", texts).sendRaw());

            // Update state
            fileCommands.add(scoreboard.Operation("Solo", Objective.PrevCP.extendName(i), ComparatorType.EQUAL, "Solo", Objective.OnCP.extendName(i)));
        }

        return new FileData("" + FileName.control_point_messages_ + i, fileCommands);
    }

    private FileData DropCarepackages() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        Boolean debug = false;

        // Change title display time
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .displayTimes(
                        VariableGameTime.second(1),
                        VariableGameTime.second(5),
                        VariableGameTime.second(2))
                .build());

        // Display spread size
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("To be found at ±" + carePackageSpread + " blocks", TextColor.LIGHT_PURPLE, false, false, false))
                .build());

        // Announce Care Packages
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex(carePackageAmount + " Care Packages!", TextColor.GOLD, true, true, false))
                .build());

        // Change title display time
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .reset()
                .build());

        // Summon Care Package entities
        for (int i = 0; i < carePackageAmount; i++) {
            fileCommands.add(Execute.In(Dimension.overworld) +
                    Summon.create(EntityType.FALLING_BLOCK)
                            .pos(Vec3.absolute(0, 300, 0))
                            .nbt(
                                    FallingBlockNbtBuilder.create(
                                            FallingBlockData.create(
                                                    ItemId.CHEST,
                                                    LootTableId.SUPPLY_DROP,
                                                    "Care Package",
                                                    1,
                                                    false,
                                                    new StaticEntityTag[]{StaticEntityTag.CARE_PACKAGE}
                                            )
                                    ).buildNbt()
                            )
            );
        }

        // Spread Care Packages
        fileCommands.add(Execute.In(Dimension.overworld, true) +
                SpreadPlayers.create(
                                Constant.spawnCenter,
                                10,
                                carePackageSpread,
                                false,
                                Entity.ofSelector(
                                        TargetSelector.ALL_ENTITIES,
                                        SelectorArgumentsBuilder.create()
                                                .type(EntityType.FALLING_BLOCK)
                                                .nbt(FallingBlockNbtBuilder.create(
                                                                FallingBlockData.create(
                                                                        new StaticEntityTag[]{StaticEntityTag.CARE_PACKAGE}))
                                                        .buildNbt())))
                        .build());

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                .name(StaticEntityTag.CARE_PACKAGES_DROPPED)
                .build());

        return new FileData(FileName.drop_carepackages, fileCommands);
    }

    private FileData TraitorHandout() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Check if player can become traitor
        if (traitorWaitTime > 0) {
            ArrayList<Season> seasons = new ArrayList<>(this.seasons);
            seasons.sort(Comparator.comparing(Season::getID));
            for (Player p : players) {
                if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
                    // Exclude players who cannot become traitor
                    fileCommands.add(Tag.action(Entity.ofName(p.getPlayerName()), TagAction.ADD)
                                    .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                                            .build());
                }
            }
        }

        // Assign first traitor
        fileCommands.add(Tag.action(Entity.ofSelector(
                TargetSelector.RANDOM_PLAYER,
                SelectorArgumentsBuilder.create()
                        .limit(1)
                        .tag(StaticEntityTag.DONT_MAKE_TRAITOR, true)
                        .scores(Map.of(ScoreObjective.RANK, minTraitorRank + ".."))
                        .gamemode(GameMode.SPECTATOR, true)),
                TagAction.ADD)
                        .name(StaticEntityTag.TRAITOR)
                        .build());

        // Make traitor teammates ineligible for becoming traitor
        for (Team t : teams) {
            fileCommands.add(Execute.If("@p[tag=" + TagTemp.Traitor + ",team=" + t.getName() + "]") +
                    Tag.action(Entity.ofSelector(
                                            TargetSelector.ALL_PLAYERS,
                                            SelectorArgumentsBuilder.create()
                                                    .team(t.getName())),
                                    TagAction.ADD)
                            .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                            .build());
        }

        // Make solo traitors ineligible to become traitor again
        fileCommands.add(Tag.action(Entity.ofSelector(
                TargetSelector.ALL_PLAYERS,
                SelectorArgumentsBuilder.create()
                        .tag(StaticEntityTag.TRAITOR)
                        .team()),
                TagAction.ADD)
                        .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                        .build());

        // Assign second traitor
        fileCommands.add(Tag.action(Entity.ofSelector(
                TargetSelector.RANDOM_PLAYER,
                SelectorArgumentsBuilder.create()
                        .limit(1)
                        .tag(StaticEntityTag.DONT_MAKE_TRAITOR, true)
                        .scores(Map.of(ScoreObjective.RANK, minTraitorRank + ".."))
                        .gamemode(GameMode.SPECTATOR, true)),
                TagAction.ADD)
                        .name(StaticEntityTag.TRAITOR)
                        .build());

        // Add additional traitor
        if (traitorMode == 2) {
            fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.ALL_PLAYERS), TagAction.REMOVE)
                    .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                    .build());
            if (traitorWaitTime > 0) {
                for (Player p : players) {
                    if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
                        fileCommands.add(Tag.action(Entity.ofName(p.getPlayerName()), TagAction.ADD)
                                        .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                                                .build());
                    }
                }
            }
            fileCommands.add(Tag.action(Entity.ofSelector(
                                    TargetSelector.ALL_PLAYERS,
                                    SelectorArgumentsBuilder.create()
                                            .tag(StaticEntityTag.TRAITOR)),
                            TagAction.ADD)
                    .name(StaticEntityTag.DONT_MAKE_TRAITOR)
                    .build());
            fileCommands.add(Tag.action(Entity.ofSelector(
                    TargetSelector.RANDOM_PLAYER,
                    SelectorArgumentsBuilder.create()
                            .limit(1)
                            .tag(StaticEntityTag.DONT_MAKE_TRAITOR, true)
                            .gamemode(GameMode.SPECTATOR, true)),
                    TagAction.ADD)
                            .name(StaticEntityTag.TRAITOR)
                            .build());
        }

        // Inform traitors
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(TextColor.RED, false, true, "You feel like betrayal today. You have become a Traitor. Your faction consists of: "));
        texts.add(new Select(false, true, "@a[tag=" + TagTemp.Traitor + "]"));
        texts.add(new Text(TextColor.RED, false, true, "."));
        fileCommands.add(Execute.As("@a[tag=" + TagTemp.Traitor + "]") +
                new TellRaw("@s", texts).sendRaw());

        // Announce Traitor Faction
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex("A Traitor Faction", TextColor.RED, true, false, false))
                .build());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("has been founded!", TextColor.DARK_RED, true, false, false))
                .build());

        // Enable timers
        fileCommands.add(Schedule.callFunction(FileName.timer_traitor_5));
        fileCommands.add(Schedule.callFunction(FileName.timer_traitor_20));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                        .name(StaticEntityTag.TRAITORS_ASSIGNED)
                                .build());

        return new FileData(FileName.traitor_handout, fileCommands);
    }

    private FileData TraitorActionBar() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Show all traitors in actionbar
        fileCommands.add(Execute.As("@a[tag=" + TagTemp.Traitor + "]") +
                Title.create(Entity.ofSelector(TargetSelector.SENDER))
                        .actionbar(TextComponent.array(List.of(
                                TextComponent.complex(">>> ", TextColor.GOLD),
                                TextComponent.complex("Traitor Faction: ", TextColor.LIGHT_PURPLE),
                                TextComponent.selector(Entity.ofSelector(
                                        TargetSelector.ALL_PLAYERS,
                                        SelectorArgumentsBuilder.create()
                                                .tag(StaticEntityTag.TRAITOR))),
                                TextComponent.complex(" <<<", TextColor.GOLD))))
                        .build());

        return new FileData(FileName.traitor_actionbar, fileCommands);
    }

    private FileData SpawnControlPoints() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (ControlPoint cp : cpList) {
            Coordinate c = cp.getCoordinate();
            fileCommands.add(Execute.In(c.getDimension()) +
                    ForceLoad.create(ForceLoadAction.ADD)
                            .from(ColumnPos.absolute(c.getX(), c.getZ()))
                            .build());

            fileCommands.add(Execute.In(c.getDimension()) +
                    SetBlock.create(
                                    BlockPos.absolute(c.getX(), c.getY() + 11, c.getZ()),
                                    DynamicBlock.create(
                                            StaticBlockId.STRUCTURE_BLOCK,
                                            BlockState.create()
                                                    .mode(StructureBlockMode.LOAD),
                                            StructureBlockEntity.create()
                                                    .setString(StructureBlockEntity.StructureDataKey.METADATA, "")
                                                    .setMirror(StructureMirror.NONE)
                                                    .setByte(StructureDataKey.IGNORE_ENTITIES, (byte) 1)
                                                    .setByte(StructureDataKey.POWERED, (byte) 0)
                                                    .setLong(StructureDataKey.SEED, 0L)
                                                    .setString(StructureDataKey.AUTHOR, "?")
                                                    .setRotation(StructureRotation.NONE)
                                                    .setInt(StructureDataKey.POS_X, -6)
                                                    .setMode(StructureBlockMode.LOAD)
                                                    .setInt(StructureDataKey.POS_Y, -13)
                                                    .setInt(StructureDataKey.SIZE_X, 13)
                                                    .setInt(StructureDataKey.POS_Z, -6)
                                                    .setFloat(StructureDataKey.INTEGRITY, 1.0f)
                                                    .setString(StructureDataKey.NAME, cp.getStructureName())
                                                    .setInt(StructureDataKey.SIZE_Y, 14)
                                                    .setInt(StructureDataKey.SIZE_Z, 13)
                                                    .setByte(StructureDataKey.SHOW_BOUNDING_BOX, (byte) 1)))
                            .build());

            // Activate structure block
            fileCommands.add(Execute.In(c.getDimension()) +
                    SetBlock.create(
                                    BlockPos.absolute(c.getX(), c.getY() + 10, c.getZ()),
                                    SimpleBlock.create(StaticBlockId.REDSTONE_BLOCK))
                            .mode(SetMode.DESTROY)
                            .build());

            // Replace blocks that do not emit light
            fileCommands.add(Execute.In(c.getDimension()) +
                    Fill.create(
                                    BlockPos.absolute(c.getX(), c.getY() + 12, c.getZ()),
                                    BlockPos.absolute(c.getX(), Constant.worldHeight - 1, c.getZ()),
                                    SimpleBlock.create(StaticBlockId.GLASS))
                            .filter(SimpleBlockPredicate.create(BlockTagId.BLOCK_BEACON_LIGHT))
                            .build());

            fileCommands.add(Execute.In(c.getDimension()) +
                    ForceLoad.create(ForceLoadAction.REMOVE)
                            .from(ColumnPos.absolute(c.getX(), c.getZ()))
                            .build());
        }

        // Remove leftover music discs from legacy Control Point
        fileCommands.add(Kill.create()
                .targets(Entity.ofSelector(
                        TargetSelector.ALL_ENTITIES,
                        SelectorArgumentsBuilder.create()
                                .type(EntityType.ITEM)
                                .nbt(ItemNbtBuilder.create(
                                                        ItemData.create(
                                                                ItemId.MUSIC_DISC_STAL,
                                                                1))
                                                .buildNbt())))
                .build());

        return new FileData(FileName.spawn_control_points, fileCommands);
    }

    private FileData DisplayRank() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Player p : players) {
            fileCommands.add(scoreboard.Set(p.getPlayerName(), getObjectiveByName(Objective.Rank), p.getRank()));
        }
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.sidebar, Objective.Rank));

        return new FileData(FileName.display_rank, fileCommands);
    }

    private FileData HorseFrostWalker() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Execute.At("@a[nbt={RootVehicle:{Entity:{id:\"" + EntityType.HORSE + "\"}}}]") +
                Fill.create(
                                BlockPos.relative(-2, -2, -2),
                                BlockPos.relative(2, 0, 2),
                                SimpleBlock.create(StaticBlockId.ICE))
                        .filter(SimpleBlockPredicate.create(StaticBlockId.WATER))
                        .build());

        return new FileData(FileName.horse_frost_walker, fileCommands);
    }

    private FileData UpdateSidebar() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(scoreboard.Add(Constant.adminOld, getObjectiveByName(Objective.SideDum), 1));
        int i = 0;
        for (ScoreboardObjective s : scoreboardObjectives) {
            if (s.getDisplaySideBar()) {
                i++;
                fileCommands.add(Execute.If("@e[scores={SideDum=" + (10 * Constant.tickFrequencyLong * i) + "}]") +
                        s.setDisplay(ScoreboardLocation.sidebar));
            }
        }
        fileCommands.add(Execute.If("@e[scores={SideDum=" + (10 * Constant.tickFrequencyLong * i + 1) + "}]") +
                scoreboard.Reset(Constant.adminOld, getObjectiveByName(Objective.SideDum)));


        return new FileData(FileName.update_sidebar, fileCommands);
    }

    private FileData RemoveBannedItems() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Regeneration potions (normal + splash, strong, long)
        Text warning = new Text(TextColor.RED, true, false, "REGENERATION POTIONS ARE NOT ALLOWED, YOU NAUGHTY BUM!");
        ItemTargetEntity target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.SPLASH_POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.REGENERATION.getPotionTag()))))))));
        String targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.REGENERATION.getPotionTag() + "\"}}}}]";
        ItemStack replacement = SimpleItemStack.create(ItemId.GLASS_BOTTLE);
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());
        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.SPLASH_POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.REGENERATION.getPotionTag(true, false)))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.REGENERATION.getPotionTag(true, false) + "\"}}}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());
        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.SPLASH_POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.REGENERATION.getPotionTag(false, true)))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.REGENERATION.getPotionTag(false, true) + "\"}}}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());

        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.REGENERATION.getPotionTag()))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.REGENERATION.getPotionTag() + "\"}}}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());
        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.REGENERATION.getPotionTag(true, false)))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.REGENERATION.getPotionTag(true, false) + "\"}}}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());
        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.REGENERATION.getPotionTag(false, true)))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.REGENERATION.getPotionTag(false, true) + "\"}}}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());

        // Strength II potions
        warning.setText("STRENGTH II POTIONS ARE NOT ALLOWED, YOU NAUGHTY BUM!");
        replacement = DynamicItemStack.create(
                ItemId.SPLASH_POTION.getResourceLocation(),
                CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                        .put(new StringTag(
                                        PotionContentsKey.POTION.toString(),
                                EffectId.STRENGTH.getPotionTag(false, false))));

        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.SPLASH_POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.STRENGTH.getPotionTag(true, false)))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.STRENGTH.getPotionTag(true, false) + "\"}}}}]";

        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());

        replacement = DynamicItemStack.create(
                ItemId.POTION.getResourceLocation(),
                CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                        .put(new StringTag(
                                PotionContentsKey.POTION.toString(),
                                EffectId.STRENGTH.getPotionTag(false, false))));

        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.POTION.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))
                                        .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                .put(CompoundTag.create(ComponentsKey.POTION_CONTENTS.toString())
                                                        .put(new StringTag(
                                                                PotionContentsKey.POTION.toString(),
                                                                EffectId.STRENGTH.getPotionTag(true, false)))))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + EffectId.STRENGTH.getPotionTag(true, false) + "\"}}}}]";

        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(replacement)
                .build());

        for (int ii = 0; ii < 5; ii++) {
            // Piercing enchantment
            warning.setText("PIERCING IS NOT ALLOWED, YOU NAUGHTY BUM!");
            target = ItemTargetEntity.create(Entity.ofSelector(
                    TargetSelector.NEAREST_PLAYER,
                    SelectorArgumentsBuilder.create()
                            .nbt(CompoundTag.create()
                                    .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                            .put(new StringTag(
                                                    SelectedItemKey.ID.toString(),
                                                    ItemId.CROSSBOW.getResourceLocation()))
                                            .put(new IntTag(
                                                    SelectedItemKey.COUNT.toString(),
                                                    1))
                                            .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                    .put(CompoundTag.create(ComponentsKey.ENCHANTMENTS.toString())
                                                            .put(new IntTag(
                                                                    EnchantmentType.PIERCING.toString(),
                                                                    ii + 1))))))));
            targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.CROSSBOW + "\",count:1,components:{\"minecraft:enchantments\":{\"" + EnchantmentType.PIERCING + "\":" + (ii + 1) + "}}}}]";
            fileCommands.add(Execute.If(targetOld) +
                    new TellRaw(targetOld, warning).sendRaw());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            target)
                    .slot(ItemSlot.MAINHAND)
                    .replaceWith(SimpleItemStack.create(ItemId.CROSSBOW))
                    .build());

            // Power enchantment
            warning.setText("POWER IS NOT ALLOWED, YOU NAUGHTY BUM!");
            target = ItemTargetEntity.create(Entity.ofSelector(
                    TargetSelector.NEAREST_PLAYER,
                    SelectorArgumentsBuilder.create()
                            .nbt(CompoundTag.create()
                                    .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                            .put(new StringTag(
                                                    SelectedItemKey.ID.toString(),
                                                    ItemId.BOW.getResourceLocation()))
                                            .put(new IntTag(
                                                    SelectedItemKey.COUNT.toString(),
                                                    1))
                                            .put(CompoundTag.create(SelectedItemKey.COMPONENTS.toString())
                                                    .put(CompoundTag.create(ComponentsKey.ENCHANTMENTS.toString())
                                                            .put(new IntTag(
                                                                    EnchantmentType.POWER.toString(),
                                                                    ii + 1))))))));
            targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.BOW + "\",count:1,components:{\"minecraft:enchantments\":{\"" + EnchantmentType.POWER + "\":" + (ii + 1) + "}}}}]";
            fileCommands.add(Execute.If(targetOld) +
                    new TellRaw(targetOld, warning).sendRaw());
            fileCommands.add(Item.create(
                            ItemAction.REPLACE_WITH,
                            target)
                    .slot(ItemSlot.MAINHAND)
                    .replaceWith(SimpleItemStack.create(ItemId.BOW))
                    .build());
        }
        // Wolf armor
        warning.setText("WOLF ARMOR IS NOT ALLOWED, YOU NAUGHTY BUM!");
        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.WOLF_ARMOR.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.WOLF_ARMOR + "\",count:1}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(SimpleItemStack.create(ItemId.LEATHER_HORSE_ARMOR))
                .build());

        // Suspicious stew
        warning.setText("SUSPICIOUS STEW IS NOT ALLOWED, YOU NAUGHTY BUM!");
        target = ItemTargetEntity.create(Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .nbt(CompoundTag.create()
                                .put(CompoundTag.create(ItemNbtKey.SELECTED_ITEM.toString())
                                        .put(new StringTag(
                                                SelectedItemKey.ID.toString(),
                                                ItemId.SUSPICIOUS_STEW.getResourceLocation()))
                                        .put(new IntTag(
                                                SelectedItemKey.COUNT.toString(),
                                                1))))));
        targetOld = "@p[nbt={SelectedItem:{id:\"" + ItemId.SUSPICIOUS_STEW + "\",count:1}}]";
        fileCommands.add(Execute.If(targetOld) +
                new TellRaw(targetOld, warning).sendRaw());
        fileCommands.add(Item.create(
                        ItemAction.REPLACE_WITH,
                        target)
                .slot(ItemSlot.MAINHAND)
                .replaceWith(SimpleItemStack.create(ItemId.BOWL))
                .build());

        return new FileData(FileName.remove_banned_items, fileCommands);
    }

    /* Control Point perks */
    // Check if perk can be handed out
    private FileData ControlPointPerksCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team team : teams) {
            int i = 0;
            for (Perk perk : perks) {
                i++;
                fileCommands.add(Execute.If(team.getPlayerColor(), Objective.CPScore, perk.getActivationTime() + "..", false) +
                        Execute.IfNext("@p[gamemode=!spectator,team=" + team.getName() + ",scores={ReceivedPerk=.." + (i - 1) + "}]") +
                        Execute.AsNext("@p[gamemode=!spectator,team=" + team.getName() + "]", true) +
                        Schedule.callFunction("" + FileName.perk_ + i));
            }
        }

        return new FileData(FileName.control_point_perks_check, fileCommands);
    }

    // Hand out perks
    private FileData ControlPointPerks(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team team : teams) {
            // Create text to be displayed
            ArrayList<TextItem> texts = new ArrayList<>();
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(TextColor.LIGHT_PURPLE, false, false, " HAS REACHED"));
            texts.add(new Text(TextColor.GOLD, false, false, " PERK " + perks.get(i).getId() + "!"));

            // Display text
            fileCommands.add(Execute.If("@s[team=" + team.getName() + "]") +
                    new TellRaw("@a", texts).sendRaw());

            // Add tag
            fileCommands.add(Execute.If("@s[team=" + team.getName() + "]") +
                    scoreboard.Set("@a[team=" + team.getName() + "]", Objective.ReceivedPerk, perks.get(i).getId()));

            // Give rewards
            fileCommands.add(Execute.If("@s[team=" + team.getName() + "]") +
                    perks.get(i).getReward("@a[team=" + team.getName() + "]"));
        }

        // Play sound
        fileCommands.add(PlaySound.create(perks.get(i).getSound())
                .source(SoundSource.MASTER)
                .targets(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .pos(Vec3.relative(0, 50, 0))
                .volume(100)
                .build()
        );

        return new FileData("" + FileName.perk_ + (i + 1), fileCommands);
    }

    // Display quotes during the match
    private FileData DisplayQuotes() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Roll a random number to pick a quote
        fileCommands.add(Execute.Store(ExecuteStore.result, Constant.adminOld, Objective.RandomQuotes) +
                Random.create(RandomAction.VALUE)
                        .range(0, quotes.size() - 1)
                                .build());

        // Pick a quote from the listAdd commentMore actions
        for (int i = 0; i < quotes.size(); i++) {
            fileCommands.add(Execute.If("@e[scores={RandomQuotes=" + i + "}]") +
                    new TellRaw("@a", new Text(TextColor.WHITE, false, false, quotes.get(i))).sendRaw());

        }

        // Reschedule displaying a new quote
        fileCommands.add(Schedule.callFunction(FileName.display_quotes, 7 * Constant.secPerMinute));

        return new FileData(FileName.display_quotes, fileCommands);
    }

    // Update amount stripmined
    private FileData UpdateMineCount() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(scoreboard.Set("@s", Objective.Mining, 0));

        ArrayList<String> blocks = new ArrayList<>();
        blocks.add("Stone");
        blocks.add("Deepslate");
        blocks.add("Diorite");
        blocks.add("Andesite");
        blocks.add("Granite");

        for (String block : blocks) {
            fileCommands.add(scoreboard.Operation("@s", getObjectiveByName(Objective.Mining), ComparatorType.ADD, "@s", getObjectiveByName(block)));
        }

        return new FileData(FileName.update_mine_count, fileCommands);
    }

    // Update minimum health
    private FileData UpdateMinHealth() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Find player with lowest health
        fileCommands.add(scoreboard.Operation(Constant.adminOld, Objective.MinHealth, ComparatorType.LESS, "@a[gamemode=!spectator]", Objective.Hearts));

        return new FileData(FileName.update_min_health, fileCommands);
    }

    // Reset health of respawned players
    private FileData RespawnPlayer() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Define player that needs to be respawned
        String respawnPlayerOld = "@a[tag=" + TagTemp.Respawn + "]";
        Entity respawnPlayer = Entity.ofSelector(
                TargetSelector.ALL_PLAYERS,
                SelectorArgumentsBuilder.create()
                        .tag(StaticEntityTag.RESPAWN));

        // Teleport player to their team
        for (Team t : teams) {
            fileCommands.add(Execute.As(respawnPlayerOld) +
                    Teleport.create()
                            .targets(Entity.ofSelector(
                                    TargetSelector.SENDER,
                                    SelectorArgumentsBuilder.create()
                                            .team(t.getName())))
                            .destination(Entity.ofSelector(
                                    TargetSelector.RANDOM_PLAYER,
                                    SelectorArgumentsBuilder.create()
                                            .gamemode(GameMode.SPECTATOR, true)
                                            .team(t.getName())))
                            .build());

            fileCommands.add(Execute.At(respawnPlayerOld, false) +
                    Execute.AsNext(respawnPlayerOld) +
                    Execute.UnlessNext("@p[team=" + t.getName() + ",tag=!Respawn]", true) +
                    SpreadPlayers.create(
                                    Constant.spawnCenter,
                                    0.3f * world.getSize(),
                                    0.7f * world.getSize(),
                                    false,
                                    Entity.ofSelector(
                                            TargetSelector.SENDER,
                                            SelectorArgumentsBuilder.create()
                                                    .team(t.getName())))
                            .build());
        }

        if (OperationMode.teamCreationInGame) {
            // Teleport player if they are not in a team
            fileCommands.add(Execute.As(respawnPlayerOld) +
                    SpreadPlayers.create(
                                    Constant.spawnCenter,
                                    0.3f * world.getSize(),
                                    0.7f * world.getSize(),
                                    false,
                                    Entity.ofSelector(
                                            TargetSelector.SENDER,
                                            SelectorArgumentsBuilder.create()
                                                    .team()))
                            .build());

            // Team caller
            fileCommands.add(Execute.As(respawnPlayerOld) +
                    Give.create(Entity.ofSelector(
                                            TargetSelector.SENDER,
                                            SelectorArgumentsBuilder.create()
                                                    .team()),
                                    GoatHornItemStack.create(
                                            InstrumentComponent.create(GoatHornInstrumentId.PONDER_GOAT_HORN),
                                            UseCooldownComponent.create(30),
                                            EnchantmentsComponent.create(
                                                    Map.of(EnchantmentId.VANISHING_CURSE, 1))))
                            .build());
        }

        // Remove player heads
        fileCommands.add(Execute.As("@a[nbt={Inventory:[{id:\"" + ItemId.PLAYER_HEAD + "\"}]}]") +
                Clear.create()
                        .targets(Entity.ofSelector(TargetSelector.SENDER))
                        .item(SimpleItemPredicate.create(ItemId.PLAYER_HEAD))
                        .build());  // Remove from inventory
        fileCommands.add(Execute.As("@e[type=" + EntityType.ITEM + ",nbt={Item:{id:\"" + ItemId.PLAYER_HEAD + "\"}}]") +
                Kill.create().targets(Entity.ofSelector(TargetSelector.SENDER)).build());   // Remove item

        // Teammate tracker
        for (Team team : teams) {
            fileCommands.add(Execute.As(respawnPlayerOld) +
                    Give.create(Entity.ofSelector(
                                            TargetSelector.SENDER,
                                            SelectorArgumentsBuilder.create()
                                                    .team(team.getName())),
                                    BundleItemStack.create(
                                            team.getDyeColor(),
                                            EnchantmentsComponent.create(Map.of(EnchantmentId.VANISHING_CURSE, 1)),
                                            CustomDataComponent.create(CompoundTag.create()
                                                    .put(new ByteTag("locateTeammate", (byte) 1)))))
                            .build());
        }

        // Set respawn health
        for (int i = 0; i < 10; i++) {
            int indexFront = 2 * i + 1;
            int indexRear = 2 * (i + 1);

            fileCommands.add(Execute.As(respawnPlayerOld, false) +
                    Execute.IfNext("@e[scores={MinHealth=" + indexFront + ".." + indexRear + "}]", true) +
                    Attribute.create(
                                    Entity.ofSelector(TargetSelector.SENDER),
                                    AttributeId.MAX_HEALTH)
                            .setBase(i + 1));
        }
        fileCommands.add(Effect.create(EffectAction.GIVE)
                .targets(respawnPlayer)
                .effect(EffectId.HEALTH_BOOST)
                .seconds(1)
                .amplifier(0)
                .build());
        fileCommands.add(Effect.create(EffectAction.CLEAR)
                .targets(respawnPlayer)
                .effect(EffectId.HEALTH_BOOST)
                .build());
        fileCommands.add(Execute.As(respawnPlayerOld) +
                Attribute.create(
                                Entity.ofSelector(TargetSelector.SENDER),
                                AttributeId.MAX_HEALTH)
                        .setBase(20));

        // Set player's gamemode to survival
        fileCommands.add(Execute.As(respawnPlayerOld) +
                SetGameMode.create(GameMode.SURVIVAL)
                        .target(Entity.ofSelector(TargetSelector.SENDER))
                        .build()
        );

        // Remove respawn tag
        fileCommands.add(Tag.action(respawnPlayer, TagAction.REMOVE)
                        .name(StaticEntityTag.RESPAWN)
                        .build());

        return new FileData(FileName.respawn_player, fileCommands);
    }

    private FileData ControlPointCaptured() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Announce that Control Point has been captured
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, "THE CONTROL POINT HAS BEEN CAPTURED!"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        texts.clear();
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .subtitle(TextComponent.complex("has been captured!", TextColor.LIGHT_PURPLE, true, true, false))
                .build());
        fileCommands.add(Title.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS))
                .title(TextComponent.complex("The Control Point", TextColor.GOLD, true, true, false))
                .build());

        // Check which team has captured the Control Point
        fileCommands.add(Schedule.callFunction(FileName.teams_highscore_alive_check));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                        .name(StaticEntityTag.CONTROL_POINT_CAPTURED)
                                .build());

        return new FileData(FileName.control_point_captured, fileCommands);
    }

    private FileData TraitorCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // When no traitors remain start teams_alive_check
        fileCommands.add(Execute.Unless("@a[limit=1,tag=" + TagTemp.Traitor + ",gamemode=!spectator]") +
                Schedule.callFunction(FileName.teams_alive_check));

        // When no non-traitors remain, traitors have won
        fileCommands.add(Execute.Unless("@a[limit=1,tag=!" + TagTemp.Traitor + ",gamemode=!spectator]") +
                Schedule.callFunction(FileName.victory_message_traitor));

        return new FileData(FileName.traitor_check, fileCommands);
    }

    private FileData TeamsAliveCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (int i = 0; i < teams.size(); i++) {
            fileCommands.add(Execute.Unless("@a[limit=1,team=!" + teams.get(i).getName() + ",gamemode=!spectator]") +
                    Schedule.callFunction("" + FileName.victory_message_ + i));
        }

        if (OperationMode.teamCreationInGame) {
            // Players without a team
            fileCommands.add(Tag.action(Entity.ofSelector(
                    TargetSelector.RANDOM_PLAYER,
                    SelectorArgumentsBuilder.create()
                            .team()
                            .gamemode(GameMode.SPECTATOR, true)),
                    TagAction.ADD)
                            .name(StaticEntityTag.AM_I_WINNING)
                            .build());
            fileCommands.add(Execute.Unless("@p[tag=!AmIWinning,gamemode=!spectator]", false) +
                    Execute.AsNext("@p[tag=AmIWinning]", true) +
                    Schedule.callFunction(FileName.victory_message_solo));
            fileCommands.add(Tag.action(Entity.ofSelector(
                                    TargetSelector.NEAREST_PLAYER,
                                    SelectorArgumentsBuilder.create()
                                            .tag(StaticEntityTag.AM_I_WINNING)),
                            TagAction.REMOVE)
                    .name(StaticEntityTag.AM_I_WINNING)
                    .build());
        }

        return new FileData(FileName.teams_alive_check, fileCommands);
    }

    private FileData TeamsHighscoreCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            for (int j = 1; j < 3; j++) {
                if (OperationMode.traitorFaction) {
                    // Traitor teams
                    fileCommands.add(Execute.If(Constant.adminOld, Objective.Victory, 1, false) +
                            Execute.IfNext(team.getPlayerColor(), Objective.CPScore, maxCPScore + "..") +
                            Execute.IfNext("@p[team=" + team.getName() + ",gamemode=!spectator,tag=" + TagTemp.Traitor + "]") +
                            Execute.UnlessNext("@p[team=" + team.getName() + ",gamemode=!spectator,tag=!" + TagTemp.Traitor + "]", true) +
                            Schedule.callFunction(FileName.victory_message_traitor));

                    // Regular teams
                    fileCommands.add(Execute.If(Constant.adminOld, Objective.Victory, 1, false) +
                            Execute.IfNext(team.getPlayerColor(), Objective.CPScore, maxCPScore + "..") +
                            Execute.IfNext("@p[team=" + team.getName() + ",gamemode=!spectator,tag=!" + TagTemp.Traitor + "]", true) +
                            Schedule.callFunction("" + FileName.victory_message_ + i));
                } else {

                    // Regular teams
                    fileCommands.add(Execute.If(Constant.adminOld, Objective.Victory, 1, false) +
                            Execute.IfNext(team.getPlayerColor(), Objective.CPScore, maxCPScore + "..", true) +
                            Schedule.callFunction("" + FileName.victory_message_ + i));
                }
            }
        }

        if (OperationMode.teamCreationInGame) {
            // Individual players
            for (int j = 1; j < 3; j++) {
                if (OperationMode.traitorFaction) {
                    // Regular solo
                    fileCommands.add(Execute.If("@e[scores={Victory=1}]", false) +
                            Execute.IfNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=!" + TagTemp.Traitor + "]") +
                            Execute.AsNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=!" + TagTemp.Traitor + "]", true) +
                            Schedule.callFunction(FileName.victory_message_solo));

                    // Traitor solo
                    fileCommands.add(Execute.If("@e[scores={Victory=1}]", false) +
                            Execute.IfNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=" + TagTemp.Traitor + "]", true) +
                            Schedule.callFunction(FileName.victory_message_traitor));
                } else {
                    // Solo
                    fileCommands.add(Execute.If("@e[scores={Victory=1}]", false) +
                            Execute.IfNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..}]") +
                            Execute.AsNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..}]", true) +
                            Schedule.callFunction(FileName.victory_message_solo));
                }
            }
        }

        return new FileData(FileName.teams_highscore_alive_check, fileCommands);
    }

    private FileData ClearSchedule() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Post-game functions
        fileCommands.add(Schedule.clearFunction(FileName.minute_ + "2"));
        fileCommands.add(Schedule.clearFunction(FileName.minute_ + "1"));
        fileCommands.add(Schedule.clearFunction(FileName.death_match));

        // Clear timer schedules
        fileCommands.add(Schedule.clearFunction(FileName.timer_main_1));
        fileCommands.add(Schedule.clearFunction(FileName.timer_main_5));
        fileCommands.add(Schedule.clearFunction(FileName.timer_main_20));
        if (OperationMode.controlPoints) {
            fileCommands.add(Schedule.clearFunction(FileName.timer_control_point_20));
        }
        if (OperationMode.traitorFaction) {
            fileCommands.add(Schedule.clearFunction(FileName.timer_traitor_5));
            fileCommands.add(Schedule.clearFunction(FileName.timer_traitor_20));
        }

        // Extra scheduled functions
        fileCommands.add(Schedule.clearFunction(FileName.display_quotes));
        if (OperationMode.eternalDay) {
            fileCommands.add(Schedule.clearFunction(FileName.messages_eternal_day));
        }
        fileCommands.add(Schedule.clearFunction(FileName.messages_pvp));
        fileCommands.add(Schedule.clearFunction(FileName.disable_respawn));

        return new FileData(FileName.clear_schedule, fileCommands);
    }

    private FileData LocateTeammate() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team t : teams) {
            for (int i = 0; i < 3; i++) {
                fileCommands.add(Execute.As("@a[team=" + t.getName() + ",nbt={SelectedItem:{id:\"minecraft:" + t.getDyeColor() + "_" + "bundle" + "\",components:{\"minecraft:custom_data\":{locateTeammate:1b}}}}]", false) +
                        Execute.AtNext("@s") +
                        Execute.IfNext("@a[team=" + t.getName() + ",distance=0.1..,gamemode=!spectator]") +
                        Execute.FacingNext("@a[team=" + t.getName() + ",distance=0.1..,gamemode=!spectator,limit=1,sort=random]", EntityAnchor.eyes) +
                        Execute.PositionedNext(new Coordinate(0, 1, 0, ReferenceFrame.relative)) +
                        Execute.PositionedNext(new Coordinate(0, 0, i + 1, ReferenceFrame.relative_facing), true) +
                        Particle.create(ParticleArgument.create(
                                        ParticleId.DUST,
                                        ParticleArgumentBuilder.create()
                                                .color(t.getDustColor())
                                                .scale(1)))
                                .pos(Vec3.relative(0, 0, 0))
                                .delta(Vec3.absolute(0, 0, 0))
                                .speed(0.0f)
                                .count(1)
                                .display(DisplayType.NORMAL)
                                .viewers(Entity.ofSelector(TargetSelector.SENDER))
                                .build());
            }
        }

        return new FileData(FileName.locate_teammate, fileCommands);
    }

    private FileData WolfUpdates() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set wolf collar color
        // Get data
        for (int i = 0; i < 2; i++) {
            fileCommands.add(Execute.As("@e[type=" + EntityType.WOLF + "]", false) +
                    Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                    Data.createGet(
                                    DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                    DataPath.createWithIndex(DataPathId.OWNER, i))
                            .build());

            fileCommands.add(Execute.As("@a", false) +
                    Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                    Data.createGet(
                                    DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                    DataPath.createWithIndex(DataPathId.UUID, i))
                            .build());

        }
        // Players in a team
        for (Team t : teams) {
            fileCommands.add(Tag.action(Entity.ofSelector(
                    TargetSelector.ALL_PLAYERS,
                    SelectorArgumentsBuilder.create()
                            .team(t.getName())),
                    TagAction.ADD)
                            .name(StaticEntityTag.COLLAR_CHECK)
                            .build());
            fileCommands.add(Execute.As("@e[type=" + EntityType.WOLF + "]", false) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(0)), ComparatorType.EQUAL, "@p[tag=" + TagTemp.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(0))) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(1)), ComparatorType.EQUAL, "@p[tag=" + TagTemp.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(1)), true) +
                    Data.createModify(
                                    DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                    DataPath.create(DataPathId.COLLAR_COLOR),
                                    ModificationSetValue.create(DataValue.createByte((byte) t.getCollarColor())))
                            .build());
            fileCommands.add(Tag.action(Entity.ofSelector(
                                    TargetSelector.ALL_PLAYERS,
                                    SelectorArgumentsBuilder.create()
                                            .team(t.getName())),
                            TagAction.REMOVE)
                    .name(StaticEntityTag.COLLAR_CHECK)
                    .build());
        }

        if (OperationMode.teamCreationInGame) {
            // Individual players
            fileCommands.add(Tag.action(Entity.ofSelector(
                    TargetSelector.ALL_PLAYERS,
                    SelectorArgumentsBuilder.create()
                            .team()),
                    TagAction.ADD)
                            .name(StaticEntityTag.COLLAR_CHECK)
                            .build());
            fileCommands.add(Execute.As("@e[type=" + EntityType.WOLF + "]", false) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(0)), ComparatorType.EQUAL, "@p[tag=" + TagTemp.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(0))) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(1)), ComparatorType.EQUAL, "@p[tag=" + TagTemp.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(1)), true) +
                    Data.createModify(
                                    DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                    DataPath.create(DataPathId.COLLAR_COLOR),
                                    ModificationSetValue.create(DataValue.createByte((byte) 0)))
                            .build());
            fileCommands.add(Tag.action(Entity.ofSelector(
                                    TargetSelector.ALL_PLAYERS,
                                    SelectorArgumentsBuilder.create()
                                            .team()),
                            TagAction.REMOVE)
                    .name(StaticEntityTag.COLLAR_CHECK)
                    .build());
        }

        // Eliminate baby wolves
        String babyWolf = "@e[type=" + EntityType.WOLF + ",scores={WolfAge=..-1}]";

        fileCommands.add(Execute.As("@e[limit=1,type=" + EntityType.WOLF + ",sort=random]", false) +
                Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.WolfAge), true) +
                Data.createGet(
                                DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                DataPath.create(DataPathId.AGE))
                        .build());
        fileCommands.add(Execute.At(babyWolf) +
                Summon.create(EntityType.DOLPHIN));
        fileCommands.add(Execute.As(babyWolf) +
                Kill.create().targets(Entity.ofSelector(TargetSelector.SENDER)).build());

        // Set tamed wolf base health
        fileCommands.add(Execute.As("@e[type=" + EntityType.WOLF + "]", false) +
                Execute.IfNext(DataClasses.entity, "@s Owner", true) +
                Attribute.create(
                        Entity.ofSelector(TargetSelector.SENDER),
                        AttributeId.MAX_HEALTH)
                        .setBase(20));

        return new FileData(FileName.wolf_updates, fileCommands);
    }

    private FileData DisableRespawn() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Add first blood tag
        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                        .name(StaticEntityTag.RESPAWN_DISABLED)
                                .build());

        // Update immediate respawn
        fileCommands.add(GameRule.create(GameRuleId.DO_IMMEDIATE_RESPAWN)
                .booleanValue(false)
                .build());

        return new FileData(FileName.disable_respawn, fileCommands);
    }

    private FileData JoinTeam() {
        ArrayList<String> fileCommands = new ArrayList<>();

        String lookingPlayer = "@p[tag=LookingForTeamMate]";

        String filledTeam;
        ArrayList<TextItem> texts = new ArrayList<>();
        for (int i = 0; i < (teams.size() - 1); i++) {
            filledTeam = Execute.Unless("@p[team=" + teams.get(i).getName() + "]", false);

            // Announce that players formed a team
            texts.add(new Select(false, false, "@p[limit=2,team=,gamemode=!spectator]"));
            texts.add(new Text(TextColor.WHITE, false, false, " have decided to join forces as team "));
            texts.add(new Text(teams.get(i).getColor(), false, false, teams.get(i).getJSONColor()));
            texts.add(new Text(TextColor.WHITE, false, false, "!"));

            fileCommands.add(filledTeam +
                    Execute.IfNext("@p[tag=LookingForTeamMate,team=]") +
                    Execute.AtNext(lookingPlayer, true) +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();

            // Try to let players join team
            fileCommands.add(filledTeam +
                    Execute.IfNext("@p[tag=LookingForTeamMate,team=]") +
                    Execute.AtNext(lookingPlayer, true) +
                    teams.get(i).joinTeam("@p[limit=2,team=,gamemode=!spectator]"));
        }

        fileCommands.add(Execute.At(lookingPlayer) +
                Clear.create()
                        .targets(Entity.ofSelector(
                                TargetSelector.NEAREST_PLAYER,
                                SelectorArgumentsBuilder.create()
                                        .limit(2)
                                        .gamemode(GameMode.SPECTATOR, true)))
                        .item(SimpleItemPredicate.create(ItemId.GOAT_HORN))
                        .build());


        for (Team team : teams) {
            fileCommands.add(Execute.At(lookingPlayer, false) +
                    Execute.IfNext("@p[tag=LookingForTeamMate,team=" + team.getName() + "]", true) +
                    Give.create(Entity.ofSelector(
                                            TargetSelector.NEAREST_PLAYER,
                                            SelectorArgumentsBuilder.create()
                                                    .limit(2)
                                                    .gamemode(GameMode.SPECTATOR, true)),
                                    BundleItemStack.create(
                                            team.getDyeColor(),
                                            EnchantmentsComponent.create(Map.of(EnchantmentId.VANISHING_CURSE, 1)),
                                            CustomDataComponent.create(CompoundTag.create()
                                                    .put(new ByteTag("locateTeammate", (byte) 1)))))
                            .build());
        }

        return new FileData(FileName.join_team, fileCommands);
    }

    private FileData UpdatePlayerDistance() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        String oldCheckingPlayer = "@p[team=,scores={TimesCalled=1..}]";
        Entity checkingPlayer = Entity.ofSelector(
                TargetSelector.NEAREST_PLAYER,
                SelectorArgumentsBuilder.create()
                        .team()
                        .scores(Map.of(ScoreObjective.TIMES_CALLED, "1..")));
        ComparatorType comparator;

        // Give player playing the horn a tag
        fileCommands.add(Tag.action(checkingPlayer, TagAction.ADD)
                        .name(StaticEntityTag.LOOKING_FOR_TEAM_MATE)
                                .build());

        // Loop through Cartesian coordinates
        for (int i = 0; i < cartesian.length; i++) {
            // Find positions of each player
            fileCommands.add(Execute.As("@a[team=]", false) +
                    Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.Pos + cartesian[i]), true) +
                    Data.createGet(
                                    DataTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)),
                                    DataPath.createWithIndex(DataPathId.POS, i))
                            .build());

            // Subtract distance of nearest player in Cartesian coordinate
            fileCommands.add(Execute.As(oldCheckingPlayer, false) +
                    Execute.AtNext(oldCheckingPlayer, true) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Pos + cartesian[i]), ComparatorType.SUBTRACT, "@p[tag=!LookingForTeamMate,team=,gamemode=!spectator]", getObjectiveByName(Objective.Pos + cartesian[i])));

            // Square the difference in Cartesian coordinates
            fileCommands.add(Execute.As(oldCheckingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Square + cartesian[i]), ComparatorType.EQUAL, "@s", getObjectiveByName(Objective.Pos + cartesian[i])));
            fileCommands.add(Execute.As(oldCheckingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Square + cartesian[i]), ComparatorType.MULTIPLY, "@s", getObjectiveByName(Objective.Pos + cartesian[i])));

            if (i == 0) {
                comparator = ComparatorType.EQUAL;
            } else {
                comparator = ComparatorType.ADD;
            }

            // Calculate distance to nearest player
            fileCommands.add(Execute.As(oldCheckingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Distance), comparator, "@s", getObjectiveByName(Objective.Square + cartesian[i])));
        }

        // Ignore players that are already in a team
        texts.add(new Text(TextColor.RED, true, false, "You are already on a team! Don't be greedy!"));

        String playerInTeam = "@p[scores={TimesCalled=1..},team=!]";
        fileCommands.add(Execute.If(playerInTeam) +
                new TellRaw(playerInTeam, texts).sendRaw());
        texts.clear();

        // Call join team function if other player is in range
        String playerInRange = "@p[tag=LookingForTeamMate,scores={Distance=.." + (minJoinDistance * minJoinDistance) + ",IsKiller=0},team=]";
        fileCommands.add(Execute.If(playerInRange, false) +
                Execute.AtNext(playerInRange) +
                Execute.UnlessNext("@p[tag=!LookingForTeamMate,gamemode=!spectator]", Objective.IsKiller, 1, true) +
                Schedule.callFunction(FileName.join_team));

        // Refuse call if player is too far away
        texts.add(new Text(TextColor.RED, true, false, "You need to be within " + minJoinDistance + " blocks of a player without a team to form a team!"));

        String playerTooFar = "@p[tag=LookingForTeamMate,scores={Distance=" + (minJoinDistance * minJoinDistance) + "..},team=,gamemode=!spectator]";
        fileCommands.add(Execute.If(playerTooFar, false) +
                Execute.UnlessNext(playerTooFar, Objective.IsKiller, 1, true) +
                new TellRaw(playerTooFar, texts).sendRaw());
        texts.clear();

        // Refuse killers
        texts.add(new Text(TextColor.RED, true, false, "You are a killer! No team for you!"));

        String playerKiller = "@p[tag=LookingForTeamMate,scores={IsKiller=1}]";
        fileCommands.add(Execute.If(playerKiller) +
                new TellRaw(playerKiller, texts).sendRaw());
        texts.clear();

        // Warn against killers
        texts.add(new Text(TextColor.RED, true, false, "Watch out! They are a killer!"));

        fileCommands.add(Execute.At(playerInRange, false) +
                Execute.IfNext("@p[tag=!LookingForTeamMate,gamemode=!spectator]", Objective.IsKiller, 1, true) +
                new TellRaw(playerInRange, texts).sendRaw());
        texts.clear();

        // Reset tag and call scoreboard objective
        fileCommands.add(Tag.action(Entity.ofSelector(
                                TargetSelector.NEAREST_PLAYER,
                                SelectorArgumentsBuilder.create()
                                        .scores(Map.of(ScoreObjective.TIMES_CALLED, "1.."))),
                        TagAction.REMOVE)
                .name(StaticEntityTag.LOOKING_FOR_TEAM_MATE)
                .build());
        fileCommands.add(scoreboard.Reset("@p[scores={TimesCalled=1..}]", getObjectiveByName(Objective.TimesCalled)));

        return new FileData(FileName.update_player_distance, fileCommands);
    }

    private FileData CheckIronMan() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Give random player with no damage taken iron man candidate
        fileCommands.add(Tag.action(Entity.ofSelector(
                                TargetSelector.RANDOM_PLAYER,
                                SelectorArgumentsBuilder.create()
                                        .scores(Map.of(ScoreObjective.DAMAGE_TAKEN, ".." + minDamage))),
                        TagAction.ADD)
                .name(StaticEntityTag.IRON_MAN_CANDIDATE)
                .build());

        // Check if there are other potential iron man candidates
        fileCommands.add(Execute.Unless("@a[tag=!IronManCandidate,scores={DamageTaken=.." + minDamage + "}]", false) +
                Execute.AsNext("@p[tag=IronManCandidate]", true) +
                Schedule.callFunction(FileName.announce_iron_man));

        // Remove iron man candidate tag
        fileCommands.add(Tag.action(Entity.ofSelector(
                                TargetSelector.NEAREST_PLAYER,
                                SelectorArgumentsBuilder.create()
                                        .tag(StaticEntityTag.IRON_MAN_CANDIDATE)),
                        TagAction.REMOVE)
                .name(StaticEntityTag.IRON_MAN_CANDIDATE)
                .build());

        return new FileData(FileName.check_iron_man, fileCommands);
    }

    private FileData AnnounceIronMan() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Announce iron man
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Select(false, false, "@s"));
        texts.add(new Text(TextColor.WHITE, false, false, " is S" + uhcNumber + " iron man!"));
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Award the iron man with their crown
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.SENDER), TagAction.ADD)
                .name(StaticEntityTag.IRON_MAN)
                .build());

        return new FileData(FileName.announce_iron_man, fileCommands);
    }

    private FileData DebugGive() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Give player Debug tag
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.SENDER), TagAction.ADD)
                .name(StaticEntityTag.DEBUG)
                .build());

        return new FileData(FileName.debug_give, fileCommands);
    }

    private FileData DebugRemove() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Remove Debug tag from player
        fileCommands.add(Tag.action(Entity.ofSelector(TargetSelector.SENDER), TagAction.REMOVE)
                .name(StaticEntityTag.DEBUG)
                .build());

        return new FileData(FileName.debug_remove, fileCommands);
    }

    private FileData DeveloperPotionControl() {
        // Turn potion effect into function execution
        ArrayList<String> fileCommands = new ArrayList<>();

        EffectId[] effects = {EffectId.SPEED, EffectId.WEAKNESS, EffectId.SLOW_FALLING, EffectId.INVISIBILITY, EffectId.POISON, EffectId.STRENGTH, EffectId.SLOWNESS};
        FileName[] functions = {FileName.developer_mode, FileName.random_teams, FileName.predictions, FileName.into_calls, FileName.spread_players, FileName.survival_mode, FileName.start_game};

        for (int i = 0; i < effects.length; i++) {
            fileCommands.add(Execute.If("@a[gamemode=creative,nbt={active_effects:[{id:\"" + effects[i] + "\"}]}]") +
                    Schedule.callFunction(functions[i]));

            fileCommands.add(Execute.If("@a[nbt={active_effects:[{id:\"" + effects[i] + "\"}]}]") +
                    Effect.create(EffectAction.CLEAR)
                            .targets(Entity.ofSelector(TargetSelector.ALL_ENTITIES))
                            .effect(effects[i])
                            .build());
        }

        return new FileData(FileName.developer_potion_control, fileCommands);
    }

    private FileData ScheduleSingleMessages() {
        // Schedule messages that are only shown once
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Schedule.callFunction(FileName.messages_pvp, 5 * Constant.secPerMinute));
        if (OperationMode.eternalDay) {
            fileCommands.add(Schedule.callFunction(FileName.messages_eternal_day, 20 * Constant.secPerMinute));
        }

        return new FileData(FileName.messages_schedule_single, fileCommands);
    }

    private FileData MessagePVP() {
        // Schedule messages that are only shown once
        ArrayList<String> fileCommands = new ArrayList<>();

        // Message
        fileCommands.add(new TellRaw("@a", new Text(TextColor.GRAY, false, false, "PVP IS NOT ALLOWED UNTIL DAY 2!")).sendRaw());

        return new FileData(FileName.messages_pvp, fileCommands);
    }

    private FileData MessageEternalDay() {
        // Schedule messages that are only shown once
        ArrayList<String> fileCommands = new ArrayList<>();

        // Message
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(TextColor.GOLD, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(TextColor.LIGHT_PURPLE, true, false, "DAY TIME HAS ARRIVED & ETERNAL DAY ENABLED!"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        texts.clear();

        // Set gamerule
        fileCommands.add(GameRule.create(GameRuleId.DO_DAYLIGHT_CYCLE)
                .booleanValue(false)
                .build());

        return new FileData(FileName.messages_eternal_day, fileCommands);
    }

}
